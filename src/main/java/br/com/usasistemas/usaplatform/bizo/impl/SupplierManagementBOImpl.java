package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ImageManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.StateAndCityManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.dao.SupplierCategoryDAO;
import br.com.usasistemas.usaplatform.dao.SupplierDAO;
import br.com.usasistemas.usaplatform.dao.SupplierFranchisorDAO;
import br.com.usasistemas.usaplatform.dao.SupplierUserDAO;
import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;
import br.com.usasistemas.usaplatform.model.data.StateData;
import br.com.usasistemas.usaplatform.model.data.SupplierCategoryData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.SupplierFranchisorData;
import br.com.usasistemas.usaplatform.model.data.SupplierUserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.SupplierTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.SupplierVisibilityTypeEnum;

public class SupplierManagementBOImpl implements SupplierManagementBO {
	
	private static final Logger log = Logger.getLogger(SupplierManagementBOImpl.class.getName());
	private SupplierDAO supplierDAO;
	private SupplierUserDAO supplierUserDAO;
	private SupplierFranchisorDAO supplierFranchisorDAO;
	private SupplierCategoryDAO supplierCategoryDAO;
	private UserManagementBO userManagement;
	private StateAndCityManagementBO stateAndCityManagement;
	private ImageManagementBO imageManagement;
	private ProductManagementBO productManagement;
	
	public SupplierDAO getSupplierDAO() {
		return supplierDAO;
	}

	public void setSupplierDAO(SupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}

	public SupplierUserDAO getSupplierUserDAO() {
		return supplierUserDAO;
	}

	public void setSupplierUserDAO(SupplierUserDAO supplierUserDAO) {
		this.supplierUserDAO = supplierUserDAO;
	}

	public SupplierFranchisorDAO getSupplierFranchisorDAO() {
		return supplierFranchisorDAO;
	}

	public void setSupplierFranchisorDAO(SupplierFranchisorDAO supplierFranchisorDAO) {
		this.supplierFranchisorDAO = supplierFranchisorDAO;
	}

	public SupplierCategoryDAO getSupplierCategoryDAO() {
		return supplierCategoryDAO;
	}

	public void setSupplierCategoryDAO(SupplierCategoryDAO supplierCategoryDAO) {
		this.supplierCategoryDAO = supplierCategoryDAO;
	}

	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
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

	public ProductManagementBO getProductManagement() {
		return productManagement;
	}

	public void setProductManagement(ProductManagementBO productManagement) {
		this.productManagement = productManagement;
	}

	@Override
	public List<SupplierData> getAllSuppliers() {
		List<SupplierData> supplierList = this.supplierDAO.listAll();
		prepareSuppliersData(supplierList);		
		return supplierList;
	}

	private void prepareSuppliersData(List<SupplierData> supplierList) {
		for(SupplierData supplier: supplierList) {
			if (supplier.getImageKey() != null && supplier.getImageURL() == null) {
				supplier.setImageURL(imageManagement.getImageServingUrl(supplier.getImageKey()));
			}
		}
	}
	
	@Override
	public List<SupplierData> getAllDomainSuppliers(String domainKey) {
		List<SupplierData> supplierList = this.supplierDAO.findByPreferedDomainKey(domainKey);
		prepareSuppliersData(supplierList);		
		return supplierList;
	}

	@Override
	public SupplierData getSupplier(Long id) {
		SupplierData supplier = supplierDAO.findById(id);
		try {
			if (supplier != null && supplier.getImageKey() != null && supplier.getImageURL() == null) {
				supplier.setImageURL(imageManagement.getImageServingUrl(supplier.getImageKey()));
			}
		} catch (Exception e) {
			log.warning("Error retrieving supplier image URL for supplierId = " + id + " - Error: " + e.toString());
		}
		return supplier;
	}
	
	@Override
	public List<SupplierData> getSuppliersByCategory(Long id) {
		List<SupplierData> supplierList = supplierDAO.findByCategoryId(id);
		prepareSuppliersData(supplierList);		
		return supplierList;
	}

	@Override
	public SupplierData saveSupplier(SupplierData supplier) {
		return supplierDAO.save(supplier);
	}

