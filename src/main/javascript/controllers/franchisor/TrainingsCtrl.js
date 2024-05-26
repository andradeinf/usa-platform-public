angular.module('franchisorLogistics').controller('TrainingsCtrl', function ($scope, $rootScope, $sce, httpSvc) {
	
	var errorClass = 'has-error';

	$scope.sortableOptions = {
		handle: '.sortHandler'
	};

	$scope.enableSortItems = function() {
		$scope.sortItems = true;
	}

	$scope.cancelSortItems = function() {
		sortItems();
		$scope.sortItems = false;
	}

	$scope.saveSortItems = function() {
		var trainingsOrder = {}
		$scope.trainings.forEach((training, index) => {
			trainingsOrder[training.id] = $scope.trainings.length - index - 1;
		});

		var config = {
			method: 'PUT' ,
			url: '/ws/trainings/orders',
			data: trainingsOrder,
			unique: true,
			requestId: 'training-updateOrders'
			};
			
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.trainings.forEach(training => {
						training.order = trainingsOrder[training.id];
					});
					sortItems();

					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Nova ordenação salva com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar a nova ordenação!\n'+data.returnMessage.message);
				}					
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar a nova ordenação!');
				}
			});

		$scope.sortItems = false;
	}

	$scope.new = function() {

		$scope.training = { id: 0
			, order: $scope.trainings.length == 0 ? 0 : $scope.trainings[0].order + 1
			, title: ''
			, description: ''
			, accessRestricted: false
			, videoId: 0
		}

		reset();

		$scope.videoUpload.selectVisible = true;
	}
	
	$scope.edit = function(training) {

		$scope.training = { id: training.id
			, order: training.order
			, title: training.title
			, description: training.description
			, accessRestricted: training.accessRestricted
			, videoId: training.videoId
			, videoName: training.videoName
		}

		reset();
		
		$scope.franchisees
				.filter(franchisee => training.franchiseeIds.includes(franchisee.id))
				.forEach(franchisee => {
					franchisee.selected = true;			
				});
	}	

	$scope.save = function(){
		if (validate()) {

			$scope.training.franchiseeIds = [];	
			if (!$scope.training.accessRestricted) {
				$scope.training.franchiseeIds = 
					$scope.franchisees
						.filter(franchisee => franchisee.selected)
						.map(franchisee => franchisee.id);
			}			
		
			var config = {
				method: ($scope.training.id == 0 ? 'POST' : 'PUT') ,
				url: '/ws/trainings',
				data: $scope.training,
				unique: true,
				requestId: 'training-update'
				};
				
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						initializeTraining(data.training);
						$scope.trainings = $scope.trainings.filter(training => training.id != data.training.id);
						$scope.trainings.push(data.training);
						sortItems();
						calculateStorageSize();

						$('#newTrainingModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Vídeo salvo com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar o video!\n'+data.returnMessage.message);
					}					
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar o video!');
					}
				});
		}
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir o vídeo "' + item.title + '"?', doDelete, 'Não', 'Sim');
	}

	var sortItems = function() {
		$scope.trainings.sort(function (a, b) {
			return b.order - a.order;
		});
	}
		
	var doDelete = function(){
			
		var config = {
					method: 'DELETE' ,
					url: '/ws/trainings/'+$scope.itemToRemove.id,
					unique: true,
					requestId: 'trainings-delete'
				};
		
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.trainings = $scope.trainings.filter(training => training.id != $scope.itemToRemove.id);
					calculateStorageSize();
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Vídeo excluído com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o vídeo!');
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o vídeo!');
				}
			});
	}

	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.training.title == "") {
			$('#title').addClass(errorClass);
			result = false;
			errorMsg += "<li>Informe o título</li>";
		}

		if ($scope.training.description == "") {
			$('#description').addClass(errorClass);
			result = false;
			errorMsg += "<li>Informe a descrição</li>";
		}

		if ($scope.file && $scope.file.type != 'video/mp4') {
			$('#file').addClass(errorClass);
			result = false;
			errorMsg += "<li>O formato do video não é aceito</li>";
		} else if ($scope.training.videoId == 0) {
			$('#file').addClass(errorClass);
			result = false;
			errorMsg += "<li>Faça o upload do video</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}

	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#title').removeClass(errorClass);
		$('#description').removeClass(errorClass);
		$('#file').removeClass(errorClass);
	}

	var reset = function() {

		$scope.franchisees.forEach(function(entry) {
			entry.selected = false;
		});

		document.getElementById("inputFile").value = "";

		$scope.videoUpload = {
			progressVisible: false,
			progress: 0,
			error: "",
			selectVisible: false,
			replace: false
		}

		resetValidation();
	}

	$scope.replaceVideo = function() {
		$scope.videoUpload.replace = true;
		$scope.videoUpload.selectVisible = true;
	}

	$scope.cancelReplaceVideo = function() {
		$scope.videoUpload.replace = false;
		$scope.videoUpload.selectVisible = false;
		$scope.file = undefined;
	}

	$scope.doUploadVideo = function(e) {
		
		if (e.files.length > 0) {

			if (e.files[0].type != 'video/mp4') {
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'O formato do video selecionado não é aceito!');
				return;
			}
			
			var config = {
			  method: 'GET' ,
			  url: '/ws/trainings/video/url',
			  unique: false,
			  requestId: 'trainings-getUploadUrl'
			};
			
			httpSvc(config)
				.success(function(data){
					var fd = new FormData();
					fd.append("uploadedFile", $scope.file);
					
					var xhr = new XMLHttpRequest();
					xhr.upload.addEventListener("progress", function (evt) {uploadProgress(evt);}, false);
			        xhr.addEventListener(       "load",     function (evt) {uploadComplete(evt);}, false);
			        xhr.addEventListener(       "error",    function (evt) {uploadFailed(evt);}, false);
			        xhr.addEventListener(       "abort",    function (evt) {uploadCanceled(evt);}, false);
			        xhr.open("POST", data.url);
					xhr.setRequestHeader('Accept', 'application/json');
					$scope.videoUpload.progressVisible = true
			        xhr.send(fd);
				});
		}
	}
	
	var uploadProgress = function(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
            	$scope.videoUpload.progress = Math.round(evt.loaded * 100 / evt.total)
            } else {
            	$scope.videoUpload.progress = 'unable to compute'
            }
        });
    }
	
	var uploadComplete = function(evt) {
    	$scope.$apply(function(){
    		$scope.videoUpload.progressVisible = false;
    		var data = JSON.parse(evt.target.response);
			if (data.returnMessage.code > 0) {
				$scope.training.videoId = data.uploadedFile.id;
				$scope.training.videoName = data.uploadedFile.name;
				$scope.videoUpload.selectVisible = false;
			} else {
				$scope.videoUpload.error = data.returnMessage.message;
			}
        });
    }
	
	var uploadFailed = function(evt) {
		$scope.$apply(function(){
    		$scope.videoUpload.progressVisible = false;
			$scope.videoUpload.error = "Erro ao carregar o arquivo";
        });
    }
	
	var uploadCanceled = function(evt) {
		$scope.$apply(function(){
    		$scope.videoUpload.progressVisible = false;
			$scope.videoUpload.error = "Upload cancelado";
        });
	}

	var initializeTraining = function(training) {

		training.videoSources = [
			{
				src: $sce.trustAsResourceUrl("/ws/trainings/video/stream/" + training.id), type: training.videoContentType
			}
		];
		training.viewCheckOpen = false;
		training.viewCheckFranchiseesInitialized = false;
	}

	$scope.formatSize = function(size) {
		var sizeUnit = 'B';
		var formatedSize = size;

		if (formatedSize > 1024) {
			formatedSize = formatedSize / 1024;
			sizeUnit = 'KB';
		}
		
		if (formatedSize > 1024) {
			formatedSize = formatedSize / 1024;
			sizeUnit = 'MB';
		}
		
		if (formatedSize > 1024) {
			formatedSize = formatedSize / 1024;
			sizeUnit = 'GB';
		}
		
		return formatedSize.toFixed(2) + ' ' + sizeUnit;
	}

	var pad = function (num, size) {
		var s = "000000000" + num;
		return s.substr(s.length-size);
	}

	$scope.formatTime = function(time) {
		var pendingTime = time;
		var hours = 0;
		var mins = 0;
		var secs = 0;
		var formatedTime = '';

		if (pendingTime >= 60) {
			secs = pendingTime % 60
			pendingTime = Math.floor(pendingTime / 60);

			if (pendingTime >= 60) {
				mins = pendingTime % 60
				hours = Math.floor(pendingTime / 60);
			} else {
				mins = pendingTime;
			}
		} else {
			secs = pendingTime;
		}

		if (hours > 0) {
			formatedTime = pad(hours, 2) + ':' + pad(mins, 2) + ':' + pad (secs, 2);
		} else {
			formatedTime =  pad(mins, 2) + ':' + pad (secs, 2);
		}
				
		return formatedTime;
	}

	var calculateStorageSize = function() {
		
		if ($scope.trainings && $scope.trainings.length > 0) {
			$scope.totalUsage = 
				$scope.trainings
				.map(trainings => trainings.videoSize)
				.reduce((totalSize, videoSize) => {
					return totalSize + videoSize
				});

			$scope.totalUsagePercentage = $scope.totalUsage / $scope.maxUsage * 100;
			$scope.totalUsagePercentage = $scope.totalUsagePercentage.toFixed(2);
		}

	}

	$scope.showMoreTrainings = function() {
		$scope.pageNumber = $scope.pageNumber + 1;
	}

	$scope.toggleTrainingCheckView = function(training) {
		training.viewCheckOpen = !training.viewCheckOpen;
    	
    	if (training.viewCheckOpen) {
    		
    		if (!training.viewCheckFranchiseesInitialized) {
    			
    			training.viewCheckFranchiseesInitialized = true;
    			training.franchisees = [];
    			
    			if (training.franchiseeIds && training.franchiseeIds.length > 0) {
        			training.franchiseeIds.forEach(function(id) {
        				training.franchisees.push({
        					franchisee: $scope.franchiseesMap[id],
        					open: false
        				});
            		});
        		} else {
        			$scope.franchisees.forEach(function(franchisee) {
        				training.franchisees.push({
        					franchisee: franchisee,
        					open: false
        				});
        			});
        		};
    		}
    	}    	
	}

	$scope.toggleCheckViewFranchisee = function(training, franchiseeItem) {
		
		franchiseeItem.open = !franchiseeItem.open;
    	
    	if (franchiseeItem.open) {
    		franchiseeItem.views = [];
    		franchiseeItem.viewUsers = [];
    		franchiseeItem.loading = true;
    		
    		var config = {
  	  			  method: 'GET',
  	  			  url: '/ws/trainings/'+training.id+'/viewControl/franchisee/'+franchiseeItem.franchisee.id,
  	  			  unique: true,
  	  			  requestId: 'trainings-getViews'
  	  			};
    		
    		httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						franchiseeItem.views = data.trainingViewControls;
						franchiseeItem.viewUsers = data.users;
						franchiseeItem.loading = false;
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
						franchiseeItem.loading = false;
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar as confirmações de leitura.');
						franchiseeItem.loading = false;
					}
				});
    	}
	}
	
	var init = function() {

		$scope.trainings = [];
		$scope.sortItems = false;
		$scope.totalUsage = 0;
		$scope.totalUsagePercentage = 0;
		$scope.maxUsage = $rootScope.maxStorageSizeMB * 1024 * 1024;

		$scope.pageNumber = 1;
		$scope.pageSize = 5;

		$scope.video = {
			config: {
				preload: "none",
				theme: {
					url: "/vendor/videogular/css/videogular.min.css"
				},
				plugins: {
					controls: {
						autoHide: true,
						autoHideTime: 5000
					}
				}
			}
		};
	
		LoadingUtil.show($('#trainings-loadingArea'));
		
		var config = {
			  method: 'GET',
			  url: '/ws/trainings/franchisor/'+gFranchisorId,
			  unique: false,
			  requestId: 'trainings-load'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					data.trainings.forEach(training => {
						initializeTraining(training);
					});
					$scope.trainings = data.trainings;
					sortItems();
					calculateStorageSize();
				}  else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}
				
				LoadingUtil.hide($('#trainings-loadingArea'));
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}
			});

		var configFranchisees = {
			method: 'GET' ,
			url: '/ws/franchisees/franchisor/' + gFranchisorId,
			unique: false,
			requestId: 'franchisees-load'
			};
		  
		  httpSvc(configFranchisees)
			  .success(function(data){
				  if (data.returnMessage.code > 0) {
					  	$scope.franchisees = data.franchisees;
					  	$scope.franchiseesMap = [];
						$scope.franchisees.forEach(function(entry) {
							$scope.franchiseesMap[entry.id] = entry;
						});
				  } else {
					  NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				  }	
			  })
			  .error(function(data){
				  //499 - duplicated request
				  if (data.status != 499) {
					  NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				  }
			  });
	}
	
	init();
});