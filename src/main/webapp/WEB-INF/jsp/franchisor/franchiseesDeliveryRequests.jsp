<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!-- History Modal -->
<div class="modal fade" id="historyModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Histórico de Status do Pedido</h4>
	        </div>
	        <div class="modal-body">
				<table class="table table-bordered table-hover history-table">
					<thead>
						<tr>
							<th>Data</th>
							<th>Status Anterior</th>
							<th>Novo Status</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="item in deliveryRequest.statusHistory.items | orderBy:'date':true">
							<td>{{item.date | date:'dd/MM/yyyy HH:mm'}}</td>
							<td><status-label status="item.previusStatus">{{item.previusStatusDescription}}</status-label></td>                          
							<td><status-label status="item.newStatus">{{item.newStatusDescription}}</status-label></td>                          
						</tr>
					</tbody>
				</table>
	        </div>
	        <div class="modal-footer">
	            <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Fechar</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- Delivery Requests -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-file-text-o"></i> Entrega</h3>
</div>
<div class="panel panel-default">
	<div class="panel-body panel-filter">
		<form>
			<div class="form-group">
				<label for="franchisee"><c:out value="${domainConfiguration.labels['FRANCHISEE']}">Loja</c:out></label>		
				<select id="franchisee" class="form-control" ng-model="franchiseeId" ng-options="unit.id as unit.name for unit in franchisees | orderBy:'name'" ng-change="changeFranchisee()"></select>     
			</div>                         	
		</form>
	</div>
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
				<tr ng-repeat="item in deliveryRequests | orderBy:'date':true">
					<td>{{item.date | date:'dd/MM/yyyy HH:mm'}}</td>
					<td>{{item.product.name}}<span ng-show="item.product.sizes.length > 1"> - {{item.product.sizeName}}: {{item.productSize.name}}</span><div class="supplier"> (Fornecedor: {{item.supplier.name}})</div></td>						                            
					<td>{{item.deliveryUnitPrice | currency:undefined:4}}</td>
					<td>{{item.quantity}} {{item.product.unit}}</td>
					<td>{{item.calculateTotal() | currency}}</td>
					<td>
						<delivery-request-history-status
							delivery-request="item"
							show-history-button="item.statusHistory"
							history-modal-name="#historyModal"
							on-history-click="showDeliveryRequestHistory(item)"
						/>
					</td>                          
				</tr>
			</tbody>
		</table>
		<button ng-show="cursorString != 'none' && cursorString != null" type="button" class="btn btn-default btn-sm btn-block" ng-click="fetchDeliveryRequests()">Carregar mais</button>
		<p ng-show="noRecordsFound" class="text-info text-center">Não há histórico!</p>
	</div>
    <!-- Loading -->
	<div id="deliveryLoad"></div>
    <!-- end loading -->
</div>