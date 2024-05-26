package br.com.usasistemas.usaplatform.bizo;

import javax.servlet.http.HttpServletRequest;

public interface ImageManagementBO {

	public String getImageUploadUrl(String redirectUrl);
	public String getImageKey(HttpServletRequest request, String fileFieldName);
	public String getImageServingUrl(String imageKey);
	public void deleteImage(String imageKey);

}
