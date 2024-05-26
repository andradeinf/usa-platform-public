package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.bizo.NotificationManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.dao.NotificationDAO;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.NotificationTypeEnum;

public class NotificationManagementBOImpl implements NotificationManagementBO {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(NotificationManagementBOImpl.class.getName());
	private NotificationDAO notificationDAO;
	private UserManagementBO userManagement;
	
	public NotificationDAO getNotificationDAO() {
		return notificationDAO;
	}
	
	public void setNotificationDAO(NotificationDAO notificationDAO) {
		this.notificationDAO = notificationDAO;
	}
	
	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	@Override
	public NotificationData createNotification(NotificationData notification) {
		
		notification.setDate(new Date());
		return notificationDAO.save(notification);
	}

	@Override
	public Long getMessageTopicUnreadNotificationsCount(UserProfileData user) {
		return Long.valueOf(getAllUnreadMessageTopicsNotifications(user).size());
	}
	
	@Override
	public List<NotificationData> getAllUnreadMessageTopicsNotifications(UserProfileData user) {
		return notificationDAO.findByUserDataAndTypeAndIsRead(user.getId(), user.getSelectedEntityId(), NotificationTypeEnum.MESSAGE_COMMENT, false);
	}

	@Override
	public List<NotificationData> getMessageTopicUnreadNotifications(Long messageTopicId, UserProfileData user) {
		return notificationDAO.findByReferenceIdAndUserAndIsRead(messageTopicId, user.getId(), user.getSelectedEntityId(), false);
	}

	@Override
	public void markNotificationsAsRead(List<Long> notificationsIds, UserProfileData user) {
		if (notificationsIds != null && !notificationsIds.isEmpty()) {
			for (Long notificationId : notificationsIds) {
				NotificationData notification = notificationDAO.findById(notificationId);
				notification.setIsRead(true);
				if (notification.getNotificationStatus() == MailNotificationStatusEnum.PENDING) {
					notification.setNotificationStatus(MailNotificationStatusEnum.CANCELLED);
				}
				notificationDAO.save(notification);
			}
		}		
	}

	@Override
	public List<NotificationData> getMessageCommentsPendingNotifications(String domainKey) {
		return notificationDAO.findByNotificationStatusAndType(MailNotificationStatusEnum.PENDING, NotificationTypeEnum.MESSAGE_COMMENT, domainKey);
	}
	
	@Override
	public List<NotificationData> getAnnouncementPendingNotifications(String domainKey) {
		return notificationDAO.findByNotificationStatusAndType(MailNotificationStatusEnum.PENDING, NotificationTypeEnum.ANNOUNCEMENT, domainKey);
	}

	@Override
	public List<NotificationData> updateNotificationStatus(List<NotificationData> notifications, MailNotificationStatusEnum notificationStatus) {
		
		for (NotificationData notification: notifications){
			notification.setNotificationStatus(notificationStatus);
		}

		return  notificationDAO.saveAll(notifications);
	}

	@Override
	public List<NotificationData> getAllMessageCommentNotifications(Long messageCommentId) {
		return notificationDAO.findByReferenceId2(messageCommentId);
	}

	@Override
	public List<NotificationData> getAllUnreadAnnouncementNotifications(UserProfileData user) {
		return notificationDAO.findByUserDataAndTypeAndIsRead(user.getId(), user.getSelectedEntityId(), NotificationTypeEnum.ANNOUNCEMENT, false);
	}
	
	@Override
	public NotificationData getAnnouncementUnreadNotification(Long announcementId, UserProfileData user) {
		
		List<NotificationData> notifications = notificationDAO.findByReferenceIdAndUserAndIsRead(announcementId, user.getId(), user.getSelectedEntityId(), false);
		if (notifications != null & !notifications.isEmpty()) {
			return notifications.get(0);
		}
		
		return null;
	}

	@Override
	public List<NotificationData> getFranchiseeAnnouncementNotifications(Long announcementId, Long franchiseeId) {
		return notificationDAO.findByReferenceIdAndEntityId(announcementId, franchiseeId);
	}

	@Override
	public Map<Long, PublicUserData> getNotificationUsers(List<NotificationData> notifications) {
		Map<Long, PublicUserData> response = new HashMap<Long, PublicUserData>();
		
		if (notifications != null & !notifications.isEmpty()) {
			for (NotificationData notification: notifications) {
				
				PublicUserData userData = userManagement.getUser(notification.getUserId());
				if (userData != null) {
					userData.setEmail(null);
					userData.setEnabled(null);
					userData.setUsername(null);
					userData.setSelectedRole(null);
					
					response.put(notification.getUserId(), userData);
				}				
			}
		}
		
		return response;	
	}
	
}
