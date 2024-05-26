package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.entity.Franchisee;

public interface FranchiseeDAO extends GenericDAO<Franchisee, FranchiseeData> {

	public List<FranchiseeData> findByFranchisorId(Long franchisorId);
	
}
