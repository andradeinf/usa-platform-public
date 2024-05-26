package br.com.usasistemas.usaplatform.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.com.usasistemas.usaplatform.dao.ProductDAO;
import br.com.usasistemas.usaplatform.dao.repository.ObjectifyRegistry;
import br.com.usasistemas.usaplatform.model.data.ProductData;

@RunWith(JUnit4.class)
@Ignore
public class GAEProductDAOImplTest {

    private ProductDAO productDAO = new GAEProductDAOImpl();
    private Closeable session;

    private static final String PRODUCT_NAME = "Test Product";
    private static final String NEW_PRODUCT_NAME = "Test Product Updated";
    
    @BeforeClass
    public static void init() {
        ObjectifyRegistry.initialize();
    }

    @Before
    public void setUp() {
        session = ObjectifyService.begin();
    }

    @After
    public void tearDown() {
        session.close();
    }

    @Test
	public void testSingleProductCrudOperations() {

        ProductData newProduct = new ProductData();
        newProduct.setName(PRODUCT_NAME);

        //test create new product
        ProductData savedProduct = productDAO.save(newProduct);
        assertNotNull(savedProduct.getId());
        assertEquals(savedProduct.getName(), PRODUCT_NAME);

        //update product
        savedProduct.setName(NEW_PRODUCT_NAME);
        ProductData updatedProduct = productDAO.save(savedProduct);
        assertNotNull(updatedProduct.getId());
        assertEquals(updatedProduct.getId(), savedProduct.getId());
        assertEquals(updatedProduct.getName(), NEW_PRODUCT_NAME);

        //test findById
        ProductData searchedProduct = productDAO.findById(updatedProduct.getId());
        assertNotNull(searchedProduct);
        assertEquals(searchedProduct.getId(), updatedProduct.getId());
        assertEquals(searchedProduct.getName(), NEW_PRODUCT_NAME);

        //test delete
        ProductData deletedProduct = productDAO.delete(searchedProduct.getId());
        assertNotNull(deletedProduct);
        assertEquals(deletedProduct.getId(), searchedProduct.getId());
    }

    @Test
    public void testMultipleProductsCrudOperations() {

        ProductData newProduct = null;
        List<ProductData> products = new ArrayList<ProductData>();
        newProduct = new ProductData();
        newProduct.setName(PRODUCT_NAME);
        products.add(newProduct);
        newProduct = new ProductData();
        newProduct.setName(PRODUCT_NAME);
        products.add(newProduct);

        //test create multiple products
        List<ProductData> savedProducts = productDAO.saveAll(products);
        assertNotNull(savedProducts);
        assertEquals(savedProducts.size(), 2);
        assertNotNull(savedProducts.get(0).getId());
        assertNotNull(savedProducts.get(1).getId());

        //update multiple products
        savedProducts.get(0).setName(NEW_PRODUCT_NAME);
        savedProducts.get(1).setName(NEW_PRODUCT_NAME);
        List<ProductData> updatedProducts = productDAO.saveAll(savedProducts);
        assertNotNull(updatedProducts);
        assertEquals(updatedProducts.size(), 2);
        assertEquals(updatedProducts.get(0).getId(), savedProducts.get(0).getId());
        assertEquals(updatedProducts.get(0).getName(), NEW_PRODUCT_NAME);
        assertEquals(updatedProducts.get(1).getId(), savedProducts.get(1).getId());
        assertEquals(updatedProducts.get(1).getName(), NEW_PRODUCT_NAME);

        //test findByIds
        List<Long> savedProductIds = savedProducts.stream().map(product -> product.getId()).collect(Collectors.toList());
        List<ProductData> searchedProducts = productDAO.findByIds(savedProductIds);
        assertNotNull(searchedProducts);
        assertEquals(searchedProducts.size(), 2);
        assertEquals(searchedProducts.get(0).getId(), updatedProducts.get(0).getId());
        assertEquals(searchedProducts.get(0).getName(), NEW_PRODUCT_NAME);
        assertEquals(searchedProducts.get(1).getId(), updatedProducts.get(1).getId());
        assertEquals(searchedProducts.get(1).getName(), NEW_PRODUCT_NAME);

        //test delete multiple products
        List<Long> searchedProductIds = searchedProducts.stream().map(product -> product.getId()).collect(Collectors.toList());
        List<ProductData> deletedProducts = productDAO.deleteAll(searchedProductIds);
        assertNotNull(deletedProducts);
        assertEquals(deletedProducts.size(), 2);
        assertEquals(deletedProducts.get(0).getId(), searchedProducts.get(0).getId());
        assertEquals(deletedProducts.get(1).getId(), searchedProducts.get(1).getId());

    }

    @Test
    public void testFindByFavoriteFranchiseeUserIds() {

        Long franchiseeUserId = Long.valueOf(1234567890);

        ProductData newProduct = null;
        List<ProductData> products = new ArrayList<ProductData>();
        newProduct = new ProductData();
        newProduct.setName(PRODUCT_NAME);
        newProduct.setFavoriteFranchiseeUserIds(Arrays.asList(franchiseeUserId));
        products.add(newProduct);
        newProduct = new ProductData();
        newProduct.setName(NEW_PRODUCT_NAME);
        products.add(newProduct);
        List<ProductData> savedProducts = productDAO.saveAll(products);
        assertNotNull(savedProducts);
        assertEquals(savedProducts.size(), 2);

        List<ProductData> searchedProducts = productDAO.findByFavoriteFranchiseeUserIds(franchiseeUserId);
        assertNotNull(searchedProducts);
        assertEquals(searchedProducts.size(), 1);
        assertEquals(searchedProducts.get(0).getName(), PRODUCT_NAME);

        List<Long> savedProductIds = savedProducts.stream().map(product -> product.getId()).collect(Collectors.toList());
        List<ProductData> deletedProducts = productDAO.deleteAll(savedProductIds);
        assertNotNull(deletedProducts);
        assertEquals(deletedProducts.size(), 2);

    }
    
}