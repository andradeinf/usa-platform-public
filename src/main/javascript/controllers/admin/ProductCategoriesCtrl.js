angular.module('franchisorLogistics').controller('ProductCategoriesCtrl', function ($scope, $rootScope, $routeParams, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.save = function(){
		if (validate()) {
			
			delete $scope.productCategory.requestMode;
		
			var config = {
				  method: ($scope.productCategory.id == 0 ? 'POST' : 'PUT') ,
				  url: '/ws/products/categories',
				  data: $scope.productCategory,
				  unique: true,
				  requestId: 'productCategory-createOrUpdate'
				};
				
			httpSvc(config)
				.success(function(data){
					if ($scope.productCategory.id == 0) {
						$scope.productCategoryList.push(data);
					}
					$('#newProductCategoryModal').modal('hide');
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados da categoria de '+$rootScope.domainConfiguration.labels['PRODUCT_LOWER']+' salvos com sucesso!');
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados da categoria de '+$rootScope.domainConfiguration.labels['PRODUCT_LOWER']+'!');
					}
				});						
		};
	}
	
	$scope.edit = function(item){
		$scope.productCategory = item;
		$scope.cancelAddRestriction();
	}
	
	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir a categoria de '+$rootScope.domainConfiguration.labels['PRODUCT_LOWER']+'?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){
		
		var config = {
				method: 'DELETE' ,
				url: '/ws/products/categories/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'productCategory-delete'
			};
	
		httpSvc(config)
			.success(function(){
				$scope.productCategoryList = $scope.productCategoryList.filter(productCategory => productCategory.id != $scope.itemToRemove.id);
				NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Categoria de '+$rootScope.domainConfiguration.labels['PRODUCT_LOWER']+' excluída com sucesso!');
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir a categoria de '+$rootScope.domainConfiguration.labels['PRODUCT_LOWER']+'!');
				}
			});
	}
	
	$scope.onChangeState = function(){
		$scope.restriction.cityId = 0;
		$scope.cities = [];
		if ($scope.restriction.stateId != 0) {
			loadStateCities();
		}
		
	}
	
	var loadStateCities = function(){
		
		if ($scope.restriction.stateId != 0) {
			
			$scope.cities.push({"id":0,"name":"Carregando ..."})
		
			//get cities
			var statesConfig = {
				  method: 'GET',
				  url: '/ws/statesAndCities/states/'+$scope.restriction.stateId+'/cities',
				  unique: false,
				  requestId: 'cities-load'
				};
			
			httpSvc(statesConfig)
				.success(function(data){
					$scope.cities = data;
					$scope.cityListTemp = {};
					$scope.cities.forEach(function(entry, index, array) {
						$scope.cityListTemp[entry.id] = entry;
					});
					$scope.cities.unshift({"id":0,"name":" TODAS"})
				});
			
		}
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#name').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.productCategory.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Nome não pode ser vazio</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	$scope.addRestriction = function(){
		$scope.productCategory.requestMode = true;
		resetRestriction();
    }
	
	$scope.saveAddRestriction = function(){
		$scope.cityList[$scope.restriction.cityId] = $scope.cityListTemp[$scope.restriction.cityId];
		$scope.productCategory.restrictions.push($scope.restriction.stateId + "-" + $scope.restriction.cityId);		
		$scope.productCategory.requestMode = false;
		resetRestriction();
    }
	
	$scope.cancelAddRestriction = function(){
		$scope.productCategory.requestMode = false;
	}
	
	$scope.deleteRestriction = function(restriction){
		var index = $scope.productCategory.restrictions.indexOf(restriction);
		$scope.productCategory.restrictions.splice(index, 1);
	}
	
	$scope.getRestrictionState = function(restriction, code){
		var restrictionData = restriction.split("-");
		if (code) {
			return $scope.stateList[restrictionData[0]].code;
		} else {
			return $scope.stateList[restrictionData[0]].name;
		}
	}
	
	$scope.getRestrictionCity = function(restriction){
		var restrictionData = restriction.split("-");
		if (restrictionData[1] == 0) {
			return "TODAS";
		} else {
			return $scope.cityList[restrictionData[1]].name;
		}
	}
	
	$scope.getRestrictionList = function(restrictions){
		var text = "";
		restrictions.forEach(function(entry, index, array) {
			text += $scope.getRestrictionState(entry, true) + " - " + $scope.getRestrictionCity(entry) + "; "
		});
		return text;
	}
	
	var initializeItem = function(item) {
		item.requestMode = false;
	}
	
	var reset = function() {
		$scope.productCategory = { id: 0
							, franchisorId: $routeParams.franchisorId
							, name: ""
						 	, notes: ""
						 	, order: 0
						 	, restrictions: []
						 	, requestMode: false}
						 	
		//clear validation
		resetValidation();		
		
		resetRestriction();
	}
	
	var resetRestriction = function(){
		$scope.restriction = {
				stateId: 0,
				cityId: 0
		}
		
		$scope.onChangeState();
	}
	
	var init = function() {
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/products/categories/franchisor/'+$routeParams.franchisorId,
			  unique: false,
			  requestId: 'productCategory-load'
			};
	
		httpSvc(config)
			.success(function(data){
				data.productCategoryList.forEach(function(entry, index, array) {
				    initializeItem(entry);
				});
				$scope.productCategoryList = data.productCategoryList;
				$scope.stateList = data.states;
				$scope.cityList = data.cities;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
		
		//states and cities
		$scope.states = [];
		$scope.states.push({"id":0,"name":"Carregando ..."})
		$scope.cities = [];
		
		//get states
		var statesConfig = {
			  method: 'GET',
			  url: '/ws/statesAndCities/states',
			  unique: false,
			  requestId: 'states-load'
			};
		
		httpSvc(statesConfig)
			.success(function(data){
				$scope.stateList = {};
				$scope.states = data;
				$scope.states.forEach(function(entry, index, array) {
					$scope.stateList[entry.id] = entry;
				});
				$scope.states.unshift({"id":0,"name":" Selecione ..."})
				
			});
	
		reset();
	}
	
	init();
});