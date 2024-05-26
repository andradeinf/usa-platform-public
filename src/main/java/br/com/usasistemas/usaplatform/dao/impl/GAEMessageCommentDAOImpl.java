package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.MessageCommentDAO;
import br.com.usasistemas.usaplatform.model.data.MessageCommentData;
import br.com.usasistemas.usaplatform.model.entity.MessageComment;

public class GAEMessageCommentDAOImpl extends GAEGenericDAOImpl<MessageComment, MessageCommentData> implements MessageCommentDAO {
	
	private static final Logger log = Logger.getLogger(GAEMessageCommentDAOImpl.class.getName());

	@Override
	public List<MessageCommentData> findByMessageTopicId(Long messageTopicId) {
		List<MessageCommentData> result = new ArrayList<MessageCommentData>();

		try {
			List<MessageComment> messageComments = ofy().load().type(MessageComment.class)
				.filter("messageTopicId", messageTopicId)
				.list();
			if (messageComments != null && !messageComments.isEmpty())
				result = this.getConverter().convertToDataList(messageComments);
		} catch (Exception e) {
			log.warning("Error when querying for MessageComments by messageTopicId: " + e.toString());
		}	
	
		return result;
	}
	
}
