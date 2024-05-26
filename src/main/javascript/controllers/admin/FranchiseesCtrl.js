angular.module('franchisorLogistics').controller('FranchiseesCtrl', function ($scope, $window, $routeParams, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.save = function(){
		if (validate()) {
			
			delete $scope.franchisee.state;
			delete $scope.franchisee.city;
		
			var config = {
				  method: ($scope.franchisee.id == 0 ? 'POST' : 'PUT') ,
				  url: '/ws/franchisees',
				  data: $scope.franchisee,
				  unique: true,
				  requestId: 'franchisees-createOrUpdate'
				};

			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if ($scope.franchisee.id == 0) {
							data.franchisee.state = data.state;
							data.franchisee.city = data.city;
							$scope.franchiseeList.push(data.franchisee);
						} else {
							$scope.franchisee.state = data.state;
							$scope.franchisee.city = data.city;
						}						
						$('#newFranchiseeModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados salvos com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados!');
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dadoss!');
					}
				});						
		};
	}
	
	$scope.edit = function(item){
		$scope.franchisee = item;
		if ($scope.franchisee.stateId != 0) {
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
				url: '/ws/franchisees/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'franchisees-delete'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.franchiseeList = $scope.franchiseeList.filter(franchisee => franchisee.id != $scope.itemToRemove.id);
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
		$window.location.href = '#/admin/franchisees/' + item.id + '/users';
	}
	
	$scope.getImage = function(item)  {
		if (item.imageURL == null) {
			return '/img/not-available.jpg';
		} else {
			return item.imageURL + '=s150';
		}
	}
	
	$scope.addImage = function(item)  {
		$scope.franchisee = item;
		$scope.progressVisible = false;
	}

	$scope.deleteImage = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir a imagem?', doDeleteImage, 'Não', 'Sim');
	}
		
	var doDeleteImage = function(){

		var config = {
				method: 'DELETE' ,
				url: '/ws/franchisees/'+$scope.itemToRemove.id+"/deleteImage",
				unique: true,
				requestId: 'franchisees-deleteImage'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.itemToRemove.imageURL = data.franchisee.imageURL;
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Imagem do excluída com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir a imagem!');
				}
			});
	}
	
	$scope.onChangeState = function(){
		$scope.franchisee.cityId = 0;
		$scope.cities = [];
		if ($scope.franchisee.stateId != 0) {
			loadStateCities();
		}
		
	}
	
	var loadStateCities = function(){
		
		if ($scope.franchisee.stateId != 0) {
			
			$scope.cities.push({"id":0,"name":"Carregando ..."})
		
			//get cities
			var statesConfig = {
				  method: 'GET',
				  url: '/ws/statesAndCities/states/'+$scope.franchisee.stateId+'/cities',
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
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.franchisee.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Nome não pode ser vazio</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		$scope.franchisee = { id: 0
							, franchisorId: $routeParams.franchisorId
							, name: ""
						 	, corporateName: ""
						 	, additionalInformation: ""
						 	, fiscalId: ""
						 	, address: ""
						 	, contactName: ""
						 	, contactPhone: ""
						 	, contactEmail: ""
							, stateId: 0
						 	, cityId: 0}
		
		//clear validadtion
		resetValidation();									 	
	}
	
	$scope.uploadImage = function() {
		
		var config = {
			  method: 'GET' ,
			  url: '/ws/image/url/franchisee',
			  unique: false,
			  requestId: 'franchisees-getImageUrl'
			};
	
		httpSvc(config)
			.success(function(data){				        
		        var fd = new FormData()
		        fd.append("uploadedFile", $scope.franchiseeFile)
		        fd.append("franchiseeId", $scope.franchisee.id)

		        var xhr = new XMLHttpRequest()		        
		        xhr.upload.addEventListener("progress", uploadProgress, false)
		        xhr.addEventListener("load", uploadComplete, false)
		        xhr.addEventListener("error", uploadFailed, false)
		        xhr.addEventListener("abort", uploadCanceled, false)
		        xhr.open("POST", data.url)
		        xhr.setRequestHeader('Accept', 'application/JSON');
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
    	$scope.franchisee.imageURL = responseObj.url;
        $scope.$apply(function(){
        	$('#addImageModal').modal('hide');            
        })        
		NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Logotipo carregado com sucesso!');
    }
    
    function uploadFailed(evt) {
        NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao carregar o logotipo!');
    }
    
	function uploadCanceled(evt) {
        $scope.$apply(function(){
        	$('#addImageModal').modal('hide');
        })
        NotificationUtil.show($('#notificationArea'), 'warning', 'Alerta!', 'Upload cancelado!');
    }
	
	var init = function() {

		//franchisees
		var franchiseesConfig = {
			  method: 'GET' ,
			  url: '/ws/franchisees/franchisor/'+$routeParams.franchisorId,
			  unique: false,
			  requestId: 'franchisees-load'
			};			
	
		httpSvc(franchiseesConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					data.franchisees.forEach(function(entry) {
						entry.state = data.states[entry.stateId];
						entry.city = data.cities[entry.cityId];
					});
					$scope.franchiseeList = data.franchisees;
					
					$('#list-overlay').remove();
					$('#list-loading-img').remove();
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}				
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}
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
		
		$("[data-mask]").inputmask();
	}
	
	init();
});