angular.module('franchisorLogistics').controller('ChangePasswordFormCtrl', function ($scope, $window, $routeParams, httpSvc) {
	
	var errorClass = 'has-error';
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#newPassword').removeClass(errorClass);
		$('#newPasswordConfirmation').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.changePassword.newPassword == "") {
			$('#newPassword').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo nova senha não pode ser vazio</li>";
		}
		
		if ($scope.changePassword.newPasswordConfirmation == "") {
			$('#newPasswordConfirmation').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo repetir nova senha não pode ser vazio</li>";
		}
		
		if ($scope.changePassword.newPassword != "" &&
			$scope.changePassword.newPasswordConfirmation != "" &&
			$scope.changePassword.newPassword != $scope.changePassword.newPasswordConfirmation) {
			$('#newPassword').addClass(errorClass);
			$('#newPasswordConfirmation').addClass(errorClass);
			result = false;
			errorMsg += "<li>Os valores informados para nova senha não são iguais</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var doRedirect = function() {
		$window.location.href = '/login';
	}
	
	$scope.doChangePassword = function() {
		if (validate()) {
			
			var config = {
					  method: 'POST',
					  url: '/ws/login/changePassword',
					  data: $scope.changePassword,
					  unique: true,
					  requestId: 'login-doChangePassword'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						ConfirmationUtil.show($('#notificationArea'), 'Troca de Senha!', 'Senha alterada com sucesso.', doRedirect);
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problemas ao solicitar troca de senha.');
					}
				});				
		};
	}
	
	var init = function() {
			
		$scope.changePassword = { uid: $routeParams.uid
								, newPassword: ""
		               			, newPasswordConfirmation: ""
				 }
		}
	
	init();

});