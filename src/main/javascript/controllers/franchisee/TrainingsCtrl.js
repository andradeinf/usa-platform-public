angular.module('franchisorLogistics').controller('TrainingsCtrl', function ($scope, $sce, httpSvc) {
	
	$scope.onVideoComplete = function(item) {
		updateVideoViewControl(item.id, 0, 'COMPLETED');
	}

	$scope.onVideoUpdate = function(item, currentTime) {
		var roundedCurrentTime = Math.floor(currentTime);
		if (roundedCurrentTime == 0) {
			$scope.trainingsViewControl[item.id] = 0;
		}
		if (roundedCurrentTime != $scope.trainingsViewControl[item.id] && roundedCurrentTime % 10 == 0) {
			$scope.trainingsViewControl[item.id] = roundedCurrentTime;
			updateVideoViewControl(item.id, roundedCurrentTime, 'PARTIAL');
		}		
	}

	var updateVideoViewControl = function(trainingId, currentTime, status) {
		var config = {
			method: 'PUT',
			url: '/ws/trainings/updateTrainingViewControl',
			data: {
				trainingId: trainingId,
				viewCurrentTime: currentTime,
				viewStatus: status
			},
			unique: true,
			requestId: 'tutorials-updateTrainingViewControl-'+status
		  };
		  
	  	httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code <= 0) {
					console.log("Error updating video view control.", data.returnMessage.message)
				}				
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					console.log("Error updating video view control.")
				}
			});	
	}

	$scope.showMoreTrainings = function() {
		$scope.pageNumber = $scope.pageNumber + 1;
	}
	
	var initializeTraining = function(training) {

		training.videoSources = [
			{
				src: $sce.trustAsResourceUrl("/ws/trainings/video/stream/" + training.id), type: training.videoContentType
			}
		];
	}
	
	var init = function() {

		$scope.trainings = [];
		$scope.trainingsViewControl = {};

		$scope.pageNumber = 1;
		$scope.pageSize = 5;

		$scope.video = {
			config: {
				preload: "none",
				theme: {
					url: "/vendor/videogular/css/videogular.min.css"
				},
				plugins: {
					controls: {
						autoHide: true,
						autoHideTime: 5000
					}
				}
			}
		};
	
		LoadingUtil.show($('#trainings-loadingArea'));
		
		var config = {
			  method: 'GET',
			  url: '/ws/trainings/franchisor/'+gFranchisorId,
			  unique: false,
			  requestId: 'trainings-load'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					data.trainings.forEach(training => {
						initializeTraining(training);
					});
					$scope.trainings = data.trainings;
				}  else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}
				
				LoadingUtil.hide($('#trainings-loadingArea'));
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}
			});
	}
	
	init();
});