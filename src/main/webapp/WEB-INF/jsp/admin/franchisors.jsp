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
		                <input id="inputFile" type="file" class="form-control" file-model="franchisorFile"/>
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

<!-- New Franchisor Modal -->
<div class="modal fade" id="newFranchisorModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISOR']}">Franquia</c:out></h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="franchisor.id" type="hidden" value="0"/>		
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="franchisor.name" placeholder="Informe o nome">
		            </div>
		            <ul class="nav nav-tabs">
				        <li class="active"><a href="#detailsTab" target="_self" data-toggle="tab">Detalhes</a></li>
				        <li><a href="#addressTab" target="_self" data-toggle="tab">Endereço</a></li>	
				        <li><a href="#contactTab" target="_self" data-toggle="tab">Contato</a></li>	  
						<li><a href="#configurationTab" target="_self" data-toggle="tab">Configurações</a></li>
						<li><a href="#featuresTab" target="_self" data-toggle="tab">Funcionalidades</a></li>     
				    </ul>
				    <div class="tab-content tab-content-form">
				    	<div class="tab-pane fade in active" id="detailsTab">
						    <div class="form-group" id="corporateName">
				                <label for="inputcorporateName"><c:out value="${domainConfiguration.labels['FRANCHISOR_CORPORATE_NAME']}">Razão Social</c:out></label>
				                <input id="inputcorporateName" type="text" class="form-control" ng-model="franchisor.corporateName">
				            </div>
				            <div class="form-group" id="fiscalId">
				                <label for="inputfiscalId"><c:out value="${domainConfiguration.labels['FRANCHISOR_FISCAL_ID']}">CNPJ</c:out></label>
				                <input id="inputfiscalId" type="text" class="form-control" ng-model="franchisor.fiscalId">
				            </div>
				            <div class="form-group text-angular-wrapper" id="additionalInformation">
				                <label for="inputadditionalInformation">Informações Adicionais</label>
				                <div text-angular="text-angular" name="htmlcontent" ng-model="franchisor.additionalInformation"></div>
				            </div>
				    	</div>
				    	<div class="tab-pane fade" id="addressTab">
				    		<div class="form-group" id="address">
				                <label for="inputaddress">Endereço</label>
				                <textarea id="inputaddress" class="form-control" ng-model="franchisor.address" rows="3" placeholder="Informe o endereço"></textarea>
				            </div>
				    	</div>				    	
				    	<div class="tab-pane fade" id="contactTab">
				    		<div class="row">
				            	<div class="col-xs-6">
				                    <div class="form-group" id="contactName">
				                        <label for="inputcontactName">Contato</label>
				                        <input id="inputcontactName" type="text" class="form-control" ng-model="franchisor.contactName" placeholder="Informe o nome da pessoa para contato">
				                    </div>
								</div>
								<div class="col-xs-6">
				                    <div class="form-group" id="contactPhone">
				                        <label for="inputcontactPhone">Telefone</label>
				                        <div class="input-group">
				                            <div class="input-group-addon">
				                                <i class="fa fa-phone"></i>
				                            </div>
				                            <input id="inputcontactPhone" type="text" class="form-control" ng-model="franchisor.contactPhone" data-inputmask='"mask": "(99) 9999[9]-9999"' data-mask/>
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
		                            <input id="inputContactEmail" type="text" class="form-control" ng-model="franchisor.contactEmail" placeholder="Informe o e-mail para contato"/>
		                        </div><!-- /.input group -->
				            </div>
				    	</div>
				    	<div class="tab-pane fade" id="configurationTab">
				    		<div class="row">
				            	<div class="col-xs-6">
				                   	<div class="form-group" id="paymentTime">
				                        <label for="inputPaymentTime">Prazo de pagamento</label>
				                        <div class="input-group">
				                        	<span class="input-group-addon">#</span>
				                        	<input id="inputPaymentTime" type="text" class="form-control" ng-model="franchisor.paymentTime" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
				                        	<span class="input-group-addon"> dias</span>
				                        </div>
				                    </div>
								</div>
							</div>
							<div class="row">
				            	<div class="col-xs-6">
				                   	<div class="form-group" id="maxStorageSizeMB">
				                        <label for="inputMaxStorageSizeMB">Limite de Armazenamento</label>
				                        <div class="input-group">
				                        	<input id="inputMaxStorageSizeMB" type="text" class="form-control" ng-model="franchisor.maxStorageSizeMB" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
				                        	<span class="input-group-addon"> MBs</span>
				                        </div>
				                    </div>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-12">
									<div class="form-group" id="loginURL">
						                <label for="inputLoginURL">Página de Login</label>
						                <input id="inputLoginURL" type="text" class="form-control" ng-model="franchisor.loginURL" placeholder="Informe o endereço da página de login customizada">
						            </div>
						    	</div>
							</div>
							<div class="row">
								<div class="col-xs-12">
									<div class="form-group" id="radioURL">
						                <label for="inputRadioURL">Streaming da Rádio</label>
						                <input id="inputRadioURL" type="text" class="form-control" ng-model="franchisor.radioURL" placeholder="Informe o endereço do stream da radio do franqueador">
						            </div>
						    	</div>
				            </div>
						</div>
						<div class="tab-pane fade" id="featuresTab">
				    		<div class="row">
				            	<div class="col-xs-6">
			                        <input type="checkbox" ng-model="franchisor.flagAnnouncement" /> <c:out value="${domainConfiguration.labels['ANNOUNCEMENTS']}">Quadro de Avisos</c:out>
								</div>
							</div>
							<div class="row">
				            	<div class="col-xs-6">
			                        <input type="checkbox" ng-model="franchisor.flagCalendar" /> <c:out value="${domainConfiguration.labels['CALENDAR']}">Calendário</c:out>
								</div>
							</div>
							<div class="row">
				            	<div class="col-xs-6">
			                        <input type="checkbox" ng-model="franchisor.flagDocuments" /> <c:out value="${domainConfiguration.labels['FILES']}">Documentos</c:out>
								</div>
							</div>
							<div class="row">
				            	<div class="col-xs-6">
			                        <input type="checkbox" ng-model="franchisor.flagTraining" /> <c:out value="${domainConfiguration.labels['TRAININGS']}">Treinamentos</c:out>
								</div>
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

