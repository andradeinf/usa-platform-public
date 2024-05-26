package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.TimeRangeReportData;

public class WSTimeRangeReportListResponse extends GenericResponse {

	private List<TimeRangeReportData> timeRangeReports;

	public List<TimeRangeReportData> getTimeRangeReports() {
		return timeRangeReports;
	}

	public void setTimeRangeReports(List<TimeRangeReportData> timeRangeReports) {
		this.timeRangeReports = timeRangeReports;
	}

}
