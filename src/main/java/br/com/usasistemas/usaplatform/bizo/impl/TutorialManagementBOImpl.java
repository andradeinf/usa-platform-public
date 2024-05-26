package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.bizo.TutorialManagementBO;
import br.com.usasistemas.usaplatform.dao.TutorialDAO;
import br.com.usasistemas.usaplatform.model.data.TutorialData;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class TutorialManagementBOImpl implements TutorialManagementBO {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(TutorialManagementBOImpl.class.getName());
	private TutorialDAO tutorialDAO;
	
	public TutorialDAO getTutorialDAO() {
		return tutorialDAO;
	}
	
	public void setTutorialDAO(TutorialDAO tutoralDAO) {
		this.tutorialDAO = tutoralDAO;
	}

	@Override
	public List<TutorialData> getTutorialsByUserProfileAndDomainKey(UserProfileEnum userProfile, String domainKey) {
		return tutorialDAO.findByUserProfileAndDomainKey(userProfile, domainKey);
	}
	
	@Override
	public List<TutorialData> getAllTutorials() {
		return tutorialDAO.listAll();
	}

	@Override
	public TutorialData saveTutorial(TutorialData tutorial) {
		return tutorialDAO.save(tutorial);
	}

	@Override
	public TutorialData deleteTutorial(Long id) {
		return tutorialDAO.delete(id);
	}

	@Override
	public List<TutorialData> getTutorialsByDomainKey(String domainKey) {
		return tutorialDAO.findByDomainKey(domainKey);
	}


}
