package br.com.usasistemas.usaplatform.model.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SupplierFranchisorData implements Serializable {
	
	private Long id;
	private Long supplierId;
	private Long franchisorId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getFranchisorId() {
		return franchisorId;
	}

	public void setFranchisorId(Long franchisorId) {
		this.franchisorId = franchisorId;
	}
}
