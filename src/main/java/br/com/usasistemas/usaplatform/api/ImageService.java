package br.com.usasistemas.usaplatform.api;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.UrlResponse;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.ImageManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;

@Controller
@RequestMapping(value = UrlMapping.IMAGE_SERVICE)
public class ImageService {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ImageService.class.getName());
	private ImageManagementBO imageManagement;
	private ProductManagementBO productManagement;
	private SupplierManagementBO supplierManagement;
	private FranchisorManagementBO franchisorManagement;
	private FranchiseeManagementBO franchiseeManagement;
	
	public ImageManagementBO getImageManagement() {
		return imageManagement;
	}

	public void setImageManagement(ImageManagementBO imageManagement) {
		this.imageManagement = imageManagement;
	}

	public ProductManagementBO getProductManagement() {
		return productManagement;
	}

	public void setProductManagement(ProductManagementBO productManagement) {
		this.productManagement = productManagement;
	}

	public SupplierManagementBO getSupplierManagement() {
		return supplierManagement;
	}

	public void setSupplierManagement(SupplierManagementBO supplierManagement) {
		this.supplierManagement = supplierManagement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManagement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}
	
	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	/*
	 * Get URL to upload image
	 */
	@RequestMapping(value = "/url/{type}", method=RequestMethod.GET)
	@ResponseBody
	public UrlResponse getImageUrl(@PathVariable String type) {
		UrlResponse response = new UrlResponse();
		response.setUrl(imageManagement.getImageUploadUrl(UrlMapping.IMAGE_SERVICE + "/" + type));
		return response;	
	}
	
	/*
	 * Process Product image upload
	 */
	@RequestMapping(value = "/product", method=RequestMethod.POST)
	@ResponseBody
	public UrlResponse processProductImageUpload(HttpServletRequest request) {
		UrlResponse response = new UrlResponse();

		String imageKey = imageManagement.getImageKey(request, "uploadedFile");
		Long productId = Long.valueOf(request.getParameter("productId"));
		
		if (imageKey != null) {

			//Add image info to product
			ProductData product = productManagement.addImage(productId, imageKey);

			response.setUrl(product.getImageURL());
		}
		
		return response;	
	}
	
	/*
	 * Process Supplier image upload
	 */
	@RequestMapping(value = "/supplier", method=RequestMethod.POST)
	@ResponseBody
	public UrlResponse processSupplierImageUpload(HttpServletRequest request) {
		UrlResponse response = new UrlResponse();

		String imageKey = imageManagement.getImageKey(request, "uploadedFile");
		Long supplierId = Long.valueOf(request.getParameter("supplierId"));
		
		if (imageKey != null) {

			//Add image info to supplier
			SupplierData supplier = supplierManagement.addImage(supplierId, imageKey);

			response.setUrl(supplier.getImageURL());
		}
		
		return response;	
	}
	
	/*
	 * Process Franchisor image upload
	 */
	@RequestMapping(value = "/franchisor", method=RequestMethod.POST)
	@ResponseBody
	public UrlResponse processFranchisorImageUpload(HttpServletRequest request) {
		UrlResponse response = new UrlResponse();

		String imageKey = imageManagement.getImageKey(request, "uploadedFile");
		Long franchisorId = Long.valueOf(request.getParameter("franchisorId"));
		
		if (imageKey != null) {

			//Add image info to franchisor
			FranchisorData franchisor = franchisorManagement.addImage(franchisorId, imageKey);

			response.setUrl(franchisor.getImageURL());
		}
		
		return response;	
	}
	
	/*
	 * Process Franchisee image upload
	 */
	@RequestMapping(value = "/franchisee", method=RequestMethod.POST)
	@ResponseBody
	public UrlResponse processFranchiseeImageUpload(HttpServletRequest request) {
		UrlResponse response = new UrlResponse();

		String imageKey = imageManagement.getImageKey(request, "uploadedFile");
		Long franchiseeId = Long.valueOf(request.getParameter("franchiseeId"));
		
		if (imageKey != null) {

			//Add image info to franchisee
			FranchiseeData franchisee = franchiseeManagement.addImage(franchiseeId, imageKey);

			response.setUrl(franchisee.getImageURL());
		}
		
		return response;	
	}

}
