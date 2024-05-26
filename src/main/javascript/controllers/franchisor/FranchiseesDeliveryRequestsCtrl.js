angular.module('franchisorLogistics').controller('FranchiseesDeliveryRequestsCtrl', function ($scope, httpSvc) {

	$scope.showDeliveryRequestHistory = function(item) {
    	$scope.deliveryRequest = item;
    }

    $scope.fetchDeliveryRequests = function() {
    	
    	LoadingUtil.show($("#deliveryLoad"));
    	
    	//get list of delivery requests
		var deliveryRequestsConfig = {
				  method: 'GET' ,
				  url: '/ws/delivery/franchisee/'+$scope.franchiseeId+'/paged/'+$scope.cursorString,
				  unique: true,
				  requestId: 'deliveryRequests-load'
				};
		
		httpSvc(deliveryRequestsConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					LoadingUtil.hide($("#deliveryLoad"));
					
					if (data.deliveryRequests.length == 0) {
						if ($scope.cursorString == 'none') {
							$scope.noRecordsFound = true;
						} else {
							NotificationUtil.show($('#notificationArea'), 'warning', 'Atenção!', 'Não há mais pedidos de entrega.');
						}
					}
					
					$scope.cursorString = data.cursorString;
					
					data.deliveryRequests.forEach(function(entry) {
						$scope.deliveryRequests.push(entry);
						
						entry.franchisee = data.franchisees[entry.franchiseeId];
						entry.franchisor = data.franchisors[entry.franchisee.franchisorId];
						entry.product = data.products[entry.productId];
						entry.product.sizes.forEach(function(size) {
							if (size.id == entry.productSizeId) {
								entry.productSize = size;
							}
						});
						entry.supplier = data.suppliers[entry.product.supplierId];
						entry.calculateTotal = function() {return entry.quantity * entry.deliveryUnitPrice};
					});
					
					$scope.initialLoad = false;
					
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os pedidos de entrega!');
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os pedidos de entrega!');
				}
			});
    }
    
    $scope.changeFranchisee = function(){
    	$scope.cursorString = 'none';
		$scope.deliveryRequests = [];		
		$scope.noRecordsFound = false;
		$scope.fetchDeliveryRequests();		
    }
    
	var init = function() {
		
		$scope.cursorString = 'none';
		$scope.deliveryRequests = [];
		$scope.noRecordsFound = false;
		
		//franchisees
		$scope.franchiseeId = 0;
		$scope.franchisees = [];
		$scope.franchisees.push({"id":0,"name":"Carregando ..."})
		
		//get franchisees
		var franchiseesConfig = {
			  method: 'GET',
			  url: '/ws/franchisees/franchisor/'+gFranchisorId,
			  unique: false,
			  requestId: 'franchisees-load'
			};
		
		httpSvc(franchiseesConfig)
			.success(function(data){
				$scope.franchisees = data.franchisees;
				$scope.franchisees.unshift({"id":0,"name":" Selecione ..."})
			});
	
	}
	
	init();
	
});