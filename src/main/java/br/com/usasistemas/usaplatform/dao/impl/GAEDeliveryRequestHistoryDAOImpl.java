package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.QueryResults;
import com.googlecode.objectify.cmd.Query;

import br.com.usasistemas.usaplatform.dao.DeliveryRequestHistoryDAO;
import br.com.usasistemas.usaplatform.dao.response.DeliveryRequestHistoryPagedResponse;
import br.com.usasistemas.usaplatform.dao.response.ListBasePagedResponse;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.entity.DeliveryRequestHistory;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;

public class GAEDeliveryRequestHistoryDAOImpl extends GAEGenericDAOImpl<DeliveryRequestHistory, DeliveryRequestHistoryData> implements DeliveryRequestHistoryDAO {
	
	private static final Logger log = Logger.getLogger(GAEDeliveryRequestHistoryDAOImpl.class.getName());

	@Override
	public List<DeliveryRequestHistoryData> findByProductId(Long productId) {
		List<DeliveryRequestHistoryData> result = new ArrayList<DeliveryRequestHistoryData>();

		try {
			List<DeliveryRequestHistory> deliveryRequests = ofy().load().type(DeliveryRequestHistory.class)
				.filter("productId", productId)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty()) {
				result = this.getConverter().convertToDataList(deliveryRequests);
			}

		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by productId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public DeliveryRequestHistoryPagedResponse findByProductId(Long productId, Long pageSize, String cursorString) {
		DeliveryRequestHistoryPagedResponse result = new DeliveryRequestHistoryPagedResponse();

		try {
			Query<DeliveryRequestHistory> q = ofy().load().type(DeliveryRequestHistory.class)
				.filter("productId", productId)
				.limit(pageSize.intValue())
				.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<DeliveryRequestHistory> iterator = q.iterator();
			result.setDeliveryRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());

		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by productId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<DeliveryRequestHistoryData> findByFranchiseeId(Long franchiseeId) {
		List<DeliveryRequestHistoryData> result = new ArrayList<DeliveryRequestHistoryData>();

		try {
			List<DeliveryRequestHistory> deliveryRequests = ofy().load().type(DeliveryRequestHistory.class)
				.filter("franchiseeId", franchiseeId)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty())
				result = this.getConverter().convertToDataList(deliveryRequests);
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by franchiseeId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public DeliveryRequestHistoryPagedResponse findByFranchiseeId(Long franchiseeId, Long pageSize, String cursorString) {
		DeliveryRequestHistoryPagedResponse result = new DeliveryRequestHistoryPagedResponse();

		try {
			Query<DeliveryRequestHistory> q = ofy().load().type(DeliveryRequestHistory.class)
				.filter("franchiseeId", franchiseeId)
				.limit(pageSize.intValue())
				.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<DeliveryRequestHistory> iterator = q.iterator();
			result.setDeliveryRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());

		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by franchiseeId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public DeliveryRequestHistoryPagedResponse findByProductIdAndFranchiseeId(Long productId, Long franchiseeId, Long pageSize, String cursorString) {
		DeliveryRequestHistoryPagedResponse result = new DeliveryRequestHistoryPagedResponse();

		try {

			Query<DeliveryRequestHistory> q = ofy().load().type(DeliveryRequestHistory.class)
				.filter("productId", productId)
				.filter("franchiseeId", franchiseeId)
				.limit(pageSize.intValue())
				.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<DeliveryRequestHistory> iterator = q.iterator();
			result.setDeliveryRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());
		
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by productId and franchiseeId: " + e.toString());
		}			
	
		return result;
	}
	

	@Override
	public DeliveryRequestHistoryPagedResponse findBySupplierIdAndFranchiseeIdAndStatus(Long supplierId, Long franchiseeId, DeliveryRequestStatusEnum status, Long pageSize, String cursorString) {
		DeliveryRequestHistoryPagedResponse result = new DeliveryRequestHistoryPagedResponse();

		try {
			Query<DeliveryRequestHistory> q = ofy().load().type(DeliveryRequestHistory.class)
				.filter("supplierId", supplierId)
				.filter("franchiseeId", franchiseeId)
				.filter("status", status)
				.limit(pageSize.intValue())
				.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<DeliveryRequestHistory> iterator = q.iterator();
			result.setDeliveryRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());
		
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by supplierId and franchiseeId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<DeliveryRequestHistoryData> findBySupplierId(Long supplierId) {
		List<DeliveryRequestHistoryData> result = new ArrayList<DeliveryRequestHistoryData>();

		try {
			List<DeliveryRequestHistory> deliveryRequests = ofy().load().type(DeliveryRequestHistory.class)
				.filter("supplierId", supplierId)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty())
				result = this.getConverter().convertToDataList(deliveryRequests);
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by supplierId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<DeliveryRequestHistoryData> findByProductSizeId(Long id) {
		List<DeliveryRequestHistoryData> result = new ArrayList<DeliveryRequestHistoryData>();

		try {
			List<DeliveryRequestHistory> deliveryRequests = ofy().load().type(DeliveryRequestHistory.class)
				.filter("productSizeId", id)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty())
				result = this.getConverter().convertToDataList(deliveryRequests);
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by productSizeId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public DeliveryRequestHistoryPagedResponse findByDueDateRange(Map<String, Object> parameters, Long pageSize, String cursorString) {

		DeliveryRequestHistoryPagedResponse result = new DeliveryRequestHistoryPagedResponse();
		
		try {
			ListBasePagedResponse<DeliveryRequestHistoryData> r = this.findByFilter(parameters, "dueDate", pageSize, cursorString);
			result.setDeliveryRequests(r.getItems());
			result.setHasMore(r.getHasMore());
			result.setCursorString(r.getCursorString());
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by dueDateRange: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<DeliveryRequestHistoryData> findByPaymentSlipKey(String paymentSlipKey) {
		List<DeliveryRequestHistoryData> result = new ArrayList<DeliveryRequestHistoryData>();

		try {
			List<DeliveryRequestHistory> deliveryRequests = ofy().load().type(DeliveryRequestHistory.class)
				.filter("paymentSlipKey", paymentSlipKey)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty()) {
				result = this.getConverter().convertToDataList(deliveryRequests);
			}

		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by paymentSlipKey: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<DeliveryRequestHistoryData> findByFiscalFileKey(String fiscalFileKey) {
		List<DeliveryRequestHistoryData> result = new ArrayList<DeliveryRequestHistoryData>();

		try {
			List<DeliveryRequestHistory> deliveryRequests = ofy().load().type(DeliveryRequestHistory.class)
				.filter("fiscalFileKey", fiscalFileKey)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty()) {
				result = this.getConverter().convertToDataList(deliveryRequests);
			}

		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequestHistory by fiscalFileKey: " + e.toString());
		}			
	
		return result;
	}
}
