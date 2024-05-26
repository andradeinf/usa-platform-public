package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.entity.City;

public interface CityDAO extends GenericDAO<City, CityData> {

	public List<CityData> findByStateId(Long id);
	
}
