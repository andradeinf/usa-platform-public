package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.TrainingData;

public class WSTrainingResponse extends GenericResponse {

	private TrainingData training;
	
	public TrainingData getTraining() {
		return training;
	}

	public void setTraining(TrainingData training) {
		this.training = training;
	}
		
}
