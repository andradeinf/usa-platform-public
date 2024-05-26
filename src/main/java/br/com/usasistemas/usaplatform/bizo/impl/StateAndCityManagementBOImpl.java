package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.bizo.StateAndCityManagementBO;
import br.com.usasistemas.usaplatform.dao.CityDAO;
import br.com.usasistemas.usaplatform.dao.StateDAO;
import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.StateData;

public class StateAndCityManagementBOImpl implements StateAndCityManagementBO {

	@SuppressWarnings("unused")
	private static final Logger log = Logger
			.getLogger(StateAndCityManagementBOImpl.class.getName());
	private StateDAO stateDAO;
	private CityDAO cityDAO;

	public StateDAO getStateDAO() {
		return stateDAO;
	}

	public void setStateDAO(StateDAO stateDAO) {
		this.stateDAO = stateDAO;
	}
	
	public CityDAO getCityDAO() {
		return cityDAO;
	}

	public void setCityDAO(CityDAO cityDAO) {
		this.cityDAO = cityDAO;
	}

	@Override
	public List<StateData> getAllStates() {
		return stateDAO.listAll();
	}

	@Override
	public List<CityData> getStateCities(Long stateId) {
		return cityDAO.findByStateId(stateId);
	}

	@Override
	public StateData getState(Long stateId) {
		return stateDAO.findById(stateId);
	}

	@Override
	public CityData getCity(Long cityId) {
		return cityDAO.findById(cityId);
	}

}
