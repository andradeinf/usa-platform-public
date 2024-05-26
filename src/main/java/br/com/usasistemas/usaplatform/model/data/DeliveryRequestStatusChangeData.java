package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;

import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;

public class DeliveryRequestStatusChangeData {
	
	private Date date;
	private Long userId;
	private DeliveryRequestStatusEnum previusStatus;
	private DeliveryRequestStatusEnum newStatus;	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public DeliveryRequestStatusEnum getPreviusStatus() {
		return this.previusStatus;
	}

	public String getPreviusStatusDescription() {
		return (previusStatus.getDescription());
	}

	public void setPreviusStatus(DeliveryRequestStatusEnum previusStatus) {
		this.previusStatus = previusStatus;
	}

	public DeliveryRequestStatusEnum getNewStatus() {
		return this.newStatus;
	}

	public String getNewStatusDescription() {
		return (newStatus.getDescription());
	}
	
	public void setNewStatus(DeliveryRequestStatusEnum newStatus) {
		this.newStatus = newStatus;
	}
	
}
