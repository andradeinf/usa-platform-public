package br.com.usasistemas.usaplatform.api.request.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MoveFolderOrFileData implements Serializable {
	
	private String type;
	private Long id;
	private Long folderId;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFolderId() {
		return this.folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

}
