angular.module('franchisorLogistics').controller('DeliveriesBySupplierReportCtrl', function ($scope, $rootScope, httpSvc) {
		
	var errorClass = 'has-error';

	$scope.openFromDatePicker = function() {
	    $scope.filter.fromDateOpened = true;
	};
	
	$scope.openToDatePicker = function() {
	    $scope.filter.toDateOpened = true;
	};
	
	$scope.new = function(){
		reset();
	}
	
	var reset = function() {
		$scope.deliveriesBySupplierReport = { 
			initDate: new Date(), 
			endDate: new Date(),
			filterSupplierId: 0,
			filterFranchisorId: 0			
		}

		$scope.filter = {
			fromDateOpened: false
		  , toDateOpened: false
	  	}
  		$scope.deliveriesBySupplierReport.initDate.setDate($scope.deliveriesBySupplierReport.endDate.getDate() - 30);
						 	
		//clear validation
		resetValidation();			 	
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.deliveriesBySupplierReport.initDate =="" || $scope.deliveriesBySupplierReport.endDate =="") {
			$('#time').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Período' não contém um período válido</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	$scope.save = function(){
		if (validate()) {
			
			var config = {
					  method: 'POST',
					  url: '/ws/report/timeRangeReport/DELIVERY_REQUEST',
					  data: $scope.deliveriesBySupplierReport,
					  unique: true,
					  requestId: 'timeRangeReport-DeliveryRequest-create'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						$scope.deliveriesBySupplierReportList.push(data.timeRangeReport);
						startRefreshTimer(0, 3);

						$('#newReportModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Relatório solicitado com sucesso! Em breve o relatório será gerado.');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao gravar a solicitação de relatório!\n'+data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao gravar a solicitação de relatório!');
					}
				});				
		};
	}
	
	var startRefreshTimer = function(counter, timer) {
		if(counter < 5){
			setTimeout(function(){
				refreshList();

				counter++;
				timer = timer * 2;
				startRefreshTimer(counter, timer);
			}, timer * 1000);
		}
	}

	var refreshList = function() {
		var config = {
			method: 'GET' ,
			url: '/ws/report/timeRangeReport/DELIVERY_REQUEST',
			unique: false,
			requestId: 'timeRangeReport-DeliveryRequest-load'
		  };
  
	  	httpSvc(config)
			.success(function(data){
				$scope.deliveriesBySupplierReportList = data.timeRangeReports;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
	}
	
	var init = function() {

		$scope.dateOptions = {
			formatYear: 'yy',
			startingDay: 1
		};
		
		$scope.formats = ['dd/MM/yyyy'];
		
		refreshList();
		
		//suppliers
		$scope.suppliers = [];
		$scope.suppliers.push({"id":0,"name":"Carregando ..."});
		$scope.suppliersMap = {};
		
		//get suppliers
		var supplierConfig = {
			  method: 'GET',
			  url: '/ws/suppliers',
			  unique: false,
			  requestId: 'suppliers-load'
			};
		
		httpSvc(supplierConfig)
			.success(function(data){
				$scope.suppliers = data.suppliers;
				$scope.suppliers.unshift({"id":0,"name":" "+$rootScope.domainConfiguration.labels['SUPPLIERS_ALL']})
				
				$scope.suppliers.forEach(function(entry) {
					$scope.suppliersMap[entry.id] = entry;
				});
			});
		
		//franchisors
		$scope.franchisors = [];
		$scope.franchisors.push({"id":0,"name":"Carregando ..."});
		$scope.franchisorsMap = {};
		
		//get franchisors
		var franchisorsConfig = {
			  method: 'GET',
			  url: '/ws/franchisors',
			  unique: false,
			  requestId: 'franchisors-load'
			};
		
		httpSvc(franchisorsConfig)
			.success(function(data){
				$scope.franchisors = data;
				$scope.franchisors.unshift({"id":0,"name":" "+$rootScope.domainConfiguration.labels['SUPPLIER_FRANCHISORS_ALL']})
				
				$scope.franchisors.forEach(function(entry) {
					$scope.franchisorsMap[entry.id] = entry;
				});
			});

	}
	
	init();
	
});