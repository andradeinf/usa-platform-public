package br.com.usasistemas.usaplatform.api.request;

import br.com.usasistemas.usaplatform.model.enums.ManufactureRequestStatusEnum;

public class WSUpdateManufactureRequestStatusRequest {
	
	private Long manufactureRequestId;
	private ManufactureRequestStatusEnum status;
	private String cancellationComment;

	public Long getManufactureRequestId() {
		return manufactureRequestId;
	}

	public void setManufactureRequestId(Long manufactureRequestId) {
		this.manufactureRequestId = manufactureRequestId;
	}

	public ManufactureRequestStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ManufactureRequestStatusEnum status) {
		this.status = status;
	}

	public String getCancellationComment() {
		return cancellationComment;
	}

	public void setCancellationComment(String cancellationComment) {
		this.cancellationComment = cancellationComment;
	}

}
