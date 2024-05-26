angular.module('franchisorLogistics')
.component('deliveryRequestHistoryStatus', {
	bindings: {
		deliveryRequest: '<',
		allowCancellation: '<',
		cancellationModalName: '@',
		onCancellationClick: '&',
		showHistoryButton: '<',
		historyModalName: '@',
		onHistoryClick: '&',
	},
	controller: function() {
		this.$onInit = function() {
			this.allowCancellation = this.allowCancellation || false;
			this.showHistoryButton = this.showHistoryButton || false;
		};
		this.processOnCancellationClick = function() {
			this.onCancellationClick(this.deliveryRequest);
		}
		this.processOnHistoryClick = function() {
			this.onHistoryClick(this.deliveryRequest);
		}
	},
	template: `
		<status-label status="$ctrl.deliveryRequest.status" allow-cancellation="$ctrl.allowCancellation" cancellation-modal-name="{{$ctrl.cancellationModalName}}" on-cancellation-click="$ctrl.processOnCancellationClick()">{{$ctrl.deliveryRequest.statusDescription}}</status-label>
		<ul class="delivery-info">
    		<li ng-show="$ctrl.deliveryRequest.status == 'PENDING_WITH_RESTRICTION' || ($ctrl.deliveryRequest.status == 'CANCELLED' && $ctrl.deliveryRequest.autoCancellationDate)"><small><strong>Cancel. Autom. em: </strong>{{$ctrl.deliveryRequest.autoCancellationDate | date:'dd/MM/yyyy'}}</small></li>
    		<li ng-show="$ctrl.deliveryRequest.sentDate"><small><strong>Enviado em: </strong>{{$ctrl.deliveryRequest.sentDate | date:'dd/MM/yyyy'}}</small></li>
    		<li ng-show="$ctrl.deliveryRequest.deadlineDate"><small><strong>Entrega até: </strong>{{$ctrl.deliveryRequest.deadlineDate | date:'dd/MM/yyyy'}}</small></li>
    		<li ng-show="$ctrl.deliveryRequest.carrierName"><small><strong>Transportadora: </strong>{{$ctrl.deliveryRequest.carrierName}}</small></li>
    		<li ng-show="$ctrl.deliveryRequest.trackingCode"><small><strong>Rastreamento: </strong>{{$ctrl.deliveryRequest.trackingCode}}</small></li>
    		<li ng-show="$ctrl.deliveryRequest.fiscalNumber"><small><strong>Número da Nota Fiscal: </strong>{{$ctrl.deliveryRequest.fiscalNumber}}</small></li>
    		<li ng-show="$ctrl.deliveryRequest.fiscalFileKey"><small><strong>Nota Fiscal: </strong><fiscal-file-download-link delivery-request="$ctrl.deliveryRequest" /></small></li>
    		<li ng-show="$ctrl.deliveryRequest.dueDate"><small><strong>Data Vcto do Boleto: </strong>{{$ctrl.deliveryRequest.dueDate | date:'dd/MM/yyyy'}}</small></li>
    		<li ng-show="$ctrl.deliveryRequest.paymentSlipKey"><small><strong>Boleto: </strong><payment-slip-download-link delivery-request="$ctrl.deliveryRequest" /></small></li>
    	</ul>
		<div class="margin-top-5 text-danger" ng-show="$ctrl.deliveryRequest.status == 'CANCELLED'"><small>{{$ctrl.deliveryRequest.cancellationComment}}</small></div>
    	<div class="text-info" ng-if="$ctrl.showHistoryButton"><small><a href data-toggle="modal" data-target="{{$ctrl.historyModalName}}" ng-click="$ctrl.processOnHistoryClick()">Histórico de Status</a></small></div>
	`
})