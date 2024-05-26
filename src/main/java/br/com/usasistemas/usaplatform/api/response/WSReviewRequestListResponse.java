package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;

public class WSReviewRequestListResponse extends GenericResponse {

	private List<ReviewRequestData> reviewRequests;
	private Map<Long, SupplierData> suppliers;
	
	public List<ReviewRequestData> getReviewRequests() {
		return reviewRequests;
	}

	public void setReviewRequests(List<ReviewRequestData> reviewRequests) {
		this.reviewRequests = reviewRequests;
	}

	public Map<Long, SupplierData> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(Map<Long, SupplierData> suppliers) {
		this.suppliers = suppliers;
	}

}
