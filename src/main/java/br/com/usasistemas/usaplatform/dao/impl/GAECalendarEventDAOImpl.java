package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.CalendarEventDAO;
import br.com.usasistemas.usaplatform.model.data.CalendarEventData;
import br.com.usasistemas.usaplatform.model.entity.CalendarEvent;

public class GAECalendarEventDAOImpl extends GAEGenericDAOImpl<CalendarEvent, CalendarEventData> implements CalendarEventDAO {
	
	private static final Logger log = Logger.getLogger(GAECalendarEventDAOImpl.class.getName());

	@Override
	public List<CalendarEventData> findByEntityIdAndDateRange(Long entityId, Date startDate, Date endDate) {
		List<CalendarEventData> result = new ArrayList<CalendarEventData>();
		
		try {
	      List<CalendarEvent> calendarEvents = ofy().load().type(CalendarEvent.class)
			.filter("entityId", entityId)
			.filter("fromHour >=", startDate)
			.filter("fromHour <=", endDate)
			.list();
	      if ((calendarEvents != null) && (!calendarEvents.isEmpty()))
	        result = getConverter().convertToDataList(calendarEvents);
	    } catch (Exception e) {
	      log.warning("Error when querying for CalendarEvents by entityId and date range : " + e.toString());
	    }
		
		return result;	
	}

	@Override
	public List<CalendarEventData> findByEntityIdAndStartDate(Long entityId, Date startDate) {
		List<CalendarEventData> result = new ArrayList<CalendarEventData>();
		
		try {
		  List<CalendarEvent> calendarEvents = ofy().load().type(CalendarEvent.class)
			.filter("entityId", entityId)
			.filter("fromHour >=", startDate)
			.list();
	      if ((calendarEvents != null) && (!calendarEvents.isEmpty()))
	        result = getConverter().convertToDataList(calendarEvents);
	    } catch (Exception e) {
	      log.warning("Error when querying for CalendarEvents by entityId and start date: " + e.toString());
	    }
		
		return result;
	}


	
}
