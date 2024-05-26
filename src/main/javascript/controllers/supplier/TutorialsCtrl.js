angular.module('franchisorLogistics').controller('TutorialsCtrl', function ($scope, httpSvc) {
	
	var init = function() {
		
		LoadingUtil.show($('#tutorials-loadingArea'));
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/tutorials',
			  unique: false,
			  requestId: 'tutorials-load'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.tutorials = data.tutorials;
				}  else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}	
				
				LoadingUtil.hide($('#tutorials-loadingArea'));
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}
			});
	}
	
	init();
});