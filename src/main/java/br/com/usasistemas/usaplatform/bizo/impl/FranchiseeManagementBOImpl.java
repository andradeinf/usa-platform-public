package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.ImageManagementBO;
import br.com.usasistemas.usaplatform.bizo.StateAndCityManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.dao.FranchiseeDAO;
import br.com.usasistemas.usaplatform.dao.FranchiseeUserDAO;
import br.com.usasistemas.usaplatform.dao.response.DeliveryRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;
import br.com.usasistemas.usaplatform.model.data.StateData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

public class FranchiseeManagementBOImpl implements FranchiseeManagementBO {
	
	private static final Logger log = Logger.getLogger(FranchiseeManagementBOImpl.class.getName());
	private FranchiseeDAO franchiseeDAO;
	private FranchiseeUserDAO franchiseeUserDAO;
	private UserManagementBO userManagement;
	private DeliveryManagementBO deliveryManagement;
	private StateAndCityManagementBO stateAndCityManagement;
	private ImageManagementBO imageManagement;
	
	public FranchiseeDAO getFranchiseeDAO() {
		return franchiseeDAO;
	}

	public void setFranchiseeDAO(FranchiseeDAO franchiseeDAO) {
		this.franchiseeDAO = franchiseeDAO;
	}

	public FranchiseeUserDAO getFranchiseeUserDAO() {
		return franchiseeUserDAO;
	}

	public void setFranchiseeUserDAO(FranchiseeUserDAO franchiseeUserDAO) {
		this.franchiseeUserDAO = franchiseeUserDAO;
	}

	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	public DeliveryManagementBO getDeliveryManagement() {
		return deliveryManagement;
	}

	public void setDeliveryManagement(DeliveryManagementBO deliveryManagement) {
		this.deliveryManagement = deliveryManagement;
	}

	public StateAndCityManagementBO getStateAndCityManagement() {
		return stateAndCityManagement;
	}

	public void setStateAndCityManagement(
			StateAndCityManagementBO stateAndCityManagement) {
		this.stateAndCityManagement = stateAndCityManagement;
	}
	
	public ImageManagementBO getImageManagement() {
		return imageManagement;
	}

	public void setImageManagement(ImageManagementBO imageManagement) {
		this.imageManagement = imageManagement;
	}

	@Override
	public List<FranchiseeData> getAllFranchisees() {
		return franchiseeDAO.listAll();
	}

	@Override
	public FranchiseeData getFranchisee(Long id) {
		return franchiseeDAO.findById(id);
	}

	@Override
	public FranchiseeData saveFranchisee(FranchiseeData franchisee) {
		return franchiseeDAO.save(franchisee);
	}

	@Override
	public FranchiseeData deleteFranchisee(Long id) {
		
		//check delivery requests and do not allow delete a franchisee that has at least one request
		DeliveryRequestPagedResponse deliveryRequestList = deliveryManagement.getDeliveryRequestsByFranchiseeId(id, 1L, null);
		if (deliveryRequestList.getDeliveryRequests() != null && !deliveryRequestList.getDeliveryRequests().isEmpty()) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage("Não é possivel excluir o franqueado pois ele possui solicitações de entrega.");
			throw new BusinessException(rm);
		}
		
		//first need to get and delete Franchisee Users
		log.fine("Getting franchisee users...");
		List<FranchiseeUserData> franchiseeUsers = this.getFranchiseeUsers(id);
		
		if (franchiseeUsers != null && !franchiseeUsers.isEmpty()) {
			for (FranchiseeUserData franchiseeUser: franchiseeUsers) {
				log.fine("Delete franchisee user data");
				this.deleteFranchiseeUser(franchiseeUser.getId());
			}
		} else {
			log.fine("No franchisee users found");
		}
		
		//second need to get and delete Franchisee Profiles
		log.fine("Getting franchisee user groups...");
		List<UserGroupData> franchiseeUserGroups = userManagement.getEntityUserGroups(id);
		
		if (franchiseeUserGroups != null && !franchiseeUserGroups.isEmpty()) {
			for (UserGroupData franchiseeUserGroup: franchiseeUserGroups) {
				log.fine("Delete franchisee user group data");
				userManagement.deleteUserGroup(franchiseeUserGroup.getId());
			}
		} else {
			log.fine("No franchisor users found");
		}
		
