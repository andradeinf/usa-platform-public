angular.module('franchisorLogistics').controller('StockCtrl', function ($scope, $routeParams, httpSvc) {
	
    $scope.openDeliveryHistory = function(item){
    	
    	$scope.historyItem = item;
    	$scope.deliveryRequests = [];
    	$scope.deliveryRequestsCursorString = 'none';
    	$scope.manufactureRequests = [];
    	$scope.manufactureRequestsCursorString = 'none';
    	$('.nav-tabs a[href="#deliveryRequests"]').tab('show')
    	
    	$scope.loadDeliveryHistory(item);
    }
    
    $scope.loadDeliveryHistory = function(item){
    	
    	LoadingUtil.show($('#history-loadingArea'));    	
    	
    	var config = {
			  method: 'GET' ,
			  url: '/ws/delivery/product/'+item.id+'/paged/'+$scope.deliveryRequestsCursorString,
			  unique: false,
			  requestId: 'deliveryRequest-loadHistory'
			};
    	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					if ($scope.deliveryRequestsCursorString != 'none' && data.deliveryRequests.length == 0) {
						NotificationUtil.show($('#notificationArea'), 'warning', 'Atenção!', 'Não há mais histórico.');
					}
					
					$scope.deliveryRequestsCursorString = data.cursorString;
					
					data.deliveryRequests.forEach(function(entry) {
						$scope.deliveryRequests.push(entry);
						
						entry.franchisee = data.franchisees[entry.franchiseeId];
						entry.product = data.products[entry.productId];
						entry.product.sizes.forEach(function(size) {
							if (size.id == entry.productSizeId) {
								entry.productSize = size;
							}
						});	
						entry.calculateTotal = function() {return entry.quantity * entry.deliveryUnitPrice};
					});	
					
					LoadingUtil.hide($('#history-loadingArea'));
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar o histórico!');
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar o histórico!');
				}
			});
    }
    
    $scope.openManufactureHistory = function(item){
    	if ($scope.manufactureRequestsCursorString == 'none') {
    		$scope.loadManufactureHistory(item);
    	}    	
    }
    
    $scope.loadManufactureHistory = function(item){
    	
    	var config = {
			  method: 'GET' ,
			  url: '/ws/manufacture/product/'+item.id+'/paged/'+$scope.manufactureRequestsCursorString,
			  unique: false,
			  requestId: 'maunfactureRequest-loadHistory'
			};
    	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					if ($scope.manufactureRequestsCursorString != 'none' && data.manufactureRequests.length == 0) {
						NotificationUtil.show($('#notificationArea'), 'warning', 'Atenção!', 'Não há mais histórico.');
					}
					
					$scope.manufactureRequestsCursorString = data.cursorString;
					
					data.manufactureRequests.forEach(function(entry) {
						$scope.manufactureRequests.push(entry);
						
						entry.product = data.products[entry.productId];
						entry.product.sizes.forEach(function(size) {
							if (size.id == entry.productSizeId) {
								entry.productSize = size;
							}
						});	
						entry.calculateTotal = function() {return entry.quantity * entry.manufactureUnitPrice};
					});	
					
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar o histórico!');
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar o histórico!');
				}
			});
    }
    
    $scope.stockClass = function(item, size){
    	var className = 'text-success';
    	if (size.currentStock <= size.minStock) {
    		if (!item.allowNegativeStock) {
    			className = 'text-danger'
    		} else {
    			className = 'text-warning'
    		}
    	}
    	return className;
    };
    
    $scope.calculateManufactureMinTotalQty = function(item, size) {
		if (item.hasDeliveryUnit) {
			return size.deliveryQty * size.manufactureMinQty;
		} else {
			return size.manufactureMinQty;
		}		
	}
    
    var initializeItem = function(item) {
	    if (item.imageURL == null) {
	    	item.thumbnail = '/img/not-available.jpg';
		} else {
			item.thumbnail = item.imageURL + '=s150';
		}
	    item.manufactureSizeList = [{id: 0, name: ' Selecione...'}];
	    item.sizes.forEach(function(size, index, array) {
    		item.manufactureSizeList.push(size);
		});
    	initializeManufacture(item);
    }
    
    var initializeManufacture = function(item) {
    	
    	item.requestMode = false;
    	item.manufactureRequest = { manufactureQty: 0
    							  , operation_sign: 1
    							  , selectedSize: null
    							  , data: { franchisorId: item.franchisorId
    	                        		  , productId: item.id
    	                        		  , productSizeId: 0
		                                  , quantity: 0
		                                  , addStockOnly: true} 
    							  };
    	if (item.sizes.length == 1) {
    		item.manufactureRequest.data.productSizeId = item.sizes[0].id;
    		item.manufactureRequest.selectedSize = item.sizes[0];
    	}
    	item.manufactureRequest.calculateManufactureQtyTotal = function() {
    		if (item.manufactureRequest.selectedSize == null) return 0;
    		if (item.hasDeliveryUnit) {
    			return item.manufactureRequest.manufactureQty * item.manufactureRequest.selectedSize.deliveryQty;
    		} else {
    			return item.manufactureRequest.manufactureQty;
    		}    		
    	};
    	item.manufactureRequest.calculateManufactureRequestTotal = function() {
    		if (item.manufactureRequest.selectedSize == null) {
    			return 0;
    		} else {
    			return item.manufactureRequest.calculateManufactureQtyTotal() * item.manufactureRequest.selectedSize.unitPrice;
    		}    		
    	}
    }
    
    $scope.setSelectedSize = function(item){
    	item.sizes.forEach(function(size, index, array) {
    		if (size.id == item.manufactureRequest.data.productSizeId) {
    			item.manufactureRequest.selectedSize = size;
    		}
		});
    }
    
    $scope.createManufactureRequest = function(item){
		item.requestMode = true;
    }
    
    $scope.createNegativeManufactureRequest = function(item){
		item.requestMode = true;
		item.manufactureRequest.operation_sign = -1;
    }
    
    $scope.cancelManufactureRequest = function(item){
		initializeManufacture(item);
    }
    
    $scope.saveManufactureRequest = function(item){
		//validate manufacture request
    	var validationResult = true;
		var errorMsg = "";
		
		if (item.manufactureRequest.data.productSizeId == 0) {
			validationResult = false;
			errorMsg += "<li>Selecione o " + item.sizeName + "</li>";
		} 
		
		if (item.manufactureRequest.manufactureQty <= 0) {
			validationResult = false;
			errorMsg += "<li>A quantidade deve ser maior que zero</li>";
		}
		
		if (item.manufactureRequest.selectedSize != null) {
			if (item.manufactureRequest.manufactureQty < item.manufactureRequest.selectedSize.manufactureMinQty && item.manufactureRequest.operation_sign > 0) {
				validationResult = false;
				errorMsg += "<li>A quantidade deve ser maior ou igual à 'Qde Min. Produção'</li>";
			}
		}
		
		if (!validationResult) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
			return;
		}
		
		item.manufactureRequest.data.quantity = item.manufactureRequest.calculateManufactureQtyTotal() * item.manufactureRequest.operation_sign;
		
		var config = {
				  method: 'POST',
				  url: '/ws/manufacture',
				  data: item.manufactureRequest.data,
				  unique: true,
				  requestId: 'manufactureRequest-create'
				};
    	
		httpSvc(config)
			.success(function(data){
				initializeManufacture(item);
				item.sizes.forEach(function(size, index, array) {
		    		if (size.id == data.productSizeId) {
		    			size.currentStock = size.currentStock + data.quantity;
		    		}
				});
				NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Produção adicionada com sucesso!');
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados de produção!');
				}
			});	            		
    }
	
	$scope.loadProducts = function(){
		
		$scope.productFilter = {
			type: 'WITH_STOCK_CONTROL'
		}
		
		if ($scope.franchisorId == 0) {
			$scope.productsList = [];
			$scope.supplierList = [];
			return;
		}
		
		LoadingUtil.show($('#productLoad'));
		
		var config = {
			  method: 'GET',
			  url: '/ws/products/supplier/'+$scope.supplierId+'/franchisor/'+$scope.franchisorId,
			  unique: false,
			  requestId: 'products-load'
			};
		
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					data.productList.forEach(function(entry, index, array) {
					    initializeItem(entry);
					});
					$scope.productsList = data.productList;
					$scope.supplierList = data.supplierList;
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados!');
				}
				LoadingUtil.hide($('#productLoad'));
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados!');
				LoadingUtil.hide($('#productLoad'));
			});
    }
	
	var init = function() {
	
		if ($routeParams.supplierId) {
			$scope.supplierId = $routeParams.supplierId;
		} else {
			$scope.supplierId = gSupplierId;
		}		
		$scope.productsList = [];
		$scope.supplierList = [];
		$scope.multipleFranchisors = false;
		
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
					$scope.loadProducts();
				} else {
					$scope.multipleFranchisors = true;
					$scope.franchisors = data.franchisors;
					$scope.franchisors.unshift({"id":0,"name":" Selecione ..."})
				}				
			});
	
	}
	
	init();
	
});