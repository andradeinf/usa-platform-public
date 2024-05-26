package br.com.usasistemas.usaplatform.dao.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;

public class ManufactureRequestPagedResponse extends BasePagedResponse {
	
	private List<ManufactureRequestData> manufactureRequests;

	public List<ManufactureRequestData> getManufactureRequests() {
		return manufactureRequests;
	}

	public void setManufactureRequests(List<ManufactureRequestData> manufactureRequests) {
		this.manufactureRequests = manufactureRequests;
	}

}
