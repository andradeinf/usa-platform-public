angular.module('franchisorLogistics').controller('SupplierCategoriesCtrl', function ($scope, $rootScope, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.save = function(){
		if (validate()) {
			
			var config = {
				  method: ($scope.supplierCategory.id == 0 ? 'POST' : 'PUT') ,
				  url: '/ws/suppliers/categories',
				  data: $scope.supplierCategory,
				  unique: true,
				  requestId: 'supplierCategory-createOrUpdate'
				};
				
			httpSvc(config)
				.success(function(data){
					if ($scope.supplierCategory.id == 0) {
						$scope.supplierCategoryList.push(data.supplierCategory);
					}
					$('#newSupplierCategoryModal').modal('hide');
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados da categoria de '+$rootScope.domainConfiguration.labels['SUPPLIER_LOWER']+' salvos com sucesso!');
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados da categoria de '+$rootScope.domainConfiguration.labels['SUPPLIER_LOWER']+'!');
					}
				});						
		};
	}
	
	$scope.edit = function(item){
		$scope.supplierCategory = item;
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir a categoria de '+$rootScope.domainConfiguration.labels['SUPPLIER_LOWER']+'?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){
	
		var config = {
				method: 'DELETE' ,
				url: '/ws/suppliers/categories/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'supplierCategory-delete'
			};
	
		httpSvc(config)
			.success(function(){
				$scope.supplierCategoryList = $scope.supplierCategoryList.filter(supplierCategory => supplierCategory.id != $scope.itemToRemove.id);
				NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Categoria de '+$rootScope.domainConfiguration.labels['SUPPLIER_LOWER']+' excluída com sucesso!');
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir a categoria de '+$rootScope.domainConfiguration.labels['SUPPLIER_LOWER']+'!');
				}
			});
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#name').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.supplierCategory.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Nome' não pode ser vazio</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		$scope.supplierCategory = { id: 0
							, name: ""
							, order: 0
						 	, hasStockControl: false
						 	, visibleToFranchisees: false
						 	, receiveMessage: false}
						 	
		//clear validation
		resetValidation();								 	
	}
	
	var init = function() {
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/suppliers/categories',
			  unique: false,
			  requestId: 'supplierCategory-load'
			};
	
		httpSvc(config)
			.success(function(data){
				$scope.supplierCategoryList = data.supplierCategories;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
		
		reset();
	}
	
	init();
});