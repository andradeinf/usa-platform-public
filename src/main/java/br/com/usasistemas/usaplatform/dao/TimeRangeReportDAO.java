package br.com.usasistemas.usaplatform.dao;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.model.data.TimeRangeReportData;
import br.com.usasistemas.usaplatform.model.entity.TimeRangeReport;
import br.com.usasistemas.usaplatform.model.enums.OperatorTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.TimeRangeReportTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;


public interface TimeRangeReportDAO extends GenericDAO<TimeRangeReport, TimeRangeReportData> {

	public List<TimeRangeReportData> findByTypeAndEntityProfileAndEntityId(TimeRangeReportTypeEnum type, UserProfileEnum entityProfile, Long entityId);
	public List<TimeRangeReportData> findByDate(Date date, OperatorTypeEnum operator);
	
}
