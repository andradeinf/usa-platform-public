package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;

public class WSFranchiseeUserListResponse extends GenericResponse {

	private List<FranchiseeUserData> franchiseeUsers;
	private List<UserGroupData> userGroups;
	private Map<Long, Map<Long, Boolean>> franchiseeUsersGroups;
	
	public List<FranchiseeUserData> getFranchiseeUsers() {
		return franchiseeUsers;
	}
	
	public void setFranchiseeUsers(List<FranchiseeUserData> franchiseeUsers) {
		this.franchiseeUsers = franchiseeUsers;
	}
	
	public List<UserGroupData> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroupData> userGroups) {
		this.userGroups = userGroups;
	}

	public Map<Long, Map<Long, Boolean>> getFranchiseeUsersGroups() {
		return franchiseeUsersGroups;
	}

	public void setFranchiseeUsersGroups(Map<Long, Map<Long, Boolean>> franchiseeUsersGroups) {
		this.franchiseeUsersGroups = franchiseeUsersGroups;
	}
	
}
