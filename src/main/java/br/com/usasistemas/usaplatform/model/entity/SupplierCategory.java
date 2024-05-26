package br.com.usasistemas.usaplatform.model.entity;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class SupplierCategory {
	
	@Id private Long id;
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
