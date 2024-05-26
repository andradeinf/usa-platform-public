package br.com.usasistemas.usaplatform.api.response;

import java.util.List;
import java.util.Map;

import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.ProductCategoryData;
import br.com.usasistemas.usaplatform.model.data.StateData;

public class WSProductServiceProductCategoryListResponse extends GenericResponse {

	private List<ProductCategoryData> productCategoryList;
	private Map<Long, StateData> states;
	private Map<Long, CityData> cities;

	public List<ProductCategoryData> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<ProductCategoryData> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}
	
	public Map<Long, StateData> getStates() {
		return states;
	}

	public void setStates(Map<Long, StateData> states) {
		this.states = states;
	}

	public Map<Long, CityData> getCities() {
		return cities;
	}

	public void setCities(Map<Long, CityData> cities) {
		this.cities = cities;
	}

}
