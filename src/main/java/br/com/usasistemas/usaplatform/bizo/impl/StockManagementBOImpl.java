package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.bizo.ManufactureManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.ReviewManagementBO;
import br.com.usasistemas.usaplatform.bizo.StockManagementBO;
import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.dao.StockConsolidationDAO;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;
import br.com.usasistemas.usaplatform.model.data.StockConsolidationData;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.DomainKeysEnum;
import br.com.usasistemas.usaplatform.model.enums.ManufactureRequestStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.ReviewRequesStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.StockConsolidationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.SystemConfigurationEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class StockManagementBOImpl implements StockManagementBO {
	
	private static final Logger log = Logger.getLogger(StockManagementBOImpl.class.getName());
	private StockConsolidationDAO stockConsolidationDAO;
	private ProductManagementBO productManagement;
	private ManufactureManagementBO manufactureManagement;
	private DeliveryManagementBO deliveryManagement;
	private ReviewManagementBO reviewManagement;
	private ConfigurationManagementBO configurationManagement;

	public StockConsolidationDAO getStockConsolidationDAO() {
		return stockConsolidationDAO;
	}

	public void setStockConsolidationDAO(StockConsolidationDAO stockConsolidationDAO) {
		this.stockConsolidationDAO = stockConsolidationDAO;
	}

	public ProductManagementBO getProductManagement() {
		return productManagement;
	}

	public void setProductManagement(ProductManagementBO productManagement) {
		this.productManagement = productManagement;
	}

	public ManufactureManagementBO getManufactureManagement() {
		return manufactureManagement;
	}

	public void setManufactureManagement(
			ManufactureManagementBO manufactureManagement) {
		this.manufactureManagement = manufactureManagement;
	}

	public DeliveryManagementBO getDeliveryManagement() {
		return deliveryManagement;
	}

	public void setDeliveryManagement(DeliveryManagementBO deliveryManagement) {
		this.deliveryManagement = deliveryManagement;
	}

	public ReviewManagementBO getReviewManagement() {
		return reviewManagement;
	}

	public void setReviewManagement(ReviewManagementBO reviewManagement) {
		this.reviewManagement = reviewManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	@Override
	public void consolidateStock(){
		
		for (DomainKeysEnum domain : DomainKeysEnum.values()) {
			
			log.info("Consolidate stock for domain " + domain.name() + "...");
			
			DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByKey(domain.name());
			Long consolidationWindow = Long.parseLong(domainConfiguration.getConfiguration().get(SystemConfigurationEnum.STOCK_CONSOLIDATION_WINDOW.toString()));
			
			Map<Long, Long> productSizeConsolidatedStock = new HashMap<Long, Long>();
			
			Map<Long, List<ManufactureRequestData>> productSizeOriginalManufactureRequests = new HashMap<Long, List<ManufactureRequestData>>();
			Map<Long, List<ManufactureRequestHistoryData>> productSizeHistoryManufactureRequests = new HashMap<Long, List<ManufactureRequestHistoryData>>();
			
			Map<Long, List<DeliveryRequestData>> productSizeOriginalDeliveryRequests = new HashMap<Long, List<DeliveryRequestData>>();
			Map<Long, List<DeliveryRequestHistoryData>> productSizeHistoryDeliveryRequests = new HashMap<Long, List<DeliveryRequestHistoryData>>();
			
			List<DeliveryRequestHistoryData> consolidatedDeliveryRequestHistory = new ArrayList<DeliveryRequestHistoryData>();
			
			/*
			 * STOCK CONSOLIDATION
			 */
			
			log.info("Starting stock consolidation...");
			StockConsolidationData stockConsolidation = new StockConsolidationData();
			stockConsolidation.setDate(DateUtil.getCurrentDate());
			stockConsolidation.setStatus(StockConsolidationStatusEnum.STARTED);
			stockConsolidation = stockConsolidationDAO.save(stockConsolidation);
			
			/*
			 * STEP 1: consolidate manufacture request stock change in the temporary HashMap AND
			 * 			create array of history objects to persist in the history table
			 */
			try {
				log.fine("STEP 1: consolidate manufacture request stock");
				
				List<ManufactureRequestData> manufactureRequests = manufactureManagement.getPendingConsolidation(domain.name(), consolidationWindow);
				log.fine(">>> Found " + manufactureRequests.size() + " manufactureRequests for consolidation");
				
				for (ManufactureRequestData manufactureRequest: manufactureRequests){
					
					log.fine(">>> Processing manufactureRequest: [manufactureRequestId=" + manufactureRequest.getId()
							+ "; productId=" + manufactureRequest.getProductId() + "; productSizeId=" + manufactureRequest.getProductSizeId()
							+ "; quantity=" + manufactureRequest.getQuantity() + "; status=" + manufactureRequest.getStatus() + "]");
					
					if (manufactureRequest.getStatus().equals(ManufactureRequestStatusEnum.CANCELLED)) {
						log.fine(">>> Ignore CANCELLED, as it should not count in the stock");
					} else {
						Long currentConsolidatedStock = productSizeConsolidatedStock.get(manufactureRequest.getProductSizeId());
						if (currentConsolidatedStock == null) {
							log.fine(">>> First request for this productSize. Initialize currentConsolidatedStock with ZERO");
							currentConsolidatedStock = 0L;
						} else {
							log.fine(">>> ProductSize found. currentConsolidatedStock=" + currentConsolidatedStock);
						}

						//add request quantity
						currentConsolidatedStock += manufactureRequest.getQuantity();
						log.fine(">>> Updated stock. currentConsolidatedStock=" + currentConsolidatedStock);
						productSizeConsolidatedStock.put(manufactureRequest.getProductSizeId(), currentConsolidatedStock);
					}
					
					//add existing request to product size array
					log.fine(">>> Save request on productSizeOriginalManufactureRequestList to bulk delete/rollback");
					List<ManufactureRequestData> productSizeOriginalManufactureRequestList = productSizeOriginalManufactureRequests.get(manufactureRequest.getProductSizeId());
					if (productSizeOriginalManufactureRequestList == null) {
						log.fine(">>> First request for this productSize. Initialize productSizeOriginalManufactureRequestList");
						productSizeOriginalManufactureRequestList = new ArrayList<ManufactureRequestData>();
						productSizeOriginalManufactureRequests.put(manufactureRequest.getProductSizeId(), productSizeOriginalManufactureRequestList);
					}
					productSizeOriginalManufactureRequestList.add(manufactureRequest);
					
					//create History object to processed request and add to product size array
					log.fine(">>> Create History request and Save on productSizeHistoryManufactureRequestList to bulk insert/rollback");
					List<ManufactureRequestHistoryData> productSizeHistoryManufactureRequestList = productSizeHistoryManufactureRequests.get(manufactureRequest.getProductSizeId());
					if (productSizeHistoryManufactureRequestList == null) {
						log.fine(">>> First request for this productSize. Initialize productSizeHistoryManufactureRequestList");
						productSizeHistoryManufactureRequestList = new ArrayList<ManufactureRequestHistoryData>();
						productSizeHistoryManufactureRequests.put(manufactureRequest.getProductSizeId(), productSizeHistoryManufactureRequestList);
					}
					ManufactureRequestHistoryData manufactureRequestHistory = manufactureManagement.convertToManufactureRequestHistory(manufactureRequest);
					manufactureRequestHistory.setStockConsolidationId(stockConsolidation.getId());
					manufactureRequestHistory.setUpdateDate(DateUtil.getCurrentDate());
					productSizeHistoryManufactureRequestList.add(manufactureRequestHistory);

				}
				log.info(">>> All manufactureRequests stock consolidated");
			} catch (Exception e) {
				log.severe("Error during ManufactureRequest consolidated stock calculation: " + e.toString());
				stockConsolidation.setStatus(StockConsolidationStatusEnum.ERROR);
				stockConsolidation.setMessage("Error during ManufactureRequest consolidated stock calculation");
				stockConsolidation.setDetails(e.toString());
				stockConsolidationDAO.save(stockConsolidation);
				return;
			}
			
			/*
			 * STEP 2: consolidate delivery request stock change in the temporary HashMap AND
			 * 			create array of history objects to persist in the history table
			 */
			try {
				log.fine("STEP 2: consolidate delivery request stock");
				
				List<DeliveryRequestData> deliveryRequests = deliveryManagement.getPendingConsolidation(domain.name(), consolidationWindow);
				log.fine(">>> Found " + deliveryRequests.size() + " deliveryRequests for consolidation");
				
				for (DeliveryRequestData deliveryRequest: deliveryRequests){
					
					log.fine(">>> Processing deliveryRequest: [deliveryRequestId=" + deliveryRequest.getId()
							+ "; productId=" + deliveryRequest.getProductId() + "; productSizeId=" + deliveryRequest.getProductSizeId()
							+ "; quantity=" + deliveryRequest.getQuantity() + "; status=" + deliveryRequest.getStatus() + "]");
					
					if (deliveryRequest.getStatus().equals(DeliveryRequestStatusEnum.CANCELLED)) {
						log.fine(">>> Ignore CANCELLED, as it should not count in the stock");
					} else {
						
						Long currentConsolidatedStock = productSizeConsolidatedStock.get(deliveryRequest.getProductSizeId());
						if (currentConsolidatedStock == null) {
							log.fine(">>> First request for this productSize. Initialize currentConsolidatedStock with ZERO");
							currentConsolidatedStock = 0L;
						} else {
							log.fine(">>> ProductSize found. currentConsolidatedStock=" + currentConsolidatedStock);
						}
					
						//subtract request quantity
						currentConsolidatedStock -= deliveryRequest.getQuantity();
						log.fine(">>> Updated stock. currentConsolidatedStock=" + currentConsolidatedStock);
						productSizeConsolidatedStock.put(deliveryRequest.getProductSizeId(), currentConsolidatedStock);
					}
					
					//add existing request to product size array
					log.fine(">>> Save request on productSizeOriginalDeliveryRequestList to bulk delete/rollback");
					List<DeliveryRequestData> productSizeOriginalDeliveryRequestList = productSizeOriginalDeliveryRequests.get(deliveryRequest.getProductSizeId());
					if (productSizeOriginalDeliveryRequestList == null) {
						log.fine(">>> First request for this productSize. Initialize productSizeOriginalDeliveryRequestList");
						productSizeOriginalDeliveryRequestList = new ArrayList<DeliveryRequestData>();
						productSizeOriginalDeliveryRequests.put(deliveryRequest.getProductSizeId(), productSizeOriginalDeliveryRequestList);
					}
					productSizeOriginalDeliveryRequestList.add(deliveryRequest);
					
					//create History object to processed request and add to product size array
					log.fine(">>> Create History request and Save on productSizeHistoryDeliveryRequestList to bulk insert/rollback");
					List<DeliveryRequestHistoryData> productSizeHistoryDeliveryRequestList = productSizeHistoryDeliveryRequests.get(deliveryRequest.getProductSizeId());
					if (productSizeHistoryDeliveryRequestList == null) {
						log.fine(">>> First request for this productSize. Initialize productSizeHistoryDeliveryRequestList");
						productSizeHistoryDeliveryRequestList = new ArrayList<DeliveryRequestHistoryData>();
						productSizeHistoryDeliveryRequests.put(deliveryRequest.getProductSizeId(), productSizeHistoryDeliveryRequestList);
					}
					DeliveryRequestHistoryData deliveryRequestHistory = deliveryManagement.convertToDeliveryRequestHistory(deliveryRequest);
					deliveryRequestHistory.setStockConsolidationId(stockConsolidation.getId());
					deliveryRequestHistory.setUpdateDate(DateUtil.getCurrentDate());
					productSizeHistoryDeliveryRequestList.add(deliveryRequestHistory);

				}
				log.info(">>> All deliveryRequests stock consolidated");
			} catch (Exception e) {
				log.severe("Error during DeliveryRequest consolidated stock calculation: " + e.toString());
				stockConsolidation.setStatus(StockConsolidationStatusEnum.ERROR);
				stockConsolidation.setMessage("Error during DeliveryRequest consolidated stock calculation");
				stockConsolidation.setDetails(e.toString());
				stockConsolidationDAO.save(stockConsolidation);
				return;
			}
			
			/*
			 * STEP 3: for each product size, read from the datastore, update stock and move records to history
			 *			(delete from original table and insert in history table
			 */
			try {
				log.fine("STEP 3: for each product size, read from the datastore, update stock and move records to history");
				
				Set<Long> productSizeIdList = productSizeConsolidatedStock.keySet();
				log.fine(">>> " + productSizeIdList.size() + " products to update");
				
				for (Long productSizeId: productSizeIdList) {
					
					log.fine(">>> Processing productSizeId: " + productSizeId);
					
					/*
					 * STEP 3.1: Save ManufactureRequests to History
					 */
					List<ManufactureRequestHistoryData> productSizeHistoryManufactureRequestList = productSizeHistoryManufactureRequests.get(productSizeId);
					if (productSizeHistoryManufactureRequestList != null) {
						log.fine(">>> Saving " + productSizeHistoryManufactureRequestList.size() + " manufactureRequests to the History table");
						manufactureManagement.saveAllManufactureRequestsToHistory(productSizeHistoryManufactureRequestList);
					}				
					
					/*
					 * STEP 3.2: Save DeliveryRequests to History
					 */
					List<DeliveryRequestHistoryData> productSizeHistoryDeliveryRequestList = productSizeHistoryDeliveryRequests.get(productSizeId);
					if (productSizeHistoryDeliveryRequestList != null) {
						log.fine(">>> Saving " + productSizeHistoryDeliveryRequestList.size() + " deliveryRequests to the History table");
						consolidatedDeliveryRequestHistory.addAll(deliveryManagement.saveAllDeliveryRequestsToHistory(productSizeHistoryDeliveryRequestList));
					}				
					
					/*
					 * STEP 3.3: Get ProductSize to update stock
					 */
					ProductSizeData productSize = productManagement.getProductSize(productSizeId);
					log.fine(">>> Consolidated stock before update: " + productSize.getConsolidatedStock());
					productSize.setConsolidatedStock(productSize.getConsolidatedStock() + productSizeConsolidatedStock.get(productSizeId));
					log.fine(">>> Consolidated stock after update: " + productSize.getConsolidatedStock());
					productManagement.saveProductSize(productSize, null);
					
					/*
					 * STEP 3.4: Delete ManufactureRequests from Original table
					 */
					List<ManufactureRequestData> productSizeOriginalManufactureRequestList = productSizeOriginalManufactureRequests.get(productSizeId);
					if (productSizeOriginalManufactureRequestList != null) {
						log.fine(">>> Deleting " + productSizeOriginalManufactureRequestList.size() + " manufactureRequests from original table");
						manufactureManagement.deleteAllManufactureRequests(productSizeOriginalManufactureRequestList);
					}				
					
					/*
					 * STEP 3.5: Delete DeliveryRequests from Original table
					 */
					List<DeliveryRequestData> productSizeOriginalDeliveryRequestList = productSizeOriginalDeliveryRequests.get(productSizeId);
					if (productSizeOriginalDeliveryRequestList != null) {
						log.fine(">>> Deleting " + productSizeOriginalDeliveryRequestList.size() + " deliveryRequests from original table");
						deliveryManagement.deleteAllDeliveryRequests(productSizeOriginalDeliveryRequestList);
					}
					
				}			
				
			} catch (Exception e) {
				log.severe("Error during datastore stock update: " + e.toString());
				stockConsolidation.setStatus(StockConsolidationStatusEnum.ERROR);
				stockConsolidation.setMessage("Error during datastore stock update");
				stockConsolidation.setDetails(e.toString());
				stockConsolidationDAO.save(stockConsolidation);
				return;
			}
			
			stockConsolidation.setStatus(StockConsolidationStatusEnum.COMPLETED);
			stockConsolidation.setMessage("Consolidation finished successfully");
			stockConsolidation.setDetails(productSizeConsolidatedStock.size() + " product size stocks consolidated; " +
											productSizeHistoryManufactureRequests.size() + " manufactureRequests moved to History; " +
											productSizeHistoryDeliveryRequests.size() + " deliveryRequests moved to History; ");
			stockConsolidationDAO.save(stockConsolidation);
			log.info("Stock consolidation finished successfully");
			log.info(productSizeConsolidatedStock.size() + " product size stocks consolidated; " +
					productSizeHistoryManufactureRequests.size() + " manufactureRequests moved to History; " +
					productSizeHistoryDeliveryRequests.size() + " deliveryRequests moved to History; ");
			
			if (domainConfiguration.getFeatureFlags().get("REVIEW_REQUEST")) {
				
				/*
				 * REVIEW REQUEST CONSOLIDATION
				 */
				
				log.info("Starting supplier review consolidation...");
				
				//Group deliveryRequestHistory information by franchisee and supplier
				Map<Long, Map<Long, List<DeliveryRequestHistoryData>>> franchiseeSupplierDeliveryRequestHistoryMap = new HashMap<Long, Map<Long, List<DeliveryRequestHistoryData>>>();
				log.info(consolidatedDeliveryRequestHistory.size() + " delivery requests to consolidate reviews");
				if (consolidatedDeliveryRequestHistory != null && !consolidatedDeliveryRequestHistory.isEmpty()) {
					for(DeliveryRequestHistoryData deliveryRequestHistory : consolidatedDeliveryRequestHistory) {
						//Group by franchisee
						Map<Long, List<DeliveryRequestHistoryData>> franchiseeDeliveryRequestHistoryMap = franchiseeSupplierDeliveryRequestHistoryMap.get(deliveryRequestHistory.getFranchiseeId());
						if (franchiseeDeliveryRequestHistoryMap == null) {
							log.fine(">>> First request for this franchisee. Initialize franchiseeDeliveryRequestHistoryMap");
							franchiseeDeliveryRequestHistoryMap = new HashMap<Long, List<DeliveryRequestHistoryData>>();
							franchiseeSupplierDeliveryRequestHistoryMap.put(deliveryRequestHistory.getFranchiseeId(), franchiseeDeliveryRequestHistoryMap);
						}			
						
						//Group by supplier
						List<DeliveryRequestHistoryData> supplierDeliveryRequestHistoryList = franchiseeDeliveryRequestHistoryMap.get(deliveryRequestHistory.getSupplierId());
						if (supplierDeliveryRequestHistoryList == null) {
							log.fine(">>> First request for this supplier. Initialize supplierDeliveryRequestHistoryList");
							supplierDeliveryRequestHistoryList = new ArrayList<DeliveryRequestHistoryData>();
							franchiseeDeliveryRequestHistoryMap.put(deliveryRequestHistory.getSupplierId(), supplierDeliveryRequestHistoryList);
						}
						supplierDeliveryRequestHistoryList.add(deliveryRequestHistory);		
					}	
				}
				
				//Create one review request for each franchisee/supplier
				for (Long franchiseeId : franchiseeSupplierDeliveryRequestHistoryMap.keySet()) {
					for (Long supplierId : franchiseeSupplierDeliveryRequestHistoryMap.get(franchiseeId).keySet()) {
						
						ReviewRequestData supplierReviewRequest = new ReviewRequestData();
						supplierReviewRequest.setStatus(ReviewRequesStatusEnum.REQUESTED);
						supplierReviewRequest.setFromEntityProfile(UserProfileEnum.FRANCHISEE);
						supplierReviewRequest.setFromEntityId(franchiseeId);
						supplierReviewRequest.setToEntityProfile(UserProfileEnum.SUPPLIER);
						supplierReviewRequest.setToEntityId(supplierId);
						supplierReviewRequest.setDeliveryRequestHistoryIds(
								franchiseeSupplierDeliveryRequestHistoryMap
									.get(franchiseeId)
									.get(supplierId)
									.stream()
									.map(entry -> entry.getId())
									.collect(Collectors.toList()));
						supplierReviewRequest.setDate(new Date());
						
						reviewManagement.createSupplierReviewRequest(supplierReviewRequest);
					}
				}
				
				log.info("Supplier review consolidation finished successfully");
			}	
		}
	}
	
	@Override
	public void movingBackFromHistory() {
		
		List<ManufactureRequestData> manufactureRequestList = new ArrayList<ManufactureRequestData>();
		for (ManufactureRequestHistoryData manufactureRequestHistory: manufactureManagement.getManufactureRequestsHistory()){
			manufactureRequestList.add(manufactureManagement.convertToManufactureRequest(manufactureRequestHistory));
		}
		manufactureManagement.saveAllManufactureRequests(manufactureRequestList);
		
		List<DeliveryRequestData> deliveryRequestList = new ArrayList<DeliveryRequestData>();
		for (DeliveryRequestHistoryData deliveryRequestHistory: deliveryManagement.getDeliveryRequestsHistory()){
			deliveryRequestList.add(deliveryManagement.convertToDeliveryRequest(deliveryRequestHistory));
		}
		manufactureManagement.saveAllManufactureRequests(manufactureRequestList);
		
		for (ProductData product: productManagement.getAllProducts()){
			for (ProductSizeData productSize: product.getSizes()){
				productSize.setConsolidatedStock(0L);
				productManagement.saveProductSize(productSize, null);
			}
		}
	}
	
}
