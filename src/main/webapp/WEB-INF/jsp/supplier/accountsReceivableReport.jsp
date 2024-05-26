<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Account Receivables -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-money"></i> Contas a Receber</h3>
</div>
<div class="panel panel-default">
	<div class="panel-body">
		<div class="panel panel-list">
		    <div class="panel-heading"><!--  overflow-h -->
		    	<form>
		    		<div class="row">
		            	<div class="col-xs-6">
							<div class="form-group">
		 						<div class="input-group">
						          <div class="input-group-addon">De</div>
		  						  <input type="text" class="form-control" uib-datepicker-popup="dd/MM/yyyy" ng-model="filter.fromDate" is-open="filter.fromDateOpened" datepicker-options="dateOptions" show-button-bar="False" alt-input-formats="altInputFormats" on-open-focus="False" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
						          <span class="input-group-btn">
						            <button type="button" class="btn btn-default" ng-click="openFromDatePicker()"><i class="glyphicon glyphicon-calendar" style="padding: 3px;"></i></button>
						          </span>
						        </div>
							</div>
						</div>
						<div class="col-xs-6">
							<div class="form-group">
		 						<div class="input-group">
						          <div class="input-group-addon">Até</div>
		  						  <input type="text" class="form-control" uib-datepicker-popup="dd/MM/yyyy" ng-model="filter.toDate" is-open="filter.toDateOpened" datepicker-options="dateOptions" show-button-bar="False" alt-input-formats="altInputFormats" on-open-focus="False" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
						          <span class="input-group-btn">
						            <button type="button" class="btn btn-default" ng-click="openToDatePicker()"><i class="glyphicon glyphicon-calendar" style="padding: 3px;"></i></button>
						          </span>
						        </div>
							</div>
						</div>
					</div>
		            <button class="btn btn-default btn-block" ng-click="searchDeliveryRequests()"><i class="fa fa-search"> Pesquisar</i></button>               	
		        </form>
		    </div>
			<div class="panel-body">			
	        	<table class="table table-bordered table-hover history-table">
	                <thead>
	                    <tr>
	                        <th>Data</th>
	                        <th><c:out value="${domainConfiguration.labels['PRODUCT']}">Produto</c:out></th>
	                        <th><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISOR']}">Franquia</c:out></th>
	                        <th><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISEE']}">Loja</c:out></th>						                            
	                        <th>Valor Total</th>
	                        <th>Boleto</th>
	                    </tr>
	                </thead>
	                <tbody>
						<tr ng-repeat="item in deliveryRequests | orderBy:'date':true">
	                        <td>{{item.dueDate | date:'dd/MM/yyyy'}}</td>
	                        <td>{{item.product.name}}<span ng-show="item.product.sizes.length > 1"> - {{item.product.sizeName}}: {{item.productSize.name}}</span></td>						                            
	                        <td>{{item.franchisor.name}}</td>
	                        <td>{{item.franchisee.name}}</td>
	                        <td>{{item.calculateTotal() | currency}}</td>     
	                        <td>
	                        	<ul class="delivery-info">
	                        		<li ng-show="item.paymentSlipKey"><payment-slip-download-link delivery-request="item" /></li>
	                        	</ul>
                            </td>                  
	                    </tr>
	                </tbody>
	        	</table>
	        	<button ng-show="cursorString != 'none' && cursorString != null" type="button" class="btn btn-default btn-sm btn-block" ng-click="fetchDeliveryRequests()">Carregar mais</button>
	        	<p ng-show="noRecordsFound" class="text-info text-center">Não há entregas a receber para este filtro!</p>
			</div>
		</div>
    </div>
    <!-- Loading -->
	<div id="deliveryLoad"></div>
    <!-- end loading -->
</div>