package br.com.usasistemas.usaplatform.dao;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.entity.UploadedFile;


public interface UploadedFileDAO extends GenericDAO<UploadedFile, UploadedFileData> {

	public List<UploadedFileData> findUnusedByDate(Date date);
	
}
