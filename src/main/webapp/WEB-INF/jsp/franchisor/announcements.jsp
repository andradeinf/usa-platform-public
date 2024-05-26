<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- New Announcement Modal -->
<div class="modal fade" id="newAnnouncementModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Novo Aviso</h4>
	        </div>
	        <div class="modal-body">
	    		<form role="form">
					<input ng-model="announcement.id" type="hidden" value="0"/>
					<div class="form-group" id="title">
						<label for="inputTitle">Título</label>
						<input type="text" class="form-control" ng-model="announcement.title" placeholder="Título">
					</div>
					<div class="form-group text-angular-wrapper" id="toolbar">
		                <label for="inputMessage">Mensagem</label>
		                <text-angular-toolbar name="announcementToolbar" ta-toolbar="[['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'pre', 'quote'],['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol', 'redo', 'undo', 'clear'],['justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent'],['html', 'uploadImage', 'insertImage','insertLink', 'insertVideo']]"></text-angular-toolbar>
		            </div>
		            <div ng-show="uploadFileField" class="form-group" id="file">
		                <label for="inputFile">Selecione o Arquivo:</label>
		                <div class="input-group">
		                	<input id="inputFile" type="file" class="form-control" file-model="file" onchange="angular.element(this).scope().doUploadImage(this)" accept="image/jpeg, image/png"/>
							<span class="input-group-btn">
						        <button class="btn btn-default" type="button" ng-click="cancelUploadImage()">Cancelar</button>
						    </span>
						</div>
						<p class="help-block">Formatos aceitos: JPEG, PNG - Tamanho máximo: 50 MB</p>
		            </div>
		            <div ng-show="progressVisible" class="progress progress-u progress-xxs" id="progress">
		                <div class="progress-bar progress-bar-u" role="progressbar" aria-valuenow="{{progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{progress}}%">
		                </div>
		            </div>
		            <div class="form-group text-angular-wrapper" id="message">
		                <div text-angular="text-angular" ta-target-toolbars='announcementToolbar' name="htmlcontent" ng-model="announcement.message"></div>
		            </div>
		            <div class="form-group" id="restrictions">
		                <label><c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISEES_FILTER_ANNOUNCEMENT']}">Restringir aviso apenas para os franqueados selecionados</c:out>:</label>
						<div class="row">
							<div class="col-sm-4" ng-repeat="item in franchisees | orderBy:'name'"><input type="checkbox" ng-model="item.selected" ng-value="item.id"> {{item.name}}</div>
						</div>
						<p class="help-block"><c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISEES_WARNING_ANNOUNCEMENT']}">Caso nenhum franqueado seja selecionado, todos poderão visualizar esse aviso</c:out>.</p>
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

