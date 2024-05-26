package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.entity.Supplier;
import br.com.usasistemas.usaplatform.model.enums.SupplierVisibilityTypeEnum;

public interface SupplierDAO extends GenericDAO<Supplier, SupplierData> {

	public List<SupplierData> findByCategoryId(Long categoryId);
	public List<SupplierData> findByCategoryIdAndVisibilityType(Long categoryId, SupplierVisibilityTypeEnum visibility);
	public List<SupplierData> findByPreferedDomainKey(String domainKey);
	
}
