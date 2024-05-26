<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- New Franchisor User Modal -->
<div class="modal fade" id="newConfiguratinModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Configuração</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="configuration.id" type="hidden" value="0"/>		
		            <div class="form-group" id="key">
		                <label for="inputKey">Key</label>
		                <input id="inputKey" type="text" class="form-control" ng-model="configuration.key" placeholder="Chave da configuração">
		            </div>
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="configuration.name" placeholder="Nome da configuração">
		            </div>
		            <div class="form-group" id="type">
		                <label for="inputType">Tipo</label>		
		                <select id="inputType" class="form-control" ng-model="configuration.type" ng-options="unit.key as unit.value for unit in configurationTypeSelect | orderBy:'value'"></select>     
		            </div>
		            <div class="form-group" id="stringValue" ng-show="configuration.type == 'STRING'">
		                <label for="inputStringValue">Valor</label>
		                <input id="inputStringValue" type="text" class="form-control" ng-model="configuration.stringValue" placeholder="Valor da configuração">
		            </div>
		            <div class="form-group text-angular-wrapper" id="stringHtmlValue" ng-show="configuration.type == 'HTML_STRING'">
		                <label for="inputStringHtmlValue">Valor</label>
		                <div text-angular="text-angular" name="htmlcontent" ng-model="configuration.stringValue"></div>
		            </div>
		            <div class="form-group" id="longValue" ng-show="configuration.type == 'LONG'">
		                <label for="inputLongValue">Valor</label>
		                <input id="inputLongValue" type="text" class="form-control" ng-model="configuration.longValue" placeholder="Valor da configuração">
		            </div>
		            <div class="form-group" id="booleanValue" ng-show="configuration.type == 'BOOLEAN'">
		                <label for="inputBooleanValue">Valor</label>
		                <select id="inputBooleanValue" class="form-control" ng-model="configuration.booleanValue" ng-options="unit.key as unit.value for unit in flagSelect"></select>
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
            Configurações do Sistema
        </h3>
    </div>
	<div class="panel-body">
		<table class="table table-striped table-bordered history-table">
            <thead>
            	<tr>
                    <th>Nome</th>
                    <th>Chave</th>
                    <th>Tipo</th>
                    <th>Valor</th>
                    <th style="width: 95px">Ações</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="item in configurationList | orderBy:'name'">
                    <td>{{item.key}}</td>
                    <td>{{item.name}}</td>
                    <td>{{item.typeDescription}}</td>
                    <td ng-show="item.type == 'STRING'">{{item.stringValue}}</td>
                    <td ng-show="item.type == 'HTML_STRING'"><span ta-bind="text" ng-model="item.stringValue"></span></td>
                    <td ng-show="item.type == 'LONG'">{{item.longValue}}</td>
                    <td ng-show="item.type == 'BOOLEAN'">{{item.booleanValue}}</td>
                    <td>
                    	<button class="pull-center btn btn-default" ng-click="edit(item)" title="Editar" data-toggle="modal" data-target="#newConfiguratinModal"><i class="fa fa-pencil"></i></button>
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
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newConfiguratinModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
</div>