	@Override
	public SupplierData deleteSupplier(Long id) {
		
		//Check if supplier has products and do not allow to delete
		List<ProductData> productList = productManagement.getSupplierProducts(id);
		if (productList != null && !productList.isEmpty()) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage("Não é possivel excluir o fornecedor pois ele possui produtos cadastrados. Primeiro remova os produtos para depois excluir o fornecedor.");
			throw new BusinessException(rm);
		}
		
		//Delete supplier x franchisor relationship
		log.fine("Delete supplier franchisor data");
		List<SupplierFranchisorData> supplierFranchisorList = supplierFranchisorDAO.findBySupplierId(id);
		if (supplierFranchisorList != null & !supplierFranchisorList.isEmpty()) {
			for (SupplierFranchisorData supplierFranchisor: supplierFranchisorList) {
				supplierFranchisorDAO.delete(supplierFranchisor.getId());
			}
		}	
		
		//first need to get and delete Supplier Users
		log.fine("Getting supplier users...");
		List<SupplierUserData> supplierUsers = this.getSupplierUsers(id);
		
		if (supplierUsers != null && !supplierUsers.isEmpty()) {
			for (SupplierUserData supplierUser: supplierUsers) {	
				log.fine("Delete supplier user data");
				this.deleteSupplierUser(supplierUser.getId());
			}
		} else {
			log.fine("No supplier users found");
		}
		
		//second need to get and delete Franchisee Profiles
		log.fine("Getting Supplier puser groups...");
		List<UserGroupData> supplierUserGroups = userManagement.getEntityUserGroups(id);
		
		if (supplierUserGroups != null && !supplierUserGroups.isEmpty()) {
			for (UserGroupData supplierUserGroup: supplierUserGroups) {
				log.fine("Delete supplier user group data");
				userManagement.deleteUserGroup(supplierUserGroup.getId());
			}
		} else {
			log.fine("No franchisor users found");
		}
		
		//delete supplier and its image
		SupplierData supplier = supplierDAO.delete(id);	
		if (supplier != null && supplier.getImageKey() != null) {
			imageManagement.deleteImage(supplier.getImageKey());
		}
	
