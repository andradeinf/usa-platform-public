package br.com.usasistemas.usaplatform.bizo;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.TutorialData;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public interface TutorialManagementBO {

	public List<TutorialData> getTutorialsByUserProfileAndDomainKey(UserProfileEnum userProfile, String domainKey);
	public List<TutorialData> getAllTutorials();
	public List<TutorialData> getTutorialsByDomainKey(String domainKey);
	public TutorialData saveTutorial(TutorialData tutorial);
	public TutorialData deleteTutorial(Long id);

}
