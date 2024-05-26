package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.EntityData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;

public class WSUserServiceUserDataResponse extends GenericResponse {

	private PublicUserData user;
	private EntityData entity;
	private List<UserGroupData> userGroups;

	public PublicUserData getUser() {
		return user;
	}

	public void setUser(PublicUserData user) {
		this.user = user;
	}

	public EntityData getEntity() {
		return entity;
	}

	public void setEntity(EntityData entity) {
		this.entity = entity;
	}

	public List<UserGroupData> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroupData> userGroups) {
		this.userGroups = userGroups;
	}

}
