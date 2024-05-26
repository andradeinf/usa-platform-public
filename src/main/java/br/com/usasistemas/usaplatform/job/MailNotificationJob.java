package br.com.usasistemas.usaplatform.job;

import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.bizo.MailManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;

@Controller
@RequestMapping(value = UrlMapping.MAIL_NOTIFICATION_JOB)
public class MailNotificationJob {
	
	private static final Logger log = Logger.getLogger(MailNotificationJob.class.getName());
	private MailManagementBO mailManegement;
	
	public MailManagementBO getMailManegement() {
		return mailManegement;
	}

	public void setMailManegement(MailManagementBO mailManegement) {
		this.mailManegement = mailManegement;
	}
	
	@RequestMapping(value = "/checkPendingDeliveryRequestNotifications", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse checkPendingDeliveryRequestNotifications() {
		
		log.info("Check pending notifications for created Delivery Requests...");
		mailManegement.checkPendingDeliveryRequestNotifications();
		
		log.info("Check pending notifications for cancelled Delivery Requests...");
		mailManegement.checkPendingCancellationDeliveryRequestNotifications();
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/checkPendingNewFileNotifications", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse checkPendingNewFileNotifications() {
		
		log.info("Check pending notifications for created Documents File...");
		mailManegement.checkPendingNewFileNotifications();
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/checkPendingNewMessageCommentNotifications", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse checkPendingNewMessageCommentNotifications() {
		
		log.info("Check pending notifications for Message Comments...");
		mailManegement.checkPendingNewMessageCommentNotifications();
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/checkPendingProductSizePriceHistoryNotifications", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse checkPendingProductSizePriceHistoryNotifications() {
		
		log.info("Check pending notifications for Product Size Price History...");
		mailManegement.checkPendingProductSizePriceHistoryNotifications();
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/sendTestMail", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse sendTestMail() {
		
		log.info("Testing e-mail send...");
		mailManegement.sendTestMail();
		
		return new GenericResponse();
	}

}
