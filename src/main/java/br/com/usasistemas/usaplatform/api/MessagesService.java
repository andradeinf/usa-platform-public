package br.com.usasistemas.usaplatform.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.AddLabelToMessageTopicRequest;
import br.com.usasistemas.usaplatform.api.request.ArchiveMessageTopicRequest;
import br.com.usasistemas.usaplatform.api.request.MoveMessageTopicRequest;
import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.api.response.UrlResponse;
import br.com.usasistemas.usaplatform.api.response.WSEntityListResponse;
import br.com.usasistemas.usaplatform.api.response.WSEnumValidValuesResponse;
import br.com.usasistemas.usaplatform.api.response.WSMessageAttachmentResponse;
import br.com.usasistemas.usaplatform.api.response.WSMessageCommentListResponse;
import br.com.usasistemas.usaplatform.api.response.WSMessageCommentResponse;
import br.com.usasistemas.usaplatform.api.response.WSMessageNotificationMenu;
import br.com.usasistemas.usaplatform.api.response.WSMessageTopicLabelListResponse;
import br.com.usasistemas.usaplatform.api.response.WSMessageTopicLabelResponse;
import br.com.usasistemas.usaplatform.api.response.WSMessageTopicListResponse;
import br.com.usasistemas.usaplatform.api.response.WSMessageTopicResponse;
import br.com.usasistemas.usaplatform.api.response.WSNotificationListResponse;
import br.com.usasistemas.usaplatform.api.response.WSUserGroupListResponse;
import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.MessageManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.dao.response.MessageTopicPagedResponse;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.MessageAttachmentData;
import br.com.usasistemas.usaplatform.model.data.MessageCommentData;
import br.com.usasistemas.usaplatform.model.data.MessageTopicData;
import br.com.usasistemas.usaplatform.model.data.MessageTopicLabelData;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.MESSAGES_SERVICE)
public class MessagesService {
	
	private static final Logger log = Logger.getLogger(MessagesService.class.getName());
	private MessageManagementBO messageManagement;
	private ConfigurationManagementBO configurationManagement;
	private FileManagementBO fileManagement;
	
	public MessageManagementBO getMessageManagement() {
		return messageManagement;
	}
	
