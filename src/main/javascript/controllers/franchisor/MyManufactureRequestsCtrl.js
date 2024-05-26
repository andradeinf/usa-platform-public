angular.module('franchisorLogistics').controller('MyManufactureRequestsCtrl', function ($scope, httpSvc) {

    $scope.statusClass = function(status){
    	var className = 'label-warning';
    	if (status == 'PENDING') {className = 'label-info';}
    	if (status == 'COMPLETED') {className = 'label-success';}
    	if (status == 'CANCELLED') {className = 'label-danger';}
    	if (status == 'ADJUSTMENT') {className = 'label-danger';}
    	return className;
    };
    
    $scope.fetchManufactureRequests = function() {
    	
    	LoadingUtil.show($("#manufactureLoad"));
    	
    	//get list of manufacture requests
		var manufactureRequestsConfig = {
				  method: 'GET' ,
				  url: '/ws/manufacture/franchisor/'+gFranchisorId+'/paged/'+$scope.cursorString,
				  unique: true,
				  requestId: 'manufactureRequests-load'
				};
		
		httpSvc(manufactureRequestsConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					LoadingUtil.hide($("#manufactureLoad"));
					
					if ($scope.cursorString != 'none' && data.manufactureRequests.length == 0) {
						NotificationUtil.show($('#notificationArea'), 'warning', 'Atenção!', 'Não há mais pedidos de produção.');
					}
					
					$scope.cursorString = data.cursorString
					
					data.manufactureRequests.forEach(function(entry) {
						entry.product = data.products[entry.productId];
						entry.product.sizes.forEach(function(size) {
							if (size.id == entry.productSizeId) {
								entry.productSize = size;
							}
						});	
					    entry.calculateTotal = function() {return entry.quantity * entry.manufactureUnitPrice};
					    $scope.manufactureRequests.push(entry);
					});
					
					$('#manufactureRequests-overlay').remove();
					$('#manufactureRequests-loading-img').remove();
					
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os pedidos de produção!');
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os pedidos de produção!');
				}
			});
    }
    
	var init = function() {
		
		$scope.cursorString = 'none';
		$scope.manufactureRequests = [];
		
		$scope.fetchManufactureRequests();	
	}
	
	init();
	
});