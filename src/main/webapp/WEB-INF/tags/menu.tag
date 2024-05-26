<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@tag import="br.com.usasistemas.usaplatform.model.data.UserProfileData" %>
<%@attribute name="user" required="true" type="br.com.usasistemas.usaplatform.model.data.UserProfileData"%>
<%@ attribute name="supplierCategories" required="false" rtexprvalue="true" type="java.util.Collection" %>
<%@ attribute name="supplierCategory" required="false" rtexprvalue="true" type="br.com.usasistemas.usaplatform.model.data.SupplierCategoryData" %>
<%@ attribute name="supplier" required="false" rtexprvalue="true" type="br.com.usasistemas.usaplatform.model.data.SupplierData" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<ul class="list-group menu" ng-controller="MenuCtrl">
    <c:if test="${user.selectedRole == 'ADMINISTRATOR'}">
		<li class="list-group-item">
	        <a href="#/home"><i class="fa fa-home"></i> <c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISORS']}">Franquias</c:out></a>
	    </li>
	    <li class="list-group-item list-toggle">
			<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-suppliers"><i class="fa fa-truck"></i> <c:out value="${domainConfiguration.labels['SUPPLIERS']}">Fornecedores</c:out></a>
			<ul id="collapse-suppliers" class="collapse">
				 <li>
				 	<a href="#/admin/suppliers/categories"> Categorias</a>
				 </li>
				 <li>
				 	<a href="#/admin/suppliers"> Lista de <c:out value="${domainConfiguration.labels['SUPPLIERS']}">Fornecedores</c:out></a>
				 </li>
			</ul>
		</li>
	    <li class="list-group-item">
	        <a href="#/admin/users"><i class="fa fa-users"></i> Usuários</a>
	    </li>
	    <li class="list-group-item list-toggle"> 
			<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-report"><i class="fa fa-file-text"></i> Relatórios</a>
			<ul id="collapse-report" class="collapse">
				 <li>
				 	<a href="#/admin/report/deliveriesBySupplierReport"> Entregas</a>
				 </li>
			</ul>
		</li>
		<li class="list-group-item list-toggle" ng-show="receiveMessage">
        	<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-messages"><i class="fa fa-envelope"></i> <c:out value="${domainConfiguration.labels['MESSAGES']}">Central de Atendimento</c:out> <span ng-hide="newMessageCount == 0" class="badge">{{newMessageCount}}</span></a>
			<ul id="collapse-messages" class="collapse in">
				<li><a href="#/messages/0"><i class="fa fa-inbox"></i> Caixa de Entrada</a></li>
				<li><a href="#/messages/99"><i class="fa fa-envelope-o"></i> Todas as Mensagens</a></li>
				<li ng-repeat="label in userLabels"><a href="#/messages/{{label.id}}"><i class="fa fa-tag"></i> {{label.name}}</a></li>
				<li><a href="#/messageLabels"><i class="fa fa-cog"></i> Gerenciar Marcadores</a></li>
			</ul>
		</li>
	    <li class="list-group-item">
	        <a href="#/admin/tutorials"><i class="fa fa-youtube-play"></i> Tutoriais do sistema</a>
	    </li>
	    <li class="list-group-item">
	        <a href="#/admin/systemConfiguration"><i class="fa fa-cog"></i> Configurações</a>
	    </li>
    </c:if>
    <c:if test="${user.selectedRole == 'FRANCHISOR'}">
    	<li class="list-group-item">
	        <a href="#/home"><i class="fa fa-home"></i> Home</a>
	    </li>
	    <c:if test="${domainConfiguration.featureFlags['PRODUCTS']}">
			<li class="list-group-item">
		        <a href="#/products"><i class="fa fa-gift"></i> <c:out value="${domainConfiguration.labels['PRODUCTS']}">Produtos</c:out></a>
		    </li>
	    </c:if>
	    <li class="list-group-item list-toggle" ng-show="receiveMessage">
        	<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-messages"><i class="fa fa-envelope"></i> <c:out value="${domainConfiguration.labels['MESSAGES']}">Central de Atendimento</c:out> <span ng-hide="newMessageCount == 0" class="badge">{{newMessageCount}}</span></a>
			<ul id="collapse-messages" class="collapse in">
				<li><a href="#/messages/0"><i class="fa fa-inbox"></i> Caixa de Entrada</a></li>
				<li><a href="#/messages/99"><i class="fa fa-envelope-o"></i> Todas as Mensagens</a></li>
				<li ng-repeat="label in userLabels"><a href="#/messages/{{label.id}}"><i class="fa fa-tag"></i> {{label.name}}</a></li>
				<li><a href="#/messageLabels"><i class="fa fa-cog"></i> Gerenciar Marcadores</a></li>
			</ul>
		</li>
		<c:if test="${user.featureFlags.flagAnnouncement}">
			<li class="list-group-item">
				<a href="#/announcements"><i class="fa fa-comment"></i> <c:out value="${domainConfiguration.labels['ANNOUNCEMENTS']}">Quadro de Avisos</c:out> <span ng-hide="newAnnouncementCount == 0" class="badge">{{newAnnouncementCount}}</span></a>
			</li>
		</c:if>
		<c:if test="${user.featureFlags.flagDocuments}">
			<li class="list-group-item">
				<a href="#/documents"><i class="fa fa-folder"></i> <c:out value="${domainConfiguration.labels['FILES']}">Documentos</c:out></a>
			</li>
		</c:if>
		<c:if test="${user.featureFlags.flagTraining}">
			<li class="list-group-item">
				<a href="#/trainings"><i class="fa fa-video-camera"></i> <c:out value="${domainConfiguration.labels['TRAININGS']}">Treinamentos</c:out></a>
			</li>
		</c:if>
		<c:if test="${user.featureFlags.flagCalendar}">
			<li class="list-group-item">
				<a href="#/calendar"><i class="fa fa-calendar"></i> <c:out value="${domainConfiguration.labels['CALENDAR']}">Calendário</c:out></a>
			</li>
		</c:if>
		<c:if test="${domainConfiguration.featureFlags['PRODUCTS']}">
			<li class="list-group-item list-toggle">
				<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-requests"><i class="fa fa-file-text"></i> Histórico</a>
				<ul id="collapse-requests" class="collapse">
					 <li>
					 	<a href="#/myManufactureRequests"> Produção</a>
					 </li>
					 <li>
					 	<a href="#/franchiseesDeliveryRequests"> Entrega</a>
					 </li>
				</ul>
			</li>
		</c:if>
		<li class="list-group-item list-toggle">
			<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-report"><i class="fa fa-file-text"></i> Relatórios</a>
			<ul id="collapse-report" class="collapse">
				<c:if test="${domainConfiguration.featureFlags['PRODUCTS']}">
					<li>
						<a href="#/deliverieRequestsByTimeRangeReport"> Entregas por Período</a>
					</li>
				</c:if>
				 <li ng-show="receiveMessage">
				 	<a href="#/franchiseeMessagesByTimeRangeReport"> Mensagens dos <c:out value="${domainConfiguration.labels['FRANCHISEES']}">Franqueados</c:out> por Período</a>
				 </li>
			</ul>
		</li>
    	<c:if test="${supplierCategories.size() > 0}">
		    <li class="list-group-item list-toggle">
				<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-suppliers"><i class="fa fa-truck"></i> <c:out value="${domainConfiguration.labels['SUPPLIERS']}">Fornecedores</c:out></a>
				<ul id="collapse-suppliers" class="collapse">
					<c:forEach items="${supplierCategories}" var="category">
				        <li><a href="#/suppliers/${category.id}"> ${category.name}</a></li>
				    </c:forEach>
				</ul>
			</li>
		</c:if>
		<c:if test="${domainConfiguration.featureFlags['TUTORIALS']}">
			<li class="list-group-item">
				<a href="#/tutorials"><i class="fa fa-youtube-play"></i> Tutoriais do sistema</a>
			</li>
		</c:if>		
    </c:if>
    <c:if test="${user.selectedRole == 'FRANCHISEE'}">
    	<li class="list-group-item">
	        <a href="#/home"><i class="fa fa-home"></i> Home</a>
		</li>
	    <c:if test="${domainConfiguration.featureFlags['PRODUCTS']}">
		    <li class="list-group-item">
		        <a href="#/products"><i class="fa fa-gift"></i> <c:out value="${domainConfiguration.labels['PRODUCTS']}">Produtos</c:out></a>
		    </li>
		</c:if>
	    <li class="list-group-item list-toggle" ng-show="receiveMessage">
        	<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-messages"><i class="fa fa-envelope"></i> <c:out value="${domainConfiguration.labels['MESSAGES']}">Central de Atendimento</c:out> <span ng-hide="newMessageCount == 0" class="badge">{{newMessageCount}}</span></a>
			<ul id="collapse-messages" class="collapse in">
				<li><a href="#/messages/0"><i class="fa fa-inbox"></i> Caixa de Entrada</a></li>
				<li><a href="#/messages/99"><i class="fa fa-envelope-o"></i> Todas as Mensagens</a></li>
				<li ng-repeat="label in userLabels"><a href="#/messages/{{label.id}}"><i class="fa fa-tag"></i> {{label.name}}</a></li>
				<li><a href="#/messageLabels"><i class="fa fa-cog"></i> Gerenciar Marcadores</a></li>
			</ul>
		</li>
		<c:if test="${user.featureFlags.flagAnnouncement}">
			<li class="list-group-item">
				<a href="#/announcements"><i class="fa fa-comment"></i> <c:out value="${domainConfiguration.labels['ANNOUNCEMENTS']}">Quadro de Avisos</c:out> <span ng-hide="newAnnouncementCount == 0" class="badge">{{newAnnouncementCount}}</span></a>
			</li>
		</c:if>	
		<c:if test="${domainConfiguration.featureFlags['FRANCHISEE_DOCUMENTS'] && user.featureFlags.flagDocuments}">
        	<li class="list-group-item">
		        <a href="#/documents"><i class="fa fa-folder"></i> <c:out value="${domainConfiguration.labels['FILES']}">Documentos</c:out></a>
		    </li>
	    </c:if>
		<c:if test="${user.featureFlags.flagTraining}">
			<li class="list-group-item">
				<a href="#/trainings"><i class="fa fa-video-camera"></i> <c:out value="${domainConfiguration.labels['TRAININGS']}">Treinamentos</c:out></a>
			</li>
		</c:if>
		<c:if test="${user.featureFlags.flagCalendar}">
			<li class="list-group-item">
				<a href="#/calendar"><i class="fa fa-calendar"></i> <c:out value="${domainConfiguration.labels['CALENDAR']}">Calendário</c:out></a>
			</li>
		</c:if>
		<c:if test="${domainConfiguration.featureFlags['PRODUCTS']}">
	        <li class="list-group-item">
	            <a href="#/accountsPayableReport"><i class="fa fa-money"></i> Contas a Pagar</a>
	        </li>
			<li class="list-group-item">
	            <a href="#/myDeliveryRequests"><i class="fa fa-file-text"></i> Meus Pedidos</a>
			</li>
			<li class="list-group-item list-toggle">
				<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-report"><i class="fa fa-file-text"></i> Relatórios</a>
				<ul id="collapse-report" class="collapse">
					 <li>
						 <a href="#/franchiseeDeliveriesByTimeRangeReport"> Entregas por Período</a>
					 </li>
				</ul>
			</li>
	    </c:if>
    	<c:if test="${supplierCategories.size() > 0}">
		    <li class="list-group-item list-toggle">
				<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-suppliers"><i class="fa fa-truck"></i> <c:out value="${domainConfiguration.labels['SUPPLIERS']}">Fornecedores</c:out></a>
				<ul id="collapse-suppliers" class="collapse">
					 <c:forEach items="${supplierCategories}" var="category">
				        <li><a href="#/suppliers/${category.id}"> ${category.name}</a></li>
				    </c:forEach>
				</ul>
			</li>
		</c:if>	    
		<c:if test="${domainConfiguration.featureFlags['TUTORIALS']}">
			<li class="list-group-item">
				<a href="#/tutorials"><i class="fa fa-youtube-play"></i> Tutoriais do sistema</a>
			</li>
		</c:if>	    
    </c:if>
    <c:if test="${user.selectedRole == 'SUPPLIER'}">
    	<c:if test="${supplierCategory.hasStockControl}">
			<c:if test="${supplier.type == 'RECEIVE_MANUFACTURE_REQUEST'}">
		    	<li class="list-group-item list-toggle">
					<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-requests"><i class="fa fa-home"></i> Pedidos</a>
					<ul id="collapse-requests" class="collapse in">
						 <li>
						 	<a href="#/deliveryRequests"> Entrega</a>
						 </li>
						 <li>
						 	<a href="#/manufactureRequests"> Produção</a>
						 </li>
					</ul>
				</li>
			</c:if>
			<c:if test="${supplier.type == 'MANUFACTURE_WITHOUT_REQUEST'}">
		    	<li class="list-group-item">
			        <a href="#/deliveryRequests"><i class="fa fa-home"></i> Pedidos</a>
			    </li>
			</c:if>
		</c:if>
		<c:if test="${supplierCategory.receiveMessage}">
			<li class="list-group-item list-toggle" ng-show="receiveMessage">
				<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-messages"><i class="fa fa-envelope"></i> <c:out value="${domainConfiguration.labels['MESSAGES']}">Central de Atendimento</c:out> <span ng-hide="newMessageCount == 0" class="badge">{{newMessageCount}}</span></a>
				<ul id="collapse-messages" class="collapse in">
					<li><a href="#/messages/0"><i class="fa fa-inbox"></i> Caixa de Entrada</a></li>
					<li><a href="#/messages/99"><i class="fa fa-envelope-o"></i> Todas as Mensagens</a></li>
					<li ng-repeat="label in userLabels"><a href="#/messages/{{label.id}}"><i class="fa fa-tag"></i> {{label.name}}</a></li>
					<li><a href="#/messageLabels"><i class="fa fa-cog"></i> Gerenciar Marcadores</a></li>
				</ul>
			</li>
		</c:if>
		<c:if test="${supplierCategory.hasStockControl}">
	        <li class="list-group-item">
	            <a href="#/products"><i class="fa fa-gift"></i> <c:out value="${domainConfiguration.labels['PRODUCTS']}">Produtos</c:out></a>
	        </li>
	        <c:if test="${domainConfiguration.featureFlags['SUPPLIER_STOCK']}">
		        <li class="list-group-item">
		            <a href="#/stock"><i class="fa fa-bar-chart-o"></i> Estoque</a>
		        </li>
	        </c:if>	        
	        <li class="list-group-item">
	            <a href="#/accountsReceivableReport"><i class="fa fa-money"></i> Contas a Receber</a>
	        </li>
	        <li class="list-group-item list-toggle">
				<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-search"><i class="fa fa-search"></i> Consultas</a>
				<ul id="collapse-search" class="collapse">
					 <li>
					 	<a href="#/deliveryReport"> Entregas por Status</a>
					 </li>
				</ul>
			</li>
	        <li class="list-group-item list-toggle">
				<a data-toggle="collapse" data-parent="#sidebar-nav" target="_self" href="#collapse-report"><i class="fa fa-file-text"></i> Relatórios</a>
				<ul id="collapse-report" class="collapse">
					 <li>
					 	<a href="#/supplierDeliveriesByTimeRangeReport"> Entregas por Período</a>
					 </li>
				</ul>
			</li>
			<c:if test="${domainConfiguration.featureFlags['TUTORIALS']}">
				<li class="list-group-item">
					<a href="#/tutorials"><i class="fa fa-youtube-play"></i> Tutoriais do sistema</a>
				</li>
			</c:if>
		</c:if>
    </c:if>
    <li class="list-group-item">
        <a href="#/configuration"><i class="fa fa-user"></i> Meus Dados</a>
    </li>
    <c:if test="${user.hasMultipleProfiles}">
	    <li class="list-group-item">
	        <a href="login#/profileSelection"><i class="fa fa-retweet"></i> Alterar perfil</a>
	    </li>
	</c:if>
</ul>
