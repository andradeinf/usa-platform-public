package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;
import java.util.List;

public class TrainingData {
	
	private Long id;
	private Long franchisorId;
	private Date updatedDate;
	private Long order;
	private String title;
	private String description;
	private Long videoId;
	private String videoName;
	private String videoContentType;
	private String videoStorePath;
	private Long videoSize;
	private String videoFileKey;
	private Boolean accessRestricted;
	private List<Long> franchiseeIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFranchisorId() {
		return franchisorId;
	}

	public void setFranchisorId(Long franchisorId) {
		this.franchisorId = franchisorId;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoContentType() {
		return videoContentType;
	}

	public void setVideoContentType(String videoContentType) {
		this.videoContentType = videoContentType;
	}

	public String getVideoStorePath() {
		return videoStorePath;
	}

	public void setVideoStorePath(String videoStorePath) {
		this.videoStorePath = videoStorePath;
	}

	public Long getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(Long videoSize) {
		this.videoSize = videoSize;
	}

	public String getVideoFileKey() {
		return videoFileKey;
	}

	public void setVideoFileKey(String videoFileKey) {
		this.videoFileKey = videoFileKey;
	}

	public Boolean getAccessRestricted() {
		return accessRestricted;
	}

	public void setAccessRestricted(Boolean accessRestricted) {
		this.accessRestricted = accessRestricted;
	}

	public List<Long> getFranchiseeIds() {
		return franchiseeIds;
	}

	public void setFranchiseeIds(List<Long> franchiseeIds) {
		this.franchiseeIds = franchiseeIds;
	}

}
