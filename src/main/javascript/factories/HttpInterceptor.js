angular.module('franchisorLogistics').factory('HttpInterceptor', function ($q, $rootScope, $log, $window) {

    var numLoadings = 0;

    return {
        request: function (config) {

            numLoadings++;

            // Show loader
            $rootScope.$broadcast("loader_show");
            return config || $q.when(config)

        },
        response: function (response) {

            if ((--numLoadings) === 0) {
                // Hide loader
                $rootScope.$broadcast("loader_hide");
            }

            return response || $q.when(response);

        },
        responseError: function (rejection) {

            if (!(--numLoadings)) {
                // Hide loader
                $rootScope.$broadcast("loader_hide");
            }
            
            if (rejection.status === 401) {
                console.log("Response Error 401",rejection);
                $window.location.href = '/login?expired=Y';
            }

            return $q.reject(rejection);
        }
    };
})
.config(function ($httpProvider) {
    $httpProvider.interceptors.push('HttpInterceptor');
});
