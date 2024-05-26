angular.module('franchisorLogistics').controller('SuppliersCtrl', function ($scope, $window, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.save = function(){
		if (validate()) {
			
			delete $scope.supplier.state;
			delete $scope.supplier.city;
			if ($scope.supplier.type == "0") {$scope.supplier.type = null};
		
			var config = {
				  method: ($scope.supplier.id == 0 ? 'POST' : 'PUT') ,
				  url: '/ws/suppliers',
				  data: $scope.supplier,
				  unique: true,
				  requestId: 'suppliers-createOrUpdate'
				};
				
			httpSvc(config)
				.success(function(data){
					if ($scope.supplier.id == 0) {
						data.supplier.state = data.state;
						data.supplier.city = data.city;
						$scope.supplierList.push(data.supplier);
					} else {
						$scope.supplier.state = data.state;
						$scope.supplier.city = data.city;
					}
					$('#newSupplierModal').modal('hide');
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
	
	$scope.edit = function(item){
		$scope.supplier = item;
		if ($scope.supplier.stateId != 0) {
			loadStateCities();
		}
	}
	
	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){
	
		var config = {
				method: 'DELETE' ,
				url: '/ws/suppliers/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'suppliers-delete'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.supplierList = $scope.supplierList.filter(supplier => supplier.id != $scope.itemToRemove.id);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Excluído com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir!');
				}
			});
	}
	
	$scope.editProfiles = function(item){
		$window.location.href = '#/admin/users/' + item.id + '/groups';
	}
	
	$scope.editUsers = function(item){
		$window.location.href = '#/admin/suppliers/' + item.id + '/users';
	}
	
	$scope.editFranchisors = function(item){
		$window.location.href = '#/admin/suppliers/' + item.id + '/franchisors';
	}
	
	$scope.stock = function(item){
		$window.location.href = '#/admin/suppliers/' + item.id + '/stock';
	}
	
	$scope.requests = function(item){
		$window.location.href = '#/admin/suppliers/' + item.id + '/requests';
	}
	
	$scope.onChangeState = function(){
		$scope.supplier.cityId = 0;
		$scope.cities = [];
		if ($scope.supplier.stateId != 0) {
			loadStateCities();
		}
		
	}
	
	$scope.getImage = function(item)  {
		if (item.imageURL == null) {
			return '/img/not-available.jpg';
		} else {
			return item.imageURL + '=s150';
		}
	}
	
	$scope.addImage = function(item)  {
		$scope.supplier = item;
		$scope.progressVisible = false;			
	}
	
	$scope.uploadImage = function() {
		
		var config = {
				  method: 'GET' ,
				  url: '/ws/image/url/supplier',
				  unique: false,
				  requestId: 'supplier-getImageUrl'
				};
		
		httpSvc(config)
			.success(function(data){	        
		        var fd = new FormData()
		        fd.append("uploadedFile", $scope.supplierFile)
		        fd.append("supplierId", $scope.supplier.id)
	
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
	
	function uploadProgress(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
                $scope.progress = Math.round(evt.loaded * 100 / evt.total)
            } else {
                $scope.progress = 'unable to compute'
            }
        })
    }
    
    function uploadComplete(evt) {
    	var responseObj = JSON.parse(this.response);
    	$scope.supplier.imageURL = responseObj.url;
        $scope.$apply(function(){
        	$('#addImageModal').modal('hide');   
        })
		NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Imagem carregada com sucesso!');
    }
    
    function uploadFailed(evt) {
        NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao carregar a imagem!');
    }
    
	function uploadCanceled(evt) {
        $scope.$apply(function(){
        	$('#addImageModal').modal('hide');
        })
        NotificationUtil.show($('#notificationArea'), 'warning', 'Alerta!', 'Upload cancelado!');
    }
	
	var loadStateCities = function(){

		if ($scope.supplier.stateId != 0) {
			
			$scope.cities.push({"id":0,"name":"Carregando ..."})
		
			//get cities
			var statesConfig = {
				  method: 'GET',
				  url: '/ws/statesAndCities/states/'+$scope.supplier.stateId+'/cities',
				  unique: false,
				  requestId: 'cities-load'
				};
			
			httpSvc(statesConfig)
				.success(function(data){
					$scope.cities = data;
					$scope.cities.unshift({"id":0,"name":" Selecione ..."})
				});
			
		}
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#name').removeClass(errorClass);
		$('#supplierType').removeClass(errorClass);
		$('#supplierCategory').removeClass(errorClass);
		$('#visibility').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.supplier.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Nome' não pode ser vazio</li>";
		}
		
		if ($scope.supplier.categoryId == 0) {
			$('#supplierCategory').addClass(errorClass);
			result = false;
			errorMsg += "<li>Selecione a categoria</li>";
		}
		
		if ($scope.supplier.categoryId != 0 && $scope.supplierCategoriesMap[$scope.supplier.categoryId].hasStockControl && $scope.supplier.type == 0) {
			$('#supplierType').addClass(errorClass);
			result = false;
			errorMsg += "<li>Selecione o tipo</li>";
		}
		
		if ($scope.supplier.visibility == 0) {
			$('#visibility').addClass(errorClass);
			result = false;
			errorMsg += "<li>Selecione a visibilidade</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		$scope.supplier = { id: 0
						  , name: ""
						  , description: ""
						  , address: ""
						  , contactName: ""
						  , contactPhone: ""
						  , contactEmail: ""
						  , segment: ""
						  , type: "0"
						  , stateId: 0
						  , cityId: 0
						  , categoryId: 0
						  , visibility: "0"
						  , deliveryNotes: ""}
						 	
		//clear validation
		resetValidation();
	}
	
	var init = function() {
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/suppliers',
			  unique: false,
			  requestId: 'suppliers-load'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					data.suppliers.forEach(function(entry) {
						entry.state = data.states[entry.stateId];
						entry.city = data.cities[entry.cityId];
					});
					$scope.supplierList = data.suppliers;
					
					$('#list-overlay').remove();
					$('#list-loading-img').remove();
				}  else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}				
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}
			});
		
		//supplier types
		$scope.supplierTypes = [];
		$scope.supplierTypes.push({"key":"0","value":"Carregando ..."})
		
		//get supplier types
		var configSupplierTypes = {
			  method: 'GET',
			  url: '/ws/suppliers/supplierTypes',
			  unique: false,
			  requestId: 'suppliersTypes-load'
			};
		
		httpSvc(configSupplierTypes)
			.success(function(data){
				if (data.returnMessage.code > 0) {
			    	$scope.supplierTypes = data.enumValues;
			    	$scope.supplierTypes.unshift({key: '0', value: 'Selecione...'})
				} else {
					$scope.supplierTypes = [{key: '0', value: 'Erro ao carregar!'}];
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados!');
			});
	
		//supplier categories
		$scope.supplierCategories = [];
		$scope.supplierCategoriesSelect = [];
		$scope.supplierCategoriesSelect.push({id: 0, name: 'Carregando ...'})
		
		//get supplier categories
		var configSupplierCategories = {
			  method: 'GET',
			  url: '/ws/suppliers/categories',
			  unique: false,
			  requestId: 'supplierCategories-load'
			};
		
		httpSvc(configSupplierCategories)
			.then(function successCallback(response){
				if (response.data.returnMessage.code > 0) {
					$scope.supplierCategories = response.data.supplierCategories;
					$scope.supplierCategoriesMap = {};
					$scope.supplierCategoriesSelect = [];
			    	$scope.supplierCategoriesSelect.push({id: 0, name: ' Selecione...'});
			    	
			    	$scope.supplierCategories.forEach(function(entry) {
						entry.open = false;
						$scope.supplierCategoriesSelect.push(entry)
						$scope.supplierCategoriesMap[entry.id] = entry;
					});
				} else {
					$scope.supplierCategoriesSelect = [{id: 0, name: 'Erro ao carregar!'}];
				}
			}, function errorCallback(response){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados!');
			});
		
		//states and cities
		$scope.states = [];
		$scope.states.push({"id":0,"name":"Carregando ..."})
		$scope.cities = [];
		
		//get states
		var statesConfig = {
			  method: 'GET',
			  url: '/ws/statesAndCities/states',
			  unique: false,
			  requestId: 'states-load'
			};
		
		httpSvc(statesConfig)
			.success(function(data){
				$scope.states = data;
				$scope.states.unshift({"id":0,"name":" Selecione ..."})
			});
		
		reset();
		
		//supplier visibility types
		$scope.supplierVisibilityTypes = [];
		$scope.supplierVisibilityTypes.push({"key":"0","value":"Carregando ..."})
		
		//get supplier visibility types
		var configSupplierVisibilityTypes = {
			  method: 'GET',
			  url: '/ws/suppliers/visibilityTypes',
			  unique: false,
			  requestId: 'visibilityTypes-load'
			};
		
		httpSvc(configSupplierVisibilityTypes)
			.success(function(data){
				if (data.returnMessage.code > 0) {
			    	$scope.supplierVisibilityTypes = data.enumValues;
			    	$scope.supplierVisibilityTypes.unshift({key: '0', value: 'Selecione...'})
				} else {
					$scope.supplierVisibilityTypes = [{key: '0', value: 'Erro ao carregar!'}];
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados!');
			});
		
		$("[data-mask]").inputmask();
		
	}
	
	init();
});