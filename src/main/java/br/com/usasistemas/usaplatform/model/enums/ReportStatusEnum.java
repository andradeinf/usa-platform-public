package br.com.usasistemas.usaplatform.model.enums;

public enum ReportStatusEnum {
	WAITING ("Aguardando"),
	PROCESSING ("Processando"),
	COMPLETED ("Completo"),
	ERROR ("Erro");
	
	private final String description;
	
	private  ReportStatusEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
