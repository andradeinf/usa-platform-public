package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import br.com.usasistemas.usaplatform.bizo.AnnouncementManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.NotificationManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.dao.AnnouncementDAO;
import br.com.usasistemas.usaplatform.dao.response.AnnouncementPagedResponse;
import br.com.usasistemas.usaplatform.model.data.AnnouncementData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.AnnouncementStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.NotificationTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class AnnouncementManagementBOImpl implements AnnouncementManagementBO {
	
	private static final Logger log = Logger.getLogger(AnnouncementManagementBOImpl.class.getName());
	private AnnouncementDAO announcementDAO;
	private FranchiseeManagementBO franchiseeManagement;
	private UserManagementBO userManagement;
	private NotificationManagementBO notificationManagement;

	public AnnouncementDAO getAnnouncementDAO() {
		return announcementDAO;
	}

	public void setAnnouncementDAO(AnnouncementDAO announcementDAO) {
		this.announcementDAO = announcementDAO;
	}

	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	public NotificationManagementBO getNotificationManagement() {
		return notificationManagement;
	}

	public void setNotificationManagement(NotificationManagementBO notificationManagement) {
		this.notificationManagement = notificationManagement;
	}

	@Override
	public AnnouncementPagedResponse getFranchisorAnnouncements(Long franchisorId, UserProfileData user, Long pageSize, Long page) {
		
		AnnouncementPagedResponse response = null;
		
		if (user.getSelectedRole() == UserProfileEnum.FRANCHISOR) {
			response = this.announcementDAO.findByFranchisorId(franchisorId, pageSize, page);
		} else {
			List<Long> franchiseeIds = new ArrayList<Long>();
			franchiseeIds.add(user.getSelectedEntityId());
			franchiseeIds.add(0L); //filter announcements without franchisee restrictions
			
			response = this.announcementDAO.findByFranchisorIdAndFranchiseeIdsAndSatus(franchisorId, franchiseeIds, AnnouncementStatusEnum.PUBLISHED, pageSize, page);
		}		
		
		//no records found. return with empty array
		if (response.getAnnouncements() == null) {
			response.setAnnouncements(new ArrayList<AnnouncementData>());
		} else {
			for (AnnouncementData announcement : response.getAnnouncements()){
				//remove franchiseeIds if only the franchisee code Zero is there (to allow select without restriction)
				removeFranchiseeZero(announcement);
			}
		}
		
		return response;
	}	
	
	@Override
	public AnnouncementData saveAnnouncement(AnnouncementData announcement) {
		
		if (announcement.getId() == null) {
			announcement.setStatus(AnnouncementStatusEnum.DRAFT);
		}	
		announcement.setDate(new Date());

		announcement = this.announcementDAO.save(announcement);
		
		return announcement;
	}
	
	@Override
	public AnnouncementData publishAnnouncement(AnnouncementData announcement) {
		
		announcement.setStatus(AnnouncementStatusEnum.PUBLISHED);
		announcement.setDate(new Date());
		
		//Add franchisee zero to allow search of announcements without filter (records that has no restriction)
		List<Long> franchiseeIds = announcement.getFranchiseeIds();
		if (franchiseeIds == null || franchiseeIds.isEmpty()) {
			franchiseeIds = new ArrayList<Long>();
			franchiseeIds.add(0L);
			announcement.setFranchiseeIds(franchiseeIds);
		}
		
		announcement = this.announcementDAO.save(announcement);
		
		//remove franchiseeIds if only the franchisee code Zero is there (to allow select without restriction)
		removeFranchiseeZero(announcement);
		
		//Asynchronously process announcement publishing
		Queue queue = QueueFactory.getQueue("messages");
		queue.add(TaskOptions.Builder.withUrl("/job/async/newAnnouncement/"+announcement.getId()));
		
		return announcement;
	}
	
	private void removeFranchiseeZero(AnnouncementData announcement){
		
		//remove franchiseeIds if only the franchisee code Zero is there (to allow select without restriction)
		if (announcement.getFranchiseeIds() != null && !announcement.getFranchiseeIds().isEmpty()
				&& announcement.getFranchiseeIds().size() == 1 && announcement.getFranchiseeIds().get(0) == 0L){
			announcement.setFranchiseeIds(new ArrayList<Long>());
		}
	}
	
	@Override
	public AnnouncementData deleteAnnouncement(Long id) {
		return this.announcementDAO.delete(id);
	}

	@Override
	public void createAnnouncementAsync(Long announcementId) {
		
		log.info("Process new Announcement Async!");
		
		AnnouncementData announcement = this.announcementDAO.findById(announcementId);

		// Add announcement to search index
		log.info("Add announcement to search index...");
		this.announcementDAO.addSearchIndexDocument(announcement);
		
		// Create notifications
		List<Long> franchiseeIds = announcement.getFranchiseeIds();
		if (franchiseeIds.size() == 1 && franchiseeIds.get(0) == 0L) {
			//create notification for all franchisee users
			log.info("create notification for all franchisee users...");
			List<FranchiseeData> franchisees = franchiseeManagement.getFranchiseesByFranchisorId(announcement.getFranchisorId());
			if (franchisees != null && !franchisees.isEmpty()) {
				for (FranchiseeData franchisee : franchisees) {
					createFranchiseeNotifications(franchisee.getId(), announcement);
				}
			} else {
				log.info("No Franchisees found");
			}
		} else {
			//create notifications only for restricted franchisees
			log.info("create notifications only for restricted franchisees...");
			for (Long franchiseeId: franchiseeIds) {
				createFranchiseeNotifications(franchiseeId, announcement);
			}
		}
	}
	
	private void createFranchiseeNotifications(Long franchiseeId, AnnouncementData announcement) {
		
		List<FranchiseeUserData> franchiseeUsers = franchiseeManagement.getFranchiseeUsers(franchiseeId);
		if (franchiseeUsers != null && !franchiseeUsers.isEmpty()) {
			log.info("createFranchiseeNotifications for : " + franchiseeUsers.size() + " users found");
			for (FranchiseeUserData franchiseeUser : franchiseeUsers) {
				
				NotificationData notification = new NotificationData();
				notification.setType(NotificationTypeEnum.ANNOUNCEMENT);
				notification.setReferenceId(announcement.getId());
				notification.setEntityProfile(UserProfileEnum.FRANCHISEE);
				notification.setEntityId(franchiseeUser.getFranchiseeId());
				notification.setUserId(franchiseeUser.getUserId());
				notification.setDomainKey(announcement.getDomainKey());
				notification.setIsRead(false);
				notification.setNotificationStatus(MailNotificationStatusEnum.PENDING);
				
				notificationManagement.createNotification(notification);				
			}
		}
	}

	@Override
	public Long getAnnouncementUnreadNotificationsCount(UserProfileData user) {
		Long result = 0L;
		
		// only franchisee users have notifications
		if (user.getSelectedRole() == UserProfileEnum.FRANCHISEE) {
			List<NotificationData> notifications = notificationManagement.getAllUnreadAnnouncementNotifications(user);
			if (notifications != null && !notifications.isEmpty()) {
				result = Long.valueOf(notifications.size());
			}
		}		
		
		return result;
	}

	@Override
	public Map<Long, NotificationData> getAnnouncementUnreadNotifications(List<AnnouncementData> announcementList, UserProfileData user) {
		
		Map<Long, NotificationData> response = new HashMap<Long, NotificationData>();
		
		// only franchisee users have notifications
		if (user.getSelectedRole() == UserProfileEnum.FRANCHISEE &&
			announcementList != null & !announcementList.isEmpty()) {
			for (AnnouncementData announcement: announcementList) {
				NotificationData unreadNotification = notificationManagement.getAnnouncementUnreadNotification(announcement.getId(), user);
				if (unreadNotification != null) {
					response.put(announcement.getId(), unreadNotification);
				}
			}
		}
		
		return response;
	}

	@Override
	public void markNotificationsAsRead(List<Long> notificationsIds, UserProfileData user) {
		notificationManagement.markNotificationsAsRead(notificationsIds, user);
	}

	@Override
	public List<NotificationData> getAnnouncemnetNotifications(Long announcementId, Long franchiseeId) {
		return notificationManagement.getFranchiseeAnnouncementNotifications(announcementId, franchiseeId);
	}

	@Override
	public Map<Long, PublicUserData> getAnnouncementNotificationUsers(List<NotificationData> announcementNotifications) {
		return notificationManagement.getNotificationUsers(announcementNotifications);
	}
}
