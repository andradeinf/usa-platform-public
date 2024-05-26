package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;

public class WSReviewRequestDetailsResponse extends GenericResponse {

	private List<DeliveryRequestHistoryData> deliveryRequests;
	private Map<Long, ProductData> products;
	
	public List<DeliveryRequestHistoryData> getDeliveryRequests() {
		return deliveryRequests;
	}

	public void setDeliveryRequests(List<DeliveryRequestHistoryData> deliveryRequests) {
		this.deliveryRequests = deliveryRequests;
	}

	public Map<Long, ProductData> getProducts() {
		return products;
	}

	public void setProducts(Map<Long, ProductData> products) {
		this.products = products;
	}

}
