<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- New CalendarEntry Modal -->
<div class="modal fade" id="newCalendarEventModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Novo evento</h4>
	        </div>
	        <div class="modal-body">
		        <div class="form-horizontal">
		        	<div class="form-group" id="date">
		        		<label class="col-md-1 control-label">Data:</label>
		        		<div class="col-md-3">
							<div class="single-line">
				        		<div class="input-group">
				        			<input type="text" class="form-control" uib-datepicker-popup="dd/MM/yyyy" ng-model="calendarEventsAuxData[calendarEvent.id].eventDate" is-open="calendarEventsAuxData[calendarEvent.id].eventDateOpened" datepicker-options="dateOptions" show-button-bar="False" alt-input-formats="altInputFormats" on-open-focus="False" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
				        			<span class="input-group-btn">
							        	<button type="button" class="btn btn-default" ng-click="openDatePicker()"><i class="glyphicon glyphicon-calendar" style="padding: 3px;"></i></button>
							        </span>
				        		</div>
				        	</div>
				        </div>
				        <label class="col-md-1 control-label">Hora:</label>
				        <div class="col-md-4">
			        		<div class="single-line">
			        			<p class="form-control-static">das</p>
			        		</div>
			        		<div class="single-line">
			        			<div uib-timepicker ng-model="calendarEvent.fromHour" ng-disabled="calendarEvent.allDay" show-meridian="false" show-spinners="false"></div>
			        		</div>
			        		<div class="single-line">
			        			<p class="form-control-static">às</p>
			        		</div>
		        			<div class="single-line" uib-timepicker ng-model="calendarEvent.toHour" ng-disabled="calendarEvent.allDay" show-meridian="false" show-spinners="false"></div>
			        	</div>
			        	<label class="col-md-1 control-label">ou</label>
			        	<div class="col-md-2">
			        		<div class="checkbox">
								<label><input type="checkbox" ng-model="calendarEvent.allDay" ng-click="allDayClick()"> Todo o dia</label>
							</div>
			        	</div>
		        	</div>
		        	<div class="form-group" id="title">
						<label class="col-sm-1 control-label">Título:</label>
						<div class="col-sm-11">
							<input type="text" class="form-control" ng-model="calendarEvent.title" placeholder="Título do evento">
						</div>
					</div>	
					<div class="form-group" id="location">
						<label class="col-sm-1 control-label">Local:</label>
						<div class="col-sm-11">
							<input type="text" class="form-control" ng-model="calendarEvent.location" placeholder="Local do evento">
						</div>
					</div>
		        </div>
				<form>
		            <div class="form-group" id="description">
		            	<label>Descrição:</label>
						<textarea class="form-control" ng-model="calendarEvent.description" rows="10" placeholder="Descrição do evento"></textarea>
					</div>
					<div class="form-group" id="restrictions">
		            	<label>Visível para:</label>
		            	<ul class="list-unstyled">
		                	<li><input type="radio" ng-model="calendarEvent.accessRestricted" ng-value="false"> <c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISOR_FRANCHISEES']}">Franqueador e Franqueados</c:out></li>
		                	<li><input type="radio" ng-model="calendarEvent.accessRestricted" ng-value="true"> <c:out value="${domainConfiguration.labels['VISIBILITY_ONLY_FRANCHISOR']}">Apenas Franqueador</c:out></li>
		                </ul>
		            </div>
		            <div class="form-group" id="restrictions" ng-hide="calendarEvent.accessRestricted">
		                <label><c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISEES_FILTER_CALENDAR']}">Restringir evento apenas para os franqueados selecionados</c:out>:</label>
						<div class="row">
							<div class="col-sm-4" ng-repeat="item in franchisees | orderBy:'name'"><input type="checkbox" ng-model="item.selected" ng-value="item.id"> {{item.name}}</div>
						</div>
						<p class="help-block"><c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISEES_WARNING_CALENDAR']}">Caso nenhum franqueado seja selecionado, todos poderão visualizar esse evento</c:out>.</p>
		            </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Cancelar</i></button>
    			<button ng-click="save()" class="btn btn-default"><i class="fa fa-save"> Salvar</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- List of Messages -->
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
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newCalendarEventModal"><i class="fa fa-plus"> Novo evento</i></button>
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
                    	<div class="event"ng-repeat="event in monthPresentation[row][cel].events | orderBy:'fromHour'"><i class="fa" ng-class="accessClass(event)"></i> {{event.allDay ? 'Dia todo' : event.fromHour | date:'HH:mm'}}: {{event.title}}</div>
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
	                    <i class="fa" ng-class="accessClass(event)"></i>
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
		   				<div class="row">
							<label class="col-md-2 view-form-label">Visível para:</label>
							<p class="col-md-10 view-form-field"  ng-show="event.accessRestricted"><c:out value="${domainConfiguration.labels['VISIBILITY_ONLY_FRANCHISOR']}">Apenas Franqueador</c:out></p>
							<p class="col-md-10 view-form-field"  ng-show="!event.accessRestricted && event.franchiseeIds.length == 0"><c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISEES_ALL']}">Todos os franqueados</c:out></p>
							<ul class="col-md-10 view-form-field" ng-show="!event.accessRestricted && event.franchiseeIds.length > 0">
								<li ng-repeat="franchiseeId in event.franchiseeIds">{{franchiseesMap[franchiseeId].name}}</li>
							</ul>
		   				</div>
		   			</div>
		   			<ul class="list-inline actions">
                        <li><a href ng-click="editEvent(event)" data-toggle="modal" data-target="#newCalendarEventModal"><i class="fa fa-pencil"></i> Editar</a></li>
                        <li><a href ng-click="deleteEvent(event)"><i class="fa fa-trash-o"></i> Excluir</a></li>
                    </ul>
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
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newCalendarEventModal"><i class="fa fa-plus"> Novo evento</i></button>
    	</div>
    </div>
</div>
<div class="legenda">
	<label>Evento visível para:</label>
	<i class="fa fa-lock"> <c:out value="${domainConfiguration.labels['VISIBILITY_ONLY_FRANCHISOR']}">Apenas Franqueador</c:out></i> - 
	<i class="fa fa-unlock-alt"> <c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISOR_SOME_FRANCHISEES']}">Franqueador e Alguns Franqueados</c:out></i> - 
	<i class="fa fa-unlock"> <c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISOR_ALL_FRANCHISEES']}">Franqueador e Todos os Franqueados</c:out></i>
</div>