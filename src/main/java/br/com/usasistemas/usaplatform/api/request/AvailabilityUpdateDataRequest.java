package br.com.usasistemas.usaplatform.api.request;

public class AvailabilityUpdateDataRequest {
	
	private Long productSizeId;
	private Boolean isAvailable;
	
	public Long getProductSizeId() {
		return productSizeId;
	}

	public void setProductSizeId(Long productSizeId) {
		this.productSizeId = productSizeId;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}	
}
