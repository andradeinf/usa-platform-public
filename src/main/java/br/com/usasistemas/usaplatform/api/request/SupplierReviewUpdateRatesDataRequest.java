package br.com.usasistemas.usaplatform.api.request;

public class SupplierReviewUpdateRatesDataRequest {
	
	private Long quality;
	private Long delivery;
	private Long price;
	private Long paymentCondition;
	
	public Long getQuality() {
		return quality;
	}
	
	public void setQuality(Long quality) {
		this.quality = quality;
	}
	
	public Long getDelivery() {
		return delivery;
	}
	
	public void setDelivery(Long delivery) {
		this.delivery = delivery;
	}
	
	public Long getPrice() {
		return price;
	}
	
	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getPaymentCondition() {
		return paymentCondition;
	}

	public void setPaymentCondition(Long paymentCondition) {
		this.paymentCondition = paymentCondition;
	}
}
