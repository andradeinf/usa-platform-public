package br.com.usasistemas.usaplatform.model.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class UserGroup {
	
	@Id private Long id;
	private Long entityId; //FranchiseeId/FranchisorId/SupplierId
	private String name;
	private Boolean receiveMessage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getReceiveMessage() {
		return receiveMessage;
	}

	public void setReceiveMessage(Boolean receiveMessage) {
		this.receiveMessage = receiveMessage;
	}
	
}
