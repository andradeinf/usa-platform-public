package br.com.usasistemas.usaplatform.bizo;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.dao.response.ProductSizePriceHistoryPagedResponse;
import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.ProductCategoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.data.ProductSizePriceHistoryData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.StateData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;


public interface ProductManagementBO {
	
	public List<ProductData> getAllProducts();
	public ProductData getProduct(Long id);
	public List<ProductData> getFranchisorProducts(Long franchisorId);
	public List<ProductCategoryData> getFranchisorProductCategories(Long franchisorId, UserProfileData user);
	public List<ProductData> getSupplierFranchisorProducts(Long supplierId, Long franchisorId);
	public ProductData saveProduct(ProductData product);
	public ProductData deleteProduct(Long id);
	public ProductData addImage(Long productId, String imageKey);
	public ProductSizeData franchisorUpdate(Long productSizeId, Long minStock);
	public Long calculateProductSizeStock(ProductSizeData productSize);
	public ProductCategoryData saveProductCategory(ProductCategoryData productCategory);
	public ProductCategoryData deleteProductCategory(Long id);
	public ProductSizeData getProductSize(Long id);
	public ProductSizeData saveProductSize(ProductSizeData productSize, UserProfileData user);
	public ProductSizeData deleteProductSize(Long id);
	public List<ProductData> getFranchisorProductsByCategory(Long franchisorId, Long categoryId, Boolean includeInactive);
	public List<ProductCategoryData> duplicateFranchisorProductsData(Long sourceFranchisorId, Long targetFranchisorId);
	public List<ProductData> getSupplierProducts(Long supplierId);
	public ProductData addCatalog(Long productId, UploadedFileData catalogFile);
	public List<EnumValidValueResponseData> getTypes();
	public List<ProductCategoryData> getSupplierProductCategories(Long supplierId);
	public List<ProductData> getSupplierProductsByCategory(Long supplierId, Long categoryId);
	public ProductSizeData priceUpdate(Long productSizeId, Double unitPrice, UserProfileData user);
	public ProductSizeData availabilityUpdate(Long productSizeId, Boolean isAvailable);
	public ProductSizePriceHistoryPagedResponse getPriceHistoryByProductId(Long productId, long pageSize, String cursorString);
	public Map<Long, ProductSizeData> getProductSizePriceHistoryProductSizes(List<ProductSizePriceHistoryData> productSizePriceHistories);
	public Map<Long, PublicUserData> getProductSizePriceHistoryUsers(List<ProductSizePriceHistoryData> productSizePriceHistories);
	public List<ProductSizePriceHistoryData> getProductSizePriceHistoryPendingNotifications();
	public List<ProductSizePriceHistoryData> updateProductSizePriceHistoryNotificationStatus(List<ProductSizePriceHistoryData> productSizePriceHistories, MailNotificationStatusEnum notificationStatus);
	public Map<Long, StateData> getProductCategoriesStates(List<ProductCategoryData> productCategoryList);
	public Map<Long, CityData> getProductCategoriesCities(List<ProductCategoryData> productCategoryList);
	public List<ProductData> getFavoriteFranchiseeUserProducts(Long franchiseeUserId);
	public ProductData addFavoriteFranchiseeProduct(Long productId, Long selectedEntityUserId);
	public ProductData removeFavoriteFranchiseeProduct(Long productId, Long selectedEntityUserId);
	
}
