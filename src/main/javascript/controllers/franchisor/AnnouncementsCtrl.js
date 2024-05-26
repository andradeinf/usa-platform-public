angular.module('franchisorLogistics').controller('AnnouncementsCtrl', function ($scope, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.openUploadImage = function(deferred,restoreSelection) {
		document.getElementById("inputFile").value = "";
		resetUploadImage();		
		
		$scope.uploadFileField = true;
		$scope.uploadImageDeferred = deferred;
		$scope.uploadImageRestoreSelection = restoreSelection;
	}
	
	$scope.cancelUploadImage = function() {
		resetUploadImage();
		$scope.uploadImageRestoreSelection();
	}
	
	
	var resetUploadImage = function() {
		$scope.uploadFileField = false;
		$scope.progressVisible = false;
		resetFileError();
	}
	
	var setFileUploadError = function() {
		resetFileError();		
		$('#file').addClass(errorClass);
	}
	
	var resetFileError = function() {
		$('#file').removeClass(errorClass);
	}
	
	$scope.doUploadImage = function(e) {
		
		resetFileError();
		
		if (e.files.length > 0) {
			var config = {
					  method: 'GET' ,
					  url: '/ws/announcements/url',
					  unique: false,
					  requestId: 'announcements-getUploadUrl'
					};
			
			httpSvc(config)
				.success(function(data){	        
			        var fd = new FormData()
			        fd.append("uploadedFile", $scope.file)
			        var xhr = new XMLHttpRequest()
			        xhr.upload.addEventListener("progress", uploadProgress, false)
			        xhr.addEventListener("load", uploadComplete, false)
			        xhr.addEventListener("error", uploadFailed, false)
			        xhr.addEventListener("abort", uploadCanceled, false)
			        xhr.open("POST", data.url)
			        xhr.setRequestHeader('Accept', 'application/json');
			        $scope.progressVisible = true
			        xhr.send(fd);
				});
		}
	}
	
	function uploadProgress(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
                $scope.progress = Math.round(evt.loaded * 100 / evt.total)
            } else {
                $scope.progress = 'unable to compute'
            }
        })
    }
	
	function uploadComplete(evt) {
		$scope.progressVisible = false;
		if (this.status == 413) {
			setFileUploadError();
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao carregar o arquivo! Tamanho máximo excedido');
		} else if (this.status == 415) {
			setFileUploadError();
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao carregar o arquivo! Formato não permitido');
		} else {
	    	var responseObj = JSON.parse(this.response);	    	
			$scope.uploadImageRestoreSelection();
	        document.execCommand('insertImage', true, responseObj.url);
	        $scope.uploadImageDeferred.resolve();
	        $scope.uploadFileField = false;	        
		}
    }
    
    function uploadFailed(evt) {
        NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao carregar o arquivo!');
    }
    
	function uploadCanceled(evt) {
        NotificationUtil.show($('#notificationArea'), 'warning', 'Alerta!', 'Upload cancelado!');
    }
	
	$scope.new = function() {
		reset();
	}
	
	$scope.edit = function(item){
		$scope.franchisees.forEach(function(entry) {
			entry.selected = false;
		});
		
		$scope.announcement = item;
		
		if ($scope.announcement.franchiseeIds) {
			$scope.announcement.franchiseeIds.forEach(function(franchiseeId) {
				$scope.franchiseesMap[franchiseeId].selected = true;
			});	
		}		
		
		//clear validation
		resetValidation();
		
		resetUploadImage();
	}
	
	$scope.publish = function(item) {
		var config = {
	  			  method: 'PUT',
	  			  url: '/ws/announcements/publish',
	  			  data: item,
	  			  unique: true,
	  			  requestId: 'announcements-publish'
	  			};
		
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						
						data.announcement.collapsed = true;
						data.announcement.open = false;
						data.announcement.franchiseesInitialized = false;
						
						var index = $scope.announcements.indexOf(item);
						$scope.announcements[index] = data.announcement;
						
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Aviso publicado com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao publicar o aviso.');
					}
				});	
	}
	
	$scope.save = function(){
		if (validate()) {
			
			//update list of selected franchisees
			$scope.announcement.franchiseeIds = [];
			$scope.franchisees.forEach(function(entry) {
	        	if (entry.selected) {
	        		$scope.announcement.franchiseeIds.push(entry.id);
	        	}
			});
			
			var config = {
	  			  method: ($scope.announcement.id == 0 ? 'POST' : 'PUT') ,
	  			  url: '/ws/announcements',
	  			  data: $scope.announcement,
	  			  unique: true,
	  			  requestId: 'announcements-save'
	  			};
		
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if ($scope.announcement.id == 0) {
							data.announcement.collapsed = true;
							data.announcement.open = false;
							data.announcement.franchiseesInitialized = false;
							$scope.announcements.push(data.announcement);
						}						
						$('#newAnnouncementModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Aviso salvo com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar o aviso.');
					}
				});	
		};
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir o aviso "' + item.title + '"?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){
		
		var config = {
					method: 'DELETE' ,
					url: '/ws/announcements/'+$scope.itemToRemove.id,
					unique: true,
					requestId: 'announcements-delete'
				};
		
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.announcements = $scope.announcements.filter(announcement => announcement.id != $scope.itemToRemove.id);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Aviso excluído com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o aviso!');
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o aviso!');
				}
			});
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#title').removeClass(errorClass);
		$('#message').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.announcement.title == "") {
			$('#title').addClass(errorClass);
			result = false;
			errorMsg += "<li>Informe o título do aviso</li>";
		}
		
		if ($scope.announcement.message == "") {
			$('#message').addClass(errorClass);
			result = false;
			errorMsg += "<li>Informe a mensagem do aviso</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		
		$scope.announcement = { id: 0
							, franchisorId: gFranchisorId
							, title: ''
							, message: ''
							, collapsed: true
							, open: false
							, franchiseesInitialized: false
							}
		
		$scope.franchisees.forEach(function(entry) {
			entry.selected = false;
		});
		
		//clear validation
		resetValidation();	
		
		resetUploadImage();
	}
	
	$scope.previousAnnouncemens = function() {
		
		$scope.page -= 1;
		$scope.fetchAnnouncements();
		
	}
	
	$scope.nextAnnouncemens = function() {
		
		$scope.page += 1;
		$scope.fetchAnnouncements();
		
	}
	
	$scope.toggleAnnouncementDetails = function(item){
		item.collapsed = !item.collapsed;
	}
	
	$scope.fetchAnnouncements = function() {
		
		LoadingUtil.show($("#announcemensLoad"));
		
		//get announcements
		var announcementsConfig = {
			  method: 'GET',
			  url: '/ws/announcements/franchisor/'+gFranchisorId+'/paged/'+$scope.page+'/pageSize/'+$scope.pageSize,
			  unique: false,
			  requestId: 'announcements-load'
			};
			
		httpSvc(announcementsConfig)
		.success(function(data){
			if (data.returnMessage.code > 0) {
				$scope.announcements = data.announcementList;
				$scope.announcements.forEach(function(announcement) {
					announcement.collapsed = true;
					announcement.open = false;
					announcement.franchiseesInitialized = false;
				});
				$scope.nextEnabled = data.hasMore;
				
				LoadingUtil.hide($("#announcemensLoad"));
			} else {
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os avisos!');
			}	
		})
		.error(function(data){
			//499 - duplicated request
			if (data.status != 499) {
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os avisos!');
			}
		});
	}
	
	$scope.toggleAnnouncementCheckRead = function(announcement) {
		announcement.open = !announcement.open;
    	
    	if (announcement.open) {
    		
    		if (!announcement.franchiseesInitialized) {
    			
    			announcement.franchiseesInitialized = true;
    			announcement.franchisees = [];
    			
    			if (announcement.franchiseeIds.length > 0) {
        			announcement.franchiseeIds.forEach(function(id) {
        				announcement.franchisees.push({
        					franchisee: $scope.franchiseesMap[id],
        					open: false
        				});
            		});
        		} else {
        			$scope.franchisees.forEach(function(franchisee) {
        				announcement.franchisees.push({
        					franchisee: franchisee,
        					open: false
        				});
        			});
        		};
    		}
    	}    	
	}
	
	$scope.toggleCheckReadFranchisee = function(announcement, franchiseeItem) {
		
		franchiseeItem.open = !franchiseeItem.open;
    	
    	if (franchiseeItem.open) {
    		franchiseeItem.notifications = [];
    		franchiseeItem.notificationUsers = [];
    		franchiseeItem.loading = true;
    		
    		var config = {
  	  			  method: 'GET',
  	  			  url: '/ws/announcements/'+announcement.id+'/notifications/franchisee/'+franchiseeItem.franchisee.id,
  	  			  unique: true,
  	  			  requestId: 'announcements-getNotifications'
  	  			};
    		
    		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					franchiseeItem.notifications = data.notifications;
					franchiseeItem.notificationUsers = data.users;
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
	
	$scope.setPageSize = function (size) {
		$scope.pageSize = size;
		$scope.page = 1;
		$scope.fetchAnnouncements();
	}
	
	var init = function() {
		
		//announcements
		$scope.page = 1;
		$scope.pageSize = 10;
		$scope.announcements = [];
		
		$scope.fetchAnnouncements();
		
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