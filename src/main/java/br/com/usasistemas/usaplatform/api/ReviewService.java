package br.com.usasistemas.usaplatform.api;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.SupplierReviewUpdateRatesDataRequest;
import br.com.usasistemas.usaplatform.api.response.WSReviewRequestDetailsResponse;
import br.com.usasistemas.usaplatform.api.response.WSReviewRequestResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.ReviewManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

@Controller
@RequestMapping(value = UrlMapping.REVIEW_SERVICE)
public class ReviewService {
	
	private static final Logger log = Logger.getLogger(ReviewService.class.getName());
	private ReviewManagementBO reviewManagement;
	private ConfigurationManagementBO configurationManagement;
	
	public ReviewManagementBO getReviewManagement() {
		return reviewManagement;
	}

	public void setReviewManagement(ReviewManagementBO reviewManagement) {
		this.reviewManagement = reviewManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	@RequestMapping(value = "/getNextRequestedSupplierReviewRequests/{franchiseeId}", method=RequestMethod.GET)
	@ResponseBody
	public WSReviewRequestResponse getNextRequestedSupplierReviewRequests(@PathVariable Long franchiseeId, HttpSession session, HttpServletRequest request) {
		WSReviewRequestResponse response = new WSReviewRequestResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			if (domainConfiguration.getFeatureFlags().get("REVIEW_REQUEST")) {
				
				//Get Logged User
				UserProfileData user = SessionUtil.getLoggedUser(session);
				
				response.setReviewRequest(reviewManagement.getNextRequestedSupplierReviewRequest(franchiseeId, user.getId()));
				if (response.getReviewRequest() != null) {
					response.setSupplier(reviewManagement.getReviewRequestSupplier(response.getReviewRequest().getToEntityId()));
				}
				
			} else {
				log.fine("Feature REVIEW_REQUEST not enabled for domain " + domainConfiguration.getKey());
			}			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting next Requested Supplier Review Request: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/cancelRequestedSupplierReviewRequest/{id}", method=RequestMethod.PUT)
	@ResponseBody
	public WSReviewRequestResponse cancelRequestedSupplierReviewRequest(@PathVariable Long id, HttpSession session) {
		WSReviewRequestResponse response = new WSReviewRequestResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			response.setReviewRequest(reviewManagement.cancelRequestedSupplierReviewRequest(id, user.getId()));			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error cancelling Requested Supplier Review Request: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/addRequestedSupplierReviewRequestRates/{id}", method=RequestMethod.PUT)
	@ResponseBody
	public WSReviewRequestResponse addRequestedSupplierReviewRequestRates(@PathVariable Long id, @RequestBody SupplierReviewUpdateRatesDataRequest updateRatesRequest, HttpSession session) {
		WSReviewRequestResponse response = new WSReviewRequestResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			response.setReviewRequest(reviewManagement.addRequestedSupplierReviewRequestRates(id, user.getId(), updateRatesRequest.getQuality(), updateRatesRequest.getDelivery(), updateRatesRequest.getPrice(), updateRatesRequest.getPaymentCondition()));			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error adding rates to Requested Supplier Review Request: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/getReviewRequestDetails/{id}", method=RequestMethod.GET)
	@ResponseBody
	public WSReviewRequestDetailsResponse getReviewRequestDetails(@PathVariable Long id) {
		WSReviewRequestDetailsResponse response = new WSReviewRequestDetailsResponse();
		
		try {
			response.setDeliveryRequests(reviewManagement.getReviewRequestDeliveryRequests(id));
			response.setProducts(reviewManagement.getDeliveryRequestsProducts(response.getDeliveryRequests()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Review Request details: " + e.toString());
		}
		
		return response;
	}

}
