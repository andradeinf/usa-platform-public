angular.module('franchisorLogistics').config(SupplierRouter);

function SupplierRouter ($routeProvider) {

	if (gHasStockControl == 'true') {
		$routeProvider
			.when('/deliveryRequests', {
				templateUrl: '/supplier/deliveryRequests',
				controller: 'DeliveryRequestsCtrl',
				pageTrack: gSiteName+'/supplier/'+gSupplierId+'/deliveryRequests'
			})
			.when('/manufactureRequests', {
				templateUrl: '/supplier/manufactureRequests',
				controller: 'ManufactureRequestsCtrl',
				pageTrack: gSiteName+'/supplier/'+gSupplierId+'/manufactureRequests'
			})
			.when('/products', {
				templateUrl: '/supplier/products',
				controller: 'ProductsCtrl',
				pageTrack: gSiteName+'/supplier/'+gSupplierId+'/products'
			})
			.when('/stock', {
				templateUrl: '/supplier/stock',
				controller: 'StockCtrl',
				pageTrack: gSiteName+'/supplier/'+gSupplierId+'/stock'
			})
			.when('/deliveryReport', {
				templateUrl: '/supplier/deliveryReport',
				controller: 'DeliveryReportCtrl',
				pageTrack: gSiteName+'/supplier/'+gSupplierId+'/deliveryReport'
			})
			.when('/supplierDeliveriesByTimeRangeReport', {
				templateUrl: '/supplier/supplierDeliveriesByTimeRangeReport',
				controller: 'SupplierDeliveriesByTimeRangeReportCtrl',
				pageTrack: gSiteName+'/supplier/'+gSupplierId+'/supplierDeliveriesByTimeRangeReport'
			})
			.when('/accountsReceivableReport', {
				templateUrl: '/supplier/accountsReceivableReport',
				controller: 'AccountsReceivableReportCtrl',
				pageTrack: gSiteName+'/supplier/'+gSupplierId+'/accountsReceivableReport'
			})
			.when('/tutorials', {
				templateUrl: '/supplier/tutorials',
				controller: 'TutorialsCtrl',
				pageTrack: gSiteName+'/supplier/'+gSupplierId+'/tutorials'
			});
	}

	if (gReceiveMessage == 'true') {
		$routeProvider
			.when('/messages/:label', {
				templateUrl: '/messages',
				controller: 'MessagesCtrl',
				pageTrack: gSiteName+'/supplier/'+gSupplierId+'/messages'
			})
			.when('/messageLabels', {
				templateUrl: '/messageLabels',
				controller: 'MessageLabelsCtrl',
				pageTrack: gSiteName+'/supplier/'+gSupplierId+'/messageLabels'
			});
	}
		
	$routeProvider
		.when('/error/:errorId', {
			templateUrl: '/error',
			controller: 'ErrorCtrl',
			pageTrack: gSiteName+'/supplier/'+gSupplierId+'/error'
		})
		.when('/error', {
			redirectTo: '/error/genericError'
		})
		.when('/configuration', {
			templateUrl: '/configuration',
			controller: 'ConfigurationCtrl',
			pageTrack: gSiteName+'/supplier/'+gSupplierId+'/configuration'
		});
	
	if (gHasStockControl == 'true') {
		$routeProvider
			.otherwise({ redirectTo: '/deliveryRequests' });
	} else if (gReceiveMessage == 'true') {
		$routeProvider
			.otherwise({ redirectTo: '/messages/0' });	
	} else {
		$routeProvider
			.otherwise({ redirectTo: '/configuration' });
	}
}
