package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.SupplierDAO;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.entity.Supplier;
import br.com.usasistemas.usaplatform.model.enums.SupplierVisibilityTypeEnum;

public class GAESupplierDAOImpl extends GAEGenericDAOImpl<Supplier, SupplierData> implements SupplierDAO {
	
	private static final Logger log = Logger.getLogger(GAESupplierDAOImpl.class.getName());

	@Override
	public List<SupplierData> findByCategoryId(Long categoryId) {
		List<SupplierData> result = new ArrayList<SupplierData>();

		try {
			List<Supplier> suppliers = ofy().load().type(Supplier.class)
				.filter("categoryId", categoryId)
				.list();
			if (suppliers != null && !suppliers.isEmpty())
				result = this.getConverter().convertToDataList(suppliers);
		} catch (Exception e) {
			log.warning("Error when querying for Suppliers by categoryId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<SupplierData> findByCategoryIdAndVisibilityType(Long categoryId, SupplierVisibilityTypeEnum visibility) {
		List<SupplierData> result = new ArrayList<SupplierData>();

		try {
			List<Supplier> suppliers = ofy().load().type(Supplier.class)
				.filter("categoryId", categoryId)
				.filter("visibility", visibility)
				.list();
			if (suppliers != null && !suppliers.isEmpty())
				result = this.getConverter().convertToDataList(suppliers);
		} catch (Exception e) {
			log.warning("Error when querying for Suppliers by visibility type: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<SupplierData> findByPreferedDomainKey(String domainKey) {
		List<SupplierData> result = new ArrayList<SupplierData>();

		try {
			List<Supplier> suppliers = ofy().load().type(Supplier.class)
				.filter("preferedDomainKey", domainKey)
				.list();
			if (suppliers != null && !suppliers.isEmpty())
				result = this.getConverter().convertToDataList(suppliers);
		} catch (Exception e) {
			log.warning("Error when querying for Suppliers by preferedDomainKey: " + e.toString());
		}			
	
		return result;
	}
	
}
