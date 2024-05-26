package br.com.usasistemas.usaplatform.api;

import java.util.Arrays;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.WSConfigurationServiceSystemConfigurationDataListResponse;
import br.com.usasistemas.usaplatform.api.response.WSConfigurationServiceSystemConfigurationDataResponse;
import br.com.usasistemas.usaplatform.api.response.WSDomainConfigurationResponse;
import br.com.usasistemas.usaplatform.api.response.WSEnumValidValuesResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.model.data.SystemConfigurationData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.SystemConfigurationEnum;

@Controller
@RequestMapping(value = UrlMapping.USERS_CONFIGURATION)
public class ConfigurationService {
	
	private static final Logger log = Logger.getLogger(ConfigurationService.class.getName());
	private ConfigurationManagementBO configurationManagement;
	
	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}
	
	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public WSConfigurationServiceSystemConfigurationDataListResponse getConfigurations() {
		
		WSConfigurationServiceSystemConfigurationDataListResponse response = new WSConfigurationServiceSystemConfigurationDataListResponse();
		
		try {
			response.setSystemConfigurationList(configurationManagement.getAllConfigurations());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error to get list of configurations: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/getSystemUpdateInfo", method=RequestMethod.GET)
	@ResponseBody
	public WSConfigurationServiceSystemConfigurationDataListResponse getSystemUpdateInfo() {
		WSConfigurationServiceSystemConfigurationDataListResponse response = new WSConfigurationServiceSystemConfigurationDataListResponse();
		
		try {
			response.setSystemConfigurationList(configurationManagement.getConfigurationsByKey(Arrays.asList(SystemConfigurationEnum.SYSTEM_UPDATE_INFO_SUMMARY.toString(), SystemConfigurationEnum.SYSTEM_UPDATE_INFO_DETAILS.toString())));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating new configuration: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/getDomainConfiguration", method=RequestMethod.GET)
	@ResponseBody
	public WSDomainConfigurationResponse getDomainConfiguration(HttpServletRequest request) {
		WSDomainConfigurationResponse response = new WSDomainConfigurationResponse();
		
		try {
			response.setDomainConfiguration(configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting domain configuration: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/types", method=RequestMethod.GET)
	@ResponseBody
	public WSEnumValidValuesResponse getStatuses() {
		
		WSEnumValidValuesResponse response = new WSEnumValidValuesResponse();
		
		try {
			response.setEnumValues(configurationManagement.getTypes());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Configuration Types: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSConfigurationServiceSystemConfigurationDataResponse postSupplier(@RequestBody SystemConfigurationData systemConfiguration, HttpSession session) {
		
		if (systemConfiguration.getId() == 0) {
			systemConfiguration.setId(null);
		}
		
		WSConfigurationServiceSystemConfigurationDataResponse response = new WSConfigurationServiceSystemConfigurationDataResponse();
		
		try {
			response.setSystemConfiguration(configurationManagement.saveConfiguration(systemConfiguration));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating new configuration: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	public WSConfigurationServiceSystemConfigurationDataResponse put(@RequestBody SystemConfigurationData systemConfiguration, HttpSession session) {
		
		WSConfigurationServiceSystemConfigurationDataResponse response = new WSConfigurationServiceSystemConfigurationDataResponse();
		
		try {
			response.setSystemConfiguration(configurationManagement.saveConfiguration(systemConfiguration));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating new configuration: " + e.toString());
		}
		
		return response;
	}

	/*
	 * Delete configuration by configuration ID
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSConfigurationServiceSystemConfigurationDataResponse delete(@PathVariable Long id) {
		
		WSConfigurationServiceSystemConfigurationDataResponse response = new WSConfigurationServiceSystemConfigurationDataResponse();
		
		try {
			response.setSystemConfiguration(configurationManagement.deleteConfiguration(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting configuration: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/domains", method=RequestMethod.GET)
	@ResponseBody
	public WSEnumValidValuesResponse getDomains() {
		
		WSEnumValidValuesResponse response = new WSEnumValidValuesResponse();
		
		try {
			response.setEnumValues(configurationManagement.getDomains());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Configuration Domains: " + e.toString());
		}
		
		return response;		
	}
}
