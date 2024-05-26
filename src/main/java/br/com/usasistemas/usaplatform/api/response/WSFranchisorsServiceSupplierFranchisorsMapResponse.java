package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.FranchisorData;

public class WSFranchisorsServiceSupplierFranchisorsMapResponse extends GenericResponse {

	private List<FranchisorData> franchisors;
	private Map<Long, Boolean> supplierFranchisors;

	public List<FranchisorData> getFranchisors() {
		return franchisors;
	}

	public void setFranchisors(List<FranchisorData> franchisors) {
		this.franchisors = franchisors;
	}

	public Map<Long, Boolean> getSupplierFranchisors() {
		return supplierFranchisors;
	}

	public void setSupplierFranchisors(Map<Long, Boolean> supplierFranchisors) {
		this.supplierFranchisors = supplierFranchisors;
	}

}
