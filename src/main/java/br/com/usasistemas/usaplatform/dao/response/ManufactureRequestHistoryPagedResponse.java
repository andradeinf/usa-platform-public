package br.com.usasistemas.usaplatform.dao.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.ManufactureRequestHistoryData;

public class ManufactureRequestHistoryPagedResponse extends BasePagedResponse {
	
	private List<ManufactureRequestHistoryData> manufactureRequests;

	public List<ManufactureRequestHistoryData> getManufactureRequests() {
		return manufactureRequests;
	}

	public void setManufactureRequests(List<ManufactureRequestHistoryData> manufactureRequests) {
		this.manufactureRequests = manufactureRequests;
	}

}
