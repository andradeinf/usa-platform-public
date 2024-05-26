package br.com.usasistemas.usaplatform.dao.response;

import java.util.List;

public class ListBasePagedResponse<D> extends BasePagedResponse {
	
	private List<D> items;

	public List<D> getItems() {
		return items;
	}

	public void setItems(List<D> items) {
		this.items = items;
	}

}
