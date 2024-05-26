angular.module('franchisorLogistics')
.component('paymentSlipDownloadLink', {
	bindings: {
		deliveryRequest: '<'
	},
	template: `
		<a href ng-href="/ws/delivery/paymentSlip/download/{{$ctrl.deliveryRequest.id}}">
			<file-icon content-type="$ctrl.deliveryRequest.paymentSlipContentType" /> {{$ctrl.deliveryRequest.paymentSlipName}}
		</a>
	`
})