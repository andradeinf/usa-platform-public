package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.bizo.ReviewManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.dao.ReviewRequestDAO;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.enums.ReviewRequesStatusEnum;

public class ReviewManagementBOImpl implements ReviewManagementBO {
	
	private static final Logger log = Logger.getLogger(ReviewManagementBOImpl.class.getName());
	private ReviewRequestDAO reviewRequestDAO;
	private SupplierManagementBO supplierManagement;
	private DeliveryManagementBO deliveryManagement;
	
	public ReviewRequestDAO getReviewRequestDAO() {
		return reviewRequestDAO;
	}

	public void setReviewRequestDAO(ReviewRequestDAO reviewRequestDAO) {
		this.reviewRequestDAO = reviewRequestDAO;
	}

	public SupplierManagementBO getSupplierManagement() {
		return supplierManagement;
	}

	public void setSupplierManagement(SupplierManagementBO supplierManagement) {
		this.supplierManagement = supplierManagement;
	}

	public DeliveryManagementBO getDeliveryManagement() {
		return deliveryManagement;
	}

	public void setDeliveryManagement(DeliveryManagementBO deliveryManagement) {
		this.deliveryManagement = deliveryManagement;
	}

	@Override
	public ReviewRequestData createSupplierReviewRequest(ReviewRequestData supplierReviewRequest) {
		return reviewRequestDAO.save(supplierReviewRequest);
	}

	@Override
	public ReviewRequestData getNextRequestedSupplierReviewRequest(Long franchiseeId, Long userId) {
		
		//get next requested review request for the given franchisee ID
		List<ReviewRequestData> reviewRequests = reviewRequestDAO.findByFromEntityIdAndStatus(franchiseeId, ReviewRequesStatusEnum.REQUESTED, 1L, null).getReviewRequests();
		
		if (reviewRequests != null && !reviewRequests.isEmpty()) {
			
			//check if user already answered a review request in the same day
			Calendar preparedDate = new GregorianCalendar();
			preparedDate.add(GregorianCalendar.DATE, -2);
			Date lastTwoDays = preparedDate.getTime();
			
			List<ReviewRequestData> userReviewRequests = reviewRequestDAO.findByReviewerUserIdAndReviewerDate(userId, lastTwoDays);
			
			if (userReviewRequests != null && !userReviewRequests.isEmpty()) {
				//return null to avoid present reviews too often to the same user
				log.info("Skipping review request to user " + userId + " as he has already reviewed " + userReviewRequests.size() + " requests since " + lastTwoDays.toString());
				return null;
			} else {
				return reviewRequests.get(0);
			}			
			
		} else {
			return null;
		}
	}
	
	@Override
	public ReviewRequestData cancelRequestedSupplierReviewRequest(Long id, Long userId) {
		ReviewRequestData response = reviewRequestDAO.findById(id);
		
		response.setReviewerDate(new Date());
		response.setReviewerUserId(userId);
		response.setStatus(ReviewRequesStatusEnum.REJECTED);
		reviewRequestDAO.save(response);
		
		return response;
	}
	
	@Override
	public ReviewRequestData addRequestedSupplierReviewRequestRates(Long id, Long userId, Long qualityRate, Long deliveryRate, Long priceRate, Long paymentConditionRate) {
		ReviewRequestData response = reviewRequestDAO.findById(id);
		
		// Do not apply any change if the request was already evaluated
		// (by another user, for example)
		if (response.getStatus().equals(ReviewRequesStatusEnum.REQUESTED)) {
			
			response.setReviewerDate(new Date());
			response.setReviewerUserId(userId);
			response.setStatus(ReviewRequesStatusEnum.COMPLETED);
			response.setQualityRate(qualityRate);
			response.setDeliveryRate(deliveryRate);
			response.setPriceRate(priceRate);
			response.setPaymentConditionRate(paymentConditionRate);
			reviewRequestDAO.save(response);
			
			//Asynchronously process supplier rating
			Queue queue = QueueFactory.getDefaultQueue();
			queue.add(TaskOptions.Builder.withUrl("/job/async/processSupplierReviewRequest/" + id).countdownMillis(30000));
		}
		
		return response;
	}
	
	@Override
	public void processSupplierReviewRequesttAsync(Long id) {
		
		ReviewRequestData reviewRequest = reviewRequestDAO.findById(id);
		
		// Do not process any change if the request was already consolidated
		// (by another queue request, for example)
		if (reviewRequest.getStatus().equals(ReviewRequesStatusEnum.COMPLETED)) {
			
			SupplierData supplier = supplierManagement.getSupplier(reviewRequest.getToEntityId());
			supplier.setQualityRate(((supplier.getQualityRate() * supplier.getQualityCount()) + reviewRequest.getQualityRate()) / (supplier.getQualityCount() + 1));
			supplier.setQualityCount(supplier.getQualityCount() + 1);
			supplier.setDeliveryRate(((supplier.getDeliveryRate() * supplier.getDeliveryCount()) + reviewRequest.getDeliveryRate()) / (supplier.getDeliveryCount() + 1));
			supplier.setDeliveryCount(supplier.getDeliveryCount() + 1);
			supplier.setPriceRate(((supplier.getPriceRate() * supplier.getPriceCount()) + reviewRequest.getPriceRate()) / (supplier.getPriceCount() + 1));
			supplier.setPriceCount(supplier.getPriceCount() + 1);
			supplier.setPaymentConditionRate(((supplier.getPaymentConditionRate() * supplier.getPaymentConditionCount()) + reviewRequest.getPaymentConditionRate()) / (supplier.getPaymentConditionCount() + 1));
			supplier.setPaymentConditionCount(supplier.getPaymentConditionCount() + 1);
			supplierManagement.saveSupplier(supplier);
			
			reviewRequest.setStatus(ReviewRequesStatusEnum.CONSOLIDATED);
			reviewRequestDAO.save(reviewRequest);
		}
	}
	
	@Override
	public List<DeliveryRequestHistoryData> getReviewRequestDeliveryRequests(Long id) {
		List<DeliveryRequestHistoryData> deliveryRequests = new ArrayList<DeliveryRequestHistoryData>();
		
		ReviewRequestData reviewRequest = reviewRequestDAO.findById(id);
		if (reviewRequest.getDeliveryRequestHistoryIds() != null && !reviewRequest.getDeliveryRequestHistoryIds().isEmpty()) {
			for (Long deliveryRequestId : reviewRequest.getDeliveryRequestHistoryIds()) {
				DeliveryRequestHistoryData deliveryRequest = deliveryManagement.getDeliveryRequestHistory(deliveryRequestId);
				if (deliveryRequest != null) {
					deliveryRequests.add(deliveryRequest);
				}
			}
		}
		
		return deliveryRequests;
	}

	@Override
	public Map<Long, ProductData> getDeliveryRequestsProducts(List<DeliveryRequestHistoryData> deliveryRequests) {
		return deliveryManagement.getDeliveryRequestHistoriesProducts(deliveryRequests);
	}
	
	@Override
	public SupplierData getReviewRequestSupplier(Long supplierId) {
		return supplierManagement.getSupplier(supplierId);
	}
}
