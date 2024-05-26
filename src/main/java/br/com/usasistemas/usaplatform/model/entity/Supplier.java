package br.com.usasistemas.usaplatform.model.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.usasistemas.usaplatform.model.enums.SupplierTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.SupplierVisibilityTypeEnum;

@Entity
@Index
public class Supplier {
	
	@Id private Long id;
	private String name;
	private String address;
	private Long stateId;
	private Long cityId;
	private String contactName;
	private String contactPhone;
	private SupplierTypeEnum type;
	private Long categoryId;
	private String imageKey;
	private String imageURL;
	private String segment; 
	private String contactEmail;
	private String description;
	private SupplierVisibilityTypeEnum visibility;
	private String preferedDomainKey;
	private Double qualityRate;
	private Long qualityCount;
	private Double deliveryRate;
	private Long deliveryCount;
	private Double priceRate;
	private Long priceCount;
	private Double paymentConditionRate;
	private Long paymentConditionCount;
	private String deliveryNotes;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public SupplierTypeEnum getType() {
		return type;
	}
	
	public String getTypeDescription() {
		if (type != null) {
			return type.getDescription();
		} else {
			return "";
		}
	}

	public void setType(SupplierTypeEnum type) {
		this.type = type;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SupplierVisibilityTypeEnum getVisibility() {
		return visibility;
	}

	public void setVisibility(SupplierVisibilityTypeEnum visibility) {
		this.visibility = visibility;
	}

	public String getPreferedDomainKey() {
		return preferedDomainKey;
	}

	public void setPreferedDomainKey(String preferedDomainKey) {
		this.preferedDomainKey = preferedDomainKey;
	}

	public Double getQualityRate() {
		return qualityRate;
	}

	public void setQualityRate(Double qualityRate) {
		this.qualityRate = qualityRate;
	}

	public Long getQualityCount() {
		return qualityCount;
	}

	public void setQualityCount(Long qualityCount) {
		this.qualityCount = qualityCount;
	}

	public Double getDeliveryRate() {
		return deliveryRate;
	}

	public void setDeliveryRate(Double deliveryRate) {
		this.deliveryRate = deliveryRate;
	}

	public Long getDeliveryCount() {
		return deliveryCount;
	}

	public void setDeliveryCount(Long deliveryCount) {
		this.deliveryCount = deliveryCount;
	}

	public Double getPriceRate() {
		return priceRate;
	}

	public void setPriceRate(Double priceRate) {
		this.priceRate = priceRate;
	}

	public Long getPriceCount() {
		return priceCount;
	}

	public void setPriceCount(Long priceCount) {
		this.priceCount = priceCount;
	}

	public Double getPaymentConditionRate() {
		return paymentConditionRate;
	}

	public void setPaymentConditionRate(Double paymentConditionRate) {
		this.paymentConditionRate = paymentConditionRate;
	}

	public Long getPaymentConditionCount() {
		return paymentConditionCount;
	}

	public void setPaymentConditionCount(Long paymentConditionCount) {
		this.paymentConditionCount = paymentConditionCount;
	}

	public String getDeliveryNotes() {
		return deliveryNotes;
	}

	public void setDeliveryNotes(String deliveryNotes) {
		this.deliveryNotes = deliveryNotes;
	}
	
}
