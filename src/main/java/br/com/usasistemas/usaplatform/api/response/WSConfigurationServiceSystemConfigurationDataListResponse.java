package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.SystemConfigurationData;

public class WSConfigurationServiceSystemConfigurationDataListResponse extends GenericResponse {

	private List<SystemConfigurationData> systemConfigurationList;

	public List<SystemConfigurationData> getSystemConfigurationList() {
		return systemConfigurationList;
	}

	public void setSystemConfigurationList(List<SystemConfigurationData> systemConfigurationList) {
		this.systemConfigurationList = systemConfigurationList;
	}

}
