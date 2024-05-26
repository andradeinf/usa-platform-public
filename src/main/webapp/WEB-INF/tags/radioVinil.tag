<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="radioVinil" ng-controller="RadioVinilCtrl">
	<div class="panel panel-default" ng-show="radio.show" class="videogular-container">
		<div class="panel-body">
			<img class="img-responsive" src="/img/radio_vinil.png" alt="">
			<videogular vg-theme="radio.config.theme.url" class="videogular-container audio" vg-player-ready="onPlayerReady($API)">
				<vg-media vg-src="radio.streamSources" vg-type="audio" vg-preload="radio.config.preload"></vg-media>
				<vg-controls>
					<div class='iconButton vg-custom-play-stop-button' ng-click='clickCustomPlayStop()'><i class='fa' ng-class='customPlayStopClass()'></i></div>"
					<vg-volume>
						<vg-mute-button></vg-mute-button>
					</vg-volume>
				</vg-controls>
			</videogular>
		</div>
	</div>
</div>