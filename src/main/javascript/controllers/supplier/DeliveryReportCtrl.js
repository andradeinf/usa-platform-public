angular.module('franchisorLogistics').controller('DeliveryReportCtrl', function ($scope, $routeParams, httpSvc) {
	
	$scope.loadFranchisees = function(item){
		$scope.franchiseeId = 0;
		$scope.franchisees = [];
		$scope.franchiseesDetails = {};
		$scope.franchisees.push({"id":0,"name":"Carregando ..."})
		
		$scope.deliveryRequests = [];		
		$scope.noRecordsFound = false;
		
		if ($scope.franchisorId != 0) {
		
			//get franchisees
			var config = {
				  method: 'GET',
				  url: '/ws/franchisees/franchisor/'+$scope.franchisorId,
				  unique: false,
				  requestId: 'franchisees-load'
				};
			
			httpSvc(config)
				.success(function(data){
					$scope.franchisees = data.franchisees;
					$scope.franchisees.unshift({"id":0,"name":" Selecione ..."})
					
					$scope.franchisees.forEach(function(franchisee) {
						$scope.franchiseesDetails[franchisee.id] = franchisee;
					});	
				});
		}
	}
	
	$scope.statusClass = function(status){
    	var className = 'label-warning';
    	if (status == 'PENDING') {className = 'label-info';}
    	if (status == 'COMPLETED') {className = 'label-success';}
    	if (status == 'CANCELLED') {className = 'label-danger';}
    	if (status == 'ADJUSTMENT') {className = 'label-danger';}
    	return className;
    };
    
    $scope.fetchDeliveryRequests = function() {
    	
    	LoadingUtil.show($("#deliveryLoad"));
    	
    	//get list of delivery requests
		var deliveryRequestsConfig = {
				  method: 'GET' ,
				  url: '/ws/delivery/supplier/'+$scope.supplierId+'/franchisee/'+$scope.franchiseeId+'/status/'+$scope.deliveryStatus+'/paged/'+$scope.cursorString,
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
						
						entry.product = data.products[entry.productId];
						entry.product.sizes.forEach(function(size) {
							if (size.id == entry.productSizeId) {
								entry.productSize = size;
							}
						});	
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
    
    $scope.reloadDeliveryRequests = function(){
    	$scope.cursorString = 'none';
		$scope.deliveryRequests = [];		
		$scope.noRecordsFound = false;
		
		if ($scope.franchiseeId != 0) {
			$scope.fetchDeliveryRequests();	
		}			
    }
	
	var init = function() {
	
		if ($routeParams.supplierId) {
			$scope.supplierId = $routeParams.supplierId;
		} else {
			$scope.supplierId = gSupplierId;
		}	
		
		//deliveryRequests
		$scope.cursorString = 'none';
		$scope.deliveryRequests = [];
		$scope.noRecordsFound = false;
		
		//franchisees
		$scope.franchiseeId = 0;
		$scope.franchisees = [];
		$scope.franchiseesDetails = {};
		
		//franchisors
		$scope.franchisorId = 0;
		$scope.franchisors = [];
		$scope.franchisors.push({"id":0,"name":"Carregando ..."})
		
		//get franchisors
		var config = {
			  method: 'GET',
			  url: '/ws/suppliers/'+$scope.supplierId+'/franchisors',
			  unique: false,
			  requestId: 'franchisors-load'
			};
		
		httpSvc(config)
			.success(function(data){
				if (data.franchisors.length == 1) {
					$scope.franchisorId = data.franchisors[0].id;
					$scope.franchisors = data.franchisors;
					$scope.loadFranchisees();
				} else {
					$scope.multipleFranchisors = true;
					$scope.franchisors = data.franchisors;
					$scope.franchisors.unshift({"id":0,"name":" Selecione ..."})
				}				
			});
	
		//get delivery request statuses
		var deliveryStatusesConfig = {
				  method: 'GET',
				  url: '/ws/delivery/statuses',
				  unique: false,
				  requestId: 'deliveryStatuses-load'
				};
		
		httpSvc(deliveryStatusesConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {
			    	$scope.deliveryStatuses = data.enumValues;
			    	$scope.deliveryStatus = data.enumValues[0].key;
				} else {
					$scope.deliveryStatuses = [{key: 0, value: 'Erro ao carregar!'}];
					$scope.deliveryStatus = 0;
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os status de entrega!');
			});
	}
	
	init();
	
});