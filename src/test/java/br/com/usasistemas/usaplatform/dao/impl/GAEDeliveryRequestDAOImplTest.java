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

import br.com.usasistemas.usaplatform.dao.DeliveryRequestDAO;
import br.com.usasistemas.usaplatform.dao.repository.ObjectifyRegistry;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;

@RunWith(JUnit4.class)
@Ignore
public class GAEDeliveryRequestDAOImplTest {

    private DeliveryRequestDAO deliveryRequestDAO = new GAEDeliveryRequestDAOImpl();
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
    public void testFindNotCancelledByProductSizeId() {

        List<DeliveryRequestData> list = deliveryRequestDAO.findNotCancelledByProductSizeId(5097210278576128L);
        list.forEach(deliveryRequest -> System.out.println(deliveryRequest.getId() + " - " + deliveryRequest.getStatusDescription()));
        assertEquals(0, list.size());

    }

    @Test
    public void testFindFinishedOlderThanDays() {

        List<DeliveryRequestData> list = deliveryRequestDAO.findFinishedOlderThanDays(0L, "FRANQUIAS");
        list.forEach(deliveryRequest -> System.out.println(deliveryRequest.getId() + " - " + deliveryRequest.getStatusDescription()));
        assertEquals(0, list.size());

    }
    
}