package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;

import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.NotificationTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class NotificationData {
	
	private Long id;
	private Date date;
	private UserProfileEnum entityProfile;
	private Long entityId;
	private Long userGroupId;
	private Long userId;
	private Long referenceId;
	private Long referenceId2;
	private NotificationTypeEnum type;
	private Boolean isRead;
	private MailNotificationStatusEnum notificationStatus;
	private String domainKey;

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

	public UserProfileEnum getEntityProfile() {
		return entityProfile;
	}

	public void setEntityProfile(UserProfileEnum entityProfile) {
		this.entityProfile = entityProfile;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public Long getReferenceId2() {
		return referenceId2;
	}

	public void setReferenceId2(Long referenceId2) {
		this.referenceId2 = referenceId2;
	}

	public NotificationTypeEnum getType() {
		return type;
	}

	public void setType(NotificationTypeEnum type) {
		this.type = type;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public MailNotificationStatusEnum getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(MailNotificationStatusEnum notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public String getDomainKey() {
		return domainKey;
	}

	public void setDomainKey(String domainKey) {
		this.domainKey = domainKey;
	}

}
