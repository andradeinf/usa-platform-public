package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.entity.Franchisor;

public interface FranchisorDAO extends GenericDAO<Franchisor, FranchisorData> {

	FranchisorData findByLoginURL(String loginURL);
	List<FranchisorData> findByPreferedDomainKey(String domainKey);
	
}
