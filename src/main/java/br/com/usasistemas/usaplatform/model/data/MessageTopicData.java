package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class MessageTopicData {
	
	private Long id;
	private UserProfileEnum fromEntityProfile;
	private Long fromEntityId;
	private Long fromUserGroupId;
	private Long fromUserId;
	private UserProfileEnum toEntityProfile;
	private Long toEntityId;
	private Long toUserGroupId;
	private Date date;
	private Date updateDate;
	private String title;
	private String message;
	private List<Long> groups;
	private String domainKey;
	private List<Long> attachmentIds;
	private List<Long> labels;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfileEnum getFromEntityProfile() {
		return fromEntityProfile;
	}

	public void setFromEntityProfile(UserProfileEnum fromEntityProfile) {
		this.fromEntityProfile = fromEntityProfile;
	}

	public Long getFromEntityId() {
		return fromEntityId;
	}

	public void setFromEntityId(Long fromEntityId) {
		this.fromEntityId = fromEntityId;
	}

	public Long getFromUserGroupId() {
		return fromUserGroupId;
	}

	public void setFromUserGroupId(Long fromUserGroupId) {
		this.fromUserGroupId = fromUserGroupId;
	}

	public Long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public UserProfileEnum getToEntityProfile() {
		return toEntityProfile;
	}

	public void setToEntityProfile(UserProfileEnum toEntityProfile) {
		this.toEntityProfile = toEntityProfile;
	}

	public Long getToEntityId() {
		return toEntityId;
	}

	public void setToEntityId(Long toEntityId) {
		this.toEntityId = toEntityId;
	}

	public Long getToUserGroupId() {
		return toUserGroupId;
	}

	public void setToUserGroupId(Long toUserGroupId) {
		this.toUserGroupId = toUserGroupId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Long> getGroups() {
		return groups;
	}

	public void setGroups(List<Long> groups) {
		this.groups = groups;
	}

	public String getDomainKey() {
		return domainKey;
	}

	public void setDomainKey(String domainKey) {
		this.domainKey = domainKey;
	}

	public List<Long> getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(List<Long> attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

	public List<Long> getLabels() {
		return labels;
	}

	public void setLabels(List<Long> labels) {
		this.labels = labels;
	}

}
