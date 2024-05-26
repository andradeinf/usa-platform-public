package br.com.usasistemas.usaplatform.model.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SupplierUserData implements Serializable {
	
	private Long id;
	private Long userId;
	private Long supplierId;
	private PublicUserData user = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	
	public PublicUserData getUser() {
		return user;
	}

	public void setUser(PublicUserData user) {
		this.user = user;
	}

}
