package br.com.usasistemas.usaplatform.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import br.com.usasistemas.usaplatform.dao.ImageDAO;

public class GAEImageDAOImpl implements ImageDAO {
	
	private static final Logger log = Logger.getLogger(GAEImageDAOImpl.class.getName());

	@Override
	public String getImageUploadUrl(String redirectUrl) {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		return blobstoreService.createUploadUrl(redirectUrl);
	}

	@Override
	public String getImageKey(HttpServletRequest request, String fileFieldName) {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
		return blobs.get(fileFieldName).get(0).getKeyString();
	}

	@Override
	public String getImageServingUrl(String imageKey) {
		String imageUrl = null;
		
		try {
			ImagesService imagesService = ImagesServiceFactory.getImagesService();
			
			// Get the image serving URL
			ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(new BlobKey(imageKey));
			imageUrl = imagesService.getServingUrl(options);
			
			// In development mode, it always returns 0.0.0.0 :-) Known problem
			//  http://code.google.com/p/googleappengine/issues/detail?id=4402
			//  http://code.google.com/p/googleappengine/issues/detail?id=5871
			imageUrl = imageUrl.replace("http://127.0.0.1:8888", "");
		} catch (Exception e) {
			log.warning("Not able to get image URL: " + e.toString());
		}
		
		return imageUrl;
	}

	@Override
	public void delete(String imageKey) {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		blobstoreService.delete(new BlobKey(imageKey));		
	}

}
