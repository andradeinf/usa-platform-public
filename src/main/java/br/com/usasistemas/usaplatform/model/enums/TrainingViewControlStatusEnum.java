package br.com.usasistemas.usaplatform.model.enums;

public enum TrainingViewControlStatusEnum {
	PARTIAL ("Parcial"),
	COMPLETED ("Completo");
	
	private final String description;
	
	private  TrainingViewControlStatusEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
