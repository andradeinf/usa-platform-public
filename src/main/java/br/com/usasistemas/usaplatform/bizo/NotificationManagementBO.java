package br.com.usasistemas.usaplatform.bizo;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;

public interface NotificationManagementBO {

	public NotificationData createNotification(NotificationData notification);
	public Long getMessageTopicUnreadNotificationsCount(UserProfileData user);
	public List<NotificationData> getMessageTopicUnreadNotifications(Long messageTopicId, UserProfileData user);
	public List<NotificationData> getAllUnreadMessageTopicsNotifications(UserProfileData user);
	public void markNotificationsAsRead(List<Long> notificationsIds, UserProfileData user);
	public List<NotificationData> getMessageCommentsPendingNotifications(String domainKey);
	public List<NotificationData> updateNotificationStatus(List<NotificationData> notifications, MailNotificationStatusEnum notificationStatus);
	public List<NotificationData> getAllMessageCommentNotifications(Long messageCommentId);
	public List<NotificationData> getAllUnreadAnnouncementNotifications(UserProfileData user);
	public NotificationData getAnnouncementUnreadNotification(Long messageTopicId, UserProfileData user);
	public List<NotificationData> getAnnouncementPendingNotifications(String domainKey);
	public List<NotificationData> getFranchiseeAnnouncementNotifications(Long announcementId, Long franchiseeId);
	public Map<Long, PublicUserData> getNotificationUsers(List<NotificationData> notifications);

}
