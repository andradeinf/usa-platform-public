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

import br.com.usasistemas.usaplatform.dao.SupplierCategoryDAO;
import br.com.usasistemas.usaplatform.dao.repository.ObjectifyRegistry;
import br.com.usasistemas.usaplatform.model.data.SupplierCategoryData;

@RunWith(JUnit4.class)
@Ignore
public class GAESupplierCategoryDAOImplTest {

    private SupplierCategoryDAO supplierCategoryDAO = new GAESupplierCategoryDAOImpl();
    private Closeable session;
    
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
    public void testFindByDomainKey() {

        String DOMAIN_KEY = "DOMAIN_1";
        String SUPPLIER_CATEGORY_NAME = "SUPPLIER_CATEGORY_1";

        SupplierCategoryData newSupplierCategory = null;
        List<SupplierCategoryData> supplierCategories = new ArrayList<SupplierCategoryData>();
        newSupplierCategory = new SupplierCategoryData();
        newSupplierCategory.setName(SUPPLIER_CATEGORY_NAME);
        newSupplierCategory.setDomainKeys(Arrays.asList(DOMAIN_KEY));
        supplierCategories.add(newSupplierCategory);
        newSupplierCategory = new SupplierCategoryData();
        newSupplierCategory.setName("AnySupplierCategoryName");
        newSupplierCategory.setDomainKeys(Arrays.asList("AnyDomainKey"));
        supplierCategories.add(newSupplierCategory);
        List<SupplierCategoryData> savedSupplierCategories = supplierCategoryDAO.saveAll(supplierCategories);
        assertNotNull(savedSupplierCategories);
        assertEquals(savedSupplierCategories.size(), 2);

        List<SupplierCategoryData> searchedSupplierCategories = supplierCategoryDAO.findByDomainKey(DOMAIN_KEY);
        assertNotNull(searchedSupplierCategories);
        assertEquals(searchedSupplierCategories.size(), 1);
        assertEquals(searchedSupplierCategories.get(0).getName(), SUPPLIER_CATEGORY_NAME);

        List<Long> savedSupplierCategoryIds = savedSupplierCategories.stream().map(supplierCategory -> supplierCategory.getId()).collect(Collectors.toList());
        List<SupplierCategoryData> deletedSupplierCategories = supplierCategoryDAO.deleteAll(savedSupplierCategoryIds);
        assertNotNull(deletedSupplierCategories);
        assertEquals(deletedSupplierCategories.size(), 2);

    }
    
}