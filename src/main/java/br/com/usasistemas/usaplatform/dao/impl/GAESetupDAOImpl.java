package br.com.usasistemas.usaplatform.dao.impl;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

import br.com.usasistemas.usaplatform.dao.SetupDAO;

public class GAESetupDAOImpl implements SetupDAO{
	
	private static final Logger log = Logger.getLogger(GAESetupDAOImpl.class.getName());

	@Override
	public void fixSupplierManufactureType() {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Supplier");
		PreparedQuery pq = datastore.prepare(q);
		
		for (Entity supplier : pq.asIterable()) {
			  String id = (String) supplier.getProperty("id");
			  String name = (String) supplier.getProperty("name");
			  String type = (String) supplier.getProperty("type");

			  System.out.println(id + " " + name + ", " + type);
			  
			  supplier.setProperty("manufactureType", type);
			  supplier.setProperty("type", "OPERATIONAL");
			  datastore.put(supplier);
			}
	}
	
	@Override
	public void removeNotUsedColumns() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = null;
		
		Query qDeliveryRequest = new Query("DeliveryRequest");
		pq = datastore.prepare(qDeliveryRequest);
		for (Entity deliveryRequest : pq.asIterable()) {
			deliveryRequest.removeProperty("domainKey");
			datastore.put(deliveryRequest);
		}
		
		Query qManufactureRequest = new Query("ManufactureRequest");
		pq = datastore.prepare(qManufactureRequest);
		for (Entity manufactureRequest : pq.asIterable()) {
			manufactureRequest.removeProperty("domainKey");
			datastore.put(manufactureRequest);
		}
	}
	
	@Override
	public void cleanUpExpiredSessions() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query query = new Query("_ah_SESSION");
		Filter filter = new FilterPredicate("_expires", Query.FilterOperator.LESS_THAN, System.currentTimeMillis() - 86400000);
		query.setFilter(filter);
		query.addSort("_expires", SortDirection.ASCENDING);
		
		PreparedQuery results = datastore.prepare(query);
		
		for (Entity session : results.asIterable(FetchOptions.Builder.withLimit(100))) { 
			log.info("Session: " + session.getKey() + " - " + session.getProperty("_expires"));
            datastore.delete(session.getKey()); 
		} 
	}

}
