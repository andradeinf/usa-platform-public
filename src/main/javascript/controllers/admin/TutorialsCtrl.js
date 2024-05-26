angular.module('franchisorLogistics').controller('TutorialsCtrl', function ($scope, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.save = function(){
		if (validate()) {
			
			if ($scope.tutorial.userProfileDescription) {$scope.tutorial.userProfileDescription = undefined};
			
			var config = {
				  method: ($scope.tutorial.id == 0 ? 'POST' : 'PUT') ,
				  url: '/ws/tutorials',
				  data: $scope.tutorial,
				  unique: true,
				  requestId: 'tutorials-createOrUpdate'
				};
				
			httpSvc(config)
				.success(function(data){
					if ($scope.tutorial.id != 0) {
						var index = $scope.tutorialsList.indexOf($scope.tutorial);
						$scope.tutorialsList.splice(index, 1);						
					}
					$scope.tutorialsList.push(data.tutorial);
					$('#newTutorialModal').modal('hide');
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados do tutorial salvos com sucesso!');
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados do tutorial!');
					}
				});						
		};
	}
	
	$scope.edit = function(item){
		$scope.tutorial = item;
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir o tutorial "' + item.name + '"?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){
		
		var config = {
				method: 'DELETE' ,
				url: '/ws/tutorials/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'tutorials-delete'
			};
	
		httpSvc(config)
			.success(function(){
				$scope.tutorialsList = $scope.tutorialsList.filter(tutorial => tutorial.id != $scope.itemToRemove.id);
				NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Tutorial excluída com sucesso!');
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o tutorial!');
				}
			});
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#userProfile').removeClass(errorClass);
		$('#name').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.tutorial.userProfile == "" || $scope.tutorial.userProfile == "0") {
			$('#userProfile').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Perfil não pode ser vazio</li>";
		}
		
		if ($scope.tutorial.name == "") {
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
		$scope.tutorial = { id: 0
							, name: ""
						 	, description: ""
						 	, url: ""
							, order: null
							, userProfile: "0"}
						 	
		//clear validation
		resetValidation();								 	
	}
	
	var init = function() {
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/tutorials',
			  unique: false,
			  requestId: 'tutorials-load'
			};
	
		httpSvc(config)
			.success(function(data){
				$scope.tutorialsList = data.tutorials;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
		
		//user profile types
		$scope.userProfileTypes = [];
		$scope.userProfileTypes.push({key: '0', value: 'Carregando ...'})
		
		//get supplier types
		var configUserProfileTypes = {
			  method: 'GET',
			  url: '/ws/users/userProfileTypes',
			  unique: false,
			  requestId: 'userProfileTypes-load'
			};
		
		httpSvc(configUserProfileTypes)
			.success(function(data){
				if (data.returnMessage.code > 0) {
			    	$scope.userProfileTypes = data.enumValues;
			    	$scope.userProfileTypes.unshift({key: '0', value: 'Selecione...'})
				} else {
					$scope.userProfileTypes = [{key: '0', value: 'Erro ao carregar!'}];
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os tipos de perfil de usuários!');
			});
	
		reset();
	}
	
	init();
});