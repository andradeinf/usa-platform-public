package br.com.usasistemas.usaplatform.bizo;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.StateData;

public interface StateAndCityManagementBO {

	public List<StateData> getAllStates();
	public List<CityData> getStateCities(Long stateId);
	public StateData getState(Long stateId);
	public CityData getCity(Long cityId);

}
