package br.com.usasistemas.usaplatform.model.enums;

public enum ManufactureRequestStatusEnum {
	PENDING ("Pendente", false),
	IN_PRODUCTION ("Em Produção", false),
	COMPLETED ("Produzido", false),
	CANCELLED ("Cancelado", false),
	ADJUSTMENT ("Ajuste", true);
	
	
	private final String description;
	private final Boolean internalUse;
	
	private  ManufactureRequestStatusEnum(String description, Boolean internalUse) {
        this.description = description;
        this.internalUse = internalUse;
    }
	
	public String getDescription(){
		return this.description;
	}

	public Boolean getInternalUse() {
		return internalUse;
	}

}
