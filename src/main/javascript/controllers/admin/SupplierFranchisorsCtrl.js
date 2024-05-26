angular.module('franchisorLogistics').controller('SupplierFranchisorsCtrl', function ($scope, $routeParams, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.save = function(){
		var config = {
			  method: 'POST',
			  url: '/ws/suppliers/'+$routeParams.supplierId+'/franchisors',
			  data: $scope.supplierFranchisorList,
			  unique: true,
			  requestId: 'supplierFranchisors-update'
			};
			
		httpSvc(config)
			.success(function(data){
				NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados salvos com sucesso!');
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados!');
				}
			});
	}
	
	var init = function() {
		
		$scope.franchisorFilter = '';
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/suppliers/'+$routeParams.supplierId+'/franchisorsMap',
			  unique: false,
			  requestId: 'supplierFranchisors-load'
			};
	
		httpSvc(config)
			.success(function(data){
				$scope.franchisorList = data.franchisors;
				$scope.supplierFranchisorList = data.supplierFranchisors;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
	}
	
	init();
});