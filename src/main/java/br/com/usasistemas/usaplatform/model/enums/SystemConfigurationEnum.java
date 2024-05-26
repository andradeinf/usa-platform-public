package br.com.usasistemas.usaplatform.model.enums;

public enum SystemConfigurationEnum {
	SYSTEM_UPDATE_INFO_SUMMARY ("SYSTEM_UPDATE_INFO_SUMMARY"),
	SYSTEM_UPDATE_INFO_DETAILS ("SYSTEM_UPDATE_INFO_DETAILS"),
	STOCK_CONSOLIDATION_WINDOW ("STOCK_CONSOLIDATION_WINDOW");
	
	private final String description;
	
	private  SystemConfigurationEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
