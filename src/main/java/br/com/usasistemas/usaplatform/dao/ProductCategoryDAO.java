package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.ProductCategoryData;
import br.com.usasistemas.usaplatform.model.entity.ProductCategory;

public interface ProductCategoryDAO extends GenericDAO<ProductCategory, ProductCategoryData> {
	
	public List<ProductCategoryData> findByFranchisorId(Long franchisorId);
	
}
