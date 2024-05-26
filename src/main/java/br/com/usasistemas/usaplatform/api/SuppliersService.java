package br.com.usasistemas.usaplatform.api;

import java.util.ArrayList;
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

import br.com.usasistemas.usaplatform.api.request.WSSupplierUserRequest;
import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.api.response.WSEnumValidValuesResponse;
import br.com.usasistemas.usaplatform.api.response.WSFranchisorsServiceSupplierFranchisorsMapResponse;
import br.com.usasistemas.usaplatform.api.response.WSFranchisorsServiceSupplierFranchisorsResponse;
import br.com.usasistemas.usaplatform.api.response.WSSupplierCategoryListResponse;
import br.com.usasistemas.usaplatform.api.response.WSSupplierCategoryResponse;
import br.com.usasistemas.usaplatform.api.response.WSSupplierListResponse;
import br.com.usasistemas.usaplatform.api.response.WSSupplierResponse;
import br.com.usasistemas.usaplatform.api.response.WSSupplierUserListResponse;
import br.com.usasistemas.usaplatform.api.response.WSSupplierUserResponse;
import br.com.usasistemas.usaplatform.api.response.WSSuppliersServiceSupplierResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.StateAndCityManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.SupplierCategoryData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.SupplierFranchisorData;
import br.com.usasistemas.usaplatform.model.data.SupplierUserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.SUPPLIERS_RESOURCE)
public class SuppliersService {
	
	private static final Logger log = Logger.getLogger(SuppliersService.class.getName());
	private SupplierManagementBO supplierManagement;
	private UserManagementBO userManagement;
	private FranchisorManagementBO franchisorManagement;
	private StateAndCityManagementBO stateAndCityManagement;
	private ConfigurationManagementBO configurationManagement;
	
	public SupplierManagementBO getSupplierManagement() {
		return supplierManagement;
	}

	public void setSupplierManagement(SupplierManagementBO supplierManagement) {
		this.supplierManagement = supplierManagement;
	}

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

	public StateAndCityManagementBO getStateAndCityManagement() {
		return stateAndCityManagement;
	}