<!-- List of Announcements -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-comment"></i> <c:out value="${domainConfiguration.labels['ANNOUNCEMENTS']}">Quadro de Avisos</c:out></h3>
</div>
<div class="panel panel-default">
	<div class="panel-heading clearfix">
        <div class="btn-group pull-left" role="group">
    		<button type="button" class="btn btn-default" ng-click="fetchAnnouncements()">&nbsp;<i class="fa fa-repeat"></i>&nbsp;</button>
    		<button type="button" class="btn btn-default" ng-disabled="page==1" ng-click="previousAnnouncemens()">&nbsp;<i class="fa fa-chevron-left"></i>&nbsp;</button>
    		<button type="button" class="btn btn-default" ng-disabled="!nextEnabled" ng-click="nextAnnouncemens()">&nbsp;<i class="fa fa-chevron-right"></i>&nbsp;</button>
			<div class="btn-group" role="group">
			  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			    &nbsp;Itens por página: {{pageSize}}&nbsp;
			    <span class="caret"></span>
			  </button>
			  <ul class="dropdown-menu">
			    <li><a href="javascript:void(0);" ng-click="setPageSize(10)">10 itens</a></li>
			    <li><a href="javascript:void(0);" ng-click="setPageSize(25)">25 itens</a></li>
			    <li><a href="javascript:void(0);" ng-click="setPageSize(50)">50 itens</a></li>
			    <li><a href="javascript:void(0);" ng-click="setPageSize(100)">100 itens</a></li>
			  </ul>
			</div>
    	</div>
    	<div class="btn-group pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newAnnouncementModal"><i class="fa fa-plus"></i> Novo aviso</button>
    	</div>
    </div>
	<div class="panel-list-group">
		<ul class="list-group">
			<li class="list-group-item" ng-repeat="item in announcements | orderBy:'date':true">
				<div ng-if="item.status == 'DRAFT'" class="floating-label">Rascunho</div>
				<div class="row list-group-item-row">
					<div class="col-sm-12 list-group-item-column">
						<h4 class="list-group-item-title" ng-class="{ 'collapsed' : item.collapsed }">
							<strong ng-show="item.title.length > 0">{{item.title}}</strong>
							<small>
								<a class="toggle" href="javascript:void(0);" ng-click="toggleAnnouncementDetails(item)">
									<i class="fa fa-plus-square-o" ng-show="item.collapsed" title="Visualizar Conteúdo"></i>
			                        <i class="fa fa-minus-square-o" ng-show="!item.collapsed" title="Ocultar Conteúdo"></i>
			                    </a>
								&nbsp;&nbsp;{{item.date | date:'dd/MM/yyyy HH:mm'}}
								<span ng-if="item.franchiseeIds.length > 0 && !item.collapsed"> - <b>Visível apenas para:</b><span ng-repeat="franchisee in franchisees | orderBy:'name'" ng-if="item.franchiseeIds.includes(franchisee.id)"> {{franchisee.name}};</span></span>
								<span ng-if="item.franchiseeIds.length == 0 && !item.collapsed"> - <b>Visível para todos</b></span>
							</small>
						</h4>
						<div ng-show="!item.collapsed" ta-bind="text" ng-model="item.message"></div>
						<div class="list-group-item-actions" ng-if="item.status == 'DRAFT'">
		                   <ul class="list-inline comment-list-v2 pull-left">
		                   	<li class="pointer-cursor" ng-click="edit(item)" data-toggle="modal" data-target="#newAnnouncementModal"><i class="fa fa-pencil"></i> Editar</li>
		                    <li class="pointer-cursor" ng-click="delete(item)"><i class="fa fa-trash-o"></i> Excluir</li>
		                    <li class="pointer-cursor" ng-click="publish(item)"><i class="fa fa-paper-plane"></i> Enviar</li>
		                   </ul>
		               </div>
		               <div class="list-group-item-actions clearfix" ng-if="item.status == 'PUBLISHED' && !item.collapsed">
		                   <ul class="list-inline comment-list-v2 pull-left">
		                       <li class="pointer-cursor" ng-click="toggleAnnouncementCheckRead(item)"><i class="fa fa-list"></i> Controle de Leitura</li>
		                   </ul>
		               </div>
		               <div class="row announcement-read-control" ng-show="item.open && !item.collapsed">
		               		<div class="col-sm-4" ng-repeat="franchiseeItem in item.franchisees | orderBy:'franchisee.name'">
		               			<h1 class="pointer-cursor" ng-class="{'franchisee-open': franchiseeItem.open}" ng-click="toggleCheckReadFranchisee(item, franchiseeItem)"><i class="fa" ng-class="{'fa-minus-square-o': franchiseeItem.open, 'fa-plus-square-o': !franchiseeItem.open}"></i> {{franchiseeItem.franchisee.name}}</h1>
		               			<ul class="read-control-list" ng-show="franchiseeItem.open">
		               				<li ng-show="franchiseeItem.loading"><span class="text-info"><i class="fa fa-spinner fa-spin"></i> Carregando...</span></li>
		               				<li ng-show="!franchiseeItem.loading && franchiseeItem.notifications.length == 0"><span class="text-danger">Nenhum usuário encontrado</span></li>
	                				<li ng-repeat="notification in franchiseeItem.notifications | orderBy:'name'"><i class="fa" ng-class="{'fa-envelope-open': notification.isRead, 'fa-envelope': !notification.isRead}"></i> {{franchiseeItem.notificationUsers[notification.userId].name}}</li>
	                			</ul>
	                   		</div>
	                   </div>
					</div>
				</div>
			</li>
		</ul>
	</div>   	
   	<p ng-show="announcements.length == 0" class="text-info text-center margin-top-15">Não há avisos.</p>	
    <!-- Loading -->
    <div id="announcemensLoad">
   		<div class="overlay"></div>
    	<div class="loading-img"></div>
    </div>
    <!-- end loading -->
    <div class="panel-footer clearfix">
    	<div class="btn-group pull-left" role="group">
    		<button type="button" class="btn btn-default" ng-click="fetchAnnouncements()">&nbsp;<i class="fa fa-repeat"></i>&nbsp;</button>
    		<button type="button" class="btn btn-default" ng-disabled="page==1" ng-click="previousAnnouncemens()">&nbsp;<i class="fa fa-chevron-left"></i>&nbsp;</button>
    		<button type="button" class="btn btn-default" ng-disabled="!nextEnabled" ng-click="nextAnnouncemens()">&nbsp;<i class="fa fa-chevron-right"></i>&nbsp;</button>
			<div class="btn-group dropup" role="group">
			  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			    &nbsp;Itens por página: {{pageSize}}&nbsp;
			    <span class="caret"></span>
			  </button>
			  <ul class="dropdown-menu">
			    <li><a href="javascript:void(0);" ng-click="setPageSize(10)">10 itens</a></li>
			    <li><a href="javascript:void(0);" ng-click="setPageSize(25)">25 itens</a></li>
			    <li><a href="javascript:void(0);" ng-click="setPageSize(50)">50 itens</a></li>
			    <li><a href="javascript:void(0);" ng-click="setPageSize(100)">100 itens</a></li>
			  </ul>
			</div>
    	</div>
    	<div class="btn-group pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newAnnouncementModal"><i class="fa fa-plus"></i> Novo aviso</button>
    	</div>
    </div>
</div>
