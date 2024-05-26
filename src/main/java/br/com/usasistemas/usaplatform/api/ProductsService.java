package br.com.usasistemas.usaplatform.api;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.AvailabilityUpdateDataRequest;
import br.com.usasistemas.usaplatform.api.request.FranchisorUpdateDataRequest;
import br.com.usasistemas.usaplatform.api.request.PriceUpdateDataRequest;
import br.com.usasistemas.usaplatform.api.response.WSEnumValidValuesResponse;
import br.com.usasistemas.usaplatform.api.response.WSProductServiceProductCategoryListResponse;
import br.com.usasistemas.usaplatform.api.response.WSProductServiceProductCategoryResponse;
import br.com.usasistemas.usaplatform.api.response.WSProductServiceProductListResponse;
import br.com.usasistemas.usaplatform.api.response.WSProductServiceProductResponse;
import br.com.usasistemas.usaplatform.api.response.WSProductServiceProductSizeResponse;
import br.com.usasistemas.usaplatform.api.response.WSProductSizePriceHistoryListResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.dao.response.ProductSizePriceHistoryPagedResponse;
import br.com.usasistemas.usaplatform.model.data.ProductCategoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.PRODUCTS_RESOURCE)
public class ProductsService {
	
	private static final Logger log = Logger.getLogger(ProductsService.class.getName());
	private ProductManagementBO productManagement;
	private SupplierManagementBO supplierManagement;
	
	public ProductManagementBO getProductManagement() {
		return productManagement;
	}

	public void setProductManagement(ProductManagementBO productManagement) {
		this.productManagement = productManagement;
	}

	public SupplierManagementBO getSupplierManagement() {
		return supplierManagement;
	}

	public void setSupplierManagement(SupplierManagementBO supplierManagement) {
		this.supplierManagement = supplierManagement;
	}

	/*
	 * Get Product list without any filter
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public List<ProductData> listProducts() {
		return productManagement.getAllProducts();		
	}
	
	/*
	 * Get Product details by Product ID
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ProductData getProduct(@PathVariable Long id) {
		return productManagement.getProduct(id);
	}
	
	/*
	 * Get Franchisor Products
	 */
	@RequestMapping(value = "/franchisor/{franchisorId}", method=RequestMethod.GET)
	@ResponseBody
	public WSProductServiceProductListResponse getFranchisorProductList(@PathVariable Long franchisorId) {
		WSProductServiceProductListResponse response = new WSProductServiceProductListResponse();
		
		try {
			
			List<ProductData> productList = sortProductSizes(productManagement.getFranchisorProducts(franchisorId));
			response.setProductList(productList);
			response.setSupplierList(getProductSuppliers(productList));
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Product List: " + e.toString());
		}
		
		return response;
	}

