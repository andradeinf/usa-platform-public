package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import org.modelmapper.ModelMapper;

import br.com.usasistemas.usaplatform.bizo.CalendarManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.MailManagementBO;
import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.dao.CalendarEventDAO;
import br.com.usasistemas.usaplatform.dao.CalendarEventHistoryDAO;
import br.com.usasistemas.usaplatform.model.data.CalendarEventData;
import br.com.usasistemas.usaplatform.model.data.CalendarEventHistoryData;
import br.com.usasistemas.usaplatform.model.data.CalendarMonthData;
import br.com.usasistemas.usaplatform.model.data.CalendarMonthDefinitionData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.OperationTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class CalendarManagementBOImpl implements CalendarManagementBO {
	
	private static final Logger log = Logger.getLogger(CalendarManagementBOImpl.class.getName());
	private CalendarEventDAO calendarEventDAO;
	private CalendarEventHistoryDAO calendarEventHistoryDAO;
	private FranchiseeManagementBO franchiseeManagement;
	private MailManagementBO mailManagement;
	
	public CalendarEventDAO getCalendarEventDAO() {
		return calendarEventDAO;
	}

	public void setCalendarEventDAO(CalendarEventDAO calendarEventDAO) {
		this.calendarEventDAO = calendarEventDAO;
	}

	public CalendarEventHistoryDAO getCalendarEventHistoryDAO() {
		return calendarEventHistoryDAO;
	}

	public void setCalendarEventHistoryDAO(CalendarEventHistoryDAO calendarEventHistoryDAO) {
		this.calendarEventHistoryDAO = calendarEventHistoryDAO;
	}

	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public MailManagementBO getMailManagement() {
		return mailManagement;
	}

	public void setMailManagement(MailManagementBO mailManagement) {
		this.mailManagement = mailManagement;
	}

	@Override
	public CalendarMonthDefinitionData getCalendarMonthDefinition(Long monthOffset) {
		
		CalendarMonthDefinitionData response = new CalendarMonthDefinitionData();
		
		CalendarMonthData selectedMonth = new CalendarMonthData();
		response.setSelectedMonth(selectedMonth);
		
		CalendarMonthData previousMonth = new CalendarMonthData();
		response.setPreviousMonth(previousMonth);
		
		CalendarMonthData nextMonth = new CalendarMonthData();
		response.setNextMonth(nextMonth);

		//get current day/month
		Calendar currentMonthCal = DateUtil.getLocalCalendar();
		response.setCurrentDay(Long.valueOf(currentMonthCal.get(Calendar.DAY_OF_MONTH)));
		response.setCurrentMonth(Long.valueOf(currentMonthCal.get(Calendar.MONTH)));
		response.setCurrentYear(Long.valueOf(currentMonthCal.get(Calendar.YEAR)));
		
		//calculate selected month/year using offset
		selectedMonth.setMonth(response.getCurrentMonth() + monthOffset);
		selectedMonth.setYear(Long.valueOf(currentMonthCal.get(Calendar.YEAR)));
		if (selectedMonth.getMonth() < 0) {
			selectedMonth.setYear(selectedMonth.getYear() - 1 + selectedMonth.getMonth() / 12);
			selectedMonth.setMonth(12L + selectedMonth.getMonth() % 12);
		}
		if (selectedMonth.getMonth() > 11) {
			selectedMonth.setYear(selectedMonth.getYear() + selectedMonth.getMonth() / 12);
			selectedMonth.setMonth(selectedMonth.getMonth() % 12);
		}
		
		//Create calendar for selected month
		Calendar selectedMonthCal = DateUtil.getLocalCalendar();
		selectedMonthCal.set(Calendar.DAY_OF_MONTH, 1);
		selectedMonthCal.set(Calendar.MONTH, selectedMonth.getMonth().intValue());
		selectedMonthCal.set(Calendar.YEAR, selectedMonth.getYear().intValue());
		
		//populate selected month based on number of days of th month
		int selectedMonthMaxDay = selectedMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		List<Long> selectedMonthDays = new ArrayList<Long>();
		for (int i=1; i<=selectedMonthMaxDay; i++) {
			selectedMonthDays.add(Long.valueOf(i));
		}
		selectedMonth.setDays(selectedMonthDays);
		
		// get day of the week for first day in the selected month
		int selectedMonthFirstDayWeek = selectedMonthCal.get(Calendar.DAY_OF_WEEK);
		
		//calculate previous month/year based on selected month/year
		previousMonth.setMonth(selectedMonth.getMonth() - 1);
		previousMonth.setYear(selectedMonth.getYear());
		if (previousMonth.getMonth() < 0L) {
			previousMonth.setYear(previousMonth.getYear() - 1);
			previousMonth.setMonth(11L);
		}
		
		// Create previous month calendar and get missing days for beginning of the calendar
		Calendar previousMonthCal = DateUtil.getLocalCalendar();
		previousMonthCal.set(Calendar.DAY_OF_MONTH, 1);
		previousMonthCal.set(Calendar.MONTH, previousMonth.getMonth().intValue());
		previousMonthCal.set(Calendar.YEAR, previousMonth.getYear().intValue());
		
		//populate previous month based on number of days in the fist week before first day of selected month
		int previousMonthMaxDay = previousMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		List<Long> previousMonthDays = new ArrayList<Long>();
		for (int i=previousMonthMaxDay-selectedMonthFirstDayWeek+2; i<=previousMonthMaxDay; i++) {
			previousMonthDays.add(Long.valueOf(i));
		}
		previousMonth.setDays(previousMonthDays);
		
		//calculate next month/year based on selected month/year
		nextMonth.setMonth(selectedMonth.getMonth() + 1);
		nextMonth.setYear(selectedMonth.getYear());
		if (nextMonth.getMonth() > 11L) {
			nextMonth.setYear(nextMonth.getYear() + 1);
			nextMonth.setMonth(0L);
		}

		//populate next month based on number of days missing to complete the calendar (42 days = 6 weeks)
		List<Long> nextMonthDays = new ArrayList<Long>();
		for(int i=1;i<=42-previousMonthDays.size()-selectedMonthDays.size();i++){
			nextMonthDays.add(Long.valueOf(i));
		}
		nextMonth.setDays(nextMonthDays);
		
		return response;
	}

	@Override
	public Map<String, List<CalendarEventData>> getCalendarMonthEvents(CalendarMonthDefinitionData calendarDefinition, UserProfileData user) {

		Map<String, List<CalendarEventData>> response = new HashMap<String, List<CalendarEventData>>();
		
		//get user data
		Long franchisorId = null;
		Long franchiseeId = null;
		if (user.getSelectedRole() == UserProfileEnum.FRANCHISOR) {
			franchisorId = user.getFranchisor().getFranchisorId();
		} else if (user.getSelectedRole() == UserProfileEnum.FRANCHISEE) {
			franchiseeId = user.getFranchisee().getFranchiseeId();
			franchisorId = franchiseeManagement.getFranchisee(franchiseeId).getFranchisorId();
		}
		
		//Get events for previous month
		if (!calendarDefinition.getPreviousMonth().getDays().isEmpty()) {
			response.putAll(getCalendarMonthEvents(calendarDefinition.getPreviousMonth(), franchisorId, franchiseeId));
		}
		
		//Get events for selected month
		if (!calendarDefinition.getSelectedMonth().getDays().isEmpty()) {
			response.putAll(getCalendarMonthEvents(calendarDefinition.getSelectedMonth(), franchisorId, franchiseeId));
		}
				
		//Get events for next month
		if (!calendarDefinition.getNextMonth().getDays().isEmpty()) {
			response.putAll(getCalendarMonthEvents(calendarDefinition.getNextMonth(), franchisorId, franchiseeId));
		}
		
		return response;
	}
	
	private Map<String, List<CalendarEventData>> getCalendarMonthEvents(CalendarMonthData month, Long franchisorId, Long franchiseeId) {
		
		Map<String, List<CalendarEventData>> response = new HashMap<String, List<CalendarEventData>>();
		
		Date startDate = null;
		Date endDate = null;
		Calendar calendar = DateUtil.getLocalCalendar();
		
		//start calendar with first day in the calendar at 00:00:00
		calendar.set(month.getYear().intValue(), 
				month.getMonth().intValue(),
				month.getDays().get(0).intValue(),
        		0, 0, 0);
		startDate = calendar.getTime();
		
		//update calendar with last day in the calendar at 23:59:59
		calendar.set(month.getYear().intValue(), 
				month.getMonth().intValue(),
				month.getDays().get(month.getDays().size() - 1).intValue(),
				23, 59, 59);
		endDate = calendar.getTime();
		
		//get calendar events for the date range and franchisorId and create a map with a list of events per day
		List<CalendarEventData> calendarEvents = calendarEventDAO.findByEntityIdAndDateRange(franchisorId, startDate, endDate);
		if (calendarEvents != null && !calendarEvents.isEmpty()) {
			for (CalendarEventData calendarEvent : calendarEvents) {
				
				//do not add if event has restrictions
				if (franchiseeId != null) {
						
					//do not add events that have restricted access only by the franchisor
					if (calendarEvent.getAccessRestricted()) {
						continue;
					} else {
						
						// check if event has franchisse restriction
						List<Long> franchiseeIds = calendarEvent.getFranchiseeIds();
						if (franchiseeIds != null && !franchiseeIds.isEmpty()) {
							boolean allowed = false;
							
							for (Long eventFranchiseeId: franchiseeIds) {
								if (eventFranchiseeId.equals(franchiseeId)) {
									allowed = true;
								}
							}
							
							if (!allowed) {
								continue;
							}
						}
						
					}

				}
				
				Calendar eventDate = DateUtil.getLocalCalendar();
				eventDate.setTime(calendarEvent.getFromHour());
				String dateKey = eventDate.get(Calendar.YEAR)+"-"+eventDate.get(Calendar.MONTH)+"-"+eventDate.get(Calendar.DAY_OF_MONTH);
				List<CalendarEventData> dateCalendarEvents = response.get(dateKey);
				if (dateCalendarEvents == null) {
					dateCalendarEvents = new ArrayList<CalendarEventData>();
					response.put(dateKey, dateCalendarEvents);
				}
				dateCalendarEvents.add(calendarEvent);
				
			}
		}		
		
		return response;
		
	}
	
	@Override
	public CalendarEventData createCalendarEvent(CalendarEventData calendarEvent) {
		
		calendarEvent = calendarEventDAO.save(calendarEvent);
		
		//Asynchronously process calendar event
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/job/async/processCalendarEvent/"+calendarEvent.getId()+"/0/"+OperationTypeEnum.CREATE));
		
		return calendarEvent;
	}

	@Override
	public CalendarEventData updateCalendarEvent(CalendarEventData calendarEvent) {
		
		CalendarEventData oldCalendarEvent = calendarEventDAO.findById(calendarEvent.getId());
		calendarEvent = calendarEventDAO.save(calendarEvent);
		
		oldCalendarEvent.setId(null);
		CalendarEventHistoryData oldCalendarDeletedEvent = calendarEventHistoryDAO.save(convertToCalendarEventHistory(oldCalendarEvent));
		
		//Asynchronously process calendar event
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/job/async/processCalendarEvent/" + calendarEvent.getId() + "/" +oldCalendarDeletedEvent.getId()+"/"+OperationTypeEnum.UPDATE));
		
		return calendarEvent;
	}

	@Override
	public CalendarEventData deleteCalendarEvent(Long id) {
		
		CalendarEventData deletedEvent = calendarEventDAO.delete(id); 
		calendarEventHistoryDAO.save(convertToCalendarEventHistory(deletedEvent));
		
		//Asynchronously process calendar event
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/job/async/processCalendarEvent/0/"+deletedEvent.getId()+"/"+OperationTypeEnum.DELETE));
		
		return deletedEvent;
	}
	
	@Override
	public void processCalendarEventAsync(Long calendarEventId, Long calendarEventHistoryId, OperationTypeEnum operation) {
		
		log.fine("Processing event with calendarEventId " + calendarEventId + " and calendarEventHistoryId " + calendarEventHistoryId + " for operation " + operation);
		
		CalendarEventData calendarEventNew = null;
		if (calendarEventId != 0L) {
			calendarEventNew = calendarEventDAO.findById(calendarEventId);
		}
		
		CalendarEventData calendarEventOld = null;
		if (calendarEventHistoryId != 0L) {
			calendarEventOld = convertToCalendarEvent(calendarEventHistoryDAO.findById(calendarEventHistoryId));
		}
		
		// Send e-mail
		mailManagement.sendCalendarEventUpdate(calendarEventOld, calendarEventNew, operation);
		
	}
	
	private CalendarEventHistoryData convertToCalendarEventHistory(CalendarEventData calendarEvent) {
		
		CalendarEventHistoryData result = null;
		
		ModelMapper modelMapper = new ModelMapper();
		result = modelMapper.map(calendarEvent, CalendarEventHistoryData.class);
		
		return result;
		
	}
	
	private CalendarEventData convertToCalendarEvent(CalendarEventHistoryData calendarEventHistory) {
		
		CalendarEventData result = null;
		
		ModelMapper modelMapper = new ModelMapper();
		result = modelMapper.map(calendarEventHistory, CalendarEventData.class);
		
		return result;
		
	}

	@Override
	public List<CalendarEventData> getNextCalendarEvents(Long amount, UserProfileData user) {
		
		List<CalendarEventData> response = new ArrayList<CalendarEventData>();
		
		//get user data
		Long franchisorId = null;
		Long franchiseeId = null;
		if (user.getSelectedRole() == UserProfileEnum.FRANCHISOR) {
			franchisorId = user.getFranchisor().getFranchisorId();
		} else if (user.getSelectedRole() == UserProfileEnum.FRANCHISEE) {
			franchiseeId = user.getFranchisee().getFranchiseeId();
			franchisorId = franchiseeManagement.getFranchisee(franchiseeId).getFranchisorId();
		}

		Date startDate = null;
		Calendar calendar = DateUtil.getLocalCalendar();
		
		//start calendar with first day in the calendar at 00:00:00
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		startDate = calendar.getTime();
		
		//get calendar events for the date range and franchisorId and create a map with a list of events per day
		List<CalendarEventData> calendarEvents = calendarEventDAO.findByEntityIdAndStartDate(franchisorId, startDate);
		if (calendarEvents != null && !calendarEvents.isEmpty()) {
			for (CalendarEventData calendarEvent : calendarEvents) {
				
				//do not add if event has restrictions
				if (franchiseeId != null) {
						
					//do not add events that have restricted access only by the franchisor
					if (calendarEvent.getAccessRestricted()) {
						continue;
					} else {
						
						// check if event has franchisse restriction
						List<Long> franchiseeIds = calendarEvent.getFranchiseeIds();
						if (franchiseeIds != null && !franchiseeIds.isEmpty()) {
							boolean allowed = false;
							
							for (Long eventFranchiseeId: franchiseeIds) {
								if (eventFranchiseeId.equals(franchiseeId)) {
									allowed = true;
								}
							}
							
							if (!allowed) {
								continue;
							}
						}
						
					}

				}
				
				response.add(calendarEvent);
				
				if (response.size() == amount) {
					break;
				}
			}
		}
		
		return response;
	}
	
}
