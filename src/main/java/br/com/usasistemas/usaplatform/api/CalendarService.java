package br.com.usasistemas.usaplatform.api;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.WSCalendarEventResponse;
import br.com.usasistemas.usaplatform.api.response.WSCalendarListResponse;
import br.com.usasistemas.usaplatform.api.response.WSCalendarMonthResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.CalendarManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.CalendarEventData;
import br.com.usasistemas.usaplatform.model.data.CalendarMonthDefinitionData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

@Controller
@RequestMapping(value = UrlMapping.CALENDAR_SERVICE)
public class CalendarService {
	
	private static final Logger log = Logger.getLogger(CalendarService.class.getName());
	private CalendarManagementBO calendarManagement;
	
	public CalendarManagementBO getCalendarManagement() {
		return calendarManagement;
	}

	public void setCalendarManagement(CalendarManagementBO calendarManagement) {
		this.calendarManagement = calendarManagement;
	}

	@RequestMapping(value = "/monthOffset/{monthOffset}", method=RequestMethod.GET)
	@ResponseBody
	public WSCalendarMonthResponse listCalendar(@PathVariable Long monthOffset, HttpSession session) {
		
		WSCalendarMonthResponse response = new WSCalendarMonthResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);

			if (!user.getFeatureFlags().getFlagCalendar()) {

				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.GENERIC_SUCCESS.code());
				rm.setMessage(ResponseCodesEnum.GENERIC_SUCCESS.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access calendar!");

			} else {
			
				CalendarMonthDefinitionData calendarDefinition = calendarManagement.getCalendarMonthDefinition(monthOffset);
				response.setMonthDefinition(calendarDefinition);
				
				Map<String, List<CalendarEventData>> calendarEvents = calendarManagement.getCalendarMonthEvents(calendarDefinition, user);
				response.setCalendarEvents(calendarEvents);
			}
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Calendar List: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(value = "/nextEvents/{amount}", method=RequestMethod.GET)
	@ResponseBody
	public WSCalendarListResponse listNextEvents(@PathVariable Long amount, HttpSession session) {
		
		WSCalendarListResponse response = new WSCalendarListResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			if (!user.getFeatureFlags().getFlagCalendar()) {

				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.GENERIC_SUCCESS.code());
				rm.setMessage(ResponseCodesEnum.GENERIC_SUCCESS.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access calendar!");

			} else {
				response.setCalendarEvents(calendarManagement.getNextCalendarEvents(amount, user));
			}			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Calendar List: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSCalendarEventResponse createCalendarEvent(@RequestBody CalendarEventData calendarEvent, HttpSession session) {
		
		WSCalendarEventResponse response = new WSCalendarEventResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			if (calendarEvent.getId() == 0) {
				calendarEvent.setId(null);
				calendarEvent.setEntityProfile(user.getSelectedRole());
				calendarEvent.setEntityId(user.getSelectedEntityId());
			}
			
			response.setCalendarEvent(calendarManagement.createCalendarEvent(calendarEvent));
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Calendar Event: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	public WSCalendarEventResponse updateCalendarEvent(@RequestBody CalendarEventData calendarEvent) {
		
		WSCalendarEventResponse response = new WSCalendarEventResponse();
		
		try {
			response.setCalendarEvent(calendarManagement.updateCalendarEvent(calendarEvent));
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Calendar Event: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSCalendarEventResponse deleteCalendarEvent(@PathVariable Long id) {
		
		WSCalendarEventResponse response = new WSCalendarEventResponse();
		
		try {
			response.setCalendarEvent(calendarManagement.deleteCalendarEvent(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Calendar Event: " + e.toString());
		}
		
		return response;
	}
	
}
