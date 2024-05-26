package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.MessageTopicLabelData;

public class WSMessageTopicLabelListResponse extends GenericResponse {

	private List<MessageTopicLabelData> messageTopicLabels;

	public List<MessageTopicLabelData> getMessageTopicLabels() {
		return messageTopicLabels;
	}

	public void setMessageTopicLabels(List<MessageTopicLabelData> messageTopicLabels) {
		this.messageTopicLabels = messageTopicLabels;
	}

}
