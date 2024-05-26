package br.com.usasistemas.usaplatform.api.request;

public class MoveMessageTopicRequest {
	
	private Long messageTopicId;
	private Long sourceLabelId;
	private Long targetLabelId;
	
	public Long getMessageTopicId() {
		return messageTopicId;
	}

	public void setMessageTopicId(Long messageTopicId) {
		this.messageTopicId = messageTopicId;
	}

	public Long getSourceLabelId() {
		return sourceLabelId;
	}

	public void setSourceLabelId(Long sourceLabelId) {
		this.sourceLabelId = sourceLabelId;
	}

	public Long getTargetLabelId() {
		return targetLabelId;
	}

	public void setTargetLabelId(Long targetLabelId) {
		this.targetLabelId = targetLabelId;
	}
	
}
