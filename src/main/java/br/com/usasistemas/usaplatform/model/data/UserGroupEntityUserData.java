package br.com.usasistemas.usaplatform.model.data;

import java.io.Serializable;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@SuppressWarnings("serial")
public class UserGroupEntityUserData implements Serializable {
	
	private Long id;
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
