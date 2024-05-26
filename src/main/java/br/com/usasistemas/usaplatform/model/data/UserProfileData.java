package br.com.usasistemas.usaplatform.model.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@SuppressWarnings("serial")
public class UserProfileData implements Serializable {
	
	private Long id;
	private String email;
	private String username;
	private String name;
	private Boolean enabled;
	private FranchisorUserData franchisor = null;
	private List<FranchisorUserData> franchisorList = null;
	private FranchiseeUserData franchisee = null;
	private List<FranchiseeUserData> franchiseeList = null;
	private SupplierUserData supplier = null;
	private List<SupplierUserData> supplierList = null;
	private AdministratorUserData administrator = null;
	private UserProfileEnum selectedRole = null;
	private Boolean hasMultipleProfiles = null;
	private Boolean hasNoProfile = null;
	private Map<Long, FeatureFlagData> featureFlags = new HashMap<Long, FeatureFlagData>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public FranchisorUserData getFranchisor() {
		return franchisor;
	}

	public void setFranchisor(FranchisorUserData franchisor) {
		this.franchisor = franchisor;
	}
	
	public List<FranchisorUserData> getFranchisorList() {
		return franchisorList;
	}

	public void setFranchisorList(List<FranchisorUserData> franchisorList) {
		this.franchisorList = franchisorList;
	}

	public FranchiseeUserData getFranchisee() {
		return franchisee;
	}

	public void setFranchisee(FranchiseeUserData franchisee) {
		this.franchisee = franchisee;
	}
	
	public List<FranchiseeUserData> getFranchiseeList() {
		return franchiseeList;
	}

	public void setFranchiseeList(List<FranchiseeUserData> franchiseeList) {
		this.franchiseeList = franchiseeList;
	}

	public SupplierUserData getSupplier() {
		return supplier;
	}

	public void setSupplier(SupplierUserData supplier) {
		this.supplier = supplier;
	}
	
	public List<SupplierUserData> getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(List<SupplierUserData> supplierList) {
		this.supplierList = supplierList;
	}

	public AdministratorUserData getAdministrator() {
		return administrator;
	}

	public void setAdministrator(AdministratorUserData administrator) {
		this.administrator = administrator;
	}

	public UserProfileEnum getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(UserProfileEnum selectedRole) {
		this.selectedRole = selectedRole;
	}
	
	public Boolean getHasMultipleProfiles() {
		int profileCount = 0;
		UserProfileEnum selectedRole = null;
		
		if (hasMultipleProfiles != null) {
			return hasMultipleProfiles;
		}
		
		if (this.getFranchiseeList() != null){
			selectedRole = UserProfileEnum.FRANCHISEE;
			profileCount = profileCount + this.getFranchiseeList().size();
		}
		
		if (this.getFranchisorList() != null){
			selectedRole = UserProfileEnum.FRANCHISOR;
			profileCount = profileCount + this.getFranchisorList().size();
		}
		
		if (this.getSupplierList() != null){
			selectedRole = UserProfileEnum.SUPPLIER;
			profileCount = profileCount + this.getSupplierList().size();
		}
		
		if (this.getAdministrator() != null) {
			selectedRole = UserProfileEnum.ADMINISTRATOR;
			profileCount = profileCount + 1;
		}
		
		if (profileCount == 1) {
			this.setSelectedRole(selectedRole);
		}
		
		hasMultipleProfiles = profileCount > 1;
		
		return hasMultipleProfiles;
	}

	public void setHasMultipleProfiles(Boolean hasMultipleProfiles) {
		this.hasMultipleProfiles = hasMultipleProfiles;
	}
	
	public Boolean getHasNoProfile() {
		int profileCount = 0;
		
		if (hasNoProfile != null) {
			return hasNoProfile;
		}
		
		if (this.getFranchiseeList() != null){
			profileCount = profileCount + this.getFranchiseeList().size();
		}
		
		if (this.getFranchisorList() != null){
			profileCount = profileCount + this.getFranchisorList().size();
		}
		
		if (this.getSupplierList() != null){
			profileCount = profileCount + this.getSupplierList().size();
		}
		
		if (this.getAdministrator() != null) {
			profileCount = profileCount + 1;
		}
		
		hasNoProfile = profileCount == 0;
		
		return hasNoProfile;
	}

	public void setHasNoProfile(Boolean hasNoProfile) {
		this.hasNoProfile = hasNoProfile;
	}
	
	public UserProfileData setUserProfile(Long id) {
		
		if (this.getAdministrator() != null){
			if (this.getAdministrator().getId().equals(id)) {
				this.setSelectedRole(UserProfileEnum.ADMINISTRATOR);
				return this;
			}
		}
		
		if (this.getSupplierList() != null){
			for (SupplierUserData supplierUser: this.getSupplierList()){
				if (supplierUser.getSupplierId().equals(id)) {
					this.setSelectedRole(UserProfileEnum.SUPPLIER);
					this.setSupplier(supplierUser);
					return this;
				}
			}
		}
		
		if (this.getFranchisorList() != null){
			for (FranchisorUserData franchisorUser: this.getFranchisorList()){
				if (franchisorUser.getFranchisorId().equals(id)) {
					this.setSelectedRole(UserProfileEnum.FRANCHISOR);
					this.setFranchisor(franchisorUser);
					return this;
				}
			}
		}
		
		if (this.getFranchiseeList() != null){
			for (FranchiseeUserData franchiseeUser: this.getFranchiseeList()){
				if (franchiseeUser.getFranchiseeId().equals(id)) {
					this.setSelectedRole(UserProfileEnum.FRANCHISEE);
					this.setFranchisee(franchiseeUser);
					return this;
				}
			}
		}
		
		return null;
	}
	
	public Long getSelectedEntityId() {
		
		Long entityId = null;
		
		if (this.selectedRole.equals(UserProfileEnum.ADMINISTRATOR)) {
			entityId = 0L;
		} else if (this.selectedRole.equals(UserProfileEnum.FRANCHISOR)) {
			entityId = this.getFranchisor().getFranchisorId();
		} else if (this.selectedRole.equals(UserProfileEnum.FRANCHISEE)) {
			entityId = this.getFranchisee().getFranchiseeId();
		} else if (this.selectedRole.equals(UserProfileEnum.SUPPLIER)) {
			entityId = this.getSupplier().getSupplierId();
		}
		
		return entityId;
	}
	
	public Long getSelectedEntityUserId() {
		
		Long entityUserId = null;
		
		if (this.selectedRole.equals(UserProfileEnum.ADMINISTRATOR)) {
			entityUserId = this.getAdministrator().getId();
		} else if (this.selectedRole.equals(UserProfileEnum.FRANCHISOR)) {
			entityUserId = this.getFranchisor().getId();
		} else if (this.selectedRole.equals(UserProfileEnum.FRANCHISEE)) {
			entityUserId = this.getFranchisee().getId();
		} else if (this.selectedRole.equals(UserProfileEnum.SUPPLIER)) {
			entityUserId = this.getSupplier().getId();
		}
		
		return entityUserId;
	}

	public void putFeatureFlag(Long entityId, FeatureFlagData configuration) {
		this.featureFlags.put(entityId, configuration);
	}

	public FeatureFlagData getFeatureFlags() {
		FeatureFlagData response = this.featureFlags.get(this.getSelectedEntityId());
		if (response == null) {
			response = new FeatureFlagData();
		}
		return response;
	}

}
