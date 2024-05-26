package br.com.usasistemas.usaplatform.model.entity;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Entity
@Index
public class CalendarEvent {
	
	@Id private Long id;
	private UserProfileEnum entityProfile;	//Administrator/Franchisor/Franchisee/Supplier
	private Long entityId;					//0 for Administrator/FranchisorId/FranchiseeId/SupplierId
	private Date fromHour;
	private Date toHour;
	private Boolean allDay;
	private String title;
	private String location;
	private String description;
	private Boolean accessRestricted;
	private List<Long> franchiseeIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfileEnum getEntityProfile() {
		return entityProfile;
	}

	public void setEntityProfile(UserProfileEnum entityProfile) {
		this.entityProfile = entityProfile;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getTitle() {
		return title;
	}

	public Date getFromHour() {
		return fromHour;
	}

	public void setFromHour(Date fromHour) {
		this.fromHour = fromHour;
	}

	public Date getToHour() {
		return toHour;
	}

	public void setToHour(Date toHour) {
		this.toHour = toHour;
	}

	public Boolean getAllDay() {
		return allDay;
	}

	public void setAllDay(Boolean allDay) {
		this.allDay = allDay;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
