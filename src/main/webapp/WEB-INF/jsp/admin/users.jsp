<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- New User Modal -->
<div class="modal fade" id="newUserModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Usuário</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="user.name" placeholder="Informe o nome do usuário">
		            </div>
		            <div class="form-group" id="email">
		                <label for="inputEmail">E-mail</label>
		                <input id="inputEmail" type="text" class="form-control" ng-model="user.email" placeholder="Informe o e-mail do usuário">
		            </div>
		            <div class="form-group" id="preferedDomainKey">
		                <label for="inputPreferedDomainKey">Domínio principal</label>
		                <select id="inputPreferedDomainKey" class="form-control" ng-model="user.preferedDomainKey" ng-options="unit.key as unit.value for unit in domains | orderBy:'value'"></select>
		            </div>
		            <div class="form-group" id="enabled">
		            	<input type="checkbox" ng-model="user.enabled"> <label>Ativo</label>
		            </div>
		            <div class="form-group" id="generatedPassword">
		            	<label>Nova Senha</label>
			            <div class="input-group">
			                <span class="input-group-btn">
						    	<button class="btn btn-default" type="button" ng-click="generateOrResetPassword()">
						    		<span ng-show="user.id == 0">Gerar Senha</span>
						    		<span ng-hide="user.id == 0">Resetar Senha</span>
						    	</button>
						    </span>
						    <input id="inputGeneratedPassword" type="text" class="form-control" ng-model="user.password" readonly>
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

<!-- List of Existing Users -->
<div class="panel panel-default">
	<div class="panel-heading">
        <i class="fa fa-user"></i>
        <h3 class="panel-title">
            Usuários
        </h3>
    </div>
	<div class="panel-body">
		<form role="form">
			<div class="form-group" id="filter">
                <label for="inputFilter">Pesquisar:</label>
                <input id="inputFilter" type="text" class="form-control" ng-model="filter" ng-change="changeFilter()" placeholder="Informe o nome do usuário ou e-mail para filtrar">
            </div>
		</form>
		<table class="table table-striped table-bordered history-table">
            <thead>
            	<tr>
                    <th>Nome</th>
                    <th>E-mail</th>
                    <th>Ativo</th>
                    <th style="width: 95px">Ações</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="item in filteredData = (userList | orderBy:'name' | filter:filter) | limitTo:pageSize:pageSize*(page-1)">
                    <td>{{item.name}}</td>
                    <td>{{item.email}}</td>
                    <td>{{item.enabled ? 'Sim' : 'Não'}}</td>
                    <td>
                    	<button class="pull-center btn btn-default" ng-click="edit(item)" title="Editar" data-toggle="modal" data-target="#newUserModal"><i class="fa fa-pencil"></i></button>
                    	<button class="pull-center btn btn-default" ng-click="delete(item)" title="Excluir"><i class="fa fa-trash-o"></i></button>
                    </td>
                </tr>
        	</tbody>
        </table>
	</div>
	<!-- Loading -->
    <div id="list-overlay" class="overlay"></div>
    <div id="list-loading-img" class="loading-img"></div>
    <!-- end loading -->
    <div class="panel-footer clearfix">
    	<div class="pull-left">
    		<div class="btn-group" role="group">
	    		<button type="button" class="btn btn-default" ng-disabled="page == 1" ng-click="previousPage()"><i class="fa fa-chevron-left"></i></button>
	    		<button type="button" class="btn btn-default" ng-disabled="page == totalPages(filteredData)" ng-click="nextPage()"><i class="fa fa-chevron-right"></i></button>
	    	</div>
    	</div>
    	<div class="pull-left">
    		<p class="paging-label">Página: {{page}} / {{totalPages(filteredData)}}</p>
    	</div>
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newUserModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
</div>