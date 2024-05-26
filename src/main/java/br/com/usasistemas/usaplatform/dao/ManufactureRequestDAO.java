package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.dao.response.ManufactureRequestPagedResponse;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.entity.ManufactureRequest;
import br.com.usasistemas.usaplatform.model.enums.ManufactureRequestStatusEnum;


public interface ManufactureRequestDAO extends GenericDAO<ManufactureRequest, ManufactureRequestData> {
	
	public ManufactureRequestPagedResponse findByProductId (Long productId, Long pageSize, String cursorString);
	public List<ManufactureRequestData> findCompletedByProductSizeId(Long productSizeId);
	public List<ManufactureRequestData> findByFranchisorId(Long franchisorId);
	public List<ManufactureRequestData> findBySupplierId(Long supplierId);
	public List<ManufactureRequestData> findBySupplierIdAndStatus(Long supplierId, ManufactureRequestStatusEnum manufactureStatus);
	public List<ManufactureRequestData> findByProductSizeId(Long id);
	public List<ManufactureRequestData> findFinishedOlderThanDays(Long numberOfDays, String domainKey);
	public ManufactureRequestPagedResponse findByFranchisorId(Long franchisorId, Long pageSize, String cursorString);
	public List<ManufactureRequestData> findByProductId(Long productId);
	
}
