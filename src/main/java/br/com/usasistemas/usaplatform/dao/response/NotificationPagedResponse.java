package br.com.usasistemas.usaplatform.dao.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.NotificationData;

public class NotificationPagedResponse extends BasePagedResponse {
	
	private List<NotificationData> notifications;

	public List<NotificationData> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<NotificationData> notifications) {
		this.notifications = notifications;
	}

}
