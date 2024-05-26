angular.module('franchisorLogistics').controller('FranchiseeUsersCtrl', function ($scope, $routeParams, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.search = function(){
		if (validateSearch()) {
			
			var config = {
				  method: 'GET' ,
				  url: '/ws/users/email/'+$scope.franchiseeUser.user.email+'/search',
				  unique: false,
				  requestId: 'users-search'
				};
			
			httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.franchiseeUser.user.id = data.user.id;
					$scope.franchiseeUser.user.name = data.user.name;
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
					franchiseeUser: $scope.franchiseeUser,
					userGroups: $scope.franchiseeUserGroupList
			}
			
			var config = {
					method: ($scope.franchiseeUser.id == 0 ? 'POST' : 'PUT'),
				  url: '/ws/franchisees/'+$routeParams.franchiseeId+'/users',
				  data: requestData,
				  unique: true,
				  requestId: 'franchiseeUsers-add'
				};
			
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if ($scope.franchiseeUser.id == 0) {
							$scope.franchiseeUserList.push(data.franchiseeUser);
							$scope.franchiseeUsersGroupsList[data.franchiseeUser.id] = data.userGroups;
						}
						$('#newFranchiseeUserModal').modal('hide');
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
		$scope.franchiseeUser = item;
		$scope.franchiseeUserGroupList = $scope.franchiseeUsersGroupsList[item.id];
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir o usuário "' + item.user.name + '"?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){

		var config = {
				method: 'DELETE' ,
				url: '/ws/franchisees/users/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'franchiseeUsers-delete'
			};
	
		httpSvc(config)
			.success(function(){
				$scope.franchiseeUserList = $scope.franchiseeUserList.filter(user => user.id != $scope.itemToRemove.id);
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
		
		if ($scope.franchiseeUser.user.id == "0") {
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
		
		if ($scope.franchiseeUser.user.email == "") {
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
		$scope.franchiseeUser = { id: 0,
				  user: {
					  id: 0,
					  name: "",
					  email: ""
				  }
				}
		$scope.franchiseeUserGroupList = new Object();
						 	
		//clear validadtion
		resetValidation();							 	
	}
	
	var init = function() {
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/franchisees/'+$routeParams.franchiseeId+'/users',
			  unique: false,
			  requestId: 'franchiseeUsers-load'
			};
	
		httpSvc(config)
			.success(function(data){
				$scope.franchiseeUserList = data.franchiseeUsers;
				$scope.userGroupsList = data.userGroups;
				$scope.franchiseeUsersGroupsList = data.franchiseeUsersGroups;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
		
		reset();
	}
	
	init();
});