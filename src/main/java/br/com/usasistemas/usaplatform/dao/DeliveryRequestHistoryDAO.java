package br.com.usasistemas.usaplatform.dao;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.dao.response.DeliveryRequestHistoryPagedResponse;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.entity.DeliveryRequestHistory;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;


public interface DeliveryRequestHistoryDAO extends GenericDAO<DeliveryRequestHistory, DeliveryRequestHistoryData> {
	
	public DeliveryRequestHistoryPagedResponse findByProductId (Long productId, Long pageSize, String cursorString);
	public List<DeliveryRequestHistoryData> findByFranchiseeId(Long franchiseeId);
	public DeliveryRequestHistoryPagedResponse findByProductIdAndFranchiseeId(Long productId, Long franchiseeId, Long pageSize, String cursorString);
	public List<DeliveryRequestHistoryData> findBySupplierId(Long supplierId);
	public List<DeliveryRequestHistoryData> findByProductSizeId(Long id);
	public DeliveryRequestHistoryPagedResponse findByFranchiseeId(Long franchiseeId, Long pageSize, String cursorString);
	public DeliveryRequestHistoryPagedResponse findBySupplierIdAndFranchiseeIdAndStatus(Long supplierId, Long franchiseeId, DeliveryRequestStatusEnum status, Long pageSize, String cursorString);
	public List<DeliveryRequestHistoryData> findByProductId(Long productId);
	public List<DeliveryRequestHistoryData> findByFilter(Map<String, Object> parameters);
	public DeliveryRequestHistoryPagedResponse findByDueDateRange(Map<String, Object> parameters, Long pageSize, String cursorString);
	public List<DeliveryRequestHistoryData> findByPaymentSlipKey(String paymentSlipKey);
	public List<DeliveryRequestHistoryData> findByFiscalFileKey(String fiscalFileKey);
	
}
