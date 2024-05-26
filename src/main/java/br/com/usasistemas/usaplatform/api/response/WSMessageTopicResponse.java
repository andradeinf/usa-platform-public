package br.com.usasistemas.usaplatform.api.response;

import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.EntityData;
import br.com.usasistemas.usaplatform.model.data.MessageTopicData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;

public class WSMessageTopicResponse extends GenericResponse {

	MessageTopicData messageTopic;
	Map<Long, EntityData> entities;
	Map<Long, UserGroupData> userGroups;

	public MessageTopicData getMessageTopic() {
		return messageTopic;
	}

	public void setMessageTopic(MessageTopicData messageTopic) {
		this.messageTopic = messageTopic;
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
	
	

}
