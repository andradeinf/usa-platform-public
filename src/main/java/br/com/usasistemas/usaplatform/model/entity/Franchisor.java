package br.com.usasistemas.usaplatform.model.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class Franchisor {
	
	@Id private Long id;
	private String name;
	private String corporateName;
	private String fiscalId;
	private String address;
	private String contactName;
	private String contactPhone;	
	private String contactEmail;
	private String imageKey; 
	private String imageURL;
	private String loginURL;
	private String radioURL;
	private Long paymentTime;
	private Long maxStorageSizeMB;
	private String additionalInformation;
	private String preferedDomainKey;
	private Boolean flagAnnouncement;
	private Boolean flagCalendar;
	private Boolean flagDocuments;
	private Boolean flagTraining;

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

	public String getCorporateName() {
		return corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public String getFiscalId() {
		return fiscalId;
	}

	public void setFiscalId(String fiscalId) {
		this.fiscalId = fiscalId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
	
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
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
	
	public String getLoginURL() {
		return loginURL;
	}

	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}

	public String getRadioURL() {
		return radioURL;
	}

	public void setRadioURL(String radioUrl) {
		this.radioURL = radioUrl;
	}

	public Long getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Long paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Long getMaxStorageSizeMB() {
		return maxStorageSizeMB;
	}

	public void setMaxStorageSizeMB(Long maxStorageSizeMB) {
		this.maxStorageSizeMB = maxStorageSizeMB;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public String getPreferedDomainKey() {
		return preferedDomainKey;
	}

	public void setPreferedDomainKey(String preferedDomainKey) {
		this.preferedDomainKey = preferedDomainKey;
	}

	public Boolean getFlagAnnouncement() {
		return this.flagAnnouncement;
	}

	public void setFlagAnnouncement(Boolean flagAnnouncement) {
		this.flagAnnouncement = flagAnnouncement;
	}

	public Boolean getFlagCalendar() {
		return this.flagCalendar;
	}

	public void setFlagCalendar(Boolean flagCalendar) {
		this.flagCalendar = flagCalendar;
	}

	public Boolean getFlagDocuments() {
		return this.flagDocuments;
	}

	public void setFlagDocuments(Boolean flagDocuments) {
		this.flagDocuments = flagDocuments;
	}

	public Boolean getFlagTraining() {
		return this.flagTraining;
	}
	
	public void setFlagTraining(Boolean flagTraining) {
		this.flagTraining = flagTraining;
	}

}
