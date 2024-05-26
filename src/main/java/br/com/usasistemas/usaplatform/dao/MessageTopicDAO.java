package br.com.usasistemas.usaplatform.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.usasistemas.usaplatform.dao.response.MessageTopicPagedResponse;
import br.com.usasistemas.usaplatform.model.data.MessageTopicData;
import br.com.usasistemas.usaplatform.model.entity.MessageTopic;

public interface MessageTopicDAO extends GenericDAO<MessageTopic, MessageTopicData> {

	public static final String LABEL_FIELD = "label";
	public static final Long LABEL_INBOX = 0L;
	public static final Long LABEL_ALL = 99L;

	public MessageTopicPagedResponse findByGroupIds(Long entityUserId, List<Long> ids, Date iniDate, Date endDate, Long pageSize, Long page, Map<String,String> queryParams);
	public List<MessageTopicData> findByFromEntityId(Long fromEntityId);
	public String addSearchIndexDocument(MessageTopicData messageTopic, Set<Long> entityUserIds);
	public String addSearchIndexDocumentField(String documentId, String fieldName, String fieldValue);
	public String removeSearchIndexDocumentField(String documentId, String fieldName, String fieldValue);
	public String removeSearchIndexDocumentField(String documentId, String fieldName);
	public String replaceSearchIndexDocumentField(String documentId, String fieldName, String sourceFieldValue, String targetFieldValue);
	public void removeSearchIndexField(String fieldName, String fieldValue);
	public String buildLabelKey(Long entityUserId, Long messageLabelId);
	
}
