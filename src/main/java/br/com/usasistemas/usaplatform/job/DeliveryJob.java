package br.com.usasistemas.usaplatform.job;

import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;

@Controller
@RequestMapping(value = UrlMapping.DELIVERY_JOB)
public class DeliveryJob {
	
	private static final Logger log = Logger.getLogger(DeliveryJob.class.getName());
	private DeliveryManagementBO deliveryManagement;
	
	public DeliveryManagementBO getDeliveryManagement() {
		return deliveryManagement;
	}

	public void setDeliveryManagement(DeliveryManagementBO deliveryManagement) {
		this.deliveryManagement = deliveryManagement;
	}

	@RequestMapping(value = "/autoCancelPendingWithRestriction", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse autoCancelPendingWithRestriction() {
		
		log.info("Running auto cancellation for Pending for Restriction Delivery Requests...");
		deliveryManagement.autoCancelPendingWithRestriction();
		
		return new GenericResponse();
	}

}
