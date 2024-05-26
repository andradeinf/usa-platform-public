package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.ProductCategoryDAO;
import br.com.usasistemas.usaplatform.model.data.ProductCategoryData;
import br.com.usasistemas.usaplatform.model.entity.ProductCategory;

public class GAEProductCategoryDAOImpl extends GAEGenericDAOImpl<ProductCategory, ProductCategoryData> implements ProductCategoryDAO {
	
	private static final Logger log = Logger.getLogger(GAEProductCategoryDAOImpl.class.getName());

	@Override
	public List<ProductCategoryData> findByFranchisorId(Long franchisorId) {
		List<ProductCategoryData> result = new ArrayList<ProductCategoryData>();

		try {
			List<ProductCategory> productCategories = ofy().load().type(ProductCategory.class)
				.filter("franchisorId", franchisorId)
				.list();
			if (productCategories != null && !productCategories.isEmpty())
				result = this.getConverter().convertToDataList(productCategories);
		} catch (Exception e) {
			log.warning("Error when querying for Product Categories by franchisorId: " + e.toString());
		}			
	
		return result;
	}

}
