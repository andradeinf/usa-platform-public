package br.com.usasistemas.usaplatform.dao;

import br.com.usasistemas.usaplatform.model.data.PasswordResetData;
import br.com.usasistemas.usaplatform.model.entity.PasswordReset;

public interface PasswordResetDAO extends GenericDAO<PasswordReset, PasswordResetData> {

	public PasswordResetData findByUsername(String username);
	public PasswordResetData findByUid(String uid);
	
}
