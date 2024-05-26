<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- New Message Topic Modal -->
<div class="modal fade" id="newMessageTopicModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Nova mensagem</h4>
	        </div>
	        <div class="modal-header">
				<form role="form">
					<div class="form-group" id="fromUserGroupId">
						<label for="inputFrom">De: {{loggedUser.name}}</label>
		                <select ng-hide="fromUserGroups.length == 0" class="form-control" ng-model="messageTopic.fromUserGroupId" ng-options="unit.id as unit.name for unit in fromUserGroups | orderBy:'name'" ng-change="changeFromUserGroup()">
		                	<option value=''>Selecione a sua área...</option>
		                </select>
					</div>
					<div class="form-group" id="toEntityProfile">
		                <label for="inputTo">Para:</label>		
						<select id="inputTo" class="form-control" ng-model="messageTopic.toEntityProfile" ng-options="unit.key as unit.value for unit in toProfileTypes | orderBy:'value'" ng-change="changeToEntityProfile()">
		                	<option value=''>Selecione o tipo de mensagem...</option>
		                </select>
		            </div>
					<div class="form-group" id="toEntityId">
		                <select ng-hide="toEntities.length == 0" class="form-control" ng-model="messageTopic.toEntityId" ng-options="unit.id as unit.name for unit in toEntities | orderBy:'name'" ng-change="changeToEntity()">
		                	<option value=''>Selecione o destinatário...</option>
		                </select>
		            </div>
		            <div class="form-group" id="toUserGroupId">
		                <select ng-hide="toUserGroups.length == 0" class="form-control" ng-model="messageTopic.toUserGroupId" ng-options="unit.id as unit.name for unit in toUserGroups | orderBy:'name'" ng-change="changeToUserGroup()">
		                	<option value=''>Selecione a área de destino...</option>
		                </select>
		            </div>
		    	</form>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<div class="form-group" id="title">
						<input type="text" class="form-control" ng-model="messageTopic.title" placeholder="Título">
					</div>	
		            <div class="form-group text-angular-wrapper" id="newMessage">
						<div text-angular="text-angular" name="newMessageContent" ng-model="messageTopic.message" ta-text-editor-class="form-control newMessage-height" ta-html-editor-class="form-control newMessage-height" placeholder="Escreva sua mensagem aqui" ta-toolbar="[['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'pre', 'quote'],['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol', 'redo', 'undo', 'clear'],['justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent'],['html', 'insertImage','insertLink', 'insertVideo']]"></div>
					</div>	
		    	</form> 
		    	<div class="attachments" ng-hide="attachments.length == 0">
					<div class="row attachment" ng-repeat="item in attachments">
						<div class="col-sm-8">
							<span ng-if="item.id == 0">{{item.name}}</span>
							<span ng-if="item.id != 0"><a href ng-href="/ws/messages/attachment/download/{{item.id}}">{{item.name}}</a></span>
						</div>
						<div class="col-sm-3">
							<span ng-if="item.error.length > 0" class="attachment-error">{{item.error}}</span>
							<div ng-show="item.progressVisible" class="progress attachment-bar">
						      <div class="progress-bar active" role="progressbar" aria-valuenow="{{item.progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{item.progress}}%">
						        <span class="sr-only">{{item.progress}}%</span>
						      </div>
						    </div>						    
						</div>		
						<div class="col-sm-1">
					    	<span class="pull-right pointer-cursor" ng-click="deleteAttachment(item)" title="Excluir Anexo"><b>x</b></span>
					    </div>				
					</div>
				</div>
				<div class="form-group" id="file" ng-show="uploadFileField">
	                <label for="inputFile">Selecione o Arquivo:</label>
	                <div class="input-group">
	                	<input id="inputFile" type="file" class="form-control" file-model="file" onchange="angular.element(this).scope().doAddAttachment(this)"/>
						<span class="input-group-btn">
					        <button class="btn btn-default" type="button" ng-click="cancelAddAttachment()">Cancelar</button>
					    </span>
					</div>
	            </div>
	        </div>
	        <div class="modal-footer">
	        	<button type="button" class="btn btn-default" title="Adicionar Anexo" ng-click="addAttachment()"><i class="fa fa-paperclip"></i></button>
	        	<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Cancelar</button>
	        	<button type="button" class="btn btn-default" ng-click="save()"><i class="fa fa-paper-plane" aria-hidden="true"></i> Enviar</button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- View Message Topic Modal -->
