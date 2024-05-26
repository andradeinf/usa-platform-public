angular.module('franchisorLogistics').controller('DeliveryRequestsCtrl', function ($scope, $routeParams, $window, httpSvc) {
    
	var errorClass = 'has-error';
	var UNSELECTED_FRANCHISEE_ID = 1234567890;
	
	$scope.franchiseeSelected = function(){
    	return $scope.franchiseeId != UNSELECTED_FRANCHISEE_ID;
    }
	
	$scope.selectAll = function(){
		$scope.deliveryRequests.forEach(function(item, index, array) {
			if (item.franchiseeId == $scope.franchiseeId) {
				item.updateCheck = $scope.updateAllCheck;
			}    		
		});
    }
	
	$scope.selectFranchisee = function(){
		$scope.updateAllCheck = false;
		$scope.deliveryRequests.forEach(function(item, index, array) {
			item.updateCheck = false;
		});
    }
    
    var initializeUpdatableFields = function(item) {
	    if (item.product.hasDeliveryUnit) {
	    	item.deliveryQty = item.quantity / item.productSize.deliveryQty;
    	} else {
    		item.deliveryQty = item.quantity;
    	}
	    item.updateStatusRequest.deliveryUnitPrice = item.deliveryUnitPrice;
	}
    
    var resetRequestPaymentSlip = function() {
    	$scope.updateRequestPaymentSlip = {
    			name: "",
    			progress: 0,
    			progressVisible: false,
    			error: "",
    			id: 0
        	}
    	
    	document.getElementById("inputPaymentSlipFile").value = "";
    }
    
    var resetRequestFiscalFile = function() {
    	$scope.updateRequestFiscalFile = {
    			name: "",
    			progress: 0,
    			progressVisible: false,
    			error: "",
    			id: 0
        	}
    	
    	document.getElementById("inputFiscalFile").value = "";
    }
	
	$scope.updateDeliveryRequests = function(){
		
		var selected = false;
		$scope.deliveryRequests.forEach(function(item, index, array) {
			if (item.updateCheck) {
				selected = true;
			}
		});
		if (!selected) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Selecione ao menos um pedido para atualizar');
			return;
		}
		
		//clear validation
		resetValidation();		
		
    	$scope.updateRequest = {
			  status: $scope.deliveryStatus
	        , cancellationComment: ''
	        , sentDate: ''
	        , deadlineDate: ''
	        , carrierName: ''
	        , trackingCode: ''
	        , fiscalNumber: ''
	        , dueDate: ''
	        , autoCancellationDate: ''
	        , deliveryRequests: []
			, paymentSlipId: 0
			, fiscalFileId: 0
		}
    	
    	resetRequestPaymentSlip();
    	resetRequestFiscalFile();
    	
    	$scope.updateProcessed = false;
    	$scope.updateRequestItems = [];		
    	$scope.deliveryRequests.forEach(function(item, index, array) {
    		if (item.updateCheck) {
    			initializeUpdatableFields(item);
    			$scope.updateRequestItems.push(item);
    		}
		});
    	
    	$('#updateDeliveryRequestsModal').modal('show');
    }
	
	$scope.deletePaymentSlip = function() {
		$scope.updateRequest.paymentSlipId = 0;
		resetRequestPaymentSlip();
	}
	
	$scope.deleteFiscalFile = function() {
		$scope.updateRequest.fiscalFileId = 0;
		resetRequestFiscalFile();
	}
	
	$scope.doAddPaymentSlip = function(e) {
		
		if (e.files.length > 0) {
			
			$scope.updateRequestPaymentSlip.name = e.files[0].name;
			$scope.updateRequestPaymentSlip.progress = 0;
			$scope.updateRequestPaymentSlip.progressVisible= true;
			$scope.updateRequestPaymentSlip.error = "";
			$scope.updateRequestPaymentSlip.id = 0;
			
			var config = {
			  method: 'GET' ,
			  url: '/ws/delivery/paymentSlipUpload/url',
			  unique: false,
			  requestId: 'paymentSlipUpload-getUploadUrl'
			};
			
			httpSvc(config)
				.success(function(data){
					var fd = new FormData();
					fd.append("uploadedFile", $scope.paymentSlipFile);
					
					var xhr = new XMLHttpRequest();
					xhr.upload.addEventListener("progress", function (evt) {paymentSlipUploadProgress(evt);}, false);
			        xhr.addEventListener(       "load",     function (evt) {paymentSlipUploadComplete(evt);}, false);
			        xhr.addEventListener(       "error",    function (evt) {paymentSlipUploadFailed(evt);}, false);
			        xhr.addEventListener(       "abort",    function (evt) {paymentSlipUploadCanceled(evt);}, false);
			        xhr.open("POST", data.url);
			        xhr.setRequestHeader('Accept', 'application/json');
			        xhr.send(fd);
				});
		}
	}
	
	var paymentSlipUploadProgress = function(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
            	$scope.updateRequestPaymentSlip.progress = Math.round(evt.loaded * 100 / evt.total)
            } else {
            	$scope.updateRequestPaymentSlip.progress = 'unable to compute'
            }
        });
    }
	
	var paymentSlipUploadComplete = function(evt) {
    	$scope.$apply(function(){
    		$scope.updateRequestPaymentSlip.progressVisible = false;
    		if (evt.target.status == 413) {
    			$scope.updateRequestPaymentSlip.error = "Tamanho máximo (50 MB) excedido";
    		} else {
    			var data = JSON.parse(evt.target.response);
    			if (data.returnMessage.code > 0) {
    				$scope.updateRequestPaymentSlip.id = data.uploadedFile.id;
    				$scope.updateRequest.paymentSlipId = data.uploadedFile.id;
    			} else {
    				$scope.updateRequestPaymentSlip.error = data.returnMessage.message;
    			}
    		}	    		
        });
    }
	
	var paymentSlipUploadFailed = function(evt) {
		$scope.$apply(function(){
			$scope.updateRequestPaymentSlip.progressVisible = false;
			$scope.updateRequestPaymentSlip.error = "Erro ao carregar o arquivo";
        });
    }
	
	var paymentSlipUploadCanceled = function(evt) {
		$scope.$apply(function(){
			$scope.updateRequestPaymentSlip.progressVisible = false;
			$scope.updateRequestPaymentSlip.error = "Upload cancelado";
        });
    }
	
	$scope.doAddFiscalFile = function(e) {
		
		if (e.files.length > 0) {
			
			$scope.updateRequestFiscalFile.name = e.files[0].name;
			$scope.updateRequestFiscalFile.progress = 0;
			$scope.updateRequestFiscalFile.progressVisible= true;
			$scope.updateRequestFiscalFile.error = "";
			$scope.updateRequestFiscalFile.id = 0;
			
			var config = {
			  method: 'GET' ,
			  url: '/ws/delivery/fiscalFileUpload/url',
			  unique: false,
			  requestId: 'fiscalFileUpload-getUploadUrl'
			};
			
			httpSvc(config)
				.success(function(data){
					var fd = new FormData();
					fd.append("uploadedFile", $scope.fiscalFile);
					
					var xhr = new XMLHttpRequest();
					xhr.upload.addEventListener("progress", function (evt) {fiscalFileUploadProgress(evt);}, false);
			        xhr.addEventListener(       "load",     function (evt) {fiscalFileUploadComplete(evt);}, false);
			        xhr.addEventListener(       "error",    function (evt) {fiscalFileUploadFailed(evt);}, false);
			        xhr.addEventListener(       "abort",    function (evt) {fiscalFileUploadCanceled(evt);}, false);
			        xhr.open("POST", data.url);
			        xhr.setRequestHeader('Accept', 'application/json');
			        xhr.send(fd);
				});
		}
	}
	
	var fiscalFileUploadProgress = function(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
            	$scope.updateRequestFiscalFile.progress = Math.round(evt.loaded * 100 / evt.total)
            } else {
            	$scope.updateRequestFiscalFile.progress = 'unable to compute'
            }
        });
    }
	
	var fiscalFileUploadComplete = function(evt) {
    	$scope.$apply(function(){
    		$scope.updateRequestFiscalFile.progressVisible = false;
    		if (evt.target.status == 413) {
    			$scope.updateRequestFiscalFile.error = "Tamanho máximo (50 MB) excedido";
    		} else {
    			var data = JSON.parse(evt.target.response);
    			if (data.returnMessage.code > 0) {
    				$scope.updateRequestFiscalFile.id = data.uploadedFile.id;
    				$scope.updateRequest.fiscalFileId = data.uploadedFile.id;
    			} else {
    				$scope.updateRequestFiscalFile.error = data.returnMessage.message;
    			}
    		}	    		
        });
    }
	
	var fiscalFileUploadFailed = function(evt) {
		$scope.$apply(function(){
			$scope.updateRequestFiscalFile.progressVisible = false;
			$scope.updateRequestFiscalFile.error = "Erro ao carregar o arquivo";
        });
    }
	
	var fiscalFileUploadCanceled = function(evt) {
		$scope.$apply(function(){
			$scope.updateRequestFiscalFile.progressVisible = false;
			$scope.updateRequestFiscalFile.error = "Upload cancelado";
        });
    }
	
	$scope.saveEditDelivery = function(){
		
		if (validate()) {
		
			$scope.updateProcessed = true;
			LoadingUtil.show($('#loadingModalArea'));
			
			var updateRequestItemsMap = {};
			$scope.updateRequestItems.forEach(function(item, index, array) {
				item.updateStatusRequest.quantity = item.calculateTotalQuantity();
				$scope.updateRequest.deliveryRequests.push(item.updateStatusRequest);
				updateRequestItemsMap[item.id] = item;
			});
			
			var config = {
		  			  method: 'PUT',
		  			  url: '/ws/delivery/status',
		  			  data: $scope.updateRequest,
		  			  unique: true,
		  			  requestId: 'delivery-statusUpdate'
		  			};
			
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						data.deliveryRequests.forEach(function(item, index, array) {
							var deliveryRequest =  updateRequestItemsMap[item.id];
							var index = $scope.deliveryRequests.indexOf(deliveryRequest);
							if (item.status == deliveryRequest.status) {
								//no status update, only data (Sent information)
								initializeDelivery(item);					
								$scope.deliveryRequests[index] = item;
							} else {
								$scope.deliveryRequests.splice(index, 1);
							}	
						});
						data.newDeliveryRequests.forEach(function(item, index, array) {
							initializeDelivery(item);
							$scope.deliveryRequests.push(item);
						});
						
						$scope.updateAllCheck = false;
						$('#updateDeliveryRequestsModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Pedidos atualizados com sucesso!');
					} else {
						$scope.updateProcessed = false;
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message);
					}
					LoadingUtil.hide($('#loadingModalArea'));
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao atualizar os pedidos!');
						LoadingUtil.hide($('#loadingModalArea'));
					}
				});	
		}
    }
    
	var initializeDelivery = function(item) {
		item.franchisee = $scope.deliveryRequestsFranchisees[item.franchiseeId];    	
    	item.product = $scope.deliveryRequestsProducts[item.productId];
    	item.product.sizes.forEach(function(size, index, array) {
    		if (size.id == item.productSizeId) {
    			item.productSize = size;
		    }
		});    	
		item.updateCheck = false;
		item.updateStatusRequest = { deliveryRequestId: item.id
					                , quantity: 0
					                , deliveryUnitPrice: 0
									};
		initializeUpdatableFields(item);
		item.calculateTotalQuantity = function() {
	    	if (item.product.hasDeliveryUnit) {
	    		return item.deliveryQty * item.productSize.deliveryQty;
	    	} else {
	    		return item.deliveryQty;
	    	}	    	
	    };
	    item.calculateDelivery = function() {return item.quantity * item.deliveryUnitPrice} 
	    item.calculateDeliveryUpdated = function() {return item.calculateTotalQuantity() * item.updateStatusRequest.deliveryUnitPrice}
    }
	
	$scope.getImageURL = function(item) {
		if (item === undefined || item.imageURL == null) {
			return '/img/not-available.jpg';
		} else {
			return item.imageURL+'=s150';
		}
    }
	
	$scope.loadDeliveryRequests = function() {

		LoadingUtil.show($('#loadingArea'));
		
		var deliveryRequestsConfig = {
				  method: 'GET',
				  url: '/ws/delivery/supplier/'+$scope.supplierId+'/status/'+$scope.deliveryStatus,
				  unique: false,
				  requestId: 'deliveryRequests-load'
				};
		
		httpSvc(deliveryRequestsConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {			
					$scope.deliveryRequestsProducts = data.products;
					$scope.deliveryRequestsFranchisees = data.franchisees;
					$scope.deliveryRequestsFranchisors = data.franchisors;
					$scope.deliveryRequestsCities = data.cities;
					$scope.deliveryRequestsStates = data.states;
					$scope.deliveryRequestsFranchiseesList = [];
					$scope.deliveryRequestsFranchiseesList = [{id: UNSELECTED_FRANCHISEE_ID, name: ' Selecione...'}]; //Space to be in the top of the sort list
					$scope.franchiseeId = UNSELECTED_FRANCHISEE_ID; //Any ugly franchiseeId that we are sure does not come in any response
					for (var key in data.franchisees) {
						$scope.deliveryRequestsFranchiseesList.push(data.franchisees[key]);
					}
					$scope.deliveryRequests = data.deliveryRequests;
					$scope.deliveryRequests.forEach(function(entry, index, array) {
					    initializeDelivery(entry);
					});										
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados de entrega!');
				}
				LoadingUtil.hide($('#loadingArea'));
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os dados de entrega!');
				LoadingUtil.hide($('#loadingArea'));
			});
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#autoCancellationDate').removeClass(errorClass);
		$('#dueDate').removeClass(errorClass);
		$('#fiscalNumber').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.updateRequest.status == 'PENDING_WITH_RESTRICTION' && $scope.updateRequest.autoCancellationDate == "") {
			$('#autoCancellationDate').addClass(errorClass);
			result = false;
			errorMsg += "<li>Data de Cancelamento Automático não pode ser vazia</li>";
		}
		
		if (($scope.updateRequest.status == 'WAITING_PAYMENT' || $scope.updateRequest.status == 'COMPLETED') && 
				$scope.updateRequest.dueDate == "" && $scope.updateRequest.paymentSlipId != 0) {
			$('#dueDate').addClass(errorClass);
			result = false;
			errorMsg += "<li>Data de Vencimento do Boleto não pode ser vazia</li>";
		}
		
		if ($scope.updateRequest.status == 'COMPLETED' && 
				$scope.updateRequest.fiscalNumber == "" && $scope.updateRequest.fiscalFileId != 0) {
			$('#fiscalNumber').addClass(errorClass);
			result = false;
			errorMsg += "<li>Número da Nota Fiscal não pode ser vazia</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	$scope.printDeliveryRequests = function() {
		
		var selected = false;
		$scope.deliveryRequests.forEach(function(item, index, array) {
			if (item.updateCheck) {
				selected = true;
			}
		});
		if (!selected) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Selecione ao menos um pedido para imprimir');
			return;
		}
		
		$scope.printRequestItems = [];		
    	$scope.deliveryRequests.forEach(function(item, index, array) {
    		if (item.updateCheck) {
    			initializeUpdatableFields(item);
    			$scope.printRequestItems.push(item);
    		}
		});
		
		var popupWindow = $window.open('/supplier/deliveryRequestsPrint');
		popupWindow.franchisee = $scope.deliveryRequestsFranchisees[$scope.franchiseeId];
		popupWindow.printRequestItems = $scope.printRequestItems;
		popupWindow.deliveryRequestsCities = $scope.deliveryRequestsCities;
		popupWindow.deliveryRequestsStates = $scope.deliveryRequestsStates;
	}
	
    var init = function() {
    	
    	if ($routeParams.supplierId) {
			$scope.supplierId = $routeParams.supplierId;
		} else {
			$scope.supplierId = gSupplierId;
		}
    	$scope.franchiseeId = UNSELECTED_FRANCHISEE_ID;
    	
		//get delivery request statuses
		var deliveryStatusesConfig = {
				  method: 'GET',
				  url: '/ws/delivery/statuses',
				  unique: false,
				  requestId: 'deliveryStatuses-load'
				};
		
		httpSvc(deliveryStatusesConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {
			    	$scope.deliveryStatuses = data.enumValues;
			    	$scope.deliveryStatus = data.enumValues[0].key;
			    	$scope.loadDeliveryRequests();
				} else {
					$scope.deliveryStatuses = [{key: 0, value: 'Erro ao carregar!'}];
					$scope.deliveryStatus = 0;
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os status de entrega!');
			});
	}
	
	init();
	
});