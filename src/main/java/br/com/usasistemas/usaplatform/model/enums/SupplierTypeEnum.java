package br.com.usasistemas.usaplatform.model.enums;

public enum SupplierTypeEnum {
	RECEIVE_MANUFACTURE_REQUEST ("Recebe pedidos de produção"),
	MANUFACTURE_WITHOUT_REQUEST ("Produz sem pedido de produção");
	
	
	private final String description;
	
	private  SupplierTypeEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}

}
