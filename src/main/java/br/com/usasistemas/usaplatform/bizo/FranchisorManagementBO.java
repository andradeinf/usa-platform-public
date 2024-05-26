package br.com.usasistemas.usaplatform.bizo;

import java.util.List;

import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.FranchisorUserData;

public interface FranchisorManagementBO {

	public List<FranchisorData> getAllFranchisors();
	public FranchisorData getFranchisor(Long id);
	public FranchisorData saveFranchisor(FranchisorData franchisor);
	public FranchisorData deleteFranchisor(Long id);
	public List<FranchisorUserData> getFranchisorUsers(Long franchisorId);
	public FranchisorUserData saveFranchisorUser(FranchisorUserData franchisorUser);
	public FranchisorUserData deleteFranchisorUser(Long id);
	public FranchisorData addImage(Long franchisorId, String imagebKey);	
	public FranchisorData getFranchisorByLoginURL(String loginURL);
	public List<FranchisorData> getAllDomainFranchisors(String domainKey);

}
