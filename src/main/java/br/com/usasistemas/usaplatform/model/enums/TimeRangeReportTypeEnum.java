package br.com.usasistemas.usaplatform.model.enums;

public enum TimeRangeReportTypeEnum {
	DELIVERY_REQUEST ("deliveryRequest"),
	MESSAGE ("message");
	
	private final String folderName;
	
	private  TimeRangeReportTypeEnum(String folderName) {
        this.folderName = folderName;
    }
	
	public String getFolderName(){
		return this.folderName;
	}

}
