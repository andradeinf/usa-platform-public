package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.TrainingViewControlData;

public class WSTrainingViewControlListResponse extends GenericResponse {

	private List<TrainingViewControlData> trainingViewControls;
	private Map<Long, PublicUserData> users;
	
	public List<TrainingViewControlData> getTrainingViewControls() {
		return trainingViewControls;
	}
	
	public void setTrainingViewControls(List<TrainingViewControlData> trainingViewControls) {
		this.trainingViewControls = trainingViewControls;
	}
	
	public Map<Long, PublicUserData> getUsers() {
		return users;
	}
	
	public void setUsers(Map<Long, PublicUserData> users) {
		this.users = users;
	}

}
