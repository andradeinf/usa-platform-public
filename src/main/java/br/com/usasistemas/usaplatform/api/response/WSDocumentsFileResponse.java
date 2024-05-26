package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;

public class WSDocumentsFileResponse extends GenericResponse {

	private DocumentsFileData file;

	public DocumentsFileData getDocumentsFile() {
		return file;
	}

	public void setDocumentsFile(DocumentsFileData file) {
		this.file = file;
	}		
}
