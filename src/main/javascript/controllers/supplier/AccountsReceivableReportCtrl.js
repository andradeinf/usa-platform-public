angular.module('franchisorLogistics').controller('AccountsReceivableReportCtrl', function ($scope, $routeParams, httpSvc) {
	
    $scope.openFromDatePicker = function() {
	    $scope.filter.fromDateOpened = true;
	};
	
	$scope.openToDatePicker = function() {
	    $scope.filter.toDateOpened = true;
	};
	
	$scope.fetchDeliveryRequests = function() {
    	
    	LoadingUtil.show($("#deliveryLoad"));
    	
    	//get list of delivery requests
		var deliveryRequestsConfig = {
				  method: 'POST' ,
				  url: '/ws/delivery/supplier/'+$scope.supplierId+'/accountsReceivable/paged/'+$scope.cursorString,
				  data: $scope.searchFilter,
				  unique: true,
				  requestId: 'accountsReceivable-load'
				};
		
		httpSvc(deliveryRequestsConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					LoadingUtil.hide($("#deliveryLoad"));
					
					if (data.deliveryRequests.length == 0) {
						if ($scope.cursorString == 'none') {
							$scope.noRecordsFound = true;
						} else {
							NotificationUtil.show($('#notificationArea'), 'warning', 'Atenção!', 'Não há mais pedidos de entrega a receber.');
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
    
    $scope.searchDeliveryRequests = function(){
    	$scope.cursorString = 'none';
		$scope.deliveryRequests = [];		
		$scope.noRecordsFound = false;
		
		$scope.searchFilter = {
			  fromDate: $scope.filter.fromDate
			, toDate: $scope.filter.toDate
		}
		
		$scope.fetchDeliveryRequests();				
    }
	  
	var init = function() {
		
		$scope.dateOptions = {
			formatYear: 'yy',
			startingDay: 1
		};
		
		$scope.formats = ['dd/MM/yyyy'];
		
		$scope.filter = {
			  fromDate: new Date()
		    , fromDateOpened: false
			, toDate: new Date()
		    , toDateOpened: false
		}
		$scope.filter.toDate.setDate($scope.filter.fromDate.getDate() + 7);
	
		if ($routeParams.supplierId) {
			$scope.supplierId = $routeParams.supplierId;
		} else {
			$scope.supplierId = gSupplierId;
		}	
		
		//deliveryRequests
		$scope.cursorString = 'none';
		$scope.deliveryRequests = [];
		$scope.noRecordsFound = false;
	}
	
	init();
	
});