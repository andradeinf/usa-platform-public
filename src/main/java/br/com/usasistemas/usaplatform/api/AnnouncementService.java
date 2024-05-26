package br.com.usasistemas.usaplatform.api;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.DocumentsUrlResponse;
import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.api.response.UrlResponse;
import br.com.usasistemas.usaplatform.api.response.WSAnnouncementNotificationMenu;
import br.com.usasistemas.usaplatform.api.response.WSAnnouncementServiceAnnouncementListResponse;
import br.com.usasistemas.usaplatform.api.response.WSAnnouncementServiceAnnouncementResponse;
import br.com.usasistemas.usaplatform.api.response.WSNotificationListResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.AnnouncementManagementBO;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.ImageManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.dao.response.AnnouncementPagedResponse;
import br.com.usasistemas.usaplatform.model.data.AnnouncementData;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

@Controller
@RequestMapping(value = UrlMapping.ANNOUNCEMENT_SERVICE)
public class AnnouncementService {
	
	private static final Logger log = Logger.getLogger(AnnouncementService.class.getName());
	private FileManagementBO fileManagement;
	private AnnouncementManagementBO announcementManagement;
	private ImageManagementBO imageManagement;
	private ConfigurationManagementBO configurationManagement;
	
	public FileManagementBO getFileManagement() {
		return fileManagement;
	}

	public void setFileManagement(FileManagementBO fileManagement) {
		this.fileManagement = fileManagement;
	}
	
	public AnnouncementManagementBO getAnnouncementManagement() {
		return announcementManagement;
	}

	public void setAnnouncementManagement(AnnouncementManagementBO announcementManagement) {
		this.announcementManagement = announcementManagement;
	}
	
	public ImageManagementBO getImageManagement() {
		return imageManagement;
	}

	public void setImageManagement(ImageManagementBO imageManagement) {
		this.imageManagement = imageManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	/*
	 * Get URL to upload file
	 */
	@RequestMapping(value = "/url", method=RequestMethod.GET)
	@ResponseBody
	public DocumentsUrlResponse getDocumentUrl(HttpSession session) {
		
		DocumentsUrlResponse response = new DocumentsUrlResponse();		
		response.setUrl(fileManagement.getAnnouncementImageUploadUrl(UrlMapping.ANNOUNCEMENT_SERVICE + "/upload"));
		return response;	
	}
	
	/*
	 * Process uploaded file
	 */
	@RequestMapping(value = "/upload", method=RequestMethod.POST)
	@ResponseBody
	public UrlResponse processUploadedCatalog(HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {
		UrlResponse response = new UrlResponse();
		
		try {

			UploadedFileData file = fileManagement.getUploadedFileInfo(request, "uploadedFile");		
			
			if (file != null) {
				
				// check if format is allowed, otherwise delete
				if (!file.getContentType().equals("image/jpeg") &&
						!file.getContentType().equals("image/png")) {
					log.info("Invalid file format uploaded for announcement: " + file.getContentType());
					fileManagement.deleteFile(file.getFileKey());
					httpResponse.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
				}
				
				response.setUrl(imageManagement.getImageServingUrl(file.getFileKey()));
			}
			
		} catch (Exception e) {
			log.warning("Error uploading announcement attachment: " + e.toString());
		}
		
		return response;	
	}
	
	@RequestMapping(value="/image/{key}", method=RequestMethod.GET)
	public void getImage(@PathVariable String key, HttpServletResponse response) {
		fileManagement.readFile(key, null, false, response);
	}

	/*
	 * Get Franchisor Announcements
	 */
	@RequestMapping(value = "/franchisor/{franchisorId}/paged/{page}/pageSize/{pageSize}", method=RequestMethod.GET)
	@ResponseBody
	public WSAnnouncementServiceAnnouncementListResponse getFranchisorAnnouncementList(@PathVariable Long franchisorId, @PathVariable Long page, @PathVariable Long pageSize, HttpSession session) {
		WSAnnouncementServiceAnnouncementListResponse response = new WSAnnouncementServiceAnnouncementListResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);

			if (!user.getFeatureFlags().getFlagAnnouncement()) {

				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.GENERIC_SUCCESS.code());
				rm.setMessage(ResponseCodesEnum.GENERIC_SUCCESS.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access announcements!");

			} else {

				AnnouncementPagedResponse announcementsResponse = announcementManagement.getFranchisorAnnouncements(franchisorId, user, pageSize, page);
				response.setAnnouncementList(announcementsResponse.getAnnouncements());
				response.setHasMore(announcementsResponse.getHasMore());
				
				response.setAnnouncementUnreadNotifications(announcementManagement.getAnnouncementUnreadNotifications(response.getAnnouncementList(), user));
			}			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Announcement List: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{announcementId}/notifications/franchisee/{franchiseeId}", method=RequestMethod.GET)
	@ResponseBody
	public WSNotificationListResponse listAnnouncementNotifications(@PathVariable Long announcementId, @PathVariable Long franchiseeId) {
		
		WSNotificationListResponse response = new WSNotificationListResponse();
		
		try {
			List<NotificationData> announcementNotifications = announcementManagement.getAnnouncemnetNotifications(announcementId, franchiseeId);
			response.setNotifications(announcementNotifications);
			response.setUsers(announcementManagement.getAnnouncementNotificationUsers(announcementNotifications));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Announcemnet Notifications: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSAnnouncementServiceAnnouncementResponse postAnnouncement(@RequestBody AnnouncementData announcement, HttpServletRequest request) {
		
		WSAnnouncementServiceAnnouncementResponse response = new WSAnnouncementServiceAnnouncementResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			if (announcement.getId() == 0) {
				announcement.setId(null);	
				announcement.setDomainKey(domainConfiguration.getKey());
			}
			
			response.setAnnouncement(announcementManagement.saveAnnouncement(announcement));
		
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Announcement: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	public WSAnnouncementServiceAnnouncementResponse putAnnouncement(@RequestBody AnnouncementData announcement) {
		
		WSAnnouncementServiceAnnouncementResponse response = new WSAnnouncementServiceAnnouncementResponse();
		
		try {	
			response.setAnnouncement(announcementManagement.saveAnnouncement(announcement));		
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Announcement: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSAnnouncementServiceAnnouncementResponse delete(@PathVariable Long id) {
		
		WSAnnouncementServiceAnnouncementResponse response = new WSAnnouncementServiceAnnouncementResponse();
		
		try {
			response.setAnnouncement(announcementManagement.deleteAnnouncement(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Announcement: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/getNewAnnouncementCount", method=RequestMethod.GET)
	@ResponseBody
	public WSAnnouncementNotificationMenu getNewAnnouncementCount(HttpSession session) {
		
		WSAnnouncementNotificationMenu response = new WSAnnouncementNotificationMenu();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			response.setNewAnnouncementCount(announcementManagement.getAnnouncementUnreadNotificationsCount(user));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Announcement Unread Notification information: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/publish", method=RequestMethod.PUT)
	@ResponseBody
	public WSAnnouncementServiceAnnouncementResponse publish(@RequestBody AnnouncementData announcement) {

		WSAnnouncementServiceAnnouncementResponse response = new WSAnnouncementServiceAnnouncementResponse();
		
		try {
			response.setAnnouncement(announcementManagement.publishAnnouncement(announcement));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error marking announcement as published: " + e.toString());
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
			announcementManagement.markNotificationsAsRead(notificationsIds, user);
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
}
