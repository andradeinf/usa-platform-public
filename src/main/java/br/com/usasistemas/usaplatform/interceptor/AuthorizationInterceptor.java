package br.com.usasistemas.usaplatform.interceptor;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;

public class AuthorizationInterceptor implements HandlerInterceptor {
	
	private static final Logger log = Logger.getLogger(AuthorizationInterceptor.class.getName());
	private ConfigurationManagementBO configurationManagement;

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}
	
	@Override
	  public boolean preHandle(HttpServletRequest request,
	      HttpServletResponse response,
	      Object controller) throws Exception {

		DomainConfigurationData configuration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
		if (configuration.getRedirectTo() != null) {
			DomainConfigurationData redirectToConfiguration = configurationManagement.getDomainConfigurationByKey(configuration.getRedirectTo().name());
			log.fine("Accessing old configuration [" + configuration.getName() + "]; Redirecting to new configuration [" + redirectToConfiguration.getName() + "]");
			response.sendRedirect(request.getScheme() + "://" + redirectToConfiguration.getMainURL() + ":" + request.getServerPort());
		}
		
		String uri = request.getRequestURI();

		//Do not block public resources
	    if(uri.startsWith(UrlMapping.LOGIN) || 
	       uri.startsWith(UrlMapping.LOGIN_SERVICE) ||
	       uri.startsWith(UrlMapping.CONTACT_SERVICE) ||
           uri.startsWith(UrlMapping.SETUP) ||
           uri.startsWith("/job") ||
           uri.startsWith("/.well-known") ||
	       uri.contains("css")){
	    	return true;
	    }
		
		HttpSession session = request.getSession();
		
		if (session != null) {
			
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			if (user != null) {
				
				//User logged and profile already selected
				return true;
			}
		}
		
		//User not logged and Async request received
		//Reply with 401 to tell to angular to redirect to login page
		if(uri.startsWith("/ws")){
			response.setStatus(401);
			return false;
		}
		
		//User not logged. Redirect to Login Form
		if (uri.equals("/")) {
			response.sendRedirect(UrlMapping.LOGIN);
		} else {
			response.sendRedirect(UrlMapping.LOGIN + uri + "#homeCustom");
		}
	    return false;  
	  }
	
}
