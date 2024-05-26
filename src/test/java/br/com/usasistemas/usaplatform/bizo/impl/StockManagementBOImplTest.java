package br.com.usasistemas.usaplatform.bizo.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.bizo.ManufactureManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.ReviewManagementBO;
import br.com.usasistemas.usaplatform.dao.StockConsolidationDAO;
import br.com.usasistemas.usaplatform.dao.impl.GAEStockConsolidationDAOImpl;
import br.com.usasistemas.usaplatform.model.converter.ObjectConverter;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.data.ReviewRequestData;
import br.com.usasistemas.usaplatform.model.data.StockConsolidationData;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@RunWith(JUnit4.class)
public class StockManagementBOImplTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testConsolidateStock() {
		
		StockManagementBOImpl stockManagement = new StockManagementBOImpl();
		StockConsolidationDAO stockConsolidationDAOMock = mock(GAEStockConsolidationDAOImpl.class);
		stockManagement.setStockConsolidationDAO(stockConsolidationDAOMock);
		ManufactureManagementBO manufactureManagementMock = mock(ManufactureManagementBOImpl.class);
		stockManagement.setManufactureManagement(manufactureManagementMock);
		DeliveryManagementBO deliveryManagementMock = mock(DeliveryManagementBOImpl.class);
		stockManagement.setDeliveryManagement(deliveryManagementMock);
		ProductManagementBO productManagementMock = mock(ProductManagementBOImpl.class);
		stockManagement.setProductManagement(productManagementMock);
		ReviewManagementBO reviewManagementMock = mock(ReviewManagementBOImpl.class);
		stockManagement.setReviewManagement(reviewManagementMock);
		ConfigurationManagementBO configurationManagementMock = new ConfigurationManagementBOImpl();
		stockManagement.setConfigurationManagement(configurationManagementMock);
		
		when(stockConsolidationDAOMock.save(any(StockConsolidationData.class)))
		.thenAnswer(request -> {
			StockConsolidationData response = request.getArgumentAt(0, StockConsolidationData.class);
			response.setId(1L);
			return response;
		});
		
		when(deliveryManagementMock.getPendingConsolidation("DEVELOPMENT", 0L))
		.thenAnswer(request -> {
			List<DeliveryRequestData> response = new ArrayList<DeliveryRequestData>();
			
			DeliveryRequestData deliveryRequest1 = new DeliveryRequestData();
			deliveryRequest1.setId(1L);
			deliveryRequest1.setFranchiseeId(1L);
			deliveryRequest1.setSupplierId(1L);
			deliveryRequest1.setProductId(1L);
			deliveryRequest1.setProductSizeId(1L);
			deliveryRequest1.setQuantity(1L);
			deliveryRequest1.setStatus(DeliveryRequestStatusEnum.COMPLETED);
			response.add(deliveryRequest1);
			
			DeliveryRequestData deliveryRequest2 = new DeliveryRequestData();
			deliveryRequest2.setId(2L);
			deliveryRequest2.setFranchiseeId(1L);
			deliveryRequest2.setSupplierId(1L);
			deliveryRequest2.setProductId(2L);
			deliveryRequest2.setProductSizeId(2L);
			deliveryRequest2.setQuantity(1L);
			deliveryRequest2.setStatus(DeliveryRequestStatusEnum.COMPLETED);
			response.add(deliveryRequest2);
			
			DeliveryRequestData deliveryRequest3 = new DeliveryRequestData();
			deliveryRequest3.setId(3L);
			deliveryRequest3.setFranchiseeId(1L);
			deliveryRequest3.setSupplierId(2L);
			deliveryRequest3.setProductId(3L);
			deliveryRequest3.setProductSizeId(3L);
			deliveryRequest3.setQuantity(1L);
			deliveryRequest3.setStatus(DeliveryRequestStatusEnum.COMPLETED);
			response.add(deliveryRequest3);
			
			return response;
		});
		
		when(deliveryManagementMock.convertToDeliveryRequestHistory(any(DeliveryRequestData.class)))
		.thenAnswer(request -> ObjectConverter.convert(request.getArgumentAt(0, DeliveryRequestData.class), DeliveryRequestHistoryData.class));
			
		when(deliveryManagementMock.saveAllDeliveryRequestsToHistory(any(List.class)))
		.thenAnswer(request -> request.getArguments()[0]);
		
		when(productManagementMock.getProductSize(any(Long.class)))
		.thenAnswer(request -> {
			ProductSizeData response = new ProductSizeData();
			response.setId(request.getArgumentAt(0, Long.class));
			response.setConsolidatedStock(0L);
			return response;
		});
		
		when(reviewManagementMock.createSupplierReviewRequest(any(ReviewRequestData.class)))
		.thenAnswer(request -> {
			ReviewRequestData response = request.getArgumentAt(0, ReviewRequestData.class);
			response.setId(1L);
			return response;
		});
		
		stockManagement.consolidateStock();
		
		ArgumentCaptor<ReviewRequestData> argument = ArgumentCaptor.forClass(ReviewRequestData.class);
		verify(reviewManagementMock, times(2)).createSupplierReviewRequest(argument.capture());
		
		ReviewRequestData request1 = argument.getAllValues().get(0);		
		assertEquals(UserProfileEnum.FRANCHISEE, request1.getFromEntityProfile());
		assertEquals(Long.valueOf(1), request1.getFromEntityId());
		assertEquals(UserProfileEnum.SUPPLIER, request1.getToEntityProfile());
		assertEquals(Long.valueOf(1), request1.getToEntityId());
		assertEquals(2, request1.getDeliveryRequestHistoryIds().size());
		assertTrue(request1.getDeliveryRequestHistoryIds().contains(1L));
		assertTrue(request1.getDeliveryRequestHistoryIds().contains(2L));
		
		ReviewRequestData request2 = argument.getAllValues().get(1);		
		assertEquals(UserProfileEnum.FRANCHISEE, request2.getFromEntityProfile());
		assertEquals(Long.valueOf(1), request2.getFromEntityId());
		assertEquals(UserProfileEnum.SUPPLIER, request2.getToEntityProfile());
		assertEquals(Long.valueOf(2), request2.getToEntityId());
		assertEquals(1, request2.getDeliveryRequestHistoryIds().size());
		assertTrue(request2.getDeliveryRequestHistoryIds().contains(3L));
	}

}
