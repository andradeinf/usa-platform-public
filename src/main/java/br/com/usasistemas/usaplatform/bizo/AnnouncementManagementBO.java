package br.com.usasistemas.usaplatform.bizo;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.dao.response.AnnouncementPagedResponse;
import br.com.usasistemas.usaplatform.model.data.AnnouncementData;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;


public interface AnnouncementManagementBO {
	
	public AnnouncementPagedResponse getFranchisorAnnouncements(Long franchisorId, UserProfileData user, Long pageSize, Long page);
	public AnnouncementData saveAnnouncement(AnnouncementData announcement);
	public AnnouncementData deleteAnnouncement(Long id);
	public void createAnnouncementAsync(Long announcementId);
	public Long getAnnouncementUnreadNotificationsCount(UserProfileData user);
	public Map<Long, NotificationData> getAnnouncementUnreadNotifications(List<AnnouncementData> announcementList, UserProfileData user);
	public void markNotificationsAsRead(List<Long> notificationsIds, UserProfileData user);
	public AnnouncementData publishAnnouncement(AnnouncementData announcement);
	public List<NotificationData> getAnnouncemnetNotifications(Long announcementId, Long franchiseeId);
	public Map<Long, PublicUserData> getAnnouncementNotificationUsers(List<NotificationData> announcementNotifications);
	
}
