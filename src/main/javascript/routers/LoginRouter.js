angular.module('franchisorLogistics').config(LoginRouter);

function LoginRouter ($routeProvider) {
	$routeProvider
		.when('/home', {
			templateUrl: '/login/'+gLoginForm,
			controller: 'LoginFormCtrl',
			pageTrack: gSiteName+'/login/home'
		})
		.when('/homeCustom', {
			templateUrl: '/login/loginFormCustom',
			controller: 'LoginFormCtrl',
			pageTrack: gSiteName+'/login/loginFormCustom/'+gFranchisorId
		})
		.when('/profileSelection', {
			templateUrl: '/login/profileSelection',
			controller: 'ProfileSelectionCtrl',
			pageTrack: gSiteName+'/login/profileSelection'
		})
		.when('/resetPassword', {
			templateUrl: '/login/resetPasswordForm',
			controller: 'ResetPasswordFormCtrl',
			pageTrack: gSiteName+'/login/resetPasswordForm'
		})
		.when('/changePassword/:uid', {
			templateUrl: function(params){ return '/login/changePasswordForm/' + params.uid; },
			controller: 'ChangePasswordFormCtrl',
			pageTrack: gSiteName+'/login/changePasswordForm'
		})
		.otherwise({ redirectTo: '/home' });
}
