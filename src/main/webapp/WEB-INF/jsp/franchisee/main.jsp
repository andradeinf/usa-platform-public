<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-home"></i> Home</h3>
</div>
<div class="row" ng-show="reviewRequest">
	<div class="col-md-12">
		<div class="alert alert-warning text-center" role="alert">
			<div>
				Recentemente você realizou 
				<a href="#" target="_self" class="alert-link" ng-click="getReviewRequestDetails()" data-toggle="modal" data-target="#deliveryRequestsModal" title="Visualizar pedidos">
					{{reviewRequest.data.deliveryRequestHistoryIds.length}} pedido<span ng-show="reviewRequest.data.deliveryRequestHistoryIds.length > 1">s</span>
				</a>
				do fornecedor <strong>{{reviewRequest.supplier.name}}</strong>
			</div>
			<div class="top-buffer" ng-show="!reviewRequest.open">
				Aproveite para nos contar um pouco como foi a sua experiência!
				<div>
					<a href="javascript:void(0);" class="alert-link" ng-click="evaluateSupplierReviewRequest()">Avaliar agora</a>
					&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" class="alert-link" ng-click="cancelSupplierReviewRequest()">Não quero avaliar</a>
				</div>
			</div>			
			<div class="top-buffer" ng-show="reviewRequest.open">
				<div class="center-block review-request">
					<div class="row">
						<div class="col-md-6 review-request-label">Qualidade do Produto</div>
						<div class="col-md-6 review-request-rate">
							<i class="fa" ng-class="rateClass('quality', 1)" ng-click="setRate('quality', 1)"></i>
							<i class="fa" ng-class="rateClass('quality', 2)" ng-click="setRate('quality', 2)"></i>
							<i class="fa" ng-class="rateClass('quality', 3)" ng-click="setRate('quality', 3)"></i>
							<i class="fa" ng-class="rateClass('quality', 4)" ng-click="setRate('quality', 4)"></i>
							<i class="fa" ng-class="rateClass('quality', 5)" ng-click="setRate('quality', 5)"></i>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 review-request-label"> Qualidade da Entrega</div>
						<div class="col-md-6 review-request-rate">
							<i class="fa" ng-class="rateClass('delivery', 1)" ng-click="setRate('delivery', 1)"></i>
							<i class="fa" ng-class="rateClass('delivery', 2)" ng-click="setRate('delivery', 2)"></i>
							<i class="fa" ng-class="rateClass('delivery', 3)" ng-click="setRate('delivery', 3)"></i>
							<i class="fa" ng-class="rateClass('delivery', 4)" ng-click="setRate('delivery', 4)"></i>
							<i class="fa" ng-class="rateClass('delivery', 5)" ng-click="setRate('delivery', 5)"></i>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 review-request-label">Preço do Produto</div>
						<div class="col-md-6 review-request-rate">
							<i class="fa" ng-class="rateClass('price', 1)" ng-click="setRate('price', 1)"></i>
							<i class="fa" ng-class="rateClass('price', 2)" ng-click="setRate('price', 2)"></i>
							<i class="fa" ng-class="rateClass('price', 3)" ng-click="setRate('price', 3)"></i>
							<i class="fa" ng-class="rateClass('price', 4)" ng-click="setRate('price', 4)"></i>
							<i class="fa" ng-class="rateClass('price', 5)" ng-click="setRate('price', 5)"></i>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 review-request-label">Condição de Pagamento</div>
						<div class="col-md-6 review-request-rate">
							<i class="fa" ng-class="rateClass('paymentCondition', 1)" ng-click="setRate('paymentCondition', 1)"></i>
							<i class="fa" ng-class="rateClass('paymentCondition', 2)" ng-click="setRate('paymentCondition', 2)"></i>
							<i class="fa" ng-class="rateClass('paymentCondition', 3)" ng-click="setRate('paymentCondition', 3)"></i>
							<i class="fa" ng-class="rateClass('paymentCondition', 4)" ng-click="setRate('paymentCondition', 4)"></i>
							<i class="fa" ng-class="rateClass('paymentCondition', 5)" ng-click="setRate('paymentCondition', 5)"></i>
						</div>
					</div>
					<div class="row top-buffer">
						<div class="col-md-12">
							<button type="button" class="btn btn-warning btn-xs" ng-click="sendSupplierReviewRequest()">Enviar</button>
						</div>
					</div>
				</div>
			</div>		
		</div>
	</div>
</div>
<div class="panel panel-default" ng-if="domainConfiguration.featureFlags['PRODUCTS'] || domainConfiguration.featureFlags['FRANCHISEE_DOCUMENTS']">
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
			<a href="#/myDeliveryRequests">
				<div class="menu-page-img">
					<img class="img-responsive" src="/img/menu_my_requests.png" alt="" />
				</div>
				<div class="menu-page-text">
					<p>Meus Pedidos</p>
				</div>
			</a>
		</div>
		<c:if test="${user.featureFlags.flagDocuments}">
			<div class="col-sm-4 menu-page-item" ng-if="domainConfiguration.featureFlags['FRANCHISEE_DOCUMENTS']">
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

<!-- History Modal -->
<div class="modal fade bs-example-modal-lg" id="deliveryRequestsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Pedidos</h4>
	        </div>
	        <div class="modal-body">
	            <div class="row" style="max-height: 450px; overflow: auto;">
	                <div class="col-md-12">
	                	<table class="table table-bordered table-hover history-table">
							<thead>
								<tr>
									<th>Data</th>
									<th><c:out value="${domainConfiguration.labels['PRODUCT']}">Produto</c:out></th>
                   					<th>Quantidade</th>						                            
                   					<th>Valor Total</th>
								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="item in reviewRequest.details.deliveryRequests | orderBy:'date':true">
									<td>{{item.date | date:'dd/MM/yyyy'}}</td>
                   					<td>{{item.product.name}}<span ng-show="item.product.sizes.length > 1"> - {{item.product.sizeName}}: {{item.productSize.name}}</span></td>						                            
                   					<td>{{item.quantity}} {{item.product.unit}}</td>
                   					<td>{{item.calculateTotal() | currency}}</td>                    
								</tr>
							</tbody>
						</table>
	                </div>
	            </div>
	        </div>
	        <div class="modal-footer">
	            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        </div>
	        <div id="reviewRequestDetailsLoad"></div>
	    </div>
	</div>
</div><!--/end modal-->
