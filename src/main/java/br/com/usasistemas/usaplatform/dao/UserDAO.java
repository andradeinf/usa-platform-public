package br.com.usasistemas.usaplatform.dao;

import br.com.usasistemas.usaplatform.model.data.UserData;
import br.com.usasistemas.usaplatform.model.entity.User;

public interface UserDAO extends GenericDAO<User, UserData> {

	public UserData findByEmail(String email);
	public UserData findByUsername(String username);
	
}
