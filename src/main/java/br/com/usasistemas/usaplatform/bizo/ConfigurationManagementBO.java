package br.com.usasistemas.usaplatform.bizo;

import java.util.List;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.SystemConfigurationData;
import br.com.usasistemas.usaplatform.model.enums.DomainKeysEnum;

public interface ConfigurationManagementBO {
	
	public static final String DEFAULT_KEY = DomainKeysEnum.FRANQUIAS.name();

	public List<EnumValidValueResponseData> getTypes();
	public List<SystemConfigurationData> getAllConfigurations();
	public SystemConfigurationData saveConfiguration(SystemConfigurationData systemConfiguration);
	public List<SystemConfigurationData> getConfigurationsByKey(List<String> keys);
	public SystemConfigurationData deleteConfiguration(Long id);
	public SystemConfigurationData getConfigurationByKey(String key);
	public DomainConfigurationData getDomainConfigurationByURL(String serverName);
	public DomainConfigurationData getDomainConfigurationByKey(String key);
	public List<EnumValidValueResponseData> getDomains();
}
