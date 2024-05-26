package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import br.com.usasistemas.usaplatform.api.request.data.ProfileData;
import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.api.response.data.UserProfileDataResponse;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.MailManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.common.util.PasswordUtil;
import br.com.usasistemas.usaplatform.dao.AdministratorUserDAO;
import br.com.usasistemas.usaplatform.dao.FranchiseeUserDAO;
import br.com.usasistemas.usaplatform.dao.FranchisorUserDAO;
import br.com.usasistemas.usaplatform.dao.PasswordResetDAO;
import br.com.usasistemas.usaplatform.dao.SupplierUserDAO;
import br.com.usasistemas.usaplatform.dao.UserDAO;
import br.com.usasistemas.usaplatform.dao.UserGroupDAO;
import br.com.usasistemas.usaplatform.dao.UserGroupEntityUserDAO;
import br.com.usasistemas.usaplatform.model.data.AdministratorUserData;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.FeatureFlagData;
import br.com.usasistemas.usaplatform.model.data.FranchisorUserData;
import br.com.usasistemas.usaplatform.model.data.PasswordData;
import br.com.usasistemas.usaplatform.model.data.PasswordResetData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.SupplierUserData;
import br.com.usasistemas.usaplatform.model.data.UserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class UserManagementBOImpl implements UserManagementBO {
	
	private static final Logger log = Logger.getLogger(UserManagementBOImpl.class.getName());
	private UserDAO userDAO;
	private AdministratorUserDAO administratorUserDAO;
	private FranchiseeUserDAO franchiseeUserDAO;
	private FranchisorUserDAO franchisorUserDAO;
	private SupplierUserDAO supplierUserDAO;
	private UserGroupDAO userGroupDAO;
	private UserGroupEntityUserDAO userGroupEntityUserDAO;
	private PasswordResetDAO passwordResetDAO;
	private MailManagementBO mailManagement;
	private SupplierManagementBO supplierManagement;
	private FranchisorManagementBO franchisorManagement;
	private FranchiseeManagementBO franchiseeManagement;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public AdministratorUserDAO getAdministratorUserDAO() {
		return administratorUserDAO;
	}

	public void setAdministratorUserDAO(AdministratorUserDAO administratorUserDAO) {
		this.administratorUserDAO = administratorUserDAO;
	}

	public FranchiseeUserDAO getFranchiseeUserDAO() {
		return franchiseeUserDAO;
	}

	public void setFranchiseeUserDAO(FranchiseeUserDAO franchiseeUserDAO) {
		this.franchiseeUserDAO = franchiseeUserDAO;
	}

	public FranchisorUserDAO getFranchisorUserDAO() {
		return franchisorUserDAO;
	}

	public void setFranchisorUserDAO(FranchisorUserDAO franchisorUserDAO) {
		this.franchisorUserDAO = franchisorUserDAO;
	}

	public SupplierUserDAO getSupplierUserDAO() {
		return supplierUserDAO;
	}

	public void setSupplierUserDAO(SupplierUserDAO supplierUserDAO) {
		this.supplierUserDAO = supplierUserDAO;
	}

	public UserGroupDAO getUserGroupDAO() {
		return userGroupDAO;
	}

	public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
		this.userGroupDAO = userGroupDAO;
	}

	public UserGroupEntityUserDAO getUserGroupEntityUserDAO() {
		return userGroupEntityUserDAO;
	}

	public void setUserGroupEntityUserDAO(UserGroupEntityUserDAO userGroupEntityUserDAO) {
		this.userGroupEntityUserDAO = userGroupEntityUserDAO;
	}
	
	public PasswordResetDAO getPasswordResetDAO() {
		return passwordResetDAO;
	}

	public void setPasswordResetDAO(PasswordResetDAO passwordResetDAO) {
		this.passwordResetDAO = passwordResetDAO;
	}

	public MailManagementBO getMailManagement() {
		return mailManagement;
	}

	public void setMailManagement(MailManagementBO mailManagement) {
		this.mailManagement = mailManagement;
	}

	public SupplierManagementBO getSupplierManagement() {
		return supplierManagement;
	}

	public void setSupplierManagement(SupplierManagementBO supplierManagement) {
		this.supplierManagement = supplierManagement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManagement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}

	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	@Override
	public PublicUserData getUser(Long id) {

		log.fine("Getting main user data");
		return getPublicUserData(userDAO.findById(id));

	}
	
	@Override
	public PublicUserData getUser(String email) {
		
		log.fine("Getting main user data by email");
		return getPublicUserData(userDAO.findByEmail(email));
		
	}
	
	private void getAdditionalUserData(UserProfileData user, String domainKey){
		
		log.fine("Getting administrator user data");
		AdministratorUserData administratorUser = administratorUserDAO.findById(user.getId());
		if (administratorUser != null){
			user.setAdministrator(administratorUser);
		} else {
			log.fine("User selected is not an administrator");
		}
		
		log.fine("Getting franchisee user data");
		List<FranchiseeUserData> franchiseeUserList = franchiseeUserDAO.findByUserId(user.getId());
		if (franchiseeUserList != null && !franchiseeUserList.isEmpty()){
			
			//Remove franchisee, if franchisor is not for current domain
			for (int index = franchiseeUserList.size() - 1; index >= 0; index--) {
				FranchiseeData franchisee = franchiseeManagement.getFranchisee(franchiseeUserList.get(index).getFranchiseeId());
				FranchisorData franchisor = franchisorManagement.getFranchisor(franchisee.getFranchisorId());
				if (!franchisor.getPreferedDomainKey().equals(domainKey)){
					franchiseeUserList.remove(index);
				} else {
					FeatureFlagData feature = new FeatureFlagData();
					feature.setFlagAnnouncement(franchisor.getFlagAnnouncement());
					feature.setFlagCalendar(franchisor.getFlagCalendar());
					feature.setFlagDocuments(franchisor.getFlagDocuments());
					feature.setFlagTraining(franchisor.getFlagTraining());
					user.putFeatureFlag(franchiseeUserList.get(index).getFranchiseeId(), feature);
				}
			}
			
			if (!franchiseeUserList.isEmpty()) {
				user.setFranchisee(franchiseeUserList.get(0));
				user.setFranchiseeList(franchiseeUserList);
			} else {
				log.fine("User selected is not a franchisee for domain " + domainKey);
			}
						
		} else {
			log.fine("User selected is not a franchisee");
		}	
		
		log.fine("Getting franchisor user data");
		List<FranchisorUserData> franchisorUserList = franchisorUserDAO.findByUserId(user.getId());
		if (franchisorUserList != null && !franchisorUserList.isEmpty()){

			// remove franchisors that are not for current domain
			for (int index = franchisorUserList.size() - 1; index >= 0; index--) {
				FranchisorData franchisor = franchisorManagement.getFranchisor(franchisorUserList.get(index).getFranchisorId());
				if (!franchisor.getPreferedDomainKey().equals(domainKey)){
					franchisorUserList.remove(index);
				} else {
					FeatureFlagData feature = new FeatureFlagData();
					feature.setFlagAnnouncement(franchisor.getFlagAnnouncement());
					feature.setFlagCalendar(franchisor.getFlagCalendar());
					feature.setFlagDocuments(franchisor.getFlagDocuments());
					feature.setFlagTraining(franchisor.getFlagTraining());
					user.putFeatureFlag(franchisorUserList.get(index).getFranchisorId(), feature);
				}
			}
			
			if (!franchisorUserList.isEmpty()) {
				user.setFranchisor(franchisorUserList.get(0));
				user.setFranchisorList(franchisorUserList);
			} else {
				log.fine("User selected is not a franchisor for domain " + domainKey);
			}
			
		} else {
			log.fine("User selected is not a franchisor");
		}			
		
		log.fine("Getting supplier user data");
		List<SupplierUserData> supplierUserList = supplierUserDAO.findByUserId(user.getId());
		if (supplierUserList != null && !supplierUserList.isEmpty()){
			
			// remove supplier that are not for current domain
			for (int index = supplierUserList.size() - 1; index >= 0; index--) {
				SupplierData supplier = supplierManagement.getSupplier(supplierUserList.get(index).getSupplierId());
				if (!supplier.getPreferedDomainKey().equals(domainKey)){
					supplierUserList.remove(index);
				}
			}
			
			if (!supplierUserList.isEmpty()) {
				user.setSupplier(supplierUserList.get(0));
				user.setSupplierList(supplierUserList);
			} else {
				log.fine("User selected is not a supplier for domain " + domainKey);
			}			
			
		} else {
			log.fine("User selected is not a supplier");
		}			
	}
	
	@Override
	public PublicUserData saveUser(PublicUserData user) throws Exception {
		
		//Get original entity and update data, do avoid lose other data (like password)
		UserData userData = userDAO.findById(user.getId());
		
		// NEW USER
		if (userData == null) {
			userData = new UserData();
			
			//Initialize default password for new users
			PasswordData pd = PasswordUtil.encrypt("DEFAULT_PASSWORD");
			userData.setPasswordHash(pd.getPasswordHash());
			userData.setPasswordSalt(pd.getPasswordSalt());
		}
		
		userData.setUsername(user.getEmail().toUpperCase());
		userData.setName(user.getName());
		userData.setEmail(user.getEmail());
		userData.setPreferedDomainKey(user.getPreferedDomainKey());
		userData.setEnabled(user.getEnabled());
		
		//Apply new password if received
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			PasswordData pd = PasswordUtil.encrypt(user.getPassword());
			userData.setPasswordHash(pd.getPasswordHash());
			userData.setPasswordSalt(pd.getPasswordSalt());
		}
		
		return getPublicUserData(userDAO.save(userData));
	}
	
	@Override
	public PublicUserData updateUserPassword(Long id, String newPassword) throws Exception{
		
		//Get original entity
		PublicUserData user = getPublicUserData(userDAO.findById(id));
		user.setPassword(newPassword);
		
		return this.saveUser(user);
	}
	
	@Override
	public PublicUserData deleteUser(Long id) {
		
		// Delete dependencies with Franchisees
		List<FranchiseeUserData> franchiseeUsers = franchiseeUserDAO.findByUserId(id);
		if (franchiseeUsers != null && !franchiseeUsers.isEmpty()) {
			for (FranchiseeUserData franchiseeUser : franchiseeUsers) {
				franchiseeUserDAO.delete(franchiseeUser.getId());
			}
		}
		
		// Delete dependencies with Franchisors
		List<FranchisorUserData> franchisorUsers = franchisorUserDAO.findByUserId(id);
		if (franchisorUsers != null && !franchisorUsers.isEmpty()) {
			for (FranchisorUserData franchisorUser : franchisorUsers) {
				franchisorUserDAO.delete(franchisorUser.getId());
			}
		}
		
		// Delete dependencies with Suppliers
		List<SupplierUserData> supplierUsers = supplierUserDAO.findByUserId(id);
		if (supplierUsers != null && !supplierUsers.isEmpty()) {
			for (SupplierUserData supplierUser : supplierUsers) {
				supplierUserDAO.delete(supplierUser.getId());
			}
		}
		
		return getPublicUserData(userDAO.delete(id));
	}

	@Override
	public List<PublicUserData> getUsersByRole(UserProfileEnum role) {
		List<PublicUserData> result = new ArrayList<PublicUserData>();
		
		//Get administrators
		if (role == UserProfileEnum.ADMINISTRATOR) {
			List<AdministratorUserData> administrators = administratorUserDAO.listAll();
			if (administrators != null && !administrators.isEmpty()) {
				for(AdministratorUserData user: administrators){
					result.add(getPublicUserData(userDAO.findById(user.getId())));
				}
			}
		}
		
		//Get franchisees
		if (role == UserProfileEnum.FRANCHISEE) {
			List<FranchiseeUserData> franchisees = franchiseeUserDAO.listAll();
			if (franchisees != null && !franchisees.isEmpty()) {
				for(FranchiseeUserData user: franchisees){
					result.add(getPublicUserData(userDAO.findById(user.getId())));
				}
			}
		}
		
		//Get franchisors
		if (role == UserProfileEnum.FRANCHISOR) {
			List<FranchisorUserData> franchisors = franchisorUserDAO.listAll();
			if (franchisors != null && !franchisors.isEmpty()) {
				for(FranchisorUserData user: franchisors){
					result.add(getPublicUserData(userDAO.findById(user.getId())));
				}
			}
		}
		
		//Get supplier
		if (role == UserProfileEnum.SUPPLIER) {
			List<SupplierUserData> suppliers = supplierUserDAO.listAll();
			if (suppliers != null && !suppliers.isEmpty()) {
				for(SupplierUserData user: suppliers){
					result.add(getPublicUserData(userDAO.findById(user.getId())));
				}
			}
		}
				
		return result;
	}

	@Override
	public PublicUserData validatePasswordReset(String uid) {

		PublicUserData userData = null;
		
		//get password reset request
		PasswordResetData passwordReset = passwordResetDAO.findByUid(uid);

		if (passwordReset != null) {			
			userData = getPublicUserData(userDAO.findById(passwordReset.getUserId()));
		}
				
		return userData;
	}
	
	@Override
	public boolean deletePasswordResetToken(String uid) {
		
		boolean result = false;
		
		//get password reset request
		PasswordResetData passwordReset = passwordResetDAO.findByUid(uid);
		if (passwordReset != null) {			
			passwordResetDAO.delete(passwordReset.getId());
			result = true;
		}
				
		return result;
	}

	@Override
	public List<PublicUserData> getAllUsers() {
		List<PublicUserData> users = new ArrayList<PublicUserData>();
		
		List<UserData> userDataList = userDAO.listAll();
		if (userDataList != null && !userDataList.isEmpty()) {
			for (UserData user: userDataList) {
				users.add(getPublicUserData(user));
			}
		}
		
		return users;
	}
	
	private PublicUserData getPublicUserData(UserData userData){
		PublicUserData response = null;
		
		if (userData != null) {
			response = new PublicUserData();
			response.setId(userData.getId());
			response.setUsername(userData.getUsername());
			response.setName(userData.getName());
			response.setEmail(userData.getEmail());
			response.setPreferedDomainKey(userData.getPreferedDomainKey());
			response.setEnabled(userData.getEnabled());
		}
		
		return response;
	}

	@Override
	public void resetPassword(String username, DomainConfigurationData domainKey) {

		log.fine("Getting user data");
		UserData user = userDAO.findByUsername(username);
		
		//check username
		if (user != null) {
			log.fine("Valid user... send reset password email to: " + user.getEmail());
			try {
				
				//generate UID
				UUID uid = UUID.randomUUID();
				
				//prepare request
				PasswordResetData passwordRequest = new PasswordResetData();
				passwordRequest.setUserId(user.getId());
				passwordRequest.setRequestDate(new Date());
				passwordRequest.setUid(uid.toString());
				
				//save password reset request
				passwordResetDAO.save(passwordRequest);
				
				//send e-mail to user
				mailManagement.sendResetPassword(user, uid.toString(), domainKey);
				
			} catch (Exception e) {
				log.severe("Error saving Password Reset request: " + e.toString());
			}
		} else {
			log.fine("User not found for username provided: " + username);
		}
	}

	@Override
	public UserProfileData authenticateUser(String username, String password, String domainKey) {

		UserProfileData userProfile = null;
		
		log.fine("Getting user data");
		UserData user = userDAO.findByUsername(username);
		
		if (user != null) {
			log.fine("Checking user password");
			PasswordData pd = new PasswordData(user.getPasswordHash(), user.getPasswordSalt());
			if (PasswordUtil.authenticate(pd, password)) {
				log.fine("Valid password");
				userProfile = new UserProfileData();
				userProfile.setId(user.getId());
				userProfile.setUsername(user.getUsername());
				userProfile.setName(user.getName());
				userProfile.setEmail(user.getEmail());
				userProfile.setEnabled(user.getEnabled());
				getAdditionalUserData(userProfile, domainKey);
			} else {
				log.fine("Invalid password");
			}
		} else {
			log.fine("User not found");
		}
		
		return userProfile;

	}

	@Override
	public List<UserProfileDataResponse> getUserProfiles(UserProfileData user) {
		List<UserProfileDataResponse> response = new ArrayList<UserProfileDataResponse>();
		
		if (user.getAdministrator() != null){
			UserProfileDataResponse administratorProfile = new UserProfileDataResponse();
			administratorProfile.setProfileType(UserProfileEnum.ADMINISTRATOR);
			administratorProfile.setId(user.getAdministrator().getId());
			administratorProfile.setName("Administrador");
			response.add(administratorProfile);
		}
		
		if (user.getSupplierList() != null){
			for (SupplierUserData supplierUser: user.getSupplierList()){
				SupplierData supplier = supplierManagement.getSupplier(supplierUser.getSupplierId());
				
				UserProfileDataResponse supplierProfile = new UserProfileDataResponse();
				supplierProfile.setProfileType(UserProfileEnum.SUPPLIER);
				supplierProfile.setId(supplier.getId());
				supplierProfile.setName(supplier.getName());
				response.add(supplierProfile);
			}
		}
		
		if (user.getFranchisorList() != null){
			for (FranchisorUserData franchisorUser: user.getFranchisorList()){
				FranchisorData franchisor = franchisorManagement.getFranchisor(franchisorUser.getFranchisorId());
				
				UserProfileDataResponse franchisorProfile = new UserProfileDataResponse();
				franchisorProfile.setProfileType(UserProfileEnum.FRANCHISOR);
				franchisorProfile.setId(franchisor.getId());
				franchisorProfile.setName(franchisor.getName());
				franchisorProfile.setImageURL(franchisor.getImageURL());
				response.add(franchisorProfile);
			}
		}
		
		if (user.getFranchiseeList() != null){
			for (FranchiseeUserData franchiseeUser: user.getFranchiseeList()){
				FranchiseeData franchisee = franchiseeManagement.getFranchisee(franchiseeUser.getFranchiseeId());
				
				UserProfileDataResponse franchiseeProfile = new UserProfileDataResponse();
				franchiseeProfile.setProfileType(UserProfileEnum.FRANCHISEE);
				franchiseeProfile.setId(franchisee.getId());
				franchiseeProfile.setName(franchisee.getName());
				if (franchisee.getImageURL() != null && !franchisee.getImageURL().isEmpty()) {
					franchiseeProfile.setImageURL(franchisee.getImageURL());
				} else {
					franchiseeProfile.setImageURL(franchisorManagement.getFranchisor(franchisee.getFranchisorId()).getImageURL());
				}
				response.add(franchiseeProfile);
			}
		}
		
		return response;
	}

	@Override
	public List<EnumValidValueResponseData> getUserProfileTypes() {
		List<EnumValidValueResponseData> result = new ArrayList<EnumValidValueResponseData>();
		
		for (UserProfileEnum value: UserProfileEnum.values()) {
			EnumValidValueResponseData enumValue = new EnumValidValueResponseData();
			enumValue.setKey(value.name());
			enumValue.setValue(value.getDescription());
			result.add(enumValue);
		}

		return result;
	}

	@Override
	public List<UserGroupData> getEntityUserGroups(Long entityId) {
		return userGroupDAO.findByEntityId(entityId);
	}
	
	@Override
	public UserGroupData getUserGroup(Long id) {
		return userGroupDAO.findById(id);
	}

	@Override
	public UserGroupData saveUserGroup(UserGroupData userGroup) {
		return userGroupDAO.save(userGroup);
	}

	@Override
	public UserGroupData deleteUserGroup(Long userGroupId) {
		
		//check UserGroupEntityUser and do not allow delete a group that has at least one user
		List<UserGroupEntityUserData> UserGroupEntityUserList = userGroupEntityUserDAO.findByUserGroupId(userGroupId);
		if (UserGroupEntityUserList != null && !UserGroupEntityUserList.isEmpty()) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage("Não é possivel excluir o grupo de usuários pois ele possui usuários vinculados. Primeiro remova o perfil de todos os usuários.");
			throw new BusinessException(rm);
		}

		return userGroupDAO.delete(userGroupId);
	}

	@Override
	public List<UserGroupEntityUserData> getUserGroupEntityUserByEntityUserId(Long entityUserId) {
		return userGroupEntityUserDAO.findByEntityUserId(entityUserId);
	}

	@Override
	public UserGroupEntityUserData saveUserGroupEntityUser(UserGroupEntityUserData userGroupEntityUser) {
		return userGroupEntityUserDAO.save(userGroupEntityUser);
	}

	@Override
	public UserGroupEntityUserData deleteUserGroupEntityUser(Long userGroupId, Long entityUserId) {
		UserGroupEntityUserData userGroupEntityUser = userGroupEntityUserDAO.findByUserGroupIdAndEntityUserId(userGroupId, entityUserId);
		return userGroupEntityUserDAO.delete(userGroupEntityUser.getId());
	}
	
	@Override
	public UserGroupEntityUserData deleteUserGroupEntityUser(Long userGroupEentityUserId) {
		return userGroupEntityUserDAO.delete(userGroupEentityUserId);
	}

	@Override
	public List<UserGroupData> getSelectedRoleUserGroups(Long entityId, Long entityUserId) {
		
		List<UserGroupData> response = new ArrayList<UserGroupData>();
		
		List<UserGroupEntityUserData> userGroupsEntityUsers = this.getUserGroupEntityUserByEntityUserId(entityUserId);
		if (userGroupsEntityUsers != null && !userGroupsEntityUsers.isEmpty()) {
			for (UserGroupEntityUserData userGroupEntityUser :  userGroupsEntityUsers) {
				UserGroupData userGroup = this.getUserGroup(userGroupEntityUser.getUserGroupId());
				if (userGroup != null && userGroup.getEntityId().equals(entityId)) {
					response.add(userGroup);
				}
			}
		}
		
		return response;
	}
	
	@Override
	public List<Long> getUserGroupUserIds(Long userGroupId) {
		
		List<Long> userIds = new ArrayList<Long>();
		
		List<UserGroupEntityUserData> userGroups = userGroupEntityUserDAO.findByUserGroupId(userGroupId);
		if (userGroups != null && !userGroups.isEmpty()) {
			for(UserGroupEntityUserData userGroup : userGroups) {
				if (userGroup.getEntityProfile() == UserProfileEnum.ADMINISTRATOR) {
					userIds.add(userGroup.getEntityUserId());
				} else if (userGroup.getEntityProfile() == UserProfileEnum.FRANCHISOR) {
					FranchisorUserData franchisorUser = franchisorUserDAO.findById(userGroup.getEntityUserId());
					if (franchisorUser != null) {
						userIds.add(franchisorUser.getUserId());
					}					
				} else if (userGroup.getEntityProfile() == UserProfileEnum.FRANCHISEE) {
					FranchiseeUserData franchiseeUser = franchiseeUserDAO.findById(userGroup.getEntityUserId());
					if (franchiseeUser != null) {
						userIds.add(franchiseeUser.getUserId());
					}					
				} else if (userGroup.getEntityProfile() == UserProfileEnum.SUPPLIER) {
					SupplierUserData supplierUser = supplierUserDAO.findById(userGroup.getEntityUserId());
					if (supplierUser != null) {
						userIds.add(supplierUser.getUserId());
					}			
				}
			}
		}
			
		return userIds;
	}

	@Override
	public List<Long> getUserGroupEntityUserIds(Long userGroupId) {
		
		List<Long> entityUserIds = new ArrayList<Long>();
		
		List<UserGroupEntityUserData> userGroups = userGroupEntityUserDAO.findByUserGroupId(userGroupId);
		if (userGroups != null) {
			entityUserIds = userGroups
								.stream()
								.map(userGroup -> userGroup.getEntityUserId())
								.collect(Collectors.toList());
		}

		return entityUserIds;
	}

	@Override
	public PublicUserData updateUserProfile(ProfileData profile) throws Exception {
		
		PublicUserData user = getPublicUserData(userDAO.findById(profile.getId()));
		
		//Update User profile data
		user.setName(profile.getName());
		user.setEmail(profile.getEmail());
		
		//Update Password
		if (profile.getNewPassword() != null && !profile.getNewPassword().isEmpty()) {
			user.setPassword(profile.getNewPassword());
		}
		
		return this.saveUser(user);
		
	}

}
