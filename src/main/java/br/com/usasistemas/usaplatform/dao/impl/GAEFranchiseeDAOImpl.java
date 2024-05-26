package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.FranchiseeDAO;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.entity.Franchisee;

public class GAEFranchiseeDAOImpl extends GAEGenericDAOImpl<Franchisee, FranchiseeData> implements FranchiseeDAO {
	
	private static final Logger log = Logger.getLogger(GAEFranchiseeDAOImpl.class.getName());

	@Override
	public List<FranchiseeData> findByFranchisorId(Long franchisorId) {
		List<FranchiseeData> result = new ArrayList<FranchiseeData>();

		try {
			List<Franchisee> franchisees = ofy().load().type(Franchisee.class)
				.filter("franchisorId", franchisorId)
				.list();
			if (franchisees != null && !franchisees.isEmpty())
				result = this.getConverter().convertToDataList(franchisees);
		} catch (Exception e) {
			log.warning("Error when querying for Franchisees by franchisorId: " + e.toString());
		}			
	
		return result;
	}
	
}
