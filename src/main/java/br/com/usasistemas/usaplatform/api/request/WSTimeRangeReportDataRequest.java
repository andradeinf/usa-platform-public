package br.com.usasistemas.usaplatform.api.request;

import java.util.Date;

public class WSTimeRangeReportDataRequest {
	
	private Date initDate;
	private Date endDate;
	private Long filterSupplierId;
	private Long filterFranchisorId;
	private Long filterFranchiseeId;

	public Date getInitDate() {
		return initDate;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getFilterSupplierId() {
		return this.filterSupplierId;
	}
	
	public void setFilterSupplierId(Long filterSupplierId) {
		this.filterSupplierId = filterSupplierId;
	}
	
	public Long getFilterFranchisorId() {
		return this.filterFranchisorId;
	}

	public void setFilterFranchisorId(Long filterFranchisorId) {
		this.filterFranchisorId = filterFranchisorId;
	}

	public Long getFilterFranchiseeId() {
		return this.filterFranchiseeId;
	}

	public void setFilterFranchiseeId(Long filterFranchiseeId) {
		this.filterFranchiseeId = filterFranchiseeId;
	}
}
