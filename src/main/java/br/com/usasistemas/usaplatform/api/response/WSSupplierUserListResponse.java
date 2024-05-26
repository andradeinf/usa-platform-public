package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.SupplierUserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;

public class WSSupplierUserListResponse extends GenericResponse {

	private List<SupplierUserData> supplierUsers;
	private List<UserGroupData> userGroups;
	private Map<Long, Map<Long, Boolean>> supplierUsersGroups;
	
	public List<SupplierUserData> getSupplierUsers() {
		return supplierUsers;
	}
	
	public void setSupplierUsers(List<SupplierUserData> supplierUsers) {
		this.supplierUsers = supplierUsers;
	}

	public List<UserGroupData> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroupData> userGroups) {
		this.userGroups = userGroups;
	}

	public Map<Long, Map<Long, Boolean>> getSupplierUsersGroups() {
		return supplierUsersGroups;
	}

	public void setSupplierUsersGroups(Map<Long, Map<Long, Boolean>> supplierUsersGroups) {
		this.supplierUsersGroups = supplierUsersGroups;
	}

	
}
