package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;

public class WSAccountsPayableResponse extends GenericResponse {

	private List<DeliveryRequestData> deliveryRequests;
	private Map<Long, ProductData> products;
	private Map<Long, SupplierData> suppliers;
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

	public String getCursorString() {
		return cursorString;
	}

	public void setCursorString(String cursorString) {
		this.cursorString = cursorString;
	}

}
