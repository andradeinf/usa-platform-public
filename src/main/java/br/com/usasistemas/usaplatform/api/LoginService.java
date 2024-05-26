package br.com.usasistemas.usaplatform.api;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.data.LoginData;
import br.com.usasistemas.usaplatform.api.response.WSLoginResponse;
import br.com.usasistemas.usaplatform.api.response.WSLoginServiceUserProfileDataResponse;
import br.com.usasistemas.usaplatform.api.response.data.ResetPasswordData;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

@Controller
@RequestMapping(value = UrlMapping.LOGIN_SERVICE)
public class LoginService {
	
	private static final Logger log = Logger.getLogger(LoginService.class.getName());
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

	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSLoginResponse doLogin(@RequestBody LoginData loginData, HttpSession session, HttpServletRequest request) {		
		
		WSLoginResponse response = new WSLoginResponse();
		
		try {
			log.fine("Checking user credentials");
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			UserProfileData user = userManagement.authenticateUser(loginData.getEmail().toUpperCase(), loginData.getPassword(), domainConfiguration.getKey());
			
			if (user != null) {
				
				//Check if user is disabled and return error message
				if (!user.getEnabled()) {
					
					log.fine("User disabled! Going back to logging screen");
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.USER_DISABLED.code());
					rm.setMessage(ResponseCodesEnum.USER_DISABLED.message());
					response.setReturnMessage(rm);
					
				} else {
					
					//Check if user has no profile
					if (user.getHasNoProfile()) {
						
						log.fine("User has no profiles for this domain! Going back to logging screen");
						ReturnMessage rm = new ReturnMessage();
						rm.setCode(ResponseCodesEnum.INVALID_USER_PASSWORD.code());
						rm.setMessage(ResponseCodesEnum.INVALID_USER_PASSWORD.message());
						response.setReturnMessage(rm);
						
					} else {
						
						log.fine("Create user session");
						session.setAttribute(SessionUtil.LOGGED_USER_ATTR, user);
						
						//route to user profile selection
						if (user.getHasMultipleProfiles()) {
							ReturnMessage rm = new ReturnMessage();
							rm.setCode(ResponseCodesEnum.SELECT_PROFILE.code());
							rm.setMessage(ResponseCodesEnum.SELECT_PROFILE.message());
							response.setReturnMessage(rm);
						}
					}
				}
			} else {
				log.fine("Unable to login! Going back to logging screen");
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.INVALID_USER_PASSWORD.code());
				rm.setMessage(ResponseCodesEnum.INVALID_USER_PASSWORD.message());
				response.setReturnMessage(rm);
			}
		
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			e.printStackTrace();
			log.warning("Error doing login: " + e.toString());
		}
		
		return response;	
	}

	@RequestMapping(value = "/resetPassword", method=RequestMethod.POST)
	@ResponseBody
	public WSLoginResponse doResetPasswordRequest(@RequestBody LoginData loginData, HttpServletRequest request) {
		
		WSLoginResponse response = new WSLoginResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			userManagement.resetPassword(loginData.getEmail().toUpperCase(), domainConfiguration);			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error requesting password reset: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(value = "/changePassword", method=RequestMethod.POST)
	@ResponseBody
	public WSLoginResponse doChangePassword(@RequestBody ResetPasswordData resetPasswordData) {
		
		WSLoginResponse response = new WSLoginResponse();
		
		try {
				
			//Validate
			boolean valid = true;
			StringBuilder errorMsg = new StringBuilder();
			
			if (resetPasswordData.getNewPassword() == null) {
				valid = false;
				errorMsg.append("<li>O campo nova senha não pode ser vazio</li>");
			}
			
			if (resetPasswordData.getNewPasswordConfirmation() == null) {
				valid = false;
				errorMsg.append("<li>O campo repetir nova senha não pode ser vazio</li>");
			}
			
			if (resetPasswordData.getNewPassword() != null && !resetPasswordData.getNewPassword().isEmpty() &&
				resetPasswordData.getNewPasswordConfirmation() != null && !resetPasswordData.getNewPasswordConfirmation().isEmpty() &&
				!resetPasswordData.getNewPassword().equals(resetPasswordData.getNewPasswordConfirmation())) {
				valid = false;
				errorMsg.append("<li>Os valores informados para nova senha não são iguais</li>");
			}			
			
			if (valid) {
				
				//validate reset password request
				PublicUserData user = this.userManagement.validatePasswordReset(resetPasswordData.getUid());
				if (user == null) {
					
					//password reset invalid - redirect to password change form
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.INVALID_RESET_PASSWORD_REQUEST.code());
					rm.setMessage(ResponseCodesEnum.INVALID_RESET_PASSWORD_REQUEST.message());
					response.setReturnMessage(rm);
					
				} else {
					//Update Password
					userManagement.updateUserPassword(user.getId(), resetPasswordData.getNewPassword());
					userManagement.deletePasswordResetToken(resetPasswordData.getUid());
				}
				
				
				
			} else {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.VALIDATION_ERROR.code());
				rm.setMessage(ResponseCodesEnum.VALIDATION_ERROR.message());
				rm.setDetails(errorMsg.toString());
				response.setReturnMessage(rm);
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error requesting password reset: " + e.toString());
		}
		
		return response;		
	}
	
	@RequestMapping(value = "/userProfileSelection", method=RequestMethod.GET)
	@ResponseBody
	public WSLoginServiceUserProfileDataResponse getUserProfile(HttpSession session) {
		
		WSLoginServiceUserProfileDataResponse response = new WSLoginServiceUserProfileDataResponse();
		
		try {
		
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			//Get franchisee/franchisor/supplier details to show in the UI
			response.setUserProfiles(userManagement.getUserProfiles(user));
		
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting user profiles: " + e.toString());
			e.printStackTrace();
		}
		
		return response;
	}
	
	@RequestMapping(value = "/userProfileSelection/{id}", method=RequestMethod.GET)
	@ResponseBody
	public WSLoginResponse selectUserProfile(@PathVariable Long id, HttpSession session) {
		
		WSLoginResponse response = new WSLoginResponse();
		
		try {
		
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			log.fine("Update user session");
			session.setAttribute(SessionUtil.LOGGED_USER_ATTR, user.setUserProfile(id));
		
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error setting user profiles: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/isAvailable", method=RequestMethod.GET)
	public String isAvailable() {
		
		log.finest("Check if application is available...");		
		
		return UrlMapping.OK_VIEW;	
	}
	
}
