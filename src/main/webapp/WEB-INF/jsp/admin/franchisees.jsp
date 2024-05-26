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
		                <input id="inputFile" type="file" class="form-control" file-model="franchiseeFile"/>
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

<!-- New Franchisee Modal -->
<div class="modal fade" id="newFranchiseeModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISEE']}">Loja</c:out></h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="franchisee.id" type="hidden" value="0"/>		
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="franchisee.name" placeholder="Informe o nome">
		            </div>
		            <ul class="nav nav-tabs">
				        <li class="active"><a href="#detailsTab" target="_self" data-toggle="tab">Detalhes</a></li>
				        <li><a href="#addressTab" target="_self" data-toggle="tab">Endereço</a></li>	
				        <li><a href="#contactTab" target="_self" data-toggle="tab">Contato</a></li>	       
				    </ul>
				    <div class="tab-content tab-content-form">
				    	<div class="tab-pane fade in active" id="detailsTab">
				            <div class="form-group" id="corporateName">
				                <label for="inputcorporateName"><c:out value="${domainConfiguration.labels['FRANCHISEE_CORPORATE_NAME']}">Razão Social</c:out></label>
				                <input id="inputcorporateName" type="text" class="form-control" ng-model="franchisee.corporateName">
				            </div>
				             <div class="form-group" id="fiscalId">
				                <label for="inputfiscalId"><c:out value="${domainConfiguration.labels['FRANCHISEE_FISCAL_ID']}">CNPJ</c:out></label>
				                <input id="inputfiscalId" type="text" class="form-control" ng-model="franchisee.fiscalId">
				            </div>
				            <div class="form-group text-angular-wrapper" id="additionalInformation">
				                <label for="inputadditionalInformation">Informações Adicionais</label>
				                <div text-angular="text-angular" name="htmlcontent" ng-model="franchisee.additionalInformation"></div>
				            </div>
				    	</div>
				    	<div class="tab-pane fade" id="addressTab">
							<div class="form-group" id="address">
				                <label for="inputaddress">Endereço</label>
				                <textarea id="inputaddress" class="form-control" ng-model="franchisee.address" rows="3" placeholder="Informe o endereço"></textarea>
				            </div>
				            <div class="row">
				            	<div class="col-xs-6">
						            <div class="form-group" id="state">
						                <label for="inputState">Estado</label>		
						                <select id="inputState" class="form-control" ng-model="franchisee.stateId" ng-options="state.id as state.name for state in states  | orderBy:'name'" ng-change="onChangeState()"></select>     
						            </div>
						        </div>
						        <div class="col-xs-6">
						            <div class="form-group" id="city">
						                <label for="inputCity">Cidade</label>		
						                <select id="inputCity" class="form-control" ng-model="franchisee.cityId" ng-disabled="franchisee.stateId == 0" ng-options="city.id as city.name for city in cities  | orderBy:'name'"></select>     
						            </div>
						        </div>
				            </div>
				    	</div>
				    	<div class="tab-pane fade" id="contactTab">
		 					<div class="row">
				            	<div class="col-xs-6">
						            <div class="form-group" id="contactName">
						                <label for="inputcontactName">Contato</label>
						                <input id="inputcontactName" type="text" class="form-control" ng-model="franchisee.contactName" placeholder="Informe o nome da pessoa para contato">
						            </div>
						        </div>
								<div class="col-xs-6">
						            <div class="form-group" id="contactPhone">
						                <label for="inputcontactPhone">Telefone</label>
						                <div class="input-group">
						                    <div class="input-group-addon">
						                        <i class="fa fa-phone"></i>
						                    </div>
						                    <input id="inputcontactPhone" type="text" class="form-control" ng-model="franchisee.contactPhone" data-inputmask='"mask": "(99) 9999[9]-9999", "greedy": false' data-mask/>
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
		                            <input id="inputContactEmail" type="text" class="form-control" ng-model="franchisee.contactEmail" placeholder="Informe o e-mail para contato"/>
		                        </div><!-- /.input group -->
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

<!-- List of Existing Franchisees -->
<div class="panel panel-default">
	<div class="panel-heading">
        <i class="fa fa-cubes"></i>
        <h3 class="panel-title"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISEES']}">Lojas</c:out></h3>
    </div>
	<div class="panel-list-group">
		<ul class="list-group">
			<li class="list-group-item" ng-repeat="item in franchiseeList | orderBy:'name'">
				<div class="row list-group-item-row">
					<div class="col-sm-3 list-group-item-column">
            			<div class="thumbnail zoomer">            				
		    				<img class="img-responsive" ng-src="{{getImage(item)}}" alt="" />
		    				<i class="zoomer-icon add-icon zoom-fa fa fa-camera" ng-click="addImage(item)" data-toggle="modal" data-target="#addImageModal"></i>
		    				<i class="zoomer-icon delete-icon zoom-fa fa fa-trash-o" ng-click="deleteImage(item)" ></i>
		    			</div>
					</div>	
					<div class="col-sm-9 list-group-item-column">
						<h4 class='list-group-item-title'>
                            <strong>{{item.name}}</strong>
                            <small ng-if="item.corporateName != ''">{{item.corporateName}}</small>
                        </h4>
                        <ul class="list-unstyled">
                        	<li><i class="fa fa-check"></i> <strong>Contato:</strong> {{item.contactName}}</li>
                        	<li><i class="fa fa-check"></i> <strong>Telefone:</strong> {{item.contactPhone}}</li>
                        	<li><i class="fa fa-check"></i> <strong>E-mail:</strong> {{item.contactEmail}}</li>
                        	<li><i class="fa fa-check"></i> <strong><c:out value="${domainConfiguration.labels['FRANCHISEE_FISCAL_ID']}">CNPJ</c:out>:</strong> {{item.fiscalId}}</li>
                        	<li><i class="fa fa-check"></i> <strong>Endereço:</strong> {{item.address}}</li>
							<li><i class="fa fa-check"></i> <strong>Cidade:</strong> {{item.city.name}}</li>
                        	<li><i class="fa fa-check"></i> <strong>Estado:</strong> {{item.state.name}}</li>
                        	<li><i class="fa fa-check"></i> <strong>Informações Adicionais:</strong><div ta-bind="text" ng-model="item.additionalInformation"></div></li>
                        </ul>
                        <div class="list-group-item-actions">
							<div class="btn-group">
							  <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
							    Ações <span class="caret"></span>
							  </button>
							  <ul class="dropdown-menu" role="menu">
							    <li><a href="" ng-click="edit(item)" data-toggle="modal" data-target="#newFranchiseeModal">Editar</a></li>
							    <li><a href="" ng-click="delete(item)">Excluir</a></li>
							    <li class="divider"></li>
							    <li><a href="" ng-click="editProfiles(item)">Perfis de Usuários</a></li>
							    <li><a href="" ng-click="editUsers(item)">Usuários</a></li>
							  </ul>
							</div>
                        </div>
					</div>
				</div>	
			</li>
		</ul>
	</div>
    <!-- Loading -->
    <div id="list-overlay" class="overlay"></div>
    <div id="list-loading-img" class="loading-img"></div>
    <!-- end loading -->
    <div class="panel-footer clearfix">
    	<div class="pull-right">
			<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newFranchiseeModal"><i class="fa fa-plus"> Novo</i></button>
		</div>
    </div>
</div>
