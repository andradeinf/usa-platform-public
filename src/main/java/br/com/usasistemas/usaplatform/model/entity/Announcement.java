package br.com.usasistemas.usaplatform.model.entity;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import br.com.usasistemas.usaplatform.model.enums.AnnouncementStatusEnum;

@Entity
@Index
public class Announcement {
	
	@Id private Long id;
	private Long franchisorId;
	private Date date;
	private String title;
	@Unindex String message;
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
