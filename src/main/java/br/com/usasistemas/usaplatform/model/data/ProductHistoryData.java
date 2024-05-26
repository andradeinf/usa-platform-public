package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;

import br.com.usasistemas.usaplatform.model.enums.ProductHistoryTypeEnum;

public class ProductHistoryData {
	
	private ProductHistoryTypeEnum type;
	private ManufactureRequestData manufactureRequest;
	private DeliveryRequestData deliveryRequest;
	private ProductData product;
	private FranchiseeData franchisee;
	private FranchisorData franchisor;

	public ProductHistoryTypeEnum getType() {
		return type;
	}
	
	public String getTypeDescription() {
		return type.getDescription();
	}

	public void setType(ProductHistoryTypeEnum type) {
		this.type = type;
	}
	
	public ManufactureRequestData getManufactureRequest() {
		return manufactureRequest;
	}
	
	public void setManufactureRequest(ManufactureRequestData manufactureRequest) {
		this.manufactureRequest = manufactureRequest;
	}

	public DeliveryRequestData getDeliveryRequest() {
		return deliveryRequest;
	}
	
	public void setDeliveryRequest(DeliveryRequestData deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}
	
	public Long getId(){
		return (this.type==ProductHistoryTypeEnum.MANUFACTURE?this.manufactureRequest.getId():this.deliveryRequest.getId());
	}
	
	public Date getDate() {
		return (this.type==ProductHistoryTypeEnum.MANUFACTURE?this.manufactureRequest.getDate():this.deliveryRequest.getDate());
	}
	
	public Double getUnitPrice() {
		return (this.type==ProductHistoryTypeEnum.MANUFACTURE?this.manufactureRequest.getManufactureUnitPrice():this.deliveryRequest.getDeliveryUnitPrice());
	}
	
	public Long getQuantity() {
		return (this.type==ProductHistoryTypeEnum.MANUFACTURE?this.manufactureRequest.getQuantity():this.deliveryRequest.getQuantity());
	}
	
	public String getStatus() {
		return (this.type==ProductHistoryTypeEnum.MANUFACTURE?this.manufactureRequest.getStatus().toString():this.deliveryRequest.getStatus().toString());
	}
	
	public String getStatusDescription() {
		return (this.type==ProductHistoryTypeEnum.MANUFACTURE?this.manufactureRequest.getStatus().getDescription():this.deliveryRequest.getStatus().getDescription());
	}
	
	public String getCancellationComment() {
		return (this.type==ProductHistoryTypeEnum.MANUFACTURE?this.manufactureRequest.getCancellationComment():this.deliveryRequest.getCancellationComment());
	}
	
	public String getRequestor() {
		
		if (this.type==ProductHistoryTypeEnum.DELIVERY && this.franchisee != null)
			return this.franchisee.getName();
		
		if (this.type==ProductHistoryTypeEnum.MANUFACTURE && this.franchisor != null)
			return this.franchisor.getName();
		
		return null; 
	}

	public ProductData getProduct() {
		return product;
	}

	public void setProduct(ProductData product) {
		this.product = product;
	}

	public FranchiseeData getFranchisee() {
		return franchisee;
	}

	public void setFranchisee(FranchiseeData franchisee) {
		this.franchisee = franchisee;
	}

	public FranchisorData getFranchisor() {
		return franchisor;
	}

	public void setFranchisor(FranchisorData franchisor) {
		this.franchisor = franchisor;
	}

}
