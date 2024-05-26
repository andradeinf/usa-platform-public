package br.com.usasistemas.usaplatform.api;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.data.ContactData;
import br.com.usasistemas.usaplatform.api.response.WSContactResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.MailManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

@Controller
@RequestMapping(value = UrlMapping.CONTACT_SERVICE)
public class ContactService {
	
	private static final Logger log = Logger.getLogger(ContactService.class.getName());
	private MailManagementBO mailManagement;
	private ConfigurationManagementBO configurationManagement;
	
	public MailManagementBO getMailManagement() {
		return mailManagement;
	}

	public void setMailManagement(MailManagementBO mailManagement) {
		this.mailManagement = mailManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	@RequestMapping(value = "/franchisor", method=RequestMethod.POST)
	@ResponseBody
	public WSContactResponse franchisorContact(@RequestBody ContactData contactData, HttpServletRequest request) {
		
		WSContactResponse response = new WSContactResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			mailManagement.sendContact(contactData, "FRANCHISOR", domainConfiguration);
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error sending franchisor contact e-mail: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(value = "/supplier", method=RequestMethod.POST)
	@ResponseBody
	public WSContactResponse supplierContact(@RequestBody ContactData contactData, HttpServletRequest request) {
		
		WSContactResponse response = new WSContactResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			mailManagement.sendContact(contactData, "SUPPLIER", domainConfiguration);
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error sending supplier contact e-mail: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(value = "/company", method=RequestMethod.POST)
	@ResponseBody
	public WSContactResponse companyContact(@RequestBody ContactData contactData, HttpServletRequest request) {
		
		WSContactResponse response = new WSContactResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			mailManagement.sendContact(contactData, "COMPANY", domainConfiguration);
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error sending company contact e-mail: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(value = "/franchisee", method=RequestMethod.POST)
	@ResponseBody
	public WSContactResponse newFranchisee(@RequestBody ContactData contactData, HttpServletRequest request) {
		
		WSContactResponse response = new WSContactResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			mailManagement.sendContact(contactData, "FRANCHISEE", domainConfiguration);
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error sending newFranchisee contact e-mail: " + e.toString());
		}
		
		return response;		
	}

}
