angular.module('franchisorLogistics').controller('MainCtrl', function ($scope, $location, httpSvc) {	
	
	var onlyUnique = function(value, index, self) { 
        return self.indexOf(value) === index;
    }
    
    $scope.messageTopicBadgeCount = function(messageTopicsUnreadNotifications) {
    	if (messageTopicsUnreadNotifications == undefined) {
    		return 0;
    	} else {
    		// Use "map" to extract only the comment ID and the filter to count only unique comment ids
    		return messageTopicsUnreadNotifications.map(notification => notification.referenceId2).filter(onlyUnique).length;
    	}    	
    };
	
	$scope.messageClick = function() {
		$location.path("/messages/0");
	}
	
	$scope.announcementClick = function() {
		$location.path("/announcements");
	}
	
	$scope.calendarClick = function() {
		$location.path("/calendar");
	}
	
	var loadMessages = function() {
		
		LoadingUtil.show($("#messageTopicsLoad"));
		
		//Load messages		
		var configMessages = {
			  method: 'GET',
			  url: '/ws/messages/userGroupId/0/paged/1/pageSize/5?label=0',
			  unique: false,
			  requestId: 'messageTopics-load'
			};

		httpSvc(configMessages)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					$scope.messageTopics = data.messageTopics;
					
					$scope.messageTopicsUnreadNotifications = data.messageTopicsUnreadNotifications;
					$scope.entities = data.entities;
					$scope.userGroups = data.userGroups;
					
					Object.keys($scope.messageTopicsUnreadNotifications).forEach(function(key) {
						$scope.messageTopicsUnreadNotifications[key].forEach(function(notification) {
							$scope.messageCommentsUnreadNotifications[notification.referenceId2] = notification;
						});
					});
							
					LoadingUtil.hide($("#messageTopicsLoad"));
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar as mensagens.');
				}
			});
	}
	
	var loadAnnouncements = function() {
		LoadingUtil.show($("#announcemensLoad"));
		
		//get announcements
		var announcementsConfig = {
			  method: 'GET',
			  url: '/ws/announcements/franchisor/'+gFranchisorId+'/paged/1/pageSize/5',
			  unique: false,
			  requestId: 'announcements-load'
			};
			
		httpSvc(announcementsConfig)
		.success(function(data){
			if (data.returnMessage.code > 0) {
				$scope.announcements = data.announcementList;
				$scope.announcementUnreadNotifications = data.announcementUnreadNotifications;
				
				LoadingUtil.hide($("#announcemensLoad"));
			} else {
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os avisos!');
			}	
		})
		.error(function(data){
			//499 - duplicated request
			if (data.status != 499) {
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os avisos!');
			}
		});
	}
	
	var loadCalendarEvents = function() {
		LoadingUtil.show($("#calendarEventsLoad"));
		
		//get announcements
		var calendarEventsConfig = {
			  method: 'GET',
			  url: '/ws/calendar/nextEvents/4',
			  unique: false,
			  requestId: 'calendarEvents-load'
			};
			
		httpSvc(calendarEventsConfig)
		.success(function(data){
			if (data.returnMessage.code > 0) {
				$scope.calendarEvents = data.calendarEvents;
				
				LoadingUtil.hide($("#calendarEventsLoad"));
			} else {
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os próximos eventos!');
			}	
		})
		.error(function(data){
			//499 - duplicated request
			if (data.status != 499) {
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os próximos eventos!');
			}
		});
	}
	
	var init = function() {	
		
		$scope.messageTopics = [];
		$scope.toEntities = [];
		$scope.toUserGroups = [];
		$scope.messageCommentsUnreadNotifications = {}
		
		$scope.announcements = [];
		
		$scope.calendarEvents = [];
		
		loadMessages();
		loadAnnouncements();
		loadCalendarEvents();
	}
	
	init();
	
});