package br.com.usasistemas.usaplatform.model.data;

import java.util.List;

public class DeliveryRequestStatusHistoryData {
	
	private List<DeliveryRequestStatusChangeData> items;

	public List<DeliveryRequestStatusChangeData> getItems() {
		return this.items;
	}

	public void setItems(List<DeliveryRequestStatusChangeData> items) {
		this.items = items;
	}
	
}
