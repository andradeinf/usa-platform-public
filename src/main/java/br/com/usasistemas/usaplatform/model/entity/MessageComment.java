package br.com.usasistemas.usaplatform.model.entity;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Entity
@Index
public class MessageComment {
	
	@Id private Long id;
	private Long messageTopicId;
	private UserProfileEnum userProfile;
	private Long userEntityId;
	private Long userId;
	private Date date;
	@Unindex private String message;
	private String domainKey;
	private List<Long> attachmentIds;
	
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

}
