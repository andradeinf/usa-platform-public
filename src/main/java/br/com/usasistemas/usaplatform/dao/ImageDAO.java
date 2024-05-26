package br.com.usasistemas.usaplatform.dao;

import javax.servlet.http.HttpServletRequest;

public interface ImageDAO {

	public String getImageUploadUrl(String redirectUrl);
	public String getImageKey(HttpServletRequest request, String fileFieldName);
	public String getImageServingUrl(String imageKey);
	public void delete(String imageKey);
	
}
