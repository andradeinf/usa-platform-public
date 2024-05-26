angular.module('franchisorLogistics').controller('DocumentsCtrl', function ($scope, $rootScope, httpSvc) {
	
	var errorClass = 'has-error';

	$scope.openFolder = function(folder){
		$scope.currentFolder = folder;
		$scope.path.push(folder);

		LoadingUtil.show($('#documents-loadingArea'));

		var configDocuments = {
			method: 'GET' ,
			url: '/ws/documents/folder/' + $scope.currentFolder.id,
			unique: false,
			requestId: 'documents-load'
		  };
  
		httpSvc(configDocuments)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.folders = data.folders;
					$scope.files = data.files;
					
					LoadingUtil.hide($('#documents-loadingArea'));
				}  else {
					LoadingUtil.hide($('#documents-loadingArea'));
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}				
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					LoadingUtil.hide($('#documents-loadingArea'));
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar os dados!');
				}
			});
	}

	$scope.navigate = function(folder) {
		if (folder != $scope.currentFolder) {
			var index = $scope.path.indexOf(folder);
			$scope.path = $scope.path.slice(0, index);
			$scope.openFolder(folder);
		}
	}
	
	$scope.newFolder = function(){
		resetFolder();
	}
	
	$scope.editFolder = function(item){
		$scope.folder = item;
	}

	$scope.saveFolder = function(){
		if (validateFolder()) {
		
			var config = {
				  method: ($scope.folder.id == 0 ? 'POST' : 'PUT') ,
				  url: '/ws/documents/folder',
				  data: $scope.folder,
				  unique: true,
				  requestId: 'folder-createOrUpdate'
				};
				
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if ($scope.folder.id == 0) {
							$scope.folders.push(data.documentsFolder);
						}
						$('#newFolderModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados da pasta salvos com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados da pasta!\n'+data.returnMessage.message);
					}					
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados da pasta!');
					}
				});					
		};
	}

	$scope.deleteFolder = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir a pasta "' + item.name + '"?', doDeleteFolder, 'Não', 'Sim');
	}
		
	var doDeleteFolder = function(){
	
		var config = {
				method: 'DELETE' ,
				url: '/ws/documents/folder/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'folder-delete'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.folders = $scope.folders.filter(folder => folder.id != $scope.itemToRemove.id);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Pasta excluída com sucesso!');
				}  else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.details);
				}					
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir a pasta!');
				}
			});
	}

	var resetAddFile = function() {
		document.getElementById("inputFile").value = "";
	}
	
	$scope.addFile = function(){
		resetAddFile()
		$scope.uploadedFiles = [];
		$scope.accessRestricted = $rootScope.domainConfiguration.featureFlags['DOCUMENT_FILE_DEFAULT_ACCESS_RESTRICTED'];
		$scope.franchisees.forEach(function(entry) {
			entry.selected = false;
		});
		resetFileError();
	}
	
	$scope.editFile = function(file){
		$scope.franchisees.forEach(function(entry) {
			entry.selected = false;
		});
		
		$scope.fileToEdit = file;
		$scope.fileToEdit.franchiseeIds.forEach(function(key) {
			$scope.franchisees.forEach(function(entry) {
				if (entry.id == key) {
					entry.selected = true;
				}				
			});
		});		
	}
	
	$scope.saveEditFile = function(){
		
		$scope.fileToEdit.franchiseeIds = [];
		$scope.franchisees.forEach(function(entry) {
        	if (entry.selected) {
        		$scope.fileToEdit.franchiseeIds.push(entry.id);
        	}
		});
	
		var config = {
			  method: 'PUT' ,
			  url: '/ws/documents/file',
			  data: $scope.fileToEdit,
			  unique: true,
			  requestId: 'file-update'
			};
			
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					$('#editFileModal').modal('hide');
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Permissões do arquivo salvas com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar as permissões do arquivo!\n'+data.returnMessage.message);
				}					
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar as permissões do arquivo!');
				}
			});					
	}

	$scope.deleteUploadedFile = function(item) {
		var index = $scope.uploadedFiles.indexOf(item);
		$scope.uploadedFiles.splice(index, 1);
	}

	var uploadCallback = function(fileData, file) {
		return function(data) {
			var fd = new FormData();
			fd.append("uploadedFile", file);
			
			var xhr = new XMLHttpRequest();
			xhr.upload.addEventListener("progress", function (evt) {uploadProgress(evt, fileData);}, false);
			xhr.addEventListener(       "load",     function (evt) {uploadComplete(evt, fileData);}, false);
			xhr.addEventListener(       "error",    function (evt) {uploadFailed(evt,   fileData);}, false);
			xhr.addEventListener(       "abort",    function (evt) {uploadCanceled(evt, fileData);}, false);
			xhr.open("POST", data.url);
			xhr.setRequestHeader('Accept', 'application/json');
			xhr.send(fd);

			resetAddFile();
		}
	}

	$scope.doUploadFile = function(e) {

		if (e.files.length > 0) {

			for (const file of e.files) {

				var fileData = {
					name: file.name,
					progress: 0,
					progressVisible: true,
					error: "",
					id: 0
				}
				$scope.uploadedFiles.push(fileData);

				var config = {
					method: 'GET' ,
					url: '/ws/documents/uploadUrl',
					unique: false,
					requestId: 'documents-getUploadUrl'
				  };
				  
				httpSvc(config)
					.success(uploadCallback(fileData, file));

			}
		}

	}
	
	$scope.saveNewFiles = function() {
		
		resetFileError();
		
		if (validateFile()) {

			$scope.filesToSave = {
				fileIds: [],
				folderId: $scope.currentFolder.id,
				accessRestricted: $scope.accessRestricted,
				franchiseeIds: []
			};

			$scope.franchisees.forEach(function(franchisee) {
				if (franchisee.selected) {
					$scope.filesToSave.franchiseeIds.push(franchisee.id);
				}
			});

			$scope.uploadedFiles.forEach(function(fileItem) {
				if (fileItem.id != 0) {
					$scope.filesToSave.fileIds.push(fileItem.id);
				}				
			});

			var config = {
				method: 'POST' ,
				url: '/ws/documents/file',
				data: $scope.filesToSave,
				unique: true,
				requestId: 'files-save'
			  };
			  
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {

						data.files.forEach(function(fileItem) {
							$scope.files.push(fileItem);
						});
						
						$('#newFileModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Arquivos carregado com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os arquivos!\n'+data.returnMessage.message);
					}					
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os arquivos!');
					}
				});
		}		
	}

	$scope.deleteFile = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir o arquivo "' + item.name + '"?', doDeleteFile, 'Não', 'Sim');
	}
		
	var doDeleteFile = function(){
	
		var config = {
				method: 'DELETE' ,
				url: '/ws/documents/file/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'file-delete'
			};
	
		httpSvc(config)
			.success(function(){
				$scope.files = $scope.files.filter(file => file.id != $scope.itemToRemove.id);
				NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Arquivo excluído com sucesso!');
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o arquivo!');
				}
			});
	}
	
	$scope.refreshMoveFolder = function(folder){
		$scope.currentMoveFolder = folder;
		$scope.movePath.push(folder);

		var configDocuments = {
			method: 'GET' ,
			url: '/ws/documents/folder/' + $scope.currentMoveFolder.id,
			unique: false,
			requestId: 'documents-load'
		  };
  
		httpSvc(configDocuments)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.moveFolders = data.folders.filter(folder => folder.id != $scope.moveItem.id);
				}  else {
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

	$scope.navigateMove = function(folder) {
		if (folder != $scope.currentMoveFolder) {
			var index = $scope.movePath.indexOf(folder);
			$scope.movePath = $scope.movePath.slice(0, index);
			$scope.refreshMoveFolder(folder);
		}
	}

	var initializeMovePath = function(){
		$scope.movePath = $scope.path.map((x) => x);
		$scope.currentMoveFolder = $scope.movePath[$scope.movePath.length-1];
		$scope.movePath = $scope.movePath.slice(0, $scope.movePath.length-1);
		$scope.refreshMoveFolder($scope.currentMoveFolder);
	}

	$scope.moveFolderOrFile = function(item, type){
		$scope.moveItem = item;
		$scope.moveType = type;
		initializeMovePath();		
	}
	$scope.saveMoveFolderOrFile = function(){

		var config = {
			method: 'PUT' ,
			url: '/ws/documents/move',
			data: {
				type: $scope.moveType,
				id: $scope.moveItem.id,
				folderId: $scope.currentMoveFolder.id
			},
			unique: true,
			requestId: 'documents-move'
		  };
		  
		httpSvc(config)
		  .success(function(data){
			  if (data.returnMessage.code > 0) {

				if ($scope.moveType == 'file') {
					var index = $scope.files.indexOf($scope.moveItem);
					$scope.files.splice(index, 1);
				} else if ($scope.moveType == 'folder') {
					var index = $scope.folders.indexOf($scope.moveItem);
					$scope.folders.splice(index, 1);
				}
				  
				  $('#moveFolderOrFile').modal('hide');
				  NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Movido com sucesso!');
			  } else {
				  NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao mover!\n'+data.returnMessage.message);
			  }					
		  })
		  .error(function(data){
			  //499 - duplicated request
			  if (data.status != 499) {
				  NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao mover o arquivo ou pasta!');
			  }
		  });	
	}
	
	$scope.contentTypeClass = function(contentType){
		var className = 'fa-file-o';
		if (contentType) {
			if (contentType.indexOf("pdf") >= 0) {className = 'fa-file-pdf-o';}
			if (contentType.indexOf("image") >= 0) {className = 'fa-file-image-o';}
			if (contentType.indexOf("presentation") >= 0) {className = 'fa-file-powerpoint-o';}
			if (contentType.indexOf("excel") >= 0) {className = 'fa-file-excel-o';}
		}    	
    	return className;
    };
    
    $scope.formatFileSize = function(byteSize){
		if (byteSize) {
			var sizeUnit = 'B';
			var formatedSize = byteSize;
			
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
		} else {
			return undefined;
		}
    	
    }
    
    $scope.sort = function(columnName){
    	if ($scope.sortColumn == columnName) {
    		$scope.sortOrder = !$scope.sortOrder;
    	} else {
    		$scope.sortColumn = columnName;
    		$scope.sortOrder = false;
    	}
    }
	
	var uploadProgress = function(evt, file) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
            	file.progress = Math.round(evt.loaded * 100 / evt.total)
            } else {
            	file.progress = 'unable to compute'
            }
        });
	}
	
	var uploadComplete = function(evt, file) {
    	$scope.$apply(function(){
    		file.progressVisible = false;
    		if (evt.target.status == 413) {
    			file.error = "Tamanho máximo (50 MB) excedido";
    		} else {
				var data = JSON.parse(evt.target.response);
    			if (data.returnMessage.code > 0) {
    				file.id = data.uploadedFile.id;
    			} else {
    				file.error = data.returnMessage.message;
    			}
    		}	    		
        });
	}
	
	var uploadFailed = function(evt, file) {
		$scope.$apply(function(){
    		file.progressVisible = false;
			file.error = "Erro ao carregar o arquivo";
        });
	}
	
	var uploadCanceled = function(evt, file) {
		$scope.$apply(function(){
    		file.progressVisible = false;
			file.error = "Upload cancelado";
        });
    }
	
	var resetFolder = function() {
		$scope.folder = { id: 0
						, name: ""
						, order: 0
						, parentId: $scope.currentFolder.id}
						 	
		//clear validation
		resetFolderValidation();								 	
	}
	
	var resetFolderValidation = function() {
		$('#notificationArea').html("");
		$('#name').removeClass(errorClass);
	}
	
	var validateFolder = function() {
		resetFolderValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.folder.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Nome não pode ser vazio</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var validateFile = function() {

		var result = true;
		var errorMsg = "";
		
		if ($scope.uploadedFiles.filter(file => file.id != 0).length == 0) {
			$('#file').addClass(errorClass);
			result = false;
			errorMsg += "<li>Selecione ao menos um arquivo</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var setFileUploadError = function() {
		resetFileError();
		
		$('#file').addClass(errorClass);
	}
	
	var resetFileError = function() {
		$('#file').removeClass(errorClass);
	}
	
	var init = function() {
		
		var homeFolder = { 
			  id: 0
			, name: "Pasta Principal"
			, order: 0
			, parentId: 0 
		}

		$scope.path = [];
		$scope.sortColumn = 'name';
		$scope.sortOrder = false;
		$scope.openFolder(homeFolder);

		$scope.movePath = [];
		$scope.moveFolders = [];

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