package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;
import java.util.List;

import br.com.usasistemas.usaplatform.model.enums.ReviewRequesStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class ReviewRequestData {
	
	private Long id;
	private Date date;
	private UserProfileEnum fromEntityProfile;
	private Long fromEntityId;
	private Long reviewerUserId;
	private Date reviewerDate;
	private Long qualityRate;
	private Long deliveryRate;
	private Long priceRate;
	private Long paymentConditionRate;
	private UserProfileEnum toEntityProfile;
	private Long toEntityId;
	private List<Long> deliveryRequestHistoryIds;
	private ReviewRequesStatusEnum status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public UserProfileEnum getFromEntityProfile() {
		return fromEntityProfile;
	}

	public void setFromEntityProfile(UserProfileEnum fromEntityProfile) {
		this.fromEntityProfile = fromEntityProfile;
	}

	public Long getFromEntityId() {
		return fromEntityId;
	}

	public void setFromEntityId(Long fromEntityId) {
		this.fromEntityId = fromEntityId;
	}

	public Long getReviewerUserId() {
		return reviewerUserId;
	}

	public void setReviewerUserId(Long reviewerUserId) {
		this.reviewerUserId = reviewerUserId;
	}

	public Date getReviewerDate() {
		return reviewerDate;
	}

	public void setReviewerDate(Date reviewerDate) {
		this.reviewerDate = reviewerDate;
	}

	public Long getQualityRate() {
		return qualityRate;
	}

	public void setQualityRate(Long qualityRate) {
		this.qualityRate = qualityRate;
	}

	public Long getDeliveryRate() {
		return deliveryRate;
	}

	public void setDeliveryRate(Long deliveryRate) {
		this.deliveryRate = deliveryRate;
	}

	public Long getPriceRate() {
		return priceRate;
	}

	public void setPriceRate(Long priceRate) {
		this.priceRate = priceRate;
	}

	public Long getPaymentConditionRate() {
		return paymentConditionRate;
	}

	public void setPaymentConditionRate(Long paymentConditionRate) {
		this.paymentConditionRate = paymentConditionRate;
	}

	public UserProfileEnum getToEntityProfile() {
		return toEntityProfile;
	}

	public void setToEntityProfile(UserProfileEnum toEntityProfile) {
		this.toEntityProfile = toEntityProfile;
	}

	public Long getToEntityId() {
		return toEntityId;
	}

	public void setToEntityId(Long toEntityId) {
		this.toEntityId = toEntityId;
	}

	public List<Long> getDeliveryRequestHistoryIds() {
		return deliveryRequestHistoryIds;
	}

	public void setDeliveryRequestHistoryIds(List<Long> deliveryRequestHistoryIds) {
		this.deliveryRequestHistoryIds = deliveryRequestHistoryIds;
	}

	public ReviewRequesStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ReviewRequesStatusEnum status) {
		this.status = status;
	}

}
