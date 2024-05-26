package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.TutorialData;

public class WSTutorialResponse extends GenericResponse {

	private TutorialData tutorial;

	public TutorialData getTutorial() {
		return tutorial;
	}

	public void setTutorial(TutorialData tutorial) {
		this.tutorial = tutorial;
	}
		
}
