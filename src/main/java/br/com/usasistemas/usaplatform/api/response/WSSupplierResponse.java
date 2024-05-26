package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.StateData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;

public class WSSupplierResponse extends GenericResponse {

	private SupplierData supplier;
	private StateData state;
	private CityData city;
	
	public SupplierData getSupplier() {
		return supplier;
	}
	
	public void setSupplier(SupplierData supplier) {
		this.supplier = supplier;
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
