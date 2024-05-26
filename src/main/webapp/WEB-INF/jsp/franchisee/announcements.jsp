<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- List of announcements -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-comment"></i> <c:out value="${domainConfiguration.labels['ANNOUNCEMENTS']}">Quadro de Avisos</c:out></h3>
</div>
<div class="panel panel-default">
	<div class="panel-heading clearfix">
		<div class="btn-group pull-left" role="group">
			<button type="button" class="btn btn-default" ng-click="fetchAnnouncements()">&nbsp;<i class="fa fa-repeat"></i>&nbsp;</button>
			<button type="button" class="btn btn-default" ng-disabled="page==1" ng-click="previousAnnouncemens()">&nbsp;<i class="fa fa-chevron-left"></i>&nbsp;</button>
			<button type="button" class="btn btn-default" ng-disabled="!nextEnabled" ng-click="nextAnnouncemens()">&nbsp;<i class="fa fa-chevron-right"></i>&nbsp;</button>
			<div class="btn-group" role="group">
			  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				&nbsp;Itens por página: {{pageSize}}&nbsp;
				<span class="caret"></span>
			  </button>
			  <ul class="dropdown-menu">
				<li><a href="javascript:void(0);" ng-click="setPageSize(10)">10 itens</a></li>
				<li><a href="javascript:void(0);" ng-click="setPageSize(25)">25 itens</a></li>
				<li><a href="javascript:void(0);" ng-click="setPageSize(50)">50 itens</a></li>
				<li><a href="javascript:void(0);" ng-click="setPageSize(100)">100 itens</a></li>
			  </ul>
			</div>
		</div>
	</div>
	<div class="panel-list-group">
		<ul class="list-group">
			<li class="list-group-item" ng-repeat="item in announcements | orderBy:'date':true">
				<div ng-if="announcementUnreadNotifications[item.id] != undefined" class="floating-label">Novo</div>
				<div class="row list-group-item-row">
					<div class="col-sm-12 list-group-item-column">
						<h4 class="list-group-item-title" ng-class="{ 'collapsed' : item.collapsed }">
							<strong ng-show="item.title.length > 0">{{item.title}}</strong>
							<small>
								<a class="toggle" href="javascript:void(0);" ng-click="toggleAnnouncementDetails(item)">
									<i class="fa fa-plus-square-o" ng-show="item.collapsed" title="Visualizar Conteúdo"></i>
			                        <i class="fa fa-minus-square-o" ng-show="!item.collapsed" title="Ocultar Conteúdo"></i>
			                    </a>
								&nbsp;&nbsp;{{item.date | date:'dd/MM/yyyy HH:mm'}}</small>
						</h4>
						<div ng-show="!item.collapsed" ta-bind="text" ng-model="item.message"></div>
					</div>
				</div>
			</li>
		</ul>
	</div>   	
   	<p ng-show="announcements.length == 0" class="text-info text-center margin-top-15">Não há avisos.</p>	
    <!-- Loading -->
    <div id="announcemensLoad">
   		<div class="overlay"></div>
    	<div class="loading-img"></div>
    </div>
    <!-- end loading -->
    <div class="panel-footer clearfix">
    	<div class="btn-group pull-left" role="group">
    		<button type="button" class="btn btn-default" ng-click="fetchAnnouncements()">&nbsp;<i class="fa fa-repeat"></i>&nbsp;</button>
    		<button type="button" class="btn btn-default" ng-disabled="page==1" ng-click="previousAnnouncemens()">&nbsp;<i class="fa fa-chevron-left"></i>&nbsp;</button>
    		<button type="button" class="btn btn-default" ng-disabled="!nextEnabled" ng-click="nextAnnouncemens()">&nbsp;<i class="fa fa-chevron-right"></i>&nbsp;</button>
			<div class="btn-group dropup" role="group">
			  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			    &nbsp;Itens por página: {{pageSize}}&nbsp;
			    <span class="caret"></span>
			  </button>
			  <ul class="dropdown-menu">
			    <li><a href="javascript:void(0);" ng-click="setPageSize(10)">10 itens</a></li>
			    <li><a href="javascript:void(0);" ng-click="setPageSize(25)">25 itens</a></li>
			    <li><a href="javascript:void(0);" ng-click="setPageSize(50)">50 itens</a></li>
			    <li><a href="javascript:void(0);" ng-click="setPageSize(100)">100 itens</a></li>
			  </ul>
			</div>
    	</div>
    </div>
</div>