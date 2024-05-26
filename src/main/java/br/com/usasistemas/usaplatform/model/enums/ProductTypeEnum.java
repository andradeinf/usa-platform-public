package br.com.usasistemas.usaplatform.model.enums;

public enum ProductTypeEnum {
	CATALOG ("Catálogo"),
	WITH_STOCK_CONTROL ("Com controle de Estoque"),
	WITHOUT_STOCK_CONTROL ("Sem controle de Estoque"),;
	
	private final String description;
	
	private  ProductTypeEnum(String description) {
        this.description = description;
    }
	
	public String getDescription(){
		return this.description;
	}
	
	public static ProductTypeEnum getValue(String description)
    {
        for (ProductTypeEnum p : ProductTypeEnum.values())
        {
            if (p.getDescription().equalsIgnoreCase(description))
                return p;
        }
        throw new IllegalArgumentException("Invalid Product Type: " + description);
    }

}
