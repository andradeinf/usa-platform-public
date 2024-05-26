package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.FranchisorData;

public class WSFranchisorsServiceSupplierFranchisorsResponse extends GenericResponse {

	private List<FranchisorData> franchisors;

	public List<FranchisorData> getFranchisors() {
		return franchisors;
	}

	public void setFranchisors(List<FranchisorData> franchisors) {
		this.franchisors = franchisors;
	}

}
