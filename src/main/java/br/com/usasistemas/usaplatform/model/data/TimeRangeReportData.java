package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;

import br.com.usasistemas.usaplatform.model.enums.ReportStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.TimeRangeReportTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class TimeRangeReportData {
	
	private Long id;
	private Date date;
	private TimeRangeReportTypeEnum type;
	private UserProfileEnum entityProfile;
	private Long entityId; //FranchiseeId/FranchisorId/SupplierId
	private Long entityUserId;
	private Date initDate;
	private Date endDate;
	private Long filterSupplierId;
	private Long filterFranchisorId;
	private Long filterFranchiseeId;
	private ReportStatusEnum status;
	private String fileKey;
	private String fileName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public TimeRangeReportTypeEnum getType() {
		return this.type;
	}
	
	public void setType(TimeRangeReportTypeEnum type) {
		this.type = type;
	}

	public UserProfileEnum getEntityProfile() {
		return this.entityProfile;
	}

	public void setEntityProfile(UserProfileEnum entityProfile) {
		this.entityProfile = entityProfile;
	}
	
	public Long getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getEntityUserId() {
		return this.entityUserId;
	}

	public void setEntityUserId(Long entityUserId) {
		this.entityUserId = entityUserId;
	}

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

	public ReportStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ReportStatusEnum status) {
		this.status = status;
	}
	
	public String getStatusDescription() {
		return status.getDescription();
	}

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
