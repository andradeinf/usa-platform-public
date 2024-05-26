package br.com.usasistemas.usaplatform.dao.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;

public class DeliveryRequestHistoryPagedResponse extends BasePagedResponse {
	
	private List<DeliveryRequestHistoryData> deliveryRequests;

	public List<DeliveryRequestHistoryData> getDeliveryRequests() {
		return deliveryRequests;
	}

	public void setDeliveryRequests(List<DeliveryRequestHistoryData> deliveryRequests) {
		this.deliveryRequests = deliveryRequests;
	}

}
