package br.com.usasistemas.usaplatform.model.data;

import java.util.List;

public class SupplierCategoryData {
	
	private Long id;
	private String name;
	private Long order;
	private List<String> domainKeys;
	private Boolean hasStockControl;
	private Boolean visibleToFranchisees;
	private Boolean receiveMessage;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public List<String> getDomainKeys() {
		return domainKeys;
	}

	public void setDomainKeys(List<String> domainKeys) {
		this.domainKeys = domainKeys;
	}

	public Boolean getHasStockControl() {
		return hasStockControl;
	}

	public void setHasStockControl(Boolean hasStockControl) {
		this.hasStockControl = hasStockControl;
	}

	public Boolean getVisibleToFranchisees() {
		return visibleToFranchisees;
	}

	public void setVisibleToFranchisees(Boolean visibleToFranchisees) {
		this.visibleToFranchisees = visibleToFranchisees;
	}

	public Boolean getReceiveMessage() {
		return receiveMessage;
	}

	public void setReceiveMessage(Boolean receiveMessage) {
		this.receiveMessage = receiveMessage;
	}
	
}
