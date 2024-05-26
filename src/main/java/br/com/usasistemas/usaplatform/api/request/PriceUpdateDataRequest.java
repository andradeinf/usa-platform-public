package br.com.usasistemas.usaplatform.api.request;

public class PriceUpdateDataRequest {
	
	private Long productSizeId;
	private Double unitPrice;
	
	public Long getProductSizeId() {
		return productSizeId;
	}

	public void setProductSizeId(Long productSizeId) {
		this.productSizeId = productSizeId;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
}
