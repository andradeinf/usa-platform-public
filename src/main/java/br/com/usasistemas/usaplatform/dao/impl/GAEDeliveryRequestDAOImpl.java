package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.QueryResults;
import com.googlecode.objectify.cmd.Query;

import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.dao.DeliveryRequestDAO;
import br.com.usasistemas.usaplatform.dao.response.DeliveryRequestPagedResponse;
import br.com.usasistemas.usaplatform.dao.response.ListBasePagedResponse;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.entity.DeliveryRequest;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;

public class GAEDeliveryRequestDAOImpl extends GAEGenericDAOImpl<DeliveryRequest, DeliveryRequestData> implements DeliveryRequestDAO {
	
	private static final Logger log = Logger.getLogger(GAEDeliveryRequestDAOImpl.class.getName());

	@Override
	public DeliveryRequestPagedResponse findByProductId(Long productId, Long pageSize, String cursorString) {
		DeliveryRequestPagedResponse result = new DeliveryRequestPagedResponse();
		
		try {
			Query<DeliveryRequest> q = ofy().load().type(DeliveryRequest.class)
				.filter("productId", productId)
				.limit(pageSize.intValue())
				.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<DeliveryRequest> iterator = q.iterator();
			result.setDeliveryRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());
			
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by productId: " + e.toString());
		}

