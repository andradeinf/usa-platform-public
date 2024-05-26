angular.module('franchisorLogistics').controller('MenuCtrl', function ($scope, $rootScope, httpSvc) {	
	
	var getUserMessageTopicLabels = function() {

		var configMessageTopicLabels = {
			method: 'GET',
			url: '/ws/messages/messageTopicLabel',
			unique: false,
			requestId: 'messageTopicLabels-load'
		};

		httpSvc(configMessageTopicLabels)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.userLabels = data.messageTopicLabels.filter(function(label, index, arr){
						return label.id != 0;
					});
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os marcadores do usuário!');
			});
	}

	$rootScope.updateUserMessageTopicLabels = function() {
		var millisecondsToWait = 2000;
		setTimeout(function() {
			getUserMessageTopicLabels();
		}, millisecondsToWait);	
	}
	
	var getNewMessageCount = function() {
		
		var configMessages = {
				  method: 'GET',
				  url: '/ws/messages/getNewMessageCount',
				  unique: false,
				  requestId: 'getNewMessageCount-load'
				};
		
		httpSvc(configMessages)
		.then(function successCallback(response){
			if (response.data.returnMessage.code > 0) {
				$scope.newMessageCount = response.data.newMessageCount;
				$scope.receiveMessage = response.data.receiveMessage;
			}
		}, function errorCallback(response){
			console.log('Problema ao buscar o numero de novas mensagens do usuario!');
		});
	}
	
	$rootScope.updateNewMessageCount = function() {
		var millisecondsToWait = 2000;
		setTimeout(function() {
			getNewMessageCount();
		}, millisecondsToWait);	
	}
	
	var getNewAnnouncementCount = function() {
		
		var configAnnouncements = {
				  method: 'GET',
				  url: '/ws/announcements/getNewAnnouncementCount',
				  unique: false,
				  requestId: 'getNewAnnouncementCount-load'
				};
		
		httpSvc(configAnnouncements)
		.then(function successCallback(response){
			if (response.data.returnMessage.code > 0) {
				$scope.newAnnouncementCount = response.data.newAnnouncementCount;
			}
		}, function errorCallback(response){
			console.log('Problema ao buscar o numero de novas mensagens do usuario!');
		});
	}
	
	$rootScope.updateNewAnnouncementCount = function() {
		var millisecondsToWait = 2000;
		setTimeout(function() {
			getNewAnnouncementCount();
		}, millisecondsToWait);		
	}
	
	var init = function() {	
		
		$scope.newMessageCount = 0;
		getNewMessageCount();
		
		$scope.newAnnouncementCount = 0;
		getNewAnnouncementCount();

		$scope.userLabels = [];
		getUserMessageTopicLabels();
		
		var configDomain = {
				  method: 'GET',
				  url: '/ws/configuration/getDomainConfiguration',
				  unique: false,
				  requestId: 'getDomainConfiguration-load'
				};
		
		httpSvc(configDomain)
		.then(function successCallback(response){
			if (response.data.returnMessage.code > 0) {
				$rootScope.domainConfiguration = response.data.domainConfiguration;
			}
		}, function errorCallback(response){
			console.log('Problema ao buscar as configuracoes do sistema!');
		});
	
	}
	
	init();
	
});
