package br.com.usasistemas.usaplatform.model.entity;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class ProductCategory {
	
	@Id private Long id;
	private Long franchisorId;
	private String name;
	private String notes;
	private Long order;
	private List<String> restrictions;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFranchisorId() {
		return franchisorId;
	}
	
	public void setFranchisorId(Long franchisorId) {
		this.franchisorId = franchisorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public List<String> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(List<String> restrictions) {
		this.restrictions = restrictions;
	}

}
