package br.com.usasistemas.usaplatform.bizo;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;
import br.com.usasistemas.usaplatform.model.data.StateData;
import br.com.usasistemas.usaplatform.model.data.SupplierCategoryData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.SupplierFranchisorData;
import br.com.usasistemas.usaplatform.model.data.SupplierUserData;

public interface SupplierManagementBO {
	
	public List<SupplierData> getAllSuppliers();
	public List<SupplierData> getAllDomainSuppliers(String domainKey);
	public SupplierData getSupplier(Long id);
	public SupplierData saveSupplier(SupplierData supplier);
	public SupplierData deleteSupplier(Long id);
	public List<SupplierUserData> getSupplierUsers(Long supplierId);
	public SupplierUserData saveSupplierUser(SupplierUserData supplierUser);
	public SupplierUserData deleteSupplierUser(Long id);
	public List<EnumValidValueResponseData> getSupplierTypes();
	public List<SupplierFranchisorData> getSupplierFranchisors(Long supplierId);
	public SupplierFranchisorData saveSupplierFranchisor(SupplierFranchisorData supplierFranchisor);
	public SupplierFranchisorData deleteSupplierFranchisor(Long supplierId, Long franchisorId);
	public Map<Long, StateData> getSuppliersStates(List<SupplierData> suppliers);
	public Map<Long, CityData> getSuppliersCities(List<SupplierData> suppliers);
	public List<SupplierCategoryData> getAllCategories();
	public SupplierCategoryData saveSupplierCategory(SupplierCategoryData supplierCategory);
	public SupplierCategoryData deleteSupplierCategory(Long id);
	public List<SupplierData> getSuppliersByCategory(Long id);
	public SupplierData addImage(Long supplierId, String imageKey);
	public List<EnumValidValueResponseData> getVisibilityTypes();
	public List<SupplierData> getFranchisorSuppliersByCategory(Long franchisorId, Long categoryId);
	public List<SupplierData> getFranchisorSuppliersWithStock(Long franchisorId);
	public SupplierCategoryData getCategory(Long id);
	public List<SupplierData> getFranchisorSuppliers(Long franchisorId);
	public List<SupplierCategoryData> getSupplierCategoriesByDomainKey(String domainKey);
	public Map<Long, SupplierData> getReviewRequestsSuppliers(List<ReviewRequestData> reviewRequests);

}
