package br.com.usasistemas.usaplatform.model.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FranchisorUserData implements Serializable {
	
	private Long id;
	private Long userId;
	private Long franchisorId;
	private PublicUserData user = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getFranchisorId() {
		return franchisorId;
	}
	
	public void setFranchisorId(Long franchisorId) {
		this.franchisorId = franchisorId;
	}

	public PublicUserData getUser() {
		return user;
	}

	public void setUser(PublicUserData user) {
		this.user = user;
	}

}
