package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.SupplierCategoryData;
import br.com.usasistemas.usaplatform.model.entity.SupplierCategory;

public interface SupplierCategoryDAO extends GenericDAO<SupplierCategory, SupplierCategoryData> {

	public List<SupplierCategoryData> findByDomainKey(String domainKey);

	
}
