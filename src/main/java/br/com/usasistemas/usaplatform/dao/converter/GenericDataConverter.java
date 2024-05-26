package br.com.usasistemas.usaplatform.dao.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenericDataConverter<E, D> {

	public GenericDataConverter() {
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public D convertToData(E entity) throws ClassNotFoundException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {

		// check entity and data classes
		Class<? extends Object> entityClass = entity.getClass();
		Class<?> dataClass = Class.forName(entityClass.getName().replace("entity", "data") + "Data");

		// create an instance of the data object
		Constructor<?> dataCt = dataClass.getConstructor();
		D retobj = (D) dataCt.newInstance();

		// for each data method
		Method dataMethlist[] = dataClass.getDeclaredMethods();
		Method entityMethlist[] = entityClass.getDeclaredMethods();
		for (Method dataMeth : dataMethlist) {
			// for set Methods
			if (dataMeth.getName().startsWith("set")) {
				// get corresponding get method in the entity object
				for (Method entityMeth : entityMethlist) {
					if (entityMeth.getName().startsWith("get")
							&& entityMeth.getName().substring(3).equals(dataMeth.getName().substring(3))) {

						Object entityArglist[] = new Object[0];
						Object entityRetVal = entityMeth.invoke(entity, entityArglist);

						Object dataArglist[] = new Object[1];
						dataArglist[0] = entityRetVal;

						Object dataRetVal = dataMeth.invoke(retobj, dataArglist);
					}
				}
			}
		}

		return retobj;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public E convertFromData(D data) throws ClassNotFoundException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {

		// check entity and data classes
		Class<? extends Object> dataClass = data.getClass();
		Class<?> entityClass = Class.forName(dataClass.getName().replace("data", "entity").replace("Data", ""));

		// create an instance of the entity object
		Constructor<?> entityCt = entityClass.getConstructor();
		E retobj = (E) entityCt.newInstance();

		// for each data method
		Method dataMethlist[] = dataClass.getDeclaredMethods();
		Method entityMethlist[] = entityClass.getDeclaredMethods();
		for (Method entityMeth : entityMethlist) {
			// for set Methods
			if (entityMeth.getName().startsWith("set")) {
				// get corresponding get method in the entity object
				for (Method dataMeth : dataMethlist) {
					if (dataMeth.getName().startsWith("get")
							&& dataMeth.getName().substring(3).equals(entityMeth.getName().substring(3))) {

						Object dataArglist[] = new Object[0];
						Object dataRetVal = dataMeth.invoke(data, dataArglist);

						Object entityArglist[] = new Object[1];
						entityArglist[0] = dataRetVal;

						Object entityRetVal = entityMeth.invoke(retobj, entityArglist);
					}
				}
			}
		}

		return retobj;
	}

	public List<D> convertToDataList(List<E> entities)
			throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		List<D> results = null;

		if (!entities.isEmpty()) {
			results = new ArrayList<D>();
			for (E entity : entities) {
				results.add(convertToData(entity));
			}
		}

		return results;
	}

	public List<D> convertToDataList(Iterator<E> entitiesIterator, Long limit)
			throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		List<D> results = new ArrayList<D>();
		int count = 0;
		
		while (entitiesIterator.hasNext() && (limit == null || count < limit.intValue())) {
			count++;
			results.add(convertToData(entitiesIterator.next()));
		}
		
		return results;		
	}
	
	public List<E> convertFromDataList(List<D> datas) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		List<E> results = null;

		if (!datas.isEmpty()) {
			results = new ArrayList<E>();
			for (D data : datas) {
				results.add(convertFromData(data));
			}
		}

		return results;		
	}

}
