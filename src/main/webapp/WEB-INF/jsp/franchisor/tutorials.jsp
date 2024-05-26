<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Tutorials -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-youtube-play"></i> Tutoriais</h3>
</div>
<div class="panel panel-default">
	<div class="panel-list-group">
		<ul class="list-group">
			<li class="list-group-item" ng-repeat="item in tutorials | orderBy:'order'">
				<div class="row list-group-item-row">
		    		<div class="col-sm-6 list-group-item-column">
		    			<div class="embed-responsive embed-responsive-16by9">
		    				<youtube-video video-id="item.url"></youtube-video>
						</div>
					</div>
					<div class="col-sm-6 list-group-item-column clearfix">
						<h4 class="list-group-item-title">
		                    <strong>{{item.name}}</strong>
		                </h4>
		                <p ta-bind="text" ng-model="item.description"></p>
					</div>
				</div>
			</li>
		</ul>
	</div>
	<p ng-show="tutorials.length == 0" class="text-info text-center margin-top-15">Não existem tutoriais disponíveis...</p>
</div>
<div id="tutorials-loadingArea"></div>           