package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.MailManagementBO;
import br.com.usasistemas.usaplatform.bizo.MessageManagementBO;
import br.com.usasistemas.usaplatform.bizo.NotificationManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.exception.InfrastructureException;
import br.com.usasistemas.usaplatform.dao.MessageAttachmentDAO;
import br.com.usasistemas.usaplatform.dao.MessageCommentDAO;
import br.com.usasistemas.usaplatform.dao.MessageTopicDAO;
import br.com.usasistemas.usaplatform.dao.MessageTopicLabelDAO;
import br.com.usasistemas.usaplatform.dao.response.MessageTopicPagedResponse;
import br.com.usasistemas.usaplatform.model.data.EntityData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.MessageAttachmentData;
import br.com.usasistemas.usaplatform.model.data.MessageCommentData;
import br.com.usasistemas.usaplatform.model.data.MessageTopicData;
import br.com.usasistemas.usaplatform.model.data.MessageTopicLabelData;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.SupplierCategoryData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.SupplierFranchisorData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.DomainKeysEnum;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.NotificationTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class MessageManagementBOImpl implements MessageManagementBO {
	
	private static final Logger log = Logger.getLogger(MessageManagementBOImpl.class.getName());
	private MessageTopicDAO messageTopicDAO;
	private MessageTopicLabelDAO messageTopicLabelDAO;
	private MessageCommentDAO messageCommentDAO;
	private MessageAttachmentDAO messageAttachmentDAO;
	private UserManagementBO userManagement;
	private FranchisorManagementBO franchisorManagement;
	private FranchiseeManagementBO franchiseeManagement;
	private SupplierManagementBO supplierManagement;
	private NotificationManagementBO notificationManagement;
	private FileManagementBO fileManagement;
	private MailManagementBO mailManagement;
	
	public MessageTopicDAO getMessageTopicDAO() {
		return messageTopicDAO;
	}
	
	public void setMessageTopicDAO(MessageTopicDAO messageTopicDAO) {
		this.messageTopicDAO = messageTopicDAO;
	}

	public MessageTopicLabelDAO getMessageTopicLabelDAO() {
		return messageTopicLabelDAO;
	}
	
	public void setMessageTopicLabelDAO(MessageTopicLabelDAO messageTopicLabelDAO) {
		this.messageTopicLabelDAO = messageTopicLabelDAO;
	}

	public MessageCommentDAO getMessageCommentDAO() {
		return messageCommentDAO;
	}

	public void setMessageCommentDAO(MessageCommentDAO messageCommentDAO) {
		this.messageCommentDAO = messageCommentDAO;
	}

	public MessageAttachmentDAO getMessageAttachmentDAO() {
		return messageAttachmentDAO;
	}

	public void setMessageAttachmentDAO(MessageAttachmentDAO messageAttachmentDAO) {
		this.messageAttachmentDAO = messageAttachmentDAO;
	}

	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManagement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}

	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public SupplierManagementBO getSupplierManagement() {
		return supplierManagement;
	}

	public void setSupplierManagement(SupplierManagementBO supplierManagement) {
		this.supplierManagement = supplierManagement;
	}

	public NotificationManagementBO getNotificationManagement() {
		return notificationManagement;
	}

	public void setNotificationManagement(NotificationManagementBO notificationManagement) {
		this.notificationManagement = notificationManagement;
	}

	public FileManagementBO getFileManagement() {
		return fileManagement;
	}

	public void setFileManagement(FileManagementBO fileManagement) {
		this.fileManagement = fileManagement;
	}

	public MailManagementBO getMailManagement() {
		return mailManagement;
	}

	public void setMailManagement(MailManagementBO mailManagement) {
		this.mailManagement = mailManagement;
	}

	@Override
	public MessageTopicPagedResponse getUserMessageTopics(Long entityId, Long entityUserId, Long userGroupId, Date iniDate, Date endDate, Long pageSize, Long page, Map<String,String> queryParams) {

		MessageTopicPagedResponse response = null;		
		
		/*
		 * Get topics based on selected role user groups
		 */
		List<Long> groupIds = Arrays.asList(userGroupId);
		if (userGroupId == null || userGroupId == 0) {
			List<UserGroupData> userGroups = userManagement.getSelectedRoleUserGroups(entityId, entityUserId);
			groupIds = getUserGroupIds(userGroups);
		}
		
		if (groupIds != null && !groupIds.isEmpty()) {
			response = messageTopicDAO.findByGroupIds(entityUserId, groupIds, iniDate, endDate, pageSize, page, queryParams);
		} else {
			response = new MessageTopicPagedResponse();
			response.setHasMore(false);
		}		
		
		//no records found. return with empty array
		if (response.getMessageTopics() == null) {
			response.setMessageTopics(new ArrayList<MessageTopicData>());
		}
		
		return response;
	}

	@Override
	public MessageTopicData createMessageTopic(MessageTopicData messageTopic) {
		
		//Create message comment with the message content
		//Create object before save Topic because message will be lost when topic is saved
		MessageCommentData messageComment = new MessageCommentData();
		messageComment.setUserProfile(messageTopic.getFromEntityProfile());
		messageComment.setUserEntityId(messageTopic.getFromEntityId());
		messageComment.setUserId(messageTopic.getFromUserId());
		messageComment.setMessage(messageTopic.getMessage());
		messageComment.setDomainKey(messageTopic.getDomainKey());
		messageComment.setAttachmentIds(messageTopic.getAttachmentIds());
		
		List<Long> groups = new ArrayList<Long>();
		groups.add(messageTopic.getFromUserGroupId());
		groups.add(messageTopic.getToUserGroupId());
		messageTopic.setGroups(groups);
		
		//Save message topic
		messageTopic.setDate(new Date());
		messageTopic.setUpdateDate(new Date());
		messageTopic = messageTopicDAO.save(messageTopic);
		
		//Save message comment
		messageComment.setMessageTopicId(messageTopic.getId());		
		createMessageComment(messageComment);
		
		return messageTopic;
	}

	@Override
	public List<MessageTopicLabelData> getUserMessageTopicLabels(Long entityUserId) {
		List<MessageTopicLabelData> response = messageTopicLabelDAO.findByEntityUserId(entityUserId);

		//Always return the Inbox as one of the options
		MessageTopicLabelData inboxLabel = new MessageTopicLabelData();
		inboxLabel.setId(MessageTopicDAO.LABEL_INBOX);
		inboxLabel.setName("Caixa de Entrada");
		inboxLabel.setEntityUserId(entityUserId);
		response.add(0, inboxLabel);

		return response;
	}

	@Override
	public MessageTopicLabelData saveMessageTopicLabel(MessageTopicLabelData messageTopicLabel) {
		return messageTopicLabelDAO.save(messageTopicLabel);
	}

	@Override
	public MessageTopicLabelData deleteMessageTopicLabel(Long id) {

		MessageTopicLabelData response = messageTopicLabelDAO.delete(id);

		//Asynchronously process new comment
		Queue queue = QueueFactory.getQueue("messages");
		queue.add(TaskOptions.Builder.withUrl("/job/async/deleteMessageTopicLabel/" + messageTopicDAO.buildLabelKey(response.getEntityUserId(), response.getId())));

		return response;
	}

	@Override
	public void deleteMessageTopicLabelAsync(String labelKey){
		try {
			messageTopicDAO.removeSearchIndexField(MessageTopicDAO.LABEL_FIELD, labelKey);
		} catch (InfrastructureException ie) {
			mailManagement.sendErrorMail("Error deleting message topic label", ie.toString());
		}
		
	}

	@Override
	public void addLabelToMessageTopic(Long messageTopicId, Long entityUserId, Long messageTopicLabelId) {
		messageTopicDAO.addSearchIndexDocumentField(messageTopicId.toString(), MessageTopicDAO.LABEL_FIELD, messageTopicDAO.buildLabelKey(entityUserId, messageTopicLabelId));
	}

	@Override
	public void archiveMessageTopic(Long messageTopicId, Long entityUserId) {
		messageTopicDAO.removeSearchIndexDocumentField(messageTopicId.toString(), MessageTopicDAO.LABEL_FIELD, messageTopicDAO.buildLabelKey(entityUserId, MessageTopicDAO.LABEL_INBOX));
	}

	@Override
	public void moveMessageTopic(Long messageTopicId, Long entityUserId, Long sourceLabelId, Long targetLabelId) {
		messageTopicDAO.replaceSearchIndexDocumentField(messageTopicId.toString(), MessageTopicDAO.LABEL_FIELD, messageTopicDAO.buildLabelKey(entityUserId, sourceLabelId), messageTopicDAO.buildLabelKey(entityUserId, targetLabelId));
	}

	@Override
	public void removeLabelFromMessageTopic(Long messageTopicId, Long entityUserId, Long messageTopicLabelId) {
		messageTopicDAO.removeSearchIndexDocumentField(messageTopicId.toString(), MessageTopicDAO.LABEL_FIELD, messageTopicDAO.buildLabelKey(entityUserId, messageTopicLabelId));
	}

	@Override
	public List<EnumValidValueResponseData> getToProfileTypes(UserProfileData user, String domainKey) {
		
		List<EnumValidValueResponseData> response = userManagement.getUserProfileTypes();
		if (response != null && !response.isEmpty()) {
			
			for (int index = response.size()-1; index >= 0; index--) {
				
				EnumValidValueResponseData value = response.get(index);
				
				// Should not return the own Profile, as it is not allowed
				// to send message to itself. Except for FRANCHISOR				
				if (value.getKey().equals(user.getSelectedRole().name()) && !user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR)) {
					response.remove(index);
					continue;
				}
				
				//Should not return SUPPLIERS for INTERVENCAO_COMPORTAMENTAL
				if (domainKey.equals(DomainKeysEnum.INTERVENCAO_COMPORTAMENTAL.name())
						&& value.getKey().equals(UserProfileEnum.SUPPLIER.name())){
					response.remove(index);
					continue;
				}
				
				//Should not return ADMINISTRATOR for FRANCHISEES
				if (user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE) && value.getKey().equals(UserProfileEnum.ADMINISTRATOR.name())) {
					response.remove(index);
					continue;
				}
			}
		}
		
		return response;
	}

	@Override
	public List<EntityData> getToEntities(UserProfileEnum entityProfile, UserProfileData user) {
		
		List<EntityData> response = new ArrayList<EntityData>();
		
		UserProfileEnum userProfile = user.getSelectedRole();
		
		if (entityProfile.equals(UserProfileEnum.FRANCHISOR)) {
			
			/*
			 * LOAD FRANCHISOR
			 */
			if (userProfile.equals(UserProfileEnum.ADMINISTRATOR)) {
				
				/*
				 * Administrator can send message to any franchisor
				 */				
				List<FranchisorData> franchisors = franchisorManagement.getAllFranchisors();
				if (franchisors != null && !franchisors.isEmpty()) {
					for (FranchisorData franchisor : franchisors) {
						
						EntityData entityData = new EntityData();
						entityData.setId(franchisor.getId());
						entityData.setName(franchisor.getName());
						response.add(entityData);
					}
				}
			} else if (userProfile.equals(UserProfileEnum.SUPPLIER)) {
				
				/*
				 * Supplier can send message only to his franchisors
				 */	
				List<SupplierFranchisorData> suppliersFranchisors = supplierManagement.getSupplierFranchisors(user.getSupplier().getSupplierId());
				if (suppliersFranchisors != null && !suppliersFranchisors.isEmpty()) {
					for (SupplierFranchisorData supplierFranchisor : suppliersFranchisors) {
						
						FranchisorData franchisor = franchisorManagement.getFranchisor(supplierFranchisor.getFranchisorId());
						
						EntityData entityData = new EntityData();
						entityData.setId(franchisor.getId());
						entityData.setName(franchisor.getName());
						response.add(entityData);
					}
				}
			}
			
		} else if (entityProfile.equals(UserProfileEnum.FRANCHISEE)) {
			
			/*
			 * LOAD FRANCHISEES
			 */
			if (userProfile.equals(UserProfileEnum.ADMINISTRATOR)) {
				
				/*
				 * Administrator can send message to any franchisee from any franchisor
				 */
				List<FranchisorData> franchisors = franchisorManagement.getAllFranchisors();
				if (franchisors != null && !franchisors.isEmpty()) {
					for (FranchisorData franchisor : franchisors) {
						
						List<FranchiseeData> franchisees = franchiseeManagement.getFranchiseesByFranchisorId(franchisor.getId());
						if (franchisees != null && !franchisees.isEmpty()) {
							for (FranchiseeData franchisee : franchisees) {
								
								EntityData entityData = new EntityData();
								entityData.setId(franchisee.getId());
								entityData.setName(franchisor.getName() + " - " + franchisee.getName());
								response.add(entityData);
							}							
						}
					}
				}
			} else if (userProfile.equals(UserProfileEnum.FRANCHISOR)) {
				
				/*
				 * Franchisor can send message only for his franchisees
				 * 
				 */
				List<FranchiseeData> franchisees = franchiseeManagement.getFranchiseesByFranchisorId(user.getFranchisor().getFranchisorId());
				if (franchisees != null && !franchisees.isEmpty()) {
					for (FranchiseeData franchisee : franchisees) {
						
						EntityData entityData = new EntityData();
						entityData.setId(franchisee.getId());
						entityData.setName(franchisee.getName());
						response.add(entityData);
					}							
				}
			} else if (userProfile.equals(UserProfileEnum.SUPPLIER)) {
				
				/*
				 * Supplier can send message only to franchisees from his franchisors
				 */	
				List<SupplierFranchisorData> suppliersFranchisors = supplierManagement.getSupplierFranchisors(user.getSupplier().getSupplierId());
				if (suppliersFranchisors != null && !suppliersFranchisors.isEmpty()) {
					for (SupplierFranchisorData supplierFranchisor : suppliersFranchisors) {
						
						FranchisorData franchisor = franchisorManagement.getFranchisor(supplierFranchisor.getFranchisorId());
						
						List<FranchiseeData> franchisees = franchiseeManagement.getFranchiseesByFranchisorId(franchisor.getId());
						if (franchisees != null && !franchisees.isEmpty()) {
							for (FranchiseeData franchisee : franchisees) {
								
								EntityData entityData = new EntityData();
								entityData.setId(franchisee.getId());
								entityData.setName(franchisor.getName() + " - " + franchisee.getName());
								response.add(entityData);
							}							
						}
					}
				}
			}
			
		} else if (entityProfile.equals(UserProfileEnum.SUPPLIER)) {
			
			//First get the list of supplier categories that receive message to filter the suppliers below
			List<Long> filteredCategories = new ArrayList<Long>();
			for (SupplierCategoryData supplierCategory : supplierManagement.getAllCategories()) {
				if (supplierCategory.getReceiveMessage()) {
					filteredCategories.add(supplierCategory.getId());
				}
			}			
			
			/*
			 * LOAD SUPPLIERS
			 */
			if (userProfile.equals(UserProfileEnum.ADMINISTRATOR)) {
				
				/* 
				 * Administrator can send message to any supplier
				 */
				List<SupplierData> suppliers =  supplierManagement.getAllSuppliers();
				if (suppliers != null && !suppliers.isEmpty()) {
					for (SupplierData supplier : suppliers) {
						
						if (filteredCategories.contains(supplier.getCategoryId())) {
							EntityData entityData = new EntityData();
							entityData.setId(supplier.getId());
							entityData.setName(supplier.getName());
							response.add(entityData);
						}						
						
					}
				}
			} else if (userProfile.equals(UserProfileEnum.FRANCHISOR)) {
				
				/*
				 * Franchisor can send message only for his suppliers
				 * 
				 */
				List<SupplierData> suppliers = supplierManagement.getFranchisorSuppliers(user.getFranchisor().getFranchisorId());
				if (suppliers != null && !suppliers.isEmpty()) {
					for (SupplierData supplier : suppliers) {
						
						if (filteredCategories.contains(supplier.getCategoryId())) {
							EntityData entityData = new EntityData();
							entityData.setId(supplier.getId());
							entityData.setName(supplier.getName());
							response.add(entityData);
						}
					}
				}
			} else if (userProfile.equals(UserProfileEnum.FRANCHISEE)) {
				
				/*
				 * Franchisee can send message only for franchisor suppliers
				 * 
				 */
				FranchiseeData franchisee = franchiseeManagement.getFranchisee(user.getFranchisee().getFranchiseeId());
				
				List<SupplierData> suppliers = supplierManagement.getFranchisorSuppliers(franchisee.getFranchisorId());
				if (suppliers != null && !suppliers.isEmpty()) {
					for (SupplierData supplier : suppliers) {

						if (filteredCategories.contains(supplier.getCategoryId())) {
							EntityData entityData = new EntityData();
							entityData.setId(supplier.getId());
							entityData.setName(supplier.getName());
							response.add(entityData);
						}
					}
				}
			}
		}
		
		return response;
	}

	@Override
	public List<UserGroupData> getToUserGroups(UserProfileEnum entityProfile, Long entityId, UserProfileData user) {
		
		if (entityProfile == UserProfileEnum.FRANCHISOR && entityId == 0) {
			FranchiseeData franchisee = franchiseeManagement.getFranchisee(user.getFranchisee().getFranchiseeId());
			entityId = franchisee.getFranchisorId();
		}		
		
		List<UserGroupData> response = userManagement.getEntityUserGroups(entityId);
		if (response != null && !response.isEmpty()) {
			
			// Should not return groups that are not marked to receive messages
			for (int index = response.size()-1; index >= 0; index--) {
				if (!response.get(index).getReceiveMessage()) {
					response.remove(index);
				}
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, EntityData> getMessageTopicEntities(List<MessageTopicData> messageTopics, UserProfileData user) {

		Map<Long, EntityData> response = new HashMap<Long, EntityData>();
		
		if (messageTopics != null & !messageTopics.isEmpty()) {
			for (MessageTopicData messageTopic: messageTopics) {
				
				//from entity
				try {
					if (!response.containsKey(messageTopic.getFromEntityId())) {
						response.put(messageTopic.getFromEntityId(), getMessageTopicEntity(messageTopic.getFromEntityProfile(), messageTopic.getFromEntityId(), user));
					}
				}
				catch (Exception e) {
					log.warning("Error getting Message Topic FROM Entity [EntityProfile=" + messageTopic.getFromEntityProfile().getDescription() + ", EntityId=" + messageTopic.getFromEntityId() + "]: " + e.toString());
				}
				
				//to entity
				try {
					if (!response.containsKey(messageTopic.getToEntityId())) {
						response.put(messageTopic.getToEntityId(), getMessageTopicEntity(messageTopic.getToEntityProfile(), messageTopic.getToEntityId(), user));
					}
				}
				catch (Exception e) {
					log.warning("Error getting Message Topic TO Entity [EntityProfile=" + messageTopic.getToEntityProfile().getDescription() + ", EntityId=" + messageTopic.getToEntityId() + "]: " + e.toString());
				}
				
			}
		}
		
		return response;
	}

	private EntityData getMessageTopicEntity(UserProfileEnum entityProfile, Long entityId, UserProfileData user) {
		
		EntityData response = null;
		UserProfileEnum userProfile = user.getSelectedRole();
		
		log.finest("getMessageTopicEntity [EntityProfile=" + entityProfile.getDescription() + ", EntityId=" + entityId + ", UserProfile=" + userProfile.getDescription() + "]");
		
		if (entityProfile.equals(UserProfileEnum.FRANCHISOR)) {
			if (userProfile.equals(UserProfileEnum.ADMINISTRATOR) || userProfile.equals(UserProfileEnum.SUPPLIER)) {
				FranchisorData franchisor = franchisorManagement.getFranchisor(entityId);
				response = new EntityData();
				response.setId(entityId);
				response.setName(franchisor.getName());

			} else if (userProfile.equals(UserProfileEnum.FRANCHISEE)) {
				response = new EntityData();
				response.setId(entityId);
				response.setName(UserProfileEnum.FRANCHISOR.getDescription());
			}
			
		} else if (entityProfile.equals(UserProfileEnum.FRANCHISEE)) {
			if (userProfile.equals(UserProfileEnum.ADMINISTRATOR) || userProfile.equals(UserProfileEnum.SUPPLIER)) {
				FranchiseeData franchisee = franchiseeManagement.getFranchisee(entityId);
				FranchisorData franchisor = franchisorManagement.getFranchisor(franchisee.getFranchisorId());
				response = new EntityData();
				response.setId(entityId);
				response.setName(franchisor.getName() + " - " + franchisee.getName());
			} else if (userProfile.equals(UserProfileEnum.FRANCHISOR)) {
				FranchiseeData franchisee = franchiseeManagement.getFranchisee(entityId);
				response = new EntityData();
				response.setId(entityId);
				response.setName(franchisee.getName());
			}			
		} else if (entityProfile.equals(UserProfileEnum.SUPPLIER)) {
			SupplierData supplier = supplierManagement.getSupplier(entityId);
			response = new EntityData();
			response.setId(entityId);
			if (supplier == null) {
				response.setName("Fornecedor Não Encontrado");
			} else {
				response.setName(supplier.getName());
			}
		} else if (entityProfile.equals(UserProfileEnum.ADMINISTRATOR)) {
			response = new EntityData();
			response.setId(entityId);
			response.setName(UserProfileEnum.ADMINISTRATOR.getDescription());
		}
		
		return response;
	}

	@Override
	public Map<Long, UserGroupData> getMessageTopicUserGroups(List<MessageTopicData> messageTopics, UserProfileData user) {

		Map<Long, UserGroupData> response = new HashMap<Long, UserGroupData>();
		
		if (messageTopics != null & !messageTopics.isEmpty()) {
			for (MessageTopicData messageTopic: messageTopics) {
				
				//from group
				if (!response.containsKey(messageTopic.getFromUserGroupId())) {
					response.put(messageTopic.getFromUserGroupId(), userManagement.getUserGroup(messageTopic.getFromUserGroupId()));
				}
				
				//to group
				if (!response.containsKey(messageTopic.getToUserGroupId())) {
					response.put(messageTopic.getToUserGroupId(), userManagement.getUserGroup(messageTopic.getToUserGroupId()));
				}				
			}
		}
		
		return response;
	}

	@Override
	public MessageCommentData createMessageComment(MessageCommentData messageComment) {
		
		messageComment.setDate(new Date());
		messageComment = messageCommentDAO.save(messageComment);
		
		//Link attachments to message comment
		if (messageComment.getAttachmentIds() != null && !messageComment.getAttachmentIds().isEmpty()) {
			
			List<MessageAttachmentData> attachments = new ArrayList<MessageAttachmentData>();
			messageComment.setAttachments(attachments);
			
			for (Long attachmentId : messageComment.getAttachmentIds()) {
				MessageAttachmentData attachment = messageAttachmentDAO.findById(attachmentId);
				if (attachment != null) {
					attachment.setMessageCommentId(messageComment.getId());
					attachments.add(messageAttachmentDAO.save(attachment));
				}				
			}
		}
		
		//Asynchronously process new comment
		Queue queue = QueueFactory.getQueue("messages");
		queue.add(TaskOptions.Builder.withUrl("/job/async/newMessageComment/"+messageComment.getId()));
		
		return messageComment;
	}

	@Override
	public List<MessageCommentData> getMessageComments(Long messageTopicId) {
		
		List<MessageCommentData> messageComments = messageCommentDAO.findByMessageTopicId(messageTopicId);		
		if (messageComments != null && !messageComments.isEmpty()){
			
			// Load attachments data
			for (MessageCommentData messageComment : messageComments) {
				if (messageComment.getAttachmentIds() != null && !messageComment.getAttachmentIds().isEmpty()) {
					
					List<MessageAttachmentData> attachments = new ArrayList<MessageAttachmentData>();
					messageComment.setAttachments(attachments);
					
					for (Long attachmentId : messageComment.getAttachmentIds()) {
						MessageAttachmentData attachment = messageAttachmentDAO.findById(attachmentId);
						if (attachment != null) {
							attachments.add(attachment);
						}						
					}
				}
			}
		}
		
		return messageComments;
	}
	
	@Override
	public Map<Long, PublicUserData> getMessageCommentUsers(List<MessageCommentData> messageComments) {
		
		Map<Long, PublicUserData> response = new HashMap<Long, PublicUserData>();
		
		if (messageComments != null & !messageComments.isEmpty()) {
			for (MessageCommentData messageComment: messageComments) {
				
				if (!response.containsKey(messageComment.getUserId())) {
					
					PublicUserData userData = userManagement.getUser(messageComment.getUserId());
					
					if (userData != null) {
						userData.setEmail(null);
						userData.setEnabled(null);
						userData.setUsername(null);
						userData.setSelectedRole(null);
						
						response.put(messageComment.getUserId(), userData);
					}					
				}
			}
		}
		
		return response;
	}

	@Override
	public void createMessageCommentAsync(Long messageCommentId) {
		MessageCommentData messageComment = messageCommentDAO.findById(messageCommentId);
		MessageTopicData messageTopic = messageTopicDAO.findById(messageComment.getMessageTopicId());

		//create notification for all users in the FROM group
		List<Long> fromUserIds = userManagement.getUserGroupUserIds(messageTopic.getFromUserGroupId());
		if (fromUserIds != null && !fromUserIds.isEmpty()) {
			for (Long user : fromUserIds) {

				NotificationData notification = new NotificationData();
				notification.setType(NotificationTypeEnum.MESSAGE_COMMENT);
				notification.setReferenceId(messageComment.getMessageTopicId());
				notification.setReferenceId2(messageCommentId);
				notification.setEntityProfile(messageTopic.getFromEntityProfile());
				notification.setEntityId(messageTopic.getFromEntityId());
				notification.setUserGroupId(messageTopic.getFromUserGroupId());
				notification.setUserId(user);
				notification.setDomainKey(messageComment.getDomainKey());
				
				if (messageComment.getUserId().equals(user)) {
					notification.setIsRead(true);
					notification.setNotificationStatus(MailNotificationStatusEnum.NOT_APPLICABLE);
				} else {
					notification.setIsRead(false);
					notification.setNotificationStatus(MailNotificationStatusEnum.PENDING);
				}				
				
				notificationManagement.createNotification(notification);
				
			}
		}
		
		//create notification for all users in the TO group if not same group (FRANCHISOR only)
		if (!messageTopic.getFromUserGroupId().equals(messageTopic.getToUserGroupId())) {
			List<Long> toUserIds = userManagement.getUserGroupUserIds(messageTopic.getToUserGroupId());
			if (toUserIds != null && !toUserIds.isEmpty()) {
				for (Long user : toUserIds) {

					NotificationData notification = new NotificationData();
					notification.setType(NotificationTypeEnum.MESSAGE_COMMENT);
					notification.setReferenceId(messageComment.getMessageTopicId());
					notification.setReferenceId2(messageCommentId);
					notification.setEntityProfile(messageTopic.getToEntityProfile());
					notification.setEntityId(messageTopic.getToEntityId());
					notification.setUserGroupId(messageTopic.getToUserGroupId());
					notification.setUserId(user);
					notification.setDomainKey(messageComment.getDomainKey());
					
					if (messageComment.getUserId().equals(user)) {
						notification.setIsRead(true);
						notification.setNotificationStatus(MailNotificationStatusEnum.NOT_APPLICABLE);
					} else {
						notification.setIsRead(false);
						notification.setNotificationStatus(MailNotificationStatusEnum.PENDING);
					}
					
					notificationManagement.createNotification(notification);
					
				}
			}
		}

		//Get the entityUserId from all users to add the label to the index
		Set<Long> entityUserIds = new HashSet<Long>();
		entityUserIds.addAll(userManagement.getUserGroupEntityUserIds(messageTopic.getFromUserGroupId()));
		entityUserIds.addAll(userManagement.getUserGroupEntityUserIds(messageTopic.getToUserGroupId()));

		//update messageTopic with the updated date for last comment
		messageTopic.setUpdateDate(messageComment.getDate());
		messageTopicDAO.save(messageTopic);
		messageTopicDAO.addSearchIndexDocument(messageTopic, entityUserIds);
	}

	@Override
	public Long getMessageTopicUnreadNotificationsCount(UserProfileData user, List<UserGroupData> receiveMessageGroup) {
		
		Set<Long> result = new HashSet<Long>();
		List<Long> receiveMessageGroupIds = getUserGroupIds(receiveMessageGroup);
		
		List<NotificationData> notifications = notificationManagement.getAllUnreadMessageTopicsNotifications(user);
		if (notifications != null && !notifications.isEmpty()) {
			for (NotificationData notification: notifications) {
				MessageTopicData messageTopic = messageTopicDAO.findById(notification.getReferenceId());
				if (receiveMessageGroupIds.contains(messageTopic.getFromUserGroupId()) || 
						receiveMessageGroupIds.contains(messageTopic.getToUserGroupId())) {
					result.add(notification.getReferenceId2());
				}
			}
		}
		 
		return Long.valueOf(result.size());
	}

	@Override
	public Map<Long, List<NotificationData>> getMessageTopicUnreadNotifications(List<MessageTopicData> messageTopics, UserProfileData user) {
		
		Map<Long, List<NotificationData>> response = new HashMap<Long, List<NotificationData>>();
		
		if (messageTopics != null & !messageTopics.isEmpty()) {
			for (MessageTopicData messageTopic: messageTopics) {
				List<NotificationData> unreadNotifications = notificationManagement.getMessageTopicUnreadNotifications(messageTopic.getId(), user);
				if (unreadNotifications != null && !unreadNotifications.isEmpty()) {
					response.put(messageTopic.getId(), unreadNotifications);
				}
			}
		}
		
		return response;

	}

	@Override
	public void markNotificationsAsRead(List<Long> notificationsIds, UserProfileData user) {
		notificationManagement.markNotificationsAsRead(notificationsIds, user);
	}

	@Override
	public List<NotificationData> getMessageCommentNotifications(Long messageCommentId) {
		return notificationManagement.getAllMessageCommentNotifications(messageCommentId);
	}

	@Override
	public Map<Long, PublicUserData> getMessageCommentNotificationUsers(List<NotificationData> messageCommentNotifications) {
		return notificationManagement.getNotificationUsers(messageCommentNotifications);
	}

	@Override
	public List<UserGroupData> getMessageUserGroups(UserProfileData user) {
		
		List<UserGroupData> response = new ArrayList<UserGroupData>();
		
		List<UserGroupData> userGroups = userManagement.getSelectedRoleUserGroups(user.getSelectedEntityId(), user.getSelectedEntityUserId());
		if (userGroups != null && !userGroups.isEmpty()) {				
			for (UserGroupData userGroup : userGroups) {
				if (userGroup.getReceiveMessage()) {
					response.add(userGroup);
				}				
			}
		}
		
		return response;
	}
	
	private List<Long> getUserGroupIds (List<UserGroupData> userGroups) {
		List<Long> groupIds = new ArrayList<Long>();
		
		if (userGroups != null && !userGroups.isEmpty()) {				
			for (UserGroupData userGroup : userGroups) {
				groupIds.add(userGroup.getId());
			}
		}
		
		return groupIds;
	}

	@Override
	public MessageAttachmentData createAttachment(UploadedFileData file) {

		MessageAttachmentData attachment = new MessageAttachmentData();
		attachment.setName(file.getName());
		attachment.setDate(file.getDate());
		attachment.setContentType(file.getContentType());
		attachment.setSize(file.getSize());
		attachment.setStorePath(file.getStorePath());
		attachment.setFileKey(file.getFileKey());
		
		return messageAttachmentDAO.save(attachment);
	}

	@Override
	public MessageAttachmentData getAttachment(Long id) {
		return messageAttachmentDAO.findById(id);
	}
	
	@Override
	public void cleanUpUnusedAttachments(Long threshold) {
		
		Calendar thresholdDate = Calendar.getInstance();
		thresholdDate.add(Calendar.DAY_OF_MONTH, threshold.intValue() * -1);
		
		List<MessageAttachmentData> attachments = messageAttachmentDAO.findUnusedByDate(thresholdDate.getTime());
		
		if (attachments != null && !attachments.isEmpty()) {
			for (MessageAttachmentData attachment : attachments) {
				fileManagement.deleteFile(attachment.getFileKey());
				messageAttachmentDAO.delete(attachment.getId());
			}
		}
		
		log.info(attachments.size() + " unused attachments cleaned.");
		
	}
		
}
