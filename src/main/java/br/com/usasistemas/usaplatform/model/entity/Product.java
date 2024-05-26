package br.com.usasistemas.usaplatform.model.entity;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import br.com.usasistemas.usaplatform.model.enums.ProductTypeEnum;

@Entity
@Index
public class Product {
	
	@Id private Long id;
	private Long franchisorId;
	private Long supplierId;
	private Long productCategoryId;
	private String name;
	private String description;
	@Unindex String longDescription;
	private Double unitPrice;
	private String unit;
	private Boolean hasDeliveryUnit;
	private String deliveryUnit;
	private Long deliveryQty;
	private String imageKey; 
	private String imageURL;
	private Boolean hasManufactureMinQty;
	private Long manufactureMinQty;
	private Long manufactureTime;
	private Long deliveryTime;
	private Long minStock;
	private String sizeName;
	private Boolean allowNegativeStock;
	private Boolean allowChangeUnitPrice;
	private Boolean allowSupplierChangeUnitPrice;
	private Boolean groupSizes;
	private ProductTypeEnum type;
	private Boolean isCatalog;
	private String catalogKey; 
	private String catalogName;
	private String catalogContentType;
	private String catalogStorePath;
	private Long catalogSize;
	private List<Long> favoriteFranchiseeUserIds;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFranchisorId() {
		return franchisorId;
	}
	
	public void setFranchisorId(Long franchisorId) {
		this.franchisorId = franchisorId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		if (longDescription == null) {
			return description;
		}		
		return longDescription;
	}

	public void setDescription(String description) {
		this.description = null;
		this.longDescription = description;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Boolean getHasManufactureMinQty() {
		return hasManufactureMinQty;
	}

	public void setHasManufactureMinQty(Boolean hasManufactureMinQty) {
		this.hasManufactureMinQty = hasManufactureMinQty;
	}

	public Boolean getHasDeliveryUnit() {
		return hasDeliveryUnit;
	}

	public void setHasDeliveryUnit(Boolean hasDeliveryUnit) {
		this.hasDeliveryUnit = hasDeliveryUnit;
	}

	public String getDeliveryUnit() {
		return deliveryUnit;
	}

	public void setDeliveryUnit(String deliveryUnit) {
		this.deliveryUnit = deliveryUnit;
	}

	public Long getDeliveryQty() {
		return deliveryQty;
	}

	public void setDeliveryQty(Long deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Long getManufactureMinQty() {
		return manufactureMinQty;
	}

	public void setManufactureMinQty(Long manufactureMinQty) {
		this.manufactureMinQty = manufactureMinQty;
	}

	public Long getManufactureTime() {
		return manufactureTime;
	}

	public void setManufactureTime(Long manufactureTime) {
		this.manufactureTime = manufactureTime;
	}

	public Long getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Long deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Long getMinStock() {
		return minStock;
	}

	public void setMinStock(Long minStock) {
		this.minStock = minStock;
	}
	
	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public Boolean getAllowNegativeStock() {
		return allowNegativeStock;
	}

	public void setAllowNegativeStock(Boolean allowNegativeStock) {
		this.allowNegativeStock = allowNegativeStock;
	}

	public Boolean getAllowChangeUnitPrice() {
		return allowChangeUnitPrice;
	}

	public void setAllowChangeUnitPrice(Boolean allowChangeUnitPrice) {
		this.allowChangeUnitPrice = allowChangeUnitPrice;
	}

	public Boolean getAllowSupplierChangeUnitPrice() {
		return allowSupplierChangeUnitPrice;
	}

	public void setAllowSupplierChangeUnitPrice(Boolean allowSupplierChangeUnitPrice) {
		this.allowSupplierChangeUnitPrice = allowSupplierChangeUnitPrice;
	}

	public Boolean getGroupSizes() {
		return groupSizes;
	}

	public void setGroupSizes(Boolean groupSizes) {
		this.groupSizes = groupSizes;
	}

	public ProductTypeEnum getType() {
		return type;
	}

	public void setType(ProductTypeEnum type) {
		this.type = type;
	}

	public Boolean getIsCatalog() {
		return isCatalog;
	}

	public void setIsCatalog(Boolean isCatalog) {
		this.isCatalog = isCatalog;
	}

	public String getCatalogKey() {
		return catalogKey;
	}

	public void setCatalogKey(String catalogKey) {
		this.catalogKey = catalogKey;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getCatalogContentType() {
		return catalogContentType;
	}

	public void setCatalogContentType(String catalogContentType) {
		this.catalogContentType = catalogContentType;
	}

	public String getCatalogStorePath() {
		return catalogStorePath;
	}

	public void setCatalogStorePath(String catalogStorePath) {
		this.catalogStorePath = catalogStorePath;
	}

	public Long getCatalogSize() {
		return catalogSize;
	}

	public void setCatalogSize(Long catalogSize) {
		this.catalogSize = catalogSize;
	}

	public List<Long> getFavoriteFranchiseeUserIds() {
		return favoriteFranchiseeUserIds;
	}

	public void setFavoriteFranchiseeUserIds(List<Long> favoriteFranchiseeUserIds) {
		this.favoriteFranchiseeUserIds = favoriteFranchiseeUserIds;
	}
}
