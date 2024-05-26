angular.module('franchisorLogistics').config(FranchisorRouter);

function FranchisorRouter ($routeProvider) {
	$routeProvider
		.when('/home', {
			templateUrl: '/franchisor/main',
			controller: 'MainCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/main'
		})
		.when('/products', {
			templateUrl: '/franchisor/products',
			controller: 'ProductsCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/products'
		})
		.when('/suppliers/:categoryId', {
			templateUrl: '/franchisor/suppliers',
			controller: 'SuppliersCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/suppliers'
		})
		.when('/myManufactureRequests', {
			templateUrl: '/franchisor/myManufactureRequests',
			controller: 'MyManufactureRequestsCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/myManufactureRequests'				
		})
		.when('/franchiseesDeliveryRequests', {
			templateUrl: '/franchisor/franchiseesDeliveryRequests',
			controller: 'FranchiseesDeliveryRequestsCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/franchiseesDeliveryRequests'
		})
		.when('/documents', {
			templateUrl: '/franchisor/documents',
			controller: 'DocumentsCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/documents'
		})
		.when('/tutorials', {
			templateUrl: '/franchisor/tutorials',
			controller: 'TutorialsCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/tutorials'
		})
		.when('/trainings', {
			templateUrl: '/franchisor/trainings',
			controller: 'TrainingsCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/trainings'
		})
		.when('/messages/:label', {
			templateUrl: '/messages',
			controller: 'MessagesCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/messages'
		})
		.when('/messageLabels', {
			templateUrl: '/messageLabels',
			controller: 'MessageLabelsCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/messageLabels'
		})
		.when('/error/:errorId', {
			templateUrl: '/error',
			controller: 'ErrorCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/error'
		})
		.when('/error', {
			redirectTo: '/error/genericError'
		})
		.when('/announcements', {
			templateUrl: '/franchisor/announcements',
			controller: 'AnnouncementsCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/announcements'
		})
		.when('/calendar', {
			templateUrl: '/franchisor/calendar',
			controller: 'CalendarCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/calendar'
		})
		.when('/deliverieRequestsByTimeRangeReport', {
			templateUrl: '/franchisor/deliverieRequestsByTimeRangeReport',
			controller: 'DeliverieRequestsByTimeRangeReportCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/deliverieRequestsByTimeRangeReport'
		})
		.when('/franchiseeMessagesByTimeRangeReport', {
			templateUrl: '/franchisor/franchiseeMessagesByTimeRangeReport',
			controller: 'FranchiseeMessagesByTimeRangeReportCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/franchiseeMessagesByTimeRangeReport'
		})
		.when('/configuration', {
			templateUrl: '/configuration',
			controller: 'ConfigurationCtrl',
			pageTrack: gSiteName+'/franchisor/'+gFranchisorId+'/configuration'
		})
		.otherwise({ redirectTo: '/home' });
}
