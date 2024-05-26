package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.StateData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;

public class WSDeliveryServiceListResponse extends GenericResponse {

	private List<DeliveryRequestData> deliveryRequests;
	private Map<Long, ProductData> products;
	private Map<Long, SupplierData> suppliers;
	private Map<Long, FranchiseeData> franchisees;
	private Map<Long, FranchisorData> franchisors;
	private Map<Long, StateData> states;
	private Map<Long, CityData> cities;
	private String cursorString;

	public List<DeliveryRequestData> getDeliveryRequests() {
		return deliveryRequests;
	}

	public void setDeliveryRequests(List<DeliveryRequestData> deliveryRequests) {
		this.deliveryRequests = deliveryRequests;
	}

	public Map<Long, ProductData> getProducts() {
		return products;
	}

	public void setProducts(Map<Long, ProductData> products) {
		this.products = products;
	}

	public Map<Long, SupplierData> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(Map<Long, SupplierData> suppliers) {
		this.suppliers = suppliers;
	}	
	
	public Map<Long, FranchiseeData> getFranchisees() {
		return franchisees;
	}

	public void setFranchisees(Map<Long, FranchiseeData> franchisees) {
		this.franchisees = franchisees;
	}

	public Map<Long, FranchisorData> getFranchisors() {
		return franchisors;
	}

	public void setFranchisors(Map<Long, FranchisorData> franchisors) {
		this.franchisors = franchisors;
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

	public String getCursorString() {
		return cursorString;
	}

	public void setCursorString(String cursorString) {
		this.cursorString = cursorString;
	}

}
