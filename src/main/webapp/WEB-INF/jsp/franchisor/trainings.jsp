<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Add Training Modal -->
<div class="modal fade" id="newTrainingModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Selecionar</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
					<input ng-model="training.id" type="hidden" value="0"/>
					<div class="form-group" id="title">
						<label for="inputTitle">Título</label>
						<input type="text" class="form-control" ng-model="training.title" placeholder="Título">
					</div>
					<div class="form-group" id="description">
						<label for="inputdescription">Descrição</label>
						<textarea id="inputdescription" class="form-control" ng-model="training.description" rows="3" placeholder="Informe uma descrição"></textarea>
					</div>
					<div ng-show="!videoUpload.selectVisible && training.videoId != 0" class="form-group" id="title">
						<label for="inputTitle">Vídeo</label>
						<div class="input-group">
							<input type="text" class="form-control" ng-model="training.videoName" readonly/>
							<span class="input-group-btn">
								<button class="btn btn-default" type="button" ng-click="replaceVideo()">Substituir</button>
							</span>
						</div>
					</div>
		            <div ng-show="videoUpload.selectVisible && !videoUpload.progressVisible" class="form-group" id="file">
		                <label for="inputFile">Selecione o Vídeo:</label>
						<div class="input-group">
							<input id="inputFile" type="file" class="form-control" file-model="file" onchange="angular.element(this).scope().doUploadVideo(this)" accept="video/mp4"/>
							<span ng-if="videoUpload.replace" class="input-group-btn">
								<button class="btn btn-default" type="button" ng-click="cancelReplaceVideo()">Cancelar</button>
							</span>
						</div>						
						<p class="help-block">Formato aceito: MP4</p>
						<span ng-if="videoUpload.error.length > 0" class="attachment-error">{{videoUpload.error}}</span>
		            </div>
		            <div ng-show="videoUpload.progressVisible" class="progress progress-u progress-xxs" id="progress">
		                <div class="progress-bar progress-bar-u" role="progressbar" aria-valuenow="{{videoUpload.progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{videoUpload.progress}}%">
		                </div>
		            </div>
		            <div class="form-group" id="restrictions">
		            	<label>Acesso:</label>
		            	<ul class="list-unstyled">
		                	<li><input type="radio" ng-model="training.accessRestricted" ng-value="false"> <c:out value="${domainConfiguration.labels['VISIBILITY_FRANCHISOR_FRANCHISEES']}">Franqueador e Franqueados</c:out></li>
		                	<li><input type="radio" ng-model="training.accessRestricted" ng-value="true"> <c:out value="${domainConfiguration.labels['VISIBILITY_ONLY_FRANCHISOR']}">Apenas Franqueador</c:out></li>
		                </ul>
		            </div>
		            <div class="form-group" id="restrictions" ng-hide="training.accessRestricted">
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
        		<button ng-click="save()" class="btn btn-default"><i class="fa fa-save"> Save</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- Trainings -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-video-camera"></i> <c:out value="${domainConfiguration.labels['TRAININGS']}">Treinamentos</c:out></h3>
