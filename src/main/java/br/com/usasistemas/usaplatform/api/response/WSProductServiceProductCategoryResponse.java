package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.ProductCategoryData;

public class WSProductServiceProductCategoryResponse extends GenericResponse {

	ProductCategoryData productCategory;

	public ProductCategoryData getProductCategor() {
		return productCategory;
	}

	public void setProductCategor(ProductCategoryData productCategory) {
		this.productCategory = productCategory;
	}

}
