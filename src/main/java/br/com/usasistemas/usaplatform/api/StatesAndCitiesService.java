package br.com.usasistemas.usaplatform.api;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.bizo.StateAndCityManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.StateData;

@Controller
@RequestMapping(value = UrlMapping.STATES_AND_CITIES_RESOURCE)
public class StatesAndCitiesService {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(StatesAndCitiesService.class.getName());
	private StateAndCityManagementBO stateAndCityManagement;
	
	public StateAndCityManagementBO getStateAndCityManagement() {
		return stateAndCityManagement;
	}

	public void setStateAndCityManagement(
			StateAndCityManagementBO stateAndCityManagement) {
		this.stateAndCityManagement = stateAndCityManagement;
	}

	/*
	 * Get States list without any filter
	 */
	@RequestMapping(value = "/states", method=RequestMethod.GET)
	@ResponseBody
	public List<StateData> listStates() {
		return stateAndCityManagement.getAllStates();		
	}
	
	/*
	 * Get State Cities list filtered by State ID
	 */
	@RequestMapping(value = "/states/{id}/cities", method=RequestMethod.GET)
	@ResponseBody
	public List<CityData> getStateCities(@PathVariable Long id) {
		return stateAndCityManagement.getStateCities(id);		
	}

}
