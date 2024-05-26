package br.com.usasistemas.usaplatform.api;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.WSTutorialListResponse;
import br.com.usasistemas.usaplatform.api.response.WSTutorialResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.TutorialManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.TutorialData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.TUTORIALS_RESOURCE)
public class TutorialService {
	
	private static final Logger log = Logger.getLogger(TutorialService.class.getName());
	private TutorialManagementBO tutorialManagement;
	private ConfigurationManagementBO configurationManagement;

	public TutorialManagementBO getTutorialManagement() {
		return tutorialManagement;
	}

	public void setTutorialManagement(TutorialManagementBO tutorialManagement) {
		this.tutorialManagement = tutorialManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	/*
	 * Get Tutorials
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public WSTutorialListResponse listAll(HttpSession session, HttpServletRequest request) {
		WSTutorialListResponse response = new WSTutorialListResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			if (user.getSelectedRole().equals(UserProfileEnum.ADMINISTRATOR)) {
				response.setTutorials(tutorialManagement.getTutorialsByDomainKey(domainConfiguration.getKey()));
			} else {				
				response.setTutorials(tutorialManagement.getTutorialsByUserProfileAndDomainKey(user.getSelectedRole(), domainConfiguration.getKey()));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Tutorials List: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Tutorials
	 */
	@RequestMapping(value = "/user", method=RequestMethod.GET)
	@ResponseBody
	public WSTutorialListResponse listByUserProfile(HttpSession session, HttpServletRequest request) {
		WSTutorialListResponse response = new WSTutorialListResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);					
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			response.setTutorials(tutorialManagement.getTutorialsByUserProfileAndDomainKey(user.getSelectedRole(), domainConfiguration.getKey()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Tutorials List: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSTutorialResponse post(@RequestBody TutorialData tutorial, HttpSession session, HttpServletRequest request) {
		
		if (tutorial.getId() == 0) {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			tutorial.setId(null);
			List<String> domainKey = new ArrayList<String>();
			domainKey.add(domainConfiguration.getKey());
			tutorial.setDomainKeys(domainKey);
		}
		
		WSTutorialResponse response = new WSTutorialResponse();

		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (!user.getSelectedRole().equals(UserProfileEnum.ADMINISTRATOR)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to create new Tutorial");
			
			} else {
				response.setTutorial(tutorialManagement.saveTutorial(tutorial));
			}
			
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Tutorial: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	public WSTutorialResponse put(@RequestBody TutorialData tutorial, HttpSession session) {
		
		WSTutorialResponse response = new WSTutorialResponse();

		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (!user.getSelectedRole().equals(UserProfileEnum.ADMINISTRATOR)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to update new Tutorial");
			
			} else {
				response.setTutorial(tutorialManagement.saveTutorial(tutorial));
			}
			
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Tutorial: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSTutorialResponse delete(@PathVariable Long id, HttpSession session) {
		
		WSTutorialResponse response = new WSTutorialResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (!user.getSelectedRole().equals(UserProfileEnum.ADMINISTRATOR)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to delete new Tutorial");
			
			} else {
				response.setTutorial(tutorialManagement.deleteTutorial(id));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Tutorial: " + e.toString());
		}
		
		return response;
	}

}
