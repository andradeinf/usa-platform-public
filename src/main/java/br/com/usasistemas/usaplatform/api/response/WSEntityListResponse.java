package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.EntityData;

public class WSEntityListResponse extends GenericResponse {

	private List<EntityData> entities;

	public List<EntityData> getEntities() {
		return entities;
	}

	public void setEntities(List<EntityData> entities) {
		this.entities = entities;
	}	

}
