package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.ImageManagementBO;
import br.com.usasistemas.usaplatform.bizo.ManufactureManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.StateAndCityManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.dao.ProductCategoryDAO;
import br.com.usasistemas.usaplatform.dao.ProductDAO;
import br.com.usasistemas.usaplatform.dao.ProductSizeDAO;
import br.com.usasistemas.usaplatform.dao.ProductSizePriceHistoryDAO;
import br.com.usasistemas.usaplatform.dao.response.ProductSizePriceHistoryPagedResponse;
import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.data.ProductCategoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.data.ProductSizePriceHistoryData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.StateData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.ProductTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class ProductManagementBOImpl implements ProductManagementBO {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ProductManagementBOImpl.class.getName());
	private ProductDAO productDAO;
	private ProductSizeDAO productSizeDAO;
	private ProductSizePriceHistoryDAO productSizePriceHistoryDAO;
	private ProductCategoryDAO productCategoryDAO;
	private FranchiseeManagementBO franchiseeManagement;
	private ManufactureManagementBO manufactureManagement;
	private DeliveryManagementBO deliveryManagement;
	private ImageManagementBO imageManagement;
	private UserManagementBO userManagement;
	private FileManagementBO fileManagement;
	private StateAndCityManagementBO stateAndCityManagement;

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public ProductSizeDAO getProductSizeDAO() {
		return productSizeDAO;
	}

	public void setProductSizeDAO(ProductSizeDAO productSizeDAO) {
		this.productSizeDAO = productSizeDAO;
	}

	public ProductSizePriceHistoryDAO getProductSizePriceHistoryDAO() {
		return productSizePriceHistoryDAO;
	}

	public void setProductSizePriceHistoryDAO(ProductSizePriceHistoryDAO productSizePriceHistoryDAO) {
		this.productSizePriceHistoryDAO = productSizePriceHistoryDAO;
	}

	public ProductCategoryDAO getProductCategoryDAO() {
		return productCategoryDAO;
	}

	public void setProductCategoryDAO(ProductCategoryDAO productCategoryDAO) {
		this.productCategoryDAO = productCategoryDAO;
	}

	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public ManufactureManagementBO getManufactureManagement() {
		return manufactureManagement;
	}

	public void setManufactureManagement(
			ManufactureManagementBO manufactureManagement) {
		this.manufactureManagement = manufactureManagement;
	}

	public DeliveryManagementBO getDeliveryManagement() {
		return deliveryManagement;
	}

	public void setDeliveryManagement(DeliveryManagementBO deliveryManagement) {
		this.deliveryManagement = deliveryManagement;
	}

	public ImageManagementBO getImageManagement() {
		return imageManagement;
	}

	public void setImageManagement(ImageManagementBO imageManagement) {
		this.imageManagement = imageManagement;
	}
	
	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	public FileManagementBO getFileManagement() {
		return fileManagement;
	}

	public void setFileManagement(FileManagementBO fileManagement) {
		this.fileManagement = fileManagement;
	}
	
	public StateAndCityManagementBO getStateAndCityManagement() {
		return stateAndCityManagement;
	}

	public void setStateAndCityManagement(
			StateAndCityManagementBO stateAndCityManagement) {
		this.stateAndCityManagement = stateAndCityManagement;
	}

	@Override
	public List<ProductData> getAllProducts() {
		
		List<ProductData> productList = this.productDAO.listAll();
		for(ProductData product: productList) {
			if (product.getImageKey() != null && product.getImageURL() == null) {
				product.setImageURL(imageManagement.getImageServingUrl(product.getImageKey()));
			}
			product.setSizes(this.getProductSizes(product.getId(), true));
		}
		
		return productList;
	}

	@Override
	public ProductData getProduct(Long id) {
		
		ProductData product = this.productDAO.findById(id);
		if (product != null && product.getImageKey() != null && product.getImageURL() == null) {
			product.setImageURL(imageManagement.getImageServingUrl(product.getImageKey()));
		}
		if (product != null) {
			product.setSizes(this.getProductSizes(product.getId(), true));
		}
		
		return product;
	}

	@Override
	public List<ProductData> getFranchisorProductsByCategory(Long franchisorId, Long categoryId, Boolean includeInactive) {
		List<ProductData> products = this.productDAO.findByFranchisorIdAndCategoryId(franchisorId, categoryId);
		initializeProducts(products, true, includeInactive);
		return products;
	}
	
	@Override
	public List<ProductData> getFranchisorProducts(Long franchisorId) {
		List<ProductData> products = this.productDAO.findByFranchisorId(franchisorId);
		initializeProducts(products, true, true);
		return products;
	}
	
	@Override
	public List<ProductData> getFavoriteFranchiseeUserProducts(Long franchiseeUserId) {
		List<ProductData> products = this.productDAO.findByFavoriteFranchiseeUserIds(franchiseeUserId);
		initializeProducts(products, true, false);
		return products;
	}
	
	private void initializeProducts(List<ProductData> products, Boolean includeStockInformation, Boolean includeInactive) {
		
		for (Iterator<ProductData> iter = products.listIterator(); iter.hasNext(); ) {
			
			ProductData product = iter.next();
			
			initializeProduct(product, includeStockInformation, includeInactive);
			
			//remove products if no active product sizes
			//do not consider CATALOG or when inactives need to be added
			if (product.getType() != ProductTypeEnum.CATALOG && !includeInactive && (product.getSizes() == null || product.getSizes().isEmpty())) {
				iter.remove();
			}			
		}
	}
	
	private void initializeProduct(ProductData product, Boolean includeStockInformation, Boolean includeInactive) {
		
		//generate product URL
		if (product.getImageKey() != null && product.getImageURL() == null) {
			product.setImageURL(imageManagement.getImageServingUrl(product.getImageKey()));
		}
		
		//get Product Sizes
		List<ProductSizeData> productSizes = this.getProductSizes(product.getId(), includeInactive);
		product.setSizes(productSizes);
		
		if (product.getType() == ProductTypeEnum.WITH_STOCK_CONTROL && includeStockInformation) {
			//calculate product size stock - Only if has stock control
			for (ProductSizeData productSize: productSizes) {
				productSize.setCurrentStock(calculateProductSizeStock(productSize));
			}
		}	
	}
	
	@Override
	public List<ProductData> getSupplierFranchisorProducts(Long supplierId, Long franchisorId) {
		List<ProductData> products = this.productDAO.findBySupplierIdAndFranchisorId(supplierId, franchisorId);
		initializeProducts(products, true, false);
		return products;
	}

	@Override
	public ProductData saveProduct(ProductData product) {
		
		//TODO: Keep isCatalog updated for now - remove later
		product.setIsCatalog(product.getType() == ProductTypeEnum.CATALOG);
		
		ProductData responseProduct = this.productDAO.save(product);
		
		//duplicate product sizes
		if (product.getDuplicateId() != null) {
			responseProduct.setSizes(this.duplicateProductSizes(product.getDuplicateId(), responseProduct.getId()));
		}
		
		return responseProduct;
	}
	
	@Override
	public List<ProductCategoryData> duplicateFranchisorProductsData(Long sourceFranchisorId, Long targetFranchisorId){
		List<ProductCategoryData> response = new ArrayList<ProductCategoryData>();
		
		//get source franchisor product categories
		List<ProductCategoryData> productCategories = this.getFranchisorProductCategories(sourceFranchisorId, null);
		if (productCategories != null && !productCategories.isEmpty()) {
			for (ProductCategoryData productCategory: productCategories) {
				
				//duplicate product category
				ProductCategoryData newProductCategory = new ProductCategoryData();
				newProductCategory.setFranchisorId(targetFranchisorId);
				newProductCategory.setName(productCategory.getName());
				newProductCategory.setOrder(productCategory.getOrder());
				newProductCategory.setNotes(productCategory.getNotes());
				
				newProductCategory = this.productCategoryDAO.save(newProductCategory);
				response.add(newProductCategory);
				
				//duplicate category products
				this.duplicateFranchisorProducts(sourceFranchisorId, targetFranchisorId, productCategory.getId(), newProductCategory.getId());
			}
		}
		
		return response;
	}
	
	public List<ProductData> duplicateFranchisorProducts(Long sourceFranchisorId, Long targetFranchisorId, Long sourceProductCategoryId, Long targetProductCategoryId){
		List<ProductData> response = new ArrayList<ProductData>();
		
		//get source franchisor products
		List<ProductData> products = this.getFranchisorProductsByCategory(sourceFranchisorId, sourceProductCategoryId, true);
		if (products != null && !products.isEmpty()) {
			for (ProductData product: products) {
				
				ProductData newProduct = new ProductData();
				newProduct.setFranchisorId(targetFranchisorId);
				newProduct.setProductCategoryId(targetProductCategoryId);
				newProduct.setSupplierId(product.getSupplierId());
				newProduct.setName(product.getName());
				newProduct.setDescription(product.getDescription());
				newProduct.setUnitPrice(product.getUnitPrice());
				newProduct.setUnit(product.getUnit());
				newProduct.setHasDeliveryUnit(product.getHasDeliveryUnit());
				newProduct.setDeliveryUnit(product.getDeliveryUnit());
				newProduct.setDeliveryQty(product.getDeliveryQty());
				newProduct.setHasManufactureMinQty(product.getHasManufactureMinQty());
				newProduct.setManufactureMinQty(product.getManufactureMinQty());
				newProduct.setManufactureTime(product.getManufactureTime());
				newProduct.setDeliveryTime(product.getDeliveryTime());
				newProduct.setMinStock(product.getMinStock());
				newProduct.setCurrentStock(0L);
				newProduct.setSizeName(product.getSizeName());
				newProduct.setAllowNegativeStock(product.getAllowNegativeStock());
				newProduct.setAllowChangeUnitPrice(product.getAllowChangeUnitPrice());
				newProduct.setAllowSupplierChangeUnitPrice(product.getAllowSupplierChangeUnitPrice());
				newProduct.setGroupSizes(product.getGroupSizes());
				newProduct.setType(product.getType());
				newProduct.setIsCatalog(product.getIsCatalog());
				
				newProduct = this.productDAO.save(newProduct);
				response.add(newProduct);
				
				//duplicate product sizes
				this.duplicateProductSizes(product.getId(), newProduct.getId());
			}
		}
		
		return response;
	}
	
	public List<ProductSizeData> duplicateProductSizes(Long sourceProductId, Long targetProductId){
		List<ProductSizeData> response = new ArrayList<ProductSizeData>();
		
		//get source product sizes
		List<ProductSizeData> productSizes = this.getProductSizes(sourceProductId, true);
		if (productSizes != null && !productSizes.isEmpty()) {
			for (ProductSizeData productSize: productSizes) {
				
				ProductSizeData newProductSize = new ProductSizeData();
				newProductSize.setProductId(targetProductId);
				newProductSize.setName(productSize.getName());
				newProductSize.setDescription(productSize.getDescription());
				newProductSize.setGroupName(productSize.getGroupName());
				newProductSize.setUnitPrice(productSize.getUnitPrice());
				newProductSize.setDeliveryQty(productSize.getDeliveryQty());
				newProductSize.setManufactureMinQty(productSize.getManufactureMinQty());
				newProductSize.setMinStock(productSize.getMinStock());
				newProductSize.setIsAvailable(productSize.getIsAvailable());
				newProductSize.setIsActive(productSize.getIsActive());
				newProductSize.setConsolidatedStock(0L);					
				
				newProductSize = this.productSizeDAO.save(newProductSize);
				newProductSize.setCurrentStock(0L);
				response.add(newProductSize);
			}
		}
		
		return response;
	}

	@Override
	public ProductData deleteProduct(Long id) {
		
		//delete all product Delivery Requests
		deliveryManagement.deleteDeliveryRequestsByProductId(id);
		
		//delete all product Manufacture Requests
		manufactureManagement.deleteManufactureRequestsByProductId(id);
		
		//delete product sizes
		this.deleteProductSizes(id);
		
		//delete product and its image
		ProductData product = this.productDAO.delete(id);	
		if (product != null && product.getImageKey() != null) {
			imageManagement.deleteImage(product.getImageKey());
		}
		
		return product;
	}

	@Override
	public ProductSizeData franchisorUpdate(Long productSizeId, Long minStock) {
		ProductSizeData productSize = this.productSizeDAO.findById(productSizeId);
		
		productSize.setMinStock(minStock);
		
		productSize = this.productSizeDAO.save(productSize);
		productSize.setCurrentStock(calculateProductSizeStock(productSize));
		
		return productSize;
	}

	public Long calculateProductSizeStock(ProductSizeData productSize) {
		Long currentStock = productSize.getConsolidatedStock();
		for(ManufactureRequestData manufactureRequest: manufactureManagement.getCompletedByProductSizeId(productSize.getId())) {
			currentStock += manufactureRequest.getQuantity();
		}
		for(DeliveryRequestData deliveryRequest: deliveryManagement.getNotCancelledByProductSizeId(productSize.getId())) {
			currentStock -= deliveryRequest.getQuantity();
		}
		return currentStock;
	}

	@Override
	public ProductData addImage(Long productId, String imageKey) {
		ProductData product = productDAO.findById(productId);
		
		//BlobKey oldImage = product.getImageKey();
		
		product.setImageKey(imageKey);
		product.setImageURL(imageManagement.getImageServingUrl(imageKey));
		product = productDAO.save(product);
		
		/*if (oldImage != null) {
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			blobstoreService.delete(oldImage);
		}*/
		
		return product;
	}
	
	@Override
	public ProductData addCatalog(Long productId, UploadedFileData catalogFile) {
		ProductData product = productDAO.findById(productId);
		
	
		String oldCatalogKey = product.getCatalogKey();
		
		product.setCatalogKey(catalogFile.getFileKey());
		product.setCatalogName(catalogFile.getName());
		product.setCatalogStorePath(catalogFile.getStorePath());
		product.setCatalogSize(catalogFile.getSize());
		product.setCatalogContentType(catalogFile.getContentType());

		product = productDAO.save(product);
		
		if (oldCatalogKey != null) {
			fileManagement.deleteFile(oldCatalogKey);
		}
		
		return product;
	}

	@Override
	public List<ProductCategoryData> getFranchisorProductCategories(Long franchisorId, UserProfileData user) {
		
		List<ProductCategoryData> result = this.productCategoryDAO.findByFranchisorId(franchisorId);
		
		// filter Poduct Categories and remove ones with restriction
		// if logued user is a FRANCHISEE
		if (user != null && user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE)) {
			FranchiseeData franchisee = franchiseeManagement.getFranchisee(user.getSelectedEntityId());
			String franchiseeStateRestrictionKey = franchisee.getStateId().toString() + "-0";
			String franchiseeCityRestrictionKey = franchisee.getStateId().toString() + "-" + franchisee.getCityId().toString();
			
			for (Iterator<ProductCategoryData> iter = result.listIterator(); iter.hasNext(); ) {
				ProductCategoryData productCategory = iter.next();
				
				List<String> restrictions = productCategory.getRestrictions();
				if (restrictions != null && !restrictions.isEmpty()){
					if (!restrictions.contains(franchiseeStateRestrictionKey) &&
							!restrictions.contains(franchiseeCityRestrictionKey)){
						iter.remove();
					}
				}
			}
		}
		
		Collections.sort(result, new Comparator<ProductCategoryData>() {
	        @Override
	        public int compare(ProductCategoryData category1, ProductCategoryData category2)
	        {
	        	if (category1.getOrder() == null) return 1;
	        	if (category2.getOrder() == null) return -1;
	            return  category1.getOrder().compareTo(category2.getOrder());
	        }
	    });
		
		return result;
	}
	
	@Override
	public ProductCategoryData saveProductCategory(ProductCategoryData productCategory) {
		return this.productCategoryDAO.save(productCategory);
	}
	
	@Override
	public ProductCategoryData deleteProductCategory(Long id) {
		
		//check products and do not allow delete a category that has at least one product
		List<ProductData> productList = productDAO.findByCategoryId(id);
		if (productList != null && !productList.isEmpty()) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage("Não é possivel excluir a categoria de produto pois ela possui produtos cadastrados. Primeiro remova os produtos para depois excluir a categoria.");
			throw new BusinessException(rm);
		}
		
		return this.productCategoryDAO.delete(id);
	}
	
	private List<ProductSizeData> getProductSizes(Long productId, Boolean includeInactive){
		return productSizeDAO.findByProductId(productId, includeInactive);
	}
	
	private List<ProductSizeData> deleteProductSizes(Long productId){
		List<ProductSizeData> response = this.getProductSizes(productId, true);
		
		for (ProductSizeData productSize: response){
			productSizeDAO.delete(productSize.getId());
		}
		
		return response;
	}

	@Override
	public ProductSizeData saveProductSize(ProductSizeData productSize, UserProfileData user) {
		
		if (user != null) {
			
			//User will be null when it is called from stock consolidation, but in this case there is no
			// price change, so this logic should not be triggered
			
			ProductSizeData oldProductSize = productSizeDAO.findById(productSize.getId());
			if (oldProductSize != null) {
				
				//create Price History record in case of price change
				if (!oldProductSize.getUnitPrice().equals(productSize.getUnitPrice())){
					
					ProductSizePriceHistoryData productSizePriceHistory = new ProductSizePriceHistoryData();
					productSizePriceHistory.setDate(new Date());
					productSizePriceHistory.setNewUnitPrice(productSize.getUnitPrice());
					productSizePriceHistory.setOldUnitPrice(oldProductSize.getUnitPrice());
					productSizePriceHistory.setProductId(oldProductSize.getProductId());
					productSizePriceHistory.setProductSizeId(oldProductSize.getId());
					productSizePriceHistory.setUserId(user.getId());
					productSizePriceHistory.setNotificationStatus(MailNotificationStatusEnum.NOT_APPLICABLE);
					productSizePriceHistoryDAO.save(productSizePriceHistory);
				}
			}
		}
		
		
		
		return this.productSizeDAO.save(productSize);
	}

	@Override
	public ProductSizeData deleteProductSize(Long id) {
		
		//delete all product size price history
		List<Long> productSizePriceHistoryIds = new ArrayList<Long>();
		for (ProductSizePriceHistoryData productSizePriceHistory: productSizePriceHistoryDAO.findByProductSizeId(id)){
			productSizePriceHistoryIds.add(productSizePriceHistory.getId());
		}
		productSizePriceHistoryDAO.deleteAll(productSizePriceHistoryIds);
		
		//delete all product size Delivery Requests
		deliveryManagement.deleteDeliveryRequestsByProductSizeId(id);
		
		//delete all product size Manufacture Requests
		manufactureManagement.deleteManufactureRequestsByProductSizeId(id);
		
		return this.productSizeDAO.delete(id);
	}

	@Override
	public ProductSizeData getProductSize(Long id) {
		return productSizeDAO.findById(id);
	}

	@Override
	public List<ProductData> getSupplierProducts(Long supplierId) {
		return productDAO.findBySupplierId(supplierId);
	}

	@Override
	public List<EnumValidValueResponseData> getTypes() {
		
		List<EnumValidValueResponseData> result = new ArrayList<EnumValidValueResponseData>();
		
		for (ProductTypeEnum value: ProductTypeEnum.values()) {
			EnumValidValueResponseData enumValue = new EnumValidValueResponseData();
			enumValue.setKey(value.name());
			enumValue.setValue(value.getDescription());
			result.add(enumValue);
		}

		return result;
	}

	@Override
	public List<ProductCategoryData> getSupplierProductCategories(Long supplierId) {
		
		Map<Long, ProductCategoryData> productCategories = new HashMap<Long, ProductCategoryData>();
		for (ProductData product : productDAO.findBySupplierId(supplierId)){
			if (!productCategories.containsKey(product.getProductCategoryId())) {
				productCategories.put(product.getProductCategoryId(), productCategoryDAO.findById(product.getProductCategoryId()));
			}
		}
		
		List<ProductCategoryData> result = new ArrayList<ProductCategoryData>(productCategories.values());
		
		Collections.sort(result, new Comparator<ProductCategoryData>() {
	        @Override
	        public int compare(ProductCategoryData category1, ProductCategoryData category2)
	        {
	        	if (category1.getOrder() == null) return 1;
	        	if (category2.getOrder() == null) return -1;
	            return  category1.getOrder().compareTo(category2.getOrder());
	        }
	    });
		
		return result;
	}

	@Override
	public List<ProductData> getSupplierProductsByCategory(Long supplierId, Long categoryId) {
		List<ProductData> products = this.productDAO.findBySupplierIdAndCategoryId(supplierId, categoryId);
		// Do not include stock information neither inactive products
		initializeProducts(products, false, false);
		return products;
	}

	@Override
	public ProductSizeData priceUpdate(Long productSizeId, Double unitPrice, UserProfileData user) {
		ProductSizeData productSize = productSizeDAO.findById(productSizeId);
		ProductData product = productDAO.findById(productSize.getProductId());
		
		if (product.getAllowSupplierChangeUnitPrice() != null && !product.getAllowSupplierChangeUnitPrice()) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage("Este produto não esta autorizado a ter seu preço alterado pelo fornecedor.");
			throw new BusinessException(rm);
		}
		
		ProductSizePriceHistoryData productSizePriceHistory = new ProductSizePriceHistoryData();
		productSizePriceHistory.setDate(new Date());
		productSizePriceHistory.setNewUnitPrice(unitPrice);
		productSizePriceHistory.setOldUnitPrice(productSize.getUnitPrice());
		productSizePriceHistory.setProductId(productSize.getProductId());
		productSizePriceHistory.setProductSizeId(productSize.getId());
		productSizePriceHistory.setUserId(user.getId());
		productSizePriceHistory.setNotificationStatus(MailNotificationStatusEnum.PENDING);
		productSizePriceHistoryDAO.save(productSizePriceHistory);
		
		productSize.setUnitPrice(unitPrice);
		productSize = productSizeDAO.save(productSize);
		
		return productSize;
	}
	
	@Override
	public ProductSizeData availabilityUpdate(Long productSizeId, Boolean isAvailable) {
		ProductSizeData productSize = productSizeDAO.findById(productSizeId);
		
		productSize.setIsAvailable(isAvailable);
		productSize = productSizeDAO.save(productSize);
		
		return productSize;
	}

	@Override
	public ProductSizePriceHistoryPagedResponse getPriceHistoryByProductId(Long productId, long pageSize, String cursorString) {
		ProductSizePriceHistoryPagedResponse result = null;
		Long fechPageSize = pageSize;
		
		result = productSizePriceHistoryDAO.findByProductId(productId, fechPageSize, cursorString);
			
		//check returned records
		if (result.getProductSizePriceHistories() != null) {
			
			//clear cursor if no more records to fetch
			if (result.getProductSizePriceHistories().size() < pageSize) {
				result.setCursorString(null);
			}
			
		} else {
			//no records found. initialize with empty array
			result.setProductSizePriceHistories(new ArrayList<ProductSizePriceHistoryData>());				
		}
		
		return result;
	}

	@Override
	public Map<Long, ProductSizeData> getProductSizePriceHistoryProductSizes(List<ProductSizePriceHistoryData> productSizePriceHistories) {
		Map<Long, ProductSizeData> response = new HashMap<Long, ProductSizeData>();
		
		if(productSizePriceHistories != null && !productSizePriceHistories.isEmpty()) {
			for(ProductSizePriceHistoryData productSizePriceHistory: productSizePriceHistories) {
				if (!response.containsKey(productSizePriceHistory.getProductSizeId())){
					response.put(productSizePriceHistory.getProductSizeId(), productSizeDAO.findById(productSizePriceHistory.getProductSizeId()));					
				}
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, PublicUserData> getProductSizePriceHistoryUsers(List<ProductSizePriceHistoryData> productSizePriceHistories) {
		Map<Long, PublicUserData> response = new HashMap<Long, PublicUserData>();
		
		if(productSizePriceHistories != null && !productSizePriceHistories.isEmpty()) {
			for(ProductSizePriceHistoryData productSizePriceHistory: productSizePriceHistories) {
				if (!response.containsKey(productSizePriceHistory.getUserId())){
					response.put(productSizePriceHistory.getUserId(), userManagement.getUser(productSizePriceHistory.getUserId()));					
				}
			}
		}
		
		return response;
	}

	@Override
	public List<ProductSizePriceHistoryData> getProductSizePriceHistoryPendingNotifications() {
		return productSizePriceHistoryDAO.findByNotificationStatus(MailNotificationStatusEnum.PENDING);
	}

	@Override
	public List<ProductSizePriceHistoryData> updateProductSizePriceHistoryNotificationStatus(List<ProductSizePriceHistoryData> productSizePriceHistories, MailNotificationStatusEnum notificationStatus) {
		
		for (ProductSizePriceHistoryData productSizePriceHistory: productSizePriceHistories){
			productSizePriceHistory.setNotificationStatus(notificationStatus);
		}

		return  productSizePriceHistoryDAO.saveAll(productSizePriceHistories);
	}

	@Override
	public Map<Long, StateData> getProductCategoriesStates(List<ProductCategoryData> productCategoryList) {
		Map<Long, StateData> response = new HashMap<Long, StateData>();

		if(productCategoryList != null && !productCategoryList.isEmpty()) {
			for(ProductCategoryData productCategory: productCategoryList) {
				if (productCategory.getRestrictions() != null) {
					for(String restriction: productCategory.getRestrictions()){
						if (restriction != null) {
							String[] stateAndCity = restriction.split("-");
							Long stateId = Long.parseLong(stateAndCity[0]);
							if (!response.containsKey(stateId)){
								response.put(stateId, stateAndCityManagement.getState(stateId));					
							}
						}					
					}
				}								
			}
		}
		
		return response;
	}

	@Override
	public Map<Long, CityData> getProductCategoriesCities(List<ProductCategoryData> productCategoryList) {
		Map<Long, CityData> response = new HashMap<Long, CityData>();
		
		if(productCategoryList != null && !productCategoryList.isEmpty()) {
			for(ProductCategoryData productCategory: productCategoryList) {
				if (productCategory.getRestrictions() != null) {
					for(String restriction: productCategory.getRestrictions()){
						if (restriction != null) {
							String[] stateAndCity = restriction.split("-");
							Long cityId = Long.parseLong(stateAndCity[1]);
							if (cityId != 0 && !response.containsKey(cityId)){
								response.put(cityId, stateAndCityManagement.getCity(cityId));					
							}
						}					
					}
				}			
			}
		}
		
		return response;
	}

	@Override
	public ProductData addFavoriteFranchiseeProduct(Long productId, Long selectedEntityUserId) {
		ProductData product = productDAO.findById(productId);
		
		List<Long> favoriteFranchiseeUserIds = product.getFavoriteFranchiseeUserIds();
		if (favoriteFranchiseeUserIds == null) {
			favoriteFranchiseeUserIds = new ArrayList<Long>();
			product.setFavoriteFranchiseeUserIds(favoriteFranchiseeUserIds);
		}

		if (!favoriteFranchiseeUserIds.contains(selectedEntityUserId)){
			favoriteFranchiseeUserIds.add(selectedEntityUserId);
		}
		
		product = productDAO.save(product);				
		initializeProduct(product, true, false);
		
		return product;
	}

	@Override
	public ProductData removeFavoriteFranchiseeProduct(Long productId, Long selectedEntityUserId) {
		ProductData product = productDAO.findById(productId);
		
		List<Long> favoriteFranchiseeIds = product.getFavoriteFranchiseeUserIds();
		favoriteFranchiseeIds.remove(selectedEntityUserId);		
		
		product = productDAO.save(product);				
		initializeProduct(product, true, false);
		
		return product;
	}
	
}
