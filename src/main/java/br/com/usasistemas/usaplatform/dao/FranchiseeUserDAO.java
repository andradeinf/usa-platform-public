package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;
import br.com.usasistemas.usaplatform.model.entity.FranchiseeUser;

public interface FranchiseeUserDAO extends GenericDAO<FranchiseeUser, FranchiseeUserData> {
	
	public List<FranchiseeUserData> findByFranchiseeId(Long id);
	public List<FranchiseeUserData> findByUserId(Long id);
	
}
