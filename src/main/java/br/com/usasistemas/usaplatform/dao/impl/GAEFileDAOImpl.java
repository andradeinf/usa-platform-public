package br.com.usasistemas.usaplatform.dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.api.blobstore.UploadOptions;
import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import br.com.usasistemas.usaplatform.dao.FileDAO;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;

public class GAEFileDAOImpl implements FileDAO {
	
	private static final Logger log = Logger.getLogger(GAEFileDAOImpl.class.getName());
	private static final Long GCS_MAX_FILE_SIZE_BYTES = 52428800L; //50 MB
	
	private String storageBucketName = null;
	
	public GAEFileDAOImpl() {
		storageBucketName = SystemProperty.applicationId.get()+".appspot.com";
	}
	
	private String createFile(String fileName, byte[] content) {
		
		String fileKey = null;
		
		try {
			GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
			      .initialRetryDelayMillis(1000)
			      .retryMaxAttempts(13)
			      .totalRetryPeriodMillis(15000)
			      .build());
				
			GcsOutputChannel outputChannel =
			        gcsService.createOrReplace(new GcsFilename(storageBucketName, fileName), GcsFileOptions.getDefaultInstance());
			
			outputChannel.write(ByteBuffer.wrap(content));
			outputChannel.close();
			
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			BlobKey blobKey = blobstoreService.createGsBlobKey("/gs/" + storageBucketName + "/" + fileName);
			fileKey = blobKey.getKeyString();
			
		} catch (IOException e) {
			log.severe("Error writing content to file.");
		}
		
		return fileKey;
	}
	
	@Override
	public String createFile(String fileName, StringBuilder fileContent) {
		return createFile(fileName, fileContent.toString().getBytes());
	}
	
	@Override
	public String createExcelFile(String fileName, ByteArrayOutputStream workbook) {
		return createFile(fileName, workbook.toByteArray());
	}
	
	@Override
	public void readFile(String fileKey, String filename, Boolean download, HttpServletResponse response){		
		try {
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			if (download) {
				response.setHeader("Content-Disposition", "attachment;filename="+filename);
			}			
			blobstoreService.serve(new BlobKey(fileKey), response);
		} catch (IOException e) {
			log.severe("Error reading content to file.");
		}
	}
	
	@Override
	public String getFileUploadUrl(String redirectUrl, Boolean withRestriction, String bucketPath) {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		UploadOptions uploadOptions = UploadOptions.Builder.withGoogleStorageBucketName(storageBucketName + bucketPath);
		if (withRestriction){
			uploadOptions.maxUploadSizeBytes(GCS_MAX_FILE_SIZE_BYTES);
		}
		String uploadUrl = blobstoreService.createUploadUrl(redirectUrl, uploadOptions);
		
		if (uploadUrl.startsWith("http://andrade:8080")) {
			uploadUrl = uploadUrl.replace("http://andrade:8080", "");
		}
		
		return uploadUrl;
	}
	
	@Override
	public UploadedFileData getUploadedFileInfo(HttpServletRequest request, String fileFieldName) throws Exception {
		
		UploadedFileData response = new UploadedFileData();
		
		try {
		
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			Map<String, List<FileInfo>> files = blobstoreService.getFileInfos(request);
			if (files != null && !files.isEmpty()) {
				List<FileInfo> fileList = files.get(fileFieldName);
				if (fileList != null && !fileList.isEmpty()) {
					
					FileInfo fileInfo = fileList.get(0);
					BlobKey blobKey = blobstoreService.createGsBlobKey(fileInfo.getGsObjectName());
					
					String filename = null;
				    try {
				    	filename = MimeUtility.decodeText(fileInfo.getFilename());
					} catch (UnsupportedEncodingException e) {
						filename = fileInfo.getFilename();
					}
					
					response.setContentType(fileInfo.getContentType());
					response.setDate(fileInfo.getCreation());
					response.setName(filename);
					response.setSize(fileInfo.getSize());
					response.setStorePath(fileInfo.getGsObjectName());
					response.setFileKey(blobKey.getKeyString());
				} else {
					throw new Exception("file list is null or empty!");
				}
			} else {
				throw new Exception("files map is null or empty!");
			}
			
		} catch (Exception e) {
			
			log.log(Level.SEVERE, "Error uploading file.", e);
			
			throw e;
		}
		
		return response;
	}

	@Override
	public void deleteFile(String fileKey) {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		blobstoreService.delete(new BlobKey(fileKey));
	}

}
