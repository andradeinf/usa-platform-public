angular.module('franchisorLogistics').controller('DeliverieRequestsByTimeRangeReportCtrl', function ($scope, $rootScope, httpSvc) {
		
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
		$scope.deliverieRequestsByTimeRangeReport = { 
			initDate: new Date(),
			endDate: new Date(),
			filterSupplierId: 0,
			filterFranchiseeId: 0
		}
						 	
		$scope.filter = {
				  fromDateOpened: false
			    , toDateOpened: false
			}
		$scope.deliverieRequestsByTimeRangeReport.initDate.setDate($scope.deliverieRequestsByTimeRangeReport.endDate.getDate() - 30);
		
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
		
		if ($scope.deliverieRequestsByTimeRangeReport.initDate =="" || $scope.deliverieRequestsByTimeRangeReport.endDate =="") {
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
					  data: $scope.deliverieRequestsByTimeRangeReport,
					  unique: true,
					  requestId: 'timeRangeReport-DeliveryRequest-create'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						$scope.deliverieRequestsByTimeRangeReports.push(data.timeRangeReport);
						startRefreshTimer(0, 3);

						$('#newReportModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Relatório solicitado com sucesso! Em breve o relatório será gerado.');
					} else if (data.returnMessage.code == -100008){
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + "\n" + data.returnMessage.details);
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
				$scope.deliverieRequestsByTimeRangeReports = data.timeRangeReports;
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
			  url: '/ws/suppliers/franchisor/'+gFranchisorId+'/hasStock',
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

		//franchisees
		$scope.franchisees = [];
		$scope.franchisees.push({"id":0,"name":"Carregando ..."})
		$scope.franchiseesMap = {};
		
		//get franchisees
		var franchiseesConfig = {
			  method: 'GET',
			  url: '/ws/franchisees/franchisor/'+gFranchisorId,
			  unique: false,
			  requestId: 'franchisees-load'
			};
		
		httpSvc(franchiseesConfig)
			.success(function(data){
				$scope.franchisees = data.franchisees;
				$scope.franchisees.unshift({"id":0,"name":" "+$rootScope.domainConfiguration.labels['FRANCHISEES_ALL']});

				$scope.franchisees.forEach(function(entry) {
					$scope.franchiseesMap[entry.id] = entry;
				});
			});
	}
	
	init();
	
});