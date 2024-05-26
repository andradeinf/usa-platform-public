package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.usasistemas.usaplatform.model.enums.AnnouncementStatusEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnouncementData {
	
	private Long id;
	private Long franchisorId;
	private Date date;
	private String title;
	private String message;
	private List<Long> franchiseeIds;
	private AnnouncementStatusEnum status;
	private String domainKey;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Long> getFranchiseeIds() {
		return franchiseeIds;
	}

	public void setFranchiseeIds(List<Long> franchiseeIds) {
		this.franchiseeIds = franchiseeIds;
	}

	public AnnouncementStatusEnum getStatus() {
		return status;
	}

	public void setStatus(AnnouncementStatusEnum status) {
		this.status = status;
	}

	public String getDomainKey() {
		return domainKey;
	}

	public void setDomainKey(String domainKey) {
		this.domainKey = domainKey;
	}
	
}
