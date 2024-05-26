<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Update Modal -->
<div class="modal fade" id="franchiseeDetailsModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Detalhes para emissão da Nota Fiscal</h4>
	        </div>
	        <div class="modal-body">
				<div class="row">
					<div class="col-sm-4">
						<div class="thumbnail">
							<img class="img-responsive" ng-src="{{getImageURL(deliveryRequestsFranchisors[deliveryRequestsFranchisees[franchiseeId].franchisorId])}}" alt="" />
						</div>
					</div>
					<div class="col-sm-8">
						<h2>{{deliveryRequestsFranchisees[franchiseeId].name}}</h2>
						<ul class="list-unstyled">
							<li ng-if="deliveryRequestsFranchisees[franchiseeId].corporateName != ''"><strong><c:out value="${domainConfiguration.labels['FRANCHISOR_CORPORATE_NAME']}">Razão Social</c:out></strong> {{deliveryRequestsFranchisees[franchiseeId].corporateName}}</li>
							<li ng-if="deliveryRequestsFranchisees[franchiseeId].fiscalId != ''"><strong><c:out value="${domainConfiguration.labels['FRANCHISOR_FISCAL_ID']}">CNPJ</c:out>:</strong> {{deliveryRequestsFranchisees[franchiseeId].fiscalId}}</li>
							<li ng-if="deliveryRequestsFranchisees[franchiseeId].address != ''"><strong>Endereço:</strong> {{deliveryRequestsFranchisees[franchiseeId].address}}</li>
							<li ng-if="deliveryRequestsFranchisees[franchiseeId].cityId != '' || deliveryRequestsFranchisees[franchiseeId].cityId != 0"><strong>Cidade:</strong> {{deliveryRequestsCities[deliveryRequestsFranchisees[franchiseeId].cityId].name}}</li>
							<li ng-if="deliveryRequestsFranchisees[franchiseeId].stateId != '' || deliveryRequestsFranchisees[franchiseeId].stateId != 0"><strong>Estado:</strong> {{deliveryRequestsStates[deliveryRequestsFranchisees[franchiseeId].stateId].name}}</li>
							<li ng-if="deliveryRequestsFranchisees[franchiseeId].contactName != ''"><strong>Contato:</strong> {{deliveryRequestsFranchisees[franchiseeId].contactName}}</li>
							<li ng-if="deliveryRequestsFranchisees[franchiseeId].contactPhone != ''"><strong>Telefone:</strong> {{deliveryRequestsFranchisees[franchiseeId].contactPhone}}</li>
							<li ng-if="deliveryRequestsFranchisees[franchiseeId].contactEmail != ''"><strong>E-mail p/ envio da Nota Fiscal:</strong> {{deliveryRequestsFranchisees[franchiseeId].contactEmail}}</li>
							<li ng-if="deliveryRequestsFranchisees[franchiseeId].additionalInformation != ''"><div ta-bind="text" ng-model="deliveryRequestsFranchisees[franchiseeId].additionalInformation"></div></li>
						</ul>
					</div>
				</div>
	        </div>
	        <div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Fechar</button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- Update Modal -->
