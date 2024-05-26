<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- New Supplier User Modal -->
<div class="modal fade" id="newSupplierUserModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Usuário</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="supplierUser.id" type="hidden" value="0"/>		
		            <div class="form-group" id="email">
		                <label for="inputEmail">E-mail</label>
		                <div class="input-group">
			                <input id="inputEmail" type="text" class="form-control" ng-model="supplierUser.user.email" ng-disabled="supplierUser.id != 0" placeholder="Informe o e-mail do usuário para pesquisar">
			                <span class="input-group-btn"><button ng-click="search()" class="btn" type="button" ng-disabled="supplierUser.id != 0">Pesquisar</button></span>
			            </div>
		            </div>
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="supplierUser.user.name" disabled>
		            </div>
		            <div class="form-group" id="profiles">
		            	<label for="inputProfile">Perfis:</label>
			            <ul class="list-unstyled">
							<li ng-repeat="item in userGroupsList | orderBy:'name'"><input type="checkbox" ng-model="supplierUserGroupList[item.id]"> {{item.name}}</li>
						</ul>
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

<!-- List of Existing Supplier Users -->
<div class="panel panel-default">
	<div class="panel-heading">
        <i class="fa fa-user"></i>
        <h3 class="panel-title">
            Usuários - <c:out value="${domainConfiguration.labels['SUPPLIER']}">Fornecedor</c:out>
        </h3>
    </div>
	<div class="panel-body">
		<table class="table table-striped table-bordered history-table">
            <thead>
            	<tr>
                    <th>Nome</th>
                    <th>E-mail</th>
                    <th style="width: 95px">Ações</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="item in supplierUserList | orderBy:'name'">
                    <td>{{item.user.name}}</td>
                    <td>{{item.user.email}}</td>
                    <td>
                    	<button class="pull-center btn btn-default" ng-click="edit(item)" title="Editar" data-toggle="modal" data-target="#newSupplierUserModal"><i class="fa fa-pencil"></i></button>
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
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newSupplierUserModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
</div>