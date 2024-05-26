package br.com.usasistemas.usaplatform.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.WSFranchiseeUserRequest;
import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.api.response.WSFranchiseeDataResponse;
import br.com.usasistemas.usaplatform.api.response.WSFranchiseeListResponse;
import br.com.usasistemas.usaplatform.api.response.WSFranchiseeResponse;
import br.com.usasistemas.usaplatform.api.response.WSFranchiseeUserListResponse;
import br.com.usasistemas.usaplatform.api.response.WSFranchiseeUserResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.StateAndCityManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.FRANCHISEES_RESOURCE)
public class FranchiseesService {
	
	private static final Logger log = Logger.getLogger(FranchiseesService.class.getName());
	private FranchiseeManagementBO franchiseeManagement;
	private FranchisorManagementBO franchisorManagement;
	private StateAndCityManagementBO stateAndCityManagement;
	private UserManagementBO userManagement;
	
	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManagement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}

	public StateAndCityManagementBO getStateAndCityManagement() {
		return stateAndCityManagement;
	}

	public void setStateAndCityManagement(StateAndCityManagementBO stateAndCityManagement) {
		this.stateAndCityManagement = stateAndCityManagement;
	}

	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public List<FranchiseeData> listFranchisees() {
		return franchiseeManagement.getAllFranchisees();		
	}
	
	@RequestMapping(value = "/franchisor/{franchisorId}", method=RequestMethod.GET)
	@ResponseBody
	public WSFranchiseeListResponse getFranchiseesByFranchisorId(@PathVariable Long franchisorId) {
		
		WSFranchiseeListResponse response = new WSFranchiseeListResponse();
		
		try {
			response.setFranchisees(franchiseeManagement.getFranchiseesByFranchisorId(franchisorId));
			response.setStates(franchiseeManagement.getFranchiseesStates(response.getFranchisees()));
			response.setCities(franchiseeManagement.getFranchiseesCities(response.getFranchisees()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Franchisees by Franchisor: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	@ResponseBody
	public WSFranchiseeDataResponse getFranchisee(@PathVariable Long id) {
		
		WSFranchiseeDataResponse response = new WSFranchiseeDataResponse();
		
		FranchiseeData franchisee = franchiseeManagement.getFranchisee(id);
		response.setId(franchisee.getId());
		response.setFranchisorId(franchisee.getFranchisorId());
		response.setName(franchisee.getName());
		response.setCorporateName(franchisee.getCorporateName());
		response.setFiscalId(franchisee.getFiscalId());
		response.setAddress(franchisee.getAddress());
		response.setContactName(franchisee.getContactName());
		response.setContactPhone(franchisee.getContactPhone());
		response.setContactEmail(franchisee.getContactEmail());
		response.setAdditionalInformation(franchisee.getAdditionalInformation());
		
		if (franchisee.getImageURL() != null && !franchisee.getImageURL().isEmpty()) {
			response.setImageURL(franchisee.getImageURL());
		} else {
			response.setImageURL(franchisorManagement.getFranchisor(franchisee.getFranchisorId()).getImageURL());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{franchiseeId}/users", method=RequestMethod.GET)
	@ResponseBody
	public WSFranchiseeUserListResponse getFranchiseeUsers(@PathVariable Long franchiseeId) {
		
		WSFranchiseeUserListResponse response = new WSFranchiseeUserListResponse();
		
		try {
			List<UserGroupData> franchiseeUserGroups = userManagement.getEntityUserGroups(franchiseeId);
			response.setUserGroups(franchiseeUserGroups);
			
			List<FranchiseeUserData> franchiseeUsers = franchiseeManagement.getFranchiseeUsers(franchiseeId);
			response.setFranchiseeUsers(franchiseeUsers);
			
			//create a map to have the user groups for each user
			Map<Long, Map<Long, Boolean>> franchiseeUsersUserGroupsMap = new HashMap<Long, Map<Long, Boolean>>();
			if (franchiseeUsers != null && !franchiseeUsers.isEmpty()) {
				for (FranchiseeUserData fanchiseeUser: franchiseeUsers) {
					
					//for every user, create a map with all user groups and initialize with false for all
					Map<Long, Boolean> userGroupMap = new HashMap<Long, Boolean>();
					if (franchiseeUserGroups != null && !franchiseeUserGroups.isEmpty()){
						for (UserGroupData franchiseeUserGroup : franchiseeUserGroups) {
							userGroupMap.put(franchiseeUserGroup.getId(), false);
						}
					}
					
					//get user groups and set them to true
					List<UserGroupEntityUserData> userGroupsEntityUser = userManagement.getUserGroupEntityUserByEntityUserId(fanchiseeUser.getId());
					if (userGroupsEntityUser != null && !userGroupsEntityUser.isEmpty()){
						for (UserGroupEntityUserData franchiseeUserGroup: userGroupsEntityUser) {
							userGroupMap.put(franchiseeUserGroup.getUserGroupId(), true);
						}
					}					
					
					franchiseeUsersUserGroupsMap.put(fanchiseeUser.getId(), userGroupMap);
					
				}
			}
			response.setFranchiseeUsersGroups(franchiseeUsersUserGroupsMap);			
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Franchisee Users: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSFranchiseeResponse postFranchisee(@RequestBody FranchiseeData franchisee, HttpSession session) {
		
		if (franchisee.getId() == 0) {franchisee.setId(null);}
		
		WSFranchiseeResponse response = new WSFranchiseeResponse();

		try {
			response.setFranchisee(franchiseeManagement.saveFranchisee(franchisee));
			response.setState(stateAndCityManagement.getState(response.getFranchisee().getStateId()));
			response.setCity(stateAndCityManagement.getCity(response.getFranchisee().getCityId()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Franchisee: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{franchiseeId}/users", method=RequestMethod.POST)
	@ResponseBody
	public WSFranchiseeUserResponse postFranchiseeUser(@RequestBody WSFranchiseeUserRequest requestData, @PathVariable Long franchiseeId, HttpSession session) {
		
		WSFranchiseeUserResponse response = new WSFranchiseeUserResponse();
		
		try {
			
			//Add user to Franchisee		
			FranchiseeUserData franchiseeUser = requestData.getFranchiseeUser();
			if (franchiseeUser.getId() == 0) {franchiseeUser.setId(null);}
			franchiseeUser.setFranchiseeId(franchiseeId);
			franchiseeUser.setUserId(requestData.getFranchiseeUser().getUser().getId());
			franchiseeUser = franchiseeManagement.saveFranchiseeUser(franchiseeUser);
			response.setFranchiseeUser(franchiseeUser);
			
			//Add selected groups to user
			for (Long userGroupId: requestData.getUserGroups().keySet()){
				
				//True in the request, ADD to DB
				if (requestData.getUserGroups().get(userGroupId)){
					
					UserGroupEntityUserData franchiseeUserGroup = new UserGroupEntityUserData();
					franchiseeUserGroup.setUserGroupId(userGroupId);
					franchiseeUserGroup.setEntityProfile(UserProfileEnum.FRANCHISEE);
					franchiseeUserGroup.setEntityUserId(franchiseeUser.getId());
					userManagement.saveUserGroupEntityUser(franchiseeUserGroup);
				}
				
			}
			response.setUserGroups(requestData.getUserGroups());
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Franchisee User: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	public WSFranchiseeResponse put(@RequestBody FranchiseeData franchisee, HttpSession session) {
		
		WSFranchiseeResponse response = new WSFranchiseeResponse();

		try {
			response.setFranchisee(franchiseeManagement.saveFranchisee(franchisee));
			response.setState(stateAndCityManagement.getState(response.getFranchisee().getStateId()));
			response.setCity(stateAndCityManagement.getCity(response.getFranchisee().getCityId()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Franchisee: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{franchiseeId}/users", method=RequestMethod.PUT)
	@ResponseBody
	public GenericResponse putFranchiseeUser(@RequestBody WSFranchiseeUserRequest requestData, @PathVariable Long franchiseeId, HttpSession session) {
		
		GenericResponse response = new GenericResponse();
		
		try {
			
			//get franchiseeUserGroups and create a map to simplify the validation
			List<UserGroupEntityUserData> franchiseeUserGroupList = userManagement.getUserGroupEntityUserByEntityUserId(requestData.getFranchiseeUser().getId());
			Map<Long, Boolean> franchiseeUserGroupMap = new HashMap<Long, Boolean>();
			for (UserGroupEntityUserData franchiseeUserGroup: franchiseeUserGroupList) {
				franchiseeUserGroupMap.put(franchiseeUserGroup.getUserGroupId(), true);
			}
			
			//check all groups from request
			for (Long userGroupId: requestData.getUserGroups().keySet()){
				
				//True in the request && False in the DB = Need to ADD to DB
				if (requestData.getUserGroups().get(userGroupId) && !franchiseeUserGroupMap.containsKey(userGroupId)){
					
					UserGroupEntityUserData franchiseeUserGroup = new UserGroupEntityUserData();
					franchiseeUserGroup.setUserGroupId(userGroupId);
					franchiseeUserGroup.setEntityProfile(UserProfileEnum.FRANCHISEE);
					franchiseeUserGroup.setEntityUserId(requestData.getFranchiseeUser().getId());
					userManagement.saveUserGroupEntityUser(franchiseeUserGroup);

				}
				
				//False in the request && True in the DB = Need to DELETE from DB
				if (!requestData.getUserGroups().get(userGroupId) && franchiseeUserGroupMap.containsKey(userGroupId)){
					
					userManagement.deleteUserGroupEntityUser(userGroupId, requestData.getFranchiseeUser().getId());
					
				}
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Franchisee User: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSFranchiseeResponse delete(@PathVariable Long id) {
		
		WSFranchiseeResponse response = new WSFranchiseeResponse();
		
		try {
			response.setFranchisee(franchiseeManagement.deleteFranchisee(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Franchisee: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{id}/deleteImage", method=RequestMethod.DELETE)
	@ResponseBody
	public WSFranchiseeResponse deleteImage(@PathVariable Long id) {
		
		WSFranchiseeResponse response = new WSFranchiseeResponse();
		
		try {
			response.setFranchisee(franchiseeManagement.deleteFranchiseeImage(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Franchisee image: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/users/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public FranchiseeUserData deleteFranchiseeUser(@PathVariable Long id) {
		return franchiseeManagement.deleteFranchiseeUser(id);
	}
	
}
