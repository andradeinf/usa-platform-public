angular.module('franchisorLogistics').controller('FranchiseeCtrl', function ($scope, httpSvc) {
	
	var init = function() {
	
		//initialize franchisee data
		var franchiseeConfig = {
			  method: 'GET' ,
			  url: '/ws/franchisees/'+gFranchiseeId,
			  unique: false,
			  requestId: 'franchisee-load'
			};
		
		httpSvc(franchiseeConfig)
			.success(function(data){
				$scope.franchisee = data;
				$('#franchisee-overlay').remove();
				$('#franchisee-loading-img').remove();
			});
	
	}
	
	init();
	
});