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
@RequestMapping(value = UrlMapping.FRANCHISEE)
public class FranchiseeController {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(FranchiseeController.class.getName());
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
				
		
		view = UrlMapping.FRANCHISEE_MAIN_VIEW;
		
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
		
		view = UrlMapping.FRANCHISEE_PRODUCTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISEE) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/myDeliveryRequests", method=RequestMethod.GET)
	public String myDeliveryRequets(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISEE_MY_DELIVERY_REQUESTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISEE) {
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
		
		view = UrlMapping.FRANCHISEE_DOCUMENTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISEE) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/accountsPayableReport", method=RequestMethod.GET)
	public String accountsPayableReport(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISEE_ACCOUNTS_PAYABLE_REPORT_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISEE && user.getSelectedRole() != UserProfileEnum.ADMINISTRATOR) {
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
		
		view = UrlMapping.FRANCHISEE_ANNOUNCEMENTS_VIEW;
	
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
		
		view = UrlMapping.FRANCHISEE_CALENDAR_VIEW;
	
		return view;
	}
	
	@RequestMapping(value = "/franchiseeDeliveriesByTimeRangeReport", method=RequestMethod.GET)
	public String franchiseeDeliveriesByTimeRangeReport(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISEE_DELIVERY_TIME_RANGE_REPORT_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.FRANCHISEE) {
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
		
		view = UrlMapping.FRANCHISEE_TUTORIALS_VIEW;
		
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
		
		view = UrlMapping.FRANCHISEE_TRAININGS_VIEW;
		
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
		
		view = UrlMapping.FRANCHISEE_SUPPLIERS_VIEW;
		
		return view;
	}
	
}
