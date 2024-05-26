angular.module('franchisorLogistics').controller('SystemUpdateInfoCtrl', function ($scope, httpSvc) {
	
	var init = function() {
		
		$scope.systemUpdateInfo = { summary: null
		         				  , details: null}
	
		//get configuration info for system update
		var config = {
				  method: 'GET',
				  url: '/ws/configuration/getSystemUpdateInfo',
				  unique: false,
				  requestId: 'configuration-load'
				};
		
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					data.systemConfigurationList.forEach(function(entry, index, array) {
						if (entry.key == 'SYSTEM_UPDATE_INFO_SUMMARY') {
							$scope.systemUpdateInfo.summary = entry;
						}
						if (entry.key == 'SYSTEM_UPDATE_INFO_DETAILS') {
							$scope.systemUpdateInfo.details = entry;
						}
					});
				}
			});	
	}
	
	init();
	
});