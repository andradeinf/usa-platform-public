package br.com.usasistemas.usaplatform.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.DeliveryDataRequest;
import br.com.usasistemas.usaplatform.api.request.WSAccountsPayableRequest;
import br.com.usasistemas.usaplatform.api.request.WSAccountsReceivableRequest;
import br.com.usasistemas.usaplatform.api.request.WSUpdateDeliveryRequestStatusRequest;
import br.com.usasistemas.usaplatform.api.response.UrlResponse;
import br.com.usasistemas.usaplatform.api.response.WSAccountsPayableResponse;
import br.com.usasistemas.usaplatform.api.response.WSAccountsReceivableResponse;
import br.com.usasistemas.usaplatform.api.response.WSDeliveryServiceCreateResponse;
import br.com.usasistemas.usaplatform.api.response.WSDeliveryServiceListResponse;
import br.com.usasistemas.usaplatform.api.response.WSDeliveryServiceResponse;
import br.com.usasistemas.usaplatform.api.response.WSEnumValidValuesResponse;
import br.com.usasistemas.usaplatform.api.response.WSUploadedFileResponse;
import br.com.usasistemas.usaplatform.api.response.data.DeliveryRequestStatusResponseData;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.dao.response.DeliveryRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.DELIVERY_SERVICE)
public class DeliveryService {
	
	private static final Logger log = Logger.getLogger(DeliveryService.class.getName());
	private DeliveryManagementBO deliveryManagement;
	private FranchiseeManagementBO franchiseeManagement;
	private ConfigurationManagementBO configurationManagement;
	private FileManagementBO fileManagement;
	
	public DeliveryManagementBO getDeliveryManagement() {
		return deliveryManagement;
	}

	public void setDeliveryManagement(DeliveryManagementBO deliveryManagement) {
		this.deliveryManagement = deliveryManagement;
	}
	
	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	public FileManagementBO getFileManagement() {
		return fileManagement;
	}

	public void setFileManagement(FileManagementBO fileManagement) {
		this.fileManagement = fileManagement;
	}

