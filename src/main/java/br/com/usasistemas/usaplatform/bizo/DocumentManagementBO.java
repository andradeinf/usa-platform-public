package br.com.usasistemas.usaplatform.bizo;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.model.data.DocumentsFolderStructureData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;

public interface DocumentManagementBO {
	
	public DocumentsFolderData createDocumentsFolder(DocumentsFolderData folder);
	public List<DocumentsFolderData> getFranchisorDocumentsFolders(Long franchisorId, Long entityId, List<DocumentsFileData> documentsFiles);
	public List<DocumentsFileData> getFranchisorDocumentsFiles(Long franchisorId, Long entityId);
	public DocumentsFolderData deleteDocumentsFolder(Long id);
	public DocumentsFolderData getDocumentsFolder(Long id);
	public List<DocumentsFileData> createDocumentsFiles(List<UploadedFileData> files, Long folderId, Long franchisorId, Boolean accessRestricted, List<Long> franchiseeIds);
	public DocumentsFileData deleteDocumentsFile(Long id);
	public DocumentsFileData getDocumentsFile(Long id);
	public List<DocumentsFileData> getDocumentsFilePendingNotifications();
	public List<DocumentsFileData> updateDocumentsFileNotificationStatus(List<DocumentsFileData> documentsFiles, MailNotificationStatusEnum notificationStatus);
	public DocumentsFileData updateDocumentsFile(DocumentsFileData file);
	public List<DocumentsFolderData> getFranchisorDocumentsFoldersByParentId(Long franchisorId, Long parentId);
	public List<DocumentsFileData> getFranchisorDocumentsFilesByFolderId(Long franchisorId, Long folderId);
	public DocumentsFolderStructureData getFranchisorDocumentsByFranchiseeId(Long franchiseeId);
	public void updateDocumentsFranchiseeIndex(Long franchisorId, Date requestDate);
	public DocumentsFileData moveFile(Long id, Long folderId);
	public DocumentsFolderData moveFolder(Long id, Long parentId);

}
