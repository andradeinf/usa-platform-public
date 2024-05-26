package br.com.usasistemas.usaplatform.bizo;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.api.response.data.ManufactureRequestResponseData;
import br.com.usasistemas.usaplatform.dao.response.ManufactureRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.enums.ManufactureRequestStatusEnum;

public interface ManufactureManagementBO {
	
	public ManufactureRequestData createManufactureRequest(Long franchisorId, Long productId, Long productSizeId, Long quantity, Boolean addStockOnly, String domainKey);
	public List<EnumValidValueResponseData> getStatuses();
	public ManufactureRequestResponseData updateManufactureRequest(ManufactureRequestData manufactureRequest);
	public ManufactureRequestResponseData updateManufactureRequestStatus(Long manufactureRequestId, ManufactureRequestStatusEnum status, String cancellationComment);
	public ManufactureRequestPagedResponse getManufactureRequestsByProductId(Long productId, Long pageSize, String cursorString);
	public List<ManufactureRequestData> getCompletedByProductSizeId(Long productSizeId);
	public List<ManufactureRequestData> deleteManufactureRequestsByProductId(Long productId);
	public List<ManufactureRequestData> getManufactureRequestsBySupplierIdAndStatus(Long supplierId, ManufactureRequestStatusEnum manufactureStatus);
	public List<ManufactureRequestData> deleteManufactureRequestsByProductSizeId(Long id);
	public List<ManufactureRequestData> getPendingConsolidation(String domainKey, Long consolidationWindow);
	public List<ManufactureRequestHistoryData> saveAllManufactureRequestsToHistory(List<ManufactureRequestHistoryData> manufactureRequestsHistory);
	public List<ManufactureRequestData> deleteAllManufactureRequests(List<ManufactureRequestData> manufactureRequests);
	public List<ManufactureRequestHistoryData> getManufactureRequestsHistory();
	public List<ManufactureRequestData> saveAllManufactureRequests(List<ManufactureRequestData> manufactureRequestList);
	public ManufactureRequestData convertToManufactureRequest(ManufactureRequestHistoryData manufactureRequestHistory);
	public ManufactureRequestHistoryData convertToManufactureRequestHistory(ManufactureRequestData manufactureRequest);
	public ManufactureRequestPagedResponse getManufactureRequestsByFranchisorId(Long franchisorId, Long pageSize, String cursorString);
	public Map<Long, ProductData> getManufactureRequestsProducts(List<ManufactureRequestData> manufactureRequests);
	public Map<Long, FranchisorData> getDeliveryRequestsFranchisors(List<ManufactureRequestData> manufactureRequests);
	public Map<Long, ProductData> getMaufactureRequestsProducts(List<ManufactureRequestData> manufactureRequests);
	public Map<Long, FranchisorData> getMaufactureRequestsFranchisors(List<ManufactureRequestData> manufactureRequests);

}
