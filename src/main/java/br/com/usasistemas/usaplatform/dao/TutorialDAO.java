package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.TutorialData;
import br.com.usasistemas.usaplatform.model.entity.Tutorial;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public interface TutorialDAO extends GenericDAO<Tutorial, TutorialData> {

	public List<TutorialData> findByUserProfileAndDomainKey(UserProfileEnum userProfile, String domainKey);
	public List<TutorialData> findByDomainKey(String domainKey);
	
}
