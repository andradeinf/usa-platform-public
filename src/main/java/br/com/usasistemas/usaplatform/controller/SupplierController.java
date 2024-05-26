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
@RequestMapping(value = UrlMapping.SUPPLIER)
public class SupplierController {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(SupplierController.class.getName());
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

	@RequestMapping(value = "/requests", method=RequestMethod.GET)
	public String requests(Model model, HttpSession session) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		view = UrlMapping.SUPPLIER_REQUESTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.SUPPLIER && user.getSelectedRole() != UserProfileEnum.ADMINISTRATOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/deliveryRequests", method=RequestMethod.GET)
	public String deliveryRequests(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
				
				
		view = UrlMapping.SUPPLIER_DELIVERY_REQUESTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.SUPPLIER && user.getSelectedRole() != UserProfileEnum.ADMINISTRATOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/deliveryRequestsPrint", method=RequestMethod.GET)
	public String deliveryRequestsPrint(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.SUPPLIER_DELIVERY_REQUESTS_PRINT_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.SUPPLIER && user.getSelectedRole() != UserProfileEnum.ADMINISTRATOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/manufactureRequests", method=RequestMethod.GET)
	public String manufactureRequests(Model model, HttpSession session) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		view = UrlMapping.SUPPLIER_MANUFACTURE_REQUESTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.SUPPLIER && user.getSelectedRole() != UserProfileEnum.ADMINISTRATOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
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
		
		view = UrlMapping.SUPPLIER_PRODUCTS_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.SUPPLIER) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/stock", method=RequestMethod.GET)
	public String stock(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.SUPPLIER_STOCK_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.SUPPLIER && user.getSelectedRole() != UserProfileEnum.ADMINISTRATOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/deliveryReport", method=RequestMethod.GET)
	public String deliveryReport(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.SUPPLIER_DELIVERY_REPORT_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.SUPPLIER && user.getSelectedRole() != UserProfileEnum.ADMINISTRATOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/supplierDeliveriesByTimeRangeReport", method=RequestMethod.GET)
	public String supplierDeliveriesByTimeRangeReport(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.SUPPLIER_DELIVERY_TIME_RANGE_REPORT_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.SUPPLIER && user.getSelectedRole() != UserProfileEnum.ADMINISTRATOR) {
			view = "redirect:" + UrlMapping.HOME + "#/error/notAuthorized";
		}
		
		return view;
	}
	
	@RequestMapping(value = "/accountsReceivableReport", method=RequestMethod.GET)
	public String accountsReceivableReport(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.SUPPLIER_ACCOUNTS_RECEIVABLE_REPORT_VIEW;
		
		if (user.getSelectedRole() != UserProfileEnum.SUPPLIER && user.getSelectedRole() != UserProfileEnum.ADMINISTRATOR) {
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
		
		view = UrlMapping.SUPPLIER_TUTORIALS_VIEW;
		
		return view;
	}
	
}
