package br.com.usasistemas.usaplatform.bizo.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.Gson;

import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.DocumentManagementBO;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.dao.DocumentsFileDAO;
import br.com.usasistemas.usaplatform.dao.DocumentsFolderDAO;
import br.com.usasistemas.usaplatform.dao.DocumentsFranchiseeIndexDAO;
import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFileStructureData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFolderStructureData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFranchiseeIndexData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

public class DocumentManagementBOImpl implements DocumentManagementBO {
	
	private static final Logger log = Logger.getLogger(DocumentManagementBOImpl.class.getName());
	private DocumentsFileDAO documentsFileDAO;
	private DocumentsFolderDAO documentsFolderDAO;
	private DocumentsFranchiseeIndexDAO documentsFranchiseeIndexDAO;
	private FranchiseeManagementBO franchiseeManagement;
	private FileManagementBO fileManagement;

	public DocumentsFileDAO getDocumentsFileDAO() {
		return documentsFileDAO;
	}

	public void setDocumentsFileDAO(DocumentsFileDAO documentsFileDAO) {
		this.documentsFileDAO = documentsFileDAO;
	}

	public DocumentsFolderDAO getDocumentsFolderDAO() {
		return documentsFolderDAO;
	}

	public void setDocumentsFolderDAO(DocumentsFolderDAO documentsFolderDAO) {
		this.documentsFolderDAO = documentsFolderDAO;
	}

	public DocumentsFranchiseeIndexDAO getDocumentsFfranchiseeIndexDAO() {
		return documentsFranchiseeIndexDAO;
	}

	public void setDocumentsFranchiseeIndexDAO(DocumentsFranchiseeIndexDAO documentsFranchiseeIndexDAO) {
		this.documentsFranchiseeIndexDAO = documentsFranchiseeIndexDAO;
	}

	public FranchiseeManagementBO getFranchiseeManagement() {
		return this.franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public FileManagementBO getFileManagement() {
		return fileManagement;
	}

	public void setFileManagement(FileManagementBO fileManagement) {
		this.fileManagement = fileManagement;
	}

	@Override
	public DocumentsFolderData createDocumentsFolder(DocumentsFolderData folder) {

		if (folder.getParentId() == null) {
			folder.setParentId(0L);
		}

		if (folder.getOrder() == null) {
			folder.setOrder(0L);
		}

		triggerAsyncDocumentsFranchiseeIndexUpdate(folder.getFranchisorId());

		return documentsFolderDAO.save(folder);
	}

	@Override
	public List<DocumentsFolderData> getFranchisorDocumentsFolders(Long franchisorId, Long entityId, List<DocumentsFileData> documentsFiles) {
		List<DocumentsFolderData> folders = documentsFolderDAO.findByFranchisorId(franchisorId);
		if (folders == null) {
			folders = new ArrayList<DocumentsFolderData>();
		}

		//remove empty folders if entity is not the franchisor
		if (!franchisorId.equals(entityId)) {

			//if there is no files, do not return any folder
			if (documentsFiles == null || documentsFiles.isEmpty()) {
				return new ArrayList<DocumentsFolderData>();
			}
				
			folders = folders.stream().filter(
				folder -> documentsFiles.stream().anyMatch(
					file -> file.getFolderId().equals(folder.getId())))
						.collect(Collectors.toList());
		}
		
		return folders;
	}

	@Override
	public List<DocumentsFileData> getFranchisorDocumentsFiles(Long franchisorId, Long entityId) {
		
		List<DocumentsFileData> files = documentsFileDAO.findByFranchisorId(franchisorId);
		
		//remove files that has access restrictions if entity is not the franchisor
		if (!franchisorId.equals(entityId)) {
			if (files != null && !files.isEmpty()) {
				for (int index = files.size() - 1; index >= 0; index--) {
					
					DocumentsFileData file = files.get(index);
					
					//remove files that have restricted access only by the franchisor
					if (file.getAccessRestricted()) {
						files.remove(index);
					} else {
						
						// check if file has franchisse restriction
						List<Long> franchiseeIds = file.getFranchiseeIds();
						if (franchiseeIds != null && !franchiseeIds.isEmpty()) {
							boolean allowed = false;
							
							for (Long franchiseeId: franchiseeIds) {
								if (franchiseeId.equals(entityId)) {
									allowed = true;
								}
							}
							
							if (!allowed) {
								files.remove(index);
							}
						}
						
					}
					
				}
			}
		}
		
		return files;
	}
	
	@Override
	public DocumentsFolderData getDocumentsFolder(Long id) {
		return documentsFolderDAO.findById(id);
	}

	@Override
	public DocumentsFolderData deleteDocumentsFolder(Long id) {

		DocumentsFolderData folder = documentsFolderDAO.findById(id);

		//check documents and do not allow delete a folder that has at least one document or one folder inside
		List<DocumentsFolderData> childFolders = documentsFolderDAO.findByFranchisorIdAndParentId(folder.getFranchisorId(), folder.getId());
		List<DocumentsFileData> childFiles = documentsFileDAO.findByFranchisorIdAndFolderId(folder.getFranchisorId(), folder.getId());
		if ((childFolders != null && !childFolders.isEmpty()) || (childFiles != null && !childFiles.isEmpty())) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage("Não é possivel excluir a pasta pois ela não está vazia. Primeiro remova as pastas e/ou documents internos para depois excluir a pasta.");
			throw new BusinessException(rm);
		}

		triggerAsyncDocumentsFranchiseeIndexUpdate(folder.getFranchisorId());

		return documentsFolderDAO.delete(id);
	}

