angular.module('franchisorLogistics').controller('RadioVinilCtrl', function ($scope, $sce, $timeout, httpSvc) {
	
	$scope.onPlayerReady = function(API) {
		$scope.API = API;
	}

	var updateStreamSource = function() {
		$scope.radio.streamSources = [
			{
				src: $sce.trustAsResourceUrl($scope.url), type: "audio/mpeg"
			}
		];
	}

	$scope.clickCustomPlayStop = function() {
		if ($scope.API.currentState == "stop") {
			$scope.API.play();
		} else {
			$scope.API.stop();
			updateStreamSource();
		}
	}

	$scope.customPlayStopClass = function() {
    	var className = 'fa-play';
    	if ($scope.API.currentState == "play") {className = 'fa-stop';}
    	return className;
    };
	
	var init = function() {

		$scope.radio = {
			show: false,
			config: {
				preload: "none",
				theme: {
					url: "/vendor/videogular/css/videogular.min.css"
				}
			},
			streamSources: []
		}

		//get franchisor radio info
		var config = {
				  method: 'GET',
				  url: '/ws/franchisors/' + gFranchisorId + '/radio',
				  unique: false,
				  requestId: 'radio-load'
				};
		
		httpSvc(config)
			.success(function(data){
				if (data.url != null && data.url != "") {
					$scope.url = data.url;
					updateStreamSource();
					$scope.radio.show = true;
				}
				
			});
	}
	
	init();
	
});