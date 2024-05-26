<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- New Folder Modal -->
<div class="modal fade" id="newFolderModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Pasta</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="folder.id" type="hidden" value="0"/>		
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="folder.name" placeholder="Informe o nome da pasta">
		            </div>
		            <div class="form-group" id="order">
                        <label for="inputOrder">Ordenação</label>
                       	<input id="inputOrder" type="number" class="form-control" ng-model="folder.order" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
                    </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Cancelar</button>
    			<button ng-click="saveFolder()" class="btn btn-default"><i class="fa fa-save"></i> Salvar</button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- Add File Modal -->
<div class="modal fade" id="newFileModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Selecionar</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">		
					<div class="form-group" id="file">
						<label for="inputFile">Arquivos:</label>
						<div class="attachments" ng-hide="uploadedFiles.length == 0">
							<div class="row attachment" ng-repeat="item in uploadedFiles">
								<div class="col-sm-8">
									<span>{{item.name}}</span>
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
									<span class="pull-right pointer-cursor" ng-click="deleteUploadedFile(item)" title="Excluir Arquivo"><b>x</b></span>
								</div>				
							</div>
						</div>
		                <input id="inputFile" type="file" class="form-control" multiple onchange="angular.element(this).scope().doUploadFile(this)"/>
		                <p class="help-block">Tamanho máximo: 50 MB por arquivo</p>
					</div>
		            <div class="form-group" id="restrictions">
		            	<label>Acesso:</label>
		            	<ul class="list-unstyled">
		                	<li><input type="radio" ng-model="accessRestricted" ng-value="false"> <c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISOR_FRANCHISEES']}">Franqueador e Franqueados</c:out></li>
		                	<li><input type="radio" ng-model="accessRestricted" ng-value="true"> <c:out value="${domainConfiguration.labels['VISIBILITY_ONLY_FRANCHISOR']}">Apenas Franqueador</c:out></li>
		                </ul>
		            </div>
		            <div class="form-group" id="restrictions" ng-hide="accessRestricted">
		                <label><c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISEES_FILTER_FILE']}">Restringir acesso apenas para os franqueados selecionados abaixo</c:out>:</label>
		                <ul class="list-unstyled">
		                	<li ng-repeat="item in franchisees | orderBy:'name'"><input type="checkbox" ng-model="item.selected" ng-value="item.id"> {{item.name}}</li>
						</ul>
						<p class="help-block"><c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISEES_WARNING_FILE']}">Caso nenhum franqueado seja selecionado, todos terão acesso</c:out>.</p>
		            </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
	            <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Cancelar</i></button>
        		<button ng-click="saveNewFiles()" class="btn btn-default"><i class="fa fa-save"> Upload</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- Edit File Modal -->
<div class="modal fade" id="editFileModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Alterar Permissões</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">		
		            <div class="form-group" id="restrictions">
		            	<label>Acesso:</label>
		            	<ul class="list-unstyled">
		                	<li><input type="radio" ng-model="fileToEdit.accessRestricted" ng-value="false"> <c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISOR_FRANCHISEES']}">Franqueador e Franqueados</c:out></li>
		                	<li><input type="radio" ng-model="fileToEdit.accessRestricted" ng-value="true"> <c:out value="${domainConfiguration.labels['VISIBILITY_ONLY_FRANCHISOR']}">Apenas Franqueador</c:out></li>
		                </ul>
		            </div>
		            <div class="form-group" id="restrictions" ng-hide="fileToEdit.accessRestricted">
		                <label><c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISEES_FILTER_FILE']}">Restringir acesso apenas para os franqueados selecionados abaixo</c:out>:</label>
		                <ul class="list-unstyled">
		                	<li ng-repeat="item in franchisees | orderBy:'name'"><input type="checkbox" ng-model="item.selected" ng-value="item.id"> {{item.name}}</li>
						</ul>
						<p class="help-block"><c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISEES_WARNING_FILE']}">Caso nenhum franqueado seja selecionado, todos terão acesso</c:out>.</p>
		            </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
	            <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Cancelar</i></button>
        		<button ng-click="saveEditFile()" class="btn btn-default"><i class="fa fa-save"> Salvar</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- Move Folder/File modal -->
