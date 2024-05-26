package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;

public class WSDomainConfigurationResponse extends GenericResponse {

	private DomainConfigurationData domainConfiguration;

	public DomainConfigurationData getDomainConfiguration() {
		return domainConfiguration;
	}

	public void setDomainConfiguration(DomainConfigurationData domainConfiguration) {
		this.domainConfiguration = domainConfiguration;
	}
	
}
