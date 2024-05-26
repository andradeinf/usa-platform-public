package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.PasswordResetDAO;
import br.com.usasistemas.usaplatform.model.data.PasswordResetData;
import br.com.usasistemas.usaplatform.model.entity.PasswordReset;

public class GAEPasswordResetDAOImpl extends GAEGenericDAOImpl<PasswordReset, PasswordResetData> implements PasswordResetDAO {
	
	private static final Logger log = Logger.getLogger(GAEPasswordResetDAOImpl.class.getName());

	@Override
	public PasswordResetData findByUsername(String username) {
		PasswordResetData result = null;

		try {
			List<PasswordReset> passwordResets = ofy().load().type(PasswordReset.class)
				.filter("username", username)
				.list();
			if (passwordResets != null && !passwordResets.isEmpty())
				result = this.getConverter().convertToData(passwordResets.get(0));
		} catch (Exception e) {
			log.warning("Error when querying for User by username: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public PasswordResetData findByUid(String uid) {
		PasswordResetData result = null;

		try {
			List<PasswordReset> passwordResets = ofy().load().type(PasswordReset.class)
				.filter("uid", uid)
				.list();
			if (passwordResets != null && !passwordResets.isEmpty())
				result = this.getConverter().convertToData(passwordResets.get(0));
		} catch (Exception e) {
			log.warning("Error when querying for User by email: " + e.toString());
		}			
	
		return result;
	}

}
