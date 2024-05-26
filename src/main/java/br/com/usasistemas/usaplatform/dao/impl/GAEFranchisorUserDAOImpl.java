package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.FranchisorUserDAO;
import br.com.usasistemas.usaplatform.model.data.FranchisorUserData;
import br.com.usasistemas.usaplatform.model.entity.FranchisorUser;

public class GAEFranchisorUserDAOImpl extends GAEGenericDAOImpl<FranchisorUser, FranchisorUserData> implements FranchisorUserDAO {

	private static final Logger log = Logger.getLogger(GAEFranchisorUserDAOImpl.class.getName());
	
	@Override
	public List<FranchisorUserData> findByFranchisorId(Long id) {
		List<FranchisorUserData> result = new ArrayList<FranchisorUserData>();

		try {
			List<FranchisorUser> users = ofy().load().type(FranchisorUser.class)
				.filter("franchisorId", id)
				.list();
			if (users != null && !users.isEmpty())
				result = this.getConverter().convertToDataList(users);
		} catch (Exception e) {
			log.warning("Error when querying for FranchisorUser by FranchisorId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<FranchisorUserData> findByUserId(Long id) {
		List<FranchisorUserData> result = new ArrayList<FranchisorUserData>();

		try {
			List<FranchisorUser> users = ofy().load().type(FranchisorUser.class)
				.filter("userId", id)
				.list();
			if (users != null && !users.isEmpty())
				result = this.getConverter().convertToDataList(users);
		} catch (Exception e) {
			log.warning("Error when querying for FranchisorUser by UserId: " + e.toString());
		}			

		return result;
	}

}
