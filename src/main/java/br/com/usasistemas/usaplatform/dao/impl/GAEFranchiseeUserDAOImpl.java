package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.FranchiseeUserDAO;
import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;
import br.com.usasistemas.usaplatform.model.entity.FranchiseeUser;

public class GAEFranchiseeUserDAOImpl extends GAEGenericDAOImpl<FranchiseeUser, FranchiseeUserData> implements FranchiseeUserDAO {
	
	private static final Logger log = Logger.getLogger(GAEFranchiseeUserDAOImpl.class.getName());

	@Override
	public List<FranchiseeUserData> findByFranchiseeId(Long id) {
		List<FranchiseeUserData> result = new ArrayList<FranchiseeUserData>();

		try {
			List<FranchiseeUser> users = ofy().load().type(FranchiseeUser.class)
				.filter("franchiseeId", id)
				.list();
			if (users != null && !users.isEmpty())
				result = this.getConverter().convertToDataList(users);
		} catch (Exception e) {
			log.warning("Error when querying for FranchiseeUser by FranchiseeId: " + e.toString());
		}		

		return result;
	}
	
	@Override
	public List<FranchiseeUserData> findByUserId(Long id) {
		List<FranchiseeUserData> result = new ArrayList<FranchiseeUserData>();

		try {
			List<FranchiseeUser> users = ofy().load().type(FranchiseeUser.class)
				.filter("userId", id)
				.list();
			if (users != null && !users.isEmpty())
				result = this.getConverter().convertToDataList(users);
		} catch (Exception e) {
			log.warning("Error when querying for FranchiseeUser by UserId: " + e.toString());
		}			

		return result;
	}

}
