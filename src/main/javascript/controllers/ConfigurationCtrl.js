angular.module('franchisorLogistics').controller('ConfigurationCtrl', function ($scope, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.save = function(){
		if (validate()) {
		
			var config = {
	  			  method: 'POST',
	  			  url: '/ws/users/profile',
	  			  data: $scope.profile,
	  			  unique: true,
	  			  requestId: 'profile-update'
	  			};
		
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', data.returnMessage.message);
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados do perfil.');
					}
				});	
		};
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#name').removeClass(errorClass);
		$('#email').removeClass(errorClass);
		$('#newPassword').removeClass(errorClass);
		$('#newPassword2').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.profile.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Nome não pode ser vazio</li>";
		}
		
		if ($scope.profile.email == "") {
			$('#email').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo e-mail não pode ser vazio</li>";
		}
		
		if (($scope.profile.newPassword != "" ||
			 $scope.profile.newPassword2 != "") &&
			$scope.profile.newPassword !=  $scope.profile.newPassword2
		   ) {
		   	$('#newPassword').addClass(errorClass);
		   	$('#newPassword2').addClass(errorClass);
			result = false;
			errorMsg += "<li>Os valores informados para nova senha não são iguais</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var init = function() {
	
		var config = {
			  method: 'GET',
			  url: '/ws/users/'+gUserId,
			  unique: false,
			  requestId: 'profile-load'
			};

		httpSvc(config)
			.success(function(data){
				$scope.profile = { id: data.user.id
								 , name: data.user.name
								 , email: data.user.email
								 , newPassword: ""
								 , newPassword2: ""}			
				$('#profile-overlay').remove();
				$('#profile-loading-img').remove();
			});
	

	}
	
	init();
	
});