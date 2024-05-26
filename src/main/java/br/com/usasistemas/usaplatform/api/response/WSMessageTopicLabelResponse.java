package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.MessageTopicLabelData;

public class WSMessageTopicLabelResponse extends GenericResponse {

	private MessageTopicLabelData messageTopicLabel;

	public MessageTopicLabelData getMessageTopicLabel() {
		return messageTopicLabel;
	}

	public void setMessageTopicLabel(MessageTopicLabelData messageTopicLabel) {
		this.messageTopicLabel = messageTopicLabel;
	}

}
