package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.DocumentsFranchiseeIndexData;
import br.com.usasistemas.usaplatform.model.entity.DocumentsFranchiseeIndex;

public interface DocumentsFranchiseeIndexDAO extends GenericDAO<DocumentsFranchiseeIndex, DocumentsFranchiseeIndexData> {

	public List<DocumentsFranchiseeIndexData> findByFranchisorId(Long franchisorId);
	
}
