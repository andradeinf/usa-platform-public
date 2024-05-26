angular.module('franchisorLogistics').controller('FranchisorCtrl', function ($scope, $rootScope, httpSvc) {
	
	var init = function() {
	
		//initialize franchisor data
		var franchisorConfig = {
				  method: 'GET',
				  url: '/ws/franchisors/'+gFranchisorId,
				  unique: false,
				  requestId: 'franchisor-load'
				};
		
		httpSvc(franchisorConfig)
			.success(function(data){
				$scope.franchisor = data;
				$rootScope.maxStorageSizeMB = data.maxStorageSizeMB;
				$('#franchisor-overlay').remove();
				$('#franchisor-loading-img').remove();
			});
	
	}
	
	init();
	
});