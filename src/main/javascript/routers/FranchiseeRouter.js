angular.module('franchisorLogistics').config(FranchiseeRouter);

function FranchiseeRouter ($routeProvider) {
	$routeProvider
		.when('/home', {
			templateUrl: '/franchisee/main',
			controller: 'MainCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/main'
		})
		.when('/products', {
			templateUrl: '/franchisee/products',
			controller: 'ProductsCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/products'
		})
		.when('/suppliers/:categoryId', {
			templateUrl: '/franchisee/suppliers',
			controller: 'SuppliersCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/suppliers'
		})
		.when('/myDeliveryRequests', {
			templateUrl: '/franchisee/myDeliveryRequests',
			controller: 'MyDeliveryRequestsCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/myDeliveryRequests'
		})
		.when('/accountsPayableReport', {
				templateUrl: '/franchisee/accountsPayableReport',
				controller: 'AccountsPayableReportCtrl',
				pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/accountsPayableReport'
			})
		.when('/documents', {
			templateUrl: '/franchisee/documents',
			controller: 'DocumentsCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/documents'
		})
		.when('/tutorials', {
			templateUrl: '/franchisee/tutorials',
			controller: 'TutorialsCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/tutorials'
		})
		.when('/trainings', {
			templateUrl: '/franchisee/trainings',
			controller: 'TrainingsCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/trainings'
		})
		.when('/messages/:label', {
			templateUrl: '/messages',
			controller: 'MessagesCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/messages'
		})
		.when('/messageLabels', {
			templateUrl: '/messageLabels',
			controller: 'MessageLabelsCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/messageLabels'
		})
		.when('/error/:errorId', {
			templateUrl: '/error',
			controller: 'ErrorCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/error'
		})
		.when('/error', {
			redirectTo: '/error/genericError'
		})
		.when('/announcements', {
			templateUrl: '/franchisee/announcements',
			controller: 'AnnouncementsCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/announcements'
		})
		.when('/calendar', {
			templateUrl: '/franchisee/calendar',
			controller: 'CalendarCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/calendar'
		})
		.when('/franchiseeDeliveriesByTimeRangeReport', {
			templateUrl: '/franchisee/franchiseeDeliveriesByTimeRangeReport',
			controller: 'FranchiseeDeliveriesByTimeRangeReportCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/franchiseeDeliveriesByTimeRangeReport'
		})
		.when('/configuration', {
			templateUrl: '/configuration',
			controller: 'ConfigurationCtrl',
			pageTrack: gSiteName+'/franchisee/'+gFranchiseeId+'/configuration'
		})
		.otherwise({ redirectTo: '/home' });
}
