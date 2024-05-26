angular.module('franchisorLogistics').controller('SupplierUsersCtrl', function ($scope, $routeParams, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.search = function(){
		if (validateSearch()) {
		
			var config = {
				  method: 'GET' ,
				  url: '/ws/users/email/'+$scope.supplierUser.user.email+'/search',
				  unique: false,
				  requestId: 'users-search'
				};
			
			httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.supplierUser.user.id = data.user.id;
					$scope.supplierUser.user.name = data.user.name;
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
					supplierUser: $scope.supplierUser,
					userGroups: $scope.supplierUserGroupList
			}
		
			var config = {
				  method: ($scope.supplierUser.id == 0 ? 'POST' : 'PUT'),
				  url: '/ws/suppliers/'+$routeParams.supplierId+'/users',
				  data: requestData,
				  unique: true,
				  requestId: 'supplierUsers-add'
				};
				
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if ($scope.supplierUser.id == 0) {
							$scope.supplierUserList.push(data.supplierUser);
							$scope.supplierUsersGroupsList[data.supplierUser.id] = data.userGroups;
						}
						$('#newSupplierUserModal').modal('hide');
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
		$scope.supplierUser = item;
		$scope.supplierUserGroupList = $scope.supplierUsersGroupsList[item.id];
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja desvincular o usuário "' + item.user.name + '"?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){
		
		var config = {
				method: 'DELETE' ,
				url: '/ws/suppliers/users/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'supplierUsers-delete'
			};
	
		httpSvc(config)
			.success(function(){
				$scope.supplierUserList = $scope.supplierUserList.filter(user => user.id != $scope.itemToRemove.id);
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
		
		if ($scope.supplierUser.user.id == "0") {
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
		
		if ($scope.supplierUser.user.email == "") {
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
		$scope.supplierUser = { id: 0,
				  user: {
					  id: 0,
					  name: "",
					  email: ""
				  }
				}
		$scope.supplierUserGroupList = new Object();
						 	
		//clear validadtion
		resetValidation();								 	
	}
	
	var init = function() {
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/suppliers/'+$routeParams.supplierId+'/users',
			  unique: false,
			  requestId: 'supplierUsers-load'
			};
	
		httpSvc(config)
			.success(function(data){
				$scope.supplierUserList = data.supplierUsers;
				$scope.userGroupsList = data.userGroups;
				$scope.supplierUsersGroupsList = data.supplierUsersGroups;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
	
		reset();
	}
	
	init();
});