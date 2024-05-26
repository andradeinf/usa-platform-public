angular.module('franchisorLogistics').controller('DocumentsCtrl', function ($scope, httpSvc) {
	
	$scope.openFolder = function(folder){
		$scope.currentFolder = folder;
		$scope.path.push(folder);

		$scope.folders = folder.folders;
		$scope.files = folder.files;
	}

	$scope.navigate = function(folder) {
		if (folder != $scope.currentFolder) {
			var index = $scope.path.indexOf(folder);
			$scope.path = $scope.path.slice(0, index);
			$scope.openFolder(folder);
		}
	}
	
	$scope.contentTypeClass = function(contentType){
    	var className = 'fa-file-o';
    	if (contentType.indexOf("pdf") >= 0) {className = 'fa-file-pdf-o';}
    	if (contentType.indexOf("image") >= 0) {className = 'fa-file-image-o';}
    	if (contentType.indexOf("presentation") >= 0) {className = 'fa-file-powerpoint-o';}
    	if (contentType.indexOf("excel") >= 0) {className = 'fa-file-excel-o';}
    	return className;
    };
    
    $scope.formatFileSize = function(byteSize){
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
    }    
    
    $scope.sort = function(columnName){
    	if ($scope.sortColumn == columnName) {
    		$scope.sortOrder = !$scope.sortOrder;
    	} else {
    		$scope.sortColumn = columnName;
    		$scope.sortOrder = false;
    	}
    }
 	
	var init = function() {

		$scope.path = [];
		$scope.sortColumn = 'name';
		$scope.sortOrder = false;
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/documents/tree',
			  unique: false,
			  requestId: 'documents-load'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					data.documentTree.name = "Pasta Principal";
					$scope.openFolder(data.documentTree);					
					$('#list-overlay').remove();
					$('#list-loading-img').remove();
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
	
	init();
});