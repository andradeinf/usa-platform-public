angular.module('franchisorLogisticsPrint', ['textAngular'])
.controller('DeliveryRequestsPrintCtrl', function ($scope, $window) {
	
    $scope.print = function() {
    	$window.print();
    }
    
    $scope.calculateTotal = function() {
    	var total = 0;
    	
    	$scope.printRequestItems.forEach(function(item, index, array) {
    		total += item.calculateDelivery();
    	});
    	
    	return total;
    }
	
	var init = function() {
    	$scope.franchisee = $window.franchisee;
    	$scope.printRequestItems = $window.printRequestItems;
    	$scope.deliveryRequestsCities = $window.deliveryRequestsCities;
    	$scope.deliveryRequestsStates = $window.deliveryRequestsStates;
	}
	
	init();
	
});
