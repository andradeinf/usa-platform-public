angular.module('franchisorLogistics').controller('ProductsCtrl', function ($scope, httpSvc) {

	$scope.toggleCategory = function (category) {

		category.open = !category.open;
		
		if (!category.productsLoaded) {
			
			LoadingUtil.show($('#products-loadingArea'));
			
			//retrieve products data for category
			var productConfig = {
					  method: 'GET',
					  url: '/ws/products/supplier/'+gSupplierId+'/category/'+category.id,
					  unique: false,
					  requestId: 'products-load'
					};
			
			httpSvc(productConfig)
				.success(function(data){
					category.productsLoaded = true;
					
					data.productList.forEach(function(entry, index, array) {
					    initializeItem(entry);
					});
					category.products = data.productList;
					
					LoadingUtil.hide($('#products-loadingArea'));
					
				});
		}
	}
	
	$scope.editPrice = function(item){
		item.editPrice = true;
	}
	
	$scope.cancelEditPrice = function(item){
		initializeUpdatePrice(item);
	}
	
	$scope.saveEditPrice = function(item){
		
		//validate product size price update
    	var validationResult = true;
		var errorMsg = "";
		
		if (item.updateRequest.unitPrice == item.selectedSize.unitPrice) {
			validationResult = false;
			errorMsg += "<li>O 'Novo Preço' deve ser diferente to preço atual.</li>";
		}
		
		if (!validationResult) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
			return;
		} 
		
		var config = {
				  method: 'PUT',
				  url: '/ws/products/priceUpdate',
				  data: item.updateRequest,
				  unique: true,
				  requestId: 'productSize-priceUpdate'
				};
  	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					var index = item.sizes.indexOf(item.selectedSize);
					item.sizes[index] = data.productSize;
					item.selectedSize = item.sizes[index];
					initializeUpdatePrice(item);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados salvos com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados!');
				}
			});	
		
	}
	
	$scope.openDeliveryHistory = function(item){
    	
    	$scope.historyItem = item;
    	$scope.deliveryRequests = [];
    	$scope.deliveryRequestsCursorString = 'none';
    	$scope.deliveryNoRecordsFound = false;
    	$scope.manufactureRequests = [];
    	$scope.manufactureRequestsCursorString = 'none';
    	$scope.manufactureNoRecordsFound = false;
    	$scope.productSizePriceHistories = [];
    	$scope.priceHistoryCursorString = 'none';
    	$scope.priceHistoryNoRecordsFound = false;
    	$('.nav-tabs a[href="#deliveryRequests"]').tab('show')
    	
    	$scope.loadDeliveryHistory(item);
    }
    
    $scope.loadDeliveryHistory = function(item){
    	
    	LoadingUtil.show($('#history-loadingArea'));    	
    	
    	var config = {
			  method: 'GET' ,
			  url: '/ws/delivery/product/'+item.id+'/paged/'+$scope.deliveryRequestsCursorString,
			  unique: true,
			  requestId: 'deliveryRequest-loadHistory'
			};
    	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					LoadingUtil.hide($('#history-loadingArea'));
					
					if (data.deliveryRequests.length == 0) {
						if ($scope.deliveryRequestsCursorString == 'none') {
							$scope.deliveryNoRecordsFound = true;
						} else {
							NotificationUtil.show($('#notificationArea'), 'warning', 'Atenção!', 'Não há mais histórico.');
						}
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
    	
    	LoadingUtil.show($('#history-loadingArea'));
    	
    	var config = {
			  method: 'GET' ,
			  url: '/ws/manufacture/product/'+item.id+'/paged/'+$scope.manufactureRequestsCursorString,
			  unique: true,
			  requestId: 'maunfactureRequest-loadHistory'
			};
    	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					LoadingUtil.hide($('#history-loadingArea'));
					
					if (data.manufactureRequests.length == 0) {
						if ($scope.manufactureRequestsCursorString == 'none') {
							$scope.manufactureNoRecordsFound = true;
						} else {
							NotificationUtil.show($('#notificationArea'), 'warning', 'Atenção!', 'Não há mais histórico.');
						}
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
    
    $scope.openPriceHistory = function(item){
    	if ($scope.priceHistoryCursorString == 'none') {
    		$scope.loadPriceHistory(item);
    	}
    }
    
    $scope.loadPriceHistory = function(item){
    	
    	LoadingUtil.show($('#history-loadingArea'));    	
    	
    	var config = {
			  method: 'GET' ,
			  url: '/ws/products/'+item.id+'/priceHistory/paged/'+$scope.priceHistoryCursorString,
			  unique: true,
			  requestId: 'priceHistory-loadHistory'
			};
    	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					LoadingUtil.hide($('#history-loadingArea'));
					
					if (data.productSizePriceHistories.length == 0) {
						if ($scope.priceHistoryCursorString == 'none') {
							$scope.priceHistoryNoRecordsFound = true;
						} else {
							NotificationUtil.show($('#notificationArea'), 'warning', 'Atenção!', 'Não há mais histórico.');
						}
					}
					
					$scope.priceHistoryCursorString = data.cursorString;
					
					data.productSizePriceHistories.forEach(function(entry) {
						$scope.productSizePriceHistories.push(entry);
						
						entry.user = data.users[entry.userId];
						entry.productSize = data.productSizes[entry.productSizeId];
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
    
    $scope.selectDescription = function(item, size) {
    	var returnString = "";
    	if (!item.groupSizes && size.groupName != undefined && size.groupName != '') {
    		returnString = returnString + size.groupName + ' - ';
    	}
    	returnString = returnString + size.name;
    	if (size.description != undefined && size.description != '') {
    		returnString = returnString + ' - ' + size.description;
    	}
    	return returnString;
    }
    
    $scope.selectedSizeClass = function(item, size){
    	var className = '';
    	if (size == item.selectedSize) {className = 'other-sizes-list-item-selected'}
    	return className;
    };
    
    $scope.setSelectedSize = function(item, size) {
    	item.selectedSize = size;
    	initializeUpdatePrice(item);
    }
    
    $scope.toggleIsAvailable = function(item) {
    	
    	var availabilityUpdate = {
    			productSizeId: item.selectedSize.id,
    			isAvailable: !item.selectedSize.isAvailable
    	}
    	
    	var config = {
				  method: 'PUT',
				  url: '/ws/products/availabilityUpdate',
				  data: availabilityUpdate,
				  unique: true,
				  requestId: 'productSize-availabilityUpdate'
				};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					item.selectedSize.isAvailable = data.productSize.isAvailable;
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados salvos com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados!');
				}
			});	
    }
    
    var initializeItem = function(item) {
    	$scope.setSelectedSize(item, item.sizes[0]);
	    if (item.imageURL == null) {
	    	item.thumbnail = '/img/not-available.jpg';
		} else {
			item.thumbnail = item.imageURL + '=s150';
		}
	    item.calculateManufactureMinTotalQty = function() {return item.selectedSize.deliveryQty * item.selectedSize.manufactureMinQty}
    }
    
    var initializeUpdatePrice = function(item) {
	    if (item.type != 'CATALOG') {
	    	item.editPrice = false;
		    item.updateRequest = {productSizeId: item.selectedSize.id,
                    			  unitPrice: item.selectedSize.unitPrice};
	    }            
    }
    
	var init = function() {

		$scope.supplierList = [];
		
		//product categories
		$scope.productCategories = [];
		
		//get product categories
		var productCategoriesConfig = {
			  method: 'GET',
			  url: '/ws/products/categories/supplier/'+gSupplierId,
			  unique: false,
			  requestId: 'productCategories-load'
			};
		
		httpSvc(productCategoriesConfig)
			.success(function(data){
				$scope.productCategories = data.productCategoryList;
				
				$scope.productCategories.forEach(function(entry, index, array) {
					entry.open = false;
					entry.productsLoaded = false;
				});
			});
	
		$('[data-toggle="tooltip"]').tooltip(); 
		
	}
	
	init();
	
});