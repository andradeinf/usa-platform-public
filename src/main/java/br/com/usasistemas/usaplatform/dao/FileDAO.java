package br.com.usasistemas.usaplatform.dao;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.usasistemas.usaplatform.model.data.UploadedFileData;

public interface FileDAO {

	public String createFile(String fileName, StringBuilder fileContent);
	public String createExcelFile(String fileName, ByteArrayOutputStream workbook);
	public String getFileUploadUrl(String redirectUrl, Boolean withRestriction, String bucketPath);
	public UploadedFileData getUploadedFileInfo(HttpServletRequest request, String fileFieldName) throws Exception;
	public void deleteFile(String fileKey);
	public void readFile(String fileKey, String filename, Boolean download, HttpServletResponse response);	
	
}
