package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.QueryResults;
import com.googlecode.objectify.cmd.Query;

import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.dao.ManufactureRequestDAO;
import br.com.usasistemas.usaplatform.dao.response.ManufactureRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.entity.ManufactureRequest;
import br.com.usasistemas.usaplatform.model.enums.ManufactureRequestStatusEnum;

public class GAEManufactureRequestDAOImpl extends GAEGenericDAOImpl<ManufactureRequest, ManufactureRequestData> implements ManufactureRequestDAO {
	
	private static final Logger log = Logger.getLogger(GAEManufactureRequestDAOImpl.class.getName());

	@Override
	public List<ManufactureRequestData> findByProductId(Long productId) {
		List<ManufactureRequestData> result = new ArrayList<ManufactureRequestData>();

		try {
			List<ManufactureRequest> manufactureRequests = ofy().load().type(ManufactureRequest.class)
				.filter("productId", productId)
				.list();
			if (manufactureRequests != null && !manufactureRequests.isEmpty())
				result = this.getConverter().convertToDataList(manufactureRequests);
		} catch (Exception e) {
			log.warning("Error when querying for ManufactureRequest by productId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public ManufactureRequestPagedResponse findByProductId (Long productId, Long pageSize, String cursorString) {
		ManufactureRequestPagedResponse result = new ManufactureRequestPagedResponse();

		try {
			Query<ManufactureRequest> q = ofy().load().type(ManufactureRequest.class)
				.filter("productId", productId)
				.limit(pageSize.intValue())
				.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<ManufactureRequest> iterator = q.iterator();
			result.setManufactureRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());		
			
		} catch (Exception e) {
			log.warning("Error when querying for ManufactureRequest by productId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<ManufactureRequestData> findCompletedByProductSizeId(Long productSizeId) {
		List<ManufactureRequestData> result = new ArrayList<ManufactureRequestData>();

		try {
			//List<ManufactureRequestStatusEnum> statusFilter = new ArrayList<ManufactureRequestStatusEnum>();
			//statusFilter.add(ManufactureRequestStatusEnum.COMPLETED);
			//statusFilter.add(ManufactureRequestStatusEnum.ADJUSTMENT);

			List<ManufactureRequest> manufactureRequests = ofy().load().type(ManufactureRequest.class)
				.filter("productSizeId", productSizeId)
				//.filter("status in", statusFilter)
				.list()
				//filter only COMPLETED and ADJUSTMENT in the application, as not possible anymore from datastore
				.stream()
				.filter(deliveryRequest -> 
					(deliveryRequest.getStatus()).equals(ManufactureRequestStatusEnum.COMPLETED) ||
					(deliveryRequest.getStatus()).equals(ManufactureRequestStatusEnum.ADJUSTMENT)
				)
				.collect(Collectors.toList());
			if (manufactureRequests != null && !manufactureRequests.isEmpty())
				result = this.getConverter().convertToDataList(manufactureRequests);
		} catch (Exception e) {
			log.warning("Error when querying for ManufactureRequest by productSizeId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<ManufactureRequestData> findByFranchisorId(Long franchisorId) {
		List<ManufactureRequestData> result = new ArrayList<ManufactureRequestData>();

		try {
			List<ManufactureRequest> manufactureRequests = ofy().load().type(ManufactureRequest.class)
				.filter("franchisorId", franchisorId)
				.list();
			if (manufactureRequests != null && !manufactureRequests.isEmpty())
				result = this.getConverter().convertToDataList(manufactureRequests);
		} catch (Exception e) {
			log.warning("Error when querying for ManufacturRequest by franchisorId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public ManufactureRequestPagedResponse findByFranchisorId(Long franchisorId, Long pageSize, String cursorString) {
		ManufactureRequestPagedResponse result = new ManufactureRequestPagedResponse();

		try {
			Query<ManufactureRequest> q = ofy().load().type(ManufactureRequest.class)
			.filter("franchisorId", franchisorId)
			.limit(pageSize.intValue())
			.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<ManufactureRequest> iterator = q.iterator();
			result.setManufactureRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());	

		} catch (Exception e) {
			log.warning("Error when querying for ManufacturRequest by franchisorId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<ManufactureRequestData> findBySupplierId(Long supplierId) {
		List<ManufactureRequestData> result = new ArrayList<ManufactureRequestData>();

		try {
			List<ManufactureRequest> manufactureRequests = ofy().load().type(ManufactureRequest.class)
				.filter("supplierId", supplierId)
				.list();
			if (manufactureRequests != null && !manufactureRequests.isEmpty())
				result = this.getConverter().convertToDataList(manufactureRequests);
		} catch (Exception e) {
			log.warning("Error when querying for ManufacturRequest by supplierId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<ManufactureRequestData> findBySupplierIdAndStatus(Long supplierId, ManufactureRequestStatusEnum manufactureStatus) {
		List<ManufactureRequestData> result = new ArrayList<ManufactureRequestData>();

		try {
			List<ManufactureRequest> manufactureRequests = ofy().load().type(ManufactureRequest.class)
				.filter("supplierId", supplierId)
				.filter("status", manufactureStatus)
				.list();
			if (manufactureRequests != null && !manufactureRequests.isEmpty())
				result = this.getConverter().convertToDataList(manufactureRequests);
		} catch (Exception e) {
			log.warning("Error when querying for ManufacturRequest by supplierId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<ManufactureRequestData> findByProductSizeId(Long id) {
		List<ManufactureRequestData> result = new ArrayList<ManufactureRequestData>();

		try {
			List<ManufactureRequest> manufactureRequests = ofy().load().type(ManufactureRequest.class)
				.filter("productSizeId", id)
				.list();
			if (manufactureRequests != null && !manufactureRequests.isEmpty())
				result = this.getConverter().convertToDataList(manufactureRequests);
		} catch (Exception e) {
			log.warning("Error when querying for ManufactureRequest by productSizeId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<ManufactureRequestData> findFinishedOlderThanDays(Long numberOfDays, String domainKey) {
		List<ManufactureRequestData> result = new ArrayList<ManufactureRequestData>();

		try {
			//List<ManufactureRequestStatusEnum> statusFilter = new ArrayList<ManufactureRequestStatusEnum>();
			//statusFilter.add(ManufactureRequestStatusEnum.COMPLETED);
			//statusFilter.add(ManufactureRequestStatusEnum.ADJUSTMENT);
			//statusFilter.add(ManufactureRequestStatusEnum.CANCELLED);

			List<ManufactureRequest> manufactureRequests = ofy().load().type(ManufactureRequest.class)
				.filter("updateDate <=", DateUtil.getDateWithOffset(numberOfDays * -1))
				.filter("domainKey", domainKey)
				//.filter("status in", statusFilter)
				.list()
				//filter only COMPLETED, ADJUSTMENT and CANCELLED in the application, as not possible anymore from datastore
				.stream()
				.filter(deliveryRequest -> 
					(deliveryRequest.getStatus()).equals(ManufactureRequestStatusEnum.COMPLETED) ||
					(deliveryRequest.getStatus()).equals(ManufactureRequestStatusEnum.ADJUSTMENT) ||
					(deliveryRequest.getStatus()).equals(ManufactureRequestStatusEnum.CANCELLED)
				)
				.collect(Collectors.toList());
			if (manufactureRequests != null && !manufactureRequests.isEmpty())
				result = this.getConverter().convertToDataList(manufactureRequests);
		} catch (Exception e) {
			log.warning("Error when querying for ManufactureRequest finished older than given number of days: " + e.toString());
		}			
	
		return result;
	}

}
