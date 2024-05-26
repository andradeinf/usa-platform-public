package br.com.usasistemas.usaplatform.bizo;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.TrainingData;
import br.com.usasistemas.usaplatform.model.data.TrainingViewControlData;

public interface TrainingManagementBO {

	public List<TrainingData> getFranchisorTrainings(Long franchisorId);
	public List<TrainingData> getFanchiseeFranchisorTrainings(Long franchisorId, Long franchiseeId);
	public TrainingData createTraining(TrainingData training);
	public TrainingData updateTraining(TrainingData changedTraining);
	public TrainingData deleteTraining(Long id);
	public TrainingData getTraining(Long id);
	public void updateTrainingsOrder(Map<Long, Long> trainingsOrder);
	public TrainingViewControlData updateTrainingViewControlCurrentTime(TrainingViewControlData updatedTrainingViewControl);
	public List<TrainingViewControlData> getTrainingViewControl(Long trainingId, Long franchiseeId);
	public Map<Long, PublicUserData> getTrainingViewControlsUsers(List<TrainingViewControlData> trainingViewControls);

}
