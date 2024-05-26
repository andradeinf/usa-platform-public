<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- New Message Label Modal -->
<div class="modal fade" id="newMessageLabelModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Marcadores</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="label.id" type="hidden" value="0"/>		
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="label.name" placeholder="Informe o nome do marcador">
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

<!-- List of Existing Message Labels -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-cog"></i> Marcadores</h3>
</div>
<div class="panel panel-default">
	<div class="panel-heading clearfix">
		<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newMessageLabelModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
	<div class="panel-body">
		<table class="table table-striped table-bordered history-table" ng-show="labelList.length > 0">
            <thead>
            	<tr>
                    <th>Nome</th>
                    <th style="width: 50px">Ações</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="item in labelList | orderBy:'name'">
                    <td>{{item.name}}</td>
                    <td>
                    	<button class="pull-center btn btn-default" ng-click="edit(item)" title="Editar" data-toggle="modal" data-target="#newMessageLabelModal"><i class="fa fa-pencil"></i></button>
                    	<button class="pull-center btn btn-default" ng-click="delete(item)" title="Excluir"><i class="fa fa-trash-o"></i></button>
                    </td>
                </tr>
        	</tbody>
		</table>
		<div class="legenda" ng-show="labelList.length > 0">
			<label>Atenção:</label> Ao excluir um marcador que esteja sendo utilizado, ele será removido automaticamente de todas as mensagens, mas as mensagens não serão removidas e permanecerão sempre visíveis na pasta "Todas as Mensagens". 
		</div>
		<p class="info text-info text-center" ng-show="labelList.length == 0">Você não possui marcadores...</p>
	</div>
	<div id="messageTopicLabelsLoad"></div>
    <div class="panel-footer clearfix">
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newMessageLabelModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
</div>