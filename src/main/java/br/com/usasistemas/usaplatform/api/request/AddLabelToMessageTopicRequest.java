package br.com.usasistemas.usaplatform.api.request;

public class AddLabelToMessageTopicRequest {
	
	private Long messageTopicId;
	private Long messageTopicLabelId;
	
	public Long getMessageTopicId() {
		return messageTopicId;
	}

	public void setMessageTopicId(Long messageTopicId) {
		this.messageTopicId = messageTopicId;
	}

	public Long getMessageTopicLabelId() {
		return messageTopicLabelId;
	}

	public void setMessageTopicLabelId(Long messageTopicLabelId) {
		this.messageTopicLabelId = messageTopicLabelId;
	}
	
}
