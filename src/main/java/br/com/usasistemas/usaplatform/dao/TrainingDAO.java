package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.TrainingData;
import br.com.usasistemas.usaplatform.model.entity.Training;

public interface TrainingDAO extends GenericDAO<Training, TrainingData> {

	public List<TrainingData> findByFranchisorId(Long franchisorId);
	
}
