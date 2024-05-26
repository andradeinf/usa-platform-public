package br.com.usasistemas.usaplatform.model.enums;

public enum SystemConfigurationTypeEnum {
	STRING ("Texto"),
	HTML_STRING ("Texto Formatado"),
	LONG ("Número"),
	BOOLEAN ("Flag");
	
	private final String description;
	
	private  SystemConfigurationTypeEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
