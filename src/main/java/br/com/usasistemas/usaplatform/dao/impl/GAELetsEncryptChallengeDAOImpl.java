package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.LetsEncryptChallengeDAO;
import br.com.usasistemas.usaplatform.model.data.LetsEncryptChallengeData;
import br.com.usasistemas.usaplatform.model.entity.LetsEncryptChallenge;

public class GAELetsEncryptChallengeDAOImpl extends GAEGenericDAOImpl<LetsEncryptChallenge, LetsEncryptChallengeData> implements LetsEncryptChallengeDAO {
		
		private static final Logger log = Logger.getLogger(GAELetsEncryptChallengeDAOImpl.class.getName());

		@Override
		public LetsEncryptChallengeData findByChallenge(String challenge) {
			LetsEncryptChallengeData result = new LetsEncryptChallengeData();

			try {
				List<LetsEncryptChallenge> challenges = ofy().load().type(LetsEncryptChallenge.class)
					.filter("challenge", challenge)
					.list();
				if (challenges != null && !challenges.isEmpty())
					result = this.getConverter().convertToData(challenges.get(0));
			} catch (Exception e) {
				log.warning("Error when querying for LetsEncryptChallenge by Challenge: " + e.toString());
			}			
		
			return result;
		}
		
	}