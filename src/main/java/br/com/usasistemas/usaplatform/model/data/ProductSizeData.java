package br.com.usasistemas.usaplatform.model.data;

public class ProductSizeData {
	
	private Long id;
	private Long productId;
	private String name;
	private String description;
	private String groupName;
	private Double unitPrice;
	private Long deliveryQty;
	private Long manufactureMinQty;
	private Long minStock;
	private Long consolidatedStock;
	private Long currentStock;
	private Boolean isAvailable;
	private Boolean isActive;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Long getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Long currentStock) {
		this.currentStock = currentStock;
	}

	public Long getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(Long deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	public Long getManufactureMinQty() {
		return manufactureMinQty;
	}

	public void setManufactureMinQty(Long manufactureMinQty) {
		this.manufactureMinQty = manufactureMinQty;
	}

	public Long getMinStock() {
		return minStock;
	}

	public void setMinStock(Long minStock) {
		this.minStock = minStock;
	}

	public Long getConsolidatedStock() {
		return consolidatedStock;
	}

	public void setConsolidatedStock(Long consolidatedStock) {
		this.consolidatedStock = consolidatedStock;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
