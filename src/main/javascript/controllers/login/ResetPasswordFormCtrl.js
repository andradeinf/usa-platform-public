angular.module('franchisorLogistics').controller('ResetPasswordFormCtrl', function ($scope, $window, httpSvc) {
	
	var errorClass = 'has-error';
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#email').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		
		if ($scope.resetPassword.email == "") {
			$('#email').addClass(errorClass);
			result = false;
		}
		
		return result;
	}
	
	var doRedirect = function() {
		$window.location.href = '/login';
	}
	
	$scope.doResetPassword = function() {
		if (validate()) {
			
			var config = {
					  method: 'POST',
					  url: '/ws/login/resetPassword',
					  data: $scope.resetPassword,
					  unique: true,
					  requestId: 'login-doResetPassword'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						ConfirmationUtil.show($('#notificationArea'), 'Troca de Senha!', 'Um e-mail para trocar sua senha foi enviado para o endereço de e-mail informado,'+
								'<br />caso ele seja um e-mail cadastrado.<br/>Verifique sua caixa de spam caso não receba em alguns minutos.', doRedirect);
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
			
		$scope.resetPassword = { email: ""
		               	       , password: ""
				 			   }
		}
	
	init();

});