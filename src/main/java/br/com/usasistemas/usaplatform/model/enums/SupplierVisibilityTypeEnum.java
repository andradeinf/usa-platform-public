package br.com.usasistemas.usaplatform.model.enums;

public enum SupplierVisibilityTypeEnum {
	PUBLIC ("Público"),
	PRIVATE ("Privado");
	
	private final String description;
	
	private  SupplierVisibilityTypeEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
