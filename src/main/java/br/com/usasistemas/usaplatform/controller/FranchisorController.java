package br.com.usasistemas.usaplatform.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.FRANCHISOR)
public class FranchisorController {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(FranchisorController.class.getName());
	private UserManagementBO userManagement;
	private ConfigurationManagementBO configurationManagement;
	
	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}
	
	@RequestMapping(value = "/main", method=RequestMethod.GET)
	public String Main(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_MAIN_VIEW;
		
		return view;
	}

	@RequestMapping(value = "/products", method=RequestMethod.GET)
	public String products(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_PRODUCTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/myManufactureRequests", method=RequestMethod.GET)
	public String myManufactureRequests(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_MY_MANUFACTURE_REQUESTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/franchiseesDeliveryRequests", method=RequestMethod.GET)
	public String franchiseesDeliveryRequests(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_FRANCHISEES_DELIVERY_REQUESTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/documents", method=RequestMethod.GET)
	public String documents(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_DOCUMENTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/announcements", method=RequestMethod.GET)
	public String Announcements(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_ANNOUNCEMENTS_VIEW;
	
		return view;
	}
	
	@RequestMapping(value = "/calendar", method=RequestMethod.GET)
	public String Calendar(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_CALENDAR_VIEW;
	
		return view;
	}

	@RequestMapping(value = "/deliverieRequestsByTimeRangeReport", method=RequestMethod.GET)
	public String deliverieRequestsByTimeRangeReport(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_DELIVERY_TIME_RANGE_REPORT_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}

	@RequestMapping(value = "/franchiseeMessagesByTimeRangeReport", method=RequestMethod.GET)
	public String franchiseeMessagesByTimeRangeReport(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_FRANCHISEE_MESSAGES_TIME_RANGE_REPORT_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.TUTORIALS, method=RequestMethod.GET)
	public String Tutorials(Model model, HttpSession session) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		view = UrlMapping.FRANCHISOR_TUTORIALS_VIEW;
		
		return view;
	}

	@RequestMapping(value = UrlMapping.TRAININGS, method=RequestMethod.GET)
	public String Trainings(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_TRAININGS_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.SUPPLIERS, method=RequestMethod.GET)
	public String Suppliers(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_SUPPLIERS_VIEW;
		
		return view;
	}
}
