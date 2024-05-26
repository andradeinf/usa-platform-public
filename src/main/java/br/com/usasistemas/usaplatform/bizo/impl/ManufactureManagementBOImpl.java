package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.api.response.data.ManufactureRequestResponseData;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.ManufactureManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.dao.ManufactureRequestDAO;
import br.com.usasistemas.usaplatform.dao.ManufactureRequestHistoryDAO;
import br.com.usasistemas.usaplatform.dao.response.ManufactureRequestHistoryPagedResponse;
import br.com.usasistemas.usaplatform.dao.response.ManufactureRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.converter.ObjectConverter;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.ManufactureRequestStatusEnum;

public class ManufactureManagementBOImpl implements ManufactureManagementBO {
	
	private static final Logger log = Logger.getLogger(ManufactureManagementBOImpl.class.getName());
	private ManufactureRequestDAO manufactureRequestDAO;
	private ManufactureRequestHistoryDAO manufactureRequestHistoryDAO;
	private ProductManagementBO productManagement;
	private FranchisorManagementBO franchisorManagement;

	public ManufactureRequestDAO getManufactureRequestDAO() {
		return manufactureRequestDAO;
	}

	public void setManufactureRequestDAO(ManufactureRequestDAO manufactureRequestDAO) {
		this.manufactureRequestDAO = manufactureRequestDAO;
	}

	public ManufactureRequestHistoryDAO getManufactureRequestHistoryDAO() {
		return manufactureRequestHistoryDAO;
	}

