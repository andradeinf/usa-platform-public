package br.com.usasistemas.usaplatform.dao;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.dao.response.DeliveryRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.entity.DeliveryRequest;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;


public interface DeliveryRequestDAO extends GenericDAO<DeliveryRequest, DeliveryRequestData> {
	
	public DeliveryRequestPagedResponse findByProductId (Long productId, Long pageSize, String cursorString);
	public List<DeliveryRequestData> findByProductId (Long productId);
	public List<DeliveryRequestData> findNotCancelledByProductSizeId(Long productSizeId);
	public List<DeliveryRequestData> findByFranchiseeId(Long franchiseeId);
	public DeliveryRequestPagedResponse findByProductIdAndFranchiseeId(Long productId, Long franchiseeId, Long pageSize, String cursorString);
	public List<DeliveryRequestData> findBySupplierId(Long supplierId);
	public List<DeliveryRequestData> findBySupplierIdAndStatus(Long supplierId, DeliveryRequestStatusEnum deliveryStatus);
	public List<DeliveryRequestData> findByProductSizeId(Long id);
	public List<DeliveryRequestData> findFinishedOlderThanDays(Long numberOfDays, String domainKey);
	public List<DeliveryRequestData> findByNotificationStatus(MailNotificationStatusEnum pending);
	public DeliveryRequestPagedResponse findByFranchiseeId(Long franchiseeId, Long pageSize, String cursorString);
	public DeliveryRequestPagedResponse findBySupplierIdAndFranchiseeIdAndStatus(Long supplierId, Long franchiseeId, DeliveryRequestStatusEnum status, Long pageSize, String cursorString);
	public DeliveryRequestPagedResponse findByStatus(DeliveryRequestStatusEnum status, Long pageSize, String cursorString);
	public DeliveryRequestPagedResponse findByDueDateRange(Map<String, Object> parameters, Long pageSize, String cursorString);
	public List<DeliveryRequestData> findByPaymentSlipKey(String paymentSlipKey);
	public List<DeliveryRequestData> findByFiscalFileKey(String fiscalFileKey);

}
