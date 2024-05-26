package br.com.usasistemas.usaplatform.dao.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.AnnouncementData;

public class AnnouncementPagedResponse extends BasePagedResponse {
	
	private List<AnnouncementData> announcements;

	public List<AnnouncementData> getAnnouncements() {
		return announcements;
	}

	public void setAnnouncements(List<AnnouncementData> announcements) {
		this.announcements = announcements;
	}

}
