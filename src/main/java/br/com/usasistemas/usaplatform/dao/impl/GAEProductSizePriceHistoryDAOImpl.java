package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.QueryResults;
import com.googlecode.objectify.cmd.Query;

import br.com.usasistemas.usaplatform.dao.ProductSizePriceHistoryDAO;
import br.com.usasistemas.usaplatform.dao.response.ProductSizePriceHistoryPagedResponse;
import br.com.usasistemas.usaplatform.model.data.ProductSizePriceHistoryData;
import br.com.usasistemas.usaplatform.model.entity.ProductSizePriceHistory;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;

public class GAEProductSizePriceHistoryDAOImpl extends GAEGenericDAOImpl<ProductSizePriceHistory, ProductSizePriceHistoryData> implements ProductSizePriceHistoryDAO {
	
	private static final Logger log = Logger.getLogger(GAEProductSizePriceHistoryDAOImpl.class.getName());

	@Override
	public ProductSizePriceHistoryPagedResponse findByProductId(Long productId, Long pageSize, String cursorString) {
		ProductSizePriceHistoryPagedResponse result =  new ProductSizePriceHistoryPagedResponse();
		
		try {
			Query<ProductSizePriceHistory> q = ofy().load().type(ProductSizePriceHistory.class)
				.filter("productId", productId)
				.limit(pageSize.intValue())
				.order("-date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<ProductSizePriceHistory> iterator = q.iterator();
			result.setProductSizePriceHistories(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());

		} catch (Exception e) {
			log.warning("Error when querying for productSizePriceHistory by productId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<ProductSizePriceHistoryData> findByNotificationStatus(MailNotificationStatusEnum notificationStatus) {
		List<ProductSizePriceHistoryData> result = new ArrayList<ProductSizePriceHistoryData>();

		try {
			List<ProductSizePriceHistory> productSizePriceHistories = ofy().load().type(ProductSizePriceHistory.class)
				.filter("notificationStatus", notificationStatus)
				.list();
			if (productSizePriceHistories != null && !productSizePriceHistories.isEmpty())
				result = this.getConverter().convertToDataList(productSizePriceHistories);
		} catch (Exception e) {
			log.warning("Error when querying for ProductSizePriceHistory by NotificationStatus: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<ProductSizePriceHistoryData> findByProductSizeId(Long productSizeId) {
		List<ProductSizePriceHistoryData> result = new ArrayList<ProductSizePriceHistoryData>();

		try {
			List<ProductSizePriceHistory> productSizePriceHistories = ofy().load().type(ProductSizePriceHistory.class)
				.filter("productSizeId", productSizeId)
				.list();
			if (productSizePriceHistories != null && !productSizePriceHistories.isEmpty())
				result = this.getConverter().convertToDataList(productSizePriceHistories);
		} catch (Exception e) {
			log.warning("Error when querying for ProductSizePriceHistory by productSizeId: " + e.toString());
		}			
	
		return result;
	}

}
