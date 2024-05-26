package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.UploadedFileDAO;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.entity.UploadedFile;

public class GAEUploadedFileDAOImpl extends GAEGenericDAOImpl<UploadedFile, UploadedFileData> implements UploadedFileDAO {
	
	private static final Logger log = Logger.getLogger(GAEUploadedFileDAOImpl.class.getName());
	
	@Override
	public List<UploadedFileData> findUnusedByDate(Date date) {
		List<UploadedFileData> result = new ArrayList<UploadedFileData>();
		
		try {
			List<UploadedFile> uploadedFiles = ofy().load().type(UploadedFile.class)
				.filter("date <=", date)
				.list();
			if (uploadedFiles != null && !uploadedFiles.isEmpty())
				result = this.getConverter().convertToDataList(uploadedFiles);
		} catch (Exception e) {
			log.warning("Error when querying for UploadedFiles by date: " + e.toString());
		}	
		
		return result;
	}

}
