angular.module('franchisorLogistics').controller('FranchisorsCtrl', function ($scope, $window, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.duplicate = function(item){
		$scope.franchisor = { id: 0
				, name: item.name + " (Cópia)"
			 	, corporateName: item.corporateName
			 	, fiscalId: item.fiscalId
			 	, additionalInformation: item.additionalInformation
			 	, address: item.address
			 	, contactName: item.contactName
			 	, contactPhone: item.contactPhone
			 	, contactEmail: item.contactEmail
				, paymentTime: item.paymentTime
				, maxStorageSizeMB: item.maxStorageSizeMB
				, radioURL: item.radioURL
				, preferedDomainKey: item.preferedDomainKey
				, flagAnnouncement: item.flagAnnouncement
				, flagCalendar: item.flagCalendar
				, flagDocuments: item.flagDocuments
				, flagTraining: item.flagTraining
			 	, duplicateId: item.id}
	}
	
	$scope.save = function(){
		if (validate()) {
		
			var config = {
				  method: ($scope.franchisor.id == 0 ? 'POST' : 'PUT') ,
				  url: '/ws/franchisors',
				  data: $scope.franchisor,
				  unique: true,
				  requestId: 'franchisors-createOrUpdate'
				};
				
			httpSvc(config)
				.success(function(data){
					if ($scope.franchisor.id == 0) {
						$scope.franchisorList.push(data);
					}
					$('#newFranchisorModal').modal('hide');
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados do franqueador salvos com sucesso!');
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados do franqueador!');
					}
				});	
		
		};
	}
	
	$scope.edit = function(item){
		$scope.franchisor = item;
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){

		var config = {
				method: 'DELETE' ,
				url: '/ws/franchisors/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'franchisors-delete'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.franchisorList = $scope.franchisorList.filter(franchisor => franchisor.id != $scope.itemToRemove.id);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Franqueador excluído com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o franqueador!');
				}
			});
	}
	
	$scope.getImage = function(item)  {
		if (item.imageURL == null) {
			return '/img/not-available.jpg';
		} else {
			return item.imageURL + '=s150';
		}
	}
	
	$scope.addImage = function(item)  {
		$scope.franchisor = item;
		$scope.progressVisible = false;
	}
	
	$scope.editProfiles = function(item){
		$window.location.href = '#/admin/users/' + item.id + '/groups';
	}
	
	$scope.editUsers = function(item){
		$window.location.href = '#/admin/franchisors/' + item.id + '/users';
	}
	
	$scope.editProductCategories = function(item){
		$window.location.href = '#/admin/franchisors/' + item.id + '/productCategories';
	}
	
	$scope.editProducts = function(item){
		$window.location.href = '#/admin/franchisors/' + item.id + '/products';
	}
	
	$scope.editFranchisees = function(item){
		$window.location.href = '#/admin/franchisors/' + item.id + '/franchisees';
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#name').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.franchisor.name == "") {
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
		$scope.franchisor = { id: 0
							, name: ""
						 	, corporateName: ""
						 	, fiscalId: ""
						 	, additionalInformation: ""
						 	, address: ""
						 	, contactName: ""
						 	, contactPhone: ""
						 	, contactEmail: ""
							, paymentTime: 0
							, maxStorageSizeMB: 100
						    , loginURL: ""
							, radioURL: ""
							, flagAnnouncement: true
							, flagCalendar: true
							, flagDocuments: true
							, flagTraining: true}
						 	
		//clear validadtion
		resetValidation();
	}
	
	$scope.uploadImage = function() {
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/image/url/franchisor',
			  unique: false,
			  requestId: 'franchisors-getImageUrl'
			};
	
		httpSvc(config)
			.success(function(data){				        
		        var fd = new FormData()
		        fd.append("uploadedFile", $scope.franchisorFile)
		        fd.append("franchisorId", $scope.franchisor.id)

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
    	$scope.franchisor.imageURL = responseObj.url;
        $scope.$apply(function(){
        	$('#addImageModal').modal('hide');            
        })        
		NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Logotipo do franqueador carregado com sucesso!');
    }
    
    function uploadFailed(evt) {
        NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao carregar o logotipo do franqueador!');
    }
    
	function uploadCanceled(evt) {
        $scope.$apply(function(){
        	$('#addImageModal').modal('hide');
        })
        NotificationUtil.show($('#notificationArea'), 'warning', 'Alerta!', 'Upload cancelado!');
    }
	
	var init = function() {
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/franchisors',
			  unique: false,
			  requestId: 'franchisors-load'
			};
	
		httpSvc(config)
			.success(function(data){
				$scope.franchisorList = data;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
		
		reset();
		
		$("[data-mask]").inputmask();
		
	}
	
	init();
});