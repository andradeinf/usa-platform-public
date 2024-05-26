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
	
	$scope.rateClass = function(field, reference){
    	var className = 'pointer-cursor fa ';
    	if ($scope.reviewRequest && $scope.reviewRequest.reviewRate[field] >= reference) {
    		className += 'fa-star';
    	} else {
    		className += 'fa-star-o';
    	}
    	return className;
    };
    
    $scope.setRate = function(field, value){
    	if ($scope.reviewRequest.reviewRate[field] == value) {
    		$scope.reviewRequest.reviewRate[field] = 0;
    	} else {
    		$scope.reviewRequest.reviewRate[field] = value;
    	}
    }
	
	$scope.evaluateSupplierReviewRequest = function() {
		$scope.reviewRequest.open = true;
	}
	
	$scope.sendSupplierReviewRequest = function() {

		var config = {
			  method: 'PUT',
			  url: '/ws/review/addRequestedSupplierReviewRequestRates/'+$scope.reviewRequest.data.id,
			  data: $scope.reviewRequest.reviewRate,
			  unique: true,
			  requestId: 'supplierReviewRequest-addRates'
			};
    	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.reviewRequest = undefined;
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Obrigado pela sua opinião!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Desculpe, ocorreu um problema ao enviar a sua opinião: ' + data.returnMessage.message);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Desculpe, ocorreu um problema ao enviar a sua opinião!');
				}
			});	 
	}
	
	$scope.cancelSupplierReviewRequest = function() {
		
		var cancelSupplierReviewRequests = {
		  method: 'PUT',
		  url: '/ws/review/cancelRequestedSupplierReviewRequest/'+$scope.reviewRequest.data.id,
		  unique: true,
		  requestId: 'supplierReviewRequest-cancel'
		};
		
		httpSvc(cancelSupplierReviewRequests)
		.success(function(data){
			console.log(data.returnMessage);
		})
		.error(function(data){
			console.log(data.returnMessage);
		});
		
		$scope.reviewRequest = undefined;
	}
	
	$scope.getReviewRequestDetails = function() {
		
		if ($scope.reviewRequest.details.deliveryRequests.length == 0) {
			LoadingUtil.show($("#reviewRequestDetailsLoad"));
			
			//Load messages		
			var configMessages = {
				  method: 'GET',
				  url: '/ws/review/getReviewRequestDetails/'+$scope.reviewRequest.data.id,
				  unique: false,
				  requestId: 'supplierReviewRequestDetails-load'
				};

			httpSvc(configMessages)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						
						data.deliveryRequests.forEach(function(entry) {
							$scope.reviewRequest.details.deliveryRequests.push(entry);
							
							entry.product = data.products[entry.productId];
							entry.product.sizes.forEach(function(size) {
								if (size.id == entry.productSizeId) {
									entry.productSize = size;
								}
							});	
							entry.calculateTotal = function() {return entry.quantity * entry.deliveryUnitPrice};
						});	
								
						LoadingUtil.hide($("#reviewRequestDetailsLoad"));
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os detalhes dos pedidos.');
					}
				});
		}
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
	
	var loadSupplierReviewRequests = function() {
		
		//get supplier review requests
		var supplierReviewRequestsConfig = {
			  method: 'GET',
			  url: '/ws/review/getNextRequestedSupplierReviewRequests/'+gFranchiseeId,
			  unique: false,
			  requestId: 'supplierReviewRequests-load'
			};
			
		httpSvc(supplierReviewRequestsConfig)
		.success(function(data){
			if (data.returnMessage.code > 0) {
				if (data.reviewRequest != null) {
					$scope.reviewRequest = {
						data: data.reviewRequest,
						supplier:  data.supplier,
						reviewRate : {
							quality: 0,
							delivery: 0,
							price: 0,
							paymentCondition: 0
						},
						details: {
							deliveryRequests: []
						},
						open: false
					};
				}
				
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
		
		loadSupplierReviewRequests();
	}
	
	init();
	
});