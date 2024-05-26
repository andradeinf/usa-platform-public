package br.com.usasistemas.usaplatform.model.enums;

public enum DeliveryRequestStatusEnum {
	PENDING ("Pendente"),
	PENDING_WITH_RESTRICTION ("Pendente por Restrição"),
	WAITING_PAYMENT ("Aguardando Pagamento"),
	PREPARING_DELIVERY ("Preparando Envio"),
	COMPLETED ("Enviado"),
	CANCELLED ("Cancelado");	
	
	private final String description;
	
	private  DeliveryRequestStatusEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
