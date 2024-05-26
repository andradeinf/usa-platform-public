<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- New Supplier Category Modal -->
<div class="modal fade" id="newTutorialModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Tutorial</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="tutorial.id" type="hidden" value="0"/>
					<div class="form-group" id="userProfile">
		                <label for="inputUserProfile">Perfil</label>		
		                <select id="inputUserProfile" class="form-control" ng-model="tutorial.userProfile" ng-options="unit.key as unit.value for unit in userProfileTypes | orderBy:'name'"></select>     
		            </div>	
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="tutorial.name" placeholder="Informe o nome do tutorial">
		            </div>
		            <div class="form-group" id="order">
                        <label for="inputOrder">Ordenação</label>
                       	<input id="inputOrder" type="text" class="form-control" ng-model="tutorial.order" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
                    </div>
                    <div class="form-group" id="url">
		                <label for="inputUrl">URL Youtube</label>
		                <input id="inputUrl" type="text" class="form-control" ng-model="tutorial.url" placeholder="Informe a URL do tutorial no youtube">
		            </div>
		            <div class="form-group text-angular-wrapper" id="description">
		            	<label for="inputName">Descrição</label>
				    	<div text-angular="text-angular" name="htmlcontent" ng-model="tutorial.description"></div>
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
        <i class="fa fa-youtube-play"></i>
        <h3 class="panel-title">
            Tutoriais
        </h3>
    </div>
	<div class="panel-body">
		<div ng-repeat="(userProfileDescription, tutorials) in tutorialsList | groupBy: 'userProfileDescription'">
			<h3>{{userProfileDescription}}</h3>
			<table class="table table-striped table-bordered history-table">
	            <thead>
	            	<tr>
	                    <th>Nome</th>
	                    <th>Descrição</th>
	                    <th style="width: 50px">Ações</th>
	                </tr>
	            </thead>
	            <tbody>
	                <tr ng-repeat="item in tutorials | orderBy:'order'">
	                    <td>{{item.name}}</td>
	                    <td><span ta-bind="text" ng-model="item.description"></span></td>
	                    <td>
	                    	<button class="pull-center btn btn-default" ng-click="edit(item)" title="Editar" data-toggle="modal" data-target="#newTutorialModal"><i class="fa fa-pencil"></i></button>
	                    	<button class="pull-center btn btn-default" ng-click="delete(item)" title="Excluir"><i class="fa fa-trash-o"></i></button>
	                    </td>
	                </tr>
	        	</tbody>
	        </table>
		</div>
		
	</div>
	<!-- Loading -->
    <div id="list-overlay" class="overlay"></div>
    <div id="list-loading-img" class="loading-img"></div>
    <!-- end loading -->
    <div class="panel-footer clearfix">
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newTutorialModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
</div>