package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.entity.UserGroup;

public interface UserGroupDAO extends GenericDAO<UserGroup, UserGroupData> {

	public List<UserGroupData> findByEntityId(Long entityId);
	
}
