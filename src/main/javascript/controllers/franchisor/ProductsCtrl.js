angular.module('franchisorLogistics').controller('ProductsCtrl', function ($scope, httpSvc) {

	$scope.toggleCategory = function (category) {

		category.open = !category.open;
		
		if (!category.productsLoaded) {
			
			LoadingUtil.show($('#products-loadingArea'));
			
			//retrieve products data for category
			var productConfig = {
					  method: 'GET',
					  url: '/ws/products/franchisor/'+gFranchisorId+'/category/'+category.id,
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
					
					for (var key in data.supplierList) {
						$scope.supplierList[key] = data.supplierList[key];
					}
					
					LoadingUtil.hide($('#products-loadingArea'));
					
				});
		}
	}
	
	$scope.editItem = function(item){
    		item.editMode = true;
    }
    
    $scope.cancelEditItem = function(item){
    		initializeUpdateProduct(item);
    }
    
    $scope.saveEditItem = function(item){
		//validate product update
    	var validationResult = true;
		var errorMsg = "";
		
		if (item.updateRequest.minStock == null || item.updateRequest.minStock == "") {
			validationResult = false;
			errorMsg += "<li>O 'Estoque Mínimo' não pode ser vazio</li>";
		}
		
		if (!validationResult) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
			return;
		} 
		
		var config = {
				  method: 'PUT',
				  url: '/ws/products/franchisorUpdate',
				  data: item.updateRequest,
				  unique: true,
				  requestId: 'productSize-franchisorUpdate'
				};
    	
		httpSvc(config)
			.success(function(data){
				var index = item.sizes.indexOf(item.selectedSize);
				item.sizes[index] = data;
				item.selectedSize = item.sizes[index];
				initializeUpdateProduct(item);
				NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados salvos com sucesso!');
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados!');
				}
			});	            		
    }
    
    $scope.createManufactureRequest = function(item){
    		item.requestMode = true;
    }
    
    $scope.cancelManufactureRequest = function(item){
    		initializeManufacture(item);
    }
                
    $scope.saveManufactureRequest = function(item){
		//validate manufacture request
    	var validationResult = true;
		var errorMsg = "";
		
		if (item.manufactureRequest.manufactureQty <= 0) {
			validationResult = false;
			errorMsg += "<li>A 'Quantidade à produzir' deve ser maior que zero</li>";
		}
		
		if (item.manufactureRequest.manufactureQty < item.selectedSize.manufactureMinQty) {
			validationResult = false;
			errorMsg += "<li>A 'Quantidade à produzir' deve ser maior ou igual à 'Qde Min. Produção'</li>";
		}
		
		if (!validationResult) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
			return;
		}
		
		item.manufactureRequest.data.quantity = item.manufactureRequest.calculateManufactureQtyTotal();
		
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
				NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Solicitação de produção enviada com sucesso!');
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar a solicitação de produção!');
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
    
    $scope.getStockMessage = function(item){
    	if (item.allowNegativeStock) {
    		return "Este item permite solicitação mesmo sem estoque disponível"
    	} else {
    		return "Este item não permite solicitação sem estoque disponível"
    	}
    };
    
    $scope.stockClass = function(item){
    	var className = 'text-success';
    	if (item.selectedSize.currentStock <= item.selectedSize.minStock) {
    		if (!item.allowNegativeStock) {
    			className = 'text-danger'
    		} else {
    			className = 'text-warning'
    		}
    	}
    	return className;
    };
    
    $scope.selectedSizeClass = function(item, size){
    	var className = '';
    	if (size == item.selectedSize) {className = 'other-sizes-list-item-selected'}
    	return className;
    };
    
    $scope.setSelectedSize = function(item, size) {
    	item.selectedSize = size;
    	initializeUpdateProduct(item);
    	initializeManufacture(item);
    }
    
    $scope.getRestrictionList = function(restrictions){
		var text = "";
		if (restrictions) {
			restrictions.forEach(function(entry, index, array) {
				var restrictionData = entry.split("-");
				text += $scope.stateList[restrictionData[0]].code + " - ";
				if (restrictionData[1] == 0) {
					text += "TODAS; ";
				} else {
					text += $scope.cityList[restrictionData[1]].name + "; ";
				}			
			});
		}		
		return text;
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
    
    var initializeUpdateProduct = function(item) {
	    if (item.type != 'CATALOG') {
	    	item.editMode = false;
		    item.updateRequest = {productSizeId: item.selectedSize.id,
                    			  minStock: item.selectedSize.minStock};
	    }            
    }
    
    var initializeManufacture = function(item) {
    	if (item.type != 'CATALOG') {    	
	    	if (!item.hasManufactureMinQty) {item.selectedSize.manufactureMinQty = 0};
	    	
	    	item.requestMode = false;
	    	item.manufactureRequest = { manufactureQty: item.selectedSize.manufactureMinQty
	    							  , data: { franchisorId: item.franchisorId
	    	                        		  , productId: item.id
	    	                        		  , productSizeId: item.selectedSize.id
			                                  , quantity: 0
			                                  , addStockOnly: false} 
	    							  };
	    	item.manufactureRequest.calculateManufactureQtyTotal = function() {
	    		if (item.hasDeliveryUnit) {
	    			return item.manufactureRequest.manufactureQty * item.selectedSize.deliveryQty;
	    		} else {
	    			return item.manufactureRequest.manufactureQty;
	    		}    		
	    	};
	    	item.manufactureRequest.calculateManufactureRequestTotal = function() {
	    		return item.manufactureRequest.calculateManufactureQtyTotal() * item.selectedSize.unitPrice
	    	}
    	}
    }
    
	var init = function() {

		$scope.supplierList = [];
		
		//product categories
		$scope.productCategories = [];
		
		//get product categories
		var productCategoriesConfig = {
			  method: 'GET',
			  url: '/ws/products/categories/franchisor/'+gFranchisorId,
			  unique: false,
			  requestId: 'productCategories-load'
			};
		
		httpSvc(productCategoriesConfig)
			.success(function(data){
				$scope.productCategories = data.productCategoryList;
				$scope.stateList = data.states;
				$scope.cityList = data.cities;
				
				$scope.productCategories.forEach(function(entry, index, array) {
					entry.open = false;
					entry.productsLoaded = false;
				});
			});
	
		$('[data-toggle="tooltip"]').tooltip(); 
		
	}
	
	init();
	
});