	@Override
	public List<DocumentsFileData> createDocumentsFiles(List<UploadedFileData> files, Long folderId, Long franchisorId, Boolean accessRestricted, List<Long> franchiseeIds) {
			
		List<DocumentsFileData> response = new ArrayList<DocumentsFileData>();

		if (folderId == null) {
			folderId = 0L;
		}

		for (UploadedFileData file : files) {
			DocumentsFileData documentsFile = new DocumentsFileData();
			documentsFile.setFolderId(folderId);
			documentsFile.setFranchisorId(franchisorId);
			documentsFile.setName(file.getName());
			documentsFile.setDate(file.getDate());
			documentsFile.setContentType(file.getContentType());
			documentsFile.setSize(file.getSize());
			documentsFile.setStorePath(file.getStorePath());
			documentsFile.setFileKey(file.getFileKey());
			documentsFile.setNotificationStatus(MailNotificationStatusEnum.PENDING);
			documentsFile.setAccessRestricted(accessRestricted);
			documentsFile.setFranchiseeIds(franchiseeIds);

			response.add(documentsFileDAO.save(documentsFile));
		}

		triggerAsyncDocumentsFranchiseeIndexUpdate(franchisorId);
		
		return response;
	}
	
	@Override
	public DocumentsFileData updateDocumentsFile(DocumentsFileData file) {
		
		DocumentsFileData updatedFile = documentsFileDAO.findById(file.getId());
		updatedFile.setAccessRestricted(file.getAccessRestricted());
		updatedFile.setFranchiseeIds(file.getFranchiseeIds());

		triggerAsyncDocumentsFranchiseeIndexUpdate(updatedFile.getFranchisorId());

		return documentsFileDAO.save(updatedFile);

	}

	@Override
	public DocumentsFileData deleteDocumentsFile(Long id) {

		DocumentsFileData file = documentsFileDAO.delete(id);
		
		if (file.getFileKey() != null) {
			fileManagement.deleteFile(file.getFileKey());
		}

		triggerAsyncDocumentsFranchiseeIndexUpdate(file.getFranchisorId());
		
		return file;
	}

	@Override
	public DocumentsFileData getDocumentsFile(Long id) {
		return documentsFileDAO.findById(id);
	}

	@Override
	public List<DocumentsFileData> getDocumentsFilePendingNotifications() {
		return documentsFileDAO.findByNotificationStatus(MailNotificationStatusEnum.PENDING);
	}

	@Override
	public List<DocumentsFileData> updateDocumentsFileNotificationStatus(List<DocumentsFileData> documentsFiles, MailNotificationStatusEnum notificationStatus) {
		
		for (DocumentsFileData documentsFile: documentsFiles){
			documentsFile.setNotificationStatus(notificationStatus);
		}

		return  documentsFileDAO.saveAll(documentsFiles);
	}

	private DocumentsFileStructureData mapFile (DocumentsFileData file) {
		
		DocumentsFileStructureData mappedFile = new DocumentsFileStructureData();
		mappedFile.setId(file.getId());
		mappedFile.setName(file.getName());
		mappedFile.setContentType(file.getContentType());
		mappedFile.setDate(file.getDate());
		mappedFile.setSize(file.getSize());

		return mappedFile;
	}

	private DocumentsFolderStructureData mapFolderTree(Long franchiseeId, DocumentsFolderData folder, List<DocumentsFolderData> franchisorFolders, List<DocumentsFileData> franchisorFiles) {

		DocumentsFolderStructureData mappedFolder = new DocumentsFolderStructureData();
		mappedFolder.setId(folder.getId());
		mappedFolder.setName(folder.getName());

		//Add child folders
		mappedFolder.setFolders(
			franchisorFolders.stream()
				.filter(childFolder -> childFolder.getParentId().equals(folder.getId()))										//Only subfolders for the give folder
				.map(childFolder -> mapFolderTree(franchiseeId, childFolder, franchisorFolders, franchisorFiles))				//Map childs recursively
				.filter(mappedSubFolder -> !mappedSubFolder.getFolders().isEmpty() || !mappedSubFolder.getFiles().isEmpty()) 	//Remove empty folders
				.collect(Collectors.toList())
		);

		//Add child files
		mappedFolder.setFiles(
			franchisorFiles.stream()
				.filter(childFile -> 
					childFile.getFolderId().equals(folder.getId()) && 			//Only files for the given folder
					!childFile.getAccessRestricted() &&							//Remove restricted ones (Franchisor only)
					(
						childFile.getFranchiseeIds() == null ||					//Include if there is no franchisee restriction
						childFile.getFranchiseeIds().isEmpty() ||				//Include if there is no franchisee restriction
						childFile.getFranchiseeIds().contains(franchiseeId)		//Include if franchiseeId in the franchisee restriction
					)
				)
				.map(childFile -> mapFile(childFile))
				.collect(Collectors.toList())
		);

		return mappedFolder;
	}

