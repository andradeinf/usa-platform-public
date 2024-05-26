package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;

public class WSDocumentsFolderResponse extends GenericResponse {

	private DocumentsFolderData folder;

	public DocumentsFolderData getDocumentsFolder() {
		return folder;
	}

	public void setDocumentsFolder(DocumentsFolderData folder) {
		this.folder = folder;
	}		
}
