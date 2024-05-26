<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Add Image Modal -->
<div class="modal fade" id="addImageModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Selecionar</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">		
		            <div class="form-group" id="file">
		                <label for="inputFile">Selecione o Arquivo:</label>
		                <input id="inputFile" type="file" class="form-control" file-model="supplierFile"/>
		            </div>
		            <div ng-show="progressVisible" class="progress progress-u progress-xxs">
		                <div class="progress-bar progress-bar-u" role="progressbar" aria-valuenow="{{progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{progress}}%">
		                </div>
		            </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
	            <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Cancelar</i></button>
        		<button ng-click="uploadImage()" class="btn btn-default"><i class="fa fa-save"> Upload</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- New Supplier Modal -->
<div class="modal fade" id="newSupplierModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel"><c:out value="${domainConfiguration.labels['SUPPLIER']}">Fornecedor</c:out></h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="supplier.id" type="hidden" value="0"/>		
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="supplier.name" placeholder="Informe o nome">
		            </div>
		            <ul class="nav nav-tabs">
				        <li class="active"><a href="#clasificationTab" target="_self" data-toggle="tab">Classificação</a></li>
				        <li><a href="#addressTab" target="_self" data-toggle="tab">Endereço</a></li>	
				        <li><a href="#contactTab" target="_self" data-toggle="tab">Contato</a></li>	
				        <li><a href="#descriptionTab" target="_self" data-toggle="tab">Descrição</a></li>	      
				        <li><a href="#deliveryTab" target="_self" data-toggle="tab">Entrega</a></li>  
				    </ul>
				    <div class="tab-content tab-content-form">
				    	<div class="tab-pane fade in active" id="clasificationTab">
						    <div class="form-group" id="supplierCategory">
				                <label for="inputSupplierCategory">Categoria de <c:out value="${domainConfiguration.labels['SUPPLIER']}">Fornecedor</c:out></label>		
				                <select id="inputSupplierCategory" class="form-control" ng-model="supplier.categoryId" ng-options="unit.id as unit.name for unit in supplierCategoriesSelect | orderBy:'name'"></select>     
				            </div>
				            <div class="form-group" id="supplierType" ng-show="supplierCategoriesMap[supplier.categoryId].hasStockControl">
				                <label for="inputSupplierType">Tipo de Estoque</label>		
				                <select id="inputSupplierType" class="form-control" ng-model="supplier.type" ng-options="unit.key as unit.value for unit in supplierTypes"></select>     
				            </div>		            
				            <div class="form-group" id="segment">
				                <label for="inputSegment">Segmento</label>
				                <input id="inputSegment" type="text" class="form-control" ng-model="supplier.segment" placeholder="Informe o segmento de atuação do fornecedor">
				            </div>
				            <div class="form-group" id="visibility">
				                <label for="inputVisibility">Visibilidade</label>
				                <select id="inputVisibility" class="form-control" ng-model="supplier.visibility" ng-options="unit.key as unit.value for unit in supplierVisibilityTypes"></select>
				            </div>
				    	</div>
					    <div class="tab-pane fade" id="addressTab">
						    <div class="form-group" id="address">
				                <textarea id="inputaddress" class="form-control" ng-model="supplier.address" rows="3" placeholder="Informe o endereço"></textarea>
				            </div>
				            <div class="row">
				            	<div class="col-xs-6">
						            <div class="form-group" id="state">
						                <label for="inputState">Estado</label>		
						                <select id="inputState" class="form-control" ng-model="supplier.stateId" ng-options="state.id as state.name for state in states  | orderBy:'name'" ng-change="onChangeState()"></select>     
						            </div>
						        </div>
						        <div class="col-xs-6">
						            <div class="form-group" id="city">
						                <label for="inputCity">Cidade</label>		
						                <select id="inputCity" class="form-control" ng-model="supplier.cityId" ng-disabled="supplier.stateId == 0" ng-options="city.id as city.name for city in cities  | orderBy:'name'"></select>     
						            </div>
						        </div>
				            </div>
					    </div>
					    <div class="tab-pane fade" id="contactTab">
						    <div class="row">
				            	<div class="col-xs-6">
				                    <div class="form-group" id="contactName">
				                        <label for="inputcontactName">Nome do Contato</label>
				                        <input id="inputcontactName" type="text" class="form-control" ng-model="supplier.contactName" placeholder="Informe o nome da pessoa para contato">
				                    </div>
								</div>
								<div class="col-xs-6">
				                    <div class="form-group" id="contactPhone">
				                        <label for="inputcontactPhone">Telefone</label>
				                        <div class="input-group">
				                            <div class="input-group-addon">
				                                <i class="fa fa-phone"></i>
				                            </div>
				                            <input id="inputcontactPhone" type="text" class="form-control" ng-model="supplier.contactPhone" data-inputmask='"mask": "(99) 9999[9]-9999"' data-mask/>
				                        </div><!-- /.input group -->
				                    </div><!-- /.form group -->
				            	</div>
				            </div>
				            <div class="form-group" id="contactEmail">
				                <label for="inputContactEmail">E-mail</label>
				                <div class="input-group">
		                            <div class="input-group-addon">
		                                <i class="fa fa-envelope"></i>
		                            </div>
		                            <input id="inputContactEmail" type="text" class="form-control" ng-model="supplier.contactEmail" placeholder="Informe o e-mail para contato"/>
		                        </div><!-- /.input group -->
				            </div>
					    </div>
					    <div class="tab-pane fade text-angular-wrapper" id="descriptionTab">
					    	<div text-angular="text-angular" name="htmlcontent" ng-model="supplier.description"></div>
					    </div>
					    <div class="tab-pane fade" id="deliveryTab">
						    <div class="form-group" id="deliveryNotes">
				                <textarea id="deliveryNotes" class="form-control" ng-model="supplier.deliveryNotes" rows="3" placeholder="Informações de entrega"></textarea>
				            </div>
					    </div>
		            </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Cancelar</i></button>
    			<button ng-click="save()" class="btn btn-default"><i class="fa fa-save"> Salvar</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- List of Existing Suppliers -->
