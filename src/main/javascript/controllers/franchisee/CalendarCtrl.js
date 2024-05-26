angular.module('franchisorLogistics').controller('CalendarCtrl', function ($scope, httpSvc) {
	
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
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados!');
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados!');
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
				open: false
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
	
	$scope.toggleEventDetails = function(event){
		$scope.calendarEventsAuxData[event.id].open = !$scope.calendarEventsAuxData[event.id].open;
	}
	
	var init = function() {
		
		$scope.months = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
		
		$scope.monthOffset = 0;
		$scope.selectedDate = undefined;
		
		$scope.loadCalendarMonth();

	}
	
	init();
	
});