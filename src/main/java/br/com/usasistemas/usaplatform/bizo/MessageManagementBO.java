package br.com.usasistemas.usaplatform.bizo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.dao.response.MessageTopicPagedResponse;
import br.com.usasistemas.usaplatform.model.data.EntityData;
import br.com.usasistemas.usaplatform.model.data.MessageAttachmentData;
import br.com.usasistemas.usaplatform.model.data.MessageCommentData;
import br.com.usasistemas.usaplatform.model.data.MessageTopicData;
import br.com.usasistemas.usaplatform.model.data.MessageTopicLabelData;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public interface MessageManagementBO {

	public MessageTopicPagedResponse getUserMessageTopics(Long entityId, Long entityUserId, Long userGroupId, Date iniDate, Date endDate, Long pageSize, Long page, Map<String,String> queryParams);
	public MessageTopicData createMessageTopic(MessageTopicData messageTopic);
	public List<MessageTopicLabelData> getUserMessageTopicLabels(Long entityUserId);
	public MessageTopicLabelData saveMessageTopicLabel(MessageTopicLabelData messageTopicLabel);
	public MessageTopicLabelData deleteMessageTopicLabel(Long id);
	public void deleteMessageTopicLabelAsync(String labelKey);
	public void addLabelToMessageTopic(Long messageTopicId, Long entityUserId, Long messageTopicLabelId);
	public void archiveMessageTopic(Long messageTopicId, Long entityUserId);
	public void moveMessageTopic(Long messageTopicId, Long entityUserId, Long sourceLabelId, Long targetLabelId);
	public void removeLabelFromMessageTopic(Long messageTopicId, Long entityUserId, Long messageTopicLabelId);
	public List<EnumValidValueResponseData> getToProfileTypes(UserProfileData user, String domainKey);
	public List<EntityData> getToEntities(UserProfileEnum entityProfile, UserProfileData user);
	public List<UserGroupData> getToUserGroups(UserProfileEnum entityProfile, Long entityId, UserProfileData user);
	public Map<Long, EntityData> getMessageTopicEntities(List<MessageTopicData> messageTopics, UserProfileData user);
	public Map<Long, UserGroupData> getMessageTopicUserGroups(List<MessageTopicData> messageTopics, UserProfileData user);
	public MessageCommentData createMessageComment(MessageCommentData messageComment);
	public List<MessageCommentData> getMessageComments(Long messageTopicId);
	public Map<Long, PublicUserData> getMessageCommentUsers(List<MessageCommentData> messageComments);
	public void createMessageCommentAsync(Long messageCommentId);
	public Long getMessageTopicUnreadNotificationsCount(UserProfileData user, List<UserGroupData> receiveMessageGroup);
	public Map<Long, List<NotificationData>> getMessageTopicUnreadNotifications(List<MessageTopicData> messageTopics, UserProfileData user);
	public void markNotificationsAsRead(List<Long> notificationsIds, UserProfileData user);
	public List<NotificationData> getMessageCommentNotifications(Long messageCommentId);
	public Map<Long, PublicUserData> getMessageCommentNotificationUsers(List<NotificationData> messageCommentNotifications);
	public List<UserGroupData> getMessageUserGroups(UserProfileData user);
	public MessageAttachmentData createAttachment(UploadedFileData file);
	public MessageAttachmentData getAttachment(Long id);
	public void cleanUpUnusedAttachments(Long threshold);

}
