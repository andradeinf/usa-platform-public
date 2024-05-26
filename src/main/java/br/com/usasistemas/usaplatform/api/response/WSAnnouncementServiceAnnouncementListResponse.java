package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.AnnouncementData;
import br.com.usasistemas.usaplatform.model.data.NotificationData;

public class WSAnnouncementServiceAnnouncementListResponse extends GenericResponse {

	private List<AnnouncementData> announcementList;
	private Map<Long, NotificationData> AnnouncementUnreadNotifications;
	private Boolean hasMore;

	public List<AnnouncementData> getAnnouncementList() {
		return announcementList;
	}

	public void setAnnouncementList(List<AnnouncementData> announcementList) {
		this.announcementList = announcementList;
	}

	public Map<Long, NotificationData> getAnnouncementUnreadNotifications() {
		return AnnouncementUnreadNotifications;
	}

	public void setAnnouncementUnreadNotifications(Map<Long, NotificationData> announcementUnreadNotifications) {
		AnnouncementUnreadNotifications = announcementUnreadNotifications;
	}

	public Boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(Boolean hasMore) {
		this.hasMore = hasMore;
	}

}
