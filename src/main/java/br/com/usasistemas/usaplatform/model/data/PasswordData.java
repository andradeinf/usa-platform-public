package br.com.usasistemas.usaplatform.model.data;

public class PasswordData {

	private String passwordHash;
	private String passwordSalt;
	
	public PasswordData(){
	}
	
	public PasswordData(String passwordHash, String passwordSalt){
		this.passwordHash = passwordHash;
		this.passwordSalt = passwordSalt;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

}
