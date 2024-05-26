package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.QueryResults;
import com.googlecode.objectify.cmd.Query;

import br.com.usasistemas.usaplatform.dao.ManufactureRequestHistoryDAO;
import br.com.usasistemas.usaplatform.dao.response.ManufactureRequestHistoryPagedResponse;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestHistoryData;
import br.com.usasistemas.usaplatform.model.entity.ManufactureRequestHistory;

public class GAEManufactureRequestHistoryDAOImpl extends GAEGenericDAOImpl<ManufactureRequestHistory, ManufactureRequestHistoryData> implements ManufactureRequestHistoryDAO {
	
	private static final Logger log = Logger.getLogger(GAEManufactureRequestHistoryDAOImpl.class.getName());

	@Override
	public List<ManufactureRequestHistoryData> findByProductId (Long productId) {
		List<ManufactureRequestHistoryData> result = new ArrayList<ManufactureRequestHistoryData>();

		try {
			List<ManufactureRequestHistory> manufactureRequests = ofy().load().type(ManufactureRequestHistory.class)
				.filter("productId", productId)
				.list();
			if (manufactureRequests != null && !manufactureRequests.isEmpty()) {
				result = this.getConverter().convertToDataList(manufactureRequests);
			}
		} catch (Exception e) {
			log.warning("Error when querying for ManufactureRequestHistory by productId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public ManufactureRequestHistoryPagedResponse findByProductId (Long productId, Long pageSize, String cursorString) {
		ManufactureRequestHistoryPagedResponse result = new ManufactureRequestHistoryPagedResponse();

		try {
			Query<ManufactureRequestHistory> q = ofy().load().type(ManufactureRequestHistory.class)
				.filter("productId", productId)
				.limit(pageSize.intValue())
				.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<ManufactureRequestHistory> iterator = q.iterator();
			result.setManufactureRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());	

		} catch (Exception e) {
			log.warning("Error when querying for ManufactureRequestHistory by productId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<ManufactureRequestHistoryData> findByFranchisorId(Long franchisorId) {
		List<ManufactureRequestHistoryData> result = new ArrayList<ManufactureRequestHistoryData>();

		try {
			List<ManufactureRequestHistory> manufactureRequests = ofy().load().type(ManufactureRequestHistory.class)
				.filter("franchisorId", franchisorId)
				.list();
			if (manufactureRequests != null && !manufactureRequests.isEmpty())
				result = this.getConverter().convertToDataList(manufactureRequests);
		} catch (Exception e) {
			log.warning("Error when querying for ManufactureRequestHistory by franchisorId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public ManufactureRequestHistoryPagedResponse findByFranchisorId(Long franchisorId, Long pageSize, String cursorString) {
		ManufactureRequestHistoryPagedResponse result = new ManufactureRequestHistoryPagedResponse();

		try {
			Query<ManufactureRequestHistory> q = ofy().load().type(ManufactureRequestHistory.class)
				.filter("franchisorId", franchisorId)
				.limit(pageSize.intValue())
				.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<ManufactureRequestHistory> iterator = q.iterator();
			result.setManufactureRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());

		} catch (Exception e) {
			log.warning("Error when querying for ManufactureRequestHistory by franchisorId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<ManufactureRequestHistoryData> findBySupplierId(Long supplierId) {
		List<ManufactureRequestHistoryData> result = new ArrayList<ManufactureRequestHistoryData>();

		try {
			List<ManufactureRequestHistory> manufactureRequests = ofy().load().type(ManufactureRequestHistory.class)
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
	public List<ManufactureRequestHistoryData> findByProductSizeId(Long id) {
		List<ManufactureRequestHistoryData> result = new ArrayList<ManufactureRequestHistoryData>();

		try {
			List<ManufactureRequestHistory> manufactureRequests = ofy().load().type(ManufactureRequestHistory.class)
				.filter("productSizeId", id)
				.list();
			if (manufactureRequests != null && !manufactureRequests.isEmpty())
				result = this.getConverter().convertToDataList(manufactureRequests);
		} catch (Exception e) {
			log.warning("Error when querying for ManufactureRequestHistory by productSizeId: " + e.toString());
		}			
	
		return result;
	}
	
}
