<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row">
    <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
    	<div class="user-profile">
	        <p class="user-profile-title">Clique em um dos perfis abaixo para entrar:</p>
	        <ul class="user-profile-list">
	        	<li class="user-profile-list-item" ng-repeat="item in userProfileList | orderBy:['profileType', 'name']">
	        		<div class="row" ng-click="selectProfile(item)">
	        			<div class="col-sm-3">
	        				<img class="img-responsive" ng-src="{{item.imageURL}}" alt="" />
	        			</div>
	        			<div class="col-sm-6 clearfix">
	        				<p>{{item.name}}</p>
	        			</div>
	        		</div>     		
	        	</li>
	        </ul>
		</div>     
    </div>
    <!-- Loading -->
    <div id="list-overlay" class="overlay"></div>
    <div id="list-loading-img" class="loading-img"></div>
    <!-- end loading -->
</div><!--/row-->