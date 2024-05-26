package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.MessageCommentData;
import br.com.usasistemas.usaplatform.model.entity.MessageComment;

public interface MessageCommentDAO extends GenericDAO<MessageComment, MessageCommentData> {

	public List<MessageCommentData> findByMessageTopicId(Long messageTopicId);

	
}
