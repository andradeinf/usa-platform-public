package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.CalendarEventData;

public class WSCalendarEventResponse extends GenericResponse {
	
	CalendarEventData calendarEvent;

	public CalendarEventData getCalendarEvent() {
		return calendarEvent;
	}

	public void setCalendarEvent(CalendarEventData calendarEvent) {
		this.calendarEvent = calendarEvent;
	}

}
