angular.module('franchisorLogistics')
.component('statusLabel', {
	bindings: {
		status: '<',
		allowCancellation: '<',
		cancellationModalName: '@',
		onCancellationClick: '&'
	},
	transclude: true,
	controller: function() {
		this.$onInit = function() {
			this.className = 'label-warning';
	    	if (this.status == 'PENDING') {this.className = 'label-info';}
	    	if (this.status == 'COMPLETED') {this.className = 'label-success';}
	    	if (this.status == 'CANCELLED') {this.className = 'label-danger';}
	    	if (this.status == 'ADJUSTMENT') {this.className = 'label-danger';}
		};		
	},
	template: `
		<div class="clearfix">
			<span class="status-label label {{$ctrl.className}} pull-left" ng-transclude />
			<span ng-if="$ctrl.allowCancellation" class="pull-right"><button type="button" class="btn btn-default btn-xs" title="Cancelar pedido" data-toggle="modal" data-target="{{$ctrl.cancellationModalName}}" ng-click="$ctrl.onCancellationClick()">Cancelar</button></span>
		</div>
	`
})
