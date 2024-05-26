package br.com.usasistemas.usaplatform.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;

@Controller
@RequestMapping(value = UrlMapping.LOGIN)
public class LoginController {
	
	private static final Logger log = Logger.getLogger(LoginController.class.getName());
	private UserManagementBO userManagement;
	private FranchisorManagementBO franchisorManagement;
	private ConfigurationManagementBO configurationManagement;
	
	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManagement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	@RequestMapping(method=RequestMethod.GET)
	public String home(@RequestParam(required = false) String expired, Model model, HttpServletRequest request) {
		
		//TODO: check how to handle this
		/*
		if (expired != null && expired.equals("Y")){
			model.addAttribute("errorMsg", "Sua sessão expirou! Faça login novamente...");
		}*/
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		return UrlMapping.LOGIN_HOME_VIEW;
			
	}
	
	@RequestMapping(value = "/{customId}", method=RequestMethod.GET)
	public String customLogin(@PathVariable String customId, Model model, HttpServletRequest request) {
		
		//add domain configuration for the JSPs
		model.addAttribute("domainConfiguration", configurationManagement.getDomainConfigurationByURL(request.getServerName()));
		
		FranchisorData franchisor = franchisorManagement.getFranchisorByLoginURL(customId);
		if (franchisor != null) {
			model.addAttribute("franchisor", franchisor);
			log.info("Custom Login Page - Found franchisor: " + franchisor.getName());
			return UrlMapping.LOGIN_HOME_VIEW;
		} else {
			log.info("Custom Login Page - No franchisor found for loginURL: " + customId);
			return "redirect:" + UrlMapping.LOGIN+"#";
		}		
	}
	
	@RequestMapping(value = "/loginForm", method=RequestMethod.GET)
	public String loginForm(Model model) {
		return UrlMapping.LOGIN_FORM_VIEW;		
	}
	
	@RequestMapping(value = "/loginFormDevelopment", method=RequestMethod.GET)
	public String loginFormDevelopment(Model model) {		
		return UrlMapping.LOGIN_FORM_DEVELOPMENT_VIEW;		
	}
	
	@RequestMapping(value = "/loginFormUsaFood", method=RequestMethod.GET)
	public String loginFormUsaFood(Model model) {		
		return UrlMapping.LOGIN_FORM_USA_FOOD_VIEW;		
	}
	
	@RequestMapping(value = "/loginFormFoodHubs", method=RequestMethod.GET)
	public String loginFormFoodHubs(Model model) {		
		return UrlMapping.LOGIN_FORM_FOODHUBS_VIEW;		
	}
	
	@RequestMapping(value = "/loginFormUsaFranquias", method=RequestMethod.GET)
	public String loginFormUsaFranquias(Model model, HttpServletRequest request) {	
		
		String serverUrl = request.getServerName();
		String[] urlSplit = serverUrl.split("\\.");
		if (urlSplit.length > 0 && urlSplit[0].equals("luzdalua")) {
			log.info("Custom domain found: " + urlSplit[0]);
			return UrlMapping.LOGIN_FORM_USA_FRANQUIAS_LUZDALUA_VIEW;
		} else {
			return UrlMapping.LOGIN_FORM_USA_FRANQUIAS_VIEW;
		}		
	}

	@RequestMapping(value = "/loginFormIntervencaoComportamental", method=RequestMethod.GET)
	public String loginFormIntervencaoComportamental(Model model) {	
		return UrlMapping.LOGIN_FORM_INTERVENCAO_COMPORTAMENTAL_VIEW;	
	}
	
	@RequestMapping(value = "/loginFormUsaSistemas", method=RequestMethod.GET)
	public String loginFormUsaSistemas(Model model) {		
		return UrlMapping.LOGIN_FORM_USA_SISTEMAS_VIEW;		
	}
	
	@RequestMapping(value = "/loginFormCustom", method=RequestMethod.GET)
	public String loginFormCustom(Model model) {		
		return UrlMapping.LOGIN_FORM_CUSTOM_VIEW;		
	}
	
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:"+UrlMapping.LOGIN;		
	}
	
	@RequestMapping(value = "/resetPasswordForm", method=RequestMethod.GET)
	public String resetPasswordForm(Model model) {
		return UrlMapping.RESET_PASSWORD_FORM_VIEW;		
	}
	
	@RequestMapping(value = "/changePasswordForm/{uid}", method=RequestMethod.GET)
	public String changePassword(@PathVariable String uid, Model model) {
		
		//validate reset password request
		PublicUserData user = this.userManagement.validatePasswordReset(uid);
		
		if (user != null) {
			//password reset valid - redirect to password change form
			return UrlMapping.CHANGE_PASSWORD_FORM_VIEW;
		}
		
		//model.addAttribute("errorMsg", "Solicitação de troca de senha inválida ou expirada");
		return UrlMapping.LOGIN_FORM_VIEW;		
	}
	
	@RequestMapping(value = "/profileSelection", method=RequestMethod.GET)
	public String profileSelection(Model model) {
		return UrlMapping.PROFILE_SELECTION_VIEW;
	}
	
}
