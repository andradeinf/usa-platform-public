package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.FranchisorData;

public class WSFranchisorsServiceFranchisorResponse extends GenericResponse {

	FranchisorData franchisor;

	public FranchisorData getFranchisor() {
		return franchisor;
	}

	public void setFranchisor(FranchisorData franchisor) {
		this.franchisor = franchisor;
	}

}
