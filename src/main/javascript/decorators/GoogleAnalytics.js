angular.module('franchisorLogistics').config(['AnalyticsProvider', function (AnalyticsProvider) {
   // Add configuration code as desired
   AnalyticsProvider
   		.setAccount('UA-133310043-1')
   		.readFromRoute(true);
}]).run(['Analytics', function(Analytics) { }]);