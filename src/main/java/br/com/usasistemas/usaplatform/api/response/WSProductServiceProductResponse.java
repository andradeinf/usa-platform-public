package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.ProductData;

public class WSProductServiceProductResponse extends GenericResponse {

	ProductData product;

	public ProductData getProduct() {
		return product;
	}

	public void setProduct(ProductData product) {
		this.product = product;
	}

}
