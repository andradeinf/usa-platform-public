package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.SupplierUserData;
import br.com.usasistemas.usaplatform.model.entity.SupplierUser;

public interface SupplierUserDAO extends GenericDAO<SupplierUser, SupplierUserData> {
	
	public List<SupplierUserData> findBySupplierId(Long id);
	public List<SupplierUserData> findByUserId(Long id);
	
}
