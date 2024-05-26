package br.com.usasistemas.usaplatform.api.response;

public class WSMessageNotificationMenu extends GenericResponse {
	
	private Boolean receiveMessage;
	private Long newMessageCount;

	public Boolean getReceiveMessage() {
		return receiveMessage;
	}

	public void setReceiveMessage(Boolean receiveMessage) {
		this.receiveMessage = receiveMessage;
	}

	public Long getNewMessageCount() {
		return newMessageCount;
	}

	public void setNewMessageCount(Long newMessageCount) {
		this.newMessageCount = newMessageCount;
	}

}
