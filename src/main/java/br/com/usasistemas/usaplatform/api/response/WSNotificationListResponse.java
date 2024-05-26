package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;

public class WSNotificationListResponse extends GenericResponse {

	private List<NotificationData> notifications;
	private Map<Long, PublicUserData> users;
	
	public List<NotificationData> getNotifications() {
		return notifications;
	}
	
	public void setNotifications(List<NotificationData> notifications) {
		this.notifications = notifications;
	}
	
	public Map<Long, PublicUserData> getUsers() {
		return users;
	}
	
	public void setUsers(Map<Long, PublicUserData> users) {
		this.users = users;
	}

}
