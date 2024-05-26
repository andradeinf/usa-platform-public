package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;

public class WSDocumentsFileListResponse extends GenericResponse {

	private List<DocumentsFileData> files;
	
	public List<DocumentsFileData> getFiles() {
		return files;
	}
	
	public void setFiles(List<DocumentsFileData> files) {
		this.files = files;
	}		
}
