package br.com.usasistemas.usaplatform.model.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class FranchisorUser {
	
	@Id private Long id;
	private Long userId;
	private Long franchisorId;	

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

}
