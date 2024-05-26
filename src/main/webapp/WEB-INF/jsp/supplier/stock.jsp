<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Stocks -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-bar-chart-o"></i> Estoque</h3>
</div>
<div class="panel panel-default">
	<div class="panel-list-group">
		<ul class="list-group">
		
			<li class="list-group-item list-group-item-filter" ng-hide="!multipleFranchisors">
		        <form>
		        	<div class="form-group">
		                <label for="franchisor"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISOR']}">Franquia</c:out></label>		
		                <select id="franchisor" class="form-control" ng-model="franchisorId" ng-options="unit.id as unit.name for unit in franchisors | orderBy:'name'" ng-change="loadProducts()"></select>     
		            </div>                         	
		        </form>
		    </li>
		
			<li class="list-group-item" ng-repeat="item in productsList | filter:productFilter | orderBy:'name'">
				<div class="row list-group-item-row">
					<div class="col-sm-3 list-group-item-column">
						<div class="thumbnail zoomer">
		    				<a class="fancybox-button" rel="fancybox-button-{{item.id}}" title="{{item.name}}" ng-href="{{item.imageURL}}">
		    					<img class="img-responsive" ng-src="{{item.thumbnail}}" alt="" />
		    					<img class="zoom-icon" src="/img/overlay-icon.png" alt="" />
		    					<i class="zoomer-icon add-icon zoom-fa fa fa-search-plus"></i>
		    				</a>
		    			</div>
					</div>
					<div class="col-sm-9 list-group-item-column clearfix">
						<h4 class="list-group-item-title">
		                    <strong>{{item.name}}</strong>
		                </h4>
		                <p ta-bind="text" ng-model="item.description"></p>
						<ul class="list-unstyled margin-bottom-20">
	                    	<li ng-show="item.manufactureTime > 0"><i class="fa fa-check"></i> <strong>Prazo de Produção:</strong> {{item.manufactureTime}} dia<spam ng-show="item.manufactureTime != 1">s</spam></li>
	                        <li ng-show="item.deliveryTime > 0"><i class="fa fa-check"></i> <strong>Prazo de Preparação p/Entrega:</strong> {{item.deliveryTime}} dia<spam ng-show="item.deliveryTime != 1">s</spam></li>	
	                    </ul>
	                    <table class="table table-bordered table-hover size-table">
		                    <thead>
		                        <tr>
		                            <th class="size-table-head">{{item.sizeName}}</th>
		                            <th class="size-table-head">Valor Unit.</th>
		                            <th ng-show="item.hasDeliveryUnit" class="size-table-head">Entrega</th>
		                            <th ng-show="item.hasManufactureMinQty" class="size-table-head">Qde Mín. Produção</th>
		                            <th class="size-table-head">Estoque</th>
		                        </tr>
		                    </thead>
		                    <tbody>
								<tr ng-repeat="size in item.sizes | toArray | orderBy:'name'">
		                            <td>{{size.name}}<span ng-show="size.description != null && size.description != ''"> - {{size.description}}</span></td>
		                            <td>{{size.unitPrice | currency:undefined:4}}</td>
		                            <td ng-show="item.hasDeliveryUnit">{{item.deliveryUnit}} com {{size.deliveryQty}} {{item.unit}}</td>  
		                            <td ng-show="item.hasManufactureMinQty">{{calculateManufactureMinTotalQty(item, size)}} {{item.unit}}</td>
		                            <td ng-class="stockClass(item, size)"><strong>{{size.currentStock}} {{item.unit}}</strong></td>                    
		                        </tr>
		                    </tbody>
	                	</table>
		                <div class="list-group-item-actions" ng-hide="item.requestMode">
		                    <ul class="list-inline comment-list-v2 pull-left">
		                        <li ng-show="supplierList[item.supplierId].type=='MANUFACTURE_WITHOUT_REQUEST'"><i class="fa fa-plus"></i> <a href="javascript:void(0);" ng-click="createManufactureRequest(item)">Adicionar</a></li>
		                        <li ng-show="supplierList[item.supplierId].type=='MANUFACTURE_WITHOUT_REQUEST'"><i class="fa fa-minus"></i> <a href="javascript:void(0);" ng-click="createNegativeManufactureRequest(item)">Remover</a></li>
		                        <li><i class="fa fa-file-text-o"></i> <a href="#" target="_self" ng-click="openDeliveryHistory(item)" data-toggle="modal" data-target="#historyModal">Histórico</a></li>
		                    </ul>
		                </div>
		                <div class="list-group-item-actions" ng-show="item.requestMode">
							<h4><strong>
								<span ng-show="item.manufactureRequest.operation_sign > 0">Produção</span>
								<span ng-show="item.manufactureRequest.operation_sign < 0">Ajuste</span>
							</strong></h4>
							<form class="form-inline">
		                        <ul class="list-unstyled">
		                        	<li ng-show="item.sizes.length > 1">
		                        		<strong>{{item.sizeName}}: </strong>
		                        		<select ng-model="item.manufactureRequest.data.productSizeId" ng-options="size.id as size.name for size in item.manufactureSizeList | orderBy:'name'" ng-change="setSelectedSize(item)"></select>
		                        	</li>
		                        	<li>
		                        		<strong>Quantidade: </strong>
		                        		<input type="text" class="form-control" style="height: 22px; width: 50px; padding: 0px 2px;" ng-model="item.manufactureRequest.manufactureQty" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
		                        		<span ng-show="item.hasDeliveryUnit"> {{item.deliveryUnit}}</span>
		                        		<span ng-show="!item.hasDeliveryUnit"> {{item.unit}}</span>
		                        	</li>
		                        	<li ng-show="item.hasDeliveryUnit">
		                        		<strong>Quantidade Total: </strong>
		                        		{{item.manufactureRequest.calculateManufactureQtyTotal()}} {{item.unit}}
		                        	</li>
		                        	<li>
		                        		<strong>Valor Total: </strong>{{item.manufactureRequest.calculateManufactureRequestTotal() | currency}}
		                        	</li>
		                            <li>
		                            	<div class="top-buffer">
			                            	<button class="btn btn-default btn-xs" type="button" ng-click="saveManufactureRequest(item)">Enviar</button>
			                            	<button class="btn btn-default btn-xs" type="button" ng-click="cancelManufactureRequest(item)">Cancelar</button>
		                            	</div>
		                            </li>
		                        </ul> 
		                	</form>
		                </div>
					</div>
				</div>			
			</li>
		</ul>
	</div>
    <!-- Loading -->
	<div id="productLoad"></div>
    <!-- end loading -->
