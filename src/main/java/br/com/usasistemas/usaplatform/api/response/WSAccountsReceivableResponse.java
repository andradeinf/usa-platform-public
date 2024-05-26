package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.ProductData;

public class WSAccountsReceivableResponse extends GenericResponse {

	private List<DeliveryRequestData> deliveryRequests;
	private Map<Long, ProductData> products;
	private Map<Long, FranchiseeData> franchisees;
	private Map<Long, FranchisorData>franchisors;
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

	public String getCursorString() {
		return cursorString;
	}

	public void setCursorString(String cursorString) {
		this.cursorString = cursorString;
	}

}
