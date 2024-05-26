<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title><c:out value="${domainConfiguration.title}">Sistema para Franquias</c:out> - Pedidos de Entrega</title>
	
	    <!-- Meta -->
	    <meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    
		<!-- Favicon -->
	    <link rel="shortcut icon" href="/ocvfranquias.ico">
	    
	    <!-- CSS Vendors -->
	    <link rel="stylesheet" href="/vendor/bootstrap/css/bootstrap.min.css">
	    <link rel="stylesheet" href="/vendor/font-awesome/css/font-awesome.min.css">
	    <link rel='stylesheet' href='/vendor/text-angular/textAngular.css'>

	    <!-- CSS Application -->
		<link rel="stylesheet" href="/app/styles/franchiseLogisticsPrint.css" type="text/css" />
	    
	</head> 
	<body ng-app="franchisorLogisticsPrint">
	
		<div class="container" ng-controller="DeliveryRequestsPrintCtrl">
		
				  <div class="no-print print-bar">
				  	<button class="btn btn-lg btn-default btn-block" ng-click="print()"><i class="fa fa-print"> Imprimir</i></button>
				  </div>				  
		
				  <div class="franchisee-details">
					<h2>{{franchisee.name}}</h2>
					<ul>
						<li ng-if="franchisee.corporateName != ''"><strong><c:out value="${domainConfiguration.labels['FRANCHISOR_CORPORATE_NAME']}">Razão Social</c:out>:</strong> {{franchisee.corporateName}}</li>
						<li ng-if="franchisee.fiscalId != ''"><strong><c:out value="${domainConfiguration.labels['FRANCHISOR_FISCAL_ID']}">CNPJ</c:out>:</strong> {{franchisee.fiscalId}}</li>
						<li ng-if="franchisee.address != ''"><strong>Endereço:</strong> {{franchisee.address}}</li>
						<li ng-if="franchisee.cityId != '' || franchisee.cityId != 0"><strong>Cidade:</strong> {{deliveryRequestsCities[franchisee.cityId].name}}</li>
						<li ng-if="franchisee.stateId != '' || franchisee.stateId != 0"><strong>Estado:</strong> {{deliveryRequestsStates[franchisee.stateId].name}}</li>
						<li ng-if="franchisee.contactName != ''"><strong>Contato:</strong> {{franchisee.contactName}}</li>
						<li ng-if="franchisee.contactPhone != ''"><strong>Telefone:</strong> {{franchisee.contactPhone}}</li>
						<li ng-if="franchisee.contactEmail != ''"><strong>E-mail p/ envio da Nota Fiscal:</strong> {{franchisee.contactEmail}}</li>
						<li ng-if="franchisee.additionalInformation != ''"><div ta-bind="text" ng-model="franchisee.additionalInformation"></div></li>
					</ul>
				  </div>
				  
				  <h2 class="delivery-title">Pedidos</h2>
				  
				  <table class="delivery-requests">
				  	<thead>
                        <tr>
                            <th>Data</th>
                            <th><c:out value="${domainConfiguration.labels['PRODUCT']}">Produto</c:out></th>
                            <th>Quantidade</th>
                            <th>Valor Unit.</th>
                            <th>Valor do Pedido</th>
                        </tr>
                    </thead>
                    <tbody>
						<tr ng-repeat="item in printRequestItems | orderBy:'date':true">
                            <td class="text-center">{{item.date | date:'dd/MM/yyyy'}}</td>
                            <td>{{item.product.name}} <span ng-show="item.product.sizes.length > 1"> - {{item.product.sizeName}}: {{item.productSize.name}}</span></td>
                            <td>{{item.deliveryQty}}<span ng-show="item.product.hasDeliveryUnit"> {{item.product.deliveryUnit}} = {{item.calculateTotalQuantity()}}</span> {{item.product.unit}}</td>
                            <td class="text-right">{{item.deliveryUnitPrice | currency:undefined:4}}</td>
                            <td class="text-right">{{item.calculateDelivery() | currency}}</td>                
                        </tr>
                        <tr class="total">
                        	<td colspan="4"> Valor Total</td>
                        	<td>{{calculateTotal() | currency}}</td>
                        </tr>
                    </tbody>
				  </table>

		</div>
		
		<!-- JS Vendors -->           
		<script type="text/javascript" src="/vendor/jquery/jquery-3.1.1.min.js"></script>
		<script type="text/javascript" src="/vendor/jquery/jquery-migrate-1.4.1.min.js"></script>
		<script type="text/javascript" src="/vendor/bootstrap/js/bootstrap.min.js"></script> 
		<script type="text/javascript" src="/vendor/angular/angular.min.js"></script>	
		<script type="text/javascript" src="/vendor/angular/angular-locale_pt-br.js"></script>
		<script type="text/javascript" src="/vendor/angular-filter/angular-filter.min.js"></script>
		<script type="text/javascript" src='/vendor/text-angular/textAngular-rangy.min.js'></script>
		<script type="text/javascript" src="/vendor/text-angular/textAngular-sanitize.min.js"></script>
		<script type="text/javascript" src="/vendor/text-angular/textAngular.min.js"></script>	
	
		<!-- JS Application -->
		<script src="/app/scripts/AppPrint.min.js" type="text/javascript"></script>
		
		<!--[if lt IE 9]>
		    <script src="/vendor/legacy-browser/respond.js"></script>
		    <script src="/vendor/legacy-browser/html5shiv.js"></script>
		<![endif]-->
	</body>
</html>