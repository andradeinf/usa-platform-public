package br.com.usasistemas.usaplatform.dao;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.model.data.MessageAttachmentData;
import br.com.usasistemas.usaplatform.model.entity.MessageAttachment;

public interface MessageAttachmentDAO extends GenericDAO<MessageAttachment, MessageAttachmentData> {

	public List<MessageAttachmentData> findUnusedByDate(Date date);
	
}
