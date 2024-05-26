angular.module('franchisorLogistics').config(AdminRouter);

function AdminRouter ($routeProvider) {
	$routeProvider
		.when('/home', {
			templateUrl: '/admin/franchisors',
			controller: 'FranchisorsCtrl',
			pageTrack: gSiteName+'/admin/franchisors'				
		})
		.when('/admin/franchisors/:franchisorId/users', {
			templateUrl: '/admin/franchisors/users',
			controller: 'FranchisorUsersCtrl',
			pageTrack: gSiteName+'/admin/franchisors/users'
		})
		.when('/admin/franchisors/:franchisorId/productCategories', {
			templateUrl: '/admin/franchisors/productCategories',
			controller: 'ProductCategoriesCtrl',
			pageTrack: gSiteName+'/admin/franchisors/productCategories'
		})
		.when('/admin/franchisors/:franchisorId/products', {
			templateUrl: '/admin/franchisors/products',
			controller: 'ProductsCtrl',
			pageTrack: gSiteName+'/admin/franchisors/products'
		})
		.when('/admin/franchisors/:franchisorId/franchisees', {
			templateUrl: '/admin/franchisees',
			controller: 'FranchiseesCtrl',
			pageTrack: gSiteName+'/admin/franchisees'
		})
		.when('/admin/franchisees/:franchiseeId/users', {
			templateUrl: '/admin/franchisees/users',
			controller: 'FranchiseeUsersCtrl',
			pageTrack: gSiteName+'/admin/franchisees/users'				
		})
		.when('/admin/suppliers', {
			templateUrl: '/admin/suppliers',
			controller: 'SuppliersCtrl',
			pageTrack: gSiteName+'/admin/suppliers'
		})
		.when('/admin/suppliers/categories', {
			templateUrl: '/admin/suppliers/categories',
			controller: 'SupplierCategoriesCtrl',
			pageTrack: gSiteName+'/admin/suppliers/categories'				
		})
		.when('/admin/suppliers/:supplierId/users', {
			templateUrl: '/admin/suppliers/users',
			controller: 'SupplierUsersCtrl',
			pageTrack: gSiteName+'/admin/suppliers/users'
		})
		.when('/admin/suppliers/:supplierId/franchisors', {
			templateUrl: '/admin/suppliers/franchisors',
			controller: 'SupplierFranchisorsCtrl',
			pageTrack: gSiteName+'/admin/suppliers/franchisors'
		})
		.when('/admin/suppliers/:supplierId/requests', {
			templateUrl: '/supplier/requests',
			controller: 'RequestsCtrl',
			pageTrack: gSiteName+'/admin/suppliers/requests'
		})
		.when('/admin/suppliers/:supplierId/stock', {
			templateUrl: '/supplier/stock',
			controller: 'StockCtrl',
			pageTrack: gSiteName+'/admin/suppliers/stock'
		})
		.when('/admin/users', {
			templateUrl: '/admin/users',
			controller: 'UsersCtrl',
			pageTrack: gSiteName+'/admin/users'
		})
		.when('/admin/users/:entityId/groups', {
			templateUrl: '/admin/users/groups',
			controller: 'UserGroupsCtrl',
			pageTrack: gSiteName+'/admin/users/groups'
		})
		.when('/admin/report/deliveriesBySupplierReport', {
			templateUrl: '/admin/deliveriesBySupplierReport',
			controller: 'DeliveriesBySupplierReportCtrl',
			pageTrack: gSiteName+'/admin/deliveriesBySupplierReport'
		})
		.when('/admin/tutorials', {
			templateUrl: '/admin/tutorials',
			controller: 'TutorialsCtrl',
			pageTrack: gSiteName+'/admin/tutorials'
		})
		.when('/admin/systemConfiguration', {
			templateUrl: '/admin/systemConfiguration',
			controller: 'SystemConfigurationCtrl',
			pageTrack: gSiteName+'/admin/systemConfiguration'
		})
		.when('/messages/:label', {
			templateUrl: '/messages',
			controller: 'MessagesCtrl',
			pageTrack: gSiteName+'/admin/messages'
		})
		.when('/messageLabels', {
			templateUrl: '/messageLabels',
			controller: 'MessageLabelsCtrl',
			pageTrack: gSiteName+'/admin/messageLabels'
		})
		.when('/error/:errorId', {
			templateUrl: '/error',
			controller: 'ErrorCtrl',
			pageTrack: gSiteName+'/admin/error'
		})
		.when('/error', {
			redirectTo: '/error/genericError'
		})
		.when('/configuration', {
			templateUrl: '/configuration',
			controller: 'ConfigurationCtrl',
			pageTrack: gSiteName+'/admin/configuration'
		})
		.otherwise({ redirectTo: '/home' });
}
