package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.SupplierCategoryDAO;
import br.com.usasistemas.usaplatform.model.data.SupplierCategoryData;
import br.com.usasistemas.usaplatform.model.entity.SupplierCategory;

public class GAESupplierCategoryDAOImpl extends GAEGenericDAOImpl<SupplierCategory, SupplierCategoryData> implements SupplierCategoryDAO {
	
	private static final Logger log = Logger.getLogger(GAESupplierCategoryDAOImpl.class.getName());

	@Override
	public List<SupplierCategoryData> findByDomainKey(String domainKey) {
		List<SupplierCategoryData> result = new ArrayList<SupplierCategoryData>();
		
		try {
			List<SupplierCategory> supplierCategories = ofy().load().type(SupplierCategory.class)
				.filter("domainKeys", domainKey)
				.list();
			if (supplierCategories != null && !supplierCategories.isEmpty())
				result = this.getConverter().convertToDataList(supplierCategories);
		} catch (Exception e) {
			log.warning("Error when querying for SupplierCategories by domainKeys: " + e.toString());
		}			
	
		return result;
	}
}
