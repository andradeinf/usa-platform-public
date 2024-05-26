package br.com.usasistemas.usaplatform.model.entity;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Entity
@Index
public class Tutorial {
	
	@Id private Long id;
	private String name;
	private String description;
	private String url;
	private Long order;
	private UserProfileEnum userProfile;
	private List<String> domainKeys;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public UserProfileEnum getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfileEnum userProfile) {
		this.userProfile = userProfile;
	}

	public List<String> getDomainKeys() {
		return domainKeys;
	}

	public void setDomainKeys(List<String> domainKeys) {
		this.domainKeys = domainKeys;
	}
	
}
