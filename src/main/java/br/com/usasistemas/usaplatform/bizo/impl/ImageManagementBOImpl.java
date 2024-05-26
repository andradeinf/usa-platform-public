package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import br.com.usasistemas.usaplatform.bizo.ImageManagementBO;
import br.com.usasistemas.usaplatform.dao.ImageDAO;

public class ImageManagementBOImpl implements ImageManagementBO {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ImageManagementBOImpl.class.getName());
	private ImageDAO imageDAO;

	public ImageDAO getImageDAO() {
		return imageDAO;
	}

	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	@Override
	public String getImageUploadUrl(String redirectUrl) {
		return imageDAO.getImageUploadUrl(redirectUrl);
	}

	@Override
	public String getImageKey(HttpServletRequest request, String fileFieldName) {
		return imageDAO.getImageKey(request, fileFieldName);
	}

	@Override
	public String getImageServingUrl(String imageKey) {
		return imageDAO.getImageServingUrl(imageKey);
	}

	@Override
	public void deleteImage(String imageKey) {
		imageDAO.delete(imageKey);		
	}

}