<!-- List of Existing Franchisors -->
<div class="panel panel-default">
	<div class="panel-heading">
        <i class="fa fa-home"></i>
        <h3 class="panel-title"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISORS']}">Franquias</c:out></h3>
    </div>
	<div class="panel-list-group">
		<ul class="list-group">
			<li class="list-group-item" ng-repeat="item in franchisorList | orderBy:'name'">
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
                            <small ng-if="item.corporateName != ''">{{item.corporateName}}</small>
                        </h4>
                        <ul class="list-unstyled">
                        	<li><i class="fa fa-check"></i> <strong>Contato:</strong> {{item.contactName}}</li>
                        	<li><i class="fa fa-check"></i> <strong>Telefone:</strong> {{item.contactPhone}}</li>
                        	<li><i class="fa fa-check"></i> <strong>E-mail:</strong> {{item.contactEmail}}</li>
                        	<li><i class="fa fa-check"></i> <strong><c:out value="${domainConfiguration.labels['FRANCHISOR_FISCAL_ID']}">CNPJ</c:out>:</strong> {{item.fiscalId}}</li>
                        	<li><i class="fa fa-check"></i> <strong>Endereço:</strong> {{item.address}}</li>
                        	<li><i class="fa fa-check"></i> <strong>Informações Adicionais:</strong><div ta-bind="text" ng-model="item.additionalInformation"></div></li>
                        </ul>
                        <div class="list-group-item-actions">
							<div class="btn-group">
							  <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
							    Ações <span class="caret"></span>
							  </button>
							  <ul class="dropdown-menu" role="menu">
							    <li><a href="" ng-click="edit(item)" data-toggle="modal" data-target="#newFranchisorModal">Editar</a></li>
							    <li><a href="" ng-click="delete(item)">Excluir</a></li>
							    <li><a href="" ng-click="duplicate(item)" data-toggle="modal" data-target="#newFranchisorModal">Duplicar</a></li>
							    <li class="divider"></li>
							    <li><a href="" ng-click="editProfiles(item)">Perfis de Usuários</a></li>
							    <li><a href="" ng-click="editUsers(item)">Usuários</a></li>
							    <li><a href="" ng-click="editProductCategories(item)">Categorias de <c:out value="${domainConfiguration.labels['PRODUCTS']}">Produtos</c:out></a></li>
							    <li><a href="" ng-click="editProducts(item)"><c:out value="${domainConfiguration.labels['PRODUCTS']}">Produtos</c:out></a></li>
							    <li><a href="" ng-click="editFranchisees(item)"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISEES']}">Lojas</c:out></a></li>
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
			<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newFranchisorModal"><i class="fa fa-plus"> Novo</i></button>
		</div>
    </div>
</div>