package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.SupplierCategoryData;

public class WSSupplierCategoryListResponse extends GenericResponse {

	private List<SupplierCategoryData> supplierCategories;

	public List<SupplierCategoryData> getSupplierCategories() {
		return supplierCategories;
	}

	public void setSupplierCategories(List<SupplierCategoryData> supplierCategories) {
		this.supplierCategories = supplierCategories;
	}
	
}