		return result;
	}
	
	@Override
	public List<DeliveryRequestData> findByProductId(Long productId) {
		List<DeliveryRequestData> result = new ArrayList<DeliveryRequestData>();

		try {
			List<DeliveryRequest> deliveryRequests = ofy().load().type(DeliveryRequest.class)
				.filter("productId", productId)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty()) {
				result = this.getConverter().convertToDataList(deliveryRequests);
			}
			
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by productId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<DeliveryRequestData> findNotCancelledByProductSizeId(Long productSizeId) {
		List<DeliveryRequestData> result = new ArrayList<DeliveryRequestData>();

		try {
			List<DeliveryRequest> deliveryRequests = ofy().load().type(DeliveryRequest.class)
				.filter("productSizeId", productSizeId)
				//.filter("status !=", DeliveryRequestStatusEnum.CANCELLED)
				.list()
				//filter out CANCELLED in the application, as not possible anymore from datastore
				.stream()
				.filter(deliveryRequest -> 
					!(deliveryRequest.getStatus()).equals(DeliveryRequestStatusEnum.CANCELLED)
				)
				.collect(Collectors.toList());

			if (deliveryRequests != null && !deliveryRequests.isEmpty())
				result = this.getConverter().convertToDataList(deliveryRequests);
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by productSizeId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<DeliveryRequestData> findByFranchiseeId(Long franchiseeId) {
		List<DeliveryRequestData> result = new ArrayList<DeliveryRequestData>();

		try {
			List<DeliveryRequest> deliveryRequests = ofy().load().type(DeliveryRequest.class)
				.filter("franchiseeId", franchiseeId)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty())
				result = this.getConverter().convertToDataList(deliveryRequests);
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by franchiseeId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public DeliveryRequestPagedResponse findByFranchiseeId(Long franchiseeId, Long pageSize, String cursorString) {
		DeliveryRequestPagedResponse result = new DeliveryRequestPagedResponse();
		
		try {
			Query<DeliveryRequest> q = ofy().load().type(DeliveryRequest.class)
			.filter("franchiseeId", franchiseeId)
			.limit(pageSize.intValue())
			.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<DeliveryRequest> iterator = q.iterator();
			result.setDeliveryRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());

		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by franchiseeId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public DeliveryRequestPagedResponse findByProductIdAndFranchiseeId(Long productId, Long franchiseeId, Long pageSize, String cursorString) {
		DeliveryRequestPagedResponse result = new DeliveryRequestPagedResponse();

		try {
			Query<DeliveryRequest> q = ofy().load().type(DeliveryRequest.class)
			.filter("productId", productId)
			.filter("franchiseeId", franchiseeId)
			.limit(pageSize.intValue())
			.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<DeliveryRequest> iterator = q.iterator();
			result.setDeliveryRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());

		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by productId and franchiseeId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public DeliveryRequestPagedResponse findBySupplierIdAndFranchiseeIdAndStatus(Long supplierId, Long franchiseeId, DeliveryRequestStatusEnum status, Long pageSize, String cursorString) {
		DeliveryRequestPagedResponse result = new DeliveryRequestPagedResponse();

		try {
			Query<DeliveryRequest> q = ofy().load().type(DeliveryRequest.class)
			.filter("supplierId", supplierId)
			.filter("franchiseeId", franchiseeId)
			.filter("status", status)
			.limit(pageSize.intValue())
			.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<DeliveryRequest> iterator = q.iterator();
			result.setDeliveryRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());

		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by supplierId and franchiseeId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<DeliveryRequestData> findBySupplierId(Long supplierId) {
		List<DeliveryRequestData> result = new ArrayList<DeliveryRequestData>();

		try {
			List<DeliveryRequest> deliveryRequests = ofy().load().type(DeliveryRequest.class)
				.filter("supplierId", supplierId)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty())
				result = this.getConverter().convertToDataList(deliveryRequests);
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by supplierId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<DeliveryRequestData> findBySupplierIdAndStatus(Long supplierId, DeliveryRequestStatusEnum deliveryStatus) {
		List<DeliveryRequestData> result = new ArrayList<DeliveryRequestData>();

		try {
			List<DeliveryRequest> deliveryRequests = ofy().load().type(DeliveryRequest.class)
				.filter("supplierId", supplierId)
				.filter("status", deliveryStatus)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty())
				result = this.getConverter().convertToDataList(deliveryRequests);
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by supplierId and status: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<DeliveryRequestData> findByProductSizeId(Long id) {
		List<DeliveryRequestData> result = new ArrayList<DeliveryRequestData>();

		try {
			List<DeliveryRequest> deliveryRequests = ofy().load().type(DeliveryRequest.class)
				.filter("productSizeId", id)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty())
				result = this.getConverter().convertToDataList(deliveryRequests);
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by productSizeId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<DeliveryRequestData> findFinishedOlderThanDays(Long numberOfDays, String domainKey) {
		List<DeliveryRequestData> result = new ArrayList<DeliveryRequestData>();

		try {
			//List<DeliveryRequestStatusEnum> statusFilter = new ArrayList<DeliveryRequestStatusEnum>();
			//statusFilter.add(DeliveryRequestStatusEnum.COMPLETED);
			//statusFilter.add(DeliveryRequestStatusEnum.CANCELLED);

			List<DeliveryRequest> deliveryRequests = ofy().load().type(DeliveryRequest.class)
				.filter("updateDate <=", DateUtil.getDateWithOffset(numberOfDays * -1))
				.filter("domainKey", domainKey)
				//.filter("status in", statusFilter)
				.list()
				//filter only COMPLETED and CANCELLED in the application, as not possible anymore from datastore
				.stream()
				.filter(deliveryRequest -> 
					(deliveryRequest.getStatus()).equals(DeliveryRequestStatusEnum.COMPLETED) ||
					(deliveryRequest.getStatus()).equals(DeliveryRequestStatusEnum.CANCELLED)
				)
				.collect(Collectors.toList());
			if (deliveryRequests != null && !deliveryRequests.isEmpty())
				result = this.getConverter().convertToDataList(deliveryRequests);
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest finished older than given number of days: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<DeliveryRequestData> findByNotificationStatus(MailNotificationStatusEnum notificationStatus) {
		List<DeliveryRequestData> result = new ArrayList<DeliveryRequestData>();

		try {
			List<DeliveryRequest> deliveryRequests = ofy().load().type(DeliveryRequest.class)
				.filter("notificationStatus", notificationStatus)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty())
				result = this.getConverter().convertToDataList(deliveryRequests);
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by NotificationStatus: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public DeliveryRequestPagedResponse findByStatus(DeliveryRequestStatusEnum status, Long pageSize, String cursorString) {
		DeliveryRequestPagedResponse result = new DeliveryRequestPagedResponse();

		try {
			Query<DeliveryRequest> q = ofy().load().type(DeliveryRequest.class)
			.filter("status", status)
			.order("-date");

			if (pageSize != null) {
				q = q.limit(pageSize.intValue());
			}
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<DeliveryRequest> iterator = q.iterator();
			result.setDeliveryRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());

		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by Status: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public DeliveryRequestPagedResponse findByDueDateRange(Map<String, Object> parameters, Long pageSize, String cursorString) {

		DeliveryRequestPagedResponse result = new DeliveryRequestPagedResponse();

		try {
			ListBasePagedResponse<DeliveryRequestData> r = this.findByFilter(parameters, "dueDate", pageSize, cursorString);
			result.setDeliveryRequests(r.getItems());
			result.setHasMore(r.getHasMore());
			result.setCursorString(r.getCursorString());
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by dueDateRange: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<DeliveryRequestData> findByPaymentSlipKey(String paymentSlipKey) {
		List<DeliveryRequestData> result = new ArrayList<DeliveryRequestData>();

		try {
			List<DeliveryRequest> deliveryRequests = ofy().load().type(DeliveryRequest.class)
				.filter("paymentSlipKey", paymentSlipKey)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty()) {
				result = this.getConverter().convertToDataList(deliveryRequests);
			}
			
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by paymentSlipKey: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<DeliveryRequestData> findByFiscalFileKey(String fiscalFileKey) {
		List<DeliveryRequestData> result = new ArrayList<DeliveryRequestData>();

		try {
			List<DeliveryRequest> deliveryRequests = ofy().load().type(DeliveryRequest.class)
				.filter("fiscalFileKey", fiscalFileKey)
				.list();
			if (deliveryRequests != null && !deliveryRequests.isEmpty()) {
				result = this.getConverter().convertToDataList(deliveryRequests);
			}
			
		} catch (Exception e) {
			log.warning("Error when querying for DeliveryRequest by fiscalFileKey: " + e.toString());
		}			
	
		return result;
	}

}
