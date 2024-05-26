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
@RequestMapping(value = UrlMapping.ADMIN)
public class AdminController {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(AdminController.class.getName());
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

	@RequestMapping(value = "/landing", method=RequestMethod.GET)
	public String requests(Model model, HttpSession session) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		view = UrlMapping.ADMIN_LANDING_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.ADMINISTRATOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.FRANCHISEES, method=RequestMethod.GET)
	public String Franchiseees(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISEES_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.FRANCHISEES+"/users", method=RequestMethod.GET)
	public String FranchiseeUsers(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISEE_USERS_VIEW;
		
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
		
		view = UrlMapping.SUPPLIERS_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.SUPPLIERS+"/categories", method=RequestMethod.GET)
	public String SupplierCategoies(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.SUPPLIER_CATEGORIES_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.SUPPLIERS+"/users", method=RequestMethod.GET)
	public String SupplierUsers(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.SUPPLIER_USERS_VIEW;
		
		return view;
	}	
	
	@RequestMapping(value = UrlMapping.SUPPLIERS+"/franchisors", method=RequestMethod.GET)
	public String SupplierFranchisors(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.SUPPLIER_FRANCHISORS_VIEW;
		
		return view;
	}	
	
	@RequestMapping(value = UrlMapping.FRANCHISORS, method=RequestMethod.GET)
	public String Franchisores(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISORS_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.FRANCHISORS+"/users", method=RequestMethod.GET)
	public String FranchisorUsers(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.FRANCHISOR_USERS_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.FRANCHISORS+"/products", method=RequestMethod.GET)
	public String getFranchisorProducts(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.PRODUCTS_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.FRANCHISORS+"/productCategories", method=RequestMethod.GET)
	public String getFranchisorProductCategories(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.PRODUCT_CATEGORIES_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.USERS, method=RequestMethod.GET)
	public String Users(Model model, HttpSession session) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		view = UrlMapping.ADMIN_USERS_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.USERS+"/groups", method=RequestMethod.GET)
	public String UserGroups(Model model, HttpSession session) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		view = UrlMapping.ADMIN_USER_GROUPS_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = "/deliveriesBySupplierReport", method=RequestMethod.GET)
	public String DeliveriesBySupplierReport(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.ADMIN_DELIVERIES_BY_SUPPLIER_REPORT_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.TUTORIALS, method=RequestMethod.GET)
	public String Tutorials(Model model, HttpSession session) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		view = UrlMapping.ADMIN_TUTORIALS_VIEW;
		
		return view;
	}
	
	@RequestMapping(value = UrlMapping.SYSTEM_CONFIGURATION, method=RequestMethod.GET)
	public String SystemConfigurations(Model model, HttpSession session) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		view = UrlMapping.ADMIN_SYSTEM_CONFIGURATION_VIEW;
		
		return view;
	}
}
