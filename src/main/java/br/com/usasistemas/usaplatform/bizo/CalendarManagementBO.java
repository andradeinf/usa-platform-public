package br.com.usasistemas.usaplatform.bizo;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.CalendarEventData;
import br.com.usasistemas.usaplatform.model.data.CalendarMonthDefinitionData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.OperationTypeEnum;

public interface CalendarManagementBO {

	public CalendarMonthDefinitionData getCalendarMonthDefinition(Long monthOffset);
	public Map<String, List<CalendarEventData>> getCalendarMonthEvents(CalendarMonthDefinitionData calendarDefinition, UserProfileData user);
	public CalendarEventData createCalendarEvent(CalendarEventData calendarEvent);
	public CalendarEventData updateCalendarEvent(CalendarEventData calendarEvent);
	public CalendarEventData deleteCalendarEvent(Long id);
	public void processCalendarEventAsync(Long calendarEventId, Long calendarEventHistoryId, OperationTypeEnum operation);
	public List<CalendarEventData> getNextCalendarEvents(Long amount, UserProfileData user);
	
}
