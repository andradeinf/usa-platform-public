package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.CalendarEventData;
import br.com.usasistemas.usaplatform.model.data.CalendarMonthDefinitionData;

public class WSCalendarMonthResponse extends GenericResponse {

	private CalendarMonthDefinitionData monthDefinition;
	private Map<String, List<CalendarEventData>> calendarEvents;

	public CalendarMonthDefinitionData getMonthDefinition() {
		return monthDefinition;
	}

	public void setMonthDefinition(CalendarMonthDefinitionData monthDefinition) {
		this.monthDefinition = monthDefinition;
	}

	public Map<String, List<CalendarEventData>> getCalendarEvents() {
		return calendarEvents;
	}

	public void setCalendarEvents(Map<String, List<CalendarEventData>> calendarEvents) {
		this.calendarEvents = calendarEvents;
	}

}
