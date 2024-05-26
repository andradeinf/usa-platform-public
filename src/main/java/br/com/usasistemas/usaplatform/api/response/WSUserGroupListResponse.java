package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.UserGroupData;

public class WSUserGroupListResponse extends GenericResponse {

	List<UserGroupData> userGroups;

	public List<UserGroupData> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroupData> userGroups) {
		this.userGroups = userGroups;
	}
	
}
