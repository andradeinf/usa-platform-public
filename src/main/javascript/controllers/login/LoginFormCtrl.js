angular.module('franchisorLogistics').controller('LoginFormCtrl', function ($scope, $window, $location, $anchorScroll, httpSvc) {
	
	var errorClass = 'has-error';
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#email').removeClass(errorClass);
		$('#password').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		
		if ($scope.login.email == "") {
			$('#email').addClass(errorClass);
			result = false;
		}
		
		if ($scope.login.password == "") {
			$('#password').addClass(errorClass);
			result = false;
		}
		
		return result;
	}
	
	var resetFranchisor = function() {
		$scope.franchisor = {
				name: "",
				segment: "",
				state: "",
				email: "",
				contact: "",
				comments: ""
		}
	}
	
	var resetFranchisorContactValidation = function() {
		$('#notificationArea').html("");
		$('#franchisorName').removeClass(errorClass);
		$('#franchisorSegment').removeClass(errorClass);
		$('#franchisorState').removeClass(errorClass);
		$('#franchisorEmail').removeClass(errorClass);
		$('#franchisorContact').removeClass(errorClass);
		$('#franchisorComments').removeClass(errorClass);
	}
	
	var validateFranchisorContact = function() {
		resetFranchisorContactValidation();
		var result = true;
		
		if ($scope.franchisor.name == "") {
			$('#franchisorName').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisor.segment == "") {
			$('#franchisorSegment').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisor.state == "") {
			$('#franchisorState').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisor.email == "") {
			$('#franchisorEmail').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisor.contact == "") {
			$('#franchisorContact').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisor.comments == "") {
			$('#franchisorComments').addClass(errorClass);
			result = false;
		}
		
		return result;
	}
	
	var resetSupplier = function() {
		$scope.supplier = {
				name: "",
				segment: "",
				state: "",
				email: "",
				contact: "",
				comments: ""
		}
	}
	
	var resetSupplierContactValidation = function() {
		$('#notificationArea').html("");
		$('#supplierName').removeClass(errorClass);
		$('#supplierSegment').removeClass(errorClass);
		$('#supplierState').removeClass(errorClass);
		$('#supplierEmail').removeClass(errorClass);
		$('#supplierContact').removeClass(errorClass);
		$('#supplierComments').removeClass(errorClass);
	}
	
	var validateSupplierContact = function() {
		resetSupplierContactValidation();
		var result = true;
		
		if ($scope.supplier.name == "") {
			$('#supplierName').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.segment == "") {
			$('#supplierSegment').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.state == "") {
			$('#supplierState').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.email == "") {
			$('#supplierEmail').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.contact == "") {
			$('#supplierContact').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.comments == "") {
			$('#supplierComments').addClass(errorClass);
			result = false;
		}
		
		return result;
	}
	
	var resetCompany = function() {
		$scope.company = {
				name: "",
				state: "",
				email: "",
				contact: "",
				comments: ""
		}
	}
	
	var resetCompanyContactValidation = function() {
		$('#notificationArea').html("");
		$('#companyName').removeClass(errorClass);
		$('#companyStateCity').removeClass(errorClass);
		$('#companyContact').removeClass(errorClass);
		$('#companyEmail').removeClass(errorClass);
		$('#companyComments').removeClass(errorClass);
	}
	
	var validateCompanyContact = function() {
		resetCompanyContactValidation();
		var result = true;
		
		if ($scope.company.name == "") {
			$('#companyName').addClass(errorClass);
			result = false;
		}
		
		if ($scope.company.state == "") {
			$('#companyStateCity').addClass(errorClass);
			result = false;
		}
		
		if ($scope.company.email == "") {
			$('#companyEmail').addClass(errorClass);
			result = false;
		}
		
		if ($scope.company.contact == "") {
			$('#companyContact').addClass(errorClass);
			result = false;
		}
		
		if ($scope.company.comments == "") {
			$('#companyComments').addClass(errorClass);
			result = false;
		}
		
		return result;
	}
	
	var resetNewFranchisee = function() {
		$scope.franchisee = {
				name: "",
				corporateName: "",
				fiscalId: "",
				address: "",
				contact: "",
				phone: "",
				email: "",
				deliveryDetails: "",
				comments: ""
		}
	}
	
	var resetNewFranchiseeValidation = function() {
		$('#notificationArea').html("");
		$('#franchiseeName').removeClass(errorClass);
		$('#franchiseeCorporateName').removeClass(errorClass);
		$('#franchiseeFiscalId').removeClass(errorClass);
		$('#franchiseeAddress').removeClass(errorClass);
		$('#franchiseeContactName').removeClass(errorClass);
		$('#franchiseeContactPhone').removeClass(errorClass);
		$('#franchiseeContactEmail').removeClass(errorClass);
		$('#franchiseeDeliveryDetails').removeClass(errorClass);
		$('#franchiseeComments').removeClass(errorClass);
	}
	
	var validateNewFranchisee = function() {
		resetNewFranchiseeValidation();
		var result = true;
		
		if ($scope.franchisee.name == "") {
			$('#franchiseeName').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisee.corporateName == "") {
			$('#franchiseeCorporateName').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisee.fiscalId == "") {
			$('#franchiseeFiscalId').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisee.address == "") {
			$('#franchiseeAddress').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisee.contact == "") {
			$('#franchiseeContactName').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisee.phone == "") {
			$('#franchiseeContactPhone').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisee.email == "") {
			$('#franchiseeContactEmail').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisee.deliveryDetails == "") {
			$('#franchiseeDeliveryDetails').addClass(errorClass);
			result = false;
		}
		
		if ($scope.franchisee.comments == "") {
			$('#franchiseeComments').addClass(errorClass);
			result = false;
		}
		
		return result;
	}
	
	var resetNewSupplier = function() {
		$scope.supplier = {
				name: "",
				fiscalId: "",
				address: "",
				contact: "",
				phone: "",
				email: "",
				comments: ""
		}
	}
	
	var resetNewSupplierValidation = function() {
		$('#notificationArea').html("");
		$('#supplierName').removeClass(errorClass);
		$('#supplierFiscalId').removeClass(errorClass);
		$('#supplierAddress').removeClass(errorClass);
		$('#supplierContactName').removeClass(errorClass);
		$('#supplierContactPhone').removeClass(errorClass);
		$('#supplierContactEmail').removeClass(errorClass);
		$('#supplierComments').removeClass(errorClass);
	}
	
	var validateNewSupplier = function() {
		resetNewSupplierValidation();
		var result = true;
		
		if ($scope.supplier.name == "") {
			$('#supplierName').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.fiscalId == "") {
			$('#supplierFiscalId').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.address == "") {
			$('#supplierAddress').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.contact == "") {
			$('#supplierContactName').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.phone == "") {
			$('#supplierContactPhone').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.email == "") {
			$('#supplierContactEmail').addClass(errorClass);
			result = false;
		}
		
		if ($scope.supplier.comments == "") {
			$('#supplierComments').addClass(errorClass);
			result = false;
		}
		
		return result;
	}
	
	$scope.doLogin = function() {
		if (validate()) {
			
			var config = {
					  method: 'POST',
					  url: '/ws/login',
					  data: $scope.login,
					  unique: true,
					  requestId: 'login-doLogin'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if (data.returnMessage.code == 100002) {
							$window.location.href = '#/profileSelection';
						} else {
							$window.location.href = '/';
						}						
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problemas ao realizar o login.');
					}
				});				
		};
	}
	
	$scope.sendFranchisor = function() {
		
		if (validateFranchisorContact()) {
		
			var config = {
					  method: 'POST',
					  url: '/ws/contact/franchisor',
					  data: $scope.franchisor,
					  unique: true,
					  requestId: 'franchisorContact-send'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Obrigado pelo seu contato! Em breve lhe daremos um retorno.', 0);
						resetFranchisor();
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problemas ao enviar os dados.');
					}
				});
		}
	}
	
	$scope.sendSupplier = function() {
		
		if (validateSupplierContact()) {
			var config = {
					  method: 'POST',
					  url: '/ws/contact/supplier',
					  data: $scope.supplier,
					  unique: true,
					  requestId: 'supplierContact-send'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Obrigado pelo seu contato! Em breve lhe daremos um retorno.', 0);
						resetSupplier();
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problemas ao enviar os dados.');
					}
				});	
		}
	}
	
	$scope.sendCompany = function() {
		
		if (validateCompanyContact()) {
			var config = {
					  method: 'POST',
					  url: '/ws/contact/company',
					  data: $scope.company,
					  unique: true,
					  requestId: 'companyContact-send'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Obrigado pelo seu contato! Em breve lhe daremos um retorno.', 0);
						resetCompany();
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problemas ao enviar os dados.');
					}
				});	
		}
	}
	
	$scope.sendNewFranchisee = function() {
		
		if (validateNewFranchisee()) {
			var config = {
					  method: 'POST',
					  url: '/ws/contact/franchisee',
					  data: $scope.franchisee,
					  unique: true,
					  requestId: 'franchisee-send'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Obrigado pelo seu contato! Em breve lhe daremos um retorno.', 0);
						resetNewFranchisee();
						$scope.toggleFranchiseeContact();
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problemas ao enviar os dados.');
					}
				});	
		}
	}
	
	$scope.sendNewSupplier = function() {
		
		if (validateNewSupplier()) {
			var config = {
					  method: 'POST',
					  url: '/ws/contact/supplier',
					  data: $scope.supplier,
					  unique: true,
					  requestId: 'supplier-send'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Obrigado pelo seu contato! Em breve lhe daremos um retorno.', 0);
						resetNewSupplier();
						$scope.toggleSupplierContact();
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problemas ao enviar os dados.');
					}
				});	
		}
	}
    
    $scope.gotoLocation = function(location) {
        $location.hash(location);
        $anchorScroll();
      };
      
    $scope.toggleLogin = function() {
    	$scope.isLogin = !$scope.isLogin;
    };
    
    $scope.toggleFranchiseeContact = function() {
    	$scope.isFranchiseeContact = !$scope.isFranchiseeContact;
    };
    
    $scope.toggleSupplierContact = function() {
    	$scope.isSupplierContact = !$scope.isSupplierContact;
    };
	
	var init = function() {
		
		$scope.isLogin = false;
		$scope.isFranchiseeContact = false;
		$scope.isSupplierContact = false;
			
		$scope.login = { email: ""
		               , password: ""
				 }
		
		resetFranchisor();
		resetSupplier();
		resetCompany();
		resetNewFranchisee();
		resetNewSupplier();
		
		if (gFranchisorId) {
			$scope.franchisor = {
					id: gFranchisorId,
					imageURL: gFranchisorImage
				}
		}
		
		if ($location.hash() != "") {
			$scope.gotoLocation($location.hash());
		}
		
	}	
	
	init();
});