package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.entity.Product;

public interface ProductDAO extends GenericDAO<Product, ProductData> {
	
	public List<ProductData> findByFranchisorId(Long franchisorId);
	public List<ProductData> findBySupplierIdAndFranchisorId(Long supplierId, Long franchisorId);
	public List<ProductData> findByFranchisorIdAndCategoryId(Long franchisorId, Long categoryId);
	public List<ProductData> findByCategoryId(Long categoryId);
	public List<ProductData> findBySupplierId(Long supplierId);
	public List<ProductData> findBySupplierIdAndCategoryId(Long supplierId, Long categoryId);
	public List<ProductData> findByFavoriteFranchiseeUserIds(Long franchiseeUserId);
	
}
