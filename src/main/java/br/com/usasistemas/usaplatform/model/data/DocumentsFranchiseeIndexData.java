package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;

public class DocumentsFranchiseeIndexData {
	
	private Long id;
	private Date updatedAt;
	private Long franchisorId;
	private String indexJson;

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
