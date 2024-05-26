package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.TimeRangeReportData;

public class WSTimeRangeReportResponse extends GenericResponse {

	private TimeRangeReportData timeRangeReport;

	public TimeRangeReportData getTimeRangeReport() {
		return timeRangeReport;
	}

	public void setTimeRangeReport(TimeRangeReportData timeRangeReport) {
		this.timeRangeReport = timeRangeReport;
	}

}
