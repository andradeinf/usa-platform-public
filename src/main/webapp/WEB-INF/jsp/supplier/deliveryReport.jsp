<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Delivery Report -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-file-text-o"></i> Entregas por Status</h3>
</div>
<div class="panel panel-default">
	<div class="panel-body">
		<div class="panel panel-list">
		    <div class="panel-heading overflow-h">
		    	<form>
		        	<div class="form-group">
		                <label for="franchisor"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISOR']}">Franquia</c:out></label>		
		                <select id="franchisor" class="form-control" ng-model="franchisorId" ng-options="unit.id as unit.name for unit in franchisors | orderBy:'name'" ng-change="loadFranchisees()"></select>     
		            </div> 
		            <div class="form-group" ng-show="franchisorId != 0">
		                <label for="franchisee"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISEE']}">Loja</c:out></label>		
		                <select id="franchisee" class="form-control" ng-model="franchiseeId" ng-options="unit.id as unit.name for unit in franchisees | orderBy:'name'" ng-change="reloadDeliveryRequests()"></select>     
		            </div>
		            <div class="form-group">
  						<label for="status">Status</label>
  						<select id="status" class="form-control" ng-model="deliveryStatus" ng-options="unit.key as unit.value for unit in deliveryStatuses" ng-change="reloadDeliveryRequests()"></select>
					</div>                     	
		        </form>
		    </div>
			<div class="panel-body">
				<!-- FRANCHISEE -->
    			<div class="shadow-wrapper" ng-show="franchiseeId != 0">
                    <div class="franchisee-details-box box-shadow shadow-effect-2">
						<h2>{{franchiseesDetails[franchiseeId].name}}</h2>
                        <ul class="list-unstyled">
	                        <li ng-if="franchiseesDetails[franchiseeId].corporateName != ''"><strong><c:out value="${domainConfiguration.labels['FRANCHISOR_CORPORATE_NAME']}">Razão Social</c:out>:</strong> {{franchiseesDetails[franchiseeId].corporateName}}</li>
	                        <li ng-if="franchiseesDetails[franchiseeId].fiscalId != ''"><strong><c:out value="${domainConfiguration.labels['FRANCHISOR_FISCAL_ID']}">CNPJ</c:out>:</strong> {{franchiseesDetails[franchiseeId].fiscalId}}</li>
	                        <li ng-if="franchiseesDetails[franchiseeId].address != ''"><strong>Endereço:</strong> {{franchiseesDetails[franchiseeId].address}}</li>
	                        <li ng-if="franchiseesDetails[franchiseeId].contactName != ''"><strong>Contato:</strong> {{franchiseesDetails[franchiseeId].contactName}}</li>
	                        <li ng-if="franchiseesDetails[franchiseeId].contactPhone != ''"><strong>Telefone:</strong> {{franchiseesDetails[franchiseeId].contactPhone}}</li>
	                        <li ng-if="franchiseesDetails[franchiseeId].contactEmail != ''"><strong>E-mail p/ envio da Nota Fiscal:</strong> {{franchiseesDetails[franchiseeId].contactEmail}}</li>
	                        <li ng-if="franchiseesDetails[franchiseeId].additionalInformation != ''"><div ta-bind="text" ng-model="franchiseesDetails[franchiseeId].additionalInformation"></div></li>
	                    </ul>
                    </div>
                </div>
			
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
	                        <td>{{item.product.name}}<span ng-show="item.product.sizes.length > 1"> - {{item.product.sizeName}}: {{item.productSize.name}}</span></td>						                            
	                        <td>{{item.deliveryUnitPrice | currency:undefined:4}}</td>
	                        <td>{{item.quantity}} {{item.product.unit}}</td>
	                        <td>{{item.calculateTotal() | currency}}</td>
	                        <td>
	                        	<delivery-request-history-status  delivery-request="item" />
	                        </td>                          
	                    </tr>
	                </tbody>
	        	</table>
	        	<button ng-show="cursorString != 'none' && cursorString != null" type="button" class="btn btn-default btn-sm btn-block" ng-click="fetchDeliveryRequests()">Carregar mais</button>
	        	<p ng-show="noRecordsFound" class="text-info text-center">Não há histórico!</p>
			</div>
		</div>
    </div>
    <!-- Loading -->
	<div id="deliveryLoad"></div>
    <!-- end loading -->
</div>