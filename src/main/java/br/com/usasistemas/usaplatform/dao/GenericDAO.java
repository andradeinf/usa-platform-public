package br.com.usasistemas.usaplatform.dao;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.dao.response.ListBasePagedResponse;

public interface GenericDAO<E,D> {
	
	public D findById(Long id);
	public D save(D data);
	public D delete(Long id);
	public List<D> findByIds(List<Long> ids);
	public List<D> findByFilter(Map<String, Object> parameters);
	public ListBasePagedResponse<D> findByFilter(Map<String, Object> parameters, String order, Long limit, String cursorString);
	public List<D> listAll();
	public List<D> saveAll(List<D> data);
	public List<D> deleteAll(List<Long> ids);

}
