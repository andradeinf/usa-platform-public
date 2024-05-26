package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.MessageCommentData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;

public class WSMessageCommentListResponse extends GenericResponse {

	List<MessageCommentData> messageComments;
	Map<Long, PublicUserData> users;

	public List<MessageCommentData> getMessageComments() {
		return messageComments;
	}

	public void setMessageComments(List<MessageCommentData> messageComments) {
		this.messageComments = messageComments;
	}

	public Map<Long, PublicUserData> getUsers() {
		return users;
	}

	public void setUsers(Map<Long, PublicUserData> users) {
		this.users = users;
	}

}
