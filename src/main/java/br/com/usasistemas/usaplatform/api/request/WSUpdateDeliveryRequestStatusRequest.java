package br.com.usasistemas.usaplatform.api.request;

import java.util.List;

import br.com.usasistemas.usaplatform.api.request.data.DeliveryRequestStatusData;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;

public class WSUpdateDeliveryRequestStatusRequest {
	
	private DeliveryRequestStatusEnum status;
	private String cancellationComment;
	private String sentDate;
	private String deadlineDate;
	private String carrierName;
	private String trackingCode;
	private String fiscalNumber;
	private String dueDate;
	private String autoCancellationDate;
	private List<DeliveryRequestStatusData> deliveryRequests;
	private Long paymentSlipId;
	private Long fiscalFileId;
	
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

	public String getSentDate() {
		return sentDate;
	}

	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}

	public String getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(String deadlineDate) {
		this.deadlineDate = deadlineDate;
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

	public String getAutoCancellationDate() {
		return autoCancellationDate;
	}

	public void setAutoCancellationDate(String autoCancellationDate) {
		this.autoCancellationDate = autoCancellationDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public List<DeliveryRequestStatusData> getDeliveryRequests() {
		return deliveryRequests;
	}

	public void setDeliveryRequests(List<DeliveryRequestStatusData> deliveryRequests) {
		this.deliveryRequests = deliveryRequests;
	}

	public Long getPaymentSlipId() {
		return paymentSlipId;
	}

	public void setPaymentSlipId(Long paymentSlipId) {
		this.paymentSlipId = paymentSlipId;
	}

	public Long getFiscalFileId() {
		return fiscalFileId;
	}

	public void setFiscalFileId(Long fiscalFileId) {
		this.fiscalFileId = fiscalFileId;
	}

}
