package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.UploadedFileData;

public class WSUploadedFileResponse extends GenericResponse {

	private UploadedFileData uploadedFile;

	public UploadedFileData getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFileData uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
		
}
