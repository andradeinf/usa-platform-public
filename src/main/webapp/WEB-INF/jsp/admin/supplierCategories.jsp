<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- New Supplier Category Modal -->
<div class="modal fade" id="newSupplierCategoryModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Categoria de <c:out value="${domainConfiguration.labels['SUPPLIERS']}">Fornecedores</c:out></h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="supplierCategory.id" type="hidden" value="0"/>		
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="supplierCategory.name" placeholder="Informe o nome da categoria">
		            </div>
		            <div class="form-group" id="order">
                        <label for="inputOrder">Ordenação</label>
                       	<input id="inputOrder" type="text" class="form-control" ng-model="supplierCategory.order" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
                    </div>
                    <div class="form-group" id="options">
                    	<label for="inputOrder">Opções</label>
                    	<ul class="list-unstyled">
                        <li><input type="checkbox" ng-model="supplierCategory.hasStockControl"> Recebe pedidos através do sistema</li>
                        <li><input type="checkbox" ng-model="supplierCategory.visibleToFranchisees"> Visível para <c:out value="${domainConfiguration.labels['FRANCHISEES']}">Franqueados</c:out></li>
                        <li><input type="checkbox" ng-model="supplierCategory.receiveMessage"> Recebe mensagens através do sistema</li>
                    </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Cancelar</button>
    			<button ng-click="save()" class="btn btn-default"><i class="fa fa-save"></i> Salvar</button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- List of Existing Supplier Categories -->
<div class="panel panel-default">
	<div class="panel-heading">
        <i class="fa fa-truck"></i>
        <h3 class="panel-title">
            Categorias de <c:out value="${domainConfiguration.labels['SUPPLIERS']}">Fornecedores</c:out>
        </h3>
    </div>
	<div class="panel-body">
		<table class="table table-striped table-bordered history-table">
            <thead>
            	<tr>
                    <th>Nome</th>
                    <th>Ordenação</th>
                    <th style="width: 50px">Ações</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="item in supplierCategoryList | orderBy:'name'">
                    <td>{{item.name}}</td>
                    <td>{{item.order}}</td>
                    <td>
                    	<button class="pull-center btn btn-default" ng-click="edit(item)" title="Editar" data-toggle="modal" data-target="#newSupplierCategoryModal"><i class="fa fa-pencil"></i></button>
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
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newSupplierCategoryModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
</div>