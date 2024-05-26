package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.FranchisorUserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;

public class WSFranchisorUserListResponse extends GenericResponse {

	private List<FranchisorUserData> franchisorUsers;
	private List<UserGroupData> userGroups;
	private Map<Long, Map<Long, Boolean>> franchisorUsersGroups;
	
	public List<FranchisorUserData> getFranchisorUsers() {
		return franchisorUsers;
	}
	
	public void setFranchisorUsers(List<FranchisorUserData> franchisorUsers) {
		this.franchisorUsers = franchisorUsers;
	}

	public List<UserGroupData> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroupData> userGroups) {
		this.userGroups = userGroups;
	}

	public Map<Long, Map<Long, Boolean>> getFranchisorUsersGroups() {
		return franchisorUsersGroups;
	}

	public void setFranchisorUsersGroups(Map<Long, Map<Long, Boolean>> franchisorUsersGroups) {
		this.franchisorUsersGroups = franchisorUsersGroups;
	}
	

	
}
