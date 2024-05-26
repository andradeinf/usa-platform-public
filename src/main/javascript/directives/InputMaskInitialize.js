angular.module('franchisorLogistics').directive('inputMaskInitialize', function () {
	
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.inputmask();
        }
    };
    
});