package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.cmd.Query;

import br.com.usasistemas.usaplatform.dao.ProductSizeDAO;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.entity.ProductSize;

public class GAEProductSizeDAOImpl extends GAEGenericDAOImpl<ProductSize, ProductSizeData> implements ProductSizeDAO {
	
	private static final Logger log = Logger.getLogger(GAEProductSizeDAOImpl.class.getName());

	@Override
	public List<ProductSizeData> findByProductId(Long productId, Boolean includeInactive) {
		List<ProductSizeData> result = new ArrayList<ProductSizeData>();
		
		try {
			Query<ProductSize> q = ofy().load().type(ProductSize.class)
				.filter("productId", productId);	

			if (!includeInactive) {
				q = q.filter("isActive", true);
			}

			List<ProductSize> productSizes = q.list();
			if (productSizes != null && !productSizes.isEmpty())
				result = this.getConverter().convertToDataList(productSizes);
		} catch (Exception e) {
			log.warning("Error when querying for ProductSize by productId: " + e.toString());
		}			
	
		return result;
	}

}