		return supplier;
	}

	@Override
	public List<SupplierUserData> getSupplierUsers(Long supplierId) {

		log.fine("Getting supplier users...");
		List<SupplierUserData> supplierUsers = supplierUserDAO.findBySupplierId(supplierId);
		
		//for each user, get generic user data
		log.fine("Getting generic user data...");
		if (supplierUsers != null && !supplierUsers.isEmpty()) {
			for (SupplierUserData supplierUser: supplierUsers) {
				supplierUser.setUser(userManagement.getUser(supplierUser.getUserId()));					
			}
		}
		
		return supplierUsers;
	}
	
	@Override
	public SupplierUserData saveSupplierUser(SupplierUserData supplierUser) {
		
		supplierUser = supplierUserDAO.save(supplierUser);
		
		//set user data to return complete supplier user data
		supplierUser.setUser(userManagement.getUser(supplierUser.getUserId()));
		return supplierUser;
	}
	
	@Override
	public SupplierUserData deleteSupplierUser(Long id) {
		
		//delete supplier user groups
		List<UserGroupEntityUserData> supplierUserGroups = userManagement.getUserGroupEntityUserByEntityUserId(id);
		if (supplierUserGroups != null & !supplierUserGroups.isEmpty()) {
			for (UserGroupEntityUserData supplierUserGroup : supplierUserGroups){
				userManagement.deleteUserGroupEntityUser(supplierUserGroup.getId());
			}
		}
		
		SupplierUserData supplierUser = supplierUserDAO.delete(id);
		
		//set user data to return complete supplier user data
		supplierUser.setUser(userManagement.getUser(supplierUser.getUserId()));
		return supplierUser;
	}

	@Override
	public List<EnumValidValueResponseData> getSupplierTypes() {
		List<EnumValidValueResponseData> result = new ArrayList<EnumValidValueResponseData>();
		
		for (SupplierTypeEnum value: SupplierTypeEnum.values()) {
			EnumValidValueResponseData enumValue = new EnumValidValueResponseData();
			enumValue.setKey(value.name());
			enumValue.setValue(value.getDescription());
			result.add(enumValue);
		}

		return result;
	}
	
	@Override
	public List<SupplierFranchisorData> getSupplierFranchisors(Long supplierId){
		return supplierFranchisorDAO.findBySupplierId(supplierId);
	}

	@Override
	public SupplierFranchisorData saveSupplierFranchisor(SupplierFranchisorData supplierFranchisor) {
		return supplierFranchisorDAO.save(supplierFranchisor);
	}

	@Override
	public SupplierFranchisorData deleteSupplierFranchisor(Long supplierId, Long franchisorId) {
		SupplierFranchisorData supplierFranchisor = supplierFranchisorDAO.findBySupplierIdAndFranchisorId(supplierId, franchisorId);
		return supplierFranchisorDAO.delete(supplierFranchisor.getId());
	}

	@Override
	public Map<Long, StateData> getSuppliersStates(List<SupplierData> suppliers) {
		Map<Long, StateData> response = new HashMap<Long, StateData>();
		
		if(suppliers != null && !suppliers.isEmpty()) {
			for(SupplierData supplier: suppliers) {
				if (supplier.getStateId() != null && !response.containsKey(supplier.getStateId())){
					response.put(supplier.getStateId(), stateAndCityManagement.getState(supplier.getStateId()));					
				}
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, CityData> getSuppliersCities(List<SupplierData> suppliers) {
		Map<Long, CityData> response = new HashMap<Long, CityData>();
		
		if(suppliers != null && !suppliers.isEmpty()) {
			for(SupplierData supplier: suppliers) {
				if (supplier.getCityId() != null && !response.containsKey(supplier.getCityId())){
					response.put(supplier.getCityId(), stateAndCityManagement.getCity(supplier.getCityId()));					
				}
			}
		}
		
		return response;
	}

	@Override
	public List<SupplierCategoryData> getAllCategories() {
		
		List<SupplierCategoryData> result = supplierCategoryDAO.listAll();
		
		Collections.sort(result, new Comparator<SupplierCategoryData>() {
	        @Override
	        public int compare(SupplierCategoryData category1, SupplierCategoryData category2)
	        {
	        	if (category1.getOrder() == null) return 1;
	        	if (category2.getOrder() == null) return -1;
	            return  category1.getOrder().compareTo(category2.getOrder());
	        }
	    });
		
		return result;
	}

	@Override
	public SupplierCategoryData saveSupplierCategory(SupplierCategoryData supplierCategory) {
		return supplierCategoryDAO.save(supplierCategory);
	}

	@Override
	public SupplierCategoryData deleteSupplierCategory(Long id) {
		return supplierCategoryDAO.delete(id);
	}

	@Override
	public SupplierData addImage(Long supplierId, String imageKey) {
		SupplierData supplier = supplierDAO.findById(supplierId);
		
		//BlobKey oldImage = product.getImageKey();
		
		supplier.setImageKey(imageKey);
		supplier.setImageURL(imageManagement.getImageServingUrl(imageKey));
		supplier = supplierDAO.save(supplier);
		
		/*if (oldImage != null) {
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			blobstoreService.delete(oldImage);
		}*/
		
		return supplier;
	}

	@Override
	public List<EnumValidValueResponseData> getVisibilityTypes() {
		List<EnumValidValueResponseData> result = new ArrayList<EnumValidValueResponseData>();
		
		for (SupplierVisibilityTypeEnum value: SupplierVisibilityTypeEnum.values()) {
			EnumValidValueResponseData enumValue = new EnumValidValueResponseData();
			enumValue.setKey(value.name());
			enumValue.setValue(value.getDescription());
			result.add(enumValue);
		}

		return result;	
	}

	@Override
	public List<SupplierData> getFranchisorSuppliersByCategory(Long franchisorId, Long categoryId) {
		
		Map<Long, SupplierData> suppliersMap = new HashMap<Long, SupplierData>();
		
		//first get list of suppliers related to franchisor
		List<SupplierFranchisorData> franchisorSuppliers = supplierFranchisorDAO.findByFranchisorId(franchisorId);
		if (franchisorSuppliers != null && !franchisorSuppliers.isEmpty()) {
			for (SupplierFranchisorData franchisorSupplier: franchisorSuppliers) {
				
				//fetch supplier data
				SupplierData supplier = supplierDAO.findById(franchisorSupplier.getSupplierId());
				
				//add only if same category
				if (supplier.getCategoryId().equals(categoryId)) {
					if (supplier.getImageKey() != null && supplier.getImageURL() == null) {
						supplier.setImageURL(imageManagement.getImageServingUrl(supplier.getImageKey()));
					}
					suppliersMap.put(supplier.getId(), supplier);
				}
			}
		}
		
		//add all public suppliers for the given category
		List<SupplierData> suppliers = supplierDAO.findByCategoryIdAndVisibilityType(categoryId, SupplierVisibilityTypeEnum.PUBLIC);
		if (suppliers != null && !suppliers.isEmpty()) {
			for (SupplierData supplier: suppliers) {
				
				//add only if not added already
				if (!suppliersMap.containsKey(supplier.getId())) {
					if (supplier.getImageKey() != null && supplier.getImageURL() == null) {
						supplier.setImageURL(imageManagement.getImageServingUrl(supplier.getImageKey()));
					}
					suppliersMap.put(supplier.getId(), supplier);
				}
			}
		}
		
		return new ArrayList<SupplierData>(suppliersMap.values());
	}

	@Override
	public List<SupplierData> getFranchisorSuppliersWithStock(Long franchisorId) {
		
		List<SupplierData> suplliers = new ArrayList<SupplierData>();
		
		//create a map with the categories
		List<SupplierCategoryData> supplierCategories = supplierCategoryDAO.listAll();
		Map<Long, SupplierCategoryData> supplierCategoriesMap = new HashMap<Long, SupplierCategoryData>();
		if (supplierCategories != null && !supplierCategories.isEmpty()) {
			for (SupplierCategoryData supplierCategory: supplierCategories) {
				supplierCategoriesMap.put(supplierCategory.getId(), supplierCategory);
			}
		}
		
		//get list of suppliers related to franchisor
		List<SupplierFranchisorData> franchisorSuppliers = supplierFranchisorDAO.findByFranchisorId(franchisorId);
		if (franchisorSuppliers != null && !franchisorSuppliers.isEmpty()) {
			for (SupplierFranchisorData franchisorSupplier: franchisorSuppliers) {
				
				//get supplier info and add only if supplier category has stock control
				SupplierData supplier = supplierDAO.findById(franchisorSupplier.getSupplierId());
				if (supplierCategoriesMap.get(supplier.getCategoryId()).getHasStockControl()) {
					suplliers.add(supplier);
				}
			}			
		}
		
		return suplliers;
	}

	@Override
	public SupplierCategoryData getCategory(Long id) {
		return supplierCategoryDAO.findById(id);
	}

	@Override
	public List<SupplierData> getFranchisorSuppliers(Long franchisorId) {
		List<SupplierData> suppliers = new ArrayList<SupplierData>();
		
		//first get list of suppliers related to franchisor
		List<SupplierFranchisorData> franchisorSuppliers = supplierFranchisorDAO.findByFranchisorId(franchisorId);
		if (franchisorSuppliers != null && !franchisorSuppliers.isEmpty()) {
			for (SupplierFranchisorData franchisorSupplier: franchisorSuppliers) {
				//fetch supplier data
				suppliers.add(supplierDAO.findById(franchisorSupplier.getSupplierId()));
			}
		}
		
		return suppliers;
	}
	
	@Override
	public List<SupplierCategoryData> getSupplierCategoriesByDomainKey(String domainKey) {
		return supplierCategoryDAO.findByDomainKey(domainKey);
	}
	
	@Override
	public Map<Long, SupplierData> getReviewRequestsSuppliers(List<ReviewRequestData> reviewRequests) {
		
		Map<Long, SupplierData> response = new HashMap<Long, SupplierData>();
		
		if(reviewRequests != null && !reviewRequests.isEmpty()) {
			for(ReviewRequestData reviewRequest: reviewRequests) {
				if (reviewRequest.getToEntityId() != null && !response.containsKey(reviewRequest.getToEntityId())){
					response.put(reviewRequest.getToEntityId(), supplierDAO.findById(reviewRequest.getToEntityId()));					
				}
			}
		}
		
		return response;
	}

}
