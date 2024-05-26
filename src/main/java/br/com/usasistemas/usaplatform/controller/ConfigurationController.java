package br.com.usasistemas.usaplatform.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;

@Controller
@RequestMapping(value = UrlMapping.CONFIGURATION)
public class ConfigurationController {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(LoginController.class.getName());
	private UserManagementBO userManagement;
	
	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	@RequestMapping(method=RequestMethod.GET)
	public String configuration(Model model, HttpSession session) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		view = UrlMapping.CONFIGURATION_VIEW;
		
		return view;
	}
	
}
