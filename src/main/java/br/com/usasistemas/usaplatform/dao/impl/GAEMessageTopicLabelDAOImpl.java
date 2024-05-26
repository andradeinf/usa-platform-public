package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.MessageTopicLabelDAO;
import br.com.usasistemas.usaplatform.model.data.MessageTopicLabelData;
import br.com.usasistemas.usaplatform.model.entity.MessageTopicLabel;

public class GAEMessageTopicLabelDAOImpl extends GAEGenericDAOImpl<MessageTopicLabel, MessageTopicLabelData> implements MessageTopicLabelDAO {
	
	private static final Logger log = Logger.getLogger(GAEMessageTopicLabelDAOImpl.class.getName());

	
	@Override
	public List<MessageTopicLabelData> findByEntityUserId(Long entityUserId) {
		List<MessageTopicLabelData> result = new ArrayList<MessageTopicLabelData>();
		
		try {
		  List<MessageTopicLabel> messageTopicLabels = ofy().load().type(MessageTopicLabel.class)
				.filter("entityUserId", entityUserId)
				.order("name")
				.list();
	      if ((messageTopicLabels != null) && (!messageTopicLabels.isEmpty()))
	        result = getConverter().convertToDataList(messageTopicLabels);
	    } catch (Exception e) {
	      log.warning("Error when querying for MessageTopicLabels by entityUserId : " + e.toString());
	    }
		
		return result;	
	}
	
}
