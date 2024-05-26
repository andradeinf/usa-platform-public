<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- New Product Category Modal -->
<div class="modal fade" id="newProductCategoryModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Categoria de <c:out value="${domainConfiguration.labels['PRODUCTS']}">Produtos</c:out></h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="productCategory.id" type="hidden" value="0"/>		
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="productCategory.name" placeholder="Informe o nome da categoria">
		            </div>
		            <div class="form-group" id="notes">
		                <label for="inputnotes">Anotações</label>
		                <textarea id="inputnotes" class="form-control" ng-model="productCategory.notes" rows="3" placeholder="Informe as anotações"></textarea>
		            </div>
		            <div class="form-group" id="order">
                        <label for="inputOrder">Ordenação</label>
                       	<input id="inputOrder" type="text" class="form-control" ng-model="productCategory.order" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
                    </div>	
                    <div class="form-group" id="restriction">
                        <label for="inputRestriction">Restrito para</label>
                       	<table class="table table-bordered table-hover size-table" ng-show="productCategory.restrictions.length > 0"><!--  -->
                       		<thead>
		                        <tr>
		                            <th class="size-table-head">Estado</th>
		                            <th class="size-table-head">Cidade</th>
		                            <th class="size-table-head" style="width: 50px">Ações</th>
		                        </tr>
		                    </thead>
		                    <tbody>
		                    	<tr ng-repeat="restriction in productCategory.restrictions"><!-- ng-repeat="size in item.restrictions | orderBy:['groupName', 'name']" -->
		                            <td>{{getRestrictionState(restriction, false)}}</td>
		                            <td>{{getRestrictionCity(restriction)}}</td>
		                            <td>
				                    	<button class="pull-center btn btn-default btn-sm" ng-click="deleteRestriction(restriction)" title="Excluir"><i class="fa fa-trash-o"></i></button>
				                    </td>
		                        </tr>
		                    </tbody>
                       	</table>
                       	<div class="list-group-item-actions" ng-hide="productCategory.requestMode">
		                    <ul class="list-inline comment-list-v2 pull-left">
		                        <li><i class="fa fa-plus"></i> <a href="javascript:void(0);" ng-click="addRestriction()">Adicionar</a></li>
		                    </ul>
		                </div>
		                <div class="list-group-item-actions" ng-show="productCategory.requestMode">
							<h4><strong>Nova Restrição</strong></h4>
							<form class="form-inline">
		                        <ul class="list-unstyled">
		                        	<li>
		                        		<strong>Estado: </strong>
		                        		<select ng-model="restriction.stateId" ng-options="state.id as state.name for state in states  | orderBy:'name'" ng-change="onChangeState()"></select>
		                        	</li>
		                        	<li>
		                        		<strong>Cidade: </strong>
		                        		<select ng-model="restriction.cityId" ng-disabled="restiction.stateId == 0" ng-options="city.id as city.name for city in cities  | orderBy:'name'"></select>
		                        	</li>
		                            <li>
		                            	<div class="top-buffer">
			                            	<button class="btn btn-default btn-xs" type="button" ng-click="saveAddRestriction()">Enviar</button>
			                            	<button class="btn btn-default btn-xs" type="button" ng-click="cancelAddRestriction()">Cancelar</button>
		                            	</div>
		                            </li>
		                        </ul> 
		                	</form>
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

<!-- List of Existing Product Categories -->
<div class="panel panel-default">
	<div class="panel-heading">
        <i class="fa fa-user"></i>
        <h3 class="panel-title">
            Categorias de <c:out value="${domainConfiguration.labels['PRODUCTS']}">Produtos</c:out>
        </h3>
    </div>
	<div class="panel-body">
		<table class="table table-striped table-bordered history-table">
            <thead>
            	<tr>
                    <th>Nome</th>
                    <th>Anotações</th>
                    <th>Ordenação</th>
                    <th>Restrição</th>
                    <th style="width: 50px">Ações</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="item in productCategoryList | orderBy:'name'">
                    <td>{{item.name}}</td>
                    <td>{{item.notes}}</td>
                    <td>{{item.order}}</td>
                    <td><i class="fa fa-lock" ng-show="item.restrictions.length > 0" ng-attr-title="{{getRestrictionList(item.restrictions)}}"></i><i class="fa fa-unlock" ng-hide="item.restrictions.length > 0"></i></td>
                    <td>
                    	<button class="pull-center btn btn-default" ng-click="edit(item)" title="Editar" data-toggle="modal" data-target="#newProductCategoryModal"><i class="fa fa-pencil"></i></button>
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
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newProductCategoryModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
</div>