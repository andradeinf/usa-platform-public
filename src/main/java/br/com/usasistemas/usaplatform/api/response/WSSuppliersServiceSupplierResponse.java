package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.SupplierData;

public class WSSuppliersServiceSupplierResponse extends GenericResponse {

	SupplierData supplier;

	public SupplierData getSupplier() {
		return supplier;
	}

	public void setSupplier(SupplierData supplier) {
		this.supplier = supplier;
	}

}