	public void setMessageManagement(MessageManagementBO messageManagement) {
		this.messageManagement = messageManagement;
	}
	
	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}
	
	public FileManagementBO getFileManagement() {
		return fileManagement;
	}

	public void setFileManagement(FileManagementBO fileManagement) {
		this.fileManagement = fileManagement;
	}

	@RequestMapping(value = "/userGroupId/{userGroupId}/paged/{page}/pageSize/{pageSize}", method=RequestMethod.GET)
	@ResponseBody
	public WSMessageTopicListResponse listMessageTopics(@PathVariable Long userGroupId, @PathVariable Long page, @PathVariable Long pageSize, @RequestParam Map<String,String> queryParams, HttpSession session) {
		
		WSMessageTopicListResponse response = new WSMessageTopicListResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			MessageTopicPagedResponse messageTopicResponse = messageManagement.getUserMessageTopics(user.getSelectedEntityId(), user.getSelectedEntityUserId(), userGroupId, null, null, pageSize, page, queryParams);
			response.setMessageTopics(messageTopicResponse.getMessageTopics());
			response.setHasMore(messageTopicResponse.getHasMore());
			
			response.setEntities(messageManagement.getMessageTopicEntities(response.getMessageTopics(), user));
			response.setUserGroups(messageManagement.getMessageTopicUserGroups(response.getMessageTopics(), user));
			response.setMessageTopicsUnreadNotifications(messageManagement.getMessageTopicUnreadNotifications(response.getMessageTopics(), user));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Message Topic List: " + e.toString());
		}
		
		return response;		
	}

	@RequestMapping(value = "/messageTopicLabel", method=RequestMethod.GET)
	@ResponseBody
	public WSMessageTopicLabelListResponse listUserMessageTopicLabels(HttpSession session) {
		
		WSMessageTopicLabelListResponse response = new WSMessageTopicLabelListResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			response.setMessageTopicLabels(messageManagement.getUserMessageTopicLabels(user.getSelectedEntityUserId()));
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Message Topic Label List: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(value = "/{messageTopicId}/comments", method=RequestMethod.GET)
	@ResponseBody
	public WSMessageCommentListResponse listMessageComments(@PathVariable Long messageTopicId, HttpSession session) {
		
		WSMessageCommentListResponse response = new WSMessageCommentListResponse();
		
		try {
			List<MessageCommentData> messageComments = messageManagement.getMessageComments(messageTopicId);
			response.setMessageComments(messageComments);
			response.setUsers(messageManagement.getMessageCommentUsers(messageComments));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Message Comments List: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(value = "/comment/{messageCommentId}/notifications", method=RequestMethod.GET)
	@ResponseBody
	public WSNotificationListResponse listMessageCommentNotifications(@PathVariable Long messageCommentId, HttpSession session) {
		
		WSNotificationListResponse response = new WSNotificationListResponse();
		
		try {
			List<NotificationData> messageCommentNotifications = messageManagement.getMessageCommentNotifications(messageCommentId);
			response.setNotifications(messageCommentNotifications);
			response.setUsers(messageManagement.getMessageCommentNotificationUsers(messageCommentNotifications));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Message Comments Notifications: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(value = "/toProfileTypes", method=RequestMethod.GET)
	@ResponseBody
	public WSEnumValidValuesResponse getToProfileTypes(HttpSession session, HttpServletRequest request) {
		
		WSEnumValidValuesResponse response = new WSEnumValidValuesResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			
			List<EnumValidValueResponseData> validValues = messageManagement.getToProfileTypes(user, domainConfiguration.getKey());
			if (validValues != null && !validValues.isEmpty()) {
				//Map domain names
				Map<String, String> labels = domainConfiguration.getLabels();
				
				for (EnumValidValueResponseData value : validValues) {
					String newValue = labels.get(value.getKey());
					if (newValue != null) {
						value.setValue(newValue);
					}
					
					if (user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR) && value.getKey().equals(UserProfileEnum.FRANCHISOR.name())) {
						value.setValue(value.getValue() + " (Mensagem Interna)");
					}
				}
			}
			response.setEnumValues(validValues);		
			
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Destination Profile Types: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/toEntities/{entityProfile}", method=RequestMethod.GET)
	@ResponseBody
	public WSEntityListResponse getToEntities(@PathVariable UserProfileEnum entityProfile, HttpSession session) {
		
		WSEntityListResponse response = new WSEntityListResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			response.setEntities(messageManagement.getToEntities(entityProfile, user));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting To Entities: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/toUserGroups/{entityProfile}/{entityId}", method=RequestMethod.GET)
	@ResponseBody
	public WSUserGroupListResponse getToUserGroups(@PathVariable UserProfileEnum entityProfile, @PathVariable Long entityId, HttpSession session) {
		
		WSUserGroupListResponse response = new WSUserGroupListResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			response.setUserGroups(messageManagement.getToUserGroups(entityProfile, entityId, user));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Destination User Groups: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/getNewMessageCount", method=RequestMethod.GET)
	@ResponseBody
	public WSMessageNotificationMenu getNewMessageCount(HttpSession session) {
		
		WSMessageNotificationMenu response = new WSMessageNotificationMenu();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			List<UserGroupData> receiveMessageGroup = messageManagement.getMessageUserGroups(user);
			response.setReceiveMessage(receiveMessageGroup.size() > 0);
			response.setNewMessageCount(messageManagement.getMessageTopicUnreadNotificationsCount(user, receiveMessageGroup));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Notification information: " + e.toString());
		}
		
		return response;
	}
		
	@RequestMapping(value = "/messageTopicLabel", method=RequestMethod.POST)
	@ResponseBody
	public WSMessageTopicLabelResponse createMessageTopicLabel(@RequestBody MessageTopicLabelData messageTopicLabel, HttpSession session) {
		
		WSMessageTopicLabelResponse response = new WSMessageTopicLabelResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			if (messageTopicLabel.getId() == 0) {
				messageTopicLabel.setId(null);
				messageTopicLabel.setEntityUserId(user.getSelectedEntityUserId());		
			}
					
			response.setMessageTopicLabel(messageManagement.saveMessageTopicLabel(messageTopicLabel));
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Message Topic Label: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/messageTopicLabel", method=RequestMethod.PUT)
	@ResponseBody
	public WSMessageTopicLabelResponse updateMessageTopicLabel(@RequestBody MessageTopicLabelData messageTopicLabel) {
		
		WSMessageTopicLabelResponse response = new WSMessageTopicLabelResponse();
		
		try {
			response.setMessageTopicLabel(messageManagement.saveMessageTopicLabel(messageTopicLabel));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Message Topic Label: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/messageTopicLabel/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSMessageTopicLabelResponse deleteMessageTopicLabel(@PathVariable Long id) {
		
		WSMessageTopicLabelResponse response = new WSMessageTopicLabelResponse();
		
		try {
			response.setMessageTopicLabel(messageManagement.deleteMessageTopicLabel(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Message Topic Label: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/messageTopic/label", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse addLabelToMessageTopic(@RequestBody AddLabelToMessageTopicRequest addLabelToMessageTopic, HttpSession session) {
		
		GenericResponse response = new GenericResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			messageManagement.addLabelToMessageTopic(addLabelToMessageTopic.getMessageTopicId(), user.getSelectedEntityUserId(), addLabelToMessageTopic.getMessageTopicLabelId());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error adding label to Message Topic: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/messageTopic/archive", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse archiveMessageTopic(@RequestBody ArchiveMessageTopicRequest archiveMessageTopic, HttpSession session) {
		
		GenericResponse response = new GenericResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			messageManagement.archiveMessageTopic(archiveMessageTopic.getMessageTopicId(), user.getSelectedEntityUserId());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error archiving Message Topic: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/messageTopic/move", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse archiveMessageTopic(@RequestBody MoveMessageTopicRequest moveMessageTopic, HttpSession session) {
		
		GenericResponse response = new GenericResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			messageManagement.moveMessageTopic(moveMessageTopic.getMessageTopicId(), user.getSelectedEntityUserId(), moveMessageTopic.getSourceLabelId(), moveMessageTopic.getTargetLabelId());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error archiving Message Topic: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/messageTopic/{messageTopicId}/label/{messageTopicLabelId}", method=RequestMethod.DELETE)
	@ResponseBody
	public GenericResponse removeLabelFromMessageTopic(@PathVariable Long messageTopicId, @PathVariable Long messageTopicLabelId, HttpSession session) {
		
		GenericResponse response = new GenericResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			messageManagement.removeLabelFromMessageTopic(messageTopicId, user.getSelectedEntityUserId(), messageTopicLabelId);
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error removing label from Message Topic: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSMessageTopicResponse createMessageTopic(@RequestBody MessageTopicData messageTopic, HttpSession session, HttpServletRequest request) {
		
		WSMessageTopicResponse response = new WSMessageTopicResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			if (messageTopic.getId() == 0) {
				messageTopic.setId(null);
				messageTopic.setFromEntityProfile(user.getSelectedRole());
				messageTopic.setFromUserId(user.getId());		
				messageTopic.setDomainKey(domainConfiguration.getKey());
			}
			
			messageTopic = messageManagement.createMessageTopic(messageTopic);
			
			List<MessageTopicData> messageTopics = new ArrayList<MessageTopicData>();
			messageTopics.add(messageTopic);
			
			response.setMessageTopic(messageTopic);
			response.setEntities(messageManagement.getMessageTopicEntities(messageTopics, user));
			response.setUserGroups(messageManagement.getMessageTopicUserGroups(messageTopics, user));
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Message Topic: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/comment", method=RequestMethod.POST)
	@ResponseBody
	public WSMessageCommentResponse createMessageComment(@RequestBody MessageCommentData messageComment, HttpSession session, HttpServletRequest request) {
		
		WSMessageCommentResponse response = new WSMessageCommentResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			if (messageComment.getId() == 0) {
				messageComment.setId(null);
				messageComment.setUserProfile(user.getSelectedRole());
				messageComment.setUserEntityId(user.getSelectedEntityId());
				messageComment.setUserId(user.getId());
				messageComment.setDomainKey(domainConfiguration.getKey());
			}
			
			response.setMessageComment(messageManagement.createMessageComment(messageComment));
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Message Comment: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/markAsRead", method=RequestMethod.PUT)
	@ResponseBody
	public GenericResponse markAsRead(@RequestBody List<Long> notificationsIds, HttpSession session) {
		
		GenericResponse response = new GenericResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			messageManagement.markNotificationsAsRead(notificationsIds, user);
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error marking notification as read: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get URL to upload message attachment
	 */
	@RequestMapping(value = "/attachment/url", method=RequestMethod.GET)
	@ResponseBody
	public UrlResponse getAttachmentUrl(HttpSession session) {
		
		UrlResponse response = new UrlResponse();		
		response.setUrl(fileManagement.getMessageAttachmentUploadUrl(UrlMapping.MESSAGES_SERVICE + "/attachment/upload"));
		return response;	
	}
	
	/*
	 * Process uploaded message attachment
	 */
	@RequestMapping(value = "/attachment/upload", method=RequestMethod.POST)
	@ResponseBody
	public WSMessageAttachmentResponse processUploadedAttachment(HttpServletRequest request) {
		WSMessageAttachmentResponse response = new WSMessageAttachmentResponse();
		
		try {
			UploadedFileData file = fileManagement.getUploadedFileInfo(request, "uploadedFile");	
			response.setAttachment(messageManagement.createAttachment(file));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.FAIL_TO_UPLOAD_ATTACHMENT.code());
			rm.setMessage(ResponseCodesEnum.FAIL_TO_UPLOAD_ATTACHMENT.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error uploading message attachment: " + e.toString());
		}
		
		return response;	
	}
	
	@RequestMapping(value="/attachment/download/{id}", method=RequestMethod.GET)
	public void getFile(@PathVariable Long id, HttpServletResponse response) {
		MessageAttachmentData file = messageManagement.getAttachment(id);
		fileManagement.readFile(file.getFileKey(), file.getName(), true, response);
	}
}
