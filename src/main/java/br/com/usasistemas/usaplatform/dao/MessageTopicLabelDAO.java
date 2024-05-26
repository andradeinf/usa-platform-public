package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.MessageTopicLabelData;
import br.com.usasistemas.usaplatform.model.entity.MessageTopicLabel;

public interface MessageTopicLabelDAO extends GenericDAO<MessageTopicLabel, MessageTopicLabelData> {

	public List<MessageTopicLabelData> findByEntityUserId(Long entityUserId);

}