	/*
	 * Get Delivery Request list without any filter
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public WSDeliveryServiceListResponse listDeliveryRequests() {
		
		WSDeliveryServiceListResponse response = new WSDeliveryServiceListResponse();
		
		try {
			response.setDeliveryRequests(deliveryManagement.getDeliveryRequests());
			response.setProducts(deliveryManagement.getDeliveryRequestsProducts(response.getDeliveryRequests()));
			response.setFranchisees(deliveryManagement.getDeliveryRequestsFranchisees(response.getDeliveryRequests()));
			response.setFranchisors(deliveryManagement.getDeliveryRequestsFranchiseesFranchisors(new ArrayList<FranchiseeData>(response.getFranchisees().values())));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Delivery Requests: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Delivery Request list by Franchisee
	 */
	@RequestMapping(value = "/franchisee/{franchiseeId}/paged/{cursorString}", method=RequestMethod.GET)
	@ResponseBody
	public WSDeliveryServiceListResponse listDeliveryRequestsByFranchisee(@PathVariable Long franchiseeId, @PathVariable String cursorString) {
		
		if (cursorString.equals("none")) {
			cursorString = null;
		}
		
		WSDeliveryServiceListResponse response = new WSDeliveryServiceListResponse();
		
		try {
			DeliveryRequestPagedResponse deliveryRequestPagedResponse = deliveryManagement.getDeliveryRequestsByFranchiseeId(franchiseeId, 10L, cursorString);
			response.setDeliveryRequests(deliveryRequestPagedResponse.getDeliveryRequests());
			response.setCursorString(deliveryRequestPagedResponse.getCursorString());
			response.setProducts(deliveryManagement.getDeliveryRequestsProducts(response.getDeliveryRequests()));
			response.setSuppliers(deliveryManagement.getDeliveryRequestsProductsSuppliers(new ArrayList<ProductData>(response.getProducts().values())));
			response.setFranchisees(deliveryManagement.getDeliveryRequestsFranchisees(response.getDeliveryRequests()));
			response.setFranchisors(deliveryManagement.getDeliveryRequestsFranchiseesFranchisors(new ArrayList<FranchiseeData>(response.getFranchisees().values())));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Delivery Requests by Franchisee: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Delivery Request list by Franchisee and Product
	 */
	@RequestMapping(value = "/product/{productId}/franchisee/{franchiseeId}/paged/{cursorString}", method=RequestMethod.GET)
	@ResponseBody
	public WSDeliveryServiceListResponse getProductHistory(@PathVariable Long productId, @PathVariable Long franchiseeId, @PathVariable String cursorString) {
		
		if (cursorString.equals("none")) {
			cursorString = null;
		}
		
		WSDeliveryServiceListResponse response = new WSDeliveryServiceListResponse();
		
		try {
			DeliveryRequestPagedResponse deliveryRequestPagedResponse = deliveryManagement.getDeliveryRequestsByProductIdAndFranchiseeId(productId, franchiseeId, 10L, cursorString);
			response.setDeliveryRequests(deliveryRequestPagedResponse.getDeliveryRequests());
			response.setCursorString(deliveryRequestPagedResponse.getCursorString());
			response.setProducts(deliveryManagement.getDeliveryRequestsProducts(response.getDeliveryRequests()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Delivery Requests by Franchisee: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Delivery Request list by Franchisee and Supplier
	 */
	@RequestMapping(value = "/supplier/{supplierId}/franchisee/{franchiseeId}/status/{status}/paged/{cursorString}", method=RequestMethod.GET)
	@ResponseBody
	public WSDeliveryServiceListResponse getSuplierFranchiseeDeliveryRequests(@PathVariable Long supplierId, @PathVariable Long franchiseeId, @PathVariable String status, @PathVariable String cursorString) {
		
		if (cursorString.equals("none")) {
			cursorString = null;
		}
		
		WSDeliveryServiceListResponse response = new WSDeliveryServiceListResponse();
		
		try {
			DeliveryRequestStatusEnum deliveryStatus = DeliveryRequestStatusEnum.valueOf(status);
			
			DeliveryRequestPagedResponse deliveryRequestPagedResponse = deliveryManagement.getDeliveryRequestsBySupplierIdAndFranchiseeIdAndStatus(supplierId, franchiseeId, deliveryStatus, 10L, cursorString);
			response.setDeliveryRequests(deliveryRequestPagedResponse.getDeliveryRequests());
			response.setCursorString(deliveryRequestPagedResponse.getCursorString());
			response.setProducts(deliveryManagement.getDeliveryRequestsProducts(response.getDeliveryRequests()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Delivery Requests by Franchisee: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Delivery Request list by Product
	 */
	@RequestMapping(value = "/product/{productId}/paged/{cursorString}", method=RequestMethod.GET)
	@ResponseBody
	public WSDeliveryServiceListResponse getProductHistory(@PathVariable Long productId, @PathVariable String cursorString) {
		
		if (cursorString.equals("none")) {
			cursorString = null;
		}
		
		WSDeliveryServiceListResponse response = new WSDeliveryServiceListResponse();
		
		try {
			DeliveryRequestPagedResponse deliveryRequestPagedResponse = deliveryManagement.getDeliveryRequestsByProductId(productId, 10L, cursorString);
			response.setDeliveryRequests(deliveryRequestPagedResponse.getDeliveryRequests());
			response.setCursorString(deliveryRequestPagedResponse.getCursorString());
			response.setProducts(deliveryManagement.getDeliveryRequestsProducts(response.getDeliveryRequests()));
			response.setFranchisees(deliveryManagement.getDeliveryRequestsFranchisees(response.getDeliveryRequests()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Delivery Requests by Product: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Delivery Request list by Supplier
	 */
	@RequestMapping(value = "/supplier/{supplierId}", method=RequestMethod.GET)
	@ResponseBody
	public WSDeliveryServiceListResponse listDeliveryRequestsBySupplier(@PathVariable Long supplierId) {
		
		WSDeliveryServiceListResponse response = new WSDeliveryServiceListResponse();
		
		try {
			response.setDeliveryRequests(deliveryManagement.getDeliveryRequestsBySupplierId(supplierId));
			response.setProducts(deliveryManagement.getDeliveryRequestsProducts(response.getDeliveryRequests()));
			response.setFranchisees(deliveryManagement.getDeliveryRequestsFranchisees(response.getDeliveryRequests()));
			response.setFranchisors(deliveryManagement.getDeliveryRequestsFranchiseesFranchisors(new ArrayList<FranchiseeData>(response.getFranchisees().values())));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Delivery Requests by Franchisee: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Delivery Request list by Supplier and Status
	 */
	@RequestMapping(value = "/supplier/{supplierId}/status/{status}", method=RequestMethod.GET)
	@ResponseBody
	public WSDeliveryServiceListResponse listDeliveryRequestsBySupplierAndStatus(@PathVariable Long supplierId, @PathVariable String status) {
		
		WSDeliveryServiceListResponse response = new WSDeliveryServiceListResponse();
		
		try {
			DeliveryRequestStatusEnum deliveryStatus = DeliveryRequestStatusEnum.valueOf(status);
			
			response.setDeliveryRequests(deliveryManagement.getDeliveryRequestsBySupplierIdAndStatus(supplierId, deliveryStatus));
			response.setProducts(deliveryManagement.getDeliveryRequestsProducts(response.getDeliveryRequests()));
			response.setFranchisees(deliveryManagement.getDeliveryRequestsFranchisees(response.getDeliveryRequests()));			
			List<FranchiseeData> franchisees = new ArrayList<FranchiseeData>(response.getFranchisees().values());			
			response.setFranchisors(deliveryManagement.getDeliveryRequestsFranchiseesFranchisors(franchisees));
			response.setStates(franchiseeManagement.getFranchiseesStates(franchisees));
			response.setCities(franchiseeManagement.getFranchiseesCities(franchisees));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Delivery Requests by Franchisee: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Delivery Request statuses
	 */
	@RequestMapping(value = "/statuses", method=RequestMethod.GET)
	@ResponseBody
	public WSEnumValidValuesResponse getStatuses() {
		
		WSEnumValidValuesResponse response = new WSEnumValidValuesResponse();
		
		try {
			response.setEnumValues(deliveryManagement.getStatuses());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Delivery Statuses: " + e.toString());
		}
		
		return response;
		
	}
	
	/*
	 * Get Accounts Receivable
	 */
	@RequestMapping(value = "/supplier/{supplierId}/accountsReceivable/paged/{cursorString}", method=RequestMethod.POST)
	@ResponseBody
	public WSAccountsReceivableResponse listAccountsReceivable(@RequestBody WSAccountsReceivableRequest filter, @PathVariable Long supplierId, @PathVariable String cursorString) {
		
		if (cursorString.equals("none")) {
			cursorString = null;
		}
		
		WSAccountsReceivableResponse response = new WSAccountsReceivableResponse();
		
		try {
			
			//Start date should be at 00:00:00
			String fromDateWithoutTimeStr = DateUtil.getDate(filter.getFromDate(), DateUtil.SLASH_PATTERN);
			Date fromDate = DateUtil.getDate(fromDateWithoutTimeStr, DateUtil.SLASH_PATTERN);
			log.fine("FromDate: " + DateUtil.getDate(fromDate));
			
			//End date should be at 23:59:59
			String toDateWithoutTimeStr = DateUtil.getDate(filter.getToDate(), DateUtil.SLASH_PATTERN);
			Date toDate = DateUtil.getDate(toDateWithoutTimeStr + " 23:59:59", DateUtil.SLASH_FULL_PATTERN);
			log.fine("ToDate: " + DateUtil.getDate(toDate));
			
			DeliveryRequestPagedResponse deliveryRequestPagedResponse = deliveryManagement.getAccountsReceivable(supplierId, fromDate, toDate, 10L, cursorString);
			response.setDeliveryRequests(deliveryRequestPagedResponse.getDeliveryRequests());
			response.setCursorString(deliveryRequestPagedResponse.getCursorString());
			response.setProducts(deliveryManagement.getDeliveryRequestsProducts(response.getDeliveryRequests()));
			response.setFranchisees(deliveryManagement.getDeliveryRequestsFranchisees(response.getDeliveryRequests()));
			response.setFranchisors(deliveryManagement.getDeliveryRequestsFranchiseesFranchisors(new ArrayList<FranchiseeData>(response.getFranchisees().values())));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Accounts Receivable: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Accounts Receivable
	 */
	@RequestMapping(value = "/franchisee/{franchiseeId}/accountsPayable/paged/{cursorString}", method=RequestMethod.POST)
	@ResponseBody
	public WSAccountsPayableResponse listAccountsPayable(@RequestBody WSAccountsPayableRequest filter, @PathVariable Long franchiseeId, @PathVariable String cursorString) {
		
		if (cursorString.equals("none")) {
			cursorString = null;
		}
		
		WSAccountsPayableResponse response = new WSAccountsPayableResponse();
		
		try {
			
			//Start date should be at 00:00:00
			String fromDateWithoutTimeStr = DateUtil.getDate(filter.getFromDate(), DateUtil.SLASH_PATTERN);
			Date fromDate = DateUtil.getDate(fromDateWithoutTimeStr, DateUtil.SLASH_PATTERN);
			log.info("FromDate: " + DateUtil.getDate(fromDate));
			
			//End date should be at 23:59:59
			String toDateWithoutTimeStr = DateUtil.getDate(filter.getToDate(), DateUtil.SLASH_PATTERN);
			Date toDate = DateUtil.getDate(toDateWithoutTimeStr + " 23:59:59", DateUtil.SLASH_FULL_PATTERN);
			log.info("ToDate: " + DateUtil.getDate(toDate));
			
			DeliveryRequestPagedResponse deliveryRequestPagedResponse = deliveryManagement.getAccountsPayable(franchiseeId, fromDate, toDate, 10L, cursorString);
			response.setDeliveryRequests(deliveryRequestPagedResponse.getDeliveryRequests());
			response.setCursorString(deliveryRequestPagedResponse.getCursorString());
			response.setProducts(deliveryManagement.getDeliveryRequestsProducts(response.getDeliveryRequests()));
			response.setSuppliers(deliveryManagement.getDeliveryRequestsSuppliers(response.getDeliveryRequests()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Accounts Receivable: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Create new Delivery request
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSDeliveryServiceCreateResponse createDeliveryRequest(@RequestBody DeliveryDataRequest deliveryRequest, HttpSession session, HttpServletRequest request) {
		
		WSDeliveryServiceCreateResponse response = new WSDeliveryServiceCreateResponse();
		
		try {
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByURL(request.getServerName());
			response.setDeliveryRequest(deliveryManagement.createDeliveryRequest(deliveryRequest.getFranchiseeId(), deliveryRequest.getProductId(), deliveryRequest.getProductSizeId(), deliveryRequest.getQuantity(), domainConfiguration.getKey()));
		} catch (BusinessException be) {
			response.setReturnMessage(be.getReturnMessage());
			log.warning("Error creating Delivery Request: " + be.toString());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Delivery Request: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Update delivery status
	 */
	@RequestMapping(value = "/status", method=RequestMethod.PUT)
	@ResponseBody
	public WSDeliveryServiceResponse updateDeliveryRequestStatus(@RequestBody WSUpdateDeliveryRequestStatusRequest request, HttpSession session) {
		
		WSDeliveryServiceResponse response = new WSDeliveryServiceResponse();
		
		try {
			Date sentDate = null;
			Date deadlineDate = null;
			Date dueDate = null;
			Date autoCancellationDate = null;
			
			//parse dates and throw custom fault if not correct
			if (request.getSentDate() != null && !request.getSentDate().isEmpty()) {
				try{    
					sentDate = DateUtil.getDate(request.getSentDate());
		        }catch(Exception e) {
		        	log.warning("Error parsing sentDate: " + e.toString());	
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
					rm.setMessage("A data de envio informada não é válida");
					throw new BusinessException(rm);
		        }
			}
			
			if (request.getDeadlineDate() != null && !request.getDeadlineDate().isEmpty()) {
				try{    
					deadlineDate = DateUtil.getDate(request.getDeadlineDate());    
		        }catch(Exception e) {
		        	log.warning("Error parsing deadlineDate: " + e.toString());	
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
					rm.setMessage("A data limite para entrega informada não é válida");
					throw new BusinessException(rm);
		        }
			}
			
			if (request.getDueDate() != null && !request.getDueDate().isEmpty()) {
				try{    
					dueDate = DateUtil.getDate(request.getDueDate());    
		        }catch(Exception e) {
		        	log.warning("Error parsing dueDate: " + e.toString());	
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
					rm.setMessage("A data de vencimento da Nota Fiscal informada não é válida");
					throw new BusinessException(rm);
		        }
			}
			
			if (request.getAutoCancellationDate() != null && !request.getAutoCancellationDate().isEmpty()) {
				try{    
					autoCancellationDate = DateUtil.getDate(request.getAutoCancellationDate());    
		        }catch(Exception e) {
		        	log.warning("Error parsing autoCancellationDate: " + e.toString());	
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
					rm.setMessage("A data para cancelamento automático informada não é válida");
					throw new BusinessException(rm);
		        }
			}
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (!user.getSelectedRole().equals(UserProfileEnum.SUPPLIER) && !user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to update Delivery Requests");
			
			} else {
				DeliveryRequestStatusResponseData deliveryRequestUpdates = deliveryManagement.updateDeliveryRequestStatus(request.getDeliveryRequests(), request.getStatus(), request.getCancellationComment(), sentDate, deadlineDate, request.getCarrierName(), request.getTrackingCode(), request.getFiscalNumber(), dueDate, autoCancellationDate, request.getPaymentSlipId(), request.getFiscalFileId(), user); 
				response.setDeliveryRequests(deliveryRequestUpdates.getDeliveryRequests());
				response.setNewDeliveryRequests(deliveryRequestUpdates.getNewDeliveryRequests());
			}	
		
		} catch (BusinessException be) {
			response.setReturnMessage(be.getReturnMessage());
			log.warning("Error updating Delivery Request: " + be.toString());		
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Delivery Request: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get URL to upload payment slip
	 */
	@RequestMapping(value = "/paymentSlipUpload/url", method=RequestMethod.GET)
	@ResponseBody
	public UrlResponse getPaymentSlipUploadUrl(HttpSession session) {
		
		UrlResponse response = new UrlResponse();		
		response.setUrl(fileManagement.getPaymentSlipUploadUrl(UrlMapping.DELIVERY_SERVICE + "/fileUpload/upload"));
		return response;	
	}
	
	/*
	 * Get URL to upload file
	 */
	@RequestMapping(value = "/fiscalFileUpload/url", method=RequestMethod.GET)
	@ResponseBody
	public UrlResponse getFiscalFileUploadUrl(HttpSession session) {
		
		UrlResponse response = new UrlResponse();		
		response.setUrl(fileManagement.getFiscalFileUploadUrl(UrlMapping.DELIVERY_SERVICE + "/fileUpload/upload"));
		return response;	
	}
	
	/*
	 * Process uploaded file
	 */
	@RequestMapping(value = "/fileUpload/upload", method=RequestMethod.POST)
	@ResponseBody
	public WSUploadedFileResponse processUploadedFile(HttpServletRequest request) {
		WSUploadedFileResponse response = new WSUploadedFileResponse();
		
		try {
			UploadedFileData file = fileManagement.getUploadedFileInfo(request, "uploadedFile");	
			response.setUploadedFile(fileManagement.createUploadedFile(file));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.FAIL_TO_UPLOAD_ATTACHMENT.code());
			rm.setMessage(ResponseCodesEnum.FAIL_TO_UPLOAD_ATTACHMENT.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error uploading file: " + e.toString());
		}
		
		return response;	
	}
	
	@RequestMapping(value="/fileUpload/downloadUploadedFile/{id}", method=RequestMethod.GET)
	public void getfileUploadDownloadFile(@PathVariable Long id, HttpServletResponse response) {
		UploadedFileData file = fileManagement.getUploadedFile(id);
		fileManagement.readFile(file.getFileKey(), file.getName(), true, response);
	}
	
	@RequestMapping(value="/paymentSlip/download/{id}", method=RequestMethod.GET)
	public void getPaymentSlip(@PathVariable Long id, HttpServletResponse response) {
		
		String paymentSlipKey = null;
		String paymentSlipName = null;
		
		DeliveryRequestData deliveryRequest = deliveryManagement.getDeliveryRequest(id);
		if (deliveryRequest != null) {
			paymentSlipKey = deliveryRequest.getPaymentSlipKey();
			paymentSlipName = deliveryRequest.getPaymentSlipName();
		} else {
			DeliveryRequestHistoryData deliveryRequestHistory =  deliveryManagement.getDeliveryRequestHistory(id);
			paymentSlipKey = deliveryRequestHistory.getPaymentSlipKey();
			paymentSlipName = deliveryRequestHistory.getPaymentSlipName();
		}
		
		fileManagement.readFile(paymentSlipKey, paymentSlipName, true, response);
	}
	
	@RequestMapping(value="/fiscalFile/download/{id}", method=RequestMethod.GET)
	public void getFiscalFile(@PathVariable Long id, HttpServletResponse response) {
		
		String fiscalFileKey = null;
		String fiscalFileName = null;
		
		DeliveryRequestData deliveryRequest = deliveryManagement.getDeliveryRequest(id);
		if (deliveryRequest != null) {
			fiscalFileKey = deliveryRequest.getFiscalFileKey();
			fiscalFileName = deliveryRequest.getFiscalFileName();
		} else {
			DeliveryRequestHistoryData deliveryRequestHistory =  deliveryManagement.getDeliveryRequestHistory(id);
			fiscalFileKey = deliveryRequestHistory.getFiscalFileKey();
			fiscalFileName = deliveryRequestHistory.getFiscalFileName();
		}
		
		fileManagement.readFile(fiscalFileKey, fiscalFileName, true, response);
	}
	
}
