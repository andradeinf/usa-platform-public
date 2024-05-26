package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.DocumentsFileDAO;
import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;
import br.com.usasistemas.usaplatform.model.entity.DocumentsFile;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;

public class GAEDocumentsFileDAOImpl extends GAEGenericDAOImpl<DocumentsFile, DocumentsFileData> implements DocumentsFileDAO {
	
	private static final Logger log = Logger.getLogger(GAEDocumentsFileDAOImpl.class.getName());

	@Override
	public List<DocumentsFileData> findByFranchisorId(Long franchisorId) {
		List<DocumentsFileData> result = new ArrayList<DocumentsFileData>();

		try {
			List<DocumentsFile> files = ofy().load().type(DocumentsFile.class)
				.filter("franchisorId", franchisorId)
				.list();
			if (files != null && !files.isEmpty())
				result = this.getConverter().convertToDataList(files);
		} catch (Exception e) {
			log.warning("Error when querying for Documents Files by franchisorId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<DocumentsFileData> findByNotificationStatus(MailNotificationStatusEnum notificationStatus) {
		List<DocumentsFileData> result = new ArrayList<DocumentsFileData>();

		try {
			List<DocumentsFile> documentsFiles = ofy().load().type(DocumentsFile.class)
				.filter("notificationStatus", notificationStatus)
				.list();
			if (documentsFiles != null && !documentsFiles.isEmpty())
				result = this.getConverter().convertToDataList(documentsFiles);
		} catch (Exception e) {
			log.warning("Error when querying for DocumentsFile by NotificationStatus: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<DocumentsFileData> findByFranchisorIdAndFolderId(Long franchisorId, Long folderId) {
		List<DocumentsFileData> result = new ArrayList<DocumentsFileData>();

		try {
			List<DocumentsFile> files = ofy().load().type(DocumentsFile.class)
				.filter("franchisorId", franchisorId)
				.filter("folderId", folderId)
				.list();
			if (files != null && !files.isEmpty())
				result = this.getConverter().convertToDataList(files);
		} catch (Exception e) {
			log.warning("Error when querying for Documents Files by franchisorId: " + e.toString());
		}			
	
		return result;
	}

}
