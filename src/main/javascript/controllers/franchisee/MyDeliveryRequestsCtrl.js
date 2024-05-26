angular.module('franchisorLogistics').controller('MyDeliveryRequestsCtrl', function ($scope, httpSvc) {
    
	var errorClass = 'has-error';
	
	$scope.cancelDeliveryRequest = function(item) {
    	$scope.deliveryRequest = item;
    	$scope.updateRequest = {
                  status: 'CANCELLED'
                , cancellationComment: '' 
                , deliveryRequests: [
                	{ deliveryRequestId: item.id
	                , quantity: 0
	                , deliveryUnitPrice: 0
					}
                ]
  			}
	}
	
	$scope.showDeliveryRequestHistory = function(item) {
    	$scope.deliveryRequest = item;
    }
    
    $scope.confirmCancellation = function() {
    	
    	if (validate()) {
    		
    		var config = {
		  			  method: 'PUT',
		  			  url: '/ws/delivery/status',
		  			  data: $scope.updateRequest,
		  			  unique: true,
		  			  requestId: 'delivery-statusUpdate'
		  			};
			
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						var index = $scope.deliveryRequests.indexOf($scope.deliveryRequest);
						var deliveryRequestResponse = data.deliveryRequests[0];
						
						deliveryRequestResponse.franchisee = $scope.deliveryRequests[index].franchisee;
						deliveryRequestResponse.franchisor = $scope.deliveryRequests[index].franchisor;
						deliveryRequestResponse.product = $scope.deliveryRequests[index].product;
						deliveryRequestResponse.productSize = $scope.deliveryRequests[index].productSize;
						deliveryRequestResponse.supplier = $scope.deliveryRequests[index].supplier;
						deliveryRequestResponse.calculateTotal = function() {return deliveryRequestResponse.quantity * deliveryRequestResponse.deliveryUnitPrice};
						$scope.deliveryRequests[index] = deliveryRequestResponse;
						
						$('#cancelModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Solicitação de entrega cancelada com sucesso!');		
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Não foi possível cancelar a solicitação de entrega: ' + data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao cancelar a solicitação de entrega!');
					}
				});	  
    	}
    }
    
    var resetValidation = function() {
		$('#notificationArea').html("");
		$('#cancellationComment').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.updateRequest.cancellationComment == "") {
			$('#cancellationComment').addClass(errorClass);
			result = false;
			errorMsg += "<li>O motivo do cancelamento não pode ser vazio</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
    
    $scope.fetchDeliveryRequests = function() {
    	
    	//get list of delivery requests
		var deliveryRequestsConfig = {
				  method: 'GET' ,
				  url: '/ws/delivery/franchisee/'+gFranchiseeId+'/paged/'+$scope.cursorString,
				  unique: false,
				  requestId: 'deliveryRequests-load'
				};
		
		httpSvc(deliveryRequestsConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					if ($scope.cursorString != 'none' && data.deliveryRequests.length == 0) {
						NotificationUtil.show($('#notificationArea'), 'warning', 'Atenção!', 'Não há mais pedidos de entrega.');
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
			
					$('#deliveryRequests-overlay').remove();
					$('#deliveryRequests-loading-img').remove();
					
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
    
	var init = function() {
		
		$scope.cursorString = 'none';
		$scope.deliveryRequests = [];
		
		$scope.fetchDeliveryRequests();
	
	}
	
	init();
	
});