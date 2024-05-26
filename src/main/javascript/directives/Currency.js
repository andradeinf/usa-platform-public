angular.module('franchisorLogistics').directive('currency', function() {
	
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attr, ngModel) {

            function fromUser(text) {
            	text = text.toString();
            	text = text.replace(/["."]/g, "").replace(/[","]/g, ".");
            	
            	var integerPart;
            	var decimalPart;
            	var commaIndex = text.indexOf(".");
            	if( commaIndex == -1 ) {
            		integerPart = text;
            		decimalPart = "0000";
            	} else {
            		integerPart = text.substring(0, commaIndex);
            		decimalPart = text.substring(commaIndex + 1) + "0000";
            	}
            	text = integerPart + "." + decimalPart.substring(0, 4);
            	return text;
            }

            function toUser(text) {
            	
            	if (text) {
	            	text = text.toString();
	            	text = text.replace(/["."]/g, ",");
            	} else {
            		text = "0,0000"
            	}
            	return formatCurrencyValue(text);
            }
            
            ngModel.$parsers.push(fromUser);
            ngModel.$formatters.push(toUser);
        }
    };
    
});