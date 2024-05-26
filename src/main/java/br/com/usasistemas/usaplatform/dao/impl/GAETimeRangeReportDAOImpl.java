package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.cmd.Query;

import br.com.usasistemas.usaplatform.dao.TimeRangeReportDAO;
import br.com.usasistemas.usaplatform.model.data.TimeRangeReportData;
import br.com.usasistemas.usaplatform.model.entity.TimeRangeReport;
import br.com.usasistemas.usaplatform.model.enums.OperatorTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.TimeRangeReportTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class GAETimeRangeReportDAOImpl extends GAEGenericDAOImpl<TimeRangeReport, TimeRangeReportData> implements TimeRangeReportDAO {
	
	private static final Logger log = Logger.getLogger(GAETimeRangeReportDAOImpl.class.getName());

	@Override
	public List<TimeRangeReportData> findByTypeAndEntityProfileAndEntityId(TimeRangeReportTypeEnum type, UserProfileEnum entityProfile, Long entityId) {
		List<TimeRangeReportData> result = new ArrayList<TimeRangeReportData>();

		try {
			List<TimeRangeReport> timeRangeReports = ofy().load().type(TimeRangeReport.class)
				.filter("type", type)
				.filter("entityProfile", entityProfile)
				.filter("entityId", entityId)
				.list();
			if (timeRangeReports != null && !timeRangeReports.isEmpty())
				result = this.getConverter().convertToDataList(timeRangeReports);
		} catch (Exception e) {
			log.warning("Error when querying for TimeRangeReport by entityId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<TimeRangeReportData> findByDate(Date date, OperatorTypeEnum operator) {
		List<TimeRangeReportData> result = new ArrayList<TimeRangeReportData>();
		
		try {
			Query<TimeRangeReport> q = ofy().load().type(TimeRangeReport.class);

			if (operator.equals(OperatorTypeEnum.EQUAL)) {
				q = q.filter("date", date);
			} else if (operator.equals(OperatorTypeEnum.GREATER_OR_EQUAL_THAN)) {
				q = q.filter("date >=", date);
			} else if (operator.equals(OperatorTypeEnum.GREATER_THAN)) {
				q = q.filter("date >", date);
			} else if (operator.equals(OperatorTypeEnum.LOWER_OR_EQUAL_THAN)) {
				q = q.filter("date <=", date);
			} else if (operator.equals(OperatorTypeEnum.LOWER_THAN)) {
				q = q.filter("date <", date);
			}	
			
			List<TimeRangeReport> timeRangeReports = q.list();
			if (timeRangeReports != null && !timeRangeReports.isEmpty())
				result = this.getConverter().convertToDataList(timeRangeReports);
		} catch (Exception e) {
			log.warning("Error when querying for TimeRangeReport by Date: " + e.toString());
		}	
		
		return result;
	}

}
