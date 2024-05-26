package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;

public class WSDeliveryServiceResponse extends GenericResponse {

	List<DeliveryRequestData> deliveryRequests;
	List<DeliveryRequestData> newDeliveryRequests;

	public List<DeliveryRequestData> getDeliveryRequests() {
		return deliveryRequests;
	}

	public void setDeliveryRequests(List<DeliveryRequestData> deliveryRequests) {
		this.deliveryRequests = deliveryRequests;
	}

	public List<DeliveryRequestData> getNewDeliveryRequests() {
		return newDeliveryRequests;
	}

	public void setNewDeliveryRequests(List<DeliveryRequestData> newDeliveryRequests) {
		this.newDeliveryRequests = newDeliveryRequests;
	}
	
}
