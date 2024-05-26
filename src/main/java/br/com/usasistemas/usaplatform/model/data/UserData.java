package br.com.usasistemas.usaplatform.model.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserData implements Serializable {
	
	private Long id;
	private String email;
	private String username;
	private String passwordHash;
	private String passwordSalt;
	private String name;
	private String preferedDomainKey;
	private Boolean enabled;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPreferedDomainKey() {
		return preferedDomainKey;
	}

	public void setPreferedDomainKey(String preferedDomainKey) {
		this.preferedDomainKey = preferedDomainKey;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

}
