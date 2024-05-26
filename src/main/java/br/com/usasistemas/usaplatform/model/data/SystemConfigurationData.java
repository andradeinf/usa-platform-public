package br.com.usasistemas.usaplatform.model.data;

import br.com.usasistemas.usaplatform.model.enums.SystemConfigurationTypeEnum;

public class SystemConfigurationData {
	
	private Long id;
	private String key;
	private String name;
	private SystemConfigurationTypeEnum type;
	private String stringValue;
	private Long longValue;
	private Boolean booleanValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SystemConfigurationTypeEnum getType() {
		return type;
	}

	public void setType(SystemConfigurationTypeEnum type) {
		this.type = type;
	}
	
	public String getTypeDescription() {
		if (type != null) {
			return type.getDescription();
		} else {
			return "";
		}		
	}
	
	public void setTypeDescription(String description) {
		//DO NOTHING
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

}
