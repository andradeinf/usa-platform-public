<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Trainings -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-video-camera"></i> <c:out value="${domainConfiguration.labels['TRAININGS']}">Treinamentos</c:out></h3>
</div>
<div class="panel panel-default">
	<div class="panel-heading clearfix" ng-show="trainings.length > pageNumber * pageSize" >
		<div class="pull-left">
			<button type="button" class="btn btn-default btn-sm" ng-click="showMoreTrainings()"><i class="fa fa-chevron-down"> Carregar mais</i></button>
		</div>
    </div>
	<div class="panel-list-group">
		<ul class="list-group">
			<li class="list-group-item" ng-repeat="item in trainings | orderBy:'order':true | limitTo: pageNumber * pageSize">
				<div class="row list-group-item-row">
		    		<div class="col-sm-5 list-group-item-column">
						<div class="videogular-container">
							<videogular vg-theme="video.config.theme.url" vg-complete="onVideoComplete(item)" vg-update-time="onVideoUpdate(item, $currentTime)">
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
							<small>{{item.updatedDate | date:'dd/MM/yyyy HH:mm'}}</small>
		                </h4>
						<p ta-bind="text" ng-model="item.description"></p>
					</div>
				</div>
			</li>
		</ul>
		<p ng-show="trainings.length == 0" class="text-info text-center margin-top-15">Não existem <c:out value="${domainConfiguration.labels['TRAININGS']}">Treinamentos</c:out> disponíveis...</p>
	</div>
	<div class="panel-footer clearfix" ng-show="trainings.length > pageNumber * pageSize" >
		<div class="pull-left">
			<button type="button" class="btn btn-default btn-sm" ng-click="showMoreTrainings()"><i class="fa fa-chevron-down"> Carregar mais</i></button>
		</div>
    </div>
</div>
<div id="trainings-loadingArea"></div>  
        