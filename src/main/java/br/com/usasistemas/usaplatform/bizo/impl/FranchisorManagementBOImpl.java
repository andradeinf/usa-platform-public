package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.ImageManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.dao.FranchisorDAO;
import br.com.usasistemas.usaplatform.dao.FranchisorUserDAO;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.FranchisorUserData;
import br.com.usasistemas.usaplatform.model.data.ProductCategoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

public class FranchisorManagementBOImpl implements FranchisorManagementBO {

	private static final Logger log = Logger.getLogger(FranchisorManagementBOImpl.class.getName());
	private FranchisorDAO franchisorDAO;
	private FranchisorUserDAO franchisorUserDAO;
	private UserManagementBO userManagement;
	private ImageManagementBO imageManagement;
	private FranchiseeManagementBO franchiseeManagement;
	private ProductManagementBO productManagement;

	public FranchisorDAO getFranchisorDAO() {
		return franchisorDAO;
	}

	public void setFranchisorDAO(FranchisorDAO franchisorDAO) {
		this.franchisorDAO = franchisorDAO;
	}

	public FranchisorUserDAO getFranchisorUserDAO() {
		return franchisorUserDAO;
	}

	public void setFranchisorUserDAO(FranchisorUserDAO franchisorUserDAO) {
		this.franchisorUserDAO = franchisorUserDAO;
	}

	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	public ImageManagementBO getImageManagement() {
		return imageManagement;
	}

	public void setImageManagement(ImageManagementBO imageManagement) {
		this.imageManagement = imageManagement;
	}

	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public ProductManagementBO getProductManagement() {
		return productManagement;
	}

	public void setProductManagement(ProductManagementBO productManagement) {
		this.productManagement = productManagement;
	}

	@Override
	public List<FranchisorData> getAllFranchisors() {

		List<FranchisorData> franchisorList = franchisorDAO.listAll();
		prepareFranchisorsData(franchisorList);

		return franchisorList;
	}
	
	@Override
	public List<FranchisorData> getAllDomainFranchisors(String domainKey) {

		List<FranchisorData> franchisorList = franchisorDAO.findByPreferedDomainKey(domainKey);
		prepareFranchisorsData(franchisorList);

		return franchisorList;
	}

	private void prepareFranchisorsData(List<FranchisorData> franchisorList) {
		for(FranchisorData franchisor: franchisorList) {
			if (franchisor.getImageKey() != null && franchisor.getImageURL() == null) {
				franchisor.setImageURL(imageManagement.getImageServingUrl(franchisor.getImageKey()));
			}
		}
	}

	@Override
	public FranchisorData getFranchisor(Long id) {

		FranchisorData franchisor = franchisorDAO.findById(id);
		if (franchisor != null && franchisor.getImageKey() != null && franchisor.getImageURL() == null) {
			franchisor.setImageURL(imageManagement.getImageServingUrl(franchisor.getImageKey()));
		}

		return franchisor;
	}

	@Override
	public FranchisorData saveFranchisor(FranchisorData franchisor) {
		FranchisorData response = this.franchisorDAO.save(franchisor);

		//Asynchronously duplicate franchisor data
		if (franchisor.getDuplicateId() != null) {
			Queue queue = QueueFactory.getDefaultQueue();
			queue.add(TaskOptions.Builder.withUrl("/job/async/duplicateFranchisor/"+franchisor.getDuplicateId()+"/"+response.getId()));
		}

		return response;
	}

	@Override
	public FranchisorData deleteFranchisor(Long id) {

		//check products and do not allow delete a franchisor that has at least one product
		List<ProductData> productList = productManagement.getFranchisorProducts(id);
		if (productList != null && !productList.isEmpty()) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage("Não é possivel excluir o franqueador pois ele possui produtos cadastrados. Primeiro remova os produtos para depois excluir o franqueado.");
			throw new BusinessException(rm);
		} else {
			//delete franchisor product categories
			log.fine("Delete franchisor product categories data");
			List<ProductCategoryData> productCategoryList = productManagement.getFranchisorProductCategories(id, null);
			if (productCategoryList != null & !productCategoryList.isEmpty()) {
				for (ProductCategoryData productCategory: productCategoryList) {
					productManagement.deleteProductCategory(productCategory.getId());
				}
			}
		}

