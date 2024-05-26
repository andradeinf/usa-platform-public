package br.com.usasistemas.usaplatform.model.enums;

public enum MailNotificationStatusEnum {
	PENDING ("Pendente"),
	PENDING_CANCELLATION ("Pendente Cancelamento"),
	SENT ("Enviado"),
	CANCELLED ("Cancelado"),
	NOT_APPLICABLE ("N/A");
	
	
	private final String description;
	
	private  MailNotificationStatusEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
