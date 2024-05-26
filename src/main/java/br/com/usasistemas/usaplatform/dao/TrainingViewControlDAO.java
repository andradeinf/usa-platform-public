package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.TrainingViewControlData;
import br.com.usasistemas.usaplatform.model.entity.TrainingViewControl;

public interface TrainingViewControlDAO extends GenericDAO<TrainingViewControl, TrainingViewControlData> {

	public List<TrainingViewControlData> findByTrainingIdAndFranchiseeId(Long trainingId, Long franchiseeId);
	public TrainingViewControlData findByTrainingIdAndFranchiseeUserId(Long trainingId, Long franchiseeUserId);
	public List<TrainingViewControlData> findByTrainingId(Long trainingId);
	
}
