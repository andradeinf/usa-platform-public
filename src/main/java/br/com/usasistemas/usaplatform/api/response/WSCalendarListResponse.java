package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.CalendarEventData;

public class WSCalendarListResponse extends GenericResponse {

	private List<CalendarEventData> calendarEvents;

	public List<CalendarEventData> getCalendarEvents() {
		return calendarEvents;
	}

	public void setCalendarEvents(List<CalendarEventData> calendarEvents) {
		this.calendarEvents = calendarEvents;
	}
}
