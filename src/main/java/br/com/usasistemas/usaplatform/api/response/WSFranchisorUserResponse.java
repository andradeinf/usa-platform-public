package br.com.usasistemas.usaplatform.api.response;

import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.FranchisorUserData;

public class WSFranchisorUserResponse extends GenericResponse {

	private FranchisorUserData franchisorUser;
	private Map<Long, Boolean> userGroups;
	
	public FranchisorUserData getFranchisorUser() {
		return franchisorUser;
	}
	
	public void setFranchisorUser(FranchisorUserData franchisorUser) {
		this.franchisorUser = franchisorUser;
	}
	
	public Map<Long, Boolean> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Map<Long, Boolean> userGroups) {
		this.userGroups = userGroups;
	}
	
}
