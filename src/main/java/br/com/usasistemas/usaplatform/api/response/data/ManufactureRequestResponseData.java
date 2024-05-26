package br.com.usasistemas.usaplatform.api.response.data;

import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.data.ProductData;

public class ManufactureRequestResponseData {
	
	private ManufactureRequestData manufactureRequest;
	private ProductData product;
	private FranchisorData franchisor;
	
	public ManufactureRequestData getManufactureRequest() {
		return manufactureRequest;
	}
	
	public void setManufactureRequest(ManufactureRequestData manufactureRequest) {
		this.manufactureRequest = manufactureRequest;
	}
	
	public ProductData getProduct() {
		return product;
	}

	public void setProduct(ProductData product) {
		this.product = product;
	}
	
	public FranchisorData getFranchisor() {
		return franchisor;
	}

	public void setFranchisor(FranchisorData franchisor) {
		this.franchisor = franchisor;
	}

}
