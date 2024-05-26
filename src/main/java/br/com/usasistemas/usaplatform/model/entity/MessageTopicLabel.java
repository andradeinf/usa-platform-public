package br.com.usasistemas.usaplatform.model.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class MessageTopicLabel {
	
	@Id private Long id;
	private Long entityUserId;
	private String name;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getEntityUserId() {
		return entityUserId;
	}

	public void setEntityUserId(Long entityUserId) {
		this.entityUserId = entityUserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