		return franchiseeDAO.delete(id);
	}

	@Override
	public List<FranchiseeUserData> getFranchiseeUsers(Long franchiseeId) {

		log.fine("Getting franchisee users...");
		List<FranchiseeUserData> franchiseeUsers = franchiseeUserDAO.findByFranchiseeId(franchiseeId);
		
		//for each user, get generic user data
		log.fine("Getting generic user data...");
		if (franchiseeUsers != null && !franchiseeUsers.isEmpty()) {
			for (FranchiseeUserData franchiseeUser: franchiseeUsers) {
				franchiseeUser.setUser(userManagement.getUser(franchiseeUser.getUserId()));					
			}
		}
		
		return franchiseeUsers;
	}
	
	@Override
	public FranchiseeUserData saveFranchiseeUser(FranchiseeUserData franchiseeUser) {
		
		franchiseeUser = franchiseeUserDAO.save(franchiseeUser);
		
		//set user data to return complete franchisee user data
		franchiseeUser.setUser(userManagement.getUser(franchiseeUser.getUserId()));
		return franchiseeUser;
	}
	
	@Override
	public FranchiseeUserData deleteFranchiseeUser(Long id) {
		
		//delete franchisee user groups
		List<UserGroupEntityUserData> franchiseeUserGroups = userManagement.getUserGroupEntityUserByEntityUserId(id);
		if (franchiseeUserGroups != null & !franchiseeUserGroups.isEmpty()) {
		  for (UserGroupEntityUserData franchiseeUserGroup : franchiseeUserGroups){
			  userManagement.deleteUserGroupEntityUser(franchiseeUserGroup.getId());
		  }
		}
		
		FranchiseeUserData franchiseeUser = franchiseeUserDAO.delete(id);

		//set user data to return complete franchisee user data
		franchiseeUser.setUser(userManagement.getUser(franchiseeUser.getUserId()));
		return franchiseeUser;
	}

	@Override
	public List<FranchiseeData> deleteFranchiseeByFranchisorId(Long franchisorId) {
		
		List<FranchiseeData> franchiseeList = franchiseeDAO.findByFranchisorId(franchisorId);
		if(franchiseeList != null && !franchiseeList.isEmpty()) {
			for(FranchiseeData franchisee: franchiseeList) {
				franchisee = this.deleteFranchisee(franchisee.getId());
			}
		}
		
		return franchiseeList;
		
	}

	@Override
	public List<FranchiseeData> getFranchiseesByFranchisorId(Long francchisorId) {
		return franchiseeDAO.findByFranchisorId(francchisorId);
	}

	@Override
	public Map<Long, StateData> getFranchiseesStates(List<FranchiseeData> franchisees) {
		Map<Long, StateData> response = new HashMap<Long, StateData>();
		
		if(franchisees != null && !franchisees.isEmpty()) {
			for(FranchiseeData franchisee: franchisees) {
				if (franchisee.getStateId() != null && !response.containsKey(franchisee.getStateId())){
					response.put(franchisee.getStateId(), stateAndCityManagement.getState(franchisee.getStateId()));					
				}
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, CityData> getFranchiseesCities(List<FranchiseeData> franchisees) {
		Map<Long, CityData> response = new HashMap<Long, CityData>();
		
		if(franchisees != null && !franchisees.isEmpty()) {
			for(FranchiseeData franchisee: franchisees) {
				if (franchisee.getCityId() != null && !response.containsKey(franchisee.getCityId())){
					response.put(franchisee.getCityId(), stateAndCityManagement.getCity(franchisee.getCityId()));					
				}
			}
		}
		
		return response;
	}

	@Override
	public FranchiseeData addImage(Long franchiseeId, String imageKey) {
		FranchiseeData franchisee = franchiseeDAO.findById(franchiseeId);
		
		//BlobKey oldImage = product.getImageKey();
		
		franchisee.setImageKey(imageKey);
		franchisee.setImageURL(imageManagement.getImageServingUrl(imageKey));
		franchisee = franchiseeDAO.save(franchisee);
		
		/*if (oldImage != null) {
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			blobstoreService.delete(oldImage);
		}*/
		
		return franchisee;
	}

	@Override
	public FranchiseeData deleteFranchiseeImage(Long franchiseeId) {
		FranchiseeData franchisee = franchiseeDAO.findById(franchiseeId);
		
		//BlobKey oldImage = product.getImageKey();
		
		franchisee.setImageKey(null);
		franchisee.setImageURL(null);
		franchisee = franchiseeDAO.save(franchisee);
		
		/*if (oldImage != null) {
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			blobstoreService.delete(oldImage);
		}*/
		
		return franchisee;
	}
	
}
