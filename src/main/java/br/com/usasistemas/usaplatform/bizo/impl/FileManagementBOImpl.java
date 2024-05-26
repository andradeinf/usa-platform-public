package br.com.usasistemas.usaplatform.bizo.impl;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.dao.FileDAO;
import br.com.usasistemas.usaplatform.dao.UploadedFileDAO;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;

public class FileManagementBOImpl implements FileManagementBO {
	
	private static final Logger log = Logger.getLogger(FileManagementBOImpl.class.getName());
	private FileDAO fileDAO;
	private UploadedFileDAO uploadedFileDAO;

	public FileDAO getFileDAO() {
		return fileDAO;
	}

	public void setFileDAO(FileDAO fileDAO) {
		this.fileDAO = fileDAO;
	}

	public UploadedFileDAO getUploadedFileDAO() {
		return uploadedFileDAO;
	}

	public void setUploadedFileDAO(UploadedFileDAO uploadedFileDAO) {
		this.uploadedFileDAO = uploadedFileDAO;
	}

	@Override
	public String createFile(String fileName, StringBuilder fileContent) {
		return fileDAO.createFile(fileName, fileContent);
	}
	
	@Override
	public String createExcelFile(String fileName, ByteArrayOutputStream workbook) {
		return fileDAO.createExcelFile(fileName, workbook);
	}

	@Override
	public void readFile(String fileKey, String filename, Boolean download, HttpServletResponse response) {
		fileDAO.readFile(fileKey, filename, download, response);
	}

	@Override
	public String getDocumentsFileUploadUrl(String redirectUrl, Boolean noRestriction) {
		return fileDAO.getFileUploadUrl(redirectUrl, !noRestriction, "/documents");
	}
	
	@Override
	public String getCatalogUploadUrl(String redirectUrl) {
		return fileDAO.getFileUploadUrl(redirectUrl, false, "/catalogs");
	}
	
	@Override
	public String getAnnouncementImageUploadUrl(String redirectUrl) {
		return fileDAO.getFileUploadUrl(redirectUrl, true, "/announcements");
	}

	@Override
	public UploadedFileData getUploadedFileInfo(HttpServletRequest request, String fileFieldName) throws Exception {
		return fileDAO.getUploadedFileInfo(request, fileFieldName);
	}

	@Override
	public void deleteFile(String fileKey) {
		fileDAO.deleteFile(fileKey);
	}

	@Override
	public String getMessageAttachmentUploadUrl(String redirectUrl) {
		return fileDAO.getFileUploadUrl(redirectUrl, true, "/messageAttachments");
	}

	@Override
	public String getPaymentSlipUploadUrl(String redirectUrl) {
		return fileDAO.getFileUploadUrl(redirectUrl, true, "/paymentSlips");
	}
	
	@Override
	public String getFiscalFileUploadUrl(String redirectUrl) {
		return fileDAO.getFileUploadUrl(redirectUrl, true, "/fiscalFiles");
	}

	@Override
	public String getTrainingVideoUploadUrl(String redirectUrl) {
		return fileDAO.getFileUploadUrl(redirectUrl, false, "/trainingMaterial");
	}

	@Override
	public UploadedFileData createUploadedFile(UploadedFileData file) {
		return uploadedFileDAO.save(file);
	}

	@Override
	public UploadedFileData getUploadedFile(Long id) {
		return uploadedFileDAO.findById(id);
	}

	@Override
	public UploadedFileData deleteUploadedFile(Long id) {
		return uploadedFileDAO.delete(id);
	}
	
	@Override
	public void cleanUpUnusedUploadedFiles(Long threshold) {
		
		Calendar thresholdDate = Calendar.getInstance();
		thresholdDate.add(Calendar.DAY_OF_MONTH, threshold.intValue() * -1);
		
		List<UploadedFileData> uploadedFiles = uploadedFileDAO.findUnusedByDate(thresholdDate.getTime());
		
		if (uploadedFiles != null && !uploadedFiles.isEmpty()) {
			for (UploadedFileData uploadedFile : uploadedFiles) {
				fileDAO.deleteFile(uploadedFile.getFileKey());
				uploadedFileDAO.delete(uploadedFile.getId());
			}
		}
		
		log.info(uploadedFiles.size() + " unused uploaded files cleaned.");
		
	}
}
