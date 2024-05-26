package br.com.usasistemas.usaplatform.model.enums;

public enum ReviewRequesStatusEnum {
	REQUESTED ("Solicitado"),
	REJECTED ("Rejeitado"),
	COMPLETED ("Completo"),
	CONSOLIDATED ("Consolidado");
	
	private final String description;
	
	private  ReviewRequesStatusEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
