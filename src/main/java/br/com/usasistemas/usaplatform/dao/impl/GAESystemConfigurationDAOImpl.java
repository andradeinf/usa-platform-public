package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.SystemConfigurationDAO;
import br.com.usasistemas.usaplatform.model.data.SystemConfigurationData;
import br.com.usasistemas.usaplatform.model.entity.SystemConfiguration;

public class GAESystemConfigurationDAOImpl extends GAEGenericDAOImpl<SystemConfiguration, SystemConfigurationData> implements SystemConfigurationDAO {
	
	private static final Logger log = Logger.getLogger(GAESystemConfigurationDAOImpl.class.getName());

	@Override
	public SystemConfigurationData findByKey(String key) {
		SystemConfigurationData result = null;

		try {
			List<SystemConfiguration> keys = ofy().load().type(SystemConfiguration.class).filter("key", key).list();
			if (keys != null && !keys.isEmpty())
				result = this.getConverter().convertToData(keys.get(0));
		} catch (Exception e) {
			log.warning("Error when querying for SystemConfiguration by key: " + e.toString());
		}			
	
		return result;
	}

}
