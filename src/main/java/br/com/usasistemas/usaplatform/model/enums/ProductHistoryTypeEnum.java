package br.com.usasistemas.usaplatform.model.enums;

public enum ProductHistoryTypeEnum {
	MANUFACTURE ("Produção"),
	DELIVERY ("Entrega");
	
	private final String description;
	
	private  ProductHistoryTypeEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}
	
	public static ProductHistoryTypeEnum getValue(String description)
    {
        for (ProductHistoryTypeEnum p : ProductHistoryTypeEnum.values())
        {
            if (p.getDescription().equalsIgnoreCase(description))
                return p;
        }
        throw new IllegalArgumentException("Invalid Product History Type: " + description);
    }

}
