package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.TrainingViewControlData;

public class WSTrainingViewControlResponse extends GenericResponse {

	private TrainingViewControlData trainingViewControl;

	public TrainingViewControlData getTrainingViewControl() {
		return trainingViewControl;
	}

	public void setTrainingViewControl(TrainingViewControlData trainingViewControl) {
		this.trainingViewControl = trainingViewControl;
	}
		
}
