angular.module('franchisorLogistics').controller('ProductsCtrl', function ($scope, $rootScope, $routeParams, httpSvc) {
	
	var errorClass = 'has-error';

	$scope.new = function(){
		reset();
	}
	
	$scope.newSize = function(item){
		$scope.product = item;
		resetSize();
	}
	
	$scope.duplicate = function(item){
		$scope.product = { id: 0
		         , supplierId: item.supplierId
		         , productCategoryId: item.productCategoryId
				 , franchisorId: item.franchisorId
				 , name: item.name + " (Cópia)"
				 , description: item.description
				 , unit: item.unit
				 , hasDeliveryUnit: item.hasDeliveryUnit
				 , deliveryUnit: item.deliveryUnit
				 , hasManufactureMinQty: item.hasManufactureMinQty
				 , manufactureTime: item.manufactureTime
				 , deliveryTime: item.deliveryTime
				 , sizeName: item.sizeName
				 , allowNegativeStock: item.allowNegativeStock
				 , allowChangeUnitPrice: item.allowChangeUnitPrice
				 , allowSupplierChangeUnitPrice: item.allowSupplierChangeUnitPrice
				 , groupSizes: item.groupSizes
				 , duplicateId: item.id
				 , type: item.type}
	}
	
	$scope.save = function(){
		if (validate()) {
			
			var config = {
					  method: ($scope.product.id == 0 ? 'POST' : 'PUT') ,
					  url: '/ws/products',
					  data: $scope.product,
					  unique: true,
					  requestId: 'products-createOrUpdate'
					};
					
			httpSvc(config)
				.success(function(data){
					if ($scope.product.id == 0) {
						$scope.productCategoriesMap[$scope.product.productCategoryId].products.push(data);
					}
					$('#newProductModal').modal('hide');
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados salvos com sucesso!');
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados!');
					}
				});				
		};
	}
	
	$scope.saveSize = function(){
		if (validateSize()) {
			
			var config = {
					  method: ($scope.productSize.id == 0 ? 'POST' : 'PUT') ,
					  url: '/ws/products/size',
					  data: $scope.productSize,
					  unique: true,
					  requestId: 'productSize-createOrUpdate'
					};
					
			httpSvc(config)
				.success(function(data){
					if ($scope.productSize.id == 0) {
						if ($scope.product.sizes == null) {
							$scope.product.sizes = [];
						}
						$scope.product.sizes.push(data);
					}
					$('#newProductSizeModal').modal('hide');
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados da opção salvos com sucesso!');
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados da opção!');
					}
				});			
		};
	}
	
	$scope.edit = function(item){
		$scope.product = item;
	}
	
	$scope.editSize = function(item, size){
		$scope.product = item;
		$scope.productSize = size;
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir? Todos os dados de produção e entrega associados serão apagados.', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){
			
		var config = {
					method: 'DELETE' ,
					url: '/ws/products/'+$scope.itemToRemove.id,
					unique: true,
					requestId: 'products-delete'
				};
		
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.productCategoriesMap[$scope.itemToRemove.productCategoryId].products = $scope.productCategoriesMap[$scope.itemToRemove.productCategoryId].products.filter(product => product.id != $scope.itemToRemove.id);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Excluído com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir!');
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir!');
				}
			});
	}

	$scope.deleteSize = function(item, size){
		$scope.itemToRemove = item;
		$scope.sizeToRemove = size;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir a opção? Todos os dados de produção e entrega associados serão apagados.', doDeleteSize, 'Não', 'Sim');
	}
		
	var doDeleteSize = function(){

		var config = {
					method: 'DELETE' ,
					url: '/ws/products/size/'+$scope.sizeToRemove.id,
					unique: true,
					requestId: 'productSize-delete'
				};
		
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.itemToRemove.sizes = $scope.itemToRemove.sizes.filter(size => size.id != $scope.sizeToRemove.id);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Opção excluída com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir a opção!');
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir a opção!');
				}
			});
	}
	
	$scope.changeHasDeliveryUnit = function(){
		if (!$scope.product.hasDeliveryUnit) {
			$scope.product.deliveryUnit = "";
		}
	}
	
	$scope.getImage = function(item)  {
		if (item.imageURL == null) {
			return '/img/not-available.jpg';
		} else {
			return item.imageURL + '=s150';
		}
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#productCategory').removeClass(errorClass);
		$('#supplier').removeClass(errorClass);
		$('#name').removeClass(errorClass);
		$('#description').removeClass(errorClass);
		$('#type').removeClass(errorClass);
		$('#unit').removeClass(errorClass);
		$('#deliveryUnit').removeClass(errorClass);
		$('#manufactureTime').removeClass(errorClass);
		$('#deliveryTime').removeClass(errorClass);
		$('#sizeName').removeClass(errorClass);
		
	}
	
	var resetSizeValidation = function() {
		$('#notificationArea').html("");
		$('#sizeName2').removeClass(errorClass);
		$('#sizeUnitPrice').removeClass(errorClass);
		$('#sizeDeliveryQty').removeClass(errorClass);
		$('#sizeManufactureMinQty').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.product.productCategoryId == 0) {
			$('#productCategory').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Categoria' não pode ser vazio</li>";
		}
		
		if ($scope.product.supplierId == 0) {
			$('#supplier').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo '"+$rootScope.domainConfiguration.labels['SUPPLIER']+"' não pode ser vazio</li>";
		}
		
		if ($scope.product.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Nome' não pode ser vazio</li>";
		}
		
		if ($scope.product.description == "") {
			$('#description').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Descrição' não pode ser vazio</li>";
		}
		
		if ($scope.product.type == "0") {
			$('#type').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Tipo' não pode ser vazio</li>";
		}
		
		if ($scope.product.unit == "" && $scope.product.type != "0" && $scope.product.type != "CATALOG") {
			$('#unit').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Unidade' não pode ser vazio</li>";
		}
		
		if ($scope.product.hasDeliveryUnit && $scope.product.deliveryUnit == "" && $scope.product.type != "0" && $scope.product.type != "CATALOG") {
			$('#deliveryUnit').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Unidade de entrega' não pode ser vazio</li>";
		}
		
		if ($scope.product.manufactureTime < 0) {
			$('#manufactureTime').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Prazo de produção' não pode ser menor que zero</li>";
		}
		
		if ($scope.product.deliveryTime < 0) {
			$('#deliveryTime').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Prazo de entrega' não pode ser menor que zero</li>";
		}
		
		if ($scope.product.sizeName == "" && $scope.product.type != "0" && $scope.product.type != "CATALOG") {
			$('#sizeName').addClass(errorClass);
			result = false;
			errorMsg += "<li>Informe um título para as opções. Ex.: tamanho, cor, peso, etc</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var validateSize = function() {
		resetSizeValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.productSize.name == "") {
			$('#sizeName2').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Nome' não pode ser vazio</li>";
		}
		
		if ($scope.productSize.unitPrice == 0) {
			$('#sizeUnitPrice').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Valor unitário' não pode ser zero</li>";
		}
		
		if ($scope.product.hasDeliveryUnit && $scope.productSize.deliveryQty <= 0) {
			$('#sizeDeliveryQty').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Quantidade por Unidade de Entrega' não pode ser zero</li>";
		}
		
		if ($scope.product.hasManufactureMinQty && $scope.productSize.manufactureMinQty == 0) {
			$('#sizeManufactureMinQty').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Quantidade mínima para produção' não pode ser zero</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		$scope.product = { id: 0
				         , supplierId: 0
				         , productCategoryId: 0
						 , franchisorId: $routeParams.franchisorId
						 , name: ""
						 , description: ""
						 , unit: ""
						 , hasDeliveryUnit: false
						 , deliveryUnit: ""
						 , hasManufactureMinQty: false
						 , manufactureTime: 0
						 , deliveryTime: 0
						 , sizeName: ""
						 , allowNegativeStock: false
						 , allowChangeUnitPrice: false
						 , allowSupplierChangeUnitPrice: false
						 , groupSizes: false
						 , type: "0"}
						 	
		//clear validation
		resetValidation();			 	
	}
	
	var resetSize = function(product) {
		$scope.productSize = {
			id: 0,
			productId: $scope.product.id,
			name: "",
			description: "",
			groupName: "",
			unitPrice: 0,
			deliveryQty: 0,
			manufactureMinQty: 0,
			minStock: 0,
			consolidatedStock: 0,
			isAvailable: true,			 
		    isActive: true
		}
		
		//clear validation
		resetSizeValidation();
	}
	
	$scope.calculateManufactureMinTotalQty = function(item, size) {
		if (size != null && item != null) {
			if (item.hasDeliveryUnit) {
				return size.deliveryQty * size.manufactureMinQty;
			} else {
				return size.manufactureMinQty;
			}
		} else {
			return 0;
		}    				
	}
	
	$scope.addFile = function(item)  {
		$scope.product = item;
		$scope.progressVisible = false;			
	}
	
	$scope.uploadImage = function() {
		
		var config = {
				  method: 'GET' ,
				  url: '/ws/image/url/product',
				  unique: false,
				  requestId: 'products-getImageUrl'
				};
		
		httpSvc(config)
			.success(function(data){	        
		        var fd = new FormData()
		        fd.append("uploadedFile", $scope.productFile)
		        fd.append("productId", $scope.product.id)
	
		        var xhr = new XMLHttpRequest()
		        xhr.upload.addEventListener("progress", uploadProgress, false)
		        xhr.addEventListener("load", uploadComplete, false)
		        xhr.addEventListener("error", uploadFailed, false)
		        xhr.addEventListener("abort", uploadCanceled, false)
		        xhr.open("POST", data.url)
		        xhr.setRequestHeader('Accept', 'application/json');
		        $scope.progressVisible = true
		        xhr.send(fd)			
			});
	}
    
    function uploadComplete(evt) {
    	var responseObj = JSON.parse(this.response);
    	$scope.product.imageURL = responseObj.url;
        $scope.$apply(function(){
        	$('#addImageModal').modal('hide');   
        })
		NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Imagem carregada com sucesso!');
    }
    
    function uploadFailed(evt) {
        NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao carregar a imagem!');
    }
	
	$scope.uploadCatalog = function() {
		
		var config = {
				  method: 'GET' ,
				  url: '/ws/documents/catalog/url',
				  unique: false,
				  requestId: 'documents-getUploadUrl'
				};
		
		httpSvc(config)
			.success(function(data){	        
		        var fd = new FormData()
		        fd.append("uploadedFile", $scope.file)
		        fd.append("productId", $scope.product.id)
	
		        var xhr = new XMLHttpRequest()
		        xhr.upload.addEventListener("progress", uploadProgress, false)
		        xhr.addEventListener("load", uploadCatalogComplete, false)
		        xhr.addEventListener("error", uploadCatalogFailed, false)
		        xhr.addEventListener("abort", uploadCanceled, false)
		        xhr.open("POST", data.url)
		        xhr.setRequestHeader('Accept', 'application/json');
		        $scope.progressVisible = true
		        xhr.send(fd);
			});
	}
	
    function uploadCatalogComplete(evt) {
    	var responseObj = JSON.parse(this.response);
    	$scope.product.catalogName = responseObj.url;
        $scope.$apply(function(){
        	$('#newFileModal').modal('hide');   
        })
		NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Catálogo carregado com sucesso!');
    }
    
    function uploadCatalogFailed(evt) {
        NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao carregar o catálogo!');
    }
	
	function uploadProgress(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
                $scope.progress = Math.round(evt.loaded * 100 / evt.total)
            } else {
                $scope.progress = 'unable to compute'
            }
        })
    }
	
	function uploadCanceled(evt) {
        $scope.$apply(function(){
        	$('#addImageModal').modal('hide');
        })
        NotificationUtil.show($('#notificationArea'), 'warning', 'Alerta!', 'Upload cancelado!');
    }
	
	$scope.contentTypeClass = function(contentType){
		if (contentType == null) return;

    	var className = 'fa-file-o';
    	if (contentType.indexOf("pdf") >= 0) {className = 'fa-file-pdf-o';}
    	if (contentType.indexOf("image") >= 0) {className = 'fa-file-image-o';}
    	if (contentType.indexOf("presentation") >= 0) {className = 'fa-file-powerpoint-o';}
    	if (contentType.indexOf("excel") >= 0) {className = 'fa-file-excel-o';}
    	return className;
    };
    
    $scope.statusClass = function(size){
    	var className = "";
    	if (size.isActive) {
    		if (size.isAvailable) {
    			className = 'text-success';
    		} else {
    			className = 'text-warning';
    		}
    		
    	} else {
    		className = 'text-danger';
    	}
    	return className;
    };
    
    $scope.statusTitle = function(size){
    	var title = "";
    	if (size.isActive) {
    		if (size.isAvailable) {
    			title = 'Disponível';
    		} else {
    			title = 'Indisponível';
    		}    		
    	} else {
    		title = 'Inativo';
    	}
    	return title;
    };
    
    $scope.getRestrictionList = function(restrictions){
		var text = "";
		restrictions.forEach(function(entry, index, array) {
			var restrictionData = entry.split("-");
			text += $scope.stateList[restrictionData[0]].code + " - ";
			if (restrictionData[1] == 0) {
				text += "TODAS; ";
			} else {
				text += $scope.cityList[restrictionData[1]].name + "; ";
			}			
		});
		return text;
	}
    
    $scope.toggleCategory = function (category) {

		category.open = !category.open;
		
		if (!category.productsLoaded) {
			
			LoadingUtil.show($('#products-loadingArea'));
			
			//retrieve products data for category
			var productConfig = {
					  method: 'GET',
					  url: '/ws/products/franchisor/'+$routeParams.franchisorId+'/category/'+category.id,
					  unique: false,
					  requestId: 'products-load'
					};
			
			httpSvc(productConfig)
				.success(function(data){
					category.productsLoaded = true;
					category.products = data.productList;
					
					for (var key in data.supplierList) {
						$scope.supplierList[key] = data.supplierList[key];
					}
					
					LoadingUtil.hide($('#products-loadingArea'));
					
				});
		}
	}
	
	var init = function() {
		
		//suppliers
		$scope.supplierList = [];
		$scope.suppliers = [];
		$scope.suppliers.push({"id":0,"name":"Carregando ..."})
		
		//get suppliers
		var supplierConfig = {
			  method: 'GET',
			  url: '/ws/suppliers/franchisor/'+$routeParams.franchisorId+'/hasStock',
			  unique: false,
			  requestId: 'suppliers-load'
			};
		
		httpSvc(supplierConfig)
			.success(function(data){
				$scope.suppliers = data.suppliers;
				$scope.suppliers.unshift({"id":0,"name":" Selecione ..."})
			});
		
		//product categories
		$scope.productCategories = [];
		$scope.productCategoriesMap = {};
		$scope.productCategorySelect = [];
		$scope.productCategorySelect.push({"id":0,"name":"Carregando ..."})
		
		//get product categories
		var productCategoriesConfig = {
			  method: 'GET',
			  url: '/ws/products/categories/franchisor/'+$routeParams.franchisorId,
			  unique: false,
			  requestId: 'productCategories-load'
			};
		
		httpSvc(productCategoriesConfig)
			.success(function(data){
				$scope.productCategories = data.productCategoryList;
				$scope.stateList = data.states;
				$scope.cityList = data.cities;
				$scope.productCategorySelect = [];
				$scope.productCategorySelect.push({"id":0,"name":" Selecione ..."})
				
				$scope.productCategories.forEach(function(entry, index, array) {
					entry.open = false;
					entry.productsLoaded = false;
					entry.products = [];
					$scope.productCategoriesMap[entry.id] = entry;
					$scope.productCategorySelect.push(entry);					
				});
			});
		
		//Load Entity Profiles
		var configTypes = {
			  method: 'GET',
			  url: '/ws/products/types',
			  unique: false,
			  requestId: 'types-load'
			};

		httpSvc(configTypes)
			.success(function(data){
				if (data.returnMessage.code > 0) {
			    	$scope.types = data.enumValues;
			    	$scope.types.push({key: '0', value: ' Selecione ...'});
				} else {
					$scope.types = [{key: '0', value: 'Erro ao carregar!'}];
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os tipos de produto!');
			});
	
		reset();
	}
	
	init();
});