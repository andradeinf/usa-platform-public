package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.UserGroupEntityUserDAO;
import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.entity.UserGroupEntityUser;

public class GAEUserGroupEntityUserDAOImpl extends GAEGenericDAOImpl<UserGroupEntityUser, UserGroupEntityUserData> implements UserGroupEntityUserDAO {
	
	private static final Logger log = Logger.getLogger(GAEUserGroupEntityUserDAOImpl.class.getName());

	@Override
	public List<UserGroupEntityUserData> findByEntityUserId(Long id) {
		List<UserGroupEntityUserData> result = new ArrayList<UserGroupEntityUserData>();

		try {
			List<UserGroupEntityUser> userGroupsEntityUser = ofy().load().type(UserGroupEntityUser.class)
				.filter("entityUserId", id)
				.list();
			if (userGroupsEntityUser != null && !userGroupsEntityUser.isEmpty())
				result = this.getConverter().convertToDataList(userGroupsEntityUser);
		} catch (Exception e) {
			log.warning("Error when querying for UserGroupsEntityUser by EntityUserId: " + e.toString());
		}			

		return result;
	}

	@Override
	public UserGroupEntityUserData findByUserGroupIdAndEntityUserId(Long userGroupId, Long entityUserId) {
		UserGroupEntityUserData result = new UserGroupEntityUserData();

		try {
			List<UserGroupEntityUser> userGroupEntityUser = ofy().load().type(UserGroupEntityUser.class)
				.filter("userGroupId", userGroupId)
				.filter("entityUserId", entityUserId)
				.list();
			if (userGroupEntityUser != null && !userGroupEntityUser.isEmpty())
				result = this.getConverter().convertToData(userGroupEntityUser.get(0));
		} catch (Exception e) {
			log.warning("Error when querying for UserGroupEntityUser by UserGroupId and EntityUserId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<UserGroupEntityUserData> findByUserGroupId(Long userGroupId) {
		List<UserGroupEntityUserData> result = new ArrayList<UserGroupEntityUserData>();

		try {
			List<UserGroupEntityUser> userGroupEntityUsers = ofy().load().type(UserGroupEntityUser.class)
				.filter("userGroupId", userGroupId)
				.list();
			if (userGroupEntityUsers != null && !userGroupEntityUsers.isEmpty())
				result = this.getConverter().convertToDataList(userGroupEntityUsers);
		} catch (Exception e) {
			log.warning("Error when querying for UserGroupEntityUsers by UserGroupId: " + e.toString());
		}			
	
		return result;
	}	

}
