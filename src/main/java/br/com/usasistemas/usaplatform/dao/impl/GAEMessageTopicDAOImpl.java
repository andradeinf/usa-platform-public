package br.com.usasistemas.usaplatform.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;

import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.common.exception.InfrastructureException;
import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.dao.MessageTopicDAO;
import br.com.usasistemas.usaplatform.dao.response.MessageTopicPagedResponse;
import br.com.usasistemas.usaplatform.model.data.MessageTopicData;
import br.com.usasistemas.usaplatform.model.entity.MessageTopic;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

public class GAEMessageTopicDAOImpl extends GAEGenericDAOImpl<MessageTopic, MessageTopicData> implements MessageTopicDAO {
	
	private static final Logger log = Logger.getLogger(GAEMessageTopicDAOImpl.class.getName());
	private static final String INDEX_NAME = "MessageTopicSearchIndex";

	private Index index;
	private Date referenceDate;
	
	public GAEMessageTopicDAOImpl() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME).build();
		index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		
		referenceDate = new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime();
	}

	@Override
	public MessageTopicPagedResponse findByGroupIds(Long entityUserId, List<Long> ids, Date iniDate, Date endDate, Long pageSize, Long page, Map<String,String> queryParams) {
		MessageTopicPagedResponse result = new MessageTopicPagedResponse();

		try {
			QueryOptions.Builder builder = 
				QueryOptions
					.newBuilder()
			  		.setFieldsToReturn(LABEL_FIELD);
						  
			if (page != null && pageSize != null) {
				Long offset = (page - 1) * pageSize;

				builder
					.setLimit(pageSize.intValue())
					.setNumberFoundAccuracy(pageSize.intValue() + 1)
					.setOffset(offset.intValue());
			}
						  
			QueryOptions options = builder.build();
			
			String queryOperator = "";
			StringBuilder groupIdsFilter = new StringBuilder("(");
			for (Long id : ids) {
				groupIdsFilter.append(queryOperator).append(id);
				queryOperator = " OR ";
			}
			groupIdsFilter.append(")");

			Long label = null;
			if (queryParams != null) {
				if (queryParams.containsKey(LABEL_FIELD)) {
					label = Long.parseLong(queryParams.get(LABEL_FIELD));
					queryParams.remove(LABEL_FIELD);
				}
			} else {
				queryParams = new HashMap<String,String>();
			}
		
			// prepare query
			StringBuilder queryString = new StringBuilder();
			if (queryParams.keySet() != null && !queryParams.keySet().isEmpty()) {

				// query type
				if (queryParams.get("type").equals("FROM")) {
					queryString.append("toUserGroupId: ").append(groupIdsFilter.toString());	
				} else if (queryParams.get("type").equals("TO")) {
					queryString.append("fromUserGroupId: ").append(groupIdsFilter.toString());	
				}
				queryParams.remove("type");
				
				for (String key : queryParams.keySet()){
					queryString.append(" AND ").append(key).append("=").append(queryParams.get(key));
				}					
			} else {
				//query with user groups only
				queryString.append("group: ").append(groupIdsFilter.toString());				
			}

			if (label != null && !label.equals(LABEL_ALL)){
				queryString.append(" AND ").append(LABEL_FIELD).append("=").append(buildLabelKey(entityUserId, label));
			}

			if (iniDate != null && endDate != null) {
				queryString.append(" AND date >= ").append(DateUtil.getDate(iniDate, DateUtil.INVERSE_DASH_PATTERN));
				queryString.append(" AND date <= ").append(DateUtil.getDate(endDate, DateUtil.INVERSE_DASH_PATTERN));
			}

			Query query = Query.newBuilder().setOptions(options).build(queryString.toString());
			Results<ScoredDocument> searchResult = index.search(query);			
			
			result.setHasMore(searchResult.getNumberFound() > searchResult.getNumberReturned());

			List<MessageTopicData> messageTopics = this.findByIds(
															searchResult
																.getResults()
																.stream()
																.map(doc -> Long.parseLong(doc.getId()))
																.collect(Collectors.toList())
														);
			if (messageTopics == null) {
				messageTopics = new ArrayList<MessageTopicData>();
			}

			//Create a mapping with all fields returned by the index
			Map<String, Iterable<Field>> resultMap =  searchResult
				.getResults()
				.stream()
				.collect(Collectors.toMap(ScoredDocument::getId, ScoredDocument::getFields));

			//For each messageTopic returned, check if there are fields named LABEL_FIELD starting
			//with the userId and add to the response
			messageTopics.stream().forEach(messageTopic -> {

				List<Long> labels = new ArrayList<Long>();
				for (Field field : resultMap.get(messageTopic.getId().toString())) {
					if (field.getName().equals(LABEL_FIELD) && field.getAtom().startsWith(entityUserId.toString())){
						labels.add(extractMessageLabelId(field.getAtom()));						
					}
				}
				messageTopic.setLabels(labels);
				
			});

			result.setMessageTopics(messageTopics);
			
		} catch (SearchException e) {
			log.warning("Error when querying for MessageTopics: " + e.toString());
		}
		
		return result;
	}
	
	@Override
	public List<MessageTopicData> findByFromEntityId(Long fromEntityId) {
		List<MessageTopicData> result = new ArrayList<MessageTopicData>();
		
		try {
			QueryOptions options =
				      QueryOptions.newBuilder()
				          .setReturningIdsOnly(true)
				          .build();
			
			String queryString = "fromEntityId: " + fromEntityId;
			Query query = Query.newBuilder().setOptions(options).build(queryString);
			Results<ScoredDocument> searchResult = index.search(query);
			
			result = this.findByIds(
					searchResult
						.getResults()
						.stream()
						.map(doc -> Long.parseLong(doc.getId()))
						.collect(Collectors.toList())
				);
		} catch (SearchException e) {
			log.warning("Error when querying for MessageTopics by fromEntityId: " + e.toString());
		}		
	
		return result;
	}
	
	@Override
	public String addSearchIndexDocument(MessageTopicData messageTopic, Set<Long> entityUserIds) {
		String indexId = null;

		Document existingDoc = index.get(messageTopic.getId().toString());
		if (existingDoc == null || existingDoc.getRank() < this.calculateDocumentRank(messageTopic.getUpdateDate())) {
		
			//Prepare labelKeys for all entityUserIds received
			Set<String> labelKeys = new HashSet<String>();
			if (entityUserIds != null) {
				labelKeys = entityUserIds.stream().map(entityUserId -> buildLabelKey(entityUserId, LABEL_INBOX)).collect(Collectors.toSet());
			}

			Builder indexBuilder = 
				Document.newBuilder()
				.setId(messageTopic.getId().toString())
				.setRank(calculateDocumentRank(messageTopic.getUpdateDate()));

			if (existingDoc == null) {

				//New index - build from scratch
				indexBuilder
					.addField(Field.newBuilder().setName("group").setAtom(messageTopic.getGroups().get(0).toString()))
					.addField(Field.newBuilder().setName("group").setAtom(messageTopic.getGroups().get(1).toString()))
					.addField(Field.newBuilder().setName("title").setText(messageTopic.getTitle().toString()))
					.addField(Field.newBuilder().setName("fromEntityProfile").setAtom(messageTopic.getFromEntityProfile().toString().toLowerCase()))
					.addField(Field.newBuilder().setName("fromEntityId").setAtom(messageTopic.getFromEntityId().toString()))
					.addField(Field.newBuilder().setName("fromUserGroupId").setAtom(messageTopic.getFromUserGroupId().toString()))
					.addField(Field.newBuilder().setName("toEntityProfile").setAtom(messageTopic.getToEntityProfile().toString().toLowerCase()))
					.addField(Field.newBuilder().setName("toEntityId").setAtom(messageTopic.getToEntityId().toString()))
					.addField(Field.newBuilder().setName("toUserGroupId").setAtom(messageTopic.getToUserGroupId().toString()))
					.addField(Field.newBuilder().setName("date").setDate(messageTopic.getDate()));
			
			} else {

				//Existing index - Update index
				for (Field field : existingDoc.getFields()) {
					//add all existing fields
					indexBuilder.addField(field);

					//remove existing labels from the new list of labelKeys
					if (field.getName().equals(LABEL_FIELD)) {
						labelKeys = labelKeys.stream().filter(labelKey -> !field.getAtom().equals(labelKey)).collect(Collectors.toSet());				
					}
				};
			}			
					
			//add (new) labelKeys
			labelKeys
				.stream()
				.forEach(labelKey -> 
					indexBuilder.addField(Field.newBuilder().setName(LABEL_FIELD).setAtom(labelKey))
				);
				
			indexId = index.put(indexBuilder.build()).getIds().get(0);
			
		} else {
			log.warning("Skipping indexing change for messageTopicId " + messageTopic.getId() + " as index is already more recently updated!");
		}
		
		return indexId;
	}

	@Override
	public String addSearchIndexDocumentField(String documentId, String fieldName, String fieldValue) {

		Document originalDoc = index.get(documentId);

		Builder indexBuilder = Document.newBuilder();

		indexBuilder
			.setId(originalDoc.getId())
			.setRank(originalDoc.getRank());

		originalDoc.getFields().forEach(field -> {
			if (field.getName().equals(fieldName) && field.getAtom().equals(fieldValue)) {
				log.fine("Field with same name " + field.getName() + " and value " + field.getAtom() + " found in index document " + documentId + ", so skipping it.");
			} else {
				indexBuilder.addField(field);
			}
		});

		indexBuilder.addField(Field.newBuilder().setName(fieldName).setAtom(fieldValue));
		return index.put(indexBuilder.build()).getIds().get(0);
	}

	@Override
	public String replaceSearchIndexDocumentField(String documentId, String fieldName, String sourceFieldValue, String targetFieldValue) {
		Builder indexBuilder = removeSearchIndexDocumentField(index.get(documentId), fieldName, sourceFieldValue);
		indexBuilder.addField(Field.newBuilder().setName(fieldName).setAtom(targetFieldValue));
		return index.put(indexBuilder.build()).getIds().get(0);
	}

	@Override
	public String removeSearchIndexDocumentField(String documentId, String fieldName) {
		return removeSearchIndexDocumentField(documentId, fieldName, null);
	}

	@Override
	public String removeSearchIndexDocumentField(String documentId, String fieldName, String fieldValue) {
		Builder indexBuilder = removeSearchIndexDocumentField(index.get(documentId), fieldName, fieldValue);
		return index.put(indexBuilder.build()).getIds().get(0);
	}

	@Override
	public void removeSearchIndexField(String fieldName, String fieldValue) throws InfrastructureException {
	
		try {

			int count = 0;
			while (true) {
				count++;

				QueryOptions options =
				      QueryOptions.newBuilder()
				          .setLimit(100)
				          .build();

				Query query = 
					Query.newBuilder()
					.setOptions(options)
					.build(fieldName + "=" + fieldValue);
				
				Results<ScoredDocument> searchResult = index.search(query);
				if (searchResult.getResults() == null || searchResult.getResults().size() == 0) break;
				log.info("Found " + searchResult.getResults().size() + " search index documents with field " + fieldName + " = " + fieldValue);
				
				searchResult
					.getResults()
					.stream()
					.forEach(doc -> {
						Builder indexBuilder = removeSearchIndexDocumentField(doc, fieldName, fieldValue);
						index.put(indexBuilder.build()).getIds().get(0);
					});

				if (count == 500 && searchResult.getResults().size() == 100) {
					log.severe("Too much messages to remove label for value '" + fieldValue + "'... stop and trigger notification");
					
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
					rm.setMessage("removeSearchIndexField: Too much messages to remove label for value '" + fieldValue + "'... stop and trigger notification");
					throw new InfrastructureException(rm);	
				}
			}
		
		} catch (SearchException e) {
			log.warning("Error when removing field from MessageTopic search index: " + e.toString());

			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage("removeSearchIndexField: Error searching message topic index");
			throw new InfrastructureException(rm);
		}

	}

	private int calculateDocumentRank(Date updatedDate) {
		return Long.valueOf((updatedDate.getTime() - referenceDate.getTime())/1000).intValue();
	}

	private Builder removeSearchIndexDocumentField(Document originalDoc, String fieldName, String fieldValue) {

		Builder indexBuilder = Document.newBuilder();

		indexBuilder
			.setId(originalDoc.getId())
			.setRank(originalDoc.getRank());

		originalDoc.getFields().forEach(field -> {
			if (field.getName().equals(fieldName) && (fieldValue == null || (fieldValue != null && field.getAtom().equals(fieldValue)))) {
				log.fine("Field " + field.getName() + " with value " + field.getAtom() + " removed from index document " + originalDoc.getId());
			} else {
				indexBuilder.addField(field);
			}
		});		

		return indexBuilder;
	}

	@Override
	public String buildLabelKey(Long entityUserId, Long messageLabelId) {
		return entityUserId + "-" + messageLabelId;
	}

	private Long extractMessageLabelId(String labelKey) {
		String[] labelParts = labelKey.split("-");
		return Long.parseLong(labelParts[1]);	
	}
	
}
