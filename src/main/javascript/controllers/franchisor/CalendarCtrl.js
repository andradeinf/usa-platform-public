angular.module('franchisorLogistics').controller('CalendarCtrl', function ($scope, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.accessClass = function(item){
		var className = '';
		
		if (item.accessRestricted) {
			className = 'fa-lock';
		} else if (!item.accessRestricted && item.franchiseeIds && item.franchiseeIds.length > 0) {
			className = 'fa-unlock-alt';
		} else if (!item.accessRestricted && (!(item.franchiseeIds) || item.franchiseeIds.length == 0)) {
			className = 'fa-unlock';
		}
		
		return className;
	}
	
	$scope.dayClasses = function(item){
    	var className = '';
    	
    	if (item == $scope.selectedDate) {
    		className += ' bg-info';
    	} else if (item.day == $scope.monthDefinition.currentDay && item.month == $scope.monthDefinition.currentMonth && item.year == $scope.monthDefinition.currentYear) {
    		className += ' bg-danger';
    	} else {
    		className += item.type;
    	}
    	
    	return className;
    };
	
	$scope.save = function(){
		if (validate()) {
			
			//update list of selected franchisees
			$scope.calendarEvent.franchiseeIds = [];
			$scope.franchisees.forEach(function(entry) {
	        	if (entry.selected) {
	        		$scope.calendarEvent.franchiseeIds.push(entry.id);
	        	}
			});
			
			/*
			 * Event date changes
			 */
			var fromDate = new Date($scope.calendarEvent.fromHour);
			var toDate = new Date($scope.calendarEvent.toHour);
			var eventDate = $scope.calendarEventsAuxData[$scope.calendarEvent.id].eventDate;
			
			var originalEventsKey = fromDate.getFullYear()+"-"+fromDate.getMonth()+"-"+fromDate.getDate();
			var newEventsKey = eventDate.getFullYear()+"-"+eventDate.getMonth()+"-"+eventDate.getDate();
			
			//update event dates
			fromDate.setDate(eventDate.getDate());
			fromDate.setMonth(eventDate.getMonth());
			fromDate.setFullYear(eventDate.getFullYear());	
			$scope.calendarEvent.fromHour = fromDate.getTime();
			
			toDate.setDate(eventDate.getDate());
			toDate.setMonth(eventDate.getMonth());
			toDate.setFullYear(eventDate.getFullYear());
			$scope.calendarEvent.toHour = toDate.getTime();
		
			var config = {
	  			  method: ($scope.calendarEvent.id == 0 ? 'POST' : 'PUT') ,
	  			  url: '/ws/calendar',
	  			  data: $scope.calendarEvent,
	  			  unique: true,
	  			  requestId: 'calendarOrUpdateEvent-save'
	  			};
		
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						
						if ($scope.calendarEvent.id == 0) {
							//add new event if in current calendar
							if ($scope.calendarEvents[newEventsKey]) {
								$scope.calendarEvents[newEventsKey].push(data.calendarEvent);
								initializeEvent(data.calendarEvent);
							}
						} else {
							//change updated event location if date changed
							if (originalEventsKey != newEventsKey) {
								
								//add existing event to new date if in current calendar
								if ($scope.calendarEvents[newEventsKey]) {
									$scope.calendarEvents[newEventsKey].push($scope.calendarEvent);
								}

								//remove event from old date
								var index = $scope.calendarEvents[originalEventsKey].indexOf($scope.calendarEvent);
								$scope.calendarEvents[originalEventsKey].splice(index, 1);
							}
						}
						
						$('#newCalendarEventModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Evento criado com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar o evento.');
					}
				});	
		};
	}

	$scope.deleteEvent = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir o evento?', doDeleteEvent, 'Não', 'Sim');
	}
		
	var doDeleteEvent = function(){
		
		var config = {
					method: 'DELETE' ,
					url: '/ws/calendar/'+$scope.itemToRemove.id,
					unique: true,
					requestId: 'calendarEvent-delete'
				};
		
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					var eventKey = $scope.selectedDate.year+'-'+$scope.selectedDate.month+'-'+$scope.selectedDate.day;
					var index = $scope.calendarEvents[eventKey].indexOf($scope.itemToRemove);
					$scope.calendarEvents[eventKey].splice(index, 1);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Evento excluído com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o evento!');
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o evento!');
				}
			});
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#date').removeClass(errorClass);
		$('#title').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.calendarEventsAuxData[$scope.calendarEvent.id].eventDate == null ||
				$scope.calendarEvent.fromHour == null ||
				$scope.calendarEvent.toHour == null) {
			$('#date').addClass(errorClass);
			result = false;
			errorMsg += "<li>Informe a data e hora do evento</li>";
		}
		
		if ($scope.calendarEvent.title == "") {
			$('#title').addClass(errorClass);
			result = false;
			errorMsg += "<li>Informe o título do evento</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		
		$scope.calendarEvent = { id: 0
							, allDay: false
							, fromHour: new Date($scope.selectedDate.year, $scope.selectedDate.month, $scope.selectedDate.day)
							, toHour: new Date($scope.selectedDate.year, $scope.selectedDate.month, $scope.selectedDate.day)
							, title: ''
							, location: ''
							, description: ''
							, accessRestricted: false
							, franchiseeIds: []}
		
		$scope.franchisees.forEach(function(entry) {
			entry.selected = false;
		});
		
		$scope.calendarEvent.toHour.setHours($scope.calendarEvent.toHour.getHours() + 1);
		
		initializeEvent($scope.calendarEvent);
		
		//clear validation
		resetValidation();								 	
	}
	
	$scope.new = function() {
		reset();
	}
	
	$scope.editEvent = function(event) {
		$scope.franchisees.forEach(function(entry) {
			entry.selected = false;
		});
		
		$scope.calendarEvent = event;
		if ($scope.calendarEvent.franchiseeIds) {
			$scope.calendarEvent.franchiseeIds.forEach(function(franchiseeId) {
				$scope.franchiseesMap[franchiseeId].selected = true;
			});
		}			
		
		//clear validation
		resetValidation();
	}
	
	$scope.today = function() {
		$scope.monthOffset = 0;
		$scope.selectedDate = undefined;
		$scope.loadCalendarMonth();
	}
	
	$scope.previousMonth = function() {
		$scope.monthOffset -= 1;
		$scope.loadCalendarMonth();
	}
	
	$scope.nextMonth = function() {
		$scope.monthOffset += 1;
		$scope.loadCalendarMonth();
	}
	
	$scope.loadCalendarMonth = function() {
		
		var configCalendar = {
				  method: 'GET',
				  url: '/ws/calendar/monthOffset/'+$scope.monthOffset,
				  unique: false,
				  requestId: 'calendar-load'
				};

		httpSvc(configCalendar)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.monthDefinition = data.monthDefinition;
					$scope.calendarEvents = data.calendarEvents;
					$scope.calendarEventsAuxData = {};
					$scope.monthPresentation = [];
					
					//previousMonth
					var monthPresentationLine = [];
					$scope.monthPresentation.push(monthPresentationLine);
					for (var i=0; i < $scope.monthDefinition.previousMonth.days.length; i++) {
						monthPresentationLine.push(initializeDate('previous-month', i));
					}
					
					//selectedMonth
					monthPresentationLine = $scope.monthPresentation[$scope.monthPresentation.length-1];
					for (var i=0; i < $scope.monthDefinition.selectedMonth.days.length; i++) {
						if (monthPresentationLine.length == 7) {
							monthPresentationLine = [];
							$scope.monthPresentation.push(monthPresentationLine);
						}
						monthPresentationLine.push(initializeDate('selected-month', i));
					}
					
					//nextMonth
					monthPresentationLine = $scope.monthPresentation[$scope.monthPresentation.length-1];
					for (var i=0; i < $scope.monthDefinition.nextMonth.days.length; i++) {
						if (monthPresentationLine.length == 7) {
							monthPresentationLine = [];
							$scope.monthPresentation.push(monthPresentationLine);
						}
						monthPresentationLine.push(initializeDate('next-month', i));
					}
					
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os eventos!');
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os eventos!');
			});
		
	}
	
	var initializeDate = function(type, dayIndex) {
		var dateInfo = {}
		
		dateInfo.type = type;
		
		if (type == 'previous-month') {
			dateInfo.day = $scope.monthDefinition.previousMonth.days[dayIndex];
			dateInfo.month = $scope.monthDefinition.previousMonth.month;
			dateInfo.year = $scope.monthDefinition.previousMonth.year;
		} else if (type == 'selected-month') {
			dateInfo.day = $scope.monthDefinition.selectedMonth.days[dayIndex];
			dateInfo.month = $scope.monthDefinition.selectedMonth.month;
			dateInfo.year = $scope.monthDefinition.selectedMonth.year
		} else if (type == 'next-month') {
			dateInfo.day = $scope.monthDefinition.nextMonth.days[dayIndex];
			dateInfo.month = $scope.monthDefinition.nextMonth.month;
			dateInfo.year = $scope.monthDefinition.nextMonth.year
		}
		
		var eventsKey = dateInfo.year+"-"+dateInfo.month+"-"+dateInfo.day;
		if (!$scope.calendarEvents[eventsKey]) {
			$scope.calendarEvents[eventsKey] = [];
		}
		
		dateInfo.events = $scope.calendarEvents[eventsKey];
		dateInfo.events.forEach(function(event) {
			initializeEvent(event);
		});
		
		if ($scope.selectedDate == undefined) {
			//set current date as selected date
			if (dateInfo.day == $scope.monthDefinition.currentDay &&
					dateInfo.month == $scope.monthDefinition.currentMonth &&
					dateInfo.year == $scope.monthDefinition.currentYear) {
				$scope.selectedDate = dateInfo;
			}
		} else {
			//update reference to selected date
			if (dateInfo.day == $scope.selectedDate.day &&
					dateInfo.month == $scope.selectedDate.month &&
					dateInfo.year == $scope.selectedDate.year) {
				$scope.selectedDate = dateInfo;
			}
		}
		
		return dateInfo;
	}
	
	var initializeEvent = function(event){
		$scope.calendarEventsAuxData[event.id] = {
				open: false,
				eventDate: new Date(event.fromHour),
				eventDateOpened: false
			}
	}
	
	$scope.openDatePicker = function() {
	    $scope.calendarEventsAuxData[$scope.calendarEvent.id].eventDateOpened = true;
	};
	
	$scope.dateClick = function(item) {
		$scope.selectedDate = item;
	}
	
	$scope.range = function(min, max, step) {
	    step = step || 1;
	    var input = [];
	    for (var i = min; i < max; i += step) {
	        input.push(i);
	    }
	    return input;
	};
	
	$scope.allDayClick = function() {
		
		if ($scope.calendarEvent.allDay) {
			
			if ($scope.calendarEvent.fromHour == null) {
				$scope.calendarEvent.fromHour = new Date();
			} else {
				$scope.calendarEvent.fromHour = new Date($scope.calendarEvent.fromHour);
			}
			
			$scope.calendarEvent.fromHour.setHours(0);
			$scope.calendarEvent.fromHour.setMinutes(0);
			$scope.calendarEvent.fromHour.setSeconds(0);
			//need to recreate to refresh the UI component
			$scope.calendarEvent.fromHour = new Date($scope.calendarEvent.fromHour);
			
			if ($scope.calendarEvent.toHour == null) {
				$scope.calendarEvent.toHour = new Date();
			} else {
				$scope.calendarEvent.toHour = new Date($scope.calendarEvent.toHour);
			}
			
			$scope.calendarEvent.toHour.setHours(23);
			$scope.calendarEvent.toHour.setMinutes(59);
			$scope.calendarEvent.toHour.setSeconds(59);
			//need to recreate to refresh the UI component
			$scope.calendarEvent.toHour = new Date($scope.calendarEvent.toHour);
		} 
		
	}
	
	$scope.toggleEventDetails = function(event){
		$scope.calendarEventsAuxData[event.id].open = !$scope.calendarEventsAuxData[event.id].open;
	}
	
	var init = function() {
		
		$scope.months = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
		
		$scope.monthOffset = 0;
		$scope.selectedDate = undefined;
		
		$scope.loadCalendarMonth();

		var configFranchisees = {
			  method: 'GET' ,
			  url: '/ws/franchisees/franchisor/' + gFranchisorId,
			  unique: false,
			  requestId: 'franchisees-load'
			};
		
		httpSvc(configFranchisees)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.franchisees = data.franchisees;
					$scope.franchiseesMap = [];
					$scope.franchisees.forEach(function(entry) {
						$scope.franchiseesMap[entry.id] = entry;				
					});
					
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}	
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