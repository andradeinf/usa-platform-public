package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.TrainingDAO;
import br.com.usasistemas.usaplatform.model.data.TrainingData;
import br.com.usasistemas.usaplatform.model.entity.Training;

public class GAETrainingDAOImpl extends GAEGenericDAOImpl<Training, TrainingData> implements TrainingDAO {
	
	private static final Logger log = Logger.getLogger(GAETrainingDAOImpl.class.getName());

	@Override
	public List<TrainingData> findByFranchisorId(Long franchisorId) {
		List<TrainingData> result = new ArrayList<TrainingData>();

		try {
			List<Training> trainings = ofy().load().type(Training.class)
				.filter("franchisorId", franchisorId)
				.list();
			if (trainings != null && !trainings.isEmpty())
				result = this.getConverter().convertToDataList(trainings);
		} catch (Exception e) {
			log.warning("Error when querying for Trainings by franchisorId: " + e.toString());
		}			
	
		return result;
	}
	
}
