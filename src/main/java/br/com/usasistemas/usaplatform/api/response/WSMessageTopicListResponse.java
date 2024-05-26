package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.EntityData;
import br.com.usasistemas.usaplatform.model.data.MessageTopicData;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;


public class WSMessageTopicListResponse extends GenericResponse {

	private List<MessageTopicData> messageTopics;
	private Map<Long, EntityData> entities;
	private Map<Long, UserGroupData> userGroups;
	private Map<Long, List<NotificationData>> messageTopicsUnreadNotifications;
	private Boolean hasMore;

	public List<MessageTopicData> getMessageTopics() {
		return messageTopics;
	}

	public void setMessageTopics(List<MessageTopicData> messageTopics) {
		this.messageTopics = messageTopics;
	}

	public Map<Long, EntityData> getEntities() {
		return entities;
	}

	public void setEntities(Map<Long, EntityData> entities) {
		this.entities = entities;
	}

	public Map<Long, UserGroupData> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Map<Long, UserGroupData> userGroups) {
		this.userGroups = userGroups;
	}

	public Map<Long, List<NotificationData>> getMessageTopicsUnreadNotifications() {
		return messageTopicsUnreadNotifications;
	}

	public void setMessageTopicsUnreadNotifications(Map<Long, List<NotificationData>> messageTopicsUnreadNotifications) {
		this.messageTopicsUnreadNotifications = messageTopicsUnreadNotifications;
	}

	public Boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(Boolean hasMore) {
		this.hasMore = hasMore;
	}

}
