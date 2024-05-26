package br.com.usasistemas.usaplatform.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.dao.LetsEncryptChallengeDAO;
import br.com.usasistemas.usaplatform.model.data.SupplierCategoryData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.HOME)
public class HomeController {
	
	private FranchiseeManagementBO franchiseeManagement;
	private SupplierManagementBO supplierManagement;
	private LetsEncryptChallengeDAO letsEncryptChallengeDAO;
	private ConfigurationManagementBO configurationManagement;
	
	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public SupplierManagementBO getSupplierManagement() {
		return supplierManagement;
	}

	public void setSupplierManagement(SupplierManagementBO supplierManagement) {
		this.supplierManagement = supplierManagement;
	}

	public LetsEncryptChallengeDAO getLetsEncryptChallengeDAO() {
		return letsEncryptChallengeDAO;
	}

	public void setLetsEncryptChallengeDAO(LetsEncryptChallengeDAO letsEncryptChallengeDAO) {
		this.letsEncryptChallengeDAO = letsEncryptChallengeDAO;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	@RequestMapping(method=RequestMethod.GET)
	public String Home(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		if (user.getSelectedRole() == UserProfileEnum.ADMINISTRATOR) {
			view = UrlMapping.ADMIN_HOME_VIEW;
		}
		
		if (user.getSelectedRole() == UserProfileEnum.FRANCHISOR || user.getSelectedRole() == UserProfileEnum.FRANCHISEE) {
			
			List<SupplierCategoryData> supplierCategories = supplierManagement.getSupplierCategoriesByDomainKey(configurationManagement.getDomainConfigurationByURL(request.getServerName()).getKey());
			Collections.sort(supplierCategories, new Comparator<SupplierCategoryData>() {
		        @Override
		        public int compare(SupplierCategoryData category1, SupplierCategoryData category2)
		        {
		            return  category1.getOrder().compareTo(category2.getOrder());
		        }
		    });
			
			if (user.getSelectedRole() == UserProfileEnum.FRANCHISOR) {
				model.addAttribute("supplierCategories", supplierCategories);
				view = UrlMapping.FRANCHISOR_HOME_VIEW;
			}
			
			if (user.getSelectedRole() == UserProfileEnum.FRANCHISEE) {
				for (int i=0; i < supplierCategories.size(); i++) {
					if (!supplierCategories.get(i).getVisibleToFranchisees()) {
						supplierCategories.remove(i);
					}
				}
				model.addAttribute("supplierCategories", supplierCategories);
				model.addAttribute("franchisee", franchiseeManagement.getFranchisee(user.getFranchisee().getFranchiseeId()));
				view = UrlMapping.FRANCHISEE_HOME_VIEW;
			}
		}
		
		if (user.getSelectedRole() == UserProfileEnum.SUPPLIER) {
			SupplierData supplier = supplierManagement.getSupplier(user.getSupplier().getSupplierId());
			model.addAttribute("supplier", supplier);
			model.addAttribute("supplierCategory", supplierManagement.getCategory(supplier.getCategoryId()));
			view = UrlMapping.SUPPLIER_HOME_VIEW;
		}
		
		return view;
	}
	
	@RequestMapping(value = "/{customId}", method=RequestMethod.GET)
	public String HomeCustom(@PathVariable String customId, Model model, HttpSession session) {
		
		//If the request was received here is because user is already logged and is trying to access
		//the system with some custom franchisor home URL or invalid URL, so redirect to user home
		
		return "redirect:"+UrlMapping.HOME;
	}
	
	@RequestMapping(value = ".well-known/acme-challenge/{challenge}", method=RequestMethod.GET, produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String challenge(@PathVariable String challenge, HttpSession session) {
		return letsEncryptChallengeDAO.findByChallenge(challenge).getResponse();
	}
	
	@RequestMapping(value = "messages", method=RequestMethod.GET)
	public String Messages(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.MESSAGES_VIEW;
	
		return view;
	}

	@RequestMapping(value = "messageLabels", method=RequestMethod.GET)
	public String MessageLabels(Model model, HttpSession session) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		view = UrlMapping.MESSAGE_LABELS_VIEW;
	
		return view;
	}

	@RequestMapping(value = "error", method=RequestMethod.GET)
	public String Error(Model model, HttpSession session, HttpServletRequest request) {
		
		String view = null;
		
		//Get Logged User
		UserProfileData user = SessionUtil.getLoggedUser(session);
		model.addAttribute("user", user);
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		view = UrlMapping.ERROR_VIEW;
	
		return view;
	}
	
}