</div>
<div class="panel panel-default">
	<div class="panel-heading clearfix">
		<div class="pull-left">
			<button ng-show="trainings.length > pageNumber * pageSize" type="button" class="btn btn-default btn-sm" ng-click="showMoreTrainings()"><i class="fa fa-chevron-down"> Carregar mais</i></button>
		</div>
    	<div class="pull-right" ng-hide="sortItems">
			<button class="btn btn-default" ng-click="enableSortItems()" ng-show="trainings.length > 1"><i class="fa fa-sort"> Reordenar</i></button>    	
			<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newTrainingModal"><i class="fa fa-plus"> Novo</i></button>    	
		</div>
		<div class="pull-right" ng-show="sortItems">
			<button class="btn btn-default" ng-click="cancelSortItems()"><i class="fa fa-times"> Cancelar</i></button>    	
			<button class="btn btn-default" ng-click="saveSortItems()"><i class="fa fa-save"> Salvar</i></button>    	
		</div>
    </div>
	<div class="panel-list-group">
		<ul class="list-group" ui-sortable="sortableOptions" ng-model="trainings">
			<li class="list-group-item" ng-repeat="item in trainings | limitTo: pageNumber * pageSize">
				<div class="row" ng-show="sortItems">
					<div class="col-sm-12">
						<div class="sortHandler">
							<a href class="btn btn-default btn-block" role="button" title="Segure e arraste para reordenar"><i class="fa fa-sort"></i></a>
						</div>
					</div>
				</div>
				<div class="row list-group-item-row">
		    		<div class="col-sm-5 list-group-item-column">
						<div class="videogular-container">
							<videogular vg-theme="video.config.theme.url">
								<vg-media vg-src="item.videoSources">
								</vg-media>
								<vg-controls class="videogular-controls" vg-autohide="video.config.plugins.controls.autoHide" vg-autohide-time="video.config.plugins.controls.autoHideTime">
									<vg-play-pause-button></vg-play-pause-button>
									<vg-time-display class="videogular-time-display">{{ currentTime | date:'mm:ss':'+0000' }}</vg-time-display>
									<vg-scrub-bar>
										<vg-scrub-bar-current-time></vg-scrub-bar-current-time>
										<vg-scrub-bar-buffer></vg-scrub-bar-buffer>
									</vg-scrub-bar>
									<vg-time-display class="videogular-time-display">{{ timeLeft | date:'mm:ss':'+0000' }}</vg-time-display>
									<vg-volume>
										<vg-mute-button></vg-mute-button>
										<vg-volume-bar></vg-volume-bar>
									</vg-volume>
									<vg-playback-button></vg-playback-button>
									<vg-fullscreen-button></vg-fullscreen-button>
								</vg-controls>
							</videogular>
						</div>		    			
					</div>
					<div class="col-sm-7 list-group-item-column clearfix">
						<h4 class="list-group-item-title">
							<strong>{{item.title}}</strong>
							<small>
								{{formatSize(item.videoSize)}} - {{item.updatedDate | date:'dd/MM/yyyy HH:mm'}}
								<span ng-if="item.accessRestricted"> - <b>Visível <c:out value="${domainConfiguration.labels['VISIBILITY_ONLY_FRANCHISOR']}">Apenas Franqueador</c:out></b></span>
								<span ng-if="!item.accessRestricted && item.franchiseeIds.length > 0"> - <b>Visível apenas para:</b><span ng-repeat="franchisee in franchisees | orderBy:'name'" ng-if="item.franchiseeIds.includes(franchisee.id)"> {{franchisee.name}};</span></span>
								<span ng-if="!item.accessRestricted && item.franchiseeIds.length == 0"> - <b>Visível para todos</b></span>
							</small>
		                </h4>
						<p ta-bind="text" ng-model="item.description"></p>
					</div>
				</div>
				<div class="list-group-item-actions" ng-hide="sortItems">
					<ul class="list-inline">
						<li><a href ng-click="edit(item)" data-toggle="modal" data-target="#newTrainingModal"><i class="fa fa-pencil"></i> Editar</a></li>
						<li><a href ng-click="delete(item)"><i class="fa fa-trash-o"></i> Excluir</a></li>
						<li><a href ng-click="toggleTrainingCheckView(item)"><i class="fa fa-list"></i> Controle de Visualização</a></li>
					</ul>
				</div>
				<div class="row announcement-read-control" ng-show="item.viewCheckOpen">
					<div class="col-sm-4" ng-repeat="franchiseeItem in item.franchisees | orderBy:'franchisee.name'">
						<h1 class="pointer-cursor" ng-class="{'franchisee-open': franchiseeItem.open}" ng-click="toggleCheckViewFranchisee(item, franchiseeItem)"><i class="fa" ng-class="{'fa-minus-square-o': franchiseeItem.open, 'fa-plus-square-o': !franchiseeItem.open}"></i> {{franchiseeItem.franchisee.name}}</h1>
						<ul class="read-control-list" ng-show="franchiseeItem.open">
							<li ng-show="franchiseeItem.loading"><span class="text-info"><i class="fa fa-spinner fa-spin"></i> Carregando...</span></li>
							<li ng-show="!franchiseeItem.loading && franchiseeItem.views.length == 0"><span class="text-danger">Nenhuma visualização até agora</span></li>
							 <li ng-repeat="view in franchiseeItem.views | orderBy:'name'"><i class="fa fa-video-camera" ng-class="{'fa-video-camera-completed': view.viewStatus == 'COMPLETED', 'fa-video-camera-partial': view.viewStatus == 'PARTIAL'}"></i> {{franchiseeItem.viewUsers[view.userId].name}}<span ng-show="view.viewStatus == 'PARTIAL'"> ({{formatTime(view.viewCurrentTime)}})</span></li>
						 </ul>
					</div>
				</div>
			</li>
		</ul>
		<p ng-show="trainings.length == 0" class="text-info text-center margin-top-15">Não existem <c:out value="${domainConfiguration.labels['TRAININGS']}">Treinamentos</c:out> disponíveis...</p>
	</div>
	<div class="panel-footer clearfix">
		<div class="pull-left">
			<button ng-show="trainings.length > pageNumber * pageSize" type="button" class="btn btn-default btn-sm" ng-click="showMoreTrainings()"><i class="fa fa-chevron-down"> Carregar mais</i></button>
		</div>
    	<div class="pull-right" ng-hide="sortItems">
			<button class="btn btn-default" ng-click="enableSortItems()" ng-show="trainings.length > 1"><i class="fa fa-sort"> Reordenar</i></button>    	
			<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newTrainingModal"><i class="fa fa-plus"> Novo</i></button>    	
		</div>
		<div class="pull-right" ng-show="sortItems">
			<button class="btn btn-default" ng-click="cancelSortItems()"><i class="fa fa-times"> Cancelar</i></button>    	
			<button class="btn btn-default" ng-click="saveSortItems()"><i class="fa fa-save"> Salvar</i></button>    	
		</div>
    </div>	
</div>
<div id="trainings-loadingArea"></div>  
<div class="row">
	<div class="col-sm-6">
		<label>Armazenamento utilizado:</label> {{formatSize(totalUsage)}} / {{formatSize(maxUsage)}}
	</div>
	<div class="col-sm-3 col-sm-offset-3">
		<div class="progress storage-usage-size">
			<div class="progress-bar" ng-class="{'progress-bar-success': totalUsagePercentage < 75, 'progress-bar-warning': totalUsagePercentage >= 75 && totalUsagePercentage < 90, 'progress-bar-danger': totalUsagePercentage >= 90}" role="progressbar" aria-valuenow="{{totalUsagePercentage}}" aria-valuemin="0" aria-valuemax="100" style="width: {{totalUsagePercentage}}%">
				{{totalUsagePercentage}}%
			</div>
		</div>
	</div>
</div>
