package br.com.usasistemas.usaplatform.bizo;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.com.usasistemas.usaplatform.model.data.TimeRangeReportData;
import br.com.usasistemas.usaplatform.model.enums.TimeRangeReportTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public interface ReportManagementBO {

	public List<TimeRangeReportData> getTimeRangeReports(TimeRangeReportTypeEnum type, UserProfileEnum entityProfile, Long entityId);
	public void getTimeRangeReport(Long id, HttpServletResponse response);
	public TimeRangeReportData createTimeRangeReport(TimeRangeReportTypeEnum type, UserProfileEnum entityProfile, Long entityId, Long entityUserId, Date initialDate, Date finalDate, Long filterSupplierId, Long filterFranchisorId, Long filterFranchiseeId);
	public TimeRangeReportData generateTimeRangeReport(Long id);
	public void cleanUpTimeRangeReports(Long threshold);
}