<div class="modal fade" id="moveFolderOrFile" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Mover</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<div class="form-group">
						<label>Mover "{{moveItem.name}}" para cá:</label>
						<nav class="documents-path">
							<ol class="breadcrumb">
								<li class="breadcrumb-item" ng-repeat="item in movePath"><button type="button" class="btn btn-sm btn-light" ng-click="navigateMove(item)" >{{item.name}}</button></li>
							</ol>
						</nav>
					</div>
					<div class="form-group" ng-show="moveFolders.length > 0">
		            	<label>Navegar para pasta:</label>
						<ul class="document-list">
							<li ng-repeat="folder in moveFolders | orderBy:'name'" class="pointer-cursor documents-folder" ng-click="refreshMoveFolder(folder)"><i class="fa fa-folder icon-padding"></i>{{folder.name}}</li>
						</ul>
		            </div>					
		    	</form>
	        </div>
	        <div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Cancelar</button>
    			<button ng-click="saveMoveFolderOrFile()" ng-disabled="(moveType == 'folder' && moveItem.parentId == currentMoveFolder.id) || (moveType == 'file' && moveItem.folderId == currentMoveFolder.id)" class="btn btn-default"><i class="fa fa-save"></i> Salvar</button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- List of Documents -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-folder"></i> <c:out value="${domainConfiguration.labels['FILES']}">Documentos</c:out></h3>
