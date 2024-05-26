package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.TrainingData;

public class WSTrainingListResponse extends GenericResponse {

	private List<TrainingData> trainings;

	public List<TrainingData> getTrainings() {
		return trainings;
	}

	public void setTrainings(List<TrainingData> trainings) {
		this.trainings = trainings;
	}
	
}
