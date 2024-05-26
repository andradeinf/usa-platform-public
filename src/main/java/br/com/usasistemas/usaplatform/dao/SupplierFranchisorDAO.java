package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.SupplierFranchisorData;
import br.com.usasistemas.usaplatform.model.entity.SupplierFranchisor;

public interface SupplierFranchisorDAO extends GenericDAO<SupplierFranchisor, SupplierFranchisorData> {
	
	public List<SupplierFranchisorData> findBySupplierId(Long id);
	public List<SupplierFranchisorData> findByFranchisorId(Long id);
	public SupplierFranchisorData findBySupplierIdAndFranchisorId(Long supplierId, Long franchisorId);
	
}
