package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.TutorialDAO;
import br.com.usasistemas.usaplatform.model.data.TutorialData;
import br.com.usasistemas.usaplatform.model.entity.Tutorial;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class GAETutorialDAOImpl extends GAEGenericDAOImpl<Tutorial, TutorialData> implements TutorialDAO {
	
	private static final Logger log = Logger.getLogger(GAETutorialDAOImpl.class.getName());

	@Override
	public List<TutorialData> findByUserProfileAndDomainKey(UserProfileEnum userProfile, String domainKey) {
		List<TutorialData> result = new ArrayList<TutorialData>();

		try {
			List<Tutorial> tutorials = ofy().load().type(Tutorial.class)
				.filter("userProfile", userProfile)
				.filter("domainKeys", domainKey)
				.list();
			if (tutorials != null && !tutorials.isEmpty())
				result = this.getConverter().convertToDataList(tutorials);
		} catch (Exception e) {
			log.warning("Error when querying for Tutorials by userProfile and domainKey: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<TutorialData> findByDomainKey(String domainKey) {
		List<TutorialData> result = new ArrayList<TutorialData>();
		
		try {
			List<Tutorial> tutorials = ofy().load().type(Tutorial.class)
				.filter("domainKeys", domainKey)
				.list();
			if (tutorials != null && !tutorials.isEmpty())
				result = this.getConverter().convertToDataList(tutorials);
		} catch (Exception e) {
			log.warning("Error when querying for Tutorials by domainKeys: " + e.toString());
		}			
	
		return result;
	}
	
}
