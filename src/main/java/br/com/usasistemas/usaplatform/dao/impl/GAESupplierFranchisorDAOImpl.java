package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.SupplierFranchisorDAO;
import br.com.usasistemas.usaplatform.model.data.SupplierFranchisorData;
import br.com.usasistemas.usaplatform.model.entity.SupplierFranchisor;

public class GAESupplierFranchisorDAOImpl extends GAEGenericDAOImpl<SupplierFranchisor, SupplierFranchisorData> implements SupplierFranchisorDAO {
	
	private static final Logger log = Logger.getLogger(GAESupplierFranchisorDAOImpl.class.getName());

	@Override
	public List<SupplierFranchisorData> findBySupplierId(Long id) {
		List<SupplierFranchisorData> result = new ArrayList<SupplierFranchisorData>();

		try {
			List<SupplierFranchisor> franchisors = ofy().load().type(SupplierFranchisor.class)
				.filter("supplierId", id)
				.list();
			if (franchisors != null && !franchisors.isEmpty())
				result = this.getConverter().convertToDataList(franchisors);
		} catch (Exception e) {
			log.warning("Error when querying for SupplierFranchisors by SupplierId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<SupplierFranchisorData> findByFranchisorId(Long id) {
		List<SupplierFranchisorData> result = new ArrayList<SupplierFranchisorData>();

		try {
			List<SupplierFranchisor> franchisors = ofy().load().type(SupplierFranchisor.class)
				.filter("franchisorId", id)
				.list();
			if (franchisors != null && !franchisors.isEmpty())
				result = this.getConverter().convertToDataList(franchisors);
		} catch (Exception e) {
			log.warning("Error when querying for SupplierFranchisors by FranchisorID: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public SupplierFranchisorData findBySupplierIdAndFranchisorId(Long supplierId, Long franchisorId) {
		SupplierFranchisorData result = new SupplierFranchisorData();

		try {
			List<SupplierFranchisor> franchisors = ofy().load().type(SupplierFranchisor.class)
				.filter("supplierId", supplierId)
				.filter("franchisorId", franchisorId)
				.list();
			if (franchisors != null && !franchisors.isEmpty())
				result = this.getConverter().convertToData(franchisors.get(0));
		} catch (Exception e) {
			log.warning("Error when querying for SupplierFranchisors by SupplierID and FranchisorID: " + e.toString());
		}			
	
		return result;
	}

}
