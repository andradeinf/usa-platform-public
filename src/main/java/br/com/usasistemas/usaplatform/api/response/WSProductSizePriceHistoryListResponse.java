package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.data.ProductSizePriceHistoryData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;

public class WSProductSizePriceHistoryListResponse extends GenericResponse {

	private List<ProductSizePriceHistoryData> productSizePriceHistories;
	private Map<Long, ProductSizeData> productSizes;
	private Map<Long, PublicUserData> users;
	private String cursorString;

	public List<ProductSizePriceHistoryData> getProductSizePriceHistories() {
		return productSizePriceHistories;
	}

	public void setProductSizePriceHistories(List<ProductSizePriceHistoryData> productSizePriceHistories) {
		this.productSizePriceHistories = productSizePriceHistories;
	}

	public Map<Long, ProductSizeData> getProductSizes() {
		return productSizes;
	}

	public void setProductSizes(Map<Long, ProductSizeData> productSizes) {
		this.productSizes = productSizes;
	}

	public Map<Long, PublicUserData> getUsers() {
		return users;
	}

	public void setUsers(Map<Long, PublicUserData> users) {
		this.users = users;
	}

	public String getCursorString() {
		return cursorString;
	}

	public void setCursorString(String cursorString) {
		this.cursorString = cursorString;
	}

}
