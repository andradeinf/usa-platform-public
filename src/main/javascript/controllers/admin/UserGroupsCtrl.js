angular.module('franchisorLogistics').controller('UserGroupsCtrl', function ($scope, $routeParams, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.save = function(){
		if (validate()) {
		
			var config = {
				  method: ($scope.userGroup.id == 0 ? 'POST' : 'PUT') ,
				  url: '/ws/users/groups',
				  data: $scope.userGroup,
				  unique: true,
				  requestId: 'userGroup-createOrUpdate'
				};
				
			httpSvc(config)
				.success(function(data){
					if ($scope.userGroup.id == 0) {
						$scope.userGroupList.push(data);
					}
					$('#newUserGroupModal').modal('hide');
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Perfil salvos com sucesso!');
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar o perfil!');
					}
				});						
		};
	}
	
	$scope.edit = function(item){
		$scope.userGroup = item;
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir o perfil "' + item.name + '"?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){

		var config = {
				method: 'DELETE' ,
				url: '/ws/users/groups/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'userGroup-delete'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.userGroupList = $scope.userGroupList.filter(group => group.id != $scope.itemToRemove.id);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Perfil excluído com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o perfil!');
				}
			});
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#name').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.userGroup.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Nome' não pode ser vazio</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		$scope.userGroup = { id: 0
							, entityId: $routeParams.entityId
							, name: ""
						 	, receiveMessage: false}
						 	
		//clear validation
		resetValidation();								 	
	}
	
	var init = function() {

		var config = {
			  method: 'GET' ,
			  url: '/ws/users/'+$routeParams.entityId+'/groups',
			  unique: false,
			  requestId: 'userGroups-load'
			};
	
		httpSvc(config)
			.success(function(data){
				$scope.userGroupList = data.userGroups;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
	
		reset();
	}
	
	init();
});