package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.entity.ProductSize;

public interface ProductSizeDAO extends GenericDAO<ProductSize, ProductSizeData> {
	
	public List<ProductSizeData> findByProductId(Long productId, Boolean includeInactive);
	
}
