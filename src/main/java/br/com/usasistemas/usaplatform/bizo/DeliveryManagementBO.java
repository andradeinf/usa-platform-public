package br.com.usasistemas.usaplatform.bizo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.api.request.data.DeliveryRequestStatusData;
import br.com.usasistemas.usaplatform.api.response.data.DeliveryRequestStatusResponseData;
import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.dao.response.DeliveryRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;

public interface DeliveryManagementBO {
	
	public DeliveryRequestData createDeliveryRequest(Long franchiseeId, Long productId, Long productSizeId, Long quantity, String domainKey);
	public List<DeliveryRequestData> getDeliveryRequests();
	public DeliveryRequestData getDeliveryRequest(Long id);
	public DeliveryRequestHistoryData getDeliveryRequestHistory(Long id);
	public DeliveryRequestPagedResponse getDeliveryRequestsByFranchiseeId(Long franchiseeId, Long pageSize, String cursorString);
	public List<EnumValidValueResponseData> getStatuses();
	public DeliveryRequestStatusResponseData updateDeliveryRequestStatus( List<DeliveryRequestStatusData> deliveryRequests, DeliveryRequestStatusEnum status, String cancellationComment, Date sentDate, Date deadlineDate, String carrierName, String trackingCode, String fiscalNumber, Date dueDate, Date autoCancellationDate, Long paymentSlipId, Long fiscalFileId, UserProfileData user);
	public DeliveryRequestPagedResponse getDeliveryRequestsByProductId(Long productId, Long pageSize, String cursorString);
	public DeliveryRequestPagedResponse getDeliveryRequestsByProductIdAndFranchiseeId(Long productId, Long franchiseeId, Long pageSize, String cursorString);
	public List<DeliveryRequestData> getNotCancelledByProductSizeId(Long productSizeId);
	public List<DeliveryRequestData> deleteDeliveryRequestsByProductId(Long productId);
	public List<DeliveryRequestData> getDeliveryRequestsBySupplierId(Long supplierId);
	public List<DeliveryRequestData> getDeliveryRequestsBySupplierIdAndStatus(Long supplierId, DeliveryRequestStatusEnum deliveryStatus);
	public Map<Long, ProductData> getDeliveryRequestsProducts(List<DeliveryRequestData> deliveryRequests);
	public Map<Long, ProductData> getDeliveryRequestHistoriesProducts(List<DeliveryRequestHistoryData> deliveryRequests);
	public Map<Long, FranchiseeData> getDeliveryRequestsFranchisees(List<DeliveryRequestData> deliveryRequests);
	public Map<Long, FranchisorData> getDeliveryRequestsFranchiseesFranchisors(List<FranchiseeData> franchisees);
	public Map<Long, SupplierData> getDeliveryRequestsProductsSuppliers(List<ProductData> products);
	public List<DeliveryRequestData> deleteDeliveryRequestsByProductSizeId(Long id);
	public List<DeliveryRequestData> getPendingConsolidation(String domainKey, Long consolidationWindow);
	public List<DeliveryRequestData> getPendingNotifications();
	public List<DeliveryRequestData> updateNotificationStatus(List<DeliveryRequestData> deliveryRequests, MailNotificationStatusEnum notificationStatus);
	public List<DeliveryRequestHistoryData> saveAllDeliveryRequestsToHistory(List<DeliveryRequestHistoryData> deliveryRequestsHistory);
	public List<DeliveryRequestData> deleteAllDeliveryRequests(List<DeliveryRequestData> deliveryRequests);
	public List<DeliveryRequestHistoryData> getDeliveryRequestsHistory();
	public DeliveryRequestData convertToDeliveryRequest(DeliveryRequestHistoryData deliveryRequestHistory);
	public DeliveryRequestHistoryData convertToDeliveryRequestHistory(DeliveryRequestData deliveryRequest);
	public DeliveryRequestPagedResponse getDeliveryRequestsBySupplierIdAndFranchiseeIdAndStatus(Long supplierId, Long franchiseeId, DeliveryRequestStatusEnum status, Long pageSize, String cursorString);
	public void autoCancelPendingWithRestriction();
	public List<DeliveryRequestData> getPendingCancellationNotifications();
	public List<DeliveryRequestData> getDeliveryRequestsByFilter(Date initialDate, Date finalDate, Long franchiseeId, Long supplierId);
	public DeliveryRequestPagedResponse getAccountsReceivable(Long supplierId, Date fromDate, Date toDate, Long pageSize, String cursorString);
	public Map<Long, SupplierData> getDeliveryRequestsSuppliers(List<DeliveryRequestData> deliveryRequests);
	public DeliveryRequestPagedResponse getAccountsPayable(Long franchiseeId, Date fromDate, Date toDate, long pageSize, String cursorString);
	public void checkAndDeleteReplacedPaymentSlip(String paymentSlipKey);
	public void checkAndDeleteReplacedFiscalFile(String fiscalFileKey);
}
