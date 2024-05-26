angular.module('franchisorLogistics').controller('SuppliersCtrl', function ($scope, $routeParams, httpSvc) {
	
	$scope.toggleFilter = function(){
		$scope.showFilter = !$scope.showFilter;
	}
	
	var init = function() {
		
		$scope.noRecordsFound = false;
		$scope.supplierFilter = '';
		$scope.showFilter = false;
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/suppliers/franchisor/'+gFranchisorId+'/category/'+$routeParams.categoryId,
			  unique: false,
			  requestId: 'suppliers-load'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					data.suppliers.forEach(function(entry) {
						entry.state = data.states[entry.stateId];
						entry.city = data.cities[entry.cityId];
						if (entry.imageURL == null) {
							entry.thumbnail = '/img/not-available.jpg';
						} else {
							entry.thumbnail = entry.imageURL + '=s150';
						}
						if (entry.description == null) {
							entry.description = '';
						}
					});
					$scope.supplierList = data.suppliers;
					$scope.stateMap = data.states;
					
					if (data.suppliers.length == 0) {
						$scope.noRecordsFound = true;
					}
					
					$('#list-overlay').remove();
					$('#list-loading-img').remove();
				}  else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}				
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}
			});
		
	}
	
	init();
});