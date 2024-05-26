package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.dao.response.ProductSizePriceHistoryPagedResponse;
import br.com.usasistemas.usaplatform.model.data.ProductSizePriceHistoryData;
import br.com.usasistemas.usaplatform.model.entity.ProductSizePriceHistory;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;

public interface ProductSizePriceHistoryDAO extends GenericDAO<ProductSizePriceHistory, ProductSizePriceHistoryData> {
	
	public ProductSizePriceHistoryPagedResponse findByProductId(Long productId, Long pageSize, String cursorString);
	public List<ProductSizePriceHistoryData> findByNotificationStatus(MailNotificationStatusEnum notificationStatus);
	public List<ProductSizePriceHistoryData> findByProductSizeId(Long productSizeId);
	
}
