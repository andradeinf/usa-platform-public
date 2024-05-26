angular.module('franchisorLogistics').controller('ProfileSelectionCtrl', function ($scope, $window, httpSvc) {
	
	$scope.selectProfile = function(item) {
		var config = {
			  method: 'GET',
			  url: '/ws/login/userProfileSelection/'+item.id,
			  unique: true,
			  requestId: 'selectProfile-add'
			};
	
		
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$window.location.href = '/';
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao selecionar o perfil do usuário!');
				}
			});
	}
	
	var init = function() {
		
		//user profile list
		var config = {
			  method: 'GET' ,
			  url: '/ws/login/userProfileSelection',
			  unique: false,
			  requestId: 'userProfile-load'
			};			
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.userProfileList = data.userProfiles;
					$('#list-overlay').remove();
					$('#list-loading-img').remove();
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os perfis do usuário!');
				}
			});
		
	}
	
	init();

});