package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.MessageCommentData;

public class WSMessageCommentResponse extends GenericResponse {

	MessageCommentData messageComment;

	public MessageCommentData getMessageComment() {
		return messageComment;
	}

	public void setMessageComment(MessageCommentData messageComment) {
		this.messageComment = messageComment;
	}

}
