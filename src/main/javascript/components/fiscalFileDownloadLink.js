angular.module('franchisorLogistics')
.component('fiscalFileDownloadLink', {
	bindings: {
		deliveryRequest: '<'
	},
	template: `
		<a href ng-href="/ws/delivery/fiscalFile/download/{{$ctrl.deliveryRequest.id}}">
			<file-icon content-type="$ctrl.deliveryRequest.fiscalFileContentType" /> {{$ctrl.deliveryRequest.fiscalFileName}}
		</a>
	`
})