angular.module('franchisorLogistics').controller('AnnouncementsCtrl', function ($scope, $rootScope, httpSvc) {
	
	var markAsRead = function() {
		
		var notificationIds = [];
		Object.keys($scope.announcementUnreadNotifications).forEach(function(key) {
			notificationIds.push($scope.announcementUnreadNotifications[key].id);
		});

		var config = {
	  			  method: 'PUT',
	  			  url: '/ws/announcements/markAsRead',
	  			  data: notificationIds,
	  			  unique: true,
	  			  requestId: 'announcements-markAsRead'
	  			};
		
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {						
						//update unread messages badge in the menu
						$rootScope.updateNewAnnouncementCount();						
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao marcar os avisos lidos!');
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao marcar os avisos lidos!');
					}
				});
	}
	
	$scope.previousAnnouncemens = function() {
		
		$scope.page -= 1;
		$scope.fetchAnnouncements();
		
	}
	
	$scope.nextAnnouncemens = function() {
		
		$scope.page += 1;
		$scope.fetchAnnouncements();
		
	}
	
	$scope.toggleAnnouncementDetails = function(item){
		item.collapsed = !item.collapsed;
	}
	
	$scope.fetchAnnouncements = function() {
		
		LoadingUtil.show($("#announcemensLoad"));
		
		//get announcements
		var announcementsConfig = {
			  method: 'GET',
			  url: '/ws/announcements/franchisor/'+gFranchisorId+'/paged/'+$scope.page+'/pageSize/'+$scope.pageSize,
			  unique: false,
			  requestId: 'announcements-load'
			};
			
		httpSvc(announcementsConfig)
		.success(function(data){
			if (data.returnMessage.code > 0) {
				$scope.announcements = data.announcementList;
				$scope.nextEnabled = data.hasMore;
				$scope.announcementUnreadNotifications = data.announcementUnreadNotifications;
				
				$scope.announcements.forEach(function(announcement) {
					announcement.collapsed = !($scope.announcementUnreadNotifications[announcement.id] != undefined);
				});
				
				LoadingUtil.hide($("#announcemensLoad"));
				
				markAsRead();
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
	
	$scope.setPageSize = function (size) {
		$scope.pageSize = size;
		$scope.page = 1;
		$scope.fetchAnnouncements();
	}
	
	var init = function() {
		
		//announcements
		$scope.page = 1;
		$scope.pageSize = 10;
		$scope.announcements = [];
		
		$scope.fetchAnnouncements();

	}
	
	init();
	
});