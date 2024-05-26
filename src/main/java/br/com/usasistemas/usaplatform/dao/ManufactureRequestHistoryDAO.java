package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.dao.response.ManufactureRequestHistoryPagedResponse;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestHistoryData;
import br.com.usasistemas.usaplatform.model.entity.ManufactureRequestHistory;


public interface ManufactureRequestHistoryDAO extends GenericDAO<ManufactureRequestHistory, ManufactureRequestHistoryData> {
	
	public ManufactureRequestHistoryPagedResponse findByProductId (Long productId, Long pageSize, String cursorString);
	public List<ManufactureRequestHistoryData> findByFranchisorId(Long franchisorId);
	public List<ManufactureRequestHistoryData> findBySupplierId(Long supplierId);
	public List<ManufactureRequestHistoryData> findByProductSizeId(Long id);
	public ManufactureRequestHistoryPagedResponse findByFranchisorId(Long franchisorId, Long pageSize, String cursorString);
	public List<ManufactureRequestHistoryData> findByProductId(Long productId);
	
}
