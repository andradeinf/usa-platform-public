package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.entity.UserGroupEntityUser;

public interface UserGroupEntityUserDAO extends GenericDAO<UserGroupEntityUser, UserGroupEntityUserData> {
	
	public List<UserGroupEntityUserData> findByEntityUserId(Long id);
	public UserGroupEntityUserData findByUserGroupIdAndEntityUserId(Long userGroupId, Long entityUserId);
	public List<UserGroupEntityUserData> findByUserGroupId(Long userGroupId);
	
}
