package br.com.usasistemas.usaplatform.api.response;

import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.SupplierUserData;

public class WSSupplierUserResponse extends GenericResponse {

	private SupplierUserData supplierUser;
	private Map<Long, Boolean> userGroups;
	
	public SupplierUserData getSupplierUser() {
		return supplierUser;
	}
	
	public void setSupplierUser(SupplierUserData supplierUser) {
		this.supplierUser = supplierUser;
	}
	
	public Map<Long, Boolean> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Map<Long, Boolean> userGroups) {
		this.userGroups = userGroups;
	}
	
}