	@Override
	public void updateDocumentsFranchiseeIndex(Long franchisorId, Date requestDate) {

		List<DocumentsFranchiseeIndexData> currentIndexes = documentsFranchiseeIndexDAO.findByFranchisorId(franchisorId);
		if (currentIndexes != null && !currentIndexes.isEmpty() && currentIndexes.get(0).getUpdatedAt().compareTo(requestDate) > 0) {
			//Skip index update if updated more recently
			log.info("Skipping updateDocumentsFranchiseeIndex for franchisorId " + franchisorId);
			return;
		}

		Gson gson = new Gson();
		Date updatedAt = new Date();
		List<DocumentsFolderData> franchisorFolders = documentsFolderDAO.findByFranchisorId(franchisorId);
		List<DocumentsFileData> franchisorFiles = documentsFileDAO.findByFranchisorId(franchisorId);

		Collections.sort(franchisorFolders, new Comparator<DocumentsFolderData>() {
	        @Override
	        public int compare(DocumentsFolderData folder1, DocumentsFolderData folder2)
	        {
	            return  folder1.getOrder().compareTo(folder2.getOrder());
	        }
	    });

		List<FranchiseeData> franchisees = franchiseeManagement.getFranchiseesByFranchisorId(franchisorId);
		if (franchisees != null) {
			franchisees.stream().forEach(franchisee -> {

				DocumentsFolderData rootFolder = new DocumentsFolderData();
				rootFolder.setId(0L);
				rootFolder.setName("root");
				rootFolder.setFranchisorId(franchisorId);

				DocumentsFolderStructureData folderTree = mapFolderTree(franchisee.getId(), rootFolder, franchisorFolders, franchisorFiles);

				DocumentsFranchiseeIndexData documentsFranchiseeIndex = new DocumentsFranchiseeIndexData();
				documentsFranchiseeIndex.setId(franchisee.getId());
				documentsFranchiseeIndex.setUpdatedAt(updatedAt);
				documentsFranchiseeIndex.setFranchisorId(franchisorId);
				documentsFranchiseeIndex.setIndexJson(gson.toJson(folderTree));
				documentsFranchiseeIndexDAO.save(documentsFranchiseeIndex);
			});
		}
	}

	private void triggerAsyncDocumentsFranchiseeIndexUpdate(Long franchisorId) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String requestDate = formatter.format(new Date());

		//Asynchronously process indexing
		Queue queue = QueueFactory.getQueue("indexing");
		queue.add(
			TaskOptions.Builder
				.withUrl("/job/indexing/updateDocumentsFranchiseeIndex/"+franchisorId)
				.countdownMillis(300000)
				.param("requestDate", requestDate)
		);

	}
	
	@Override
	public List<DocumentsFolderData> getFranchisorDocumentsFoldersByParentId(Long franchisorId, Long parentId) {
		return documentsFolderDAO.findByFranchisorIdAndParentId(franchisorId, parentId);
	}

	@Override
	public List<DocumentsFileData> getFranchisorDocumentsFilesByFolderId(Long franchisorId, Long folderId) {
		return documentsFileDAO.findByFranchisorIdAndFolderId(franchisorId, folderId);
	}

	@Override
	public DocumentsFolderStructureData getFranchisorDocumentsByFranchiseeId(Long franchiseeId) {
		Gson gson = new Gson();
		DocumentsFranchiseeIndexData indexData = documentsFranchiseeIndexDAO.findById(franchiseeId);
		String jsonIndex = "{'id':0,'name':'root','folders':[],'files':[]}";
		if (indexData != null) {
			jsonIndex = indexData.getIndexJson();
		}
		
		return gson.fromJson(jsonIndex, DocumentsFolderStructureData.class);
	}

	@Override
	public DocumentsFileData moveFile(Long id, Long folderId) {
		DocumentsFileData file = documentsFileDAO.findById(id);
		if (file != null) {
			file.setFolderId(folderId);
			file = documentsFileDAO.save(file);
			triggerAsyncDocumentsFranchiseeIndexUpdate(file.getFranchisorId());
		}
		return file;
	}

	@Override
	public DocumentsFolderData moveFolder(Long id, Long parentId) {
		DocumentsFolderData folder = documentsFolderDAO.findById(id);
		if (folder != null) {
			folder.setParentId(parentId);
			folder = documentsFolderDAO.save(folder);
			triggerAsyncDocumentsFranchiseeIndexUpdate(folder.getFranchisorId());
		}
		return folder;
	}
}
