package br.com.usasistemas.usaplatform.dao.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.MessageTopicData;

public class MessageTopicPagedResponse extends BasePagedResponse {
	
	private List<MessageTopicData> messageTopics;

	public List<MessageTopicData> getMessageTopics() {
		return messageTopics;
	}

	public void setMessageTopics(List<MessageTopicData> messageTopics) {
		this.messageTopics = messageTopics;
	}

}
