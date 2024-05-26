<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-home"></i> Home</h3>
</div>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title"><i class="fa fa-bookmark"></i> Menu de Acesso Rápido</h3>
	</div>
	<div class="panel-body">
		<div class="col-sm-4 menu-page-item" ng-if="domainConfiguration.featureFlags['PRODUCTS']">
			<a href="#/products">
				<div class="menu-page-img">
					<img class="img-responsive" src="/img/menu_products.png" alt="" />
				</div>
				<div class="menu-page-text">
					<p>Produtos</p>
				</div>
			</a>
		</div>
		<div class="col-sm-4 menu-page-item" ng-if="domainConfiguration.featureFlags['PRODUCTS']">
			<a href="#/franchiseesDeliveryRequests">
				<div class="menu-page-img">
					<img class="img-responsive" src="/img/menu_my_requests.png" alt="" />
				</div>
				<div class="menu-page-text">
					<p>Pedidos de Entrega</p>
				</div>
			</a>
		</div>
		<c:if test="${user.featureFlags.flagDocuments}">
			<div class="col-sm-4 menu-page-item">
				<a href="#/documents">
					<div class="menu-page-img">
						<img class="img-responsive" src="/img/menu_documents.png" alt="" />
					</div>
					<div class="menu-page-text">
						<p>Documentos</p>
					</div>
				</a>
			</div>
		</c:if>		
	</div>
</div>

<c:if test="${user.featureFlags.flagAnnouncement}">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title"><i class="fa fa-comment"></i> Últimos Avisos</h3>
		</div>
		<ul class="list-group pointer-cursor" ng-click="announcementClick()" >
			<li class="list-group-item" ng-show="announcements.length == 0">
				<p class="info text-info text-center">Você não possui avisos...</p>
			</li>
			<li class="list-group-item" ng-repeat="item in announcements | orderBy:'date':true">
				<div ng-if="announcementUnreadNotifications[item.id] != undefined" class="floating-label">Novo</div>
				<div>
					{{item.date | date:'dd/MM/yyyy HH:mm'}} - {{item.title}}
				</div>
			</li>
		</ul>
	</div>
	<!-- Loading -->
	<div id="announcemensLoad"></div>
	<!-- end loading -->
</c:if>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title"><i class="fa fa-envelope"></i> Caixa de Entrada - Últimas Mensagens</h3>
	</div>
	<ul class="list-group pointer-cursor" ng-click="messageClick()" >
		<li class="list-group-item" ng-show="messageTopics.length == 0">
			<p class="info text-info text-center">Você não possui mensagens...</p>
		</li>
		<li class="list-group-item" ng-repeat="item in messageTopics | orderBy:'updateDate':true">
			<div ng-if="messageTopicBadgeCount(messageTopicsUnreadNotifications[item.id]) > 0" class="badge floating-badge">{{messageTopicBadgeCount(messageTopicsUnreadNotifications[item.id])}}</div>
			<div style="padding-right: 25px;">
				{{item.date | date:'dd/MM/yyyy HH:mm'}} - {{item.title}}
			</div>
		</li>
	</ul>
</div>
<!-- Loading -->
<div id="messageTopicsLoad"></div>
<!-- end loading -->
<c:if test="${user.featureFlags.flagCalendar}">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title"><i class="fa fa-calendar"></i> Próximos eventos</h3>
		</div>
		<div class="panel-body pointer-cursor" ng-click="calendarClick()">
			<div class="row">
				<div class="col-md-3 menu-page-calendar-item" ng-repeat="item in calendarEvents | orderBy:'fromHour'">
					<div class="menu-page-calendar-date">
						<p>{{item.fromHour | date:'dd MMM'}} <small>{{item.fromHour | date:'HH:mm'}}</small></p>
					</div>
					<div class="menu-page-calendar-text">
						<p ng-attr-title="{{item.title}}">{{item.title}}</p>
					</div>
				</div>
				<div class="col-sm-12" ng-show="calendarEvents.length == 0">
					<p class="info text-info text-center">Você não possui próximos eventos...</p>
				</div>
			</div>
		</div>
	</div>
	<!-- Loading -->
	<div id="calendarEventsLoad"></div>
	<!-- end loading -->
</c:if>
