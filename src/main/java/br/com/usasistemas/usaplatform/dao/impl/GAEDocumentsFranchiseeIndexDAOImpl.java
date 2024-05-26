package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.DocumentsFranchiseeIndexDAO;
import br.com.usasistemas.usaplatform.model.data.DocumentsFranchiseeIndexData;
import br.com.usasistemas.usaplatform.model.entity.DocumentsFranchiseeIndex;

public class GAEDocumentsFranchiseeIndexDAOImpl extends GAEGenericDAOImpl<DocumentsFranchiseeIndex, DocumentsFranchiseeIndexData> implements DocumentsFranchiseeIndexDAO {
	
	private static final Logger log = Logger.getLogger(GAEDocumentsFranchiseeIndexDAOImpl.class.getName());

	@Override
	public List<DocumentsFranchiseeIndexData> findByFranchisorId(Long franchisorId) {
		List<DocumentsFranchiseeIndexData> result = new ArrayList<DocumentsFranchiseeIndexData>();

		try {
			List<DocumentsFranchiseeIndex> indexes = ofy().load().type(DocumentsFranchiseeIndex.class)
				.filter("franchisorId", franchisorId)
				.list();
			//(List<DocumentsFranchiseeIndex>) q.execute(franchisorId);
			if (indexes != null && !indexes.isEmpty())
				result = this.getConverter().convertToDataList(indexes);
		} catch (Exception e) {
			log.warning("Error when querying for Documents Franchisee Indexes by franchisorId: " + e.toString());
		}			
	
		return result;
	}

}
