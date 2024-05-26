<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Products -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-gift"></i> <c:out value="${domainConfiguration.labels['PRODUCTS']}">Produtos</c:out></h3>
</div>
<div class="panel panel-default">
	<div class="panel-body panel-body-with-subpanels">
		
		<div class="panel panel-default subpanel" ng-repeat="category in productCategories | orderBy:'order'">
			<div class="panel-heading pointer-cursor" ng-click="toggleCategory(category)">
				<i class="fa" ng-class="{'fa-angle-up': category.open, 'fa-angle-down': !category.open}"></i>
				<h3 class="panel-title">{{category.name}}</h3>
			</div>
			<div class="panel-list-group" ng-show="category.open">
				<ul class="list-group">
					<li class="list-group-item list-group-item-notes list-group-item-warning" ng-show="{{category.notes != ''}}">{{category.notes}}</li>
					<li class="list-group-item text-info text-center" ng-show="category.products.length == 0">Não há produtos nesta categoria!</li>
					<li class="list-group-item" ng-repeat="item in category.products | orderBy:'name'">
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
				                <ul class="list-unstyled margin-bottom-20" ng-show="item.catalogName" ng-if="item.type == 'CATALOG'">
				                	<li><i class="fa fa-check"></i> <strong>Catálogo:</strong> <a href ng-href="/ws/documents/catalog/{{item.id}}" target="_blank" title="Abrir catálogo"><file-icon content-type="item.catalogContentType" /> {{item.catalogName}}</a></li>
			                    </ul>
			                    <ul class="list-unstyled margin-bottom-20" ng-if="item.type != 'CATALOG'">
			                    	<li><i class="fa fa-check"></i> <strong>Valor Unitário:</strong> {{item.selectedSize.unitPrice | currency:undefined:4}} / {{item.unit}}</li>
			                        <li ng-show="item.sizes.length > 1"><i class="fa fa-check"></i> <strong>{{item.sizeName}}: </strong> <span ng-show="item.selectedSize.groupName != null && item.selectedSize.groupName != ''">{{item.selectedSize.groupName}} - </span>{{item.selectedSize.name}}<span ng-show="item.selectedSize.description != null && item.selectedSize.description != ''"> - {{item.selectedSize.description}}</span></li>
			                        <li ng-show="item.hasDeliveryUnit"><i class="fa fa-check"></i> <strong>Formato de Entrega:</strong> {{item.deliveryUnit}} com {{item.selectedSize.deliveryQty}} {{item.unit}}</li>
			                        <li><i class="fa fa-check"></i> <strong>Status: <span ng-show="item.selectedSize.isAvailable" class="text-success">Disponível</span><span ng-show="!item.selectedSize.isAvailable" class="text-danger">Indisponível</span></strong></li>			                        
			                    </ul>
			                    <ul class="list-unstyled margin-bottom-20" ng-if="item.type == 'WITH_STOCK_CONTROL'">
			                        <li ng-show="item.hasManufactureMinQty"><i class="fa fa-check"></i> <strong>Qde Min. Produção:</strong> {{item.selectedSize.manufactureMinQty}} <span ng-show="item.hasDeliveryUnit">{{item.deliveryUnit}} = {{item.calculateManufactureMinTotalQty()}} </span>{{item.unit}}</li>
			                        <li ng-show="item.manufactureTime > 0"><i class="fa fa-check"></i> <strong>Prazo de Produção:</strong> {{item.manufactureTime}} dia<spam ng-show="item.manufactureTime != 1">s</spam></li>
			                        <li><i class="fa fa-check"></i> <strong>Estoque Mínimo: </strong> {{item.selectedSize.minStock}} {{item.unit}}</li>
			                    </ul>
			                    <div class="row margin-left-0" ng-if="item.sizes.length > 1 && item.type != 'CATALOG'" ng-hide="item.editPrice">
			                    	<strong>Opções de {{item.sizeName}}:</strong>
			                    </div>
			                    <div class="row other-sizes" ng-if="item.sizes.length > 1 && item.groupSizes && item.type != 'CATALOG'" ng-hide="item.editPrice">
			                    	<div class="col-sm-9">
										<ul class="other-sizes-list">
										  <li class="other-sizes-list-group" ng-repeat="(groupName, sizes) in item.sizes | groupBy: 'groupName'">
										    <div class="other-sizes-list-group-title">{{ groupName }}</div>
										    <ul class="other-sizes-inner-list">
										      <li class="other-sizes-list-item pointer-cursor" ng-class="selectedSizeClass(item, size)" ng-repeat="size in sizes"  ng-click="setSelectedSize(item, size)">
										        {{selectDescription(item, size)}} <small class="pull-right">{{size.unitPrice | currency:undefined:4}}</small>
										      </li>
										    </ul>
										  </li>
										</ul>
			                    	</div>												                    	
								</div>
			                    <div class="row other-sizes" ng-if="item.sizes.length > 1 && !item.groupSizes && item.type != 'CATALOG'" ng-hide="item.editPrice">
			                    	<div class="col-sm-9">
				                    	<ul class="other-sizes-list">
				                    		<li class="other-sizes-list-item pointer-cursor" ng-class="selectedSizeClass(item, size)" ng-repeat="size in item.sizes | orderBy:['groupName','name']" ng-click="setSelectedSize(item, size)">
				                    			{{selectDescription(item, size)}} <small class="pull-right">{{size.unitPrice | currency:undefined:4}}</small>
				                    		</li>
				                    	</ul>
			                    	</div>
			                    </div>
				                <div class="list-group-item-actions" ng-hide="item.editPrice" ng-if="item.type != 'CATALOG'">
				                    <ul class="list-inline comment-list-v2 pull-left">
				                    	<li><i class="fa fa-file-text-o"></i> <a href="#" target="_self" ng-click="openDeliveryHistory(item)" data-toggle="modal" data-target="#historyModal">Histórico</a></li>
				                        <li ng-if="item.allowSupplierChangeUnitPrice"><i class="fa fa-usd"></i> <a href="javascript:void(0);" ng-click="editPrice(item)">Alterar preço</a></li>
				                        <li ng-if="item.selectedSize.isAvailable"><i class="fa fa-toggle-on"></i> <a href="javascript:void(0);" ng-click="toggleIsAvailable(item)">Tornar indisponível</a></li>
				                        <li ng-if="!item.selectedSize.isAvailable"><i class="fa fa-toggle-off"></i> <a href="javascript:void(0);" ng-click="toggleIsAvailable(item)">Tornar disponível</a></li>				                        
				                    </ul>
				                </div>
				                <div class="list-group-item-actions" ng-if="item.allowSupplierChangeUnitPrice" ng-show="item.editPrice">
									<h4><strong>Alteração de Preço</strong></h2>
									<form class="form-inline">
				                        <ul class="list-unstyled">
				                        	<li>
				                        		<strong>Novo Preço: </strong> R$
				                        		<input type="text" class="form-control" style="height: 22px; width: 65px; padding: 0px 2px; font-size: 13px;" currency ng-model="item.updateRequest.unitPrice" placeholder="0,0000" onkeypress="return checkCurrencyValue(event);" onblur="javascript:formatCurrencyField(this);"/>
		                        				/ {{item.unit}}
				                        	</li>
				                            <li>
				                            	<div class="top-buffer">
					                            	<button class="btn btn-default btn-xs" type="button" ng-click="saveEditPrice(item)">Salvar</button>
			                            			<button class="btn btn-default btn-xs" type="button" ng-click="cancelEditPrice(item)">Cancelar</button>
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
		</div>
		
	</div>
