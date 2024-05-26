package br.com.usasistemas.usaplatform.model.data;

public class LetsEncryptChallengeData {
	
	private Long id;
	private String challenge;
	private String response;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
