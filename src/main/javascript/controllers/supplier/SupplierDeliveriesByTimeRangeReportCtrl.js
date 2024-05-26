angular.module('franchisorLogistics').controller('SupplierDeliveriesByTimeRangeReportCtrl', function ($scope, $rootScope, httpSvc) {
		
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
		$scope.supplierDeliveriesByTimeRangeReport = {
			initDate: new Date(),
			endDate: new Date(),
			filterFranchisorId: 0
		}
						 	
		$scope.filter = {
				  fromDateOpened: false
			    , toDateOpened: false
			}
		$scope.supplierDeliveriesByTimeRangeReport.initDate.setDate($scope.supplierDeliveriesByTimeRangeReport.endDate.getDate() - 30);
		
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
		
		if ($scope.supplierDeliveriesByTimeRangeReport.initDate =="" || $scope.supplierDeliveriesByTimeRangeReport.endDate =="") {
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
					  data: $scope.supplierDeliveriesByTimeRangeReport,
					  unique: true,
					  requestId: 'timeRangeReport-DeliveryRequest-create'
					};
					
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						$scope.supplierDeliveriesByTimeRangeReports.push(data.timeRangeReport);
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
				$scope.supplierDeliveriesByTimeRangeReports = data.timeRangeReports;
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
		
		//franchisors
		$scope.franchisors = [];
		$scope.franchisors.push({"id":0,"name":"Carregando ..."});
		$scope.franchisorsMap = {};
		
		//get franchisors
		var franchisorsConfig = {
			  method: 'GET',
			  url: '/ws/suppliers/'+gSupplierId+'/franchisors',
			  unique: false,
			  requestId: 'franchisors-load'
			};
		
		httpSvc(franchisorsConfig)
			.success(function(data){
				$scope.franchisors = data.franchisors;
				$scope.franchisors.unshift({"id":0,"name":" "+$rootScope.domainConfiguration.labels['SUPPLIER_FRANCHISORS_ALL']})
				
				$scope.franchisors.forEach(function(entry) {
					$scope.franchisorsMap[entry.id] = entry;
				});
			});

	}
	
	init();
	
});