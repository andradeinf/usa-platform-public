package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;
import br.com.usasistemas.usaplatform.model.entity.DocumentsFile;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;


public interface DocumentsFileDAO extends GenericDAO<DocumentsFile, DocumentsFileData> {

	public List<DocumentsFileData> findByFranchisorId(Long franchisorId);
	public List<DocumentsFileData> findByNotificationStatus(MailNotificationStatusEnum notificationStatus);
	public List<DocumentsFileData> findByFranchisorIdAndFolderId(Long franchisorId, Long folderId);
	
}
