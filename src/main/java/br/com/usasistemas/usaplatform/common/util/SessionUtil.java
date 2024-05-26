package br.com.usasistemas.usaplatform.common.util;

import javax.servlet.http.HttpSession;

import br.com.usasistemas.usaplatform.model.data.UserProfileData;

public class SessionUtil {
	
	public static final String LOGGED_USER_ATTR = "loggedUser";
	
	public static UserProfileData getLoggedUser(HttpSession session) {

		UserProfileData user = null;
		
		if (session != null) {
			user = (UserProfileData) session.getAttribute(SessionUtil.LOGGED_USER_ATTR);
		}
		
		return user;
	}
	
}
