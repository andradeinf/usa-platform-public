package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.StateData;

public class WSFranchiseeResponse extends GenericResponse {

	private FranchiseeData franchisee;
	private StateData state;
	private CityData city;

	public FranchiseeData getFranchisee() {
		return franchisee;
	}

	public void setFranchisee(FranchiseeData franchisee) {
		this.franchisee = franchisee;
	}
	
	public StateData getState() {
		return state;
	}

	public void setState(StateData state) {
		this.state = state;
	}

	public CityData getCity() {
		return city;
	}

	public void setCity(CityData city) {
		this.city = city;
	}

}
