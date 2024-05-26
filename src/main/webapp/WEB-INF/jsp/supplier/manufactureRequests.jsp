<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Requests -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-bar-chart-o"></i> Pedidos</h3>
</div>
<div class="panel panel-default">
	<div class="panel-body">
		<div>
			<ul class="nav nav-tabs" ng-hide="supplierType != 'RECEIVE_MANUFACTURE_REQUEST'">
		        <li class="active"><a href="#deliveryRequests" target="_self" data-toggle="tab" ng-click="loadDeliveryRequests()">Entrega</a></li>
		        <li><a href="#manufactureRequests" target="_self" data-toggle="tab" ng-click="loadManufactureRequests()">Produção</a></li>		        
		    </ul>
			<div class="tab-content">
		        <div class="tab-pane fade in active" id="deliveryRequests">
		        	<div class="panel panel-default">
		        		<div class="panel-list-group">
		        			<ul class="list-group">
		        			
								<li class="list-group-item list-group-item-filter">
				                    <form>
										<div class="form-group">
				    						<label for="status">Status</label>
				    						<select id="status" class="form-control" ng-model="deliveryStatus" ng-options="unit.key as unit.value for unit in deliveryStatuses" ng-change="loadDeliveryRequests()"></select>
				  						</div>
				  						<div class="form-group">
				    						<label for="franchisee">Loja</label>
				    						<select id="franchisee" class="form-control" ng-model="franchiseeId" ng-options="unit.id as deliveryRequestsFranchisors[unit.franchisorId].name + ' - ' + unit.name for unit in deliveryRequestsFranchiseesList | orderBy:'name'"></select>
				  						</div>                      	
				                    </form>
							    </li>
		        	
								<li class="list-group-item" ng-repeat="item in deliveryRequests | filter:franchiseeId | orderBy:'date':true">
									<div class="row list-group-item-row">
				                		<div class="col-sm-2 list-group-item-column">
				                			<div class="thumbnail">
							    				<img class="img-responsive" ng-src="{{getImageURL(deliveryRequestsFranchisors[deliveryRequestsFranchisees[item.franchiseeId].franchisorId])}}" alt="" />			    				
							    			</div>
										</div>
										<div class="col-sm-7 list-group-item-column clearfix">
											<h4 class="list-group-item-title">
				                                <strong>{{item.product.name}}<span ng-show="item.product.sizes.length > 1"> - {{item.product.sizeName}}: {{item.productSize.name}}</span></strong>
				                                <small>{{item.date | date:'dd/MM/yyyy HH:mm'}}</small>
				                            </h4>
				                            <p ta-bind="text" ng-model="item.product.description"></p>
				                            <ul class="list-unstyled">
				                            	<li>
				                            		<i class="fa fa-check"></i> <strong>Loja:</strong> {{item.franchisee.name}}
				                            		<a href="javascript:{return false;}" ng-click="toggleFullContactInfo(item)">
														<i class="fa fa-plus-square-o" ng-show="!item.showFullContactInfo"></i>
				                            			<i class="fa fa-minus-square-o" ng-show="item.showFullContactInfo"></i>
				                                	</a>
				                            	</li>
				                            	<li ng-show="item.showFullContactInfo">
				                                	<ul class="list-unstyled margin-left-25">
				                                    	<li ng-if="item.franchisee.corporateName != ''"><i class="fa fa-angle-right"></i> <strong><c:out value="${domainConfiguration.labels['FRANCHISOR_CORPORATE_NAME']}">Razão Social</c:out>:</strong> {{item.franchisee.corporateName}}</li>
				                                    	<li ng-if="item.franchisee.fiscalId != ''"><i class="fa fa-angle-right"></i> <strong><c:out value="${domainConfiguration.labels['FRANCHISOR_FISCAL_ID']}">CNPJ</c:out>:</strong> {{item.franchisee.fiscalId}}</li>
				                                    	<li ng-if="item.franchisee.contactName != ''"><i class="fa fa-angle-right"></i> <strong>Contato:</strong> {{item.franchisee.contactName}}</li>
				                                    	<li ng-if="item.franchisee.contactPhone != ''"><i class="fa fa-angle-right"></i> <strong>Telefone:</strong> {{item.franchisee.contactPhone}}</li>
				                                    	<li ng-if="item.franchisee.contactEmail != ''"><i class="fa fa-angle-right"></i> <strong>E-mail:</strong> {{item.franchisee.contactEmail}}</li>
				                                    	<li ng-if="item.franchisee.address != ''"><i class="fa fa-angle-right"></i> <strong>Endereço:</strong> {{item.franchisee.address}}</li>
				                                    	<li ng-if="item.franchisee.additionalInformation != ''"><div ta-bind="text" ng-model="item.franchisee.additionalInformation"></div></li>
				                                	</ul>
				                                </li>
				                            	<li ng-show="item.product.sizes.length > 1"><i class="fa fa-check"></i> <strong>{{item.product.sizeName}}:</strong> {{item.productSize.name}} <spam ng-show="item.productSize.description != null && item.productSize.description != ''"> - {{item.productSize.description}}</spam></li>
				                            	<li><i class="fa fa-check"></i> <strong>Quantidade:</strong> <span ng-show="item.product.hasDeliveryUnit">{{item.quantity / item.productSize.deliveryQty}} {{item.product.deliveryUnit}} = </span>{{item.quantity}} {{item.product.unit}}</span></li>
				                            	<li><i class="fa fa-check"></i> <strong>Valor Unitário:</strong> {{item.deliveryUnitPrice | currency:undefined:4}} / {{item.product.unit}}</li>
				                            	<li><i class="fa fa-check"></i> <strong>Valor do Pedido:</strong> {{item.calculateDelivery() | currency}}</li>
				                            	<li ng-show="deliveryRequestsProducts[item.productId].deliveryTime > 0"><i class="fa fa-check"></i> <strong>Prazo de Preparação p/Entrega:</strong> {{item.product.deliveryTime}} dia<spam ng-show="item.product.deliveryTime != 1">s</spam></li>
				                            	<li ng-show="item.status == 'COMPLETED'"><i class="fa fa-check"></i> <strong>Data do envio:</strong> {{item.sentDate | date:'dd/MM/yyyy'}}</li>
				                            	<li ng-show="item.status == 'COMPLETED'"><i class="fa fa-check"></i> <strong>Data Limite para Entrega:</strong> {{item.deadlineDate | date:'dd/MM/yyyy'}}</li>
				                            	<li ng-show="item.status == 'COMPLETED'"><i class="fa fa-check"></i> <strong>Transportadora:</strong> {{item.carrierName}}</li>
				                            	<li ng-show="item.status == 'COMPLETED'"><i class="fa fa-check"></i> <strong>Código de rastreamento:</strong> {{item.trackingCode}}</li>
				                            	<li ng-show="item.status == 'COMPLETED'"><i class="fa fa-check"></i> <strong>Número da Nota Fiscal:</strong> {{item.fiscalNumber}}</li>
				                            	<li ng-show="item.status == 'COMPLETED'"><i class="fa fa-check"></i> <strong>Data de Vcto do Boleto:</strong> {{item.dueDate | date:'dd/MM/yyyy'}}</li>
				                            	<li class="text-danger" ng-show="item.status == 'CANCELLED'"><i class="fa fa-check"></i> <strong>Motivo do Cancelamento:</strong> {{item.cancellationComment}}</li>
				                            </ul>
				                            <div class="list-group-item-actions" ng-hide="item.updateStatusRequestMode">
				                                <ul class="list-inline comment-list-v2 pull-left">
				                                    <li><i class="fa fa-pencil"></i> <a href="javascript:void(0);" ng-click="editDelivery(item)">Atualizar status</a></li>
				                                </ul>
				                            </div>
				                            <div ng-show="item.updateStatusRequestMode" class="list-group-item-actions">
				                            	<form>
				                            		<div class="form-group">
							    						<label for="status">Atualizar status para:</label>
							    						<select id="status" class="form-control input-sm" ng-model="item.updateStatusRequest.status" ng-options="unit.key as unit.value for unit in deliveryStatuses"></select>
							  						</div>
							  						<div class="form-group" ng-show="item.updateStatusRequest.status == 'COMPLETED'">
							    						<label for="sentDate">Data do Envio:</label>
							    						<input id="sentDate" type="text" class="form-control" ng-model="item.updateStatusRequest.sentDate" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
							  						</div>
							  						<div class="form-group" ng-show="item.updateStatusRequest.status == 'COMPLETED'">
							    						<label for="deadlineDate">Data Limite para Entrega:</label>
							    						<input id="deadlineDate" type="text" class="form-control" ng-model="item.updateStatusRequest.deadlineDate" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
							  						</div>
							  						<div class="form-group" ng-show="item.updateStatusRequest.status == 'COMPLETED'">
							    						<label for="carrierName">Transportadora:</label>
							    						<input id="carrierName" type="text" class="form-control" ng-model="item.updateStatusRequest.carrierName"/>
							  						</div>
							  						<div class="form-group" ng-show="item.updateStatusRequest.status == 'COMPLETED'">
							    						<label for="trackingCode">Código de rastreamento:</label>
							    						<input id="trackingCode" type="text" class="form-control" ng-model="item.updateStatusRequest.trackingCode"/>
							  						</div>
							  						<div class="form-group" ng-show="item.updateStatusRequest.status == 'COMPLETED'">
							    						<label for="fiscalNumber">Número da Nota Fiscal:</label>
							    						<input id="fiscalNumber" type="text" class="form-control" ng-model="item.updateStatusRequest.fiscalNumber"/>
							  						</div>
							  						<div class="form-group" ng-show="item.updateStatusRequest.status == 'COMPLETED'">
							    						<label for="dueDate">Data do Vencimento:</label>
							    						<input id="dueDate" type="text" class="form-control" ng-model="item.updateStatusRequest.dueDate" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
							  						</div>
							  						<div class="form-group" ng-show="item.updateStatusRequest.status == 'CANCELLED'">
							    						<label for="cancellationComment">Motivo do Cancelamento:</label>
							    						<textarea id="cancellationComment" class="form-control" ng-model="item.updateStatusRequest.cancellationComment" rows="3" placeholder="Informe o motivo do cancelamento"></textarea>
							  						</div>
							  						<div class="form-group">
						                            	<button class="btn btn-default btn-xs" type="button" ng-click="saveEditDelivery(item)">Salvar</button>
						                            	<button class="btn btn-default btn-xs" type="button" ng-click="cancelEditDelivery(item)">Cancelar</button>
							  						</div>
					                        	</form>
				                            </div>
										</div>
										<div class="col-sm-3 list-group-item-column">
											<div class="thumbnail zoomer">
							    				<a class="fancybox-button" rel="fancybox-button-{{item.id}}" title="{{item.product.name}}" ng-href="{{item.product.imageURL}}">
							    					<img class="img-responsive" ng-src="{{getImageURL(item.product)}}" alt="" />
							    					<img class="zoom-icon" src="/img/overlay-icon.png" alt="" />
							    					<i class="zoomer-icon add-icon zoom-fa fa fa-search-plus"></i>
							    				</a>
							    			</div>
										</div>
				        			</div>
								</li>
							</ul>
						</div>
					</div>	
		        </div><!--/end tab-pane -->
		        <div class="tab-pane fade in" id="manufactureRequests">
		        	<div class="panel panel-default">
		        		<div class="panel-list-group">
		        			<ul class="list-group">
		        			
								<li class="list-group-item list-group-item-filter">
				                    <form>
										<div class="form-group">
				    						<label for="status">Status</label>
				    						<select id="status" class="form-control" ng-model="manufactureStatus" ng-options="unit.key as unit.value for unit in manufactureStatuses" ng-change="loadManufactureRequests()"></select>
				  						</div>                     	
				                    </form>
							    </li>
		        	
								<li class="list-group-item" ng-repeat="item in manufactureRequests | filter:manufactureStatus | orderBy:'manufactureRequest.date':true">
									<div class="row list-group-item-row">
				                		<div class="col-sm-2 list-group-item-column">
				                			<div class="thumbnail">
							    				<img class="img-responsive" ng-src="{{getImageURL(item.franchisor)}}" alt="" />
							    			</div>
										</div>
										<div class="col-sm-7 list-group-item-column clearfix">
											<h4 class="list-group-item-title">
				                                <strong>{{item.product.name}}<span ng-show="item.product.sizes.length > 1"> - {{item.product.sizeName}}: {{item.productSize.name}}</span></strong>
				                                <small>{{item.date | date:'dd/MM/yyyy HH:mm'}}</small>
				                            </h4>
				                            <p ta-bind="text" ng-model="item.product.description"></p>
				                            <ul class="list-unstyled">
				                            	<li>
				                            		<i class="fa fa-check"></i> <strong>Franquia/Rede:</strong> {{item.franchisor.name}}
				                            		<a href="javascript:{return false;}" ng-click="toggleFullContactInfo(item)">
				                            			<i class="fa fa-plus-square-o" ng-show="!item.showFullContactInfo"></i>
				                            			<i class="fa fa-minus-square-o" ng-show="item.showFullContactInfo"></i>
				                                	</a>
				                            	</li>
				                            	<li ng-show="item.showFullContactInfo">
				                                	<ul class="list-unstyled margin-left-25"> 
				                                    	<li ng-if="item.franchisor.corporateName != ''"><i class="fa fa-angle-right"></i> <strong><c:out value="${domainConfiguration.labels['FRANCHISOR_CORPORATE_NAME']}">Razão Social</c:out>:</strong> {{item.franchisor.corporateName}}</li>
				                                    	<li ng-if="item.franchisor.fiscalId != ''"><i class="fa fa-angle-right"></i> <strong><c:out value="${domainConfiguration.labels['FRANCHISOR_FISCAL_ID']}">CNPJ</c:out>:</strong> {{item.franchisor.fiscalId}}</li>
				                                    	<li ng-if="item.franchisor.contactName != ''"><i class="fa fa-angle-right"></i> <strong>Contato:</strong> {{item.franchisor.contactName}}</li>
				                                    	<li ng-if="item.franchisor.contactEmail != ''"><i class="fa fa-angle-right"></i> <strong>E-mail:</strong> {{item.franchisor.contactEmail}}</li>
				                                    	<li ng-if="item.franchisor.contactPhone != ''"><i class="fa fa-angle-right"></i> <strong>Telefone:</strong> {{item.franchisor.contactPhone}}</li>
				                                    	<li ng-if="item.franchisor.address != ''"><i class="fa fa-angle-right"></i> <strong>Endereço:</strong> {{item.franchisor.address}}</li>
				                                    	<li ng-if="item.franchisor.additionalInformation != ''"><div ta-bind="text" ng-model="item.franchisor.additionalInformation"></div></li>
				                                	</ul>
				                                </li>
				                            	<li ng-show="item.product.sizes.length > 1"><i class="fa fa-check"></i> <strong>{{item.product.sizeName}}:</strong> {{item.productSize.name}} <spam ng-show="item.productSize.description != null && item.productSize.description != ''"> - {{item.productSize.description}}</spam></li>
				                            	<li><i class="fa fa-check"></i> <strong>Quantidade:</strong> <span ng-show="item.product.hasDeliveryUnit">{{item.quantity / item.productSize.deliveryQty}} {{item.product.deliveryUnit}} = </span>{{item.quantity}} {{item.product.unit}}</li>
				                            	<li><i class="fa fa-check"></i> <strong>Valor Unitário:</strong> {{item.manufactureUnitPrice | currency:undefined:4}} / {{item.product.unit}}</li>
				                            	<li><i class="fa fa-check"></i> <strong>Valor do Pedido:</strong> {{item.calculateManufacture() | currency}}</li>
				                            	<li ng-show="item.product.manufactureTime > 0"><i class="fa fa-check"></i> <strong>Prazo de Produção:</strong> {{item.product.manufactureTime}} dia<spam ng-show="item.product.manufactureTime != 1">s</spam></li>
				                            	<li class="text-danger" ng-show="item.status == 'CANCELLED'"><i class="fa fa-check"></i> <strong>Motivo do Cancelamento:</strong> {{item.cancellationComment}}</li>
				                            </ul>
				                            <div class="list-group-item-actions" ng-hide="item.updateStatusRequestMode">
				                                <ul class="list-inline comment-list-v2 pull-left">
				                                    <li><i class="fa fa-pencil"></i> <a href="javascript:void(0);" ng-click="editManufacture(item)">Atualizar status</a></li>
				                                </ul>
				                            </div>
				                            <div ng-show="item.updateStatusRequestMode" class="list-group-item-actions">
				                            	<form>
													<div class="form-group">
							    						<label for="status">Atualizar status para:</label>
							    						<select id="status" class="form-control input-sm" ng-model="item.updateStatusRequest.status" ng-options="unit.key as unit.value for unit in manufactureStatuses"></select>
							  						</div>
							  						<div class="form-group" ng-show="item.updateStatusRequest.status == 'CANCELLED'">
							    						<label for="cancellationComment">Motivo do Cancelamento:</label>
							    						<textarea id="cancellationComment" class="form-control" ng-model="item.updateStatusRequest.cancellationComment" rows="3" placeholder="Informe o motivo do cancelamento"></textarea>
							  						</div>
							  						<div class="form-group">
						                            	<button class="btn btn-default btn-xs" type="button" ng-click="saveEditManufacture(item)">Salvar</button>
						                            	<button class="btn btn-default btn-xs" type="button" ng-click="cancelEditManufacture(item)">Cancelar</button>
							  						</div>
					                        	</form>
				                            </div>
										</div>
				                		<div class="col-sm-3 list-group-item-column">
				                			<div class="thumbnail zoomer">
							    				<a class="fancybox-button" rel="fancybox-button-{{item.id}}" title="{{item.product.name}}" ng-href="{{item.product.imageURL}}">
							    					<img class="img-responsive" ng-src="{{item.thumbnail}}" alt="" />
							    					<img class="zoom-icon" src="/img/overlay-icon.png" alt="" />
							    					<i class="zoomer-icon add-icon zoom-fa fa fa-search-plus"></i>
							    				</a>
							    			</div>
										</div>
				        			</div>
								</li>
							</ul>
						</div>
					</div>		        	
		        </div><!--/end tab-pane -->
		    </div><!--/end tab-content -->
		</div><!--/end tab-->
		<div id="loadingArea"></div>
    </div>
</div>