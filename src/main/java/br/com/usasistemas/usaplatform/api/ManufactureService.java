package br.com.usasistemas.usaplatform.api;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.ManufactureDataRequest;
import br.com.usasistemas.usaplatform.api.request.WSUpdateManufactureRequestStatusRequest;
import br.com.usasistemas.usaplatform.api.response.WSEnumValidValuesResponse;
import br.com.usasistemas.usaplatform.api.response.WSManufactureServiceListResponse;
import br.com.usasistemas.usaplatform.api.response.data.ManufactureRequestResponseData;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.ManufactureManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.dao.response.ManufactureRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.enums.ManufactureRequestStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

@Controller
@RequestMapping(value = UrlMapping.MANUFACTURE_SERVICE)
public class ManufactureService {
	
	private static final Logger log = Logger.getLogger(ManufactureService.class.getName());
	private ManufactureManagementBO manufactureManagement;
	private ProductManagementBO productManagement;
	private FranchisorManagementBO franchisorManagement;
	private ConfigurationManagementBO configurationManagement;
	
	public ManufactureManagementBO getManufactureManagement() {
		return manufactureManagement;
	}

	public void setManufactureManagement(
			ManufactureManagementBO manufactureManagement) {
		this.manufactureManagement = manufactureManagement;
	}

	public ProductManagementBO getProductManagement() {
		return productManagement;
	}

	public void setProductManagement(ProductManagementBO productManagement) {
		this.productManagement = productManagement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManagement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	/*
	 * Get Manufacture Request list by Franchisor
	 */
	@RequestMapping(value = "/franchisor/{franchisorId}/paged/{cursorString}", method=RequestMethod.GET)
	@ResponseBody
	public WSManufactureServiceListResponse listManufactureRequestsByFranchisor(@PathVariable Long franchisorId, @PathVariable String cursorString) {
		
		if (cursorString.equals("none")) {
			cursorString = null;
		}
		
		WSManufactureServiceListResponse response = new WSManufactureServiceListResponse();
		
		try {
			ManufactureRequestPagedResponse manufactureRequestPagedResponse = manufactureManagement.getManufactureRequestsByFranchisorId(franchisorId, 10L, cursorString);
			response.setManufactureRequests(manufactureRequestPagedResponse.getManufactureRequests());
			response.setCursorString(manufactureRequestPagedResponse.getCursorString());
			response.setProducts(manufactureManagement.getManufactureRequestsProducts(response.getManufactureRequests()));
			response.setFranchisors(manufactureManagement.getDeliveryRequestsFranchisors(response.getManufactureRequests()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Manufacture Requests by Franchisor: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Manufacture Request list by Franchisor
	 */
	@RequestMapping(value = "/product/{productId}/paged/{cursorString}", method=RequestMethod.GET)
	@ResponseBody
	public WSManufactureServiceListResponse listManufactureRequestsByProduct(@PathVariable Long productId, @PathVariable String cursorString) {
		
		if (cursorString.equals("none")) {
			cursorString = null;
		}
		
		WSManufactureServiceListResponse response = new WSManufactureServiceListResponse();
		
		try {
			ManufactureRequestPagedResponse manufactureRequestPagedResponse = manufactureManagement.getManufactureRequestsByProductId(productId, 10L, cursorString);
			response.setManufactureRequests(manufactureRequestPagedResponse.getManufactureRequests());
			response.setCursorString(manufactureRequestPagedResponse.getCursorString());
			response.setProducts(manufactureManagement.getManufactureRequestsProducts(response.getManufactureRequests()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Manufacture Requests by Franchisor: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Manufacture Request list by Supplier
	 */
	/*@RequestMapping(value = "/supplier/{supplierId}", method=RequestMethod.GET)
	@ResponseBody
	public WSManufactureServiceListResponse listManufactureRequestsBySupplier(@PathVariable Long supplierId) {
		
		WSManufactureServiceListResponse response = new WSManufactureServiceListResponse();
		
		try {
			response.setManufactureRequests(manufactureManagement.getManufactureRequestsBySupplierId(supplierId));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Manufacture Requests by Supplier: " + e.toString());
		}
		
		return response;
	}*/
	
	/*
	 * Get Manufacture Request list by Supplier and Status
	 */
	@RequestMapping(value = "/supplier/{supplierId}/status/{status}", method=RequestMethod.GET)
	@ResponseBody
	public WSManufactureServiceListResponse listManufactureRequestsBySupplierAndStatus(@PathVariable Long supplierId, @PathVariable String status) {
		
		WSManufactureServiceListResponse response = new WSManufactureServiceListResponse();
		
		try {
			ManufactureRequestStatusEnum manufactureStatus = ManufactureRequestStatusEnum.valueOf(status);
			
			response.setManufactureRequests(manufactureManagement.getManufactureRequestsBySupplierIdAndStatus(supplierId, manufactureStatus));
			response.setProducts(manufactureManagement.getMaufactureRequestsProducts(response.getManufactureRequests()));
			response.setFranchisors(manufactureManagement.getMaufactureRequestsFranchisors(response.getManufactureRequests()));

		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Manufacture Requests by Supplier: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Manufacture Request statuses
	 */
	@RequestMapping(value = "/statuses", method=RequestMethod.GET)
	@ResponseBody
	public WSEnumValidValuesResponse getStatuses() {
		
		WSEnumValidValuesResponse response = new WSEnumValidValuesResponse();
		
		try {
			response.setEnumValues(manufactureManagement.getStatuses());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Manufacture Statuses: " + e.toString());
		}
		
		return response;		
	}
	
	/*
	 * Create new Manufacture request
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public ManufactureRequestData createManufactureRequest(@RequestBody ManufactureDataRequest manufactureRequest, HttpSession session, HttpServletRequest request) {
		DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
		return manufactureManagement.createManufactureRequest(manufactureRequest.getFranchisorId(), manufactureRequest.getProductId(), manufactureRequest.getProductSizeId(), manufactureRequest.getQuantity(), manufactureRequest.getAddStockOnly(), domainConfiguration.getKey());
	}
	
	/*
	 * Update manufacture status
	 */
	@RequestMapping(value = "/status", method=RequestMethod.PUT)
	@ResponseBody
	public ManufactureRequestResponseData updateManufactureRequestStatus(@RequestBody WSUpdateManufactureRequestStatusRequest request, HttpSession session) {
		return manufactureManagement.updateManufactureRequestStatus(request.getManufactureRequestId(), request.getStatus(), request.getCancellationComment());
	}

}
