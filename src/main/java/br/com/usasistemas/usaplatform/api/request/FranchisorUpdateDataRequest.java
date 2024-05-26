package br.com.usasistemas.usaplatform.api.request;

public class FranchisorUpdateDataRequest {
	
	private Long productSizeId;
	private Long minStock;
	
	public Long getProductSizeId() {
		return productSizeId;
	}

	public void setProductSizeId(Long productSizeId) {
		this.productSizeId = productSizeId;
	}

	public Long getMinStock() {
		return minStock;
	}

	public void setMinStock(Long minStock) {
		this.minStock = minStock;
	}

}
