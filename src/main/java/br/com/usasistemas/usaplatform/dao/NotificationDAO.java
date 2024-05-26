package br.com.usasistemas.usaplatform.dao;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.dao.response.NotificationPagedResponse;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.entity.Notification;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.NotificationTypeEnum;

public interface NotificationDAO extends GenericDAO<Notification, NotificationData> {

	public List<NotificationData> findByUserDataAndTypeAndIsRead(Long userId, Long entityId, NotificationTypeEnum notificationType, Boolean isRead);
	public List<NotificationData> findByReferenceIdAndUserAndIsRead(Long referenceId, Long userId, Long entityId, Boolean isRead);
	public List<NotificationData> findByReferenceId2AndUser(Long referenceId2, Long userId, Long entityId);
	public List<NotificationData> findByNotificationStatusAndType(MailNotificationStatusEnum status, NotificationTypeEnum type, String domainKey);
	public List<NotificationData> findByReferenceId2(Long referenceId2);
	public List<NotificationData> findByReferenceIdAndEntityId(Long referenceId, Long entityId);
	public List<NotificationData> findByReferenceId(Long referenceId);
	public NotificationPagedResponse findByIsReadAndTypeAndDateRange(Boolean isRead, NotificationTypeEnum type, Date startDate, Date endDate, Long pageSize, String cursorString);
	
}
