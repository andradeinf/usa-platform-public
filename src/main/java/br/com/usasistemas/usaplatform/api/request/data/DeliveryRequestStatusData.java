package br.com.usasistemas.usaplatform.api.request.data;

public class DeliveryRequestStatusData {
	
	private Long deliveryRequestId;
	private Long quantity;
	private Double deliveryUnitPrice;

	public Long getDeliveryRequestId() {
		return deliveryRequestId;
	}

	public void setDeliveryRequestId(Long deliveryRequestId) {
		this.deliveryRequestId = deliveryRequestId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Double getDeliveryUnitPrice() {
		return deliveryUnitPrice;
	}

	public void setDeliveryUnitPrice(Double deliveryUnitPrice) {
		this.deliveryUnitPrice = deliveryUnitPrice;
	}

}
