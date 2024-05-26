package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.ProductSizeData;

public class WSProductServiceProductSizeResponse extends GenericResponse {

	ProductSizeData productSize;

	public ProductSizeData getProductSize() {
		return productSize;
	}

	public void setProductSize(ProductSizeData productSize) {
		this.productSize = productSize;
	}

}
