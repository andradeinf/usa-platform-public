package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;

public class DocumentsFileData {
	
	private Long id;
	private Long folderId;
	private Long franchisorId;
	private String name;
	private Date date;
	private String contentType;
	private String storePath;
	private Long size;
	private String fileKey;
	private MailNotificationStatusEnum notificationStatus;
	private Boolean accessRestricted;
	private List<Long> franchiseeIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public Long getFranchisorId() {
		return franchisorId;
	}

	public void setFranchisorId(Long franchisorId) {
		this.franchisorId = franchisorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	public MailNotificationStatusEnum getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(MailNotificationStatusEnum notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public Boolean getAccessRestricted() {
		return accessRestricted;
	}

	public void setAccessRestricted(Boolean accessRestricted) {
		this.accessRestricted = accessRestricted;
	}

	public List<Long> getFranchiseeIds() {
		return franchiseeIds;
	}

	public void setFranchiseeIds(List<Long> franchiseeIds) {
		this.franchiseeIds = franchiseeIds;
	}
	
}