<div class="modal fade" id="updateDeliveryRequestsModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Atualizar Pedidos</h4>
	        </div>
	        <div class="modal-body">
	        	<form>
					<div class="form-group">
   						<label for="status">Atualizar status para:</label>
   						<select id="status" class="form-control input-sm" ng-model="updateRequest.status" ng-options="unit.key as unit.value for unit in deliveryStatuses"></select>
 					</div>
 					<div class="row" ng-show="updateRequest.status == 'PENDING_WITH_RESTRICTION'">
		            	<div class="col-xs-6">
							<div class="form-group" id="autoCancellationDate">
		 						<label for="inpuyAutoCancellationDate">Data para Cancelamento Automático:</label>
		 						<input id="inpuyAutoCancellationDate" type="text" class="form-control" ng-model="updateRequest.autoCancellationDate" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
							</div>
						</div>
					</div>
					<div class="row" ng-show="updateRequest.status == 'COMPLETED'">
		            	<div class="col-xs-6">
							<div class="form-group">
		 						<label for="sentDate">Data do envio:</label>
		 						<input id="sentDate" type="text" class="form-control" ng-model="updateRequest.sentDate" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
							</div>
						</div>
						<div class="col-xs-6">
							<div class="form-group">
		 						<label for="deadlineDate">Data Limite para Entrega:</label>
		 						<input id="deadlineDate" type="text" class="form-control" ng-model="updateRequest.deadlineDate" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
							</div>
						</div>
					</div>
					<div class="row" ng-show="updateRequest.status == 'COMPLETED'">
		            	<div class="col-xs-6">
							<div class="form-group">
		 						<label for="carrierName">Transportadora:</label>
		 						<input id="carrierName" type="text" class="form-control" ng-model="updateRequest.carrierName"/>
							</div>
						</div>
						<div class="col-xs-6">
							<div class="form-group">
		 						<label for="trackingCode">Código de rastreamento:</label>
		 						<input id="trackingCode" type="text" class="form-control" ng-model="updateRequest.trackingCode"/>
							</div>
						</div>
					</div>
					<div class="row" ng-show="updateRequest.status == 'COMPLETED'">
						<div class="col-xs-6">
							<div class="form-group" id="fiscalNumber">
		 						<label for="inputFiscalNumber">Número da Nota Fiscal:</label>
		 						<input id="inputFiscalNumber" type="text" class="form-control" ng-model="updateRequest.fiscalNumber"/>
							</div>
						</div>
						<div class="col-xs-6">
		            		<div class="form-group" id="fiscalFile" ng-show="updateRequest.fiscalFileId == 0">
				                <label for="inputFiscalFile">Nota Fiscal:</label>
			                	<input id="inputFiscalFile" type="file" class="form-control" file-model="fiscalFile" onchange="angular.element(this).scope().doAddFiscalFile(this)"/>
				            </div>
				            <div class="form-group" id="fiscalFile" ng-hide="updateRequest.fiscalFileId == 0">
				                <label for="inputFiscalFile">Nota Fiscal:</label>
				                <div class="attachments">
									<div class="row attachment">
										<div class="col-sm-8">
											<span ng-if="updateRequestFiscalFile.id == 0">{{updateRequestFiscalFile.name}}</span>
											<span ng-if="updateRequestFiscalFile.id != 0"><a href ng-href="/ws/delivery/fileUpload/downloadUploadedFile/{{updateRequestFiscalFile.id}}">{{updateRequestFiscalFile.name}}</a></span>
										</div>
										<div class="col-sm-3">
											<span ng-if="updateRequestFiscalFile.error.length > 0" class="attachment-error">{{updateRequestPaymentSlip.error}}</span>
											<div ng-show="updateRequestFiscalFile.progressVisible" class="progress attachment-bar">
										      <div class="progress-bar active" role="progressbar" aria-valuenow="{{updateRequestFiscalFile.progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{updateRequestFiscalFile.progress}}%">
										        <span class="sr-only">{{updateRequestFiscalFile.progress}}%</span>
										      </div>
										    </div>						    
										</div>		
										<div class="col-sm-1">
									    	<span class="pull-right pointer-cursor" ng-click="deleteFiscalFile()" title="Excluir Nota Fiscal"><b>x</b></span>
									    </div>				
									</div>
								</div>								
				            </div>				            
						</div>
					</div>
					<div class="row" ng-show="updateRequest.status == 'COMPLETED' && updateRequest.fiscalFileId != 0">
						<div class="col-xs-12">
							<p class="help-block text-center">Ao salvar, os pedidos de entrega abaixo que já possuem uma nota fiscal anexa terão o arquivo substituído.</p>
						</div>
					</div>
					<div class="row" ng-show="updateRequest.status == 'WAITING_PAYMENT' || updateRequest.status == 'COMPLETED'">
		            	<div class="col-xs-6">
							<div class="form-group" id="dueDate">
		 						<label for="inputDueDate">Data de Vencimento do Boleto:</label>
		 						<input id="inputDueDate" type="text" class="form-control" ng-model="updateRequest.dueDate" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
							</div>
						</div>
		            	<div class="col-xs-6">
		            		<div class="form-group" id="paymentSlipFile" ng-show="updateRequest.paymentSlipId == 0">
				                <label for="inputPaymentSlipFile">Boleto para pagamento:</label>
			                	<input id="inputPaymentSlipFile" type="file" class="form-control" file-model="paymentSlipFile" onchange="angular.element(this).scope().doAddPaymentSlip(this)"/>
				            </div>
				            <div class="form-group" id="file" ng-hide="updateRequest.paymentSlipId == 0">
				                <label for="inputPaymentSlipFile">Boleto para pagamento:</label>
				                <div class="attachments">
									<div class="row attachment">
										<div class="col-sm-8">
											<span ng-if="updateRequestPaymentSlip.id == 0">{{updateRequestPaymentSlip.name}}</span>
											<span ng-if="updateRequestPaymentSlip.id != 0"><a href ng-href="/ws/delivery/fileUpload/downloadUploadedFile/{{updateRequestPaymentSlip.id}}">{{updateRequestPaymentSlip.name}}</a></span>
										</div>
										<div class="col-sm-3">
											<span ng-if="updateRequestPaymentSlip.error.length > 0" class="attachment-error">{{updateRequestPaymentSlip.error}}</span>
											<div ng-show="updateRequestPaymentSlip.progressVisible" class="progress attachment-bar">
										      <div class="progress-bar active" role="progressbar" aria-valuenow="{{updateRequestPaymentSlip.progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{updateRequestPaymentSlip.progress}}%">
										        <span class="sr-only">{{updateRequestPaymentSlip.progress}}%</span>
										      </div>
										    </div>						    
										</div>		
										<div class="col-sm-1">
									    	<span class="pull-right pointer-cursor" ng-click="deletePaymentSlip()" title="Excluir Boleto"><b>x</b></span>
									    </div>				
									</div>
								</div>								
				            </div>				            
						</div>						
					</div>
					<div class="row" ng-show="(updateRequest.status == 'WAITING_PAYMENT' || updateRequest.status == 'COMPLETED') && updateRequest.paymentSlipId != 0">
						<div class="col-xs-12">
							<p class="help-block text-center">Ao salvar, os pedidos de entrega abaixo que já possuem um boleto anexo terão o arquivo substituído.</p>
						</div>
					</div>
 					<div class="form-group" ng-show="updateRequest.status == 'CANCELLED'">
						<label for="cancellationComment">Motivo do Cancelamento:</label>
						<textarea id="cancellationComment" class="form-control" ng-model="updateRequest.cancellationComment" rows="3" placeholder="Informe o motivo do cancelamento"></textarea>
					</div>
		        </form>
				<table class="table table-bordered table-hover history-table">
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
						<tr ng-repeat="item in updateRequestItems | orderBy:'date':true">
                            <td>{{item.date | date:'dd/MM/yyyy'}}</td>
                            <td>{{item.product.name}} <span ng-show="item.product.sizes.length > 1"> - {{item.product.sizeName}}: {{item.productSize.name}}</span></td>
                            <td>
                            	<input ng-show="updateRequest.status == 'COMPLETED' && !updateProcessed" type="text" class="form-control" style="height: 22px; width: 30px; padding: 0px 2px; display: inline" ng-model="item.deliveryQty" onkeypress="return checkOnlynumbers(event);"/>
                            	<span ng-show="item.product.hasDeliveryUnit && (updateRequest.status != 'COMPLETED' || updateProcessed)">{{item.deliveryQty}}</span>
                            	<span ng-show="item.product.hasDeliveryUnit"> {{item.product.deliveryUnit}} = </span>
                            	<span ng-show="updateRequest.status != 'COMPLETED' || item.product.hasDeliveryUnit">{{item.calculateTotalQuantity()}}</span> {{item.product.unit}}</td>
                            <td>
                            	<span ng-show="item.product.allowChangeUnitPrice && updateRequest.status == 'COMPLETED' && !updateProcessed">R$<input type="text" class="form-control" currency style="height: 22px; width: 60px; padding: 0px 2px; display: inline" ng-model="item.updateStatusRequest.deliveryUnitPrice" placeholder="0,0000" onkeypress="return checkCurrencyValue(event);" onblur="javascript:formatCurrencyField(this);"/></span>
		                        <span ng-show="!item.product.allowChangeUnitPrice || updateRequest.status != 'COMPLETED' || updateProcessed">{{item.updateStatusRequest.deliveryUnitPrice | currency:undefined:4}}</span>
                            </td>
                            <td>{{item.calculateDeliveryUpdated() | currency}}</td>   
                        </tr>
                    </tbody>
               	</table>
	        </div>
	        <div class="modal-footer">
        		<button ng-hide="updateProcessed" type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Cancelar</button>
    			<button ng-hide="updateProcessed" ng-click="saveEditDelivery()" class="btn btn-default"><i class="fa fa-save"></i> Salvar</button>
    			<button ng-show="updateProcessed" type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Fechar</button>
	        </div>
	    </div>
	    <div id="loadingModalArea"></div>
	</div>
