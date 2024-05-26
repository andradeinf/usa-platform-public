package br.com.usasistemas.usaplatform.bizo;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;
import br.com.usasistemas.usaplatform.model.data.StateData;

public interface FranchiseeManagementBO {
	
	public List<FranchiseeData> getAllFranchisees();
	public FranchiseeData getFranchisee(Long id);
	public FranchiseeData saveFranchisee(FranchiseeData franchisee);
	public FranchiseeData deleteFranchisee(Long id);
	public List<FranchiseeUserData> getFranchiseeUsers(Long franchiseeId);
	public FranchiseeUserData saveFranchiseeUser(FranchiseeUserData franchiseeUser);
	public FranchiseeUserData deleteFranchiseeUser(Long id);
	public List<FranchiseeData> deleteFranchiseeByFranchisorId(Long franchisorId);
	public List<FranchiseeData> getFranchiseesByFranchisorId(Long francchisorId);
	public Map<Long, StateData> getFranchiseesStates(List<FranchiseeData> franchisees);
	public Map<Long, CityData> getFranchiseesCities(List<FranchiseeData> franchisees);
	public FranchiseeData addImage(Long franchiseeId, String imageKey);
	public FranchiseeData deleteFranchiseeImage(Long franchiseeId);
	
}
