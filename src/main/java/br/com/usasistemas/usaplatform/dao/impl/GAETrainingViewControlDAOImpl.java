package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.TrainingViewControlDAO;
import br.com.usasistemas.usaplatform.model.data.TrainingViewControlData;
import br.com.usasistemas.usaplatform.model.entity.TrainingViewControl;

public class GAETrainingViewControlDAOImpl extends GAEGenericDAOImpl<TrainingViewControl, TrainingViewControlData> implements TrainingViewControlDAO {
	
	private static final Logger log = Logger.getLogger(GAETrainingViewControlDAOImpl.class.getName());

	@Override
	public List<TrainingViewControlData> findByTrainingIdAndFranchiseeId(Long trainingId, Long franchiseeId) {
		List<TrainingViewControlData> result = new ArrayList<TrainingViewControlData>();

		try {
			List<TrainingViewControl> trainingViewControls = ofy().load().type(TrainingViewControl.class)
				.filter("trainingId", trainingId)
				.filter("franchiseeId", franchiseeId)
				.list();
			if (trainingViewControls != null && !trainingViewControls.isEmpty())
				result = this.getConverter().convertToDataList(trainingViewControls);
		} catch (Exception e) {
			log.warning("Error when querying for TrainingViewControls by trainingId and franchiseeId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public TrainingViewControlData findByTrainingIdAndFranchiseeUserId(Long trainingId, Long franchiseeUserId) {
		
		TrainingViewControlData result = null;

		try {
			List<TrainingViewControl> trainingViewControls = ofy().load().type(TrainingViewControl.class)
				.filter("trainingId", trainingId)
				.filter("franchiseeUserId", franchiseeUserId)
				.list();
			if (trainingViewControls != null && !trainingViewControls.isEmpty())
				result = this.getConverter().convertToData(trainingViewControls.get(0));
		} catch (Exception e) {
			log.warning("Error when querying for User by email: " + e.toString());
		}	
		
		return result;
	}

	@Override
	public List<TrainingViewControlData> findByTrainingId(Long trainingId) {

		List<TrainingViewControlData> result = new ArrayList<TrainingViewControlData>();

		try {
			List<TrainingViewControl> trainingViewControls = ofy().load().type(TrainingViewControl.class)
				.filter("trainingId", trainingId)
				.list();
			if (trainingViewControls != null && !trainingViewControls.isEmpty())
				result = this.getConverter().convertToDataList(trainingViewControls);
		} catch (Exception e) {
			log.warning("Error when querying for TrainingViewControls by trainingId and franchiseeId: " + e.toString());
		}			
	
		return result;
	}
	
}
