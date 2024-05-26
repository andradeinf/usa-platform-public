package br.com.usasistemas.usaplatform.dao.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;

public class DeliveryRequestPagedResponse extends BasePagedResponse {
	
	private List<DeliveryRequestData> deliveryRequests;

	public List<DeliveryRequestData> getDeliveryRequests() {
		return deliveryRequests;
	}

	public void setDeliveryRequests(List<DeliveryRequestData> deliveryRequests) {
		this.deliveryRequests = deliveryRequests;
	}

}
