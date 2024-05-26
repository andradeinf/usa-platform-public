package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.UserGroupData;

public class WSUserGroupResponse extends GenericResponse {

	UserGroupData userGroup;

	public UserGroupData getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroupData userGroup) {
		this.userGroup = userGroup;
	}
	
}