		//delete all franchisees
		franchiseeManagement.deleteFranchiseeByFranchisorId(id);

		//first need to get and delete Franchisor Users
		log.fine("Getting franchisor users...");
		List<FranchisorUserData> franchisorUsers = this.getFranchisorUsers(id);

		if (franchisorUsers != null && !franchisorUsers.isEmpty()) {
			for (FranchisorUserData franchisorUser: franchisorUsers) {
				log.fine("Delete franchisor user data");
				this.deleteFranchisorUser(franchisorUser.getId());
			}
		} else {
			log.fine("No franchisor users found");
		}

		//second need to get and delete Franchisor Profiles
		log.fine("Getting franchisor user groups...");
		List<UserGroupData> franchisorUserGroups = userManagement.getEntityUserGroups(id);

		if (franchisorUserGroups != null && !franchisorUserGroups.isEmpty()) {
			for (UserGroupData franchisorUserGroup: franchisorUserGroups) {
				log.fine("Delete franchisor  user group data");
				userManagement.deleteUserGroup(franchisorUserGroup.getId());
			}
		} else {
			log.fine("No franchisor users found");
		}

		//delete franchisor and its image
		FranchisorData franchisor = franchisorDAO.delete(id);
		if (franchisor != null && franchisor.getImageKey() != null) {
			imageManagement.deleteImage(franchisor.getImageKey());
		}

		return franchisor;
	}

	@Override
	public List<FranchisorUserData> getFranchisorUsers(Long franchisorId) {

		log.fine("Getting franchisor users...");
		List<FranchisorUserData> franchisorUsers = franchisorUserDAO.findByFranchisorId(franchisorId);

		//for each user, get generic user data
		log.fine("Getting generic user data...");
		if (franchisorUsers != null && !franchisorUsers.isEmpty()) {
			for (FranchisorUserData franchisorUser: franchisorUsers) {
				franchisorUser.setUser(userManagement.getUser(franchisorUser.getUserId()));
			}
		}

		return franchisorUsers;
	}

	@Override
	public FranchisorUserData saveFranchisorUser(FranchisorUserData franchisorUser) {

		franchisorUser = franchisorUserDAO.save(franchisorUser);

		//set user data to return complete franchisor user data
		franchisorUser.setUser(userManagement.getUser(franchisorUser.getUserId()));
		return franchisorUser;
	}

	@Override
	public FranchisorUserData deleteFranchisorUser(Long id) {

		//delete franchisor user groups
		List<UserGroupEntityUserData> franchisorUserGroups = userManagement.getUserGroupEntityUserByEntityUserId(id);
		if (franchisorUserGroups != null & !franchisorUserGroups.isEmpty()) {
			for (UserGroupEntityUserData franchisorUserGroup : franchisorUserGroups){
				userManagement.deleteUserGroupEntityUser(franchisorUserGroup.getId());
			}
		}

		FranchisorUserData franchisorUser = franchisorUserDAO.delete(id);

		//set user data to return complete franchisor user data
		franchisorUser.setUser(userManagement.getUser(franchisorUser.getUserId()));
		return franchisorUser;
	}

	@Override
	public FranchisorData addImage(Long franchisorId, String imageKey) {
		FranchisorData franchisor = franchisorDAO.findById(franchisorId);

		//BlobKey oldImage = product.getImageKey();

		franchisor.setImageKey(imageKey);
		franchisor.setImageURL(imageManagement.getImageServingUrl(imageKey));
		franchisor = franchisorDAO.save(franchisor);

		/*if (oldImage != null) {
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			blobstoreService.delete(oldImage);
		}*/

		return franchisor;
	}
	
	@Override
	public FranchisorData getFranchisorByLoginURL(String loginURL) {
		return franchisorDAO.findByLoginURL(loginURL);
	}

}
