package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class MessageCommentData {
	
	private Long id;
	private Long messageTopicId;
	private UserProfileEnum userProfile;
	private Long userEntityId;
	private Long userId;
	private Date date;
	private String message;
	private String domainKey;
	private List<Long> attachmentIds;
	private List<MessageAttachmentData> attachments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getMessageTopicId() {
		return messageTopicId;
	}

	public void setMessageTopicId(Long messageTopicId) {
		this.messageTopicId = messageTopicId;
	}

	public UserProfileEnum getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfileEnum userProfile) {
		this.userProfile = userProfile;
	}

	public Long getUserEntityId() {
		return userEntityId;
	}

	public void setUserEntityId(Long userEntityId) {
		this.userEntityId = userEntityId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public List<MessageAttachmentData> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<MessageAttachmentData> attachments) {
		this.attachments = attachments;
	}

}
