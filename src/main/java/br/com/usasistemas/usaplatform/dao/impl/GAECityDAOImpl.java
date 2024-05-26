package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.CityDAO;
import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.entity.City;

public class GAECityDAOImpl extends GAEGenericDAOImpl<City, CityData> implements CityDAO {
	
	private static final Logger log = Logger.getLogger(GAECityDAOImpl.class.getName());

	@Override
	public List<CityData> findByStateId(Long id) {
		List<CityData> result = new ArrayList<CityData>();

		try {
			List<City> cities = ofy().load().type(City.class)
				.filter("stateId", id)
				.list();
			if (cities != null && !cities.isEmpty())
				result = this.getConverter().convertToDataList(cities);
		} catch (Exception e) {
			log.warning("Error when querying for Cities by state: " + e.toString());
		}			
	
		return result;
	}
	
}
