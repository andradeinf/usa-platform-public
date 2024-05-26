package br.com.usasistemas.usaplatform.api.request;

public class DeliveryDataRequest {
	
	private Long franchiseeId;
	private Long productId;
	private Long productSizeId;
	private Long quantity;
	
	public Long getFranchiseeId() {
		return franchiseeId;
	}

	public void setFranchiseeId(Long franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getProductSizeId() {
		return productSizeId;
	}

	public void setProductSizeId(Long productSizeId) {
		this.productSizeId = productSizeId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

}
