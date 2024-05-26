package br.com.usasistemas.usaplatform.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.WSFranchisorUserRequest;
import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.api.response.UrlResponse;
import br.com.usasistemas.usaplatform.api.response.WSFranchisorUserListResponse;
import br.com.usasistemas.usaplatform.api.response.WSFranchisorUserResponse;
import br.com.usasistemas.usaplatform.api.response.WSFranchisorsServiceFranchisorResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.FranchisorUserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.FRANCHISORS_RESOURCE)
public class FranchisorsService {
	
	private static final Logger log = Logger.getLogger(FranchisorsService.class.getName());
	private FranchisorManagementBO franchisorManagement;
	private UserManagementBO userManagement;
	private ConfigurationManagementBO configurationManagement;
	
	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManagement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}

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

	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public List<FranchisorData> listFranchisors(HttpServletRequest request) {
		DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
		return franchisorManagement.getAllDomainFranchisors(domainConfiguration.getKey());	
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	@ResponseBody
	public FranchisorData getFranchisor(@PathVariable Long id) {
		return franchisorManagement.getFranchisor(id);
	}
	
	@RequestMapping(value = "/{franchisorId}/users", method=RequestMethod.GET)
	@ResponseBody
	public WSFranchisorUserListResponse getFranchisorUsers(@PathVariable Long franchisorId) {
		
		WSFranchisorUserListResponse response = new WSFranchisorUserListResponse();
		
		try {
			List<UserGroupData> franchiseeUserGroups = userManagement.getEntityUserGroups(franchisorId);
			response.setUserGroups(franchiseeUserGroups);
						
			List<FranchisorUserData> franchisorUsers = franchisorManagement.getFranchisorUsers(franchisorId);
			response.setFranchisorUsers(franchisorUsers);
			
			//create a map to have the user groups for each user
			Map<Long, Map<Long, Boolean>> franchisorUsersUserGroupsMap = new HashMap<Long, Map<Long, Boolean>>();
			if (franchisorUsers != null && !franchisorUsers.isEmpty()) {
				for (FranchisorUserData fanchisorUser: franchisorUsers) {
					
					//for every user, create a map with all user groups and initialize with false for all
					Map<Long, Boolean> userGroupMap = new HashMap<Long, Boolean>();
					if (franchiseeUserGroups != null && !franchiseeUserGroups.isEmpty()){
						for (UserGroupData franchisorUserGroup : franchiseeUserGroups) {
							userGroupMap.put(franchisorUserGroup.getId(), false);
						}
					}
					
					//get user groups and set them to true
					List<UserGroupEntityUserData> userGroupsEntityUser = userManagement.getUserGroupEntityUserByEntityUserId(fanchisorUser.getId());
					if (userGroupsEntityUser != null && !userGroupsEntityUser.isEmpty()){
						for (UserGroupEntityUserData franchisorUserGroup: userGroupsEntityUser) {
							userGroupMap.put(franchisorUserGroup.getUserGroupId(), true);
						}
					}					
					
					franchisorUsersUserGroupsMap.put(fanchisorUser.getId(), userGroupMap);
					
				}
			}
			response.setFranchisorUsersGroups(franchisorUsersUserGroupsMap);			
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Franchisor Users: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/{franchisorId}/radio", method=RequestMethod.GET)
	@ResponseBody
	public UrlResponse getFranchisorRadio(@PathVariable Long franchisorId) {

		UrlResponse response = new UrlResponse();		
		FranchisorData franchisor = franchisorManagement.getFranchisor(franchisorId);
		response.setUrl(franchisor.getRadioURL());
		return response;	
	}

	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public FranchisorData postFranchisor(@RequestBody FranchisorData franchisor, HttpServletRequest request) {
		
		if (franchisor.getId() == 0) {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			franchisor.setId(null);
			franchisor.setPreferedDomainKey(domainConfiguration.getKey());
		}
		
		return franchisorManagement.saveFranchisor(franchisor);
	}
	
	@RequestMapping(value = "/{franchisorId}/users", method=RequestMethod.POST)
	@ResponseBody
	public WSFranchisorUserResponse postFranchisorUser(@RequestBody WSFranchisorUserRequest requestData, @PathVariable Long franchisorId, HttpSession session) {
		
		WSFranchisorUserResponse response = new WSFranchisorUserResponse();
		
		try {
			
			//Add user to Franchisor		
			FranchisorUserData franchisorUser = requestData.getFranchisorUser();
			if (franchisorUser.getId() == 0) {franchisorUser.setId(null);}
			franchisorUser.setFranchisorId(franchisorId);
			franchisorUser.setUserId(requestData.getFranchisorUser().getUser().getId());
			franchisorUser = franchisorManagement.saveFranchisorUser(franchisorUser);
			response.setFranchisorUser(franchisorUser);
			
			//Add selected groups to user
			for (Long userGroupId: requestData.getUserGroups().keySet()){
				
				//True in the request, ADD to DB
				if (requestData.getUserGroups().get(userGroupId)){
					
					UserGroupEntityUserData franchisorUserGroup = new UserGroupEntityUserData();
					franchisorUserGroup.setUserGroupId(userGroupId);
					franchisorUserGroup.setEntityProfile(UserProfileEnum.FRANCHISOR);
					franchisorUserGroup.setEntityUserId(franchisorUser.getId());
					userManagement.saveUserGroupEntityUser(franchisorUserGroup);
				}
				
			}
			response.setUserGroups(requestData.getUserGroups());
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Franchisor User: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	public FranchisorData put(@RequestBody FranchisorData franchisor, HttpSession session) {
		return franchisorManagement.saveFranchisor(franchisor);
	}
	
	@RequestMapping(value = "/{franchisorId}/users", method=RequestMethod.PUT)
	@ResponseBody
	public GenericResponse putFranchisorUser(@RequestBody WSFranchisorUserRequest requestData, @PathVariable Long franchisorId, HttpSession session) {
		
		GenericResponse response = new GenericResponse();
		
		try {
			
			//get franchisorUserGroups and create a map to simplify the validation
			List<UserGroupEntityUserData> franchisorUserGroupList = userManagement.getUserGroupEntityUserByEntityUserId(requestData.getFranchisorUser().getId());
			Map<Long, Boolean> franchisorUserGroupMap = new HashMap<Long, Boolean>();
			for (UserGroupEntityUserData franchisorUserGroup: franchisorUserGroupList) {
				franchisorUserGroupMap.put(franchisorUserGroup.getUserGroupId(), true);
			}
			
			//check all groups from request
			for (Long userGroupId: requestData.getUserGroups().keySet()){
				
				//True in the request && False in the DB = Need to ADD to DB
				if (requestData.getUserGroups().get(userGroupId) && !franchisorUserGroupMap.containsKey(userGroupId)){
					
					UserGroupEntityUserData franchisorUserGroup = new UserGroupEntityUserData();
					franchisorUserGroup.setUserGroupId(userGroupId);
					franchisorUserGroup.setEntityProfile(UserProfileEnum.FRANCHISOR);
					franchisorUserGroup.setEntityUserId(requestData.getFranchisorUser().getId());
					userManagement.saveUserGroupEntityUser(franchisorUserGroup);

				}
				
				//False in the request && True in the DB = Need to DELETE from DB
				if (!requestData.getUserGroups().get(userGroupId) && franchisorUserGroupMap.containsKey(userGroupId)){
					
					userManagement.deleteUserGroupEntityUser(userGroupId, requestData.getFranchisorUser().getId());
					
				}
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Franchisor User: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSFranchisorsServiceFranchisorResponse delete(@PathVariable Long id) {
		
		WSFranchisorsServiceFranchisorResponse response = new WSFranchisorsServiceFranchisorResponse();
		
		try {
			response.setFranchisor(franchisorManagement.deleteFranchisor(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Franchisor: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/users/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public FranchisorUserData deleteFranchisorUser(@PathVariable Long id) {
		return franchisorManagement.deleteFranchisorUser(id);
	}

}