	public void setManufactureRequestHistoryDAO(
			ManufactureRequestHistoryDAO manufactureRequestHistoryDAO) {
		this.manufactureRequestHistoryDAO = manufactureRequestHistoryDAO;
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

	@Override
	public ManufactureRequestData createManufactureRequest(Long franchisorId, Long productId, Long productSizeId, Long quantity, Boolean addStockOnly, String domainKey) {
		ProductData product = this.productManagement.getProduct(productId);
		ProductSizeData productSize = this.productManagement.getProductSize(productSizeId);
		
		ManufactureRequestData manufactureRequest = new ManufactureRequestData();
		manufactureRequest.setDate(new Date());
		manufactureRequest.setSupplierId(product.getSupplierId());
		manufactureRequest.setFranchisorId(franchisorId);
		manufactureRequest.setProductId(productId);
		manufactureRequest.setProductSizeId(productSizeId);
		manufactureRequest.setQuantity(quantity);
		manufactureRequest.setManufactureUnitPrice(productSize.getUnitPrice());
		if (addStockOnly) {
			if (quantity > 0) {
				manufactureRequest.setStatus(ManufactureRequestStatusEnum.COMPLETED);
			} else {
				manufactureRequest.setStatus(ManufactureRequestStatusEnum.ADJUSTMENT);
			}
			manufactureRequest.setNotificationStatus(MailNotificationStatusEnum.NOT_APPLICABLE);
		} else {
			manufactureRequest.setStatus(ManufactureRequestStatusEnum.PENDING);
			manufactureRequest.setNotificationStatus(MailNotificationStatusEnum.PENDING);
		}
		manufactureRequest.setUpdateDate(DateUtil.getCurrentDate());
		manufactureRequest.setDomainKey(domainKey);
				
		ManufactureRequestData result = this.manufactureRequestDAO.save(manufactureRequest);
		
		//this.mailManegement.sendNewManufactureRequest(result);
		
		return result;
	}
	
	@Override
	public ManufactureRequestPagedResponse getManufactureRequestsByFranchisorId(Long franchisorId, Long pageSize, String cursorString) {

		ManufactureRequestPagedResponse result = null;
		Long fechPageSize = pageSize;
		
		if (cursorString == null || !cursorString.startsWith("HISTORY")) {
			
			//fetch MAIN table
			result = manufactureRequestDAO.findByFranchisorId(franchisorId, fechPageSize, cursorString);
			
			//check returned records
			if (result.getManufactureRequests() != null) {
				if (result.getManufactureRequests().size() == fechPageSize) {
					//fetched already enough records
					//return result without additional processing
					return result;
				} else {
					//prepare additional fetch to get records from history
					//set new page to get only missing number of records
					fechPageSize -= result.getManufactureRequests().size();
				}
			} else {
				//no records found. initialize with empty array
				result.setManufactureRequests(new ArrayList<ManufactureRequestData>());				
			}
			
			//clear cursor, as will proceed and fetch HISTORY entity for additional records
			cursorString = null;
		} else {
			
			//prepare fetch from HISTORY Data
			cursorString = cursorString.replace("HISTORY", "");
			
			//initialize response as will go directly to HISTORY
			result = new ManufactureRequestPagedResponse();
			result.setManufactureRequests(new ArrayList<ManufactureRequestData>());
		}
		
		//fetch HISTORY table
		ManufactureRequestHistoryPagedResponse historyResult = manufactureRequestHistoryDAO.findByFranchisorId(franchisorId, fechPageSize, cursorString);
		if (historyResult.getManufactureRequests() != null){
			
			//update cursor to point directly to History table (for next pages)
			result.setCursorString("HISTORY"+historyResult.getCursorString());
			
			//add new records to result
			for (ManufactureRequestHistoryData manufactureRequest: historyResult.getManufactureRequests()){
				result.getManufactureRequests().add(this.convertToManufactureRequest(manufactureRequest));
			}
		}
		
		//clear cursor if no more records to fetch
		if (result.getManufactureRequests().size() < pageSize) {
			result.setCursorString(null);
		}
		
		return result;
	}
	
	@Override
	public ManufactureRequestPagedResponse getManufactureRequestsByProductId(Long productId, Long pageSize, String cursorString) {
		
		ManufactureRequestPagedResponse result = null;
		Long fechPageSize = pageSize;
		
		if (cursorString == null || !cursorString.startsWith("HISTORY")) {
			
			//fetch MAIN table
			result = manufactureRequestDAO.findByProductId(productId, fechPageSize, cursorString);
			
			//check returned records
			if (result.getManufactureRequests() != null) {
				if (result.getManufactureRequests().size() == fechPageSize) {
					//fetched already enough records
					//return result without additional processing
					return result;
				} else {
					//prepare additional fetch to get records from history
					//set new page to get only missing number of records
					fechPageSize -= result.getManufactureRequests().size();
				}
			} else {
				//no records found. initialize with empty array
				result.setManufactureRequests(new ArrayList<ManufactureRequestData>());				
			}
			
			//clear cursor, as will proceed and fetch HISTORY entity for additional records
			cursorString = null;
		} else {
			
			//prepare fetch from HISTORY Data
			cursorString = cursorString.replace("HISTORY", "");
			
			//initialize response as will go directly to HISTORY
			result = new ManufactureRequestPagedResponse();
			result.setManufactureRequests(new ArrayList<ManufactureRequestData>());
		}
		
		//fetch HISTORY table
		ManufactureRequestHistoryPagedResponse historyResult = manufactureRequestHistoryDAO.findByProductId(productId, fechPageSize, cursorString);
		if (historyResult.getManufactureRequests() != null){
			
			//update cursor to point directly to History table (for next pages)
			result.setCursorString("HISTORY"+historyResult.getCursorString());
			
			//add new records to result
			for (ManufactureRequestHistoryData manufactureRequest: historyResult.getManufactureRequests()){
				result.getManufactureRequests().add(this.convertToManufactureRequest(manufactureRequest));
			}
		}
		
		//clear cursor if no more records to fetch
		if (result.getManufactureRequests().size() < pageSize) {
			result.setCursorString(null);
		}
		
		return result;
	}

	@Override
	public ManufactureRequestResponseData updateManufactureRequest(ManufactureRequestData manufactureRequest) {
		manufactureRequest = manufactureRequestDAO.save(manufactureRequest);
		return prepareResponseData(manufactureRequest);
	}
	
	@Override
	public ManufactureRequestResponseData updateManufactureRequestStatus(Long manufactureRequestId, ManufactureRequestStatusEnum status, String cancellationComment) {
		ManufactureRequestData manufactureRequest = manufactureRequestDAO.findById(manufactureRequestId);		
		manufactureRequest.setStatus(status);
		manufactureRequest.setUpdateDate(DateUtil.getCurrentDate());
		
		if (status == ManufactureRequestStatusEnum.CANCELLED) {
			manufactureRequest.setCancellationComment(cancellationComment);
		} else {
			manufactureRequest.setCancellationComment(null);
		}
		manufactureRequest = manufactureRequestDAO.save(manufactureRequest);
		return  prepareResponseData(manufactureRequest);
	}

	private ManufactureRequestResponseData prepareResponseData(ManufactureRequestData manufactureRequest) {
		ManufactureRequestResponseData manufactureRequestItem = new ManufactureRequestResponseData();
		manufactureRequestItem.setManufactureRequest(manufactureRequest);
		manufactureRequestItem.setProduct(productManagement.getProduct(manufactureRequest.getProductId()));
		manufactureRequestItem.setFranchisor(franchisorManagement.getFranchisor(manufactureRequest.getFranchisorId()));
		return manufactureRequestItem;
	}

	@Override
	public List<EnumValidValueResponseData> getStatuses() {
		List<EnumValidValueResponseData> result = new ArrayList<EnumValidValueResponseData>();
		
		for (ManufactureRequestStatusEnum value: ManufactureRequestStatusEnum.values()) {
			if (!value.getInternalUse()) {
				EnumValidValueResponseData enumValue = new EnumValidValueResponseData();
				enumValue.setKey(value.name());
				enumValue.setValue(value.getDescription());
				result.add(enumValue);
			}
		}

		return result;
	}

	@Override
	public List<ManufactureRequestData> getCompletedByProductSizeId(Long productSizeId) {
		return manufactureRequestDAO.findCompletedByProductSizeId(productSizeId);
	}
	
	@Override
	public List<ManufactureRequestData> deleteManufactureRequestsByProductId(Long productId) {
		
		List<ManufactureRequestData> manufactureRequestList = manufactureRequestDAO.findByProductId(productId);
		if(manufactureRequestList != null && !manufactureRequestList.isEmpty()) {
			for(ManufactureRequestData manufactureRequest: manufactureRequestList) {
				manufactureRequest = manufactureRequestDAO.delete(manufactureRequest.getId());
			}
		}
		
		List<ManufactureRequestHistoryData> manufactureRequestHistoryList = manufactureRequestHistoryDAO.findByProductId(productId);
		if(manufactureRequestHistoryList != null && !manufactureRequestHistoryList.isEmpty()) {
			for(ManufactureRequestHistoryData manufactureRequestHistory: manufactureRequestHistoryList) {
				manufactureRequestHistory = manufactureRequestHistoryDAO.delete(manufactureRequestHistory.getId());
				manufactureRequestList.add(this.convertToManufactureRequest(manufactureRequestHistory));
			}
		}
		
		return manufactureRequestList;
	}

	@Override
	public List<ManufactureRequestData> getManufactureRequestsBySupplierIdAndStatus(Long supplierId, ManufactureRequestStatusEnum manufactureStatus) {
		return manufactureRequestDAO.findBySupplierIdAndStatus(supplierId, manufactureStatus);
	}

	@Override
	public List<ManufactureRequestData> deleteManufactureRequestsByProductSizeId(Long id) {
		
		List<ManufactureRequestData> manufactureRequestList = manufactureRequestDAO.findByProductSizeId(id);
		if(manufactureRequestList != null && !manufactureRequestList.isEmpty()) {
			for(ManufactureRequestData manufactureRequest: manufactureRequestList) {
				manufactureRequest = manufactureRequestDAO.delete(manufactureRequest.getId());
			}
		}
		
		List<ManufactureRequestHistoryData> manufactureRequestHistoryList = manufactureRequestHistoryDAO.findByProductSizeId(id);
		if(manufactureRequestHistoryList != null && !manufactureRequestHistoryList.isEmpty()) {
			for(ManufactureRequestHistoryData manufactureRequestHistory: manufactureRequestHistoryList) {
				manufactureRequestHistory = manufactureRequestHistoryDAO.delete(manufactureRequestHistory.getId());
				manufactureRequestList.add(this.convertToManufactureRequest(manufactureRequestHistory));
			}
		}
		
		return manufactureRequestList;
	}

	@Override
	public List<ManufactureRequestData> getPendingConsolidation(String domainKey, Long consolidationWindow) {
		return manufactureRequestDAO.findFinishedOlderThanDays(consolidationWindow, domainKey);
	}

	@Override
	public List<ManufactureRequestHistoryData> saveAllManufactureRequestsToHistory(List<ManufactureRequestHistoryData> manufactureRequestsHistory) {
		return manufactureRequestHistoryDAO.saveAll(manufactureRequestsHistory);
	}

	@Override
	public List<ManufactureRequestData> deleteAllManufactureRequests(List<ManufactureRequestData> manufactureRequests) {
		List<Long> ids = new ArrayList<Long>();
		for (ManufactureRequestData manufactureRequest: manufactureRequests){
			ids.add(manufactureRequest.getId());
		}
		return manufactureRequestDAO.deleteAll(ids);
	}

	@Override
	public List<ManufactureRequestHistoryData> getManufactureRequestsHistory() {
		return manufactureRequestHistoryDAO.listAll();
	}

	@Override
	public List<ManufactureRequestData> saveAllManufactureRequests(List<ManufactureRequestData> manufactureRequestList) {
		return manufactureRequestDAO.saveAll(manufactureRequestList);
	}

	@Override
	public ManufactureRequestData convertToManufactureRequest(ManufactureRequestHistoryData manufactureRequestHistory) {
		try {
			return ObjectConverter.convert(manufactureRequestHistory, ManufactureRequestData.class);
		} catch (Exception e) {
			log.severe("Error during ManufactureRequestHistoryData convertion to ManufactureRequestData: " + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ManufactureRequestHistoryData convertToManufactureRequestHistory(ManufactureRequestData manufactureRequest) {
		try {
			return ObjectConverter.convert(manufactureRequest, ManufactureRequestHistoryData.class);
		} catch (Exception e) {
			log.severe("Error during ManufactureRequestData convertion to ManufactureRequestHistoryData: " + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<Long, ProductData> getManufactureRequestsProducts(List<ManufactureRequestData> manufactureRequests) {
		Map<Long, ProductData> response = new HashMap<Long, ProductData>();
		
		if(manufactureRequests != null && !manufactureRequests.isEmpty()) {
			for (ManufactureRequestData manufactureRequest: manufactureRequests) {
				if (!response.containsKey(manufactureRequest.getProductId())) {
					response.put(manufactureRequest.getProductId(), productManagement.getProduct(manufactureRequest.getProductId()));
				}				
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, FranchisorData> getDeliveryRequestsFranchisors(List<ManufactureRequestData> manufactureRequests) {
		Map<Long, FranchisorData> response = new HashMap<Long, FranchisorData>();
		
		if(manufactureRequests != null && !manufactureRequests.isEmpty()) {
			for (ManufactureRequestData manufactureRequest: manufactureRequests) {
				if (!response.containsKey(manufactureRequest.getFranchisorId())) {
					response.put(manufactureRequest.getFranchisorId(), franchisorManagement.getFranchisor(manufactureRequest.getFranchisorId()));
				}				
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, ProductData> getMaufactureRequestsProducts(List<ManufactureRequestData> manufactureRequests) {
		Map<Long, ProductData> response = new HashMap<Long, ProductData>();
		
		if(manufactureRequests != null && !manufactureRequests.isEmpty()) {
			for(ManufactureRequestData manufactureRequest: manufactureRequests) {
				if (!response.containsKey(manufactureRequest.getProductId())){
					response.put(manufactureRequest.getProductId(), productManagement.getProduct(manufactureRequest.getProductId()));					
				}
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, FranchisorData> getMaufactureRequestsFranchisors(List<ManufactureRequestData> manufactureRequests) {
		Map<Long, FranchisorData> response = new HashMap<Long, FranchisorData>();
		
		if(manufactureRequests != null && !manufactureRequests.isEmpty()) {
			for(ManufactureRequestData manufactureRequest: manufactureRequests) {
				if (!response.containsKey(manufactureRequest.getFranchisorId())){
					response.put(manufactureRequest.getFranchisorId(), franchisorManagement.getFranchisor(manufactureRequest.getFranchisorId()));					
				}
			}
		}
		
		return response;
	}

}
