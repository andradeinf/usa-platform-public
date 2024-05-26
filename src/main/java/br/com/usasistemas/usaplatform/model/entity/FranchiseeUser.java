package br.com.usasistemas.usaplatform.model.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class FranchiseeUser {
	
	@Id private Long id;
	private Long userId;
	private Long franchiseeId;
	
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

	public Long getFranchiseeId() {
		return franchiseeId;
	}
	
	public void setFranchiseeId(Long franchiseeId) {
		this.franchiseeId = franchiseeId;
	}

}
