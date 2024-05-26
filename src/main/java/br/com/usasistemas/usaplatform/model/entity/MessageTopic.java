package br.com.usasistemas.usaplatform.model.entity;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Entity
@Index
public class MessageTopic {
	
	@Id private Long id;
	private UserProfileEnum fromEntityProfile;	//Administrator/Franchisor/Franchisee/Supplier
	private Long fromEntityId;					//0 for Administrator/FranchisorId/FranchiseeId/SupplierId
	private Long fromUserGroupId; 				//UserGroupId (0 for Administrator)
	private Long fromUserId; 					//UserId
	private UserProfileEnum toEntityProfile;   	//Administrator/Franchisor/Franchisee/Supplier
	private Long toEntityId;					//0 for Administrator/FranchisorId/FranchiseeId/SupplierId
	private Long toUserGroupId;					//UserGroupId (0 for Administrator)
	private Date date;
	private Date updateDate;
	private String title;
	private List<Long> groups;
	private String domainKey;

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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

}