</div>
<div class="panel panel-default">
	<div class="panel-heading clearfix">
		<div class="pull-right">
    		<button class="btn btn-default" ng-click="newFolder()" data-toggle="modal" data-target="#newFolderModal"><i class="fa fa-plus"> Nova pasta</i></button>
    		<button class="btn btn-default" ng-click="addFile()" data-toggle="modal" data-target="#newFileModal"><i class="fa fa-upload"> Carregar Arquivo</i></button>
    	</div>
	</div>
	<nav class="documents-path">
		<ol class="breadcrumb">
			<li class="breadcrumb-item" ng-repeat="item in path"><button type="button" class="btn btn-sm btn-light" ng-click="navigate(item)" >{{item.name}}</button></li>
		</ol>
	</nav>
	<table class="table">
		<thead>
			<tr>
				<th style="padding-left: 30px" colspan="2" class="pointer-cursor" ng-click="sort('name')">Nome <i class="fa fa-sort-desc" ng-show="sortColumn == 'name' && !sortOrder"></i><i class="fa fa-sort-asc" ng-show="sortColumn == 'name' && sortOrder"></i></th>
				<th style="width: 150px" class="pointer-cursor" ng-click="sort('date')">Data <i class="fa fa-sort-desc" ng-show="sortColumn == 'date' && !sortOrder"></i><i class="fa fa-sort-asc" ng-show="sortColumn == 'date' && sortOrder"></i></th>
				<th style="width: 100px" class="pointer-cursor" ng-click="sort('size')">Tamanho <i class="fa fa-sort-desc" ng-show="sortColumn == 'size' && !sortOrder"></i><i class="fa fa-sort-asc" ng-show="sortColumn == 'size' && sortOrder"></i></th>
				<th style="width: 130px; padding-left: 20px">Ações</th>
			</tr>
		</thead>
		<tbody>
			<tr ng-repeat="folder in folders | orderBy:['order','name']">
				<td style="padding-left: 30px" colspan="4" class="pointer-cursor documents-folder" ng-click="openFolder(folder)"><i class="fa fa-folder icon-padding"></i>{{folder.name}}</td>
				<td style="width: 130px; padding-left: 20px">
					<div class="btn-group btn-group-sm" role="group">
						<button class="pull-center btn btn-sm btn-default" ng-click="editFolder(folder)" title="Editar Pasta" data-toggle="modal" data-target="#newFolderModal"><i class="fa fa-pencil"></i></button>
						<button class="pull-center btn btn-sm btn-default" ng-click="deleteFolder(folder)" title="Excluir Pasta"><i class="fa fa-trash-o"></i></button>					
						<button class="pull-center btn btn-sm btn-default" ng-click="moveFolderOrFile(folder, 'folder')" title="Mover Pasta" data-toggle="modal" data-target="#moveFolderOrFile"><i class="fa fa-exchange"></i></button>					
					</div>
					<input  ng-show="moveMode" type="checkbox" ng-model="moveChecked[folder.id].checked"/>
				</td>
			</tr>
			<tr ng-repeat="file in files | orderBy:sortColumn:sortOrder">
				<td style="padding-left: 30px"><i class="fa" ng-class="contentTypeClass(file.contentType)"></i><input type="text" class="presentation-input" ng-model="file.name" title="{{file.name}}"/></td>
				<td style="width: 20px; text-align: center"><i class="fa fa-lock" ng-show="file.accessRestricted"></i><i class="fa fa-unlock-alt" ng-show="!file.accessRestricted && file.franchiseeIds.length > 0"></i><i class="fa fa-unlock" ng-show="!file.accessRestricted && file.franchiseeIds.length == 0"></i></td>
				<td style="width: 150px">{{file.date | date:'dd/MM/yyyy HH:mm'}}</td>
				<td style="width: 100px; text-align: right">{{formatFileSize(file.size)}}</td>
				<td style="width: 170px; padding-left: 20px">
					<div class="btn-group btn-group-sm" role="group">
						<a class="pull-center btn btn-sm btn-default" href ng-href="/ws/documents/file/{{file.id}}" title="Baixar arquivo"><i class="fa fa-download"></i></a>
						<button class="pull-center btn btn-sm btn-default" ng-click="editFile(file)" title="Alterar permissões" data-toggle="modal" data-target="#editFileModal"><i class="fa fa-edit"></i></button>
						<button class="pull-center btn btn-sm btn-default" ng-click="deleteFile(file)" title="Excluir Arquivo"><i class="fa fa-trash-o"></i></button>	
						<button class="pull-center btn btn-sm btn-default" ng-click="moveFolderOrFile(file, 'file')" title="Mover Arquivo" data-toggle="modal" data-target="#moveFolderOrFile"><i class="fa fa-exchange"></i></button>					
					</div>
					<input  ng-show="moveMode" type="checkbox" ng-model="moveChecked[file.id].checked"/>
				</td>
			</tr>
			<tr ng-show="folders.length == 0 && files.length == 0">
				<td colspan="5" style="padding-left: 30px"><i>Não há documentos nesta pasta</i></td>
			</tr>
			<tr ng-show="moveMode">
				<td colspan="5" style="padding-left: 30px"><i>Selecione os arquivos e/ou pastas que deseja mover e clique abaixo para selecionar a pasta de destino.</i></td>		
			</tr>
		</tbody>
	</table>
    <div class="panel-footer clearfix">
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="newFolder()" data-toggle="modal" data-target="#newFolderModal"><i class="fa fa-plus"> Nova pasta</i></button>
    		<button class="btn btn-default" ng-click="addFile()" data-toggle="modal" data-target="#newFileModal"><i class="fa fa-upload"> Carregar Arquivo</i></button>
    	</div>
    </div>
</div>
<div id="documents-loadingArea"></div>
<div class="legenda">
	<label>Controle de acesso:</label>
	<i class="fa fa-lock"> <c:out value="${domainConfiguration.labels['VISIBILITY_ONLY_FRANCHISOR']}">Apenas Franqueador</c:out></i> - 
	<i class="fa fa-unlock-alt"> <c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISOR_SOME_FRANCHISEES']}">Franqueador e Alguns Franqueados</c:out></i> - 
	<i class="fa fa-unlock"> <c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISOR_ALL_FRANCHISEES']}">Franqueador e Todos os Franqueados</c:out></i>
</div>
<div class="legenda">
	<label>Atenção:</label> Novos documentos e alterações em documentos e pastas só serão visíveis para 
	<c:out value="${domainConfiguration.labels['FRANCHISEES']}">Franqueados</c:out> 
	após 5 minutos.
</div>