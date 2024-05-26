package br.com.usasistemas.usaplatform.dao;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.dao.response.ReviewRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;
import br.com.usasistemas.usaplatform.model.entity.ReviewRequest;
import br.com.usasistemas.usaplatform.model.enums.ReviewRequesStatusEnum;

public interface ReviewRequestDAO extends GenericDAO<ReviewRequest, ReviewRequestData> {

	public ReviewRequestPagedResponse findByFromEntityIdAndStatus(Long fromEntityId, ReviewRequesStatusEnum status, Long pageSize, String cursorString);
	public List<ReviewRequestData> findByReviewerUserIdAndReviewerDate(Long userId, Date lastTwoDays);
	
}
