package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.StateData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;

public class WSSupplierListResponse extends GenericResponse {

	private List<SupplierData> suppliers;
	private Map<Long, StateData> states;
	private Map<Long, CityData> cities;
	
	public List<SupplierData> getSuppliers() {
		return suppliers;
	}
	
	public void setSuppliers(List<SupplierData> suppliers) {
		this.suppliers = suppliers;
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
