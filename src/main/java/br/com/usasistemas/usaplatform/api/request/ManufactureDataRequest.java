package br.com.usasistemas.usaplatform.api.request;

public class ManufactureDataRequest {
	
	private Long franchisorId;
	private Long productId;
	private Long productSizeId;
	private Long quantity;
	private Boolean addStockOnly;
	
	public Long getFranchisorId() {
		return franchisorId;
	}

	public void setFranchisorId(Long franchisorId) {
		this.franchisorId = franchisorId;
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

	public Boolean getAddStockOnly() {
		return addStockOnly;
	}

	public void setAddStockOnly(Boolean addStockOnly) {
		this.addStockOnly = addStockOnly;
	}



}
