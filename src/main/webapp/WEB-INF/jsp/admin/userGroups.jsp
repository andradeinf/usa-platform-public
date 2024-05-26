<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- New Product Category Modal -->
<div class="modal fade" id="newUserGroupModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Perfil</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="userGroup.id" type="hidden" value="0"/>		
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="userGroup.name" placeholder="Informe o nome do perfil">
		            </div>
		            <div class="form-group" id="receiveMessage">
                        <input type="checkbox" ng-model="userGroup.receiveMessage"> <label>Recebe mensagens através do sistema</label>
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

<!-- List of Existing Franchisor Profiles -->
<div class="panel panel-default">
	<div class="panel-heading">
        <i class="fa fa-user"></i>
        <h3 class="panel-title">
            Perfis
        </h3>
    </div>
	<div class="panel-body">
		<table class="table table-striped table-bordered history-table">
            <thead>
            	<tr>
                    <th>Nome</th>
                    <th style="width: 50px">Ações</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="item in userGroupList | orderBy:'name'">
                    <td>{{item.name}}</td>
                    <td>
                    	<button class="pull-center btn btn-default" ng-click="edit(item)" title="Editar" data-toggle="modal" data-target="#newUserGroupModal"><i class="fa fa-pencil"></i></button>
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
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newUserGroupModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
</div>