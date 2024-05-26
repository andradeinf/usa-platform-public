angular.module('franchisorLogistics').config(function($provide) {
    $provide.factory('httpSvc', function($http, $q) {

        var uniqueRequestOptionName = "unique";
        var requestIdOptionName = 'requestId';
        
        var DUPLICATED_REQUEST_STATUS_CODE = 499; // I just made it up - nothing special 
        var EMPTY_BODY = '';
        var EMPTY_HEADERS = {};
        
        // should we care about duplicates check
        function checkForDuplicates(requestConfig) {
            return !!requestConfig[uniqueRequestOptionName];
        }

        // find identical request in pending requests
        function checkIfDuplicated(requestConfig) {
            var duplicated = $http.pendingRequests.filter(function(pendingReqConfig) {
                return pendingReqConfig[requestIdOptionName] && pendingReqConfig[requestIdOptionName] === requestConfig[requestIdOptionName];
            });
            return duplicated.length > 0;
        }
        
        function buildRejectedRequestPromise(requestConfig) {
            var dfd = $q.defer();
            
            dfd.promise.success = function (fn) {
                dfd.promise.then(fn);
                return dfd.promise;
            };
            
            dfd.promise.error = function (fn) {
                dfd.promise.then(null, fn);
                return dfd.promise;
            };
            
            // build response for duplicated request
            var response = {data: EMPTY_BODY, headers: EMPTY_HEADERS, status: DUPLICATED_REQUEST_STATUS_CODE, config: requestConfig};
            
            // reject promise with response above
            dfd.reject(response);
            return dfd.promise;
        }

        return function(requestConfig) {
            
            if(checkForDuplicates(requestConfig) && checkIfDuplicated(requestConfig)) {
                // return rejected promise with response consistent with those from $http calls
                return buildRejectedRequestPromise(requestConfig);
            }
            return $http(requestConfig);
        };
    });
})