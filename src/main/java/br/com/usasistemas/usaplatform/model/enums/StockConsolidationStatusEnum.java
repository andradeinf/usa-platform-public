package br.com.usasistemas.usaplatform.model.enums;

public enum StockConsolidationStatusEnum {
	STARTED ("Iniciado"),
	COMPLETED ("Completo"),
	ERROR ("Erro");
	
	private final String description;
	
	private  StockConsolidationStatusEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
