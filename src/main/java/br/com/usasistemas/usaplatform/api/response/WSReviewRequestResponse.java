package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;

public class WSReviewRequestResponse extends GenericResponse {

	private ReviewRequestData reviewRequest;
	private SupplierData supplier;
	
	public ReviewRequestData getReviewRequest() {
		return reviewRequest;
	}
	
	public void setReviewRequest(ReviewRequestData reviewRequest) {
		this.reviewRequest = reviewRequest;
	}
	
	public SupplierData getSupplier() {
		return supplier;
	}
	
	public void setSupplier(SupplierData supplier) {
		this.supplier = supplier;
	}

}
