package br.com.usasistemas.usaplatform.bizo;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.usasistemas.usaplatform.model.data.UploadedFileData;

public interface FileManagementBO {
	
	public String createFile(String fileName, StringBuilder fileContent);
	public String createExcelFile(String fileName, ByteArrayOutputStream workbook);	
	public void readFile(String fileKey, String filename, Boolean download, HttpServletResponse response);
	public String getDocumentsFileUploadUrl(String redirectUrl, Boolean noRestriction);
	public String getCatalogUploadUrl(String redirectUrl);
	public String getAnnouncementImageUploadUrl(String redirectUrl);
	public UploadedFileData getUploadedFileInfo(HttpServletRequest request, String fileFieldName) throws Exception;
	public void deleteFile(String fileKey);
	public String getMessageAttachmentUploadUrl(String redirectUrl);
	public String getPaymentSlipUploadUrl(String redirectUrl);
	public String getFiscalFileUploadUrl(String redirectUrl);
	public String getTrainingVideoUploadUrl(String redirectUrl);
	public UploadedFileData createUploadedFile(UploadedFileData file);
	public UploadedFileData getUploadedFile(Long id);
	public UploadedFileData deleteUploadedFile(Long id);
	void cleanUpUnusedUploadedFiles(Long threshold);

}
