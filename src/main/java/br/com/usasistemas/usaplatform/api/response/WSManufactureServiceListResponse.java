package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.data.ProductData;

public class WSManufactureServiceListResponse extends GenericResponse {

	private List<ManufactureRequestData> manufactureRequests;
	private Map<Long, ProductData> products;
	private Map<Long, FranchisorData> franchisors;
	private String cursorString;

	public List<ManufactureRequestData> getManufactureRequests() {
		return manufactureRequests;
	}

	public void setManufactureRequests(List<ManufactureRequestData> manufactureRequests) {
		this.manufactureRequests = manufactureRequests;
	}

	public Map<Long, ProductData> getProducts() {
		return products;
	}

	public void setProducts(Map<Long, ProductData> products) {
		this.products = products;
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
