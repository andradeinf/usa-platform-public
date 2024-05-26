package br.com.usasistemas.usaplatform.model.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Entity
@Index
public class UserGroupEntityUser {
	
	@Id private Long id;
	private UserProfileEnum entityProfile;	//Franchisor/Franchisee/Supplier
	private Long entityUserId; //franchiseeUserId/FranchisorUserId/SupplierUserId
	private Long userGroupId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfileEnum getEntityProfile() {
		return entityProfile;
	}

	public void setEntityProfile(UserProfileEnum entityProfile) {
		this.entityProfile = entityProfile;
	}

	public Long getEntityUserId() {
		return entityUserId;
	}

	public void setEntityUserId(Long entityUserId) {
		this.entityUserId = entityUserId;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

}
