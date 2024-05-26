package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;

public class WSDeliveryServiceCreateResponse extends GenericResponse {

	DeliveryRequestData deliveryRequest;

	public DeliveryRequestData getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequestData deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

}
