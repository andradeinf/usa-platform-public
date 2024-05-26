package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.SupplierUserDAO;
import br.com.usasistemas.usaplatform.model.data.SupplierUserData;
import br.com.usasistemas.usaplatform.model.entity.SupplierUser;

public class GAESupplierUserDAOImpl extends GAEGenericDAOImpl<SupplierUser, SupplierUserData> implements SupplierUserDAO {
	
	private static final Logger log = Logger.getLogger(GAESupplierUserDAOImpl.class.getName());

	@Override
	public List<SupplierUserData> findBySupplierId(Long id) {
		List<SupplierUserData> result = new ArrayList<SupplierUserData>();

		try {
			List<SupplierUser> users = ofy().load().type(SupplierUser.class)
				.filter("supplierId", id)
				.list();
			if (users != null && !users.isEmpty())
				result = this.getConverter().convertToDataList(users);
		} catch (Exception e) {
			log.warning("Error when querying for SupplierUser by SupplierId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<SupplierUserData> findByUserId(Long id) {
		List<SupplierUserData> result = new ArrayList<SupplierUserData>();

		try {
			List<SupplierUser> users = ofy().load().type(SupplierUser.class)
				.filter("userId", id)
				.list();
			if (users != null && !users.isEmpty())
				result = this.getConverter().convertToDataList(users);
		} catch (Exception e) {
			log.warning("Error when querying for SupplierUser by UserId: " + e.toString());
		}			
	
		return result;
	}

}
