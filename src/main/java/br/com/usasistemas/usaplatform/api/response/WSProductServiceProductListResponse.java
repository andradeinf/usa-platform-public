package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;

public class WSProductServiceProductListResponse extends GenericResponse {

	private List<ProductData> productList;
	private Map<Long, SupplierData> supplierList;

	public List<ProductData> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductData> productList) {
		this.productList = productList;
	}

	public Map<Long, SupplierData> getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(Map<Long, SupplierData> supplierList) {
		this.supplierList = supplierList;
	}

}
