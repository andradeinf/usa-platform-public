package br.com.usasistemas.usaplatform.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.com.usasistemas.usaplatform.dao.ManufactureRequestDAO;
import br.com.usasistemas.usaplatform.dao.repository.ObjectifyRegistry;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;

@RunWith(JUnit4.class)
@Ignore
public class GAEManufactureRequestDAOImplTest {

    private ManufactureRequestDAO manufactureRequestDAO = new GAEManufactureRequestDAOImpl();
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
    public void testFindFinishedOlderThanDays() {

        List<ManufactureRequestData> list = manufactureRequestDAO.findFinishedOlderThanDays(0L, "FRANQUIAS");
        list.forEach(manufactureRequest -> System.out.println(manufactureRequest.getId() + " - " + manufactureRequest.getStatusDescription()));
        assertEquals(0, list.size());

    }
    
}