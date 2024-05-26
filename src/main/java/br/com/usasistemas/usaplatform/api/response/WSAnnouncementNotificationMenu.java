package br.com.usasistemas.usaplatform.api.response;

public class WSAnnouncementNotificationMenu extends GenericResponse {
	
	private Long newAnnouncementCount;

	public Long getNewAnnouncementCount() {
		return newAnnouncementCount;
	}

	public void setNewAnnouncementCount(Long newAnnouncementCount) {
		this.newAnnouncementCount = newAnnouncementCount;
	}

}
