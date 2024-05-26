package br.com.usasistemas.usaplatform.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.google.appengine.api.modules.ModulesService;
import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.utils.SystemProperty;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.AnnouncementManagementBO;
import br.com.usasistemas.usaplatform.bizo.CalendarManagementBO;
import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.MessageManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.ReviewManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.model.enums.OperationTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

@Controller
@RequestMapping(value = UrlMapping.ASYNC_JOB)
public class AsyncJob {
	
	private static final Logger log = Logger.getLogger(AsyncJob.class.getName());
	private ProductManagementBO productManagement;
	private MessageManagementBO messageManagement;
	private CalendarManagementBO calendarManagement;
	private AnnouncementManagementBO announcementManagement;
	private ReviewManagementBO reviewManagement;
	private DeliveryManagementBO deliveryManagement;
	private FileManagementBO fileManagement;
	
	private static final Long CLEANUP_THRESHOLD = 7L;
	
	public ProductManagementBO getProductManagement() {
		return productManagement;
	}

	public void setProductManagement(ProductManagementBO productManagement) {
		this.productManagement = productManagement;
	}

	public MessageManagementBO getMessageManagement() {
		return messageManagement;
	}

	public void setMessageManagement(MessageManagementBO messageManagement) {
		this.messageManagement = messageManagement;
	}

	public CalendarManagementBO getCalendarManagement() {
		return calendarManagement;
	}

	public void setCalendarManagement(CalendarManagementBO calendarManagement) {
		this.calendarManagement = calendarManagement;
	}

	public AnnouncementManagementBO getAnnouncementManagement() {
		return announcementManagement;
	}

	public void setAnnouncementManagement(AnnouncementManagementBO announcementManagement) {
		this.announcementManagement = announcementManagement;
	}

	public ReviewManagementBO getReviewManagement() {
		return reviewManagement;
	}

	public void setReviewManagement(ReviewManagementBO reviewManagement) {
		this.reviewManagement = reviewManagement;
	}

	public DeliveryManagementBO getDeliveryManagement() {
		return deliveryManagement;
	}

	public void setDeliveryManagement(DeliveryManagementBO deliveryManagement) {
		this.deliveryManagement = deliveryManagement;
	}

	public FileManagementBO getFileManagement() {
		return fileManagement;
	}

	public void setFileManagement(FileManagementBO fileManagement) {
		this.fileManagement = fileManagement;
	}

