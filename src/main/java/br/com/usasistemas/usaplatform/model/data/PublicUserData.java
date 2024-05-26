package br.com.usasistemas.usaplatform.model.data;

import java.io.Serializable;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@SuppressWarnings("serial")
public class PublicUserData implements Serializable {

	private Long id;
	private String email;
	private String username;
	private String name;
	private String preferedDomainKey;
	private String password;
	private UserProfileEnum selectedRole;
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

	public String getPreferedDomainKey() {
		return preferedDomainKey;
	}

	public void setPreferedDomainKey(String preferedDomainKey) {
		this.preferedDomainKey = preferedDomainKey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserProfileEnum getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(UserProfileEnum selectedRole) {
		this.selectedRole = selectedRole;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

}