</div><!--/end modal-->

<!-- Requests -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-bar-chart-o"></i> Pedidos de Entrega</h3>
</div>
<div class="panel panel-default">
	<div class="panel-heading clearfix" ng-show="franchiseeSelected()">
    	<div class="pull-right">
			<button class="btn btn-default" ng-click="printDeliveryRequests()"><i class="fa fa-print"> Imprimir Pedidos</i></button>
			<button class="btn btn-default" ng-click="updateDeliveryRequests()"><i class="fa fa-edit"> Atualizar Pedidos</i></button>
		</div>
    </div>
	<div class="panel-body panel-filter">
		<form>
			<div class="form-group">
				<label for="status">Status</label>
				<select id="status" class="form-control" ng-model="deliveryStatus" ng-options="unit.key as unit.value for unit in deliveryStatuses" ng-change="loadDeliveryRequests()"></select>
			</div>
			<div class="form-group">
				<label for="franchisee"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISEE']}">Loja</c:out></label>
				<div class="input-group">
					<select id="franchisee" class="form-control" ng-model="franchiseeId" ng-options="unit.id as deliveryRequestsFranchisors[unit.franchisorId].name + ' - ' + unit.name for unit in deliveryRequestsFranchiseesList | orderBy:'name'" ng-change="selectFranchisee()"></select>
					<span class="input-group-btn">
						<button ng-disabled="!franchiseeSelected()" type="button" class="btn btn-secondary"  data-toggle="modal" data-target="#franchiseeDetailsModal" title="Detalhes para emissão da Nota Fiscal"><i class="fa fa-file-text-o"></i> Detalhes para NF</button>
					</span>
				</div>
			</div>
		</form>
		<div ng-show="franchiseeSelected()">
			<input type="checkbox" ng-click="selectAll()" ng-model="updateAllCheck"> Selecionar Todos
		</div>
	</div>
	<div class="panel-list-group" ng-show="franchiseeSelected()">
		<ul class="list-group">
			<li class="list-group-item" ng-repeat="item in deliveryRequests | filter:franchiseeId | orderBy:'date':true">
				<div class="row list-group-item-row">
					<div class="col-sm-3 list-group-item-column">
						<div class="thumbnail zoomer">
							<a class="fancybox-button" rel="fancybox-button-{{item.id}}" title="{{item.product.name}}" ng-href="{{item.product.imageURL}}">
								<img class="img-responsive" ng-src="{{getImageURL(item.product)}}" alt="" />
								<img class="zoom-icon" src="/img/overlay-icon.png" alt="" />
								<i class="zoomer-icon add-icon zoom-fa fa fa-search-plus"></i>
							</a>
						</div>
						<div class="text-center">
							<input type="checkbox" ng-model="item.updateCheck"> Selecionar
						</div>						
					</div>
					<div class="col-sm-9 list-group-item-column">
						<h4 class="list-group-item-title">
							<strong>{{item.product.name}}<span ng-show="item.product.sizes.length > 1"> - {{item.product.sizeName}}: {{item.productSize.name}}</span></strong>
							<small>{{item.date | date:'dd/MM/yyyy HH:mm'}}</small>
						</h4>
						<p ta-bind="text" ng-model="item.product.description"></p>
						<ul class="list-unstyled">
							<li ng-show="item.product.sizes.length > 1"><i class="fa fa-check"></i> <strong>{{item.product.sizeName}}:</strong> {{item.productSize.name}} <spam ng-show="item.productSize.description != null && item.productSize.description != ''"> - {{item.productSize.description}}</spam></li>
							<li><i class="fa fa-check"></i> <strong>Quantidade:</strong> <span ng-show="item.product.hasDeliveryUnit">{{item.quantity / item.productSize.deliveryQty}} {{item.product.deliveryUnit}} = </span>{{item.quantity}} {{item.product.unit}}</span></li>
							<li><i class="fa fa-check"></i> <strong>Valor Unitário:</strong> {{item.deliveryUnitPrice | currency:undefined:4}} / {{item.product.unit}}</li>
							<li><i class="fa fa-check"></i> <strong>Valor do Pedido:</strong> {{item.calculateDelivery() | currency}}</li>
							<li ng-show="deliveryRequestsProducts[item.productId].deliveryTime > 0"><i class="fa fa-check"></i> <strong>Prazo de Preparação p/Entrega:</strong> {{item.product.deliveryTime}} dia<spam ng-show="item.product.deliveryTime != 1">s</spam></li>
							<li ng-show="item.status == 'PENDING_WITH_RESTRICTION' || (item.status == 'CANCELLED' && item.autoCancellationDate)"><i class="fa fa-check"></i> <strong>Data Cancelamento Automático:</strong> {{item.autoCancellationDate | date:'dd/MM/yyyy'}}</li>
							<li ng-show="item.sentDate"><i class="fa fa-check"></i> <strong>Data do envio:</strong> {{item.sentDate | date:'dd/MM/yyyy'}}</li>
							<li ng-show="item.deadlineDate"><i class="fa fa-check"></i> <strong>Data Limite para Entrega:</strong> {{item.deadlineDate | date:'dd/MM/yyyy'}}</li>
							<li ng-show="item.carrierName"><i class="fa fa-check"></i> <strong>Transportadora:</strong> {{item.carrierName}}</li>
							<li ng-show="item.trackingCode"><i class="fa fa-check"></i> <strong>Código de rastreamento:</strong> {{item.trackingCode}}</li>
							<li ng-show="item.fiscalNumber"><i class="fa fa-check"></i> <strong>Número da Nota Fiscal:</strong> {{item.fiscalNumber}}</li>
							<li ng-show="item.fiscalFileKey"><i class="fa fa-check"></i> <strong>Nota Fiscal:</strong> <fiscal-file-download-link delivery-request="item" /></li>
							<li ng-show="item.dueDate"><i class="fa fa-check"></i> <strong>Data de Vcto do Boleto:</strong> {{item.dueDate | date:'dd/MM/yyyy'}}</li>
							<li ng-show="item.paymentSlipKey"><i class="fa fa-check"></i> <strong>Boleto:</strong> <payment-slip-download-link delivery-request="item" /></li>
							<li class="text-danger" ng-show="item.status == 'CANCELLED' && item.cancellationComment"><i class="fa fa-check"></i> <strong>Motivo do Cancelamento:</strong> {{item.cancellationComment}}</li>
						</ul>							
					</div>	
				</div>
			</li>
		</ul>
	</div>
	<div class="panel-footer clearfix" ng-show="franchiseeSelected()">
    	<div class="pull-right">
			<button class="btn btn-default" ng-click="printDeliveryRequests()"><i class="fa fa-print"> Imprimir Pedidos</i></button>
			<button class="btn btn-default" ng-click="updateDeliveryRequests()"><i class="fa fa-edit"> Atualizar Pedidos</i></button>
		</div>
    </div>
</div>
<div id="loadingArea"></div>