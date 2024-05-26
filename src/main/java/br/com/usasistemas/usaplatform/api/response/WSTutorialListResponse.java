package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.TutorialData;

public class WSTutorialListResponse extends GenericResponse {

	private List<TutorialData> tutorials;

	public List<TutorialData> getTutorials() {
		return tutorials;
	}

	public void setTutorials(List<TutorialData> tutorials) {
		this.tutorials = tutorials;
	}
	
}
