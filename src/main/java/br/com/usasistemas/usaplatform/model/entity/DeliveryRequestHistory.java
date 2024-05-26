package br.com.usasistemas.usaplatform.model.entity;

import java.util.Date;

import com.google.gson.Gson;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.usasistemas.usaplatform.model.data.DeliveryRequestStatusHistoryData;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;

@Entity
@Index
public class DeliveryRequestHistory {
	
	@Id private Long id;
	private Date date;
	private Long supplierId;
	private Long franchiseeId;
	private Long productId;
	private Long productSizeId;
	private Double deliveryUnitPrice;
	private Long quantity;
	private DeliveryRequestStatusEnum status;
	private String cancellationComment;
	private Date sentDate;
	private Date deadlineDate;
	private Date updateDate;
	private MailNotificationStatusEnum notificationStatus;
	private Long stockConsolidationId;
	private String carrierName;
	private String trackingCode;
	private String fiscalNumber;
	private Date dueDate;
	private String domainKey;
	private String paymentSlipContentType;
	private String paymentSlipStorePath;
	private String paymentSlipKey;
	private String paymentSlipName;
	private String fiscalFileContentType;
	private String fiscalFileStorePath;
	private String fiscalFileKey;
	private String fiscalFileName;
	private String statusHistory;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

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

	public Double getDeliveryUnitPrice() {
		return deliveryUnitPrice;
	}

	public void setDeliveryUnitPrice(Double deliveryUnitPrice) {
		this.deliveryUnitPrice = deliveryUnitPrice;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public DeliveryRequestStatusEnum getStatus() {
		return status;
	}

	public void setStatus(DeliveryRequestStatusEnum status) {
		this.status = status;
	}

	public String getCancellationComment() {
		return cancellationComment;
	}

	public void setCancellationComment(String cancellationComment) {
		this.cancellationComment = cancellationComment;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public MailNotificationStatusEnum getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(MailNotificationStatusEnum notificationStatus) {
		this.notificationStatus = notificationStatus;
	}
	
	public Long getStockConsolidationId() {
		return stockConsolidationId;
	}

	public void setStockConsolidationId(Long stockConsolidationId) {
		this.stockConsolidationId = stockConsolidationId;
	}
	
	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

	public String getFiscalNumber() {
		return fiscalNumber;
	}

	public void setFiscalNumber(String fiscalNumber) {
		this.fiscalNumber = fiscalNumber;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getDomainKey() {
		return domainKey;
	}

	public void setDomainKey(String domainKey) {
		this.domainKey = domainKey;
	}

	public String getPaymentSlipContentType() {
		return paymentSlipContentType;
	}

	public void setPaymentSlipContentType(String paymentSlipContentType) {
		this.paymentSlipContentType = paymentSlipContentType;
	}

	public String getPaymentSlipStorePath() {
		return paymentSlipStorePath;
	}

	public void setPaymentSlipStorePath(String paymentSlipStorePath) {
		this.paymentSlipStorePath = paymentSlipStorePath;
	}

	public String getPaymentSlipKey() {
		return paymentSlipKey;
	}

	public void setPaymentSlipKey(String paymentSlipKey) {
		this.paymentSlipKey = paymentSlipKey;
	}

	public String getPaymentSlipName() {
		return paymentSlipName;
	}

	public void setPaymentSlipName(String paymentSlipName) {
		this.paymentSlipName = paymentSlipName;
	}

	public String getFiscalFileContentType() {
		return fiscalFileContentType;
	}

	public void setFiscalFileContentType(String fiscalFileContentType) {
		this.fiscalFileContentType = fiscalFileContentType;
	}

	public String getFiscalFileStorePath() {
		return fiscalFileStorePath;
	}

	public void setFiscalFileStorePath(String fiscalFileStorePath) {
		this.fiscalFileStorePath = fiscalFileStorePath;
	}

	public String getFiscalFileKey() {
		return fiscalFileKey;
	}

	public void setFiscalFileKey(String fiscalFileKey) {
		this.fiscalFileKey = fiscalFileKey;
	}

	public String getFiscalFileName() {
		return fiscalFileName;
	}

	public void setFiscalFileName(String fiscalFileName) {
		this.fiscalFileName = fiscalFileName;
	}

	public DeliveryRequestStatusHistoryData getStatusHistory() {
		Gson gson = new Gson();
		return gson.fromJson(this.statusHistory, DeliveryRequestStatusHistoryData.class);
	}

	public void setStatusHistory(DeliveryRequestStatusHistoryData statusHistory) {
		Gson gson = new Gson();
		this.statusHistory = gson.toJson(statusHistory);
	}
	
}
