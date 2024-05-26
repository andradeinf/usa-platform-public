package br.com.usasistemas.usaplatform.bizo;

import java.util.List;

import br.com.usasistemas.usaplatform.api.request.data.ProfileData;
import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.api.response.data.UserProfileDataResponse;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public interface UserManagementBO {
	
	public PublicUserData getUser(Long id);
	public PublicUserData getUser(String email);
	public PublicUserData saveUser(PublicUserData user) throws Exception;
	public PublicUserData deleteUser(Long id);
	public List<PublicUserData> getUsersByRole(UserProfileEnum role);
	public PublicUserData validatePasswordReset(String uid);
	public boolean deletePasswordResetToken(String uid);
	public List<PublicUserData> getAllUsers();
	public PublicUserData updateUserProfile(ProfileData profile) throws Exception;
	public PublicUserData updateUserPassword(Long id, String newPassword) throws Exception;
	public void resetPassword(String username, DomainConfigurationData domainConfiguration);
	public UserProfileData authenticateUser(String username, String password, String domainKey);
	public List<UserProfileDataResponse> getUserProfiles(UserProfileData user);
	public List<EnumValidValueResponseData> getUserProfileTypes();
	
	public UserGroupData getUserGroup(Long id);
	public List<UserGroupData> getEntityUserGroups(Long entityId);
	public UserGroupData saveUserGroup(UserGroupData userGroup);
	public UserGroupData deleteUserGroup(Long userGroupId);
	
	public List<UserGroupEntityUserData> getUserGroupEntityUserByEntityUserId(Long entityUserId);
	public UserGroupEntityUserData saveUserGroupEntityUser(UserGroupEntityUserData userGroupEntityUser);
	public UserGroupEntityUserData deleteUserGroupEntityUser(Long userGroupId, Long entityUserId);
	public UserGroupEntityUserData deleteUserGroupEntityUser(Long userGroupEentityUserId);
	
	public List<UserGroupData> getSelectedRoleUserGroups(Long entityId, Long entityUserId);
	public List<Long> getUserGroupUserIds(Long userGroupId);
	public List<Long> getUserGroupEntityUserIds(Long userGroupId);

}
