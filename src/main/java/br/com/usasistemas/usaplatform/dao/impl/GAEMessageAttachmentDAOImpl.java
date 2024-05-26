package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.MessageAttachmentDAO;
import br.com.usasistemas.usaplatform.model.data.MessageAttachmentData;
import br.com.usasistemas.usaplatform.model.entity.MessageAttachment;

public class GAEMessageAttachmentDAOImpl extends GAEGenericDAOImpl<MessageAttachment, MessageAttachmentData> implements MessageAttachmentDAO {
	
	private static final Logger log = Logger.getLogger(GAEMessageAttachmentDAOImpl.class.getName());

	@Override
	public List<MessageAttachmentData> findUnusedByDate(Date date) {
		List<MessageAttachmentData> result = new ArrayList<MessageAttachmentData>();
		
		try {
			List<MessageAttachment> messageAttachments = ofy().load().type(MessageAttachment.class)
				.filter("date <=", date)
				.filter("messageCommentId", null)
				.list();
			if (messageAttachments != null && !messageAttachments.isEmpty())
				result = this.getConverter().convertToDataList(messageAttachments);
		} catch (Exception e) {
			log.warning("Error when querying for MessageAttachment by dateDate: " + e.toString());
		}	
		
		return result;
	}

	
}