</div>
    
<!-- History Modal -->
<div class="modal fade bs-example-modal-lg" id="historyModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Histórico</h4>
	        </div>
	        <div class="modal-body">
	            <div class="row" style="max-height: 450px; overflow: auto;">
	                <div class="col-md-12">
	                	<div>
	                		<ul class="nav nav-tabs">
		                        <li class="active"><a href="#deliveryRequests" target="_self" data-toggle="tab">Entrega</a></li>
		                        <li><a href="#manufactureRequests" target="_self" data-toggle="tab" ng-click="openManufactureHistory(historyItem)">Produção</a></li>
		                    </ul>
		                    <div class="tab-content">
        						<div class="tab-pane fade in active" id="deliveryRequests">
        							<table class="table table-bordered table-hover history-table">
					                    <thead>
					                        <tr>
					                            <th>Data</th>
					                            <th>Solicitante</th>
					                            <th>{{historyItem.sizeName}}</th>
					                            <th>Valor Unit.</th>
					                            <th>Quantidade</th>
					                            <th>Valor Total</th>
					                            <th>Status</th>
					                        </tr>
					                    </thead>
					                    <tbody>
											<tr ng-repeat="item in deliveryRequests">
					                            <td>{{item.date | date:'dd/MM/yyyy'}}</td>
					                            <td>{{item.franchisee.name}}</td>
					                            <td>{{item.productSize.name}}</td>
					                            <td>{{item.deliveryUnitPrice | currency:undefined:4}}</td>
					                            <td>{{item.quantity}} {{item.product.unit}}</td>
					                            <td>{{item.calculateTotal() | currency}}</td>
					                            <td>
					                            	<delivery-request-history-status  delivery-request="item" />
					                            </td>                      
					                        </tr>
					                    </tbody>
				                	</table>
				                	<button ng-show="deliveryRequestsCursorString != null" type="button" class="btn btn-default btn-sm btn-block" ng-click="loadDeliveryHistory(historyItem)">Carregar mais</button>
	                				<p ng-show="deliveryRequests.length == 0" class="text-info text-center">Não há histórico!</p>
        						</div><!--/end tab-pane -->
        						<div class="tab-pane fade in" id="manufactureRequests">
									<table class="table table-bordered table-hover history-table">
					                    <thead>
					                        <tr>
					                            <th>Data</th>
					                            <th>{{historyItem.sizeName}}</th>
					                            <th>Valor Unit.</th>
					                            <th>Quantidade</th>
					                            <th>Valor Total</th>
					                            <th>Status</th>
					                        </tr>
					                    </thead>
					                    <tbody>
											<tr ng-repeat="item in manufactureRequests">
					                            <td>{{item.date | date:'dd/MM/yyyy'}}</td>
					                            <td>{{item.productSize.name}}</td>
					                            <td>{{item.manufactureUnitPrice | currency:undefined:4}}</td>
					                            <td>{{item.quantity}} {{item.product.unit}}</td>
					                            <td>{{item.calculateTotal() | currency}}</td>
					                            <td>
					                            	<status-label status="item.status">{{item.statusDescription}}</status-label>
					                            	<div class="margin-top-5 text-danger" ng-show="item.status == 'CANCELLED'"><small>{{item.cancellationComment}}</small></div>
					                            </td>                      
					                        </tr>
					                    </tbody>
				                	</table>
				                	<button ng-show="manufactureRequestsCursorString != null" type="button" class="btn btn-default btn-sm btn-block" ng-click="loadManufactureHistory(historyItem)">Carregar mais</button>
	                				<p ng-show="manufactureRequests.length == 0" class="text-info text-center">Não há histórico!</p>
        						</div><!--/end tab-pane -->
		                    </div><!--/end tab-content -->
	                	</div><!--/end tab-->
	                	
	                </div>
	            </div>
	        </div>
	        <div class="modal-footer">
	            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        </div>
	        <div id="history-loadingArea"></div>
	    </div>
	</div>
</div><!--/end modal-->