package br.com.usasistemas.usaplatform.model.enums;

public enum NotificationTypeEnum {
	MESSAGE_TOPIC ("Mensagem"),
	MESSAGE_COMMENT ("Resposta"),
	ANNOUNCEMENT ("Aviso");
	
	private final String description;
	
	private  NotificationTypeEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
