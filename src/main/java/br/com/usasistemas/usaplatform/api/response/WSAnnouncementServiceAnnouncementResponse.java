package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.AnnouncementData;

public class WSAnnouncementServiceAnnouncementResponse extends GenericResponse {

	private AnnouncementData announcement;

	public AnnouncementData getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(AnnouncementData announcement) {
		this.announcement = announcement;
	}

}
