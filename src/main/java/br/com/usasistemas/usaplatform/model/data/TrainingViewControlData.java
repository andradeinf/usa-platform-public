package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;

import br.com.usasistemas.usaplatform.model.enums.TrainingViewControlStatusEnum;

public class TrainingViewControlData {
	
	private Long id;
	private Date updatedDate;
	private Long trainingId;
	private Long franchiseeId;
	private Long franchiseeUserId;
	private Long userId;
	private Long viewCurrentTime;
	private TrainingViewControlStatusEnum viewStatus;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public Long getTrainingId() {
		return this.trainingId;
	}

	public void setTrainingId(Long trainingId) {
		this.trainingId = trainingId;
	}

	public Long getFranchiseeId() {
		return this.franchiseeId;
	}

	public void setFranchiseeId(Long franchiseeId) {
		this.franchiseeId = franchiseeId;
	}

	public Long getFranchiseeUserId() {
		return this.franchiseeUserId;
	}

	public void setFranchiseeUserId(Long franchiseeUserId) {
		this.franchiseeUserId = franchiseeUserId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getViewCurrentTime() {
		return this.viewCurrentTime;
	}

	public void setViewCurrentTime(Long viewCurrentTime) {
		this.viewCurrentTime = viewCurrentTime;
	}

	public TrainingViewControlStatusEnum getViewStatus() {
		return this.viewStatus;
	}
	
	public void setViewStatus(TrainingViewControlStatusEnum viewStatus) {
		this.viewStatus = viewStatus;
	}
}
