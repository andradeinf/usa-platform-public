package br.com.usasistemas.usaplatform.model.enums;

public enum UserProfileEnum {
	ADMINISTRATOR ("Administrador do Sistema"),
	FRANCHISEE ("Franqueado"),
	FRANCHISOR ("Franqueador"),
	SUPPLIER ("Fornecedor");	
	
	private final String description;
	
	private  UserProfileEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
