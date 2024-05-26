package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.QueryResults;
import com.googlecode.objectify.cmd.Query;

import br.com.usasistemas.usaplatform.dao.ReviewRequestDAO;
import br.com.usasistemas.usaplatform.dao.response.ReviewRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;
import br.com.usasistemas.usaplatform.model.entity.ReviewRequest;
import br.com.usasistemas.usaplatform.model.enums.ReviewRequesStatusEnum;

public class GAEReviewRequestDAOImpl extends GAEGenericDAOImpl<ReviewRequest, ReviewRequestData> implements ReviewRequestDAO {

	private static final Logger log = Logger.getLogger(GAEReviewRequestDAOImpl.class.getName());
	
	@Override
	public ReviewRequestPagedResponse findByFromEntityIdAndStatus(Long fromEntityId, ReviewRequesStatusEnum status, Long pageSize, String cursorString) {
		ReviewRequestPagedResponse result = new ReviewRequestPagedResponse();

		try {
			Query<ReviewRequest> q = ofy().load().type(ReviewRequest.class)
				.filter("fromEntityId", fromEntityId)
				.filter("status", status)
				.limit(pageSize.intValue())
				.order("date");
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<ReviewRequest> iterator = q.iterator();
			result.setReviewRequests(this.getConverter().convertToDataList(iterator, null));
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());
			
		} catch (Exception e) {
			log.warning("Error when querying for ReviewRequest by fromEntityId and status: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<ReviewRequestData> findByReviewerUserIdAndReviewerDate(Long userId, Date lastTwoDays) {
		List<ReviewRequestData> result = new ArrayList<ReviewRequestData>();

		try {
			List<ReviewRequest> reviewRequests = ofy().load().type(ReviewRequest.class)
				.filter("reviewerUserId", userId)
				.filter("reviewerDate >", lastTwoDays)
				.list();
			if (reviewRequests != null && !reviewRequests.isEmpty())
				result = this.getConverter().convertToDataList(reviewRequests);
		} catch (Exception e) {
			log.warning("Error when querying for reviewRequests by reviewerUserId and reviewerDate: " + e.toString());
		}			
	
		return result;
	}

}
