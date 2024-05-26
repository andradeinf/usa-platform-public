package br.com.usasistemas.usaplatform.model.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

@Entity
@Index
public class DocumentsFranchiseeIndex {
	
	@Id private Long id;
	private Date updatedAt;
	private Long franchisorId;
	@Unindex String indexJson;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}
	
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getFranchisorId() {
		return franchisorId;
	}

	public void setFranchisorId(Long franchisorId) {
		this.franchisorId = franchisorId;
	}

	public String getIndexJson() {
		return this.indexJson;
	}
	
	public void setIndexJson(String indexJson) {
		this.indexJson = indexJson;
	}
	
}