<div class="modal fade" id="viewMessageTopicModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">{{messageTopic.title}}</h4>
	        </div>
	        <div class="modal-header">
		        <div class="row">
	   				<div class="col-sm-5">
	   					<strong>Criada por: </strong>
	   					<span ng-hide="messageTopic.fromUserGroupId == 0">{{userGroups[messageTopic.fromUserGroupId].name}}</span>
	   					<span ng-show="messageTopic.fromUserGroupId != 0 && entities[messageTopic.fromEntityId].name"> - </span>
	   					{{entities[messageTopic.fromEntityId].name}}
	   				</div>
	   				<div class="col-sm-5">
	   					<strong>Enviada para: </strong>
	   					<span ng-hide="messageTopic.toUserGroupId == 0">{{userGroups[messageTopic.toUserGroupId].name}}</span>
	   					<span ng-show="messageTopic.toUserGroupId != 0 && entities[messageTopic.toEntityId].name"> - </span>
	   					{{entities[messageTopic.toEntityId].name}}
	   				</div>
	   				<div class="col-sm-2">
						{{messageTopic.date | date:'dd/MM/yyyy HH:mm'}}
	   				</div>
	   			</div>
			</div>
			<div class="modal-header" ng-hide="newLabelForm">
				<div class="row">
					<div class="col-md-10 col-sm-9 col-xs-12">
						<strong>Marcadores: </strong>
						<span class="message-label message-label-color label" ng-repeat="labelId in messageTopic.labels">
							{{userLabelsMap[labelId]}}
							<a href ng-click="removeLabel(labelId)" title="Remover Marcador desta Mensagem"><i class="fa fa-times"></i></a> 
						</span>
					</div>
					<div class="col-md-2 col-sm-3 col-xs-12">
						<a class="btn btn-default btn-sm" style="display: initial;" title="Arquivar Mensagem" ng-click="archive()"><span class="fa fa-archive"></span></a>
						<span class="dropdown">
							<a class="btn btn-default btn-sm" style="display: initial;" title="Mover Mensagem Para" data-toggle="dropdown"><span class="fa fa-folder"></span></a>
							<ul class="dropdown-menu">
								<li ng-repeat="label in filteredUserLabels()"><a href ng-click="moveTo(label.id)">{{label.name}}</a></li>
								<li role="separator" class="divider" ng-show="filteredUserLabels().length > 0"></li>
								<li><a href ng-click="moveToNewLabel()">Novo Marcador</a></li>
							</ul>
						</span>
						<span class="dropdown">
							<a class="btn btn-default btn-sm" style="display: initial;" title="Adicionar Marcador" data-toggle="dropdown"><span class="fa fa-tag"></span></a>
							<ul class="dropdown-menu">
								<li ng-repeat="label in filteredUserLabels()"><a href ng-click="addLabel(label.id)">{{label.name}}</a></li>
								<li role="separator" class="divider" ng-show="filteredUserLabels().length > 0"></li>
								<li><a href ng-click="addNewLabel()">Novo Marcador</a></li>
							</ul>
						</span>
					</div>
				</div>				
			</div>
			<div class="modal-header" ng-show="newLabelForm">
				<form class="form-inline new-label-form">
					<div class="form-group">
						<label for="labelName">Nome do Marcador: </label>
						<input type="text" class="form-control input-sm single-line" id="labelName" ng-model="newLabelName" />
					</div>
					<button type="button" class="btn btn-default btn-sm" ng-click="saveNewLabel()"><i class="fa fa-plus"></i> Adicionar</button>
					<button type="button" class="btn btn-default btn-sm" ng-click="cancelAddNewLabel()"><i class="fa fa-times"></i> Cancelar</button>
				</form>
			</div>
	        <div class="modal-body message-topic-view-reply">
	        	<div class="clearfix">
		        	<form role="form">
		        		<div class="form-group text-angular-wrapper" id="newComment">
							<div text-angular="text-angular" name="newCommentContent" ng-model="messageComment.message" ta-text-editor-class="form-control newComment-height" ta-html-editor-class="form-control newComment-height" placeholder="Adicione comentário aqui..." ta-toolbar="[['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'pre', 'quote'],['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol', 'redo', 'undo', 'clear'],['justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent'],['html', 'insertImage','insertLink', 'insertVideo']]"></div>
						</div>
		        	</form>
		        	<div class="attachments" ng-hide="attachments.length == 0">
						<div class="row attachment" ng-repeat="item in attachments">
							<div class="col-sm-8">
								<span ng-if="item.id == 0">{{item.name}}</span>
								<span ng-if="item.id != 0"><a href ng-href="/ws/messages/attachment/download/{{item.id}}">{{item.name}}</a></span>
							</div>
							<div class="col-sm-3">
								<span ng-if="item.error.length > 0" class="attachment-error">{{item.error}}</span>
								<div ng-show="item.progressVisible" class="progress attachment-bar">
							      <div class="progress-bar active" role="progressbar" aria-valuenow="{{item.progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{item.progress}}%">
							        <span class="sr-only">{{item.progress}}%</span>
							      </div>
							    </div>						    
							</div>		
							<div class="col-sm-1">
						    	<span class="pull-right pointer-cursor" ng-click="deleteAttachment(item)" title="Excluir Anexo"><b>x</b></span>
						    </div>				
						</div>
					</div>
		        	<div class="form-group" id="file" ng-show="uploadFileField">
		                <label for="inputFile2">Selecione o Arquivo:</label>
		                <div class="input-group">
		                	<input id="inputFile2" type="file" class="form-control" file-model="file" onchange="angular.element(this).scope().doAddAttachment(this)"/>
							<span class="input-group-btn">
						        <button class="btn btn-default" type="button" ng-click="cancelAddAttachment()">Cancelar</button>
						    </span>
						</div>
					</div>
		        	<div ng-hide="messageComment.message.length == 0 || uploadFileField" class="pull-right">
		        		<button type="button" class="btn btn-default" title="Adicionar Anexo" ng-click="addAttachment()"><i class="fa fa-paperclip"></i></button>
						<button class="btn btn-default btn-sm" type="submit" ng-click="saveComment()"><i class="fa fa-paper-plane" aria-hidden="true"></i> Enviar</button>
	        		</div>
	        	</div>        	
	        </div>
	        <div class="modal-body message-comments">
        		<div class="row message-comments-row" ng-repeat="item in messageComments | orderBy:'date':true">
        			<div class="col-xs-10 message-comment-item" ng-class="commentClasses(item)">
        				<div class="row">
        					<div class="col-sm-7 col-md-9">
        						<a href ng-click="checkRead(item)" class="filter" title="Controle de Leitura"><i class="fa fa-list"></i></a>
        						<strong>Enviado por:</strong> 
        						<span ng-if="users[item.userId]">{{users[item.userId].name}}</span>
        						<span ng-if="!users[item.userId]"><i>Usuário não encontrado</i></span>
        					</div>
        					<div class="col-sm-5 col-md-3">
        						{{item.date | date:'dd/MM/yyyy HH:mm'}}
        					</div>
        				</div>
						<div class="message-text" ta-bind="text" ng-model="item.message"></div>
	        			<div class="attachments" ng-hide="item.attachments.length == 0">
							<div class="row attachment" ng-repeat="attachment in item.attachments">
								<div class="col-sm-12">
									<a href ng-href="/ws/messages/attachment/download/{{attachment.id}}">{{attachment.name}}</a>
								</div>			
							</div>
						</div>
	        			<div class="row read-control" ng-show="item.open">
	        				<div class="col-sm-6" ng-show="item.fromNotifications.length > 0">
	        					<h1>
	        						<span ng-hide="messageTopic.fromUserGroupId == 0">{{userGroups[messageTopic.fromUserGroupId].name}}</span>
				   					<span ng-show="messageTopic.fromUserGroupId != 0 && entities[messageTopic.fromEntityId].name"> - </span>
				   					{{entities[messageTopic.fromEntityId].name}}
	        					</h1>
	        					<ul class="read-control-list">
	                				<li ng-repeat="notification in item.fromNotifications | orderBy:'name'"><i class="fa" ng-class="{'fa-envelope-open': notification.isRead, 'fa-envelope': !notification.isRead}"></i> {{item.notificationUsers[notification.userId].name}}</li>
	                			</ul>
	        				</div>
	        				<div class="col-sm-6" ng-show="item.toNotifications.length > 0">
	        					<h1>
	        						<span ng-hide="messageTopic.toUserGroupId == 0">{{userGroups[messageTopic.toUserGroupId].name}}</span>
				   					<span ng-show="messageTopic.toUserGroupId != 0 && entities[messageTopic.toEntityId].name"> - </span>
				   					{{entities[messageTopic.toEntityId].name}}
	        					</h1>
	        					<ul class="read-control-list">
	                				<li ng-repeat="notification in item.toNotifications | orderBy:'name'"><i class="fa" ng-class="{'fa-envelope-open': notification.isRead, 'fa-envelope': !notification.isRead}"></i> {{item.notificationUsers[notification.userId].name}}</li>
	                			</ul>
	        				</div>
	        				<div class="col-sm-6" ng-show="item.fromNotifications.length == 0 && item.toNotifications.length == 0">
	        					<p class="no-info">Não há informação de leitura para esta mensagem.</p>
	        				</div>
	        			</div>
	        		</div>
        		</div>	        		
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- List of Messages -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-envelope"></i> <c:out value="${domainConfiguration.labels['MESSAGES']}">Central de Atendimento</c:out></h3>
</div>
<div class="panel panel-default">
	<div class="panel-heading clearfix" ng-hide="loggedUser.selectedRole != 'ADMINISTRATOR' && fromUserGroups.length == 0">
    	<div class="btn-group pull-left" role="group">
    		<button type="button" class="btn btn-default" ng-click="fetchMessageTopics()"><i class="fa fa-repeat"></i></button>
    		<button type="button" class="btn btn-default" ng-disabled="page==1" ng-click="previousTopics()"><i class="fa fa-chevron-left"></i></button>
    		<button type="button" class="btn btn-default" ng-disabled="!nextEnabled" ng-click="nextTopics()"><i class="fa fa-chevron-right"></i></button>
    	</div>
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newMessageTopicModal"><i class="fa fa-plus"> Nova Mensagem</i></button>
    	</div>
    </div>
    <div class="panel-body panel-filter" >
		<div class="clearfix">
			<span class="pull-right"><a class="filter" href ng-click="toggleAdvancedFilter()"><i class="fa fa-search"></i> Filtro Avançado</a></span>
		</div>
		<div class="form-group" ng-hide="advancedFilter || fromUserGroups.length <= 1">
			<label for="userGroup">Áreas:</label>
               <select class="form-control" ng-model="userGroupIdFilter" ng-options="unit.id as unit.name for unit in fromUserGroups | orderBy:'name'" ng-change="searchTopics()">
               	<option value="">Todas as Áreas</option>
               </select>
		</div>
        <form ng-show="advancedFilter"> 
        	<div class="form-group" >
        		<div class="row">
        			<div class="col-sm-6">
        				<input type="radio" ng-model="filterType" value="TO"> Enviado
        			</div>
        			<div class="col-sm-6">
        				<input type="radio" ng-model="filterType" value="FROM"> Recebido
        			</div>
        		</div>
			</div>
            <div class="form-group" ng-show="fromUserGroups.length > 1 && filterType == 'TO'">
				<label for="userGroup">De:</label>
                <select class="form-control" ng-model="userGroupIdFilter" ng-options="unit.id as unit.name for unit in fromUserGroups | orderBy:'name'"">
                	<option value="">Todas as Áreas</option>
                </select>
			</div>
			<div class="form-group"> 
        		<label ng-show="filterType == 'FROM'">De:</label> 
        		<label ng-show="filterType == 'TO'">Para:</label> 
                <select class="form-control" ng-model="toEntityProfileFilter" ng-options="unit.key as unit.value for unit in toProfileTypes | orderBy:'value'" ng-change="changeToEntityProfileFilter()"> 
                  <option value="">Todos os Tipos de Mensagem</option> 
                </select> 
            </div>
            <div class="form-group" ng-hide="toEntitiesFilter.length == 0"> 
                <select class="form-control" ng-model="toEntityIdFilter" ng-options="unit.id as unit.name for unit in toEntitiesFilter | orderBy:'name'" ng-change="changeToEntityFilter()"> 
                  <option value="">Todos os Destinatários</option> 
                </select> 
            </div>
            <div class="form-group" ng-hide="toUserGroupsFilter.length == 0"> 
                <select class="form-control" ng-model="toUserGroupIdFilter" ng-options="unit.id as unit.name for unit in toUserGroupsFilter | orderBy:'name'" ng-change="changeToUserGroupFilter()"> 
                  <option value="">Todas as Áreas</option> 
                </select> 
            </div> 
            <div class="form-group" ng-show="fromUserGroups.length > 1 && filterType == 'FROM'">
				<label for="userGroup">Para:</label>
                <select class="form-control" ng-model="userGroupIdFilter" ng-options="unit.id as unit.name for unit in fromUserGroups | orderBy:'name'">
                	<option value="">Todas as Áreas</option>
                </select>
			</div>
			<div class="form-group">
				<label for="title">Título:</label>
			    <input id="title" type="text" class="form-control" ng-model="titleFilter" placeholder="Informe palavra(s) para pesquisar"/>
			</div>
			<button class="btn btn-default btn-block" ng-click="searchTopics()"><i class="fa fa-search"> Pesquisar</i></button>		                     	
        </form>
    </div>
   	<ul class="list-group message-topics">
   		<li class="list-group-item message-topic-item" ng-show="messageTopics.length == 0">
   			<p class="info text-info text-center">Você não possui mensagens...</p>
   		</li>
   		<li class="list-group-item message-topic-item pointer-cursor" ng-click="view(item)" ng-repeat="item in messageTopics | orderBy:'updateDate':true" data-toggle="modal" data-target="#viewMessageTopicModal">
   			<div class="row">
   				<div class="col-sm-5">
   					<strong>Criada por: </strong>
   					<span ng-hide="item.fromUserGroupId == 0">{{userGroups[item.fromUserGroupId].name}}</span>
   					<span ng-show="item.fromUserGroupId != 0 && entities[item.fromEntityId].name"> - </span>
   					{{entities[item.fromEntityId].name}}
   				</div>
   				<div class="col-sm-5">
   					<strong>Enviada para: </strong>
   					<span ng-hide="item.toUserGroupId == 0">{{userGroups[item.toUserGroupId].name}}</span>
   					<span ng-show="item.toUserGroupId != 0 && entities[item.toEntityId].name"> - </span>
   					{{entities[item.toEntityId].name}}
   				</div>
   				<div class="col-sm-2">
   					{{item.date | date:'dd/MM/yyyy HH:mm'}}
   				</div>
   			</div>
   			<div class="title">
   				<span ng-show="messageTopicBadgeCount(messageTopicsUnreadNotifications[item.id]) > 0" class="badge">{{messageTopicBadgeCount(messageTopicsUnreadNotifications[item.id])}}</span>
   				<strong>{{item.title}}</strong>
   			</div>
   		</li>
   	</ul>
	<!-- Loading -->
	<div id="messageTopicsLoad"></div>
    <!-- end loading -->
    <div class="panel-footer clearfix" ng-hide="loggedUser.selectedRole != 'ADMINISTRATOR' && fromUserGroups.length == 0">
    	<div class="btn-group pull-left" role="group">
    		<button type="button" class="btn btn-default" ng-click="fetchMessageTopics()"><i class="fa fa-repeat"></i></button>
    		<button type="button" class="btn btn-default" ng-disabled="page==1" ng-click="previousTopics()"><i class="fa fa-chevron-left"></i></button>
    		<button type="button" class="btn btn-default" ng-disabled="!nextEnabled" ng-click="nextTopics()"><i class="fa fa-chevron-right"></i></button>
    	</div>
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newMessageTopicModal"><i class="fa fa-plus"> Nova Mensagem</i></button>
    	</div>
    </div>
</div>