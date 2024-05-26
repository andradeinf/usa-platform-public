package br.com.usasistemas.usaplatform.dao.response;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;

public class ReviewRequestPagedResponse extends BasePagedResponse {
	
	private List<ReviewRequestData> reviewRequests;

	public List<ReviewRequestData> getReviewRequests() {
		return reviewRequests;
	}

	public void setReviewRequests(List<ReviewRequestData> reviewRequests) {
		this.reviewRequests = reviewRequests;
	}

}