	@RequestMapping(value = "/duplicateFranchisor/{sourceFranchisorId}/{targetFranchisorId}", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse duplicateFranchisor(@PathVariable Long sourceFranchisorId, @PathVariable Long targetFranchisorId) {
		
		log.info("Running duplicate Franchisor " + sourceFranchisorId + " to " + targetFranchisorId + "...");
		
		//duplicate franchisor product categories. products and product sizes
		productManagement.duplicateFranchisorProductsData(sourceFranchisorId, targetFranchisorId);
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/newMessageComment/{messageCommentId}", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse newMessageComment(@PathVariable Long messageCommentId) {
		
		log.info("Running newMessageComment for id " + messageCommentId + "...");
		
		messageManagement.createMessageCommentAsync(messageCommentId);
		
		return new GenericResponse();
	}

	@RequestMapping(value = "/deleteMessageTopicLabel/{labelKey}", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse deleteMessageTopicLabel(@PathVariable String labelKey) {
		
		log.info("Running deleteMessageTopicLabel for labelKey " + labelKey + "...");
		
		messageManagement.deleteMessageTopicLabelAsync(labelKey);
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/newAnnouncement/{announcementId}", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse newAnnouncement(@PathVariable Long announcementId) {
		
		log.info("Running newAnnouncement for id " + announcementId + "...");
		
		announcementManagement.createAnnouncementAsync(announcementId);
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/processCalendarEvent/{calendarEventId}/{calendarEventHistoryId}/{operation}", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse processCalendarEvent(@PathVariable Long calendarEventId, @PathVariable Long calendarEventHistoryId, @PathVariable OperationTypeEnum operation) {
		
		log.info("Running processCalendarEvent for calendarEventId " + calendarEventId + ", calendarEventHistoryId " + calendarEventHistoryId + " and operation " + operation + "...");
		
		calendarManagement.processCalendarEventAsync(calendarEventId, calendarEventHistoryId, operation);
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/cleanUpUnusedAttachments", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse cleanUpUnusedAttachments() {
		
		GenericResponse response = new GenericResponse();
		
		log.info("Triggering unused attachments clean up...");
		
		try {			
			messageManagement.cleanUpUnusedAttachments(CLEANUP_THRESHOLD);
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error when cleaning up unused attachments: " + e.toString());
		}		
				
		return response;
	}
	
	@RequestMapping(value = "/processSupplierReviewRequest/{id}", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse processSupplierReviewRequest(@PathVariable Long id) {
		
		log.info("Running processSupplierReviewRequest for id " + id + "...");
		
		reviewManagement.processSupplierReviewRequesttAsync(id);
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/checkAndDeleteReplacedPaymentSlip/{paymentSlipKey}", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse checkAndDeleteReplacedPaymentSlip(@PathVariable String paymentSlipKey) {
		
		log.info("Running checkAndDeleteReplacedPaymentSlip for paymentSlipKey " + paymentSlipKey + "...");
		
		deliveryManagement.checkAndDeleteReplacedPaymentSlip(paymentSlipKey);
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/checkAndDeleteReplacedFiscalFile/{fiscalFileKey}", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse checkAndDeleteReplacedFiscalFile(@PathVariable String fiscalFileKey) {
		
		log.info("Running checkAndDeleteReplacedFiscalFile for fiscalFileKey " + fiscalFileKey + "...");
		
		deliveryManagement.checkAndDeleteReplacedFiscalFile(fiscalFileKey);
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/backup", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse backup() {
		
		GenericResponse response = new GenericResponse();
		
		log.info("Triggering backup for datastore entities...");
		
		try {
			
			String storageBucketName = SystemProperty.applicationId.get()+".appspot.com";
			String date = new SimpleDateFormat("'datastore_backup_'yyyy_MM_dd").format(new Date());
			
			ModulesService moduleService = ModulesServiceFactory.getModulesService();
			TaskOptions options = TaskOptions.Builder.withUrl("/_ah/datastore_admin/backup.create");
			options.param("name", "DailyBackup");
			options.param("filesystem", "gs");
			options.param("gs_bucket_name", storageBucketName+"/autobackups/"+date);
			options.header("Host", moduleService.getVersionHostname("default", "ah-builtin-python-bundle"));
			
			options.param("kind", "AdministratorUser");
			options.param("kind", "CalendarEvent");
			options.param("kind", "CalendarEventHistory");
			options.param("kind", "City");
			options.param("kind", "DeliveriesBySupplierReport");
			options.param("kind", "DeliveryRequest");
			options.param("kind", "DeliveryRequestHistory");
			options.param("kind", "DocumentsFile");
			options.param("kind", "DocumentsFolder");
			options.param("kind", "Franchisee");
			options.param("kind", "FranchiseeUser");
			options.param("kind", "Franchisor");
			options.param("kind", "FranchisorUser");
			options.param("kind", "LetsEncryptChallenge");
			options.param("kind", "ManufactureRequest");
			options.param("kind", "ManufactureRequestHistory");
			options.param("kind", "MessageComment");
			options.param("kind", "MessageTopic");
			options.param("kind", "Notification");
			options.param("kind", "PasswordReset");
			options.param("kind", "Product");
			options.param("kind", "ProductCategory");
			options.param("kind", "ProductSize");
			options.param("kind", "ProductSizePriceHistory");
			options.param("kind", "State");
			options.param("kind", "StockConsolidation");
			options.param("kind", "Supplier");
			options.param("kind", "SupplierCategory");
			options.param("kind", "SupplierDeliveriesByTimeRangeReport");
			options.param("kind", "SupplierFranchisor");
			options.param("kind", "SupplierUser");
			options.param("kind", "SystemConfiguration");
			options.param("kind", "Tutorial");
			options.param("kind", "User");
			options.param("kind", "UserGroup");
			options.param("kind", "UserGroupEntityUser");
			
			Queue queue = QueueFactory.getDefaultQueue();
			queue.add(options);

		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error when triggering backup: " + e.toString());
		}		
				
		return response;
	}

	@RequestMapping(value = "/cleanUpUnusedUploadedFiles", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse cleanUpUnusedUploadedFiles() {
		
		GenericResponse response = new GenericResponse();
		
		log.info("Triggering unused uploaded file clean up...");
		
		try {			
			fileManagement.cleanUpUnusedUploadedFiles(CLEANUP_THRESHOLD);
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error when cleaning up unused uploaded files: " + e.toString());
		}		
				
		return response;
	}
}
