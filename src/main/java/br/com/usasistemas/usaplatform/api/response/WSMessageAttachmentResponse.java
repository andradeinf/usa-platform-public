package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.MessageAttachmentData;

public class WSMessageAttachmentResponse extends GenericResponse {

	private MessageAttachmentData attachment;

	public MessageAttachmentData getAttachment() {
		return attachment;
	}

	public void setAttachment(MessageAttachmentData attachment) {
		this.attachment = attachment;
	}
		
}
