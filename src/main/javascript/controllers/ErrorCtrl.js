angular.module('franchisorLogistics').controller('ErrorCtrl', function ($scope, $routeParams) {	
	
	var init = function() {	
		
		$scope.errorMessage = "Desculpe. Algo inesperado aconteceu."

		if ($routeParams.errorId == "fileNotFound") {
			$scope.errorMessage = "Desculpe. O arquivo selecionado não foi encontrado."
		}

		if ($routeParams.errorId == "notAuthorized") {
			$scope.errorMessage = "Desculpe. Você não está autorizado a acessar essa página."
		}
	
	}
	
	init();
	
});