<div class="panel panel-default panel-with-group">
	<div class="panel-heading">
        <i class="fa fa-truck"></i>
        <h3 class="panel-title">
            Lista de <c:out value="${domainConfiguration.labels['SUPPLIERS']}">Fornecedores</c:out>
        </h3>
    </div>
    <div class="panel-body">
    	<uib-accordion close-others="false">		
			<div uib-accordion-group is-open="category.open" ng-repeat="category in supplierCategories | orderBy:'order'">
				<uib-accordion-heading>
					<i class="fa" ng-class="{'fa-angle-up': category.open, 'fa-angle-down': !category.open}"></i> {{category.name}}
				</uib-accordion-heading>
				<div class="panel-list-group">
					<ul class="list-group">
						<li class="list-group-item" ng-repeat="item in supplierList | filter:category.id  | orderBy:'name'">
							<div class="row list-group-item-row">	
								<div class="col-sm-3 list-group-item-column">
			            			<div class="thumbnail zoomer">            				
					    				<img class="img-responsive" ng-src="{{getImage(item)}}" alt="" />
					    				<i class="zoomer-icon add-icon zoom-fa fa fa-camera" ng-click="addImage(item)" data-toggle="modal" data-target="#addImageModal"></i>
					    			</div>
								</div>
								<div class="col-sm-9 list-group-item-column">
									<h4 class='list-group-item-title'>
			                            <strong>{{item.name}}</strong>
			                        </h4>
			                        <ul class="list-unstyled">
			                        	<li><i class="fa fa-check"></i> <strong>Contato:</strong> {{item.contactName}}</li>
			                        	<li><i class="fa fa-check"></i> <strong>Telefone:</strong> {{item.contactPhone}}</li>
			                        	<li><i class="fa fa-check"></i> <strong>Email:</strong> {{item.contactEmail}}</li>
			                        	<li><i class="fa fa-check"></i> <strong>Endereço:</strong> {{item.address}}</li>
			                        	<li><i class="fa fa-check"></i> <strong>Cidade:</strong> {{item.city.name}}</li>
			                        	<li><i class="fa fa-check"></i> <strong>Estado:</strong> {{item.state.name}}</li>
			                        	<li><i class="fa fa-check"></i> <strong>Visibilidade:</strong> {{item.visibilityDescription}}</li>
			                        </ul>
			                        <div class="list-group-item-actions">
										<div class="btn-group">
										  <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
										    Ações <span class="caret"></span>
										  </button>
										  <ul class="dropdown-menu" role="menu">
										    <li><a href="" ng-click="edit(item)" data-toggle="modal" data-target="#newSupplierModal">Editar</a></li>
										    <li><a href="" ng-click="delete(item)">Excluir</a></li>
										    <li class="divider"></li>
										    <li><a href="" ng-click="editProfiles(item)">Perfis de Usuários</a></li>
							    			<li><a href="" ng-click="editUsers(item)">Usuários</a></li>
										    <li><a href="" ng-click="editFranchisors(item)"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISORS']}">Franquias</c:out></a></li>
										    <li class="divider" ng-show="supplierCategoriesMap[item.categoryId].hasStockControl"></li>
										    <li><a href="" ng-click="requests(item)" ng-show="supplierCategoriesMap[item.categoryId].hasStockControl">Pedidos</a></li>
										    <li><a href="" ng-click="stock(item)" ng-show="supplierCategoriesMap[item.categoryId].hasStockControl">Estoque</a></li>
										  </ul>
										</div>
			                        </div>
								</div>
							</div>	
						</li>
					</ul>
				</div>
			</div uib-accordion-group>		
		</uib-accordion>
    </div>    
	
    <!-- Loading -->
    <div id="list-overlay" class="overlay"></div>
    <div id="list-loading-img" class="loading-img"></div>
    <!-- end loading -->
    <div class="panel-footer clearfix">
    	<div class="pull-right">
			<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newSupplierModal"><i class="fa fa-plus"> Novo</i></button>
		</div>
    </div>
</div>