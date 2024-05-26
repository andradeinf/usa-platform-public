package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.DocumentsFolderDAO;
import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;
import br.com.usasistemas.usaplatform.model.entity.DocumentsFolder;

public class GAEDocumentsFolderDAOImpl extends GAEGenericDAOImpl<DocumentsFolder, DocumentsFolderData> implements DocumentsFolderDAO {
	
	private static final Logger log = Logger.getLogger(GAEDocumentsFolderDAOImpl.class.getName());

	@Override
	public List<DocumentsFolderData> findByFranchisorId(Long franchisorId) {
		List<DocumentsFolderData> result = new ArrayList<DocumentsFolderData>();

		try {
			List<DocumentsFolder> folders = ofy().load().type(DocumentsFolder.class)
				.filter("franchisorId", franchisorId)
				.list();
			if (folders != null && !folders.isEmpty())
				result = this.getConverter().convertToDataList(folders);
		} catch (Exception e) {
			log.warning("Error when querying for Documents Folders by franchisorId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<DocumentsFolderData> findByFranchisorIdAndParentId(Long franchisorId, Long parentId) {
		List<DocumentsFolderData> result = new ArrayList<DocumentsFolderData>();

		try {
			List<DocumentsFolder> folders = ofy().load().type(DocumentsFolder.class)
				.filter("franchisorId", franchisorId)
				.filter("parentId", parentId)
				.list();
			if (folders != null && !folders.isEmpty())
				result = this.getConverter().convertToDataList(folders);
		} catch (Exception e) {
			log.warning("Error when querying for Documents Folders by franchisorId and parentId: " + e.toString());
		}			
	
		return result;
	}

}
