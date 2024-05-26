package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.UserGroupDAO;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.entity.UserGroup;

public class GAEUserGroupDAOImpl extends GAEGenericDAOImpl<UserGroup, UserGroupData> implements UserGroupDAO {
	
	private static final Logger log = Logger.getLogger(GAEUserGroupDAOImpl.class.getName());

	@Override
	public List<UserGroupData> findByEntityId(Long entityId) {
		List<UserGroupData> result = new ArrayList<UserGroupData>();

		try {
			List<UserGroup> userGroups = ofy().load().type(UserGroup.class)
			.filter("entityId", entityId)
			.list();
			if (userGroups != null && !userGroups.isEmpty())
				result = this.getConverter().convertToDataList(userGroups);
		} catch (Exception e) {
			log.warning("Error when querying for UserGroups by entityId: " + e.toString());
		}			
	
		return result;
	}
	
}
