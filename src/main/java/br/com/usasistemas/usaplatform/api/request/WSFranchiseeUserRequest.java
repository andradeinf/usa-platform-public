package br.com.usasistemas.usaplatform.api.request;

import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;

public class WSFranchiseeUserRequest {
	
	private FranchiseeUserData franchiseeUser;
	private Map<Long, Boolean> userGroups;
	
	public FranchiseeUserData getFranchiseeUser() {
		return franchiseeUser;
	}

	public void setFranchiseeUser(FranchiseeUserData franchiseeUser) {
		this.franchiseeUser = franchiseeUser;
	}

	public Map<Long, Boolean> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Map<Long, Boolean> userGroups) {
		this.userGroups = userGroups;
	}

}
