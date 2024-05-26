<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Manufacture Requests -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-file-text-o"></i> Produção</h3>
</div>
<div class="panel panel-default">
	<div class="panel-body">
    	<table class="table table-bordered table-hover history-table">
            <thead>
                <tr>
                    <th>Data</th>
                    <th><c:out value="${domainConfiguration.labels['PRODUCT']}">Produto</c:out></th>
                    <th>Valor Unit.</th>
                    <th>Quantidade</th>						                            
                    <th>Valor Total</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
				<tr ng-repeat="item in manufactureRequests"><!--  | orderBy:'manufactureRequest.date':true -->
                    <td>{{item.date | date:'dd/MM/yyyy HH:mm'}}</td>
                    <td>{{item.product.name}}<span ng-show="item.product.sizes.length > 1"> - {{item.product.sizeName}}: {{item.productSize.name}}</span></td>						                            
                    <td>{{item.manufactureUnitPrice | currency:undefined:4}}</td>
                    <td>{{item.quantity}} {{item.product.unit}}</td>
                    <td>{{item.calculateTotal() | currency}}</td>
                    <td>
                    	<span class="label" ng-class="statusClass(item.status)">{{item.statusDescription}}</span>
                    	<div class="margin-top-5 text-danger" ng-show="item.status == 'CANCELLED'"><small>{{item.cancellationComment}}</small></div>
                    </td>                          
                </tr>
            </tbody>
    	</table>
    	<button ng-show="cursorString != null" type="button" class="btn btn-default btn-sm btn-block" ng-click="fetchManufactureRequests()">Carregar mais</button>
    </div>
    <!-- Loading -->
	<div id="manufactureLoad"></div>
    <!-- end loading -->
</div>