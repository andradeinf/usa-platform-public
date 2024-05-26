package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.QueryResults;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import br.com.usasistemas.usaplatform.dao.GenericDAO;
import br.com.usasistemas.usaplatform.dao.converter.GenericDataConverter;
import br.com.usasistemas.usaplatform.dao.response.ListBasePagedResponse;

public class GAEGenericDAOImpl<E, D> implements GenericDAO<E, D> {

	private static final Logger log = Logger.getLogger(GAEGenericDAOImpl.class.getName());

	private Class<E> persistentClass;
	private GenericDataConverter<E, D> converter;

	@SuppressWarnings("unchecked")
	public GAEGenericDAOImpl() {
		this.persistentClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		this.converter = new GenericDataConverter<E, D>();
	}

	public GenericDataConverter<E, D> getConverter() {
		return this.converter;
	}

	protected Class<E> getPersistentClass() {
		return this.persistentClass;
	}

	@Override
	public D findById(Long id) {
		D result = null;

		try {
			if (id != null && !id.equals(0L)) {
				E entity = ofy().load().key(Key.create(persistentClass, id)).now();
				if (entity != null) {
					result = converter.convertToData(entity);
				}
			}						
		} catch (Exception e) {
			log.warning("Error when querying for " + persistentClass.getSimpleName() + " by Id: " + e.toString());
		}

		return result;
	}

	@Override
	public List<D> findByIds(List<Long> ids) {
		List<D> result = null;

		try {
			if (ids != null && !ids.isEmpty()) {
				List<E> entityList = ofy().load()
						.keys(ids.stream().map(id -> Key.create(persistentClass, id)).collect(Collectors.toList()))
						.values().stream().collect(Collectors.toList());
				if (entityList != null && !entityList.isEmpty())
					result = this.getConverter().convertToDataList(entityList);
			}
		} catch (Exception e) {
			log.warning("Error when querying for " + persistentClass.getSimpleName() + " by Ids: " + e.toString());
		}

		return result;
	}

	@Override
	public List<D> findByFilter(Map<String, Object> parameters) {
		return this.findByFilter(parameters, null, null, null).getItems();
	}

	@Override
	public ListBasePagedResponse<D> findByFilter(Map<String, Object> parameters, String order, Long limit, String cursorString) {
		ListBasePagedResponse<D> result = new ListBasePagedResponse<D>();
		result.setItems(new ArrayList<D>());

	    try {
			Query<E> q = ofy().load().type(persistentClass);
			for(String key: parameters.keySet()) {
				q = q.filter(key, parameters.get(key));
			}

			if (order != null) {
				q = q.order(order);
			}

		  	if (limit != null) {
				q = q.limit(limit.intValue() + 1)
					.chunkAll();
			}
		
			if (cursorString != null) {
				q = q.startAt(Cursor.fromUrlSafe(cursorString));
			}

			QueryResults<E> iterator = q.iterator();
			List<D>  items = this.getConverter().convertToDataList(iterator, limit);
			result.setItems(items);
			result.setCursorString(iterator.getCursorAfter().toUrlSafe());

			if (limit != null) {
				result.setHasMore(iterator.hasNext());
			}	
	        
	    } catch (Exception e) {
		  log.warning("Error when querying for " + persistentClass.getSimpleName() + " by filters : " + e.toString());
		  e.printStackTrace();
	    }
		
		return result;		
	}

	@Override
	public D save(D data) {
		D result = null;
		E entity;
		try {
			entity = converter.convertFromData(data);
			ofy().save().entity(entity).now();
			result = converter.convertToData(entity);
		} catch (Exception e) {
			log.warning("Error when saving " + persistentClass.getSimpleName() + ": " + e.toString());
		}

		return result;
	}

	@Override
	public List<D> saveAll(List<D> data) {
		List<D> result = null;
		List<E> entities;
		try {
			entities = converter.convertFromDataList(data);
			ofy().save().entities(entities).now();
			result = converter.convertToDataList(entities);
		} catch (Exception e) {
			log.warning("Error when saving " + persistentClass.getSimpleName() + ": " + e.toString());
		}

		return result;
	}

	@Override
	public D delete(Long id) {
		D result = null;

		try {
			if (id != null) {
				E deleted = ofy().load().key(Key.create(persistentClass, id)).now();
				if (deleted != null) {
					result = converter.convertToData(deleted);
					ofy().delete().entity(deleted).now();
				}
			}			
		} catch (Exception e) {
			log.warning("Error when deleting " + persistentClass.getSimpleName() + ": " + e.toString());
		}

		return result;
	}

	@Override
	public List<D> deleteAll(List<Long> ids) {
		List<D> result = new ArrayList<D>();
		List<E> deletedList = new ArrayList<E>();

		try {
			if (ids != null && !ids.isEmpty()) {
				deletedList = ofy().load()
						.keys(ids.stream().map(id -> Key.create(persistentClass, id)).collect(Collectors.toList()))
						.values().stream().collect(Collectors.toList());
				if (deletedList != null && !deletedList.isEmpty()) {
					result = this.getConverter().convertToDataList(deletedList);
					ofy().delete().entities(deletedList).now();
				}
			}
		} catch (Exception e) {
			log.warning("Error when deleting " + persistentClass.getSimpleName() + ": " + e.toString());
		}

		return result;
	}

	@Override
	public List<D> listAll() {
		List<D> result = new ArrayList<D>();

		try {
			List<E> entityList = ofy().load().type(persistentClass).list();
			if (entityList != null && !entityList.isEmpty())
				result = this.getConverter().convertToDataList(entityList);
		} catch (Exception e) {
			log.warning("Error when querying all " + persistentClass.getSimpleName() + ": " + e.toString());
		}

		return result;
	}

}
