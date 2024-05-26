angular.module('franchisorLogistics').controller('UsersCtrl', function ($scope, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.save = function(){
		if (validate()) {
		
			var config = {
				  method: ($scope.user.id == 0 ? 'POST' : 'PUT') ,
				  url: '/ws/users',
				  data: $scope.user,
				  unique: true,
				  requestId: 'users-createOrUpdate'
				};
				
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if ($scope.user.id == 0) {
							$scope.userList.push(data.user);
						}
						$('#newUserModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados do usuário salvos com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados do usuário: ' + data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados do usuário!');
					}
				});						
		};
	}
	
	$scope.edit = function(item){
		$scope.user = item;
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir o usuário "' + item.name + '"?\nTodos os vínculos do usuário no sistema serão excluídos.', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){
	
		var config = {
				method: 'DELETE' ,
				url: '/ws/users/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'users-delete'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.userList = $scope.userList.filter(user => user.id != $scope.itemToRemove.id);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Usuário excluído com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o usuário: ' + data.returnMessage.message);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o usuário!');
				}
			});
	}
	
	$scope.generateOrResetPassword = function() {
	  var text = "";
	  var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*?";

	  for (var i = 0; i < 5; i++)
	    text += possible.charAt(Math.floor(Math.random() * possible.length));

	  $scope.user.password = text;
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#name').removeClass(errorClass);
		$('#email').removeClass(errorClass);
		$('#preferedDomainKey').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.user.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Nome' não pode ser vazio</li>";
		}
		
		if ($scope.user.email == "") {
			$('#email').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'E-mail' não pode ser vazio</li>";
		}
		
		if ($scope.user.preferedDomainKey == "0") {
			$('#preferedDomainKey').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Domínio Principal não pode ser vazio</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		$scope.user = { id: 0
					  , name: ""
				 	  , email: ""
				 	  , preferedDomainKey: "0"
				 	  , password: ""
				 	  , enabled: true}
						 	
		//clear validation
		resetValidation();								 	
	}
	
	$scope.previousPage = function() {
		$scope.page -= 1;
	}
	
	$scope.nextPage = function() {
		$scope.page += 1;
	}
	
	$scope.totalPages = function(data) {
		if (data) {
			return Math.ceil(data.length / $scope.pageSize);
		} else {
			return 0;
		}		
	}
	
	$scope.changeFilter = function() {
		$scope.page = 1;
	}
	
	var init = function() {
		
		$scope.filter = "";
		$scope.page = 1;
		$scope.pageSize = 10;
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/users',
			  unique: false,
			  requestId: 'users-load'
			};
	
		httpSvc(config)
			.success(function(data){
				$scope.userList = data.userList;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
		
		//get domains
		$scope.domains = [];
		
		var configDomains = {
			  method: 'GET',
			  url: '/ws/configuration/domains',
			  unique: false,
			  requestId: 'domains-load'
			};
		
		httpSvc(configDomains)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.domains = data.enumValues;
					$scope.domains.push({key: '0', value: ' Selecione ...'});
				} else {
					$scope.domains = [{key: '0', value: 'Erro ao carregar!'}];
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os domínios!');
			});
	
		reset();
	}
	
	init();
});