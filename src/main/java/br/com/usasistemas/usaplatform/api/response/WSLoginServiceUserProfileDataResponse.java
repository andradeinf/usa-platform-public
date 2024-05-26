package br.com.usasistemas.usaplatform.api.response;

import java.util.List;

import br.com.usasistemas.usaplatform.api.response.data.UserProfileDataResponse;

public class WSLoginServiceUserProfileDataResponse extends GenericResponse {

	private List<UserProfileDataResponse> userProfiles;

	public List<UserProfileDataResponse> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(List<UserProfileDataResponse> userProfiles) {
		this.userProfiles = userProfiles;
	}

}
