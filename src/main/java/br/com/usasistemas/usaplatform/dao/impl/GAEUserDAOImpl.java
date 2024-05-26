package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.UserDAO;
import br.com.usasistemas.usaplatform.model.data.UserData;
import br.com.usasistemas.usaplatform.model.entity.User;

public class GAEUserDAOImpl extends GAEGenericDAOImpl<User, UserData> implements UserDAO {
	
	private static final Logger log = Logger.getLogger(GAEUserDAOImpl.class.getName());

	@Override
	public UserData findByEmail(String email) {
		UserData result = null;

		try {			
			List<User> users = ofy().load().type(User.class)
				.filter("email", email)
				.list();
			if (users != null && !users.isEmpty())
				result = this.getConverter().convertToData(users.get(0));
		} catch (Exception e) {
			log.warning("Error when querying for User by email: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public UserData findByUsername(String username) {
		UserData result = null;

		try {
			List<User> users = ofy().load().type(User.class)
			.filter("username", username)
			.list();
			if (users != null && !users.isEmpty())
				result = this.getConverter().convertToData(users.get(0));
		} catch (Exception e) {
			log.warning("Error when querying for User by username: " + e.toString());
		}			
	
		return result;
	}

}
