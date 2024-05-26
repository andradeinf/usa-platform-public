package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.SupplierCategoryData;

public class WSSupplierCategoryResponse extends GenericResponse {

	private SupplierCategoryData supplierCategory;

	public SupplierCategoryData getSupplierCategory() {
		return supplierCategory;
	}

	public void setSupplierCategory(SupplierCategoryData supplierCategory) {
		this.supplierCategory = supplierCategory;
	}
		
}
