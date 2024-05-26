package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.Gson;

import br.com.usasistemas.usaplatform.api.request.data.DeliveryRequestStatusData;
import br.com.usasistemas.usaplatform.api.response.data.DeliveryRequestStatusResponseData;
import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.MailManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.dao.DeliveryRequestDAO;
import br.com.usasistemas.usaplatform.dao.DeliveryRequestHistoryDAO;
import br.com.usasistemas.usaplatform.dao.response.DeliveryRequestHistoryPagedResponse;
import br.com.usasistemas.usaplatform.dao.response.DeliveryRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.converter.ObjectConverter;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestStatusChangeData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestStatusHistoryData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.ProductTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class DeliveryManagementBOImpl implements DeliveryManagementBO {
	
	private static final Logger log = Logger.getLogger(DeliveryManagementBOImpl.class.getName());
	private DeliveryRequestDAO deliveryRequestDAO;
	private DeliveryRequestHistoryDAO deliveryRequestHistoryDAO;
	private ProductManagementBO productManagement;
	private FranchiseeManagementBO franchiseeManagement;
	private FranchisorManagementBO franchisorManagement;
	private SupplierManagementBO supplierManagement;
	private MailManagementBO mailManegement;
	private FileManagementBO fileManegement;

	public DeliveryRequestDAO getDeliveryRequestDAO() {
		return deliveryRequestDAO;
	}

	public void setDeliveryRequestDAO(DeliveryRequestDAO deliveryRequestDAO) {
		this.deliveryRequestDAO = deliveryRequestDAO;
	}

	public DeliveryRequestHistoryDAO getDeliveryRequestHistoryDAO() {
		return deliveryRequestHistoryDAO;
	}

	public void setDeliveryRequestHistoryDAO(DeliveryRequestHistoryDAO deliveryRequestHistoryDAO) {
		this.deliveryRequestHistoryDAO = deliveryRequestHistoryDAO;
	}

	public ProductManagementBO getProductManagement() {
		return productManagement;
	}

	public void setProductManagement(ProductManagementBO productManagement) {
		this.productManagement = productManagement;
	}

	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManagement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}

	public SupplierManagementBO getSupplierManagement() {
		return supplierManagement;
	}

	public void setSupplierManagement(SupplierManagementBO supplierManagement) {
		this.supplierManagement = supplierManagement;
	}

	public MailManagementBO getMailManegement() {
		return mailManegement;
	}

	public void setMailManegement(MailManagementBO mailManegement) {
		this.mailManegement = mailManegement;
	}

	public FileManagementBO getFileManegement() {
		return fileManegement;
	}

	public void setFileManegement(FileManagementBO fileManegement) {
		this.fileManegement = fileManegement;
	}

	@Override
	public DeliveryRequestData createDeliveryRequest(Long franchiseeId, Long productId, Long productSizeId, Long quantity, String domainKey) {
	
		ProductData product = this.productManagement.getProduct(productId);
		ProductSizeData productSize = this.productManagement.getProductSize(productSizeId);
		
		if (!productSize.getIsAvailable()) {
			//check product size available
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage("O item selecionado não está disponível");
			throw new BusinessException(rm);
		}
		
		if (product.getType() == ProductTypeEnum.WITH_STOCK_CONTROL && !product.getAllowNegativeStock()) {
			//check product stock
			Long currentStock = this.productManagement.calculateProductSizeStock(productSize);
			if (currentStock < quantity) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
				rm.setMessage("A quantidade solicitada não pode ser maior que a quantidade em estoque");
				throw new BusinessException(rm);
			}
		}
		
		DeliveryRequestData deliveryRequest = new DeliveryRequestData();
		deliveryRequest.setDate(DateUtil.getCurrentDate());
		deliveryRequest.setSupplierId(product.getSupplierId());
		deliveryRequest.setFranchiseeId(franchiseeId);
		deliveryRequest.setProductId(productId);
		deliveryRequest.setProductSizeId(productSizeId);
		deliveryRequest.setQuantity(quantity);
		deliveryRequest.setDeliveryUnitPrice(productSize.getUnitPrice());
		deliveryRequest.setStatus(DeliveryRequestStatusEnum.PENDING);
		deliveryRequest.setUpdateDate(DateUtil.getCurrentDate());
		deliveryRequest.setNotificationStatus(MailNotificationStatusEnum.PENDING);
		deliveryRequest.setDomainKey(domainKey);
		
		DeliveryRequestData result = this.deliveryRequestDAO.save(deliveryRequest);
		
		//send mail to supplier
		//this.mailManegement.sendNewDeliveryRequest(result);
		
		//send mail to franchisor if stock below the minimum
		/*Long newStock = currentStock - quantity;
		if (newStock < product.getMinStock()) {
			this.mailManegement.sendStockBelowMinimun(product);
		}*/
		
		return result;
	}

	@Override
	public List<DeliveryRequestData> getDeliveryRequests() {
		return deliveryRequestDAO.listAll();
	}
	
	@Override
	public DeliveryRequestData getDeliveryRequest(Long id) {
		return deliveryRequestDAO.findById(id);
	}
	
	@Override
	public DeliveryRequestHistoryData getDeliveryRequestHistory(Long id) {
		return deliveryRequestHistoryDAO.findById(id);
	}
	
	@Override
	public DeliveryRequestPagedResponse getDeliveryRequestsByFranchiseeId(Long franchiseeId, Long pageSize, String cursorString) {
		DeliveryRequestPagedResponse result = null;
		Long fechPageSize = pageSize;
		
		if (cursorString == null || !cursorString.startsWith("HISTORY")) {
			
			//fetch MAIN table
			result = deliveryRequestDAO.findByFranchiseeId(franchiseeId, fechPageSize, cursorString);
			
			//check returned records
			if (result.getDeliveryRequests() != null) {
				if (result.getDeliveryRequests().size() == fechPageSize) {
					//fetched already enough records
					//return result without additional processing
					return result;
				} else {
					//prepare additional fetch to get records from history
					//set new page to get only missing number of records
					fechPageSize -= result.getDeliveryRequests().size();
				}
			} else {
				//no records found. initialize with empty array
				result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());				
			}
			
			//clear cursor, as will proceed and fetch HISTORY entity for additional records
			cursorString = null;
		} else {
			
			//prepare fetch from HISTORY Data
			cursorString = cursorString.replace("HISTORY", "");
			
			//initialize response as will go directly to HISTORY
			result = new DeliveryRequestPagedResponse();
			result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());
		}
		
		//fetch HISTORY table
		DeliveryRequestHistoryPagedResponse historyResult = deliveryRequestHistoryDAO.findByFranchiseeId(franchiseeId, fechPageSize, cursorString);
		if (historyResult.getDeliveryRequests() != null){
			
			//update cursor to point directly to History table (for next pages)
			result.setCursorString("HISTORY"+historyResult.getCursorString());
			
			//add new records to result
			for (DeliveryRequestHistoryData deliveryRequest: historyResult.getDeliveryRequests()){
				result.getDeliveryRequests().add(this.convertToDeliveryRequest(deliveryRequest));
			}
		}
		
		//clear cursor if no more records to fetch
		if (result.getDeliveryRequests().size() < pageSize) {
			result.setCursorString(null);
		}
		
		return result;
	}
	
	@Override
	public DeliveryRequestPagedResponse getDeliveryRequestsByProductIdAndFranchiseeId(Long productId, Long franchiseeId, Long pageSize, String cursorString) {
		
		DeliveryRequestPagedResponse result = null;
		Long fechPageSize = pageSize;
		
		if (cursorString == null || !cursorString.startsWith("HISTORY")) {
			
			//fetch MAIN table
			result = deliveryRequestDAO.findByProductIdAndFranchiseeId(productId, franchiseeId, fechPageSize, cursorString);
			
			//check returned records
			if (result.getDeliveryRequests() != null) {
				if (result.getDeliveryRequests().size() == fechPageSize) {
					//fetched already enough records
					//return result without additional processing
					return result;
				} else {
					//prepare additional fetch to get records from history
					//set new page to get only missing number of records
					fechPageSize -= result.getDeliveryRequests().size();
				}
			} else {
				//no records found. initialize with empty array
				result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());				
			}
			
			//clear cursor, as will proceed and fetch HISTORY entity for additional records
			cursorString = null;
		} else {
			
			//prepare fetch from HISTORY Data
			cursorString = cursorString.replace("HISTORY", "");
			
			//initialize response as will go directly to HISTORY
			result = new DeliveryRequestPagedResponse();
			result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());
		}
		
		//fetch HISTORY table
		DeliveryRequestHistoryPagedResponse historyResult = deliveryRequestHistoryDAO.findByProductIdAndFranchiseeId(productId, franchiseeId, fechPageSize, cursorString);
		if (historyResult.getDeliveryRequests() != null){
			
			//update cursor to point directly to History table (for next pages)
			result.setCursorString("HISTORY"+historyResult.getCursorString());
			
			//add new records to result
			for (DeliveryRequestHistoryData deliveryRequest: historyResult.getDeliveryRequests()){
				result.getDeliveryRequests().add(this.convertToDeliveryRequest(deliveryRequest));
			}
		}
		
		//clear cursor if no more records to fetch
		if (result.getDeliveryRequests().size() < pageSize) {
			result.setCursorString(null);
		}
		
		return result;
	}
	
	@Override
	public DeliveryRequestPagedResponse getDeliveryRequestsBySupplierIdAndFranchiseeIdAndStatus(Long supplierId, Long franchiseeId, DeliveryRequestStatusEnum status, Long pageSize, String cursorString) {
		DeliveryRequestPagedResponse result = null;
		Long fechPageSize = pageSize;
		
		if (cursorString == null || !cursorString.startsWith("HISTORY")) {
			
			//fetch MAIN table
			result = deliveryRequestDAO.findBySupplierIdAndFranchiseeIdAndStatus(supplierId, franchiseeId, status, fechPageSize, cursorString);
			
			//check returned records
			if (result.getDeliveryRequests() != null) {
				if (result.getDeliveryRequests().size() == fechPageSize) {
					//fetched already enough records
					//return result without additional processing
					return result;
				} else {
					//prepare additional fetch to get records from history
					//set new page to get only missing number of records
					fechPageSize -= result.getDeliveryRequests().size();
				}
			} else {
				//no records found. initialize with empty array
				result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());				
			}
			
			//clear cursor, as will proceed and fetch HISTORY entity for additional records
			cursorString = null;
		} else {
			
			//prepare fetch from HISTORY Data
			cursorString = cursorString.replace("HISTORY", "");
			
			//initialize response as will go directly to HISTORY
			result = new DeliveryRequestPagedResponse();
			result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());
		}
		
		//fetch HISTORY table
		DeliveryRequestHistoryPagedResponse historyResult = deliveryRequestHistoryDAO.findBySupplierIdAndFranchiseeIdAndStatus(supplierId, franchiseeId, status, fechPageSize, cursorString);
		if (historyResult.getDeliveryRequests() != null){
			
			//update cursor to point directly to History table (for next pages)
			result.setCursorString("HISTORY"+historyResult.getCursorString());
			
			//add new records to result
			for (DeliveryRequestHistoryData deliveryRequest: historyResult.getDeliveryRequests()){
				result.getDeliveryRequests().add(this.convertToDeliveryRequest(deliveryRequest));
			}
		}
		
		//clear cursor if no more records to fetch
		if (result.getDeliveryRequests().size() < pageSize) {
			result.setCursorString(null);
		}
		
		return result;
	}
	
	@Override
	public DeliveryRequestPagedResponse getDeliveryRequestsByProductId(Long productId, Long pageSize, String cursorString) {
		
		DeliveryRequestPagedResponse result = null;
		Long fechPageSize = pageSize;
		
		if (cursorString == null || !cursorString.startsWith("HISTORY")) {
			
			//fetch MAIN table
			result = deliveryRequestDAO.findByProductId(productId, fechPageSize, cursorString);
			
			//check returned records
			if (result.getDeliveryRequests() != null) {
				if (result.getDeliveryRequests().size() == fechPageSize) {
					//fetched already enough records
					//return result without additional processing
					return result;
				} else {
					//prepare additional fetch to get records from history
					//set new page to get only missing number of records
					fechPageSize -= result.getDeliveryRequests().size();
				}
			} else {
				//no records found. initialize with empty array
				result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());				
			}
			
			//clear cursor, as will proceed and fetch HISTORY entity for additional records
			cursorString = null;
		} else {
			
			//prepare fetch from HISTORY Data
			cursorString = cursorString.replace("HISTORY", "");
			
			//initialize response as will go directly to HISTORY
			result = new DeliveryRequestPagedResponse();
			result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());
		}
		
		//fetch HISTORY table
		DeliveryRequestHistoryPagedResponse historyResult = deliveryRequestHistoryDAO.findByProductId(productId, fechPageSize, cursorString);
		if (historyResult.getDeliveryRequests() != null){
			
			//update cursor to point directly to History table (for next pages)
			result.setCursorString("HISTORY"+historyResult.getCursorString());
			
			//add new records to result
			for (DeliveryRequestHistoryData deliveryRequest: historyResult.getDeliveryRequests()){
				result.getDeliveryRequests().add(this.convertToDeliveryRequest(deliveryRequest));
			}
		}
		
		//clear cursor if no more records to fetch
		if (result.getDeliveryRequests().size() < pageSize) {
			result.setCursorString(null);
		}
		
		return result;
	}
	
	@Override
	public List<DeliveryRequestData> getDeliveryRequestsBySupplierId(Long supplierId) {
		return deliveryRequestDAO.findBySupplierId(supplierId);
	}
	
	@Override
	public DeliveryRequestStatusResponseData updateDeliveryRequestStatus( List<DeliveryRequestStatusData> deliveryRequests, DeliveryRequestStatusEnum status, String cancellationComment, Date sentDate, Date deadlineDate, String carrierName, String trackingCode, String fiscalNumber, Date dueDate, Date autoCancellationDate, Long paymentSlipId, Long fiscalFileId, UserProfileData user) {
		
		DeliveryRequestStatusResponseData response = new DeliveryRequestStatusResponseData();
		Map<Long, DeliveryRequestData> originalDeliveryRequests = new HashMap<Long, DeliveryRequestData>();
				
		/*
		 * Validation
		 */
		for(DeliveryRequestStatusData deliveryRequest: deliveryRequests) {
			
			//get original delivery request from datastore, to validate against datastore values
			DeliveryRequestData originalDeliveryRequest = deliveryRequestDAO.findById(deliveryRequest.getDeliveryRequestId());
			originalDeliveryRequests.put(originalDeliveryRequest.getId(), originalDeliveryRequest);
			
			//validation for quantity and delivery unit price change
			if (status != DeliveryRequestStatusEnum.CANCELLED) {
			
				//Do not allow update quantity to null or zero
				if (deliveryRequest.getQuantity() == null || deliveryRequest.getQuantity().equals(0L)) {
					log.info("Invalid quantity received " + deliveryRequest.getQuantity());
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
					rm.setMessage("A quantidade informada não pode ser vazia e deve ser maior do que zero.");
					throw new BusinessException(rm);	
				}
				
				//Do not allow update quantity to amount grater than original amount
				if (deliveryRequest.getQuantity() > originalDeliveryRequest.getQuantity()) {
					log.info("Unable to update quantity " + deliveryRequest.getQuantity() + " as it is grater than original quantity " + originalDeliveryRequest.getQuantity());
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
					rm.setMessage("A quantidade informada não pode ser superior a quantidade original do pedido.");
					throw new BusinessException(rm);
				}
				
				//Do not allow update quantity if status is not completed
				if (!deliveryRequest.getQuantity().equals(originalDeliveryRequest.getQuantity())  && status != DeliveryRequestStatusEnum.COMPLETED) {
					log.info("Unable to update quantity " + originalDeliveryRequest.getQuantity() + " to " +deliveryRequest.getQuantity() + " as the status is " + status);
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
					rm.setMessage("Só é permitido alterar a quantidade de um pedido ao atualizar para \"Enviado\".");
					throw new BusinessException(rm);
				}
				
				//Do not allow update delivery unit price if status is not completed
				if (!deliveryRequest.getDeliveryUnitPrice().equals(originalDeliveryRequest.getDeliveryUnitPrice())  && status != DeliveryRequestStatusEnum.COMPLETED) {
					log.info("Unable to update deliveryUnitPrice " + originalDeliveryRequest.getDeliveryUnitPrice() + " to " +deliveryRequest.getDeliveryUnitPrice() + " as the status is " + status);
					ReturnMessage rm = new ReturnMessage();
					rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
					rm.setMessage("Só é permitido alterar o valor unitário de um pedido ao atualizar para \"Enviado\".");
					throw new BusinessException(rm);
				}				
			}
		
		} 
		
		/*
		 * Processing
		 */
		
		//a payment slip was added
		Set<String> replacedPaymentSlips = new HashSet<String>();
		UploadedFileData uploadedPaymentSlip = null;
		if (paymentSlipId != null && paymentSlipId != 0L) {	    	
			uploadedPaymentSlip = fileManegement.getUploadedFile(paymentSlipId);	    	
		}
		
		//a fiscal file was added
		Set<String> replacedFiscalFiles = new HashSet<String>();
		UploadedFileData uploadedFiscalFile = null;
		if (fiscalFileId != null && fiscalFileId != 0L) {	    	
			uploadedFiscalFile = fileManegement.getUploadedFile(fiscalFileId);	    	
		}
		
		// Create 2 separate arrays, to post operations in bulk to the datastore
		List<DeliveryRequestData> deliveryRequestsToCreate = new ArrayList<DeliveryRequestData>();
		List<DeliveryRequestData> deliveryRequestsToUpdate = new ArrayList<DeliveryRequestData>();
		for(DeliveryRequestStatusData deliveryRequest: deliveryRequests) {
			
			DeliveryRequestData originalDeliveryRequest = originalDeliveryRequests.get(deliveryRequest.getDeliveryRequestId());
			
			//completed updates
		    if (status == DeliveryRequestStatusEnum.COMPLETED) {
		        
		        // Create new delivery request in case of partial delivery
		        // Update current delivery request with the quantity delivered
		        // and create new delivery request with the quantity difference
		        if (deliveryRequest.getQuantity() < originalDeliveryRequest.getQuantity()) {
		        	Long remainingQuantity = originalDeliveryRequest.getQuantity() - deliveryRequest.getQuantity();
		        	log.info("Partial delivery: " + deliveryRequest.getQuantity() + ". Create delivery request copy with quantity difference: " + remainingQuantity);
		        	
		        	//Create new Delivery Request with remaining quantity
		        	DeliveryRequestData newDeliveryRequest = new DeliveryRequestData();
		        	newDeliveryRequest.setDate(originalDeliveryRequest.getDate());
		        	newDeliveryRequest.setSupplierId(originalDeliveryRequest.getSupplierId());
		        	newDeliveryRequest.setFranchiseeId(originalDeliveryRequest.getFranchiseeId());
		        	newDeliveryRequest.setProductId(originalDeliveryRequest.getProductId());
		        	newDeliveryRequest.setProductSizeId(originalDeliveryRequest.getProductSizeId());
		        	newDeliveryRequest.setDeliveryUnitPrice(originalDeliveryRequest.getDeliveryUnitPrice());	        	
		        	newDeliveryRequest.setQuantity(remainingQuantity);
		        	newDeliveryRequest.setStatus(originalDeliveryRequest.getStatus());
		        	newDeliveryRequest.setUpdateDate(DateUtil.getCurrentDate());
		        	newDeliveryRequest.setNotificationStatus(originalDeliveryRequest.getNotificationStatus());
		        	newDeliveryRequest.setCancellationComment(originalDeliveryRequest.getCancellationComment());
		        	newDeliveryRequest.setAutoCancellationDate(originalDeliveryRequest.getAutoCancellationDate());
		        	newDeliveryRequest.setSentDate(originalDeliveryRequest.getSentDate());
		        	newDeliveryRequest.setDeadlineDate(originalDeliveryRequest.getDeadlineDate());
		        	newDeliveryRequest.setCarrierName(originalDeliveryRequest.getCarrierName());
		        	newDeliveryRequest.setTrackingCode(originalDeliveryRequest.getTrackingCode());
		        	newDeliveryRequest.setFiscalNumber(originalDeliveryRequest.getFiscalNumber());
		        	newDeliveryRequest.setFiscalFileContentType(originalDeliveryRequest.getFiscalFileContentType());
		        	newDeliveryRequest.setFiscalFileStorePath(originalDeliveryRequest.getFiscalFileStorePath());
		        	newDeliveryRequest.setFiscalFileKey(originalDeliveryRequest.getFiscalFileKey());
		        	newDeliveryRequest.setFiscalFileName(originalDeliveryRequest.getFiscalFileName());
		        	newDeliveryRequest.setDueDate(originalDeliveryRequest.getDueDate());
		        	newDeliveryRequest.setPaymentSlipContentType(originalDeliveryRequest.getPaymentSlipContentType());
		        	newDeliveryRequest.setPaymentSlipStorePath(originalDeliveryRequest.getPaymentSlipStorePath());
		        	newDeliveryRequest.setPaymentSlipKey(originalDeliveryRequest.getPaymentSlipKey());
					newDeliveryRequest.setPaymentSlipName(originalDeliveryRequest.getPaymentSlipName());

					Gson gson = new Gson();
					String strStatusHistory = gson.toJson(originalDeliveryRequest.getStatusHistory());
					DeliveryRequestStatusHistoryData statusHistory = gson.fromJson(strStatusHistory, DeliveryRequestStatusHistoryData.class);
					newDeliveryRequest.setStatusHistory(statusHistory);
					
		        	deliveryRequestsToCreate.add(newDeliveryRequest);
		        	
		        	//Update delivery with delivered quantity
		        	originalDeliveryRequest.setQuantity(deliveryRequest.getQuantity());	        	

		        }
		        
		        originalDeliveryRequest.setSentDate(sentDate);
		        originalDeliveryRequest.setDeadlineDate(deadlineDate);
		        originalDeliveryRequest.setCarrierName(carrierName);
		        originalDeliveryRequest.setTrackingCode(trackingCode);
		        originalDeliveryRequest.setFiscalNumber(fiscalNumber);
		        originalDeliveryRequest.setDeliveryUnitPrice(deliveryRequest.getDeliveryUnitPrice());
		        
		        if (uploadedFiscalFile != null) {
			    	if (originalDeliveryRequest.getFiscalFileKey() != null) {
			    		replacedFiscalFiles.add(originalDeliveryRequest.getFiscalFileKey());
			    	}		    	
			    	
			    	originalDeliveryRequest.setFiscalFileContentType(uploadedFiscalFile.getContentType());
			    	originalDeliveryRequest.setFiscalFileStorePath(uploadedFiscalFile.getStorePath());
			    	originalDeliveryRequest.setFiscalFileKey(uploadedFiscalFile.getFileKey());
			    	originalDeliveryRequest.setFiscalFileName(uploadedFiscalFile.getName());
			    }
		        
			} else {
				
				if (originalDeliveryRequest.getFiscalFileKey() != null) {
		    		replacedFiscalFiles.add(originalDeliveryRequest.getFiscalFileKey());
		    	}
				
			    originalDeliveryRequest.setSentDate(null);
			    originalDeliveryRequest.setDeadlineDate(null);
			    originalDeliveryRequest.setCarrierName(null);
			    originalDeliveryRequest.setTrackingCode(null);
			    originalDeliveryRequest.setFiscalNumber(null);
			    originalDeliveryRequest.setFiscalFileContentType(null);
			    originalDeliveryRequest.setFiscalFileStorePath(null);
			    originalDeliveryRequest.setFiscalFileKey(null);
			    originalDeliveryRequest.setFiscalFileName(null);
			    
			}
		    
		    //cancellation update
			if (status == DeliveryRequestStatusEnum.CANCELLED) {
				
				if (user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE)) {
					cancellationComment = "Cancelado pelo usuário: " + cancellationComment;
					if (originalDeliveryRequest.getNotificationStatus().equals(MailNotificationStatusEnum.SENT)) {
						originalDeliveryRequest.setNotificationStatus(MailNotificationStatusEnum.PENDING_CANCELLATION);
					} else {
						originalDeliveryRequest.setNotificationStatus(MailNotificationStatusEnum.CANCELLED);
					}
				}
				
				originalDeliveryRequest.setCancellationComment(cancellationComment);
			} else {
				originalDeliveryRequest.setCancellationComment(null);
			}
			
			//pending with restriction updates
		    if (status == DeliveryRequestStatusEnum.PENDING_WITH_RESTRICTION) {
		        originalDeliveryRequest.setAutoCancellationDate(autoCancellationDate);
		    } else {
		    	originalDeliveryRequest.setAutoCancellationDate(null);
		    }
		    
		    if (status == DeliveryRequestStatusEnum.COMPLETED || status == DeliveryRequestStatusEnum.WAITING_PAYMENT) {
		        
		    	if (dueDate != null) {
			    	originalDeliveryRequest.setDueDate(dueDate);
		    	}

			    if (uploadedPaymentSlip != null) {
			    	if (originalDeliveryRequest.getPaymentSlipKey() != null) {
			    		replacedPaymentSlips.add(originalDeliveryRequest.getPaymentSlipKey());
			    	}		    	
			    	
			    	originalDeliveryRequest.setPaymentSlipContentType(uploadedPaymentSlip.getContentType());
			    	originalDeliveryRequest.setPaymentSlipStorePath(uploadedPaymentSlip.getStorePath());
			    	originalDeliveryRequest.setPaymentSlipKey(uploadedPaymentSlip.getFileKey());
			    	originalDeliveryRequest.setPaymentSlipName(uploadedPaymentSlip.getName());
			    }
			    
			}
			
			//Add new status change history
			if (!originalDeliveryRequest.getStatus().equals(status)) {
				DeliveryRequestStatusHistoryData statusHistory = originalDeliveryRequest.getStatusHistory();
				if (statusHistory == null) {
					statusHistory = new DeliveryRequestStatusHistoryData();
					statusHistory.setItems(new ArrayList<DeliveryRequestStatusChangeData>());
					originalDeliveryRequest.setStatusHistory(statusHistory);
				}

				DeliveryRequestStatusChangeData statusChange = new DeliveryRequestStatusChangeData();
				statusChange.setDate(DateUtil.getCurrentDate());
				statusChange.setUserId(user.getId());
				statusChange.setPreviusStatus(originalDeliveryRequest.getStatus());
				statusChange.setNewStatus(status);
				statusHistory.getItems().add(statusChange);
			}
			
			//Update the status info
			originalDeliveryRequest.setStatus(status);
			originalDeliveryRequest.setUpdateDate(DateUtil.getCurrentDate());
			deliveryRequestsToUpdate.add(originalDeliveryRequest);
		}
		
		//Post updates to the datastore and save response
		if (!deliveryRequestsToUpdate.isEmpty()) {
			response.setDeliveryRequests(deliveryRequestDAO.saveAll(deliveryRequestsToUpdate));
		} else {
			response.setDeliveryRequests(deliveryRequestsToUpdate);
		}
		if (!deliveryRequestsToCreate.isEmpty()) {
			response.setNewDeliveryRequests(deliveryRequestDAO.saveAll(deliveryRequestsToCreate));
		} else {
			response.setNewDeliveryRequests(deliveryRequestsToCreate);
		}
		
		//Remove payment slip from uploaded file table
		if (uploadedPaymentSlip != null) {			
			fileManegement.deleteUploadedFile(uploadedPaymentSlip.getId());
		}
		
		//trigger cleanup for replaced payment slips
		if (replacedPaymentSlips.size() > 0) {
			for (String paymentSlipKey: replacedPaymentSlips) {
				//Asynchronously check/delete replaced payment slip
				Queue queue = QueueFactory.getDefaultQueue();
				queue.add(TaskOptions.Builder.withUrl("/job/async/checkAndDeleteReplacedPaymentSlip/" + paymentSlipKey).countdownMillis(30000));
			}
		}
		
		//Remove fiscal file from uploaded file table
		if (uploadedFiscalFile != null) {
			fileManegement.deleteUploadedFile(uploadedFiscalFile.getId());
		}
		
		//trigger cleanup for replaced fiscal files
		if (replacedFiscalFiles.size() > 0) {
			for (String fiscalFileKey: replacedFiscalFiles) {
				//Asynchronously check/delete replaced fiscal files
				Queue queue = QueueFactory.getDefaultQueue();
				queue.add(TaskOptions.Builder.withUrl("/job/async/checkAndDeleteReplacedFiscalFile/" + fiscalFileKey).countdownMillis(30000));
			}
		}
			
		return response;
	}
	
	@Override
	public List<DeliveryRequestData> updateNotificationStatus(List<DeliveryRequestData> deliveryRequests, MailNotificationStatusEnum notificationStatus) {
		
		for (DeliveryRequestData deliveryRequest: deliveryRequests){
			deliveryRequest.setNotificationStatus(notificationStatus);
			deliveryRequest.setUpdateDate(DateUtil.getCurrentDate());
		}

		return  deliveryRequestDAO.saveAll(deliveryRequests);
	}

	@Override
	public List<EnumValidValueResponseData> getStatuses() {
		List<EnumValidValueResponseData> result = new ArrayList<EnumValidValueResponseData>();
		
		for (DeliveryRequestStatusEnum value: DeliveryRequestStatusEnum.values()) {
			EnumValidValueResponseData enumValue = new EnumValidValueResponseData();
			enumValue.setKey(value.name());
			enumValue.setValue(value.getDescription());
			result.add(enumValue);
		}

		return result;
	}

	@Override
	public List<DeliveryRequestData> getNotCancelledByProductSizeId(Long productSizeId) {
		return deliveryRequestDAO.findNotCancelledByProductSizeId(productSizeId);
	}

	@Override
	public List<DeliveryRequestData> deleteDeliveryRequestsByProductId(Long productId) {
		
		List<DeliveryRequestData> deliveryRequestList = deliveryRequestDAO.findByProductId(productId);
		if(deliveryRequestList != null && !deliveryRequestList.isEmpty()) {
			for(DeliveryRequestData deliveryRequest: deliveryRequestList) {
				deliveryRequest = deliveryRequestDAO.delete(deliveryRequest.getId());
			}
		}
		
		List<DeliveryRequestHistoryData> deliveryRequestHistoryList = deliveryRequestHistoryDAO.findByProductId(productId);
		if(deliveryRequestHistoryList != null && !deliveryRequestHistoryList.isEmpty()) {
			for(DeliveryRequestHistoryData deliveryRequestHistory: deliveryRequestHistoryList) {
				deliveryRequestHistory = deliveryRequestHistoryDAO.delete(deliveryRequestHistory.getId());
				deliveryRequestList.add(this.convertToDeliveryRequest(deliveryRequestHistory));
			}
		}
		
		return deliveryRequestList;
	}

	@Override
	public List<DeliveryRequestData> getDeliveryRequestsBySupplierIdAndStatus(Long supplierId, DeliveryRequestStatusEnum deliveryStatus) {
		return deliveryRequestDAO.findBySupplierIdAndStatus(supplierId, deliveryStatus);
	}

	@Override
	public Map<Long, ProductData> getDeliveryRequestsProducts(List<DeliveryRequestData> deliveryRequests) {
		Map<Long, ProductData> response = new HashMap<Long, ProductData>();
		
		if(deliveryRequests != null && !deliveryRequests.isEmpty()) {
			for(DeliveryRequestData deliveryRequest: deliveryRequests) {
				if (!response.containsKey(deliveryRequest.getProductId())){
					response.put(deliveryRequest.getProductId(), productManagement.getProduct(deliveryRequest.getProductId()));					
				}
			}
		}
		
		return response;
	}
	
	@Override
	public Map<Long, ProductData> getDeliveryRequestHistoriesProducts(List<DeliveryRequestHistoryData> deliveryRequests) {
		Map<Long, ProductData> response = new HashMap<Long, ProductData>();
		
		if(deliveryRequests != null && !deliveryRequests.isEmpty()) {
			for(DeliveryRequestHistoryData deliveryRequest: deliveryRequests) {
				if (!response.containsKey(deliveryRequest.getProductId())){
					response.put(deliveryRequest.getProductId(), productManagement.getProduct(deliveryRequest.getProductId()));					
				}
			}
		}
		
		return response;
	}
	
	@Override
	public Map<Long, SupplierData> getDeliveryRequestsSuppliers(List<DeliveryRequestData> deliveryRequests) {
		Map<Long, SupplierData> response = new HashMap<Long, SupplierData>();
		
		if(deliveryRequests != null && !deliveryRequests.isEmpty()) {
			for(DeliveryRequestData deliveryRequest: deliveryRequests) {
				if (!response.containsKey(deliveryRequest.getFranchiseeId())){
					response.put(deliveryRequest.getSupplierId(), supplierManagement.getSupplier(deliveryRequest.getSupplierId()));					
				}
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, FranchiseeData> getDeliveryRequestsFranchisees(List<DeliveryRequestData> deliveryRequests) {
		Map<Long, FranchiseeData> response = new HashMap<Long, FranchiseeData>();
		
		if(deliveryRequests != null && !deliveryRequests.isEmpty()) {
			for(DeliveryRequestData deliveryRequest: deliveryRequests) {
				if (!response.containsKey(deliveryRequest.getFranchiseeId())){
					response.put(deliveryRequest.getFranchiseeId(), franchiseeManagement.getFranchisee(deliveryRequest.getFranchiseeId()));					
				}
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, FranchisorData> getDeliveryRequestsFranchiseesFranchisors(List<FranchiseeData> franchisees) {
		Map<Long, FranchisorData> response = new HashMap<Long, FranchisorData>();
		
		if(franchisees != null && !franchisees.isEmpty()) {
			for(FranchiseeData franchisee: franchisees) {
				if (!response.containsKey(franchisee.getFranchisorId())){
					response.put(franchisee.getFranchisorId(), franchisorManagement.getFranchisor(franchisee.getFranchisorId()));					
				}
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, SupplierData> getDeliveryRequestsProductsSuppliers(List<ProductData> products) {
		Map<Long, SupplierData> response = new HashMap<Long, SupplierData>();
		
		if(products != null && !products.isEmpty()) {
			for(ProductData product: products) {
				if (!response.containsKey(product.getSupplierId())){
					response.put(product.getSupplierId(), supplierManagement.getSupplier(product.getSupplierId()));					
				}
			}
		}
		
		return response;
	}
	
	@Override
	public List<DeliveryRequestData> deleteDeliveryRequestsByProductSizeId(Long id) {
		
		List<DeliveryRequestData> deliveryRequestList = deliveryRequestDAO.findByProductSizeId(id);
		if(deliveryRequestList != null && !deliveryRequestList.isEmpty()) {
			for(DeliveryRequestData deliveryRequest: deliveryRequestList) {
				deliveryRequest = deliveryRequestDAO.delete(deliveryRequest.getId());
			}
		}
		
		List<DeliveryRequestHistoryData> deliveryRequestHistoryList = deliveryRequestHistoryDAO.findByProductSizeId(id);
		if(deliveryRequestHistoryList != null && !deliveryRequestHistoryList.isEmpty()) {
			for(DeliveryRequestHistoryData deliveryRequestHistory: deliveryRequestHistoryList) {
				deliveryRequestHistory = deliveryRequestHistoryDAO.delete(deliveryRequestHistory.getId());
				deliveryRequestList.add(this.convertToDeliveryRequest(deliveryRequestHistory));
			}
		}
		
		return deliveryRequestList;
	}

	@Override
	public List<DeliveryRequestData> getPendingConsolidation(String domainKey, Long consolidationWindow) {
		return deliveryRequestDAO.findFinishedOlderThanDays(consolidationWindow, domainKey);
	}

	@Override
	public List<DeliveryRequestData> getPendingNotifications() {
		return deliveryRequestDAO.findByNotificationStatus(MailNotificationStatusEnum.PENDING);
	}

	@Override
	public List<DeliveryRequestHistoryData> saveAllDeliveryRequestsToHistory(List<DeliveryRequestHistoryData> deliveryRequestsHistory) {
		return deliveryRequestHistoryDAO.saveAll(deliveryRequestsHistory);
	}

	@Override
	public List<DeliveryRequestData> deleteAllDeliveryRequests(List<DeliveryRequestData> deliveryRequests) {
		List<Long> ids = new ArrayList<Long>();
		for (DeliveryRequestData deliveryRequest: deliveryRequests){
			ids.add(deliveryRequest.getId());
		}
		return deliveryRequestDAO.deleteAll(ids);
	}

	@Override
	public List<DeliveryRequestHistoryData> getDeliveryRequestsHistory() {
		return deliveryRequestHistoryDAO.listAll();
	}
	
	@Override
	public DeliveryRequestData convertToDeliveryRequest(DeliveryRequestHistoryData deliveryRequestHistory) {
		try {
			return ObjectConverter.convert(deliveryRequestHistory, DeliveryRequestData.class);
		} catch (Exception e) {
			log.severe("Error during DeliveryRequestHistoryData convertion to DeliveryRequestData: " + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public DeliveryRequestHistoryData convertToDeliveryRequestHistory(DeliveryRequestData deliveryRequest) {
		try {
			return ObjectConverter.convert(deliveryRequest, DeliveryRequestHistoryData.class);
		} catch (Exception e) {
			log.severe("Error during DeliveryRequestData convertion to DeliveryRequestHistoryData: " + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<DeliveryRequestData> getDeliveryRequestsByFilter(Date initialDate, Date finalDate, Long franchiseeId, Long supplierId) {
		
		//prepare parameters for query
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date >=", initialDate);
		parameters.put("date <=", finalDate);
		if (franchiseeId != 0) {
			parameters.put("franchiseeId", franchiseeId);
		}
		if (supplierId != 0) {
			parameters.put("supplierId", supplierId);
		}
		
		//initialize response with main table requests
		List<DeliveryRequestData> response = deliveryRequestDAO.findByFilter(parameters);
		
		//add history requests in the response
		List<DeliveryRequestHistoryData> deliveryRequestHistoryList = deliveryRequestHistoryDAO.findByFilter(parameters);
		if (deliveryRequestHistoryList != null && !deliveryRequestHistoryList.isEmpty()) {
			for (DeliveryRequestHistoryData history : deliveryRequestHistoryList) {				
				response.add(convertToDeliveryRequest(history));
			}
		}		

		return response;
	}

	@Override
	public void autoCancelPendingWithRestriction() {
		
		List<DeliveryRequestData> deliveryRequestsToCancel = new ArrayList<DeliveryRequestData>();
		
		//get current date at mid-night
		String currentDayWithoutTime = DateUtil.getDate(DateUtil.getCurrentDate(), DateUtil.SLASH_PATTERN);
		Date currentDay = DateUtil.getDate(currentDayWithoutTime, DateUtil.SLASH_PATTERN);
		log.info("Current Day: " + DateUtil.getDate(currentDay));
		
		DeliveryRequestPagedResponse deliveryRequestPagedResponse = deliveryRequestDAO.findByStatus(DeliveryRequestStatusEnum.PENDING_WITH_RESTRICTION, null, null);
		if (deliveryRequestPagedResponse.getDeliveryRequests() != null && !deliveryRequestPagedResponse.getDeliveryRequests().isEmpty()){
			for (DeliveryRequestData deliveryRequest: deliveryRequestPagedResponse.getDeliveryRequests()){
				if (deliveryRequest.getAutoCancellationDate().before(currentDay)){
					deliveryRequest.setStatus(DeliveryRequestStatusEnum.CANCELLED);
					deliveryRequest.setCancellationComment("Cancelamento automático por restrição");
					deliveryRequestsToCancel.add(deliveryRequest);
				}
			}
			
			if (deliveryRequestsToCancel.size() > 0) {
				deliveryRequestDAO.saveAll(deliveryRequestsToCancel);
			}
			
			log.info(deliveryRequestsToCancel.size() + " DeliveryRequests canceled today!");
		} else {
			log.info("No DeliveryRequests to cancel today!");
		}
	}

	@Override
	public List<DeliveryRequestData> getPendingCancellationNotifications() {
		return deliveryRequestDAO.findByNotificationStatus(MailNotificationStatusEnum.PENDING_CANCELLATION);
	}

	@Override
	public DeliveryRequestPagedResponse getAccountsReceivable(Long supplierId, Date fromDate, Date toDate, Long pageSize, String cursorString) {

		//prepare parameters for query
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("supplierId", supplierId);
		parameters.put("dueDate >=", fromDate);
		parameters.put("dueDate <=", toDate);
		
		DeliveryRequestPagedResponse result = null;
		Long fechPageSize = pageSize;
		
		if (cursorString == null || !cursorString.startsWith("HISTORY")) {
			
			//fetch MAIN table
			result = deliveryRequestDAO.findByDueDateRange(parameters, fechPageSize, cursorString);
			
			//check returned records
			if (result.getDeliveryRequests() != null) {
				if (result.getDeliveryRequests().size() == fechPageSize) {
					//fetched already enough records
					//return result without additional processing
					return result;
				} else {
					//prepare additional fetch to get records from history
					//set new page to get only missing number of records
					fechPageSize -= result.getDeliveryRequests().size();
				}
			} else {
				//no records found. initialize with empty array
				result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());				
			}
			
			//clear cursor, as will proceed and fetch HISTORY entity for additional records
			cursorString = null;
		} else {
			
			//prepare fetch from HISTORY Data
			cursorString = cursorString.replace("HISTORY", "");
			
			//initialize response as will go directly to HISTORY
			result = new DeliveryRequestPagedResponse();
			result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());
		}
		
		//fetch HISTORY table
		DeliveryRequestHistoryPagedResponse historyResult = deliveryRequestHistoryDAO.findByDueDateRange(parameters, fechPageSize, cursorString);
		if (historyResult.getDeliveryRequests() != null){
			
			//update cursor to point directly to History table (for next pages)
			result.setCursorString("HISTORY"+historyResult.getCursorString());
			
			//add new records to result
			for (DeliveryRequestHistoryData deliveryRequest: historyResult.getDeliveryRequests()){
				result.getDeliveryRequests().add(this.convertToDeliveryRequest(deliveryRequest));
			}
		}
		
		//clear cursor if no more records to fetch
		if (result.getDeliveryRequests().size() < pageSize) {
			result.setCursorString(null);
		}
		
		return result;
	}

	@Override
	public DeliveryRequestPagedResponse getAccountsPayable(Long franchiseeId, Date fromDate, Date toDate, long pageSize, String cursorString) {
		
		//prepare parameters for query
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("franchiseeId", franchiseeId);
		parameters.put("dueDate >=", fromDate);
		parameters.put("dueDate <=", toDate);
		
		DeliveryRequestPagedResponse result = null;
		Long fechPageSize = pageSize;
		
		if (cursorString == null || !cursorString.startsWith("HISTORY")) {
			
			//fetch MAIN table
			result = deliveryRequestDAO.findByDueDateRange(parameters, fechPageSize, cursorString);
			
			//check returned records
			if (result.getDeliveryRequests() != null) {
				if (result.getDeliveryRequests().size() == fechPageSize) {
					//fetched already enough records
					//return result without additional processing
					return result;
				} else {
					//prepare additional fetch to get records from history
					//set new page to get only missing number of records
					fechPageSize -= result.getDeliveryRequests().size();
				}
			} else {
				//no records found. initialize with empty array
				result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());				
			}
			
			//clear cursor, as will proceed and fetch HISTORY entity for additional records
			cursorString = null;
		} else {
			
			//prepare fetch from HISTORY Data
			cursorString = cursorString.replace("HISTORY", "");
			
			//initialize response as will go directly to HISTORY
			result = new DeliveryRequestPagedResponse();
			result.setDeliveryRequests(new ArrayList<DeliveryRequestData>());
		}
		
		//fetch HISTORY table
		DeliveryRequestHistoryPagedResponse historyResult = deliveryRequestHistoryDAO.findByDueDateRange(parameters, fechPageSize, cursorString);
		if (historyResult.getDeliveryRequests() != null){
			
			//update cursor to point directly to History table (for next pages)
			result.setCursorString("HISTORY"+historyResult.getCursorString());
			
			//add new records to result
			for (DeliveryRequestHistoryData deliveryRequest: historyResult.getDeliveryRequests()){
				result.getDeliveryRequests().add(this.convertToDeliveryRequest(deliveryRequest));
			}
		}
		
		//clear cursor if no more records to fetch
		if (result.getDeliveryRequests().size() < pageSize) {
			result.setCursorString(null);
		}
		
		return result;
	}

	@Override
	public void checkAndDeleteReplacedPaymentSlip(String paymentSlipKey) {
		List<DeliveryRequestData> deliveryRequests = deliveryRequestDAO.findByPaymentSlipKey(paymentSlipKey);
		if (deliveryRequests.size() == 0) {
			List<DeliveryRequestHistoryData> deliveryRequestHistories = deliveryRequestHistoryDAO.findByPaymentSlipKey(paymentSlipKey);
			if (deliveryRequestHistories.size() == 0) {
				//If there is no delivery request neither history referencing the document, it can be deleted
				log.info("No delivery request found for paymentSlipKey " + paymentSlipKey + ". Deleting file content!");
				fileManegement.deleteFile(paymentSlipKey);
			} else {
				log.fine("Found delivery requests history for paymentSlipKey " + paymentSlipKey);
			}
		} else {
			log.fine("Found delivery requests for paymentSlipKey " + paymentSlipKey);
		}
		
	}
	
	@Override
	public void checkAndDeleteReplacedFiscalFile(String fiscalFileKey) {
		List<DeliveryRequestData> deliveryRequests = deliveryRequestDAO.findByFiscalFileKey(fiscalFileKey);
		if (deliveryRequests.size() == 0) {
			List<DeliveryRequestHistoryData> deliveryRequestHistories = deliveryRequestHistoryDAO.findByFiscalFileKey(fiscalFileKey);
			if (deliveryRequestHistories.size() == 0) {
				//If there is no delivery request neither history referencing the document, it can be deleted
				log.info("No delivery request found for fiscalFileKey " + fiscalFileKey + ". Deleting file content!");
				fileManegement.deleteFile(fiscalFileKey);
			} else {
				log.fine("Found delivery requests history for fiscalFileKey " + fiscalFileKey);
			}
		} else {
			log.fine("Found delivery requests for fiscalFileKey " + fiscalFileKey);
		}
		
	}

}
