package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.QueryResults;
import com.googlecode.objectify.cmd.Query;

import br.com.usasistemas.usaplatform.dao.NotificationDAO;
import br.com.usasistemas.usaplatform.dao.response.NotificationPagedResponse;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.entity.Notification;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.NotificationTypeEnum;

public class GAENotificationDAOImpl extends GAEGenericDAOImpl<Notification, NotificationData> implements NotificationDAO {
	
	private static final Logger log = Logger.getLogger(GAENotificationDAOImpl.class.getName());

	@Override
	public List<NotificationData> findByUserDataAndTypeAndIsRead(Long userId, Long entityId, NotificationTypeEnum notificationType, Boolean isRead) {
		List<NotificationData> result = new ArrayList<NotificationData>();

		try {			
			List<Notification> notifications = ofy().load().type(Notification.class)
				.filter("userId", userId)
				.filter("entityId", entityId)
				.filter("type", notificationType)
				.filter("isRead", isRead)
				.list();
			if (notifications != null && !notifications.isEmpty())
				result = this.getConverter().convertToDataList(notifications);
		} catch (Exception e) {
			log.warning("Error when querying for Notifications by userId and userProfile and notificationType: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<NotificationData> findByReferenceIdAndUserAndIsRead(Long referenceId, Long userId, Long entityId, Boolean isRead) {
		List<NotificationData> result = new ArrayList<NotificationData>();

		try {
			List<Notification> notifications = ofy().load().type(Notification.class)
				.filter("referenceId", referenceId)
				.filter("userId", userId)
				.filter("entityId", entityId)
				.filter("isRead", isRead)
				.list();
			if (notifications != null && !notifications.isEmpty())
				result = this.getConverter().convertToDataList(notifications);
		} catch (Exception e) {
			log.warning("Error when querying for Notifications by referenceId and userData: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<NotificationData> findByReferenceId2AndUser(Long referenceId2, Long userId, Long entityId) {
		List<NotificationData> result = new ArrayList<NotificationData>();

		try {
			List<Notification> notifications = ofy().load().type(Notification.class)
				.filter("referenceId2", referenceId2)
				.filter("userId", userId)
				.filter("entityId", entityId)
				.list();
			if (notifications != null && !notifications.isEmpty())
				result = this.getConverter().convertToDataList(notifications);
		} catch (Exception e) {
			log.warning("Error when querying for Notifications by referenceId2 and userData: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<NotificationData> findByNotificationStatusAndType(MailNotificationStatusEnum status, NotificationTypeEnum type, String domainKey) {
		List<NotificationData> result = new ArrayList<NotificationData>();

		try {
			List<Notification> notifications = ofy().load().type(Notification.class)
				.filter("notificationStatus", status)
				.filter("type", type)
				.filter("domainKey", domainKey)
				.list();
			if (notifications != null && !notifications.isEmpty())
				result = this.getConverter().convertToDataList(notifications);
		} catch (Exception e) {
			log.warning("Error when querying for Notifications by notificationStatus: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<NotificationData> findByReferenceId2(Long referenceId2) {
		List<NotificationData> result = new ArrayList<NotificationData>();

		try {
			List<Notification> notifications = ofy().load().type(Notification.class)
				.filter("referenceId2", referenceId2)
				.list();
			if (notifications != null && !notifications.isEmpty())
				result = this.getConverter().convertToDataList(notifications);
		} catch (Exception e) {
			log.warning("Error when querying for Notifications by referenceId2: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<NotificationData> findByReferenceIdAndEntityId(Long referenceId, Long entityId) {
		List<NotificationData> result = new ArrayList<NotificationData>();

		try {
			List<Notification> notifications = ofy().load().type(Notification.class)
				.filter("referenceId", referenceId)
				.filter("entityId", entityId)
				.list();
			if (notifications != null && !notifications.isEmpty())
				result = this.getConverter().convertToDataList(notifications);
		} catch (Exception e) {
			log.warning("Error when querying for Notifications by referenceId and entityId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<NotificationData> findByReferenceId(Long referenceId) {
		List<NotificationData> result = new ArrayList<NotificationData>();

		try {
			List<Notification> notifications = ofy().load().type(Notification.class)
				.filter("referenceId", referenceId)
				.list();
			if (notifications != null && !notifications.isEmpty())
				result = this.getConverter().convertToDataList(notifications);
		} catch (Exception e) {
			log.warning("Error when querying for Notifications by referenceId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public NotificationPagedResponse findByIsReadAndTypeAndDateRange(Boolean isRead, NotificationTypeEnum type, Date startDate, Date endDate, Long pageSize, String cursorString) {
		NotificationPagedResponse result = new NotificationPagedResponse();

		try {		
			Query<Notification> q = ofy().load().type(Notification.class)
				.filter("isRead", isRead)
				.filter("type", type)
				.filter("date >=", startDate)
				.filter("date <=", endDate)
				.limit(pageSize.intValue())
				.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<Notification> iterator = q.iterator();
			if (q.iterator().hasNext()) {
				result.setNotifications(this.getConverter().convertToDataList(iterator, null));
				result.setCursorString(iterator.getCursorAfter().toUrlSafe());
			} else {
				result.setNotifications(new ArrayList<NotificationData>());
				result.setCursorString(null);
			}

		} catch (Exception e) {
			log.warning("Error when querying for Notifications by isRead and dateRange: " + e.toString());
		}			
	
		return result;
	}
	
}
