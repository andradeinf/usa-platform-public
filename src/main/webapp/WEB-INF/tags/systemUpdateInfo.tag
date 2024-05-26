<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="systemUpdateInfo" ng-controller="SystemUpdateInfoCtrl">
	<div class="panel panel-danger" ng-show="systemUpdateInfo.summary != null">
		<div class="panel-heading">
			<h3 class="panel-title">Atualização</h3>
		</div>
		<div class="panel-body">
			<span ng-show="systemUpdateInfo.summary.type=='STRING'">{{systemUpdateInfo.summary.stringValue}}</span>
			<span ng-show="systemUpdateInfo.summary.type=='HTML_STRING'">
				<span ta-bind="text" ng-model="systemUpdateInfo.summary.stringValue"></span>
			</span>
			<span ng-show="systemUpdateInfo.details != null">
				<a data-toggle="modal" data-target="#systemUpdateModal" href="" >Mais detalhes</a>
			</span>
		</div>
	</div>
		            
    <!-- System Update Modal -->
	<div class="modal fade" id="systemUpdateModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
		    <div class="modal-content">
		        <div class="modal-header">
		            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		            <h4 class="modal-title" id="myModalLabel">Atualizações</h4>
		        </div>
		        <div class="modal-body">
					<span ng-show="systemUpdateInfo.details.type=='STRING'">{{systemUpdateInfo.details.stringValue}}</span>
            		<span ng-show="systemUpdateInfo.details.type=='HTML_STRING'">
            			<span ta-bind="text" ng-model="systemUpdateInfo.details.stringValue"></span>
            		</span>
		        </div>
		    </div>
		</div>
	</div><!--/end modal-->
</div>