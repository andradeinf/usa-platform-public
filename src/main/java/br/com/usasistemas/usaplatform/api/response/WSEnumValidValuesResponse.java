package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;

public class WSEnumValidValuesResponse extends GenericResponse {

	List<EnumValidValueResponseData> enumValues;

	public List<EnumValidValueResponseData> getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(List<EnumValidValueResponseData> enumValues) {
		this.enumValues = enumValues;
	}

}
