package br.com.usasistemas.usaplatform.dao.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.ProductSizePriceHistoryData;

public class ProductSizePriceHistoryPagedResponse extends BasePagedResponse {
	
	private List<ProductSizePriceHistoryData> productSizePriceHistories;

	public List<ProductSizePriceHistoryData> getProductSizePriceHistories() {
		return productSizePriceHistories;
	}

	public void setProductSizePriceHistories(List<ProductSizePriceHistoryData> productSizePriceHistories) {
		this.productSizePriceHistories = productSizePriceHistories;
	}

}
