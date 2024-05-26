package br.com.usasistemas.usaplatform.api;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.data.ProfileData;
import br.com.usasistemas.usaplatform.api.response.WSEnumValidValuesResponse;
import br.com.usasistemas.usaplatform.api.response.WSUserGroupListResponse;
import br.com.usasistemas.usaplatform.api.response.WSUserGroupResponse;
import br.com.usasistemas.usaplatform.api.response.WSUserServiceUserDataListResponse;
import br.com.usasistemas.usaplatform.api.response.WSUserServiceUserDataResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.EntityData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.USERS_RESOURCE)
public class UserService {
	
	private static final Logger log = Logger.getLogger(UserService.class.getName());
	private UserManagementBO userManagement;
	
	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public WSUserServiceUserDataListResponse getUsers() {
		
		WSUserServiceUserDataListResponse response = new WSUserServiceUserDataListResponse();
		
		try {
			response.setUserList(userManagement.getAllUsers());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error to get list of users: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get user by ID
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	@ResponseBody
	public WSUserServiceUserDataResponse getUserById(@PathVariable Long id) {
		
		WSUserServiceUserDataResponse response = new WSUserServiceUserDataResponse();
		
		try {
			response.setUser(userManagement.getUser(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error to get user: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get user by e-mail
	 */
	@RequestMapping(value = "/email/{email}/search", method=RequestMethod.GET)
	@ResponseBody
	public WSUserServiceUserDataResponse getUserByEmail(@PathVariable String email) {
		
		WSUserServiceUserDataResponse response = new WSUserServiceUserDataResponse();
		
		try {
			
			PublicUserData user = userManagement.getUser(email);
			
			if (user != null) {
				response.setUser(user);
			} else {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.USER_NOT_FOUND.code());
				rm.setMessage(ResponseCodesEnum.USER_NOT_FOUND.message());
				response.setReturnMessage(rm);
				log.warning("User not found for e-mail: " + email);
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error to search for user: " + e.toString());
		}
		
		return response;	
	}
	
	/*
	 * Get logged user
	 */
	@RequestMapping(value = "/logged", method=RequestMethod.GET)
	@ResponseBody
	public WSUserServiceUserDataResponse getLoggedUser(HttpSession session) {
		
		WSUserServiceUserDataResponse response = new WSUserServiceUserDataResponse();
		
		try {
			
			UserProfileData loggedUser = SessionUtil.getLoggedUser(session);
			
			PublicUserData loggedUserPublicData = new PublicUserData();
			loggedUserPublicData.setId(loggedUser.getId());
			loggedUserPublicData.setUsername(loggedUser.getUsername());
			loggedUserPublicData.setName(loggedUser.getName());
			loggedUserPublicData.setEmail(loggedUser.getEmail());
			loggedUserPublicData.setSelectedRole(loggedUser.getSelectedRole());			
			response.setUser(loggedUserPublicData);
			
			EntityData loggedUserEtity = new EntityData();
			if (loggedUser.getSelectedRole() == UserProfileEnum.FRANCHISOR) {
				loggedUserEtity.setId(loggedUser.getFranchisor().getFranchisorId());
			} else if (loggedUser.getSelectedRole() == UserProfileEnum.FRANCHISEE) {
				loggedUserEtity.setId(loggedUser.getFranchisee().getFranchiseeId());
			} else if (loggedUser.getSelectedRole() == UserProfileEnum.SUPPLIER) {
				loggedUserEtity.setId(loggedUser.getSupplier().getSupplierId());
			}			
			response.setEntity(loggedUserEtity);
			
			
			response.setUserGroups(userManagement.getSelectedRoleUserGroups(loggedUser.getSelectedEntityId(), loggedUser.getSelectedEntityUserId()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error to get user: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get User Profile Types
	 */
	@RequestMapping(value = "/userProfileTypes", method=RequestMethod.GET)
	@ResponseBody
	public WSEnumValidValuesResponse getUserProfileTypes() {
		
		WSEnumValidValuesResponse response = new WSEnumValidValuesResponse();
		
		try {
			response.setEnumValues(userManagement.getUserProfileTypes());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting User Profile Types: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{entityId}/groups", method=RequestMethod.GET)
	@ResponseBody
	public WSUserGroupListResponse getUserGroups(@PathVariable Long entityId) {
		
		WSUserGroupListResponse response = new WSUserGroupListResponse();
		
		try {
			response.setUserGroups(userManagement.getEntityUserGroups(entityId));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting User Groups: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSUserServiceUserDataResponse postUser(@RequestBody PublicUserData user, HttpSession session) {
		
		WSUserServiceUserDataResponse response = new WSUserServiceUserDataResponse();
		
		try {
			response.setUser(userManagement.saveUser(user));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error to create user: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/groups", method=RequestMethod.POST)
	@ResponseBody
	public UserGroupData postUserGoup(@RequestBody UserGroupData userGroup, HttpSession session) {
		
		if (userGroup.getId() == 0) {
			userGroup.setId(null);
		}
		
		return userManagement.saveUserGroup(userGroup);
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	public WSUserServiceUserDataResponse put(@RequestBody PublicUserData user, HttpSession session) {
		
		WSUserServiceUserDataResponse response = new WSUserServiceUserDataResponse();
		
		try {
			response.setUser(userManagement.saveUser(user));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error to update user: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/groups", method=RequestMethod.PUT)
	@ResponseBody
	public UserGroupData putUserGoup(@RequestBody UserGroupData userGroup, HttpSession session) {
		return userManagement.saveUserGroup(userGroup);
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSUserServiceUserDataResponse delete(@PathVariable Long id) {
		
		WSUserServiceUserDataResponse response = new WSUserServiceUserDataResponse();
		
		try {
			response.setUser(userManagement.deleteUser(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error to update user: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/groups/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSUserGroupResponse deleteUserGroup(@PathVariable Long id) {
		
		WSUserGroupResponse response = new WSUserGroupResponse();
		
		try {
			response.setUserGroup(userManagement.deleteUserGroup(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting User Group: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/profile", method=RequestMethod.POST)
	@ResponseBody
	public WSUserServiceUserDataResponse post(@RequestBody ProfileData profile, HttpSession session) {
		
		WSUserServiceUserDataResponse response = new WSUserServiceUserDataResponse();
		
		try {
			
			//Validate
			boolean valid = true;
			StringBuilder errorMsg = new StringBuilder();
			
			if (profile.getName() == null || profile.getName().isEmpty()) {
				valid = false;
				errorMsg.append("<li>Campo Nome não pode ser vazio</li>");
			}
			
			if (profile.getEmail() == null || profile.getEmail().isEmpty()) {
				valid = false;
				errorMsg.append("<li>Campo e-mail não pode ser vazio</li>");
			}
			
			if ((profile.getNewPassword() != null || !profile.getNewPassword().isEmpty() ||
				 profile.getNewPassword2() != null || !profile.getNewPassword2().isEmpty()) &&
				 !profile.getNewPassword().equals(profile.getNewPassword2())) {
				valid = false;
				errorMsg.append("<li>Os valores informados para nova senha não são iguais</li>");
			}			
			
			if (valid) {
				
				//Update User
				response.setUser(userManagement.updateUserProfile(profile));
				
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.PROFILE_UPDATE_SUCCESS.code());
				rm.setMessage(ResponseCodesEnum.PROFILE_UPDATE_SUCCESS.message());
				response.setReturnMessage(rm);
				
			} else {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.PROFILE_UPDATE_ERROR.code());
				rm.setMessage(ResponseCodesEnum.PROFILE_UPDATE_ERROR.message());
				rm.setDetails(errorMsg.toString());
				response.setReturnMessage(rm);
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error to update user profile: " + e.toString());
		}
		
		return response;
	}

}
