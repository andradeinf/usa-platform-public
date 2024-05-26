package br.com.usasistemas.usaplatform.dao;

import br.com.usasistemas.usaplatform.model.data.LetsEncryptChallengeData;
import br.com.usasistemas.usaplatform.model.entity.LetsEncryptChallenge;

public interface LetsEncryptChallengeDAO extends GenericDAO<LetsEncryptChallenge, LetsEncryptChallengeData> {

	public LetsEncryptChallengeData findByChallenge(String challenge);
	
}
