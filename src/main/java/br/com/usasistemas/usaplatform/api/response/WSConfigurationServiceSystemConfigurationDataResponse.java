package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.SystemConfigurationData;

public class WSConfigurationServiceSystemConfigurationDataResponse extends GenericResponse {

	private SystemConfigurationData systemConfiguration;

	public SystemConfigurationData getSystemConfiguration() {
		return systemConfiguration;
	}

	public void setSystemConfiguration(SystemConfigurationData systemConfiguration) {
		this.systemConfiguration = systemConfiguration;
	}

}
