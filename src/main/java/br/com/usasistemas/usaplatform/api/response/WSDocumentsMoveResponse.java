package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;

public class WSDocumentsMoveResponse extends GenericResponse {

	private DocumentsFolderData folder;
	private DocumentsFileData file;

	public DocumentsFolderData getFolder() {
		return this.folder;
	}
	public void setFolder(DocumentsFolderData folder) {
		this.folder = folder;
	}

	public DocumentsFileData getFile() {
		return file;
	}

	public void setFile(DocumentsFileData file) {
		this.file = file;
	}
		
}
