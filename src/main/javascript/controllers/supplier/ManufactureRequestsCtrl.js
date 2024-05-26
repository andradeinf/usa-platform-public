angular.module('franchisorLogistics').controller('ManufactureRequestsCtrl', function ($scope, $routeParams, httpSvc) {
    
    $scope.toggleFullContactInfo = function(item) {
    	var current = item.showFullContactInfo;
    	item.showFullContactInfo = !current;
    }
    
    $scope.editManufacture = function(item){
    	item.updateStatusRequestMode = true;
    }
    
    $scope.cancelEditManufacture = function(item){
    	initializeManufacture(item);
    }
    
    $scope.saveEditManufacture = function(item){
    	
    	var config = {
			  method: 'PUT',
			  url: '/ws/manufacture/status',
			  data: item.updateStatusRequest,
			  unique: true,
			  requestId: 'manufacture-statusUpdate'
			};
    	
    	httpSvc(config)
			.success(function(data){
				var index = $scope.manufactureRequests.indexOf(item);
				if (data.manufactureRequest.status == item.status) {
					//no status update, only data (Sent information)
					initializeManufacture(data);
					$scope.manufactureRequests[index] = data;
				} else {
					$scope.manufactureRequests.splice(index, 1);
				}
				NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Produção atualizada com sucesso!');
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao atualizar os dados de produção!');
				}
			});	            		
    }

	var initializeManufacture = function(item) {
		item.product = $scope.manufactureRequestsProducts[item.productId];
		item.franchisor = $scope.manufactureRequestsFranchisors[item.franchisorId];
		
		if (item.product.imageURL == null) {
			item.thumbnail = '/img/not-available.jpg';
		} else {
			item.thumbnail = item.product.imageURL+'=s150';
		}
		item.product.sizes.forEach(function(size, index, array) {
    		if (size.id == item.productSizeId) {
    			item.productSize = size;
		    }
		});		
    	item.showFullContactInfo = false; 
    	item.calculateManufacture = function() {return item.quantity * item.manufactureUnitPrice}
    	item.updateStatusRequestMode = false;
    	item.updateStatusRequest = { manufactureRequestId: item.id
    	                           , status: item.status
    	                           , cancellationComment: item.cancellationComment}
    }
    
	$scope.editDelivery = function(item){
    	item.updateStatusRequestMode = true;
    }
    
    $scope.cancelEditDelivery = function(item){
    	initializeDelivery(item);
    }
    
    $scope.saveEditDelivery = function(item){
    	
    	var config = {
  			  method: 'PUT',
  			  url: '/ws/delivery/status',
  			  data: item.updateStatusRequest,
  			  unique: true,
  			  requestId: 'delivery-statusUpdate'
  			};
      	
      	httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					var index = $scope.deliveryRequests.indexOf(item);
					if (data.deliveryRequest.status == item.status) {
						//no status update, only data (Sent information)
						initializeDelivery(data.deliveryRequest);					
						$scope.deliveryRequests[index] = data.deliveryRequest;
					} else {
						$scope.deliveryRequests.splice(index, 1);
					}					
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Entrega atualizada com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao atualizar os dados da entrega!');
				}
			});	            		
    }
    
	var initializeDelivery = function(item) {
    	item.product = $scope.deliveryRequestsProducts[item.productId];
    	item.franchisee = $scope.deliveryRequestsFranchisees[item.franchiseeId];
    	
    	item.product.sizes.forEach(function(size, index, array) {
    		if (size.id == item.productSizeId) {
    			item.productSize = size;
		    }
		});    	
		item.calculateDelivery = function() {return item.quantity * item.deliveryUnitPrice} 
    	
    	item.showFullContactInfo = false;          	
    	item.updateStatusRequestMode = false;
    	item.updateStatusRequest = { deliveryRequestId: item.id
    	                           , status: item.status
    	                           , cancellationComment: item.cancellationComment
    	                           , sentDate: ''
    	                           , deadlineDate: ''
    	                           , carrierName: ''
    	                           , trackingCode: ''
    	                           , fiscalNumber: ''};
    }
	
	$scope.getImageURL = function(item) {
    	
		if (item.imageURL == null) {
			return '/img/not-available.jpg';
		} else {
			return item.imageURL+'=s150';
		}
    }
	
	$scope.loadDeliveryRequests = function() {

		LoadingUtil.show($('#loadingArea'));
		
		var deliveryRequestsConfig = {
				  method: 'GET',
				  url: '/ws/delivery/supplier/'+$scope.supplierId+'/status/'+$scope.deliveryStatus,
				  unique: false,
				  requestId: 'deliveryRequests-load'
				};
		
		httpSvc(deliveryRequestsConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {			
					$scope.deliveryRequestsProducts = data.products;
					$scope.deliveryRequestsFranchisees = data.franchisees;
					$scope.deliveryRequestsFranchisors = data.franchisors;
					$scope.deliveryRequestsFranchiseesList = [];
					$scope.deliveryRequestsFranchiseesList = [{id: 1234567890, name: ' Selecione...'}]; //Space to be in the top of the sort list
					$scope.franchiseeId = 1234567890; //Any ugly franchiseeId that we are sure does not come in any response
					for (var key in data.franchisees) {
						$scope.deliveryRequestsFranchiseesList.push(data.franchisees[key]);
					}
					$scope.deliveryRequests = data.deliveryRequests;
					$scope.deliveryRequests.forEach(function(entry, index, array) {
					    initializeDelivery(entry);
					});										
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados de entrega!');
				}
				LoadingUtil.hide($('#loadingArea'));
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados de entrega!');
				LoadingUtil.hide($('#loadingArea'));
			});
	}
	
	$scope.loadManufactureRequests = function() {
		
		LoadingUtil.show($('#loadingArea'));
		
		var manufactureRequestsConfig = {
				  method: 'GET',
				  url: '/ws/manufacture/supplier/'+$scope.supplierId+'/status/'+$scope.manufactureStatus,
				  unique: false,
				  requestId: 'manufactureRequests-load'
				};
		
		httpSvc(manufactureRequestsConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {			
					$scope.manufactureRequests = data.manufactureRequests;
					$scope.manufactureRequestsProducts = data.products;
					$scope.manufactureRequestsFranchisors = data.franchisors;
					
					data.manufactureRequests.forEach(function(entry, index, array) {
					    initializeManufacture(entry);
					});					
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados de produção!');
				}
				LoadingUtil.hide($('#loadingArea'));
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados de produção!');
				LoadingUtil.hide($('#loadingArea'));
			});
	}
	
    var init = function() {
    	
    	if ($routeParams.supplierId) {
			$scope.supplierId = $routeParams.supplierId;
		} else {
			$scope.supplierId = gSupplierId;
		}
    	$scope.supplierType = '';
    	
    	//get supplier info
    	var supplierConfig = {
				  method: 'GET',
				  url: '/ws/suppliers/'+$scope.supplierId,
				  unique: false,
				  requestId: 'supplier-load'
				};
    	
    	httpSvc(supplierConfig)
			.success(function(data){
				$scope.supplierType = data.type;
				
				if ($scope.supplierType == 'RECEIVE_MANUFACTURE_REQUEST') {
					
					//get manufacture request statuses
					var manufactureStatusesConfig = {
							  method: 'GET',
							  url: '/ws/manufacture/statuses',
							  unique: false,
							  requestId: 'manufactureStatuses-load'
							};
			    	
					httpSvc(manufactureStatusesConfig)
						.success(function(data){
							if (data.returnMessage.code > 0) {
						    	$scope.manufactureStatuses = data.enumValues;
						    	$scope.manufactureStatus = data.enumValues[0].key;
							} else {
								$scope.manufactureStatuses = [{key: 0, value: 'Erro ao carregar!'}];
								$scope.manufactureStatus = 0;
							}
						})
						.error(function(data){
							NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os status de produção!');
						});					
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados!');
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
			    	$scope.loadDeliveryRequests();
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