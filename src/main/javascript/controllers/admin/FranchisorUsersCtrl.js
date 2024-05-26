angular.module('franchisorLogistics').controller('FranchisorUsersCtrl', function ($scope, $routeParams, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.search = function(){
		if (validateSearch()) {
			
			var config = {
				  method: 'GET' ,
				  url: '/ws/users/email/'+$scope.franchisorUser.user.email+'/search',
				  unique: false,
				  requestId: 'users-search'
				};
			
			httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.franchisorUser.user.id = data.user.id;
					$scope.franchisorUser.user.name = data.user.name;
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados do usuário!');
				}
			});	
		}
	}
	
	$scope.save = function(){
		if (validateSave()) {
			
			var requestData = {
					franchisorUser: $scope.franchisorUser,
					userGroups: $scope.franchisorUserGroupList
			}
		
			var config = {
				  method: ($scope.franchisorUser.id == 0 ? 'POST' : 'PUT'),
				  url: '/ws/franchisors/'+$routeParams.franchisorId+'/users',
				  data: requestData,
				  unique: true,
				  requestId: 'franchisorUsers-createOrUpdate'
				};
				
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if ($scope.franchisorUser.id == 0) {
							$scope.franchisorUserList.push(data.franchisorUser);
							$scope.franchisorUsersGroupsList[data.franchisorUser.id] = data.userGroups;
						}						
						$('#newFranchisorUserModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados do usuário salvos com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
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
		$scope.franchisorUser = item;
		$scope.franchisorUserGroupList = $scope.franchisorUsersGroupsList[item.id];
	}
	
	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir o usuário "' + item.user.name + '"?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){
		
		var config = {
				method: 'DELETE' ,
				url: '/ws/franchisors/users/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'franchisorUsers-delete'
			};
	
		httpSvc(config)
			.success(function(){
				$scope.franchisorUserList = $scope.franchisorUserList.filter(user => user.id != $scope.itemToRemove.id);
				NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Usuário excluído com sucesso!');
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o usuário!');
				}
			});
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#email').removeClass(errorClass);
	}
	
	var validateSave = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.franchisorUser.user.id == "0") {
			$('#email').addClass(errorClass);
			result = false;
			errorMsg += "<li>Faça a pesquisa pelo e-mail do usuário antes de salvar</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var validateSearch = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.franchisorUser.user.email == "") {
			$('#email').addClass(errorClass);
			result = false;
			errorMsg += "<li>O e-mail do usuário não pode ser vazio.</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		$scope.franchisorUser = { id: 0,
								  user: {
									  id: 0,
									  name: "",
									  email: ""
								  }
								}
		$scope.franchisorUserGroupList = new Object();
						 	
		//clear validadtion
		resetValidation();								 	
	}
	
	var init = function() {
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/franchisors/'+$routeParams.franchisorId+'/users',
			  unique: false,
			  requestId: 'franchisorUsers-load'
			};
	
		httpSvc(config)
			.success(function(data){
				$scope.franchisorUserList = data.franchisorUsers;
				$scope.userGroupsList = data.userGroups;
				$scope.franchisorUsersGroupsList = data.franchisorUsersGroups;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
	
		reset();
	}
	
	init();
});