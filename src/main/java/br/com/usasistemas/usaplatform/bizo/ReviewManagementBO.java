package br.com.usasistemas.usaplatform.bizo;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;

public interface ReviewManagementBO {

	public ReviewRequestData createSupplierReviewRequest(ReviewRequestData supplierReviewRequest);
	public ReviewRequestData getNextRequestedSupplierReviewRequest(Long franchiseeId, Long userId);
	public ReviewRequestData cancelRequestedSupplierReviewRequest(Long id, Long userId);
	public ReviewRequestData addRequestedSupplierReviewRequestRates(Long id, Long userId, Long qualityRate, Long deliveryRate, Long priceRate, Long paymentConditionRate);
	public void processSupplierReviewRequesttAsync(Long id);
	public List<DeliveryRequestHistoryData> getReviewRequestDeliveryRequests(Long id);
	public Map<Long, ProductData> getDeliveryRequestsProducts(List<DeliveryRequestHistoryData> deliveryRequests);
	public SupplierData getReviewRequestSupplier(Long supplierId);

}
