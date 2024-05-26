package br.com.usasistemas.usaplatform.api.response.data;

import java.io.Serializable;

import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@SuppressWarnings("serial")
public class UserProfileDataResponse  implements Serializable {
	
	private Long id;
	private String name;
	private String imageURL;
	private UserProfileEnum profileType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public UserProfileEnum getProfileType() {
		return profileType;
	}

	public void setProfileType(UserProfileEnum profileType) {
		this.profileType = profileType;
	}

}
