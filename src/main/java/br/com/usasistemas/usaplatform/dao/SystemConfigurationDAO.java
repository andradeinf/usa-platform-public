package br.com.usasistemas.usaplatform.dao;

import br.com.usasistemas.usaplatform.model.data.SystemConfigurationData;
import br.com.usasistemas.usaplatform.model.entity.SystemConfiguration;

public interface SystemConfigurationDAO extends GenericDAO<SystemConfiguration, SystemConfigurationData> {

	public SystemConfigurationData findByKey(String key);
	
}
