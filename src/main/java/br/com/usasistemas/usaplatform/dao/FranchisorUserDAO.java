package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.FranchisorUserData;
import br.com.usasistemas.usaplatform.model.entity.FranchisorUser;

public interface FranchisorUserDAO extends GenericDAO<FranchisorUser, FranchisorUserData> {
	
	public List<FranchisorUserData> findByFranchisorId(Long id);
	public List<FranchisorUserData> findByUserId(Long id);
	
}
