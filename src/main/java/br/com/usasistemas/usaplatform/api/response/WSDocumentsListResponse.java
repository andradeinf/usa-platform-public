package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;

public class WSDocumentsListResponse extends GenericResponse {

	private List<DocumentsFolderData> folders;
	private List<DocumentsFileData> files;
	
	public List<DocumentsFolderData> getFolders() {
		return folders;
	}
	
	public void setFolders(List<DocumentsFolderData> folders) {
		this.folders = folders;
	}
	
	public List<DocumentsFileData> getFiles() {
		return files;
	}
	
	public void setFiles(List<DocumentsFileData> files) {
		this.files = files;
	}		
}