	/*
	 * Get Franchisor Products
	 */
	@RequestMapping(value = "/franchisor/{franchisorId}/category/{categoryId}", method=RequestMethod.GET)
	@ResponseBody
	public WSProductServiceProductListResponse getFranchisorProductByCategoryList(@PathVariable Long franchisorId, @PathVariable Long categoryId, HttpSession session) {
		WSProductServiceProductListResponse response = new WSProductServiceProductListResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			List<ProductData> productList = sortProductSizes(productManagement.getFranchisorProductsByCategory(franchisorId, categoryId, user.getSelectedRole().equals(UserProfileEnum.ADMINISTRATOR)));
			response.setProductList(productList);
			response.setSupplierList(getProductSuppliers(productList));
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Product List: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Logged user favorite franchisee user products
	 */
	@RequestMapping(value = "/favorites", method=RequestMethod.GET)
	@ResponseBody
	public WSProductServiceProductListResponse getFavorites(HttpSession session) {
		WSProductServiceProductListResponse response = new WSProductServiceProductListResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (user == null || !user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access favorite products");
			
			} else {
				List<ProductData> productList = sortProductSizes(productManagement.getFavoriteFranchiseeUserProducts(user.getSelectedEntityUserId()));
				response.setProductList(productList);
				response.setSupplierList(getProductSuppliers(productList));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Favorite Product List: " + e.toString());
			e.printStackTrace();
		}
		
		return response;
	}
	
	/*
	 * Get Supplier Products
	 */
	@RequestMapping(value = "/supplier/{supplierId}/category/{categoryId}", method=RequestMethod.GET)
	@ResponseBody
	public WSProductServiceProductListResponse getSupplierProductByCategoryList(@PathVariable Long supplierId, @PathVariable Long categoryId) {
		WSProductServiceProductListResponse response = new WSProductServiceProductListResponse();
		
		try {
			response.setProductList(sortProductSizes(productManagement.getSupplierProductsByCategory(supplierId, categoryId)));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Product List: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/supplier/{supplierId}/franchisor/{franchisorId}", method=RequestMethod.GET)
	@ResponseBody
	public WSProductServiceProductListResponse getFranchisorProducts(@PathVariable Long supplierId, @PathVariable Long franchisorId) {
		WSProductServiceProductListResponse response = new WSProductServiceProductListResponse();
		
		try {
			
			List<ProductData> productList = productManagement.getSupplierFranchisorProducts(supplierId, franchisorId);			
			response.setProductList(productList);
			response.setSupplierList(getProductSuppliers(productList));
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Product List: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Franchisor Product Categories
	 */
	@RequestMapping(value = "/categories/franchisor/{franchisorId}", method=RequestMethod.GET)
	@ResponseBody
	public WSProductServiceProductCategoryListResponse getFranchisorProductCategoryList(@PathVariable Long franchisorId, HttpSession session) {
		WSProductServiceProductCategoryListResponse response = new WSProductServiceProductCategoryListResponse();
		
		try {
			List<ProductCategoryData> productCategoryList = productManagement.getFranchisorProductCategories(franchisorId, SessionUtil.getLoggedUser(session));			
			response.setProductCategoryList(productCategoryList);
			response.setStates(productManagement.getProductCategoriesStates(productCategoryList));
			response.setCities(productManagement.getProductCategoriesCities(productCategoryList));			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Product Category List by Franchisor: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Supplier Product Categories
	 */
	@RequestMapping(value = "/categories/supplier/{supplierId}", method=RequestMethod.GET)
	@ResponseBody
	public WSProductServiceProductCategoryListResponse getSupplierProductCategoryList(@PathVariable Long supplierId) {
		WSProductServiceProductCategoryListResponse response = new WSProductServiceProductCategoryListResponse();
		
		try {			
			List<ProductCategoryData> productCategoryList = productManagement.getSupplierProductCategories(supplierId);			
			response.setProductCategoryList(productCategoryList);
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Product Category List by Supplier: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/types", method=RequestMethod.GET)
	@ResponseBody
	public WSEnumValidValuesResponse getTypes() {
		
		WSEnumValidValuesResponse response = new WSEnumValidValuesResponse();
		
		try {
			response.setEnumValues(productManagement.getTypes());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Product Types: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Get Delivery Request list by Product
	 */
	@RequestMapping(value = "/{productId}/priceHistory/paged/{cursorString}", method=RequestMethod.GET)
	@ResponseBody
	public WSProductSizePriceHistoryListResponse geProductSizePriceHistory(@PathVariable Long productId, @PathVariable String cursorString) {
		
		if (cursorString.equals("none")) {
			cursorString = null;
		}
		
		WSProductSizePriceHistoryListResponse response = new WSProductSizePriceHistoryListResponse();
		
		try {
			ProductSizePriceHistoryPagedResponse productSizePriceHistoryPagedResponse = productManagement.getPriceHistoryByProductId(productId, 10L, cursorString);
			response.setProductSizePriceHistories(productSizePriceHistoryPagedResponse.getProductSizePriceHistories());
			response.setCursorString(productSizePriceHistoryPagedResponse.getCursorString());
			response.setProductSizes(productManagement.getProductSizePriceHistoryProductSizes(response.getProductSizePriceHistories()));
			response.setUsers(productManagement.getProductSizePriceHistoryUsers(response.getProductSizePriceHistories()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Product Size Price History by Product: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Create new Product
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public ProductData postProduct(@RequestBody ProductData product) {
		
		if (product.getId() == 0) {
			product.setId(null);
			product.setMinStock(0L);
		}
		
		return productManagement.saveProduct(product);
	}
	
	/*
	 * Create new Product Size
	 */
	@RequestMapping(value = "/size", method=RequestMethod.POST)
	@ResponseBody
	public ProductSizeData postProductSize(@RequestBody ProductSizeData productSize, HttpSession session) {
		
		if (productSize.getId() == 0) {
			productSize.setId(null);
			productSize.setMinStock(0L);
		}
		
		return productManagement.saveProductSize(productSize, SessionUtil.getLoggedUser(session));
	}
	
	/*
	 * Create new Product Category
	 */
	@RequestMapping(value = "/categories", method=RequestMethod.POST)
	@ResponseBody
	public ProductCategoryData postProductCategory(@RequestBody ProductCategoryData productCategory) {
		
		if (productCategory.getId() == 0) {
			productCategory.setId(null);
		}
		
		return productManagement.saveProductCategory(productCategory);
	}
	
	/*
	 * Update existing Product
	 */
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	public ProductData put(@RequestBody ProductData product) {
		return productManagement.saveProduct(product);
	}
	
	/*
	 * Update existing Product Size
	 */
	@RequestMapping(value = "/size", method=RequestMethod.PUT)
	@ResponseBody
	public ProductSizeData putSize(@RequestBody ProductSizeData productSize, HttpSession session) {
		return productManagement.saveProductSize(productSize, SessionUtil.getLoggedUser(session));
	}
	
	/*
	 * Update existing Product Category
	 */
	@RequestMapping(value = "/categories", method=RequestMethod.PUT)
	@ResponseBody
	public ProductCategoryData putProductCategory(@RequestBody ProductCategoryData productCategory) {
		return productManagement.saveProductCategory(productCategory);
	}
	
	/*
	 * Update product information
	 */
	@RequestMapping(value = "/franchisorUpdate", method=RequestMethod.PUT)
	@ResponseBody
	public ProductSizeData putFranchisorUpdate(@RequestBody FranchisorUpdateDataRequest franchisorUpdateRequest) {
		return productManagement.franchisorUpdate(franchisorUpdateRequest.getProductSizeId(), franchisorUpdateRequest.getMinStock());
	}
	
	/*
	 * Update price information
	 */
	@RequestMapping(value = "/priceUpdate", method=RequestMethod.PUT)
	@ResponseBody
	public WSProductServiceProductSizeResponse putPriceUpdate(@RequestBody PriceUpdateDataRequest priceUpdateRequest, HttpSession session) {
		
		WSProductServiceProductSizeResponse response = new WSProductServiceProductSizeResponse();
		
		try {
			response.setProductSize(productManagement.priceUpdate(priceUpdateRequest.getProductSizeId(), priceUpdateRequest.getUnitPrice(), SessionUtil.getLoggedUser(session)));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Product Size Price: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Update availability
	 */
	@RequestMapping(value = "/availabilityUpdate", method=RequestMethod.PUT)
	@ResponseBody
	public WSProductServiceProductSizeResponse availabilityUpdate(@RequestBody AvailabilityUpdateDataRequest priceUpdateRequest, HttpSession session) {
		
		WSProductServiceProductSizeResponse response = new WSProductServiceProductSizeResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (user == null || (
					!user.getSelectedRole().equals(UserProfileEnum.SUPPLIER) &&
					!user.getSelectedRole().equals(UserProfileEnum.ADMINISTRATOR))) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to change product availability");
			
			} else {
				response.setProductSize(productManagement.availabilityUpdate(priceUpdateRequest.getProductSizeId(), priceUpdateRequest.getIsAvailable()));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Product Size availability: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Add favorite franchisee user products
	 */
	@RequestMapping(value = "/favorites/{productId}", method=RequestMethod.PUT)
	@ResponseBody
	public WSProductServiceProductResponse addFavorites(@PathVariable Long productId, HttpSession session) {
		WSProductServiceProductResponse response = new WSProductServiceProductResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (user == null || !user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access add favorite product");
			
			} else {
				response.setProduct(productManagement.addFavoriteFranchiseeProduct(productId, user.getSelectedEntityUserId()));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error adding favorite franchisee product: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Delete product by Product ID
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSProductServiceProductResponse delete(@PathVariable Long id) {
		
		WSProductServiceProductResponse response = new WSProductServiceProductResponse();
		
		try {
			response.setProduct(productManagement.deleteProduct(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Product: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Delete product size by ProductSize ID
	 */
	@RequestMapping(value = "/size/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSProductServiceProductSizeResponse deleteSize(@PathVariable Long id) {
		
		WSProductServiceProductSizeResponse response = new WSProductServiceProductSizeResponse();
		
		try {
			response.setProductSize(productManagement.deleteProductSize(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Product Size: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Delete product by Product ID
	 */
	@RequestMapping(value = "/categories/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSProductServiceProductCategoryResponse deleteProductCategory(@PathVariable Long id) {
		
		WSProductServiceProductCategoryResponse response = new WSProductServiceProductCategoryResponse();
		
		try {
			response.setProductCategor(productManagement.deleteProductCategory(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Product Category: " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * Remove favorite franchisee user products
	 */
	@RequestMapping(value = "/favorites/{productId}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSProductServiceProductResponse deleteFavorites(@PathVariable Long productId, HttpSession session) {
		WSProductServiceProductResponse response = new WSProductServiceProductResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (user == null || !user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access remove favorite product");
			
			} else {
				response.setProduct(productManagement.removeFavoriteFranchiseeProduct(productId, user.getSelectedEntityUserId()));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error removing favorite franchisee product: " + e.toString());
		}
		
		return response;
	}
	
	private List<ProductData> sortProductSizes(List<ProductData> productList) {
		
		if (productList != null && !productList.isEmpty()){
			for (ProductData product: productList) {
				Collections.sort(product.getSizes(), new Comparator<ProductSizeData>() {
			        @Override
			        public int compare(ProductSizeData productSize1, ProductSizeData productSize2)
			        {
			            return  productSize1.getName().compareTo(productSize2.getName());
			        }
			    });
			}
		}		
		
		return productList;
	}
	
	private Map<Long, SupplierData> getProductSuppliers(List<ProductData> productList){
		
		Map<Long, SupplierData> supplierList = new HashMap<Long, SupplierData>();
		
		if (productList != null && !productList.isEmpty()){
			for(ProductData product: productList){
				if(!supplierList.containsKey(product.getSupplierId())){
					supplierList.put(product.getSupplierId(), supplierManagement.getSupplier(product.getSupplierId()));
				}
			}
		}
		
		return supplierList;
	}

}
