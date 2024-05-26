package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;
import br.com.usasistemas.usaplatform.model.entity.DocumentsFolder;


public interface DocumentsFolderDAO extends GenericDAO<DocumentsFolder, DocumentsFolderData> {

	public List<DocumentsFolderData> findByFranchisorId(Long franchisorId);
	public List<DocumentsFolderData> findByFranchisorIdAndParentId(Long franchisorId, Long parentId);
	
}
