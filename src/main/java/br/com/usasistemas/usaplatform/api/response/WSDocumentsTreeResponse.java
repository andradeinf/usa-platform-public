package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.DocumentsFolderStructureData;

public class WSDocumentsTreeResponse extends GenericResponse {

	private DocumentsFolderStructureData documentTree;

	public DocumentsFolderStructureData getDocumentTree() {
		return this.documentTree;
	}

	public void setDocumentTree(DocumentsFolderStructureData documentTree) {
		this.documentTree = documentTree;
	}
		
}
