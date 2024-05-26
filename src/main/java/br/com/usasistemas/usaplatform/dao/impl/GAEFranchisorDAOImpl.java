package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.FranchisorDAO;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.entity.Franchisor;

public class GAEFranchisorDAOImpl extends GAEGenericDAOImpl<Franchisor, FranchisorData> implements FranchisorDAO {
	
	private static final Logger log = Logger.getLogger(GAEFranchisorDAOImpl.class.getName());

	@Override
	public FranchisorData findByLoginURL(String loginURL) {
		FranchisorData result = null;

		try {
			List<Franchisor> franchisors = ofy().load().type(Franchisor.class)
				.filter("loginURL", loginURL)
				.list();
			if (franchisors != null && !franchisors.isEmpty())
				result = this.getConverter().convertToData(franchisors.get(0));
		} catch (Exception e) {
			log.warning("Error when querying for Franchisor by loginURL: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<FranchisorData> findByPreferedDomainKey(String domainKey) {
		List<FranchisorData> result = new ArrayList<FranchisorData>();

		try {
			List<Franchisor> franchisors = ofy().load().type(Franchisor.class)
				.filter("preferedDomainKey", domainKey)
				.list();
			if (franchisors != null && !franchisors.isEmpty())
				result = this.getConverter().convertToDataList(franchisors);
		} catch (Exception e) {
			log.warning("Error when querying for Franchisor by PreferedDomainKey: " + e.toString());
		}			
	
		return result;
	}
}