</div>
<div id="products-loadingArea"></div>

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
	                	<div class="tab-v1">
	                		<ul class="nav nav-tabs">
	                			<li class="active"><a href="#deliveryRequests" target="_self" data-toggle="tab">Entrega</a></li>
		                        <li ng-if="historyItem.type == 'WITH_STOCK_CONTROL'"><a href="#manufactureRequests" target="_self" data-toggle="tab" ng-click="openManufactureHistory(historyItem)">Produção</a></li>
		                        <li><a href="#priceHistory" target="_self" data-toggle="tab" ng-click="openPriceHistory(historyItem)">Preços</a></li>
		                    </ul>
		                    <div class="tab-content">
		                    	<div class="tab-pane fade in active" id="deliveryRequests">
        							<table class="table table-bordered table-hover history-table">
					                    <thead>
					                        <tr>
					                            <th>Data</th>
					                            <th>Solicitante</th>
					                            <th ng-show="historyItem.sizes.length > 1">{{historyItem.sizeName}}</th>
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
					                            <td ng-show="historyItem.sizes.length > 1">{{item.productSize.name}}</td>
					                            <td>{{item.deliveryUnitPrice | currency:undefined:4}}</td>
					                            <td>{{item.quantity}} {{item.product.unit}}</td>
					                            <td>{{item.calculateTotal() | currency}}</td>
					                            <td>
					                            	<delivery-request-history-status  delivery-request="item" />
					                            </td>                      
					                        </tr>
					                    </tbody>
				                	</table>
				                	<button ng-show="deliveryRequestsCursorString != 'none' && deliveryRequestsCursorString != null" type="button" class="btn btn-default btn-sm btn-block" ng-click="loadDeliveryHistory(historyItem)">Carregar mais</button>
	                				<p ng-show="deliveryNoRecordsFound" class="text-info text-center">Não há histórico de entrega!</p>
        						</div><!--/end tab-pane -->
        						<div class="tab-pane fade in" id="manufactureRequests">
									<table class="table table-bordered table-hover history-table">
					                    <thead>
					                        <tr>
					                            <th>Data</th>
					                            <th ng-show="historyItem.sizes.length > 1">{{historyItem.sizeName}}</th>
					                            <th>Valor Unit.</th>
					                            <th>Quantidade</th>
					                            <th>Valor Total</th>
					                            <th>Status</th>
					                        </tr>
					                    </thead>
					                    <tbody>
											<tr ng-repeat="item in manufactureRequests">
					                            <td>{{item.date | date:'dd/MM/yyyy'}}</td>
					                            <td ng-show="historyItem.sizes.length > 1">{{item.productSize.name}}</td>
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
				                	<button ng-show="manufactureRequestsCursorString != 'none' && manufactureRequestsCursorString != null" type="button" class="btn btn-default btn-sm btn-block" ng-click="loadManufactureHistory(historyItem)">Carregar mais</button>
	                				<p ng-show="manufactureNoRecordsFound" class="text-info text-center">Não há histórico de produção!</p>
        						</div><!--/end tab-pane -->  
        						<div class="tab-pane fade in" id="priceHistory">
									<table class="table table-bordered table-hover history-table">
					                    <thead>
					                        <tr>
					                            <th>Data</th>
					                            <th ng-show="historyItem.sizes.length > 1">{{historyItem.sizeName}}</th>
					                            <th>Valor Anterior</th>
					                            <th>Novo Valor</th>
					                            <th>Alterado por</th>
					                        </tr>
					                    </thead>
					                    <tbody>
											<tr ng-repeat="item in productSizePriceHistories">
					                            <td>{{item.date | date:'dd/MM/yyyy HH:mm'}}</td>
					                            <td ng-show="historyItem.sizes.length > 1">{{item.productSize.name}}</td>
					                            <td>{{item.oldUnitPrice | currency:undefined:4}}</td>
					                            <td>{{item.newUnitPrice | currency:undefined:4}}</td>
					                            <td>{{item.user.name}}</td>              
					                        </tr>
					                    </tbody>
				                	</table>
				                	<button ng-show="priceHistoryCursorString != 'none' && priceHistoryCursorString != null" type="button" class="btn btn-default btn-sm btn-block" ng-click="loadPriceHistory(historyItem)">Carregar mais</button>
	                				<p ng-show="priceHistoryNoRecordsFound" class="text-info text-center">Não há histórico de preços!</p>
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