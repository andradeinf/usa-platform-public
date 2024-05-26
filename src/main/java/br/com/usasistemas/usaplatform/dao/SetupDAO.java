package br.com.usasistemas.usaplatform.dao;

public interface SetupDAO {

	void fixSupplierManufactureType();
	void removeNotUsedColumns();
	void cleanUpExpiredSessions();

}