	public void setStateAndCityManagement(StateAndCityManagementBO stateAndCityManagement) {
		this.stateAndCityManagement = stateAndCityManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public WSSupplierListResponse listSuppliers(HttpServletRequest request) {
		WSSupplierListResponse response = new WSSupplierListResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			response.setSuppliers(supplierManagement.getAllDomainSuppliers(domainConfiguration.getKey()));
			response.setStates(supplierManagement.getSuppliersStates(response.getSuppliers()));
			response.setCities(supplierManagement.getSuppliersCities(response.getSuppliers()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Suppliers: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/franchisor/{franchisorId}/category/{categoryId}", method=RequestMethod.GET)
	@ResponseBody
	public WSSupplierListResponse getSuppliersByCategory(@PathVariable Long franchisorId, @PathVariable Long categoryId) {
		WSSupplierListResponse response = new WSSupplierListResponse();
		
		try {
			response.setSuppliers(supplierManagement.getFranchisorSuppliersByCategory(franchisorId, categoryId));
			response.setStates(supplierManagement.getSuppliersStates(response.getSuppliers()));
			response.setCities(supplierManagement.getSuppliersCities(response.getSuppliers()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Suppliers by Category: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/franchisor/{franchisorId}/hasStock", method=RequestMethod.GET)
	@ResponseBody
	public WSSupplierListResponse getFranchisorSuppliersWithStock(@PathVariable Long franchisorId) {
		WSSupplierListResponse response = new WSSupplierListResponse();
		
		try {
			response.setSuppliers(supplierManagement.getFranchisorSuppliersWithStock(franchisorId));
			response.setStates(supplierManagement.getSuppliersStates(response.getSuppliers()));
			response.setCities(supplierManagement.getSuppliersCities(response.getSuppliers()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Suppliers by Category: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	@ResponseBody
	public SupplierData getSupplier(@PathVariable Long id) {
		return supplierManagement.getSupplier(id);
	}
	
	@RequestMapping(value = "/{supplierId}/users", method=RequestMethod.GET)
	@ResponseBody
	public WSSupplierUserListResponse getSupplierUsers(@PathVariable Long supplierId) {

		WSSupplierUserListResponse response = new WSSupplierUserListResponse();
		
		try {
			List<UserGroupData> supplierUserGroups = userManagement.getEntityUserGroups(supplierId);
			response.setUserGroups(supplierUserGroups);
			
			List<SupplierUserData> supplierUsers = supplierManagement.getSupplierUsers(supplierId);
			response.setSupplierUsers(supplierUsers);
			
			//create a map to have the user groups for each user
			Map<Long, Map<Long, Boolean>> supplierUsersUserGroupsMap = new HashMap<Long, Map<Long, Boolean>>();
			if (supplierUsers != null && !supplierUsers.isEmpty()) {
				for (SupplierUserData supplierUser: supplierUsers) {
					
					//for every user, create a map with all user groups and initialize with false for all
					Map<Long, Boolean> userGroupMap = new HashMap<Long, Boolean>();
					if (supplierUserGroups != null && !supplierUserGroups.isEmpty()){
						for (UserGroupData supplierUserGroup : supplierUserGroups) {
							userGroupMap.put(supplierUserGroup.getId(), false);
						}
					}
					
					//get user groups and set them to true
					List<UserGroupEntityUserData> userGroupsEntityUser = userManagement.getUserGroupEntityUserByEntityUserId(supplierUser.getId());
					if (userGroupsEntityUser != null && !userGroupsEntityUser.isEmpty()){
						for (UserGroupEntityUserData supplierUserGroup: userGroupsEntityUser) {
							userGroupMap.put(supplierUserGroup.getUserGroupId(), true);
						}
					}					
					
					supplierUsersUserGroupsMap.put(supplierUser.getId(), userGroupMap);
					
				}
			}
			response.setSupplierUsersGroups(supplierUsersUserGroupsMap);			
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Supplier Users: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Supplier Categories
	 */
	@RequestMapping(value = "/categories", method=RequestMethod.GET)
	@ResponseBody
	public WSSupplierCategoryListResponse getSupplierCategoryList(HttpServletRequest request) {
		WSSupplierCategoryListResponse response = new WSSupplierCategoryListResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			response.setSupplierCategories(supplierManagement.getSupplierCategoriesByDomainKey(domainConfiguration.getKey()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Supplier Category List: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSSupplierResponse postSupplier(@RequestBody SupplierData supplier, HttpServletRequest request) {
		
		if (supplier.getId() == 0) {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			supplier.setId(null);
			supplier.setPreferedDomainKey(domainConfiguration.getKey());
		}
		
		WSSupplierResponse response = new WSSupplierResponse();

		try {
			response.setSupplier(supplierManagement.saveSupplier(supplier));
			response.setState(stateAndCityManagement.getState(response.getSupplier().getStateId()));
			response.setCity(stateAndCityManagement.getCity(response.getSupplier().getCityId()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Supplier: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/categories", method=RequestMethod.POST)
	@ResponseBody
	public WSSupplierCategoryResponse postSupplierCategory(@RequestBody SupplierCategoryData supplierCategory, HttpServletRequest request) {
		
		if (supplierCategory.getId() == 0) {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			
			supplierCategory.setId(null);
			List<String> domainKey = new ArrayList<String>();
			domainKey.add(domainConfiguration.getKey());
			supplierCategory.setDomainKeys(domainKey);
		}
		
		WSSupplierCategoryResponse response = new WSSupplierCategoryResponse();

		try {
			response.setSupplierCategory(supplierManagement.saveSupplierCategory(supplierCategory));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Supplier Category: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{supplierId}/users", method=RequestMethod.POST)
	@ResponseBody
	public WSSupplierUserResponse postSupplierUser(@RequestBody WSSupplierUserRequest requestData, @PathVariable Long supplierId, HttpSession session) {
		
		WSSupplierUserResponse response = new WSSupplierUserResponse();
		
		try {
			
			//Add user to Supplier		
			SupplierUserData supplierUser = requestData.getSupplierUser();
			if (supplierUser.getId() == 0) {supplierUser.setId(null);}
			supplierUser.setSupplierId(supplierId);
			supplierUser.setUserId(requestData.getSupplierUser().getUser().getId());
			supplierUser = supplierManagement.saveSupplierUser(supplierUser);
			response.setSupplierUser(supplierUser);
			
			//Add selected groups to user
			for (Long userGroupId: requestData.getUserGroups().keySet()){
				
				//True in the request, ADD to DB
				if (requestData.getUserGroups().get(userGroupId)){
					
					UserGroupEntityUserData supplierUserGroup = new UserGroupEntityUserData();
					supplierUserGroup.setUserGroupId(userGroupId);
					supplierUserGroup.setEntityProfile(UserProfileEnum.SUPPLIER);
					supplierUserGroup.setEntityUserId(supplierUser.getId());
					userManagement.saveUserGroupEntityUser(supplierUserGroup);
				}
				
			}
			response.setUserGroups(requestData.getUserGroups());
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Supplier User: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	public WSSupplierResponse put(@RequestBody SupplierData supplier, HttpSession session) {
		
		WSSupplierResponse response = new WSSupplierResponse();

		try {
			response.setSupplier(supplierManagement.saveSupplier(supplier));
			response.setState(stateAndCityManagement.getState(response.getSupplier().getStateId()));
			response.setCity(stateAndCityManagement.getCity(response.getSupplier().getCityId()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Supplier: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{supplierId}/users", method=RequestMethod.PUT)
	@ResponseBody
	public GenericResponse putSupplierUser(@RequestBody WSSupplierUserRequest requestData, @PathVariable Long supplierId, HttpSession session) {
		
		GenericResponse response = new GenericResponse();
		
		try {
			
			//get supplierUserGroups and create a map to simplify the validation
			List<UserGroupEntityUserData> supplierUserGroupList = userManagement.getUserGroupEntityUserByEntityUserId(requestData.getSupplierUser().getId());
			Map<Long, Boolean> supplierUserGroupMap = new HashMap<Long, Boolean>();
			for (UserGroupEntityUserData supplierUserGroup: supplierUserGroupList) {
				supplierUserGroupMap.put(supplierUserGroup.getUserGroupId(), true);
			}
			
			//check all groups from request
			for (Long userGroupId: requestData.getUserGroups().keySet()){
				
				//True in the request && False in the DB = Need to ADD to DB
				if (requestData.getUserGroups().get(userGroupId) && !supplierUserGroupMap.containsKey(userGroupId)){
					
					UserGroupEntityUserData supplierUserGroup = new UserGroupEntityUserData();
					supplierUserGroup.setUserGroupId(userGroupId);
					supplierUserGroup.setEntityProfile(UserProfileEnum.SUPPLIER);
					supplierUserGroup.setEntityUserId(requestData.getSupplierUser().getId());
					userManagement.saveUserGroupEntityUser(supplierUserGroup);

				}
				
				//False in the request && True in the DB = Need to DELETE from DB
				if (!requestData.getUserGroups().get(userGroupId) && supplierUserGroupMap.containsKey(userGroupId)){
					
					userManagement.deleteUserGroupEntityUser(userGroupId, requestData.getSupplierUser().getId());
					
				}
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Supplier User: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/categories", method=RequestMethod.PUT)
	@ResponseBody
	public WSSupplierCategoryResponse putSupplierCategory(@RequestBody SupplierCategoryData supplierCategory, HttpSession session) {
		
		WSSupplierCategoryResponse response = new WSSupplierCategoryResponse();

		try {
			response.setSupplierCategory(supplierManagement.saveSupplierCategory(supplierCategory));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Supplier Category: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSSuppliersServiceSupplierResponse delete(@PathVariable Long id) {
		
		WSSuppliersServiceSupplierResponse response = new WSSuppliersServiceSupplierResponse();
		
		try {
			response.setSupplier(supplierManagement.deleteSupplier(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Supplier: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/categories/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSSupplierCategoryResponse deleteSupplierCategory(@PathVariable Long id) {
		
		WSSupplierCategoryResponse response = new WSSupplierCategoryResponse();

		try {
			response.setSupplierCategory(supplierManagement.deleteSupplierCategory(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Supplier Category: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/users/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public SupplierUserData deleteSupplierUser(@PathVariable Long id) {
		return supplierManagement.deleteSupplierUser(id);
	}
	
	/*
	 * Get Supplier Types
	 */
	@RequestMapping(value = "/supplierTypes", method=RequestMethod.GET)
	@ResponseBody
	public WSEnumValidValuesResponse getSupplierTypes() {
		
		WSEnumValidValuesResponse response = new WSEnumValidValuesResponse();
		
		try {
			response.setEnumValues(supplierManagement.getSupplierTypes());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Supplier Types: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Supplier Visibility Types
	 */
	@RequestMapping(value = "/visibilityTypes", method=RequestMethod.GET)
	@ResponseBody
	public WSEnumValidValuesResponse getVisibilityTypes() {
		
		WSEnumValidValuesResponse response = new WSEnumValidValuesResponse();
		
		try {
			response.setEnumValues(supplierManagement.getVisibilityTypes());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Supplier VisibilityTypes: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{supplierId}/franchisors", method=RequestMethod.GET)
	@ResponseBody
	public WSFranchisorsServiceSupplierFranchisorsResponse getSupplierFranchisors(@PathVariable Long supplierId){
		WSFranchisorsServiceSupplierFranchisorsResponse response = new WSFranchisorsServiceSupplierFranchisorsResponse();
		
		try {
			List<FranchisorData> franchisorList = new ArrayList<FranchisorData>();
			response.setFranchisors(franchisorList);
			
			//get supplier franchisors
			List<SupplierFranchisorData> supplierFranchisorList = supplierManagement.getSupplierFranchisors(supplierId);
			if (supplierFranchisorList != null && !supplierFranchisorList.isEmpty())
				for (SupplierFranchisorData supplierFranchisor: supplierFranchisorList){
					franchisorList.add(franchisorManagement.getFranchisor(supplierFranchisor.getFranchisorId()));
				}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Supplier Franchisors: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{supplierId}/franchisorsMap", method=RequestMethod.GET)
	@ResponseBody
	public WSFranchisorsServiceSupplierFranchisorsMapResponse getSupplierFranchisorsMap(@PathVariable Long supplierId, HttpServletRequest request){
		WSFranchisorsServiceSupplierFranchisorsMapResponse response = new WSFranchisorsServiceSupplierFranchisorsMapResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());

			List<FranchisorData> franchisorList = franchisorManagement.getAllDomainFranchisors(domainConfiguration.getKey());
			response.setFranchisors(franchisorList);
			
			//create a map with all franchisors and initialize with false for all
			Map<Long, Boolean> supplierFranchisorMap = new HashMap<Long, Boolean>();
			if (franchisorList != null && !franchisorList.isEmpty())
				for (FranchisorData franchisor: franchisorList){
					supplierFranchisorMap.put(franchisor.getId(), false);
				}
			
			//get supplier franchisors and set them to true
			List<SupplierFranchisorData> supplierFranchisorList = supplierManagement.getSupplierFranchisors(supplierId);
			if (supplierFranchisorList != null && !supplierFranchisorList.isEmpty())
				for (SupplierFranchisorData supplierFranchisor: supplierFranchisorList){
					
					supplierFranchisorMap.put(supplierFranchisor.getFranchisorId(), true);
				}
			response.setSupplierFranchisors(supplierFranchisorMap);
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Supplier Franchisors: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/{supplierId}/franchisors", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse saveSupplierFranchisors(@RequestBody Map<Long, Boolean> supplierFranchisorsRequest, @PathVariable Long supplierId){
		GenericResponse response = new GenericResponse();
		
		try {
			
			//get supplier franchisors and create a map to simplify the validation
			List<SupplierFranchisorData> supplierFranchisorList = supplierManagement.getSupplierFranchisors(supplierId);
			Map<Long, Boolean> supplierFranchisorMap = new HashMap<Long, Boolean>();
			for (SupplierFranchisorData franchisor: supplierFranchisorList) {
				supplierFranchisorMap.put(franchisor.getFranchisorId(), true);
			}
			
			//check all franchisors from request
			for (Long franchisorId: supplierFranchisorsRequest.keySet()){
				
				//True in the request && False in the DB = Need to ADD to DB
				if (supplierFranchisorsRequest.get(franchisorId) && !supplierFranchisorMap.containsKey(franchisorId)){
					
					SupplierFranchisorData supplierFranchisor = new SupplierFranchisorData();
					supplierFranchisor.setFranchisorId(franchisorId);
					supplierFranchisor.setSupplierId(supplierId);
					
					supplierManagement.saveSupplierFranchisor(supplierFranchisor);

				}
				
				//False in the request && True in the DB = Need to DELETE from DB
				if (!supplierFranchisorsRequest.get(franchisorId) && supplierFranchisorMap.containsKey(franchisorId)){
					
					supplierManagement.deleteSupplierFranchisor(supplierId, franchisorId);
					
				}
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Supplier Franchisors: " + e.toString());
		}
		
		return response;
	}
}
