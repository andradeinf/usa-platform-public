package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.PublicUserData;

public class WSUserServiceUserDataListResponse extends GenericResponse {

	private List<PublicUserData> userList;

	public List<PublicUserData> getUserList() {
		return userList;
	}

	public void setUserList(List<PublicUserData> userList) {
		this.userList = userList;
	}

}
