angular.module('franchisorLogistics').controller('ProductsCtrl', function ($scope, httpSvc) {

	$scope.toggleCategory = function (category) {
		
		if (category.open) {
			category.open = false;
		} else {
			$scope.productCategories.forEach(function(entry, index, array) {
				entry.open = false;
			});
			category.open = true;
		}
		
		if (!category.productsLoaded) {
			
			LoadingUtil.show($('#products-loadingArea'));
			
			if (category.id == 0) {
				//retrieve logged user favorite products
				var productConfig = {
						  method: 'GET',
						  url: '/ws/products/favorites',
						  unique: false,
						  requestId: 'favorites-load'
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
			} else {
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
	}
	
	$scope.addFavorite = function(item){
		
		var config = {
				  method: 'PUT',
				  url: '/ws/products/favorites/'+item.id,
				  unique: true,
				  requestId: 'product-addFavorite'
				};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					initializeItem(data.product);
					$scope.favorites.products.push(data.product);
					item.favoriteFranchiseeUserIds = data.product.favoriteFranchiseeUserIds
					
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Favorito adicionado com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao adicionar favorito!');
				}
			});	
	}
	
	$scope.removeFavorite = function(item, favorite){
		
		var config = {
				  method: 'DELETE',
				  url: '/ws/products/favorites/'+item.id,
				  unique: true,
				  requestId: 'product-removeFavorite'
				};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					$scope.favorites.products = $scope.favorites.products.filter(function(product, index, arr){
					    return product.id != item.id;
					});
					
					if (favorite) {
						$scope.productCategories.forEach(function(category, index, array) {
							if (category.productsLoaded){
								category.products.forEach(function(product, index, array) {
								    if (product.id == item.id) {
								    	product.favoriteFranchiseeUserIds = [];
								    }
								});
							}
						});
					} else {
						item.favoriteFranchiseeUserIds = [];
					}
					
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Favoritos removido com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao remover favorito!');
				}
			});	
	}
	
	$scope.createDeliveryRequest = function(item){
    	//Check product stock
    	var validationResult = true;
		var errorMsg = "";
		
		if (item.type == 'WITH_STOCK_CONTROL' && !item.allowNegativeStock && item.selectedSize.currentStock <= 0) {
			validationResult = false;
			errorMsg += "<li>Item não disponível no estoque.</li>";
		}
		
		if (!validationResult) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
			return;
		}
    	
    	item.requestMode = true;
    }
    
    $scope.cancelDeliveryRequest = function(item){
    		initializeDelivery(item);
    }
                
    $scope.saveDeliveryRequest = function(item){
    	//Check delivery request
    	var validationResult = true;
		var errorMsg = "";
		
		if (item.deliveryRequest.deliveryQty <= 0) {
			validationResult = false;
			errorMsg += "<li>A quantidade solicitada deve ser maior do que zero.</li>";
		}
		
		if (item.type == 'WITH_STOCK_CONTROL' && !item.allowNegativeStock && item.deliveryRequest.calculateDeliveryQtyTotal() > item.selectedSize.currentStock) {
			validationResult = false;
			errorMsg += "<li>A quantidade solicitada não pode ser maior que a quantidade em estoque.</li>";
		}
		
		if (!validationResult) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
			return;
		}
		
		item.deliveryRequest.data.quantity = item.deliveryRequest.calculateDeliveryQtyTotal();
		
		var config = {
			  method: 'POST',
			  url: '/ws/delivery',
			  data: item.deliveryRequest.data,
			  unique: true,
			  requestId: 'deliveryRequest-create'
			};
    	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					item.selectedSize.currentStock -= data.deliveryRequest.quantity;
					initializeDelivery(item);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Solicitação de entrega enviada com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Não foi possível criar a solicitação de entrega: ' + data.returnMessage.message);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar a solicitação de entrega!');
				}
			});	            		
    }
    
    $scope.openHistory = function(item){
    	
    	$scope.historyItem = item;
    	$scope.deliveryRequests = [];
    	$scope.cursorString = 'none';
    	
    	$scope.loadHistory(item);
    }
    
    $scope.loadHistory = function(item){
    	
    	LoadingUtil.show($('#history-loadingArea'));    	
    	
    	var config = {
			  method: 'GET' ,
			  url: '/ws/delivery/product/'+item.id+'/franchisee/'+gFranchiseeId+'/paged/'+$scope.cursorString,
			  unique: false,
			  requestId: 'deliveryRequest-loadHistory'
			};
    	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					if ($scope.cursorString != 'none' && data.deliveryRequests.length == 0) {
						NotificationUtil.show($('#notificationArea'), 'warning', 'Atenção!', 'Não há mais histórico.');
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
    	initializeDelivery(item);
    }
    
    var initializeItem = function(item) {
    	$scope.setSelectedSize(item, item.sizes[0]);
		if (item.imageURL == null) {
			item.thumbnail = '/img/not-available.jpg';
		} else {
			item.thumbnail = item.imageURL + '=s150';
		}
		if (item.favoriteFranchiseeUserIds) {
			item.favoriteFranchiseeUserIds = item.favoriteFranchiseeUserIds.filter(id => id == gFranchiseeUserId);
		} else {
			item.favoriteFranchiseeUserIds = [];
		}		
    }
    
    var initializeDelivery = function(item) {
    	if (item.type != 'CATALOG') {
		    item.requestMode = false;
		    item.deliveryRequest = { deliveryQty: 0
		    			    	   , data: {franchiseeId: gFranchiseeId
			                               , productId: item.id
			                               , productSizeId: item.selectedSize.id
			                               , quantity: "0"}
		    					   }
		    item.deliveryRequest.calculateDeliveryQtyTotal = function() {
		    	if (item.hasDeliveryUnit) {
		    		return item.deliveryRequest.deliveryQty * item.selectedSize.deliveryQty
		    	} else {
		    		return item.deliveryRequest.deliveryQty
		    	}	    	
		    };
		    item.deliveryRequest.calculateDeliveryRequestTotal = function() {
		    	return item.deliveryRequest.calculateDeliveryQtyTotal() * item.selectedSize.unitPrice
		    }
    	}
    }
	
	var init = function() {

		$scope.supplierList = [];
		
		//product categories
		$scope.productCategories = [];
		$scope.favorites = {
				franchisorId: gFranchisorId,
				id: 0,
				name: "Meus Favoritos",
				notes: "",
				order: 0,
				restrictions: []
			};
		
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
				$scope.productCategories.push($scope.favorites);
				
				$scope.productCategories.forEach(function(entry, index, array) {
					entry.open = false;
					entry.productsLoaded = false;
				});
				
				$scope.toggleCategory($scope.favorites);
				
			});
	
		$('[data-toggle="tooltip"]').tooltip(); 
	}
	
	init();
	
});