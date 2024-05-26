<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- List of Calendar Events -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-calendar"></i> <c:out value="${domainConfiguration.labels['CALENDAR']}">Calendário</c:out></h3>
</div>
<div class="panel panel-default">
	<div class="panel-heading clearfix">
    	<div class="btn-group pull-left" role="group">
    		<button type="button" class="btn btn-default" ng-click="today()" title="Hoje"><i class="fa fa-calendar-check-o"></i></button>
    		<button type="button" class="btn btn-default" ng-click="loadCalendarMonth()" title="Recarregar"><i class="fa fa-repeat"></i></button>
    		<button type="button" class="btn btn-default" ng-click="previousMonth()" title="Mês Anterior"><i class="fa fa-chevron-left"></i></button>
    		<button type="button" class="btn btn-default" ng-click="nextMonth()" title="Próximo Anterior"><i class="fa fa-chevron-right"></i></button>
    	</div>
    </div>
   	<div class="panel-body">
   	
   		<h1 class="calendar-header">{{months[monthDefinition.selectedMonth.month]}} - {{monthDefinition.selectedMonth.year}}</h1>
   		
   		<table class="table table-bordered calendar">
            <thead>
                <tr>
                    <th>Dom</th>
                    <th>Seg</th>
                    <th>Ter</th>
                    <th>Qua</th>						                            
                    <th>Qui</th>
                    <th>Sex</th>
                    <th>Sab</th>
                </tr>
            </thead>
            <tbody class="pointer-cursor">
				<tr ng-repeat="row in range(0,monthPresentation.length)">
                    <td class="day" ng-repeat="cel in range(0,monthPresentation[row].length)" ng-class="dayClasses(monthPresentation[row][cel])" ng-click="dateClick(monthPresentation[row][cel])">
                    	<div class="day-title">{{monthPresentation[row][cel].day}}</div>
                    	<div class="event"ng-repeat="event in monthPresentation[row][cel].events | orderBy:'fromHour'">{{event.allDay ? 'Dia todo' : event.fromHour | date:'HH:mm'}}: {{event.title}}</div>
                    </td>
                </tr>
            </tbody>
    	</table>
   		
   		<div class="day-events">
   			<h1>Eventos do dia - {{selectedDate.day}}/{{selectedDate.month + 1}}/{{selectedDate.year}}</h1>
   			<p class="info text-info text-center" ng-show="selectedDate.events.length == 0">Não existem eventos agendados para esta data...</p>
	   		<ul class="list-group event-list">
		   		<li class="list-group-item event-list-item" ng-click="" ng-repeat="event in selectedDate.events | orderBy:'fromHour'">
		   			<div class="title">
		   				<a class="toggle" href ng-click="toggleEventDetails(event)">
							<i class="fa fa-plus-square-o" ng-show="!calendarEventsAuxData[event.id].open"></i>
	                        <i class="fa fa-minus-square-o" ng-show="calendarEventsAuxData[event.id].open"></i>
	                    </a>
		   				<span ng-show="event.allDay"> Dia todo: </span>
			   			<span ng-hide="event.allDay"> {{event.fromHour | date:'HH:mm'}} - {{event.toHour | date:'HH:mm'}}: </span>
			   			{{event.title}}
		   			</div>		   			
		   			<div class="container-fluid details" ng-show="calendarEventsAuxData[event.id].open">
						<div class="row">
							<label class="col-md-2 view-form-label">Local:</label>
							<p class="col-md-10 view-form-field">{{event.location}}</p>
		   				</div>
		   				<div class="row">
							<label class="col-md-2 view-form-label">Detalhes:</label>
							<p class="col-md-10 view-form-field view-form-field-double">{{event.description}}</p>
		   				</div>
		   			</div>
		   		</li>
		   	</ul>
   		
   		</div>
   	</div>
	<!-- Loading -->
	<div id="calendarLoad"></div>
    <!-- end loading -->
    <div class="panel-footer clearfix">
    	<div class="btn-group pull-left" role="group">
    		<button type="button" class="btn btn-default" ng-click="today()" title="Hoje"><i class="fa fa-calendar-check-o"></i></button>
    		<button type="button" class="btn btn-default" ng-click="loadCalendarMonth()" title="Recarregar"><i class="fa fa-repeat"></i></button>
    		<button type="button" class="btn btn-default" ng-click="previousMonth()" title="Mês Anterior"><i class="fa fa-chevron-left"></i></button>
    		<button type="button" class="btn btn-default" ng-click="nextMonth()" title="Próximo Anterior"><i class="fa fa-chevron-right"></i></button>
    	</div>
    </div>
</div>