package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.StateData;

public class WSFranchiseeListResponse extends GenericResponse {

	private List<FranchiseeData> franchisees;
	private Map<Long, StateData> states;
	private Map<Long, CityData> cities;
	
	public List<FranchiseeData> getFranchisees() {
		return franchisees;
	}
	
	public void setFranchisees(List<FranchiseeData> franchisees) {
		this.franchisees = franchisees;
	}

	public Map<Long, StateData> getStates() {
		return states;
	}

	public void setStates(Map<Long, StateData> states) {
		this.states = states;
	}

	public Map<Long, CityData> getCities() {
		return cities;
	}

	public void setCities(Map<Long, CityData> cities) {
		this.cities = cities;
	}

}
