package br.com.usasistemas.usaplatform.model.data;

import java.util.List;

public class DocumentsFolderStructureData {
	
	private Long id;
	private String name;
	private List<DocumentsFolderStructureData> folders;
	private List<DocumentsFileStructureData> files;

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

	public List<DocumentsFolderStructureData> getFolders() {
		return this.folders;
	}

	public void setFolders(List<DocumentsFolderStructureData> folders) {
		this.folders = folders;
	}

	public List<DocumentsFileStructureData> getFiles() {
		return this.files;
	}

	public void setFiles(List<DocumentsFileStructureData> files) {
		this.files = files;
	}

}
