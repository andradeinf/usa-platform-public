package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.dao.ProductDAO;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.entity.Product;

public class GAEProductDAOImpl extends GAEGenericDAOImpl<Product, ProductData> implements ProductDAO {
	
	private static final Logger log = Logger.getLogger(GAEProductDAOImpl.class.getName());

	@Override
	public List<ProductData> findByFranchisorId(Long franchisorId) {
		List<ProductData> result = new ArrayList<ProductData>();

		try {
			List<Product> products = ofy().load().type(Product.class)
				.filter("franchisorId", franchisorId)
				.list();
			if (products != null && !products.isEmpty())
				result = this.getConverter().convertToDataList(products);
		} catch (Exception e) {
			log.warning("Error when querying for Product by franchisorId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<ProductData> findByFranchisorIdAndCategoryId(Long franchisorId, Long categoryId) {
		List<ProductData> result = new ArrayList<ProductData>();

		try {
			List<Product> products = ofy().load().type(Product.class)
				.filter("franchisorId", franchisorId)
				.filter("productCategoryId", categoryId)
				.list();
			if (products != null && !products.isEmpty())
				result = this.getConverter().convertToDataList(products);
		} catch (Exception e) {
			log.warning("Error when querying for Product by franchisorId and categoryId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<ProductData> findByCategoryId(Long categoryId) {
		List<ProductData> result = new ArrayList<ProductData>();

		try {
			List<Product> products = ofy().load().type(Product.class)
				.filter("productCategoryId", categoryId)
				.list();
			if (products != null && !products.isEmpty())
				result = this.getConverter().convertToDataList(products);
		} catch (Exception e) {
			log.warning("Error when querying for Product by categoryId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<ProductData> findBySupplierIdAndFranchisorId(Long supplierId, Long franchisorId) {
		List<ProductData> result = new ArrayList<ProductData>();

		try {
			List<Product> products = ofy().load().type(Product.class)
				.filter("supplierId", supplierId)
				.filter("franchisorId", franchisorId)
				.list();
			if (products != null && !products.isEmpty())
				result = this.getConverter().convertToDataList(products);
		} catch (Exception e) {
			log.warning("Error when querying for Product by franchisorId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<ProductData> findBySupplierId(Long supplierId) {
		List<ProductData> result = new ArrayList<ProductData>();

		try {
			List<Product> products = ofy().load().type(Product.class)
				.filter("supplierId", supplierId)
				.list();
			if (products != null && !products.isEmpty())
				result = this.getConverter().convertToDataList(products);
		} catch (Exception e) {
			log.warning("Error when querying for Product by supplierId: " + e.toString());
		}			
	
		return result;
	}

	@Override
	public List<ProductData> findBySupplierIdAndCategoryId(Long supplierId, Long categoryId) {
		List<ProductData> result = new ArrayList<ProductData>();

		try {
			List<Product> products = ofy().load().type(Product.class)
				.filter("supplierId", supplierId)
				.filter("productCategoryId", categoryId)
				.list();
			if (products != null && !products.isEmpty())
				result = this.getConverter().convertToDataList(products);
		} catch (Exception e) {
			log.warning("Error when querying for Product by supplierId and categoryId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public List<ProductData> findByFavoriteFranchiseeUserIds(Long franchiseeUserId) {
		List<ProductData> result = new ArrayList<ProductData>();

		try {
			List<Product> products = ofy().load().type(Product.class)
				.filter("favoriteFranchiseeUserIds", franchiseeUserId)
				.list();
			if (products != null && !products.isEmpty())
				result = this.getConverter().convertToDataList(products);
		} catch (Exception e) {
			log.warning("Error when querying for Product by franchiseeUserId: " + e.toString());
		}			
	
		return result;
	}
}
