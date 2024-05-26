package br.com.usasistemas.usaplatform.dao;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.model.data.CalendarEventData;
import br.com.usasistemas.usaplatform.model.entity.CalendarEvent;

public interface CalendarEventDAO extends GenericDAO<CalendarEvent, CalendarEventData> {

	public List<CalendarEventData> findByEntityIdAndDateRange(Long entityId, Date startDate, Date endDate);
	public List<CalendarEventData> findByEntityIdAndStartDate(Long entityId, Date startDate);
	
}
