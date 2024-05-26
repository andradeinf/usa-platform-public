angular.module('franchisorLogistics').controller('MessagesCtrl', function ($scope, $rootScope, $routeParams, httpSvc) {
	
	var errorClass = 'has-error';
	var LABEL_INBOX = 0;
	var LABEL_ALL = 99;
	
	$scope.commentClasses = function(item){
    	var className = '';
    	if (item.userId == $scope.loggedUser.id) {className += ' col-xs-offset-2 my-comment';}
    	if ($scope.messageCommentsUnreadNotifications[item.id] != undefined) {className += ' unread';}
    	return className;
    };
    
    var onlyUnique = function(value, index, self) { 
        return self.indexOf(value) === index;
    }
    
    $scope.messageTopicBadgeCount = function(messageTopicsUnreadNotifications) {
    	if (messageTopicsUnreadNotifications == undefined) {
    		return 0;
    	} else {
    		// Use "map" to extract only the comment ID and the filter to count only unique comment ids
    		return messageTopicsUnreadNotifications.map(notification => notification.referenceId2).filter(onlyUnique).length;
    	}    	
    };
	
	$scope.save = function(){
		if (validate()) {
			
			$scope.attachments.forEach(function(attachment) {
				if (attachment.id != 0) {
					$scope.messageTopic.attachmentIds.push(attachment.id);
				}				
			});
		
			var config = {
	  			  method: 'POST',
	  			  url: '/ws/messages',
	  			  data: $scope.messageTopic,
	  			  unique: true,
	  			  requestId: 'messageTopics-save'
	  			};
		
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if ($scope.messageTopic.id == 0) {
							if ($scope.page == 1) {
								$scope.messageTopics.unshift(data.messageTopic);
								Object.keys(data.entities).forEach(function(key) {
									$scope.entities[key] = data.entities[key];
								});
								Object.keys(data.userGroups).forEach(function(key) {
									$scope.userGroups[key] = data.userGroups[key];
								});	
								$scope.users[$scope.loggedUser.id] = $scope.loggedUser;
								if ($scope.messageTopics.length > $scope.pageSize) {
									$scope.messageTopics.pop();
									$scope.nextEnabled = true;
								}
							}							
						}
						$('#newMessageTopicModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Mensagem enviada com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar a mensagem.');
					}
				});	
		};
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#fromUserGroupId').removeClass(errorClass);
		$('#toEntityProfile').removeClass(errorClass);
		$('#toEntityId').removeClass(errorClass);
		$('#toUserGroupId').removeClass(errorClass);
		$('#title').removeClass(errorClass);
		$('#newMessage').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.fromUserGroups.length > 0 && 
				($scope.messageTopic.fromUserGroupId == '' || $scope.messageTopic.fromUserGroupId == 0)) {
			$('#fromUserGroupId').addClass(errorClass);
			result = false;
			errorMsg += "<li>Selecione a sua área</li>";
		}
		
		if ($scope.messageTopic.toEntityProfile == "" || $scope.messageTopic.toEntityProfile == '0') {
			$('#toEntityProfile').addClass(errorClass);
			result = false;
			errorMsg += "<li>Selecione o tipo de mensagem</li>";
		}
		
		if ($scope.toEntities.length > 0 &&
				($scope.messageTopic.toEntityId == '' || $scope.messageTopic.toEntityId == 0)) {
			$('#toEntityId').addClass(errorClass);
			result = false;
			errorMsg += "<li>Selecione o destinatário</li>";
		}
		
		if ($scope.toUserGroups.length > 0 && 
				($scope.messageTopic.toUserGroupId == '' || $scope.messageTopic.toUserGroupId == 0)) {
			$('#toUserGroupId').addClass(errorClass);
			result = false;
			errorMsg += "<li>Selecione a área de destino</li>";
		}
		
		if ($scope.messageTopic.title == "") {
			$('#title').addClass(errorClass);
			result = false;
			errorMsg += "<li>Informe o título da mensagem</li>";
		}
		
		if ($scope.messageTopic.message == "") {
			$('#newMessage').addClass(errorClass);
			result = false;
			errorMsg += "<li>Informe o texto da mensagem</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	$scope.saveComment = function(){
		
		$scope.attachments.forEach(function(attachment) {
			if (attachment.id != 0) {
				$scope.messageComment.attachmentIds.push(attachment.id);
			}				
		});
		
		var config = {
  			  method: 'POST',
  			  url: '/ws/messages/comment',
  			  data: $scope.messageComment,
  			  unique: true,
  			  requestId: 'messageComments-save'
  			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.messageComments.push(data.messageComment);
					$scope.users[$scope.loggedUser.id] = $scope.loggedUser;
					resetComment();
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar a resposta.');
				}
			});	
	}
	
	var resetComment = function() {
		$scope.messageComment = {
			  id: 0
			, messageTopicId: $scope.messageTopic.id
			, message: ''
			, attachmentIds: []
		}

		$scope.attachments = [];
		
		resetAddAttachment();
}
	
	var reset = function() {
		
		$scope.messageTopic = { id: 0
							, fromEntityId: 0
							, fromUserGroupId: 0
							, toEntityProfile: '0'
							, toEntityId: 0
							, toUserGroupId: 0
							, title: ''
							, message: ''
							, attachmentIds: []}
		
		$scope.messageTopic.toEntityId = 0;
		$scope.toEntities = [];
		
		$scope.messageTopic.toUserGroupId = 0;
		$scope.toUserGroups = [];
		
		$scope.attachments = [];
		
		//set EntityId from first group, as all groups have same EntityId
		if ($scope.fromUserGroups.length > 0) {
			$scope.messageTopic.fromEntityId = $scope.fromUserGroups[0].entityId;
		}
		
		//auto select GroupId if only 1 user group
		if ($scope.fromUserGroups.length == 1) {
    		$scope.messageTopic.fromUserGroupId = $scope.fromUserGroups[0].id;
    	}
		
		//clear validation
		resetValidation();	
		
		resetAddAttachment();
	}
	
	var loadToEntities = function() {
		
		var configToEntities = {
				  method: 'GET',
				  url: '/ws/messages/toEntities/' + $scope.messageTopic.toEntityProfile,
				  unique: false,
				  requestId: 'toEntities-load'
				};
		
		httpSvc(configToEntities)
			.success(function(data){
				if (data.returnMessage.code > 0) {
			    	$scope.toEntities = data.entities;
				} else {
					$scope.toEntities = [{id: 0, name: 'Erro ao carregar!'}];
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os destinatários!');
			});
	}
	
	var loadToUserGroups = function() {
		
		var configToUserGroups = {
				  method: 'GET',
				  url: '/ws/messages/toUserGroups/' + $scope.messageTopic.toEntityProfile + '/' + $scope.messageTopic.toEntityId,
				  unique: false,
				  requestId: 'toUserGroups-load'
				};

			httpSvc(configToUserGroups)
				.success(function(data){
					if (data.returnMessage.code > 0) {
				    	$scope.toUserGroups = data.userGroups;
				    	//Auto select Entity ID for the scenarios where there is no manual entity selection (FRANCHISEE -> FRANCHISOR)
				    	if ($scope.messageTopic.toEntityId == 0) {
				    		$scope.messageTopic.toEntityId = $scope.toUserGroups[0].entityId;
				    	}
				    	if ($scope.toUserGroups.length == 0) {
				    		$scope.toUserGroups = [{id: 0, name: 'Não existem áreas de destino cadastradas para receber mensagens!'}];
				    	}
				    	if ($scope.toUserGroups.length == 1) {
				    		$scope.messageTopic.toUserGroupId = $scope.toUserGroups[0].id;
				    	}
					} else {
						$scope.toUserGroups = [{id: 0, name: 'Erro ao carregar!'}];
					}
				})
				.error(function(data){
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os perfis dos destinatários!');
				});
	}
	
	var loadMessageComments = function() {
		
		var configMessageComments = {
				  method: 'GET',
				  url: '/ws/messages/' + $scope.messageTopic.id + '/comments',
				  unique: false,
				  requestId: 'messageComments-load'
				};

			httpSvc(configMessageComments)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						$scope.messageComments = data.messageComments;
						$scope.messageComments.forEach(function(comment) {
							comment.open = false;
						});
						$scope.users = data.users;
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
					}
				})
				.error(function(data){
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar as respostas!');
				});
	}
	
	$scope.addAttachment = function() {
		document.getElementById("inputFile").value = "";
		document.getElementById("inputFile2").value = "";
		$scope.uploadFileField = true;
	}
	
	$scope.cancelAddAttachment = function() {
		resetAddAttachment();
	}
	
	var resetAddAttachment = function() {
		$scope.uploadFileField = false;
	}
	
	$scope.deleteAttachment = function(item) {
		var index = $scope.attachments.indexOf(item);
		$scope.attachments.splice(index, 1);
	}
	
	$scope.doAddAttachment = function(e) {
		
		if (e.files.length > 0) {
			
			var attachmentData = {
				name: e.files[0].name,
				progress: 0,
				progressVisible: true,
				error: "",
				id: 0
			}
			$scope.attachments.push(attachmentData);
			resetAddAttachment();
			
			var config = {
			  method: 'GET' ,
			  url: '/ws/messages/attachment/url',
			  unique: false,
			  requestId: 'messageAttachment-getUploadUrl'
			};
			
			httpSvc(config)
				.success(function(data){
					var fd = new FormData();
					fd.append("uploadedFile", $scope.file);
					
					var xhr = new XMLHttpRequest();
					xhr.upload.addEventListener("progress", function (evt) {uploadProgress(evt, attachmentData);}, false);
			        xhr.addEventListener(       "load",     function (evt) {uploadComplete(evt, attachmentData);}, false);
			        xhr.addEventListener(       "error",    function (evt) {uploadFailed(evt,   attachmentData);}, false);
			        xhr.addEventListener(       "abort",    function (evt) {uploadCanceled(evt, attachmentData);}, false);
			        xhr.open("POST", data.url);
			        xhr.setRequestHeader('Accept', 'application/json');
			        xhr.send(fd);
				});
		}
	}
	
	var uploadProgress = function(evt, attachment) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
            	attachment.progress = Math.round(evt.loaded * 100 / evt.total)
            } else {
            	attachment.progress = 'unable to compute'
            }
        });
    }
	
	var uploadComplete = function(evt, attachment) {
    	$scope.$apply(function(){
    		attachment.progressVisible = false;
    		if (evt.target.status == 413) {
    			attachment.error = "Tamanho máximo (50 MB) excedido";
    		} else {
    			var data = JSON.parse(evt.target.response);
    			if (data.returnMessage.code > 0) {
    				attachment.id = data.attachment.id;
    			} else {
    				attachment.error = data.returnMessage.message;
    			}
    		}	    		
        });
    }
	
	var uploadFailed = function(evt, attachment) {
		$scope.$apply(function(){
    		attachment.progressVisible = false;
  			attachment.error = "Erro ao carregar o arquivo";
        });
    }
	
	var uploadCanceled = function(evt, attachment) {
		$scope.$apply(function(){
    		attachment.progressVisible = false;
  			attachment.error = "Upload cancelado";
        });
    }
	
	$scope.changeToEntityProfile = function() {
		
		if ($scope.messageTopic.toEntityProfile == null) {
			$scope.messageTopic.toEntityProfile = '0';
		}
		
		$scope.messageTopic.toEntityId = 0;
		$scope.toEntities = [];
		
		$scope.messageTopic.toUserGroupId = 0;
		$scope.toUserGroups = [];
		
		if ($scope.messageTopic.toEntityProfile != '0') {
			
			if ($scope.messageTopic.toEntityProfile == 'ADMINISTRATOR') {
				loadToUserGroups();
			} else if ($scope.messageTopic.toEntityProfile == 'FRANCHISEE') {
				loadToEntities();
			} else if ($scope.messageTopic.toEntityProfile == 'FRANCHISOR') {
				if ($scope.loggedUser.selectedRole == 'FRANCHISEE') {
					loadToUserGroups();
				} else if ($scope.loggedUser.selectedRole == 'FRANCHISOR') {
					$scope.messageTopic.toEntityId = $scope.messageTopic.fromEntityId;
					loadToUserGroups();
				} else {
					loadToEntities();
				}			
			} else if ($scope.messageTopic.toEntityProfile == 'SUPPLIER') {
				loadToEntities();
			}
		}
		
	}
	
	$scope.changeToEntity = function() {
		
		if ($scope.messageTopic.toEntityId == null) {
			$scope.messageTopic.toEntityId = 0;
		}
		
		$scope.messageTopic.toUserGroupId = 0;
		$scope.toUserGroups = [];
		
		if ($scope.messageTopic.toEntityId != 0) {
			
			if ($scope.messageTopic.toEntityProfile == 'ADMINISTRATOR') {
				//noting to do
			} else if ($scope.messageTopic.toEntityProfile == 'FRANCHISEE') {
				loadToUserGroups();
			} else if ($scope.messageTopic.toEntityProfile == 'FRANCHISOR') {
				loadToUserGroups();
			} else if ($scope.messageTopic.toEntityProfile == 'SUPPLIER') {
				loadToUserGroups();
			}
		}	
	}
	
	$scope.changeToUserGroup = function() {
		
		if ($scope.messageTopic.toUserGroupId == null) {
			$scope.messageTopic.toUserGroupId = 0;
		}
		
	}
	
	$scope.changeFromUserGroup = function() {
		
		if ($scope.messageTopic.fromUserGroupId == null) {
			$scope.messageTopic.fromUserGroupId = 0;
		}
		
	}
	
	$scope.new = function() {
		reset();
	}
	
	$scope.view = function(item){
		$scope.messageTopic = item;
		loadMessageComments();
		resetComment();
		resetAddNewLabel();
	}

	$scope.filteredUserLabels = function() {
		if ($scope.messageTopic && $scope.messageTopic.labels) {
			return $scope.userLabels.filter(function(userLabel, index, arr){
				return !$scope.messageTopic.labels.includes(userLabel.id);
			});
		} else {
			return [];
		}		
	}

	$scope.removeLabel = function(labelId) {
		var config = {
			method: 'DELETE',
			url: '/ws/messages/messageTopic/' + $scope.messageTopic.id + '/label/' + labelId,
			unique: true,
			requestId: 'messageTopicDeleteLabel-save'
			};

		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.messageTopic.labels = $scope.messageTopic.labels.filter(function(id, index, arr){
						return id != labelId;
					});
		
					if (labelId == $routeParams.label) {
						$scope.messageTopics = $scope.messageTopics.filter(function(messageTopic, index, arr){
							return messageTopic.id != $scope.messageTopic.id;
						});
					}
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao remover o marcador.');
				}
			});
	}

	$scope.archive = function() {

		var config = {
			method: 'POST',
			url: '/ws/messages/messageTopic/archive',
			data: {
				messageTopicId: $scope.messageTopic.id
			},
			unique: true,
			requestId: 'messageTopicArchive-save'
		  };

		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {

					$scope.messageTopic.labels = $scope.messageTopic.labels.filter(function(id, index, arr){
						return id != LABEL_INBOX;
					});
		
					if ($routeParams.label == LABEL_INBOX) {
						$scope.messageTopics = $scope.messageTopics.filter(function(messageTopic, index, arr){
							return messageTopic.id != $scope.messageTopic.id;
						});
					}

					$('#viewMessageTopicModal').modal('hide');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao arquivar a mensagem.');
				}
			});	
	}

	$scope.moveTo = function(labelId) {

		var config = {
			method: 'POST',
			url: '/ws/messages/messageTopic/move',
			data: {
				messageTopicId: $scope.messageTopic.id,
				sourceLabelId: $routeParams.label,
				targetLabelId: labelId
			},
			unique: true,
			requestId: 'messageTopicMove-save'
		  };

		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {

					$scope.messageTopic.labels = $scope.messageTopic.labels.filter(function(id, index, arr){
						return id != $routeParams.label;
					});
					$scope.messageTopic.labels.push(labelId);
		
					if ($routeParams.label != LABEL_ALL) {
						$scope.messageTopics = $scope.messageTopics.filter(function(messageTopic, index, arr){
							return messageTopic.id != $scope.messageTopic.id;
						});
					}					

					$('#viewMessageTopicModal').modal('hide');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao mover a mensagem.');
				}
			});
	}

	$scope.moveToNewLabel = function() {
		$scope.newLabelFormMove = true;
		$scope.newLabelForm = true;
	}

	$scope.addLabel = function(labelId) {
		
		var config = {
			method: 'POST',
			url: '/ws/messages/messageTopic/label',
			data: {
				messageTopicId: $scope.messageTopic.id,
				messageTopicLabelId: labelId
			},
			unique: true,
			requestId: 'messageTopicAddLabel-save'
		  };

		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.messageTopic.labels.push(labelId);
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao adicionar o marcador.');
				}
			});
	}

	$scope.addNewLabel = function() {
		resetAddNewLabel();
		$scope.newLabelFormMove = false;
		$scope.newLabelForm = true;
	}

	var resetAddNewLabel = function() {
		$scope.newLabelName = "";
		$scope.newLabelForm = false;
	}

	$scope.cancelAddNewLabel = function() {
		resetAddNewLabel();
	}

	$scope.saveNewLabel = function() {

		var config = {
			method: 'POST',
			url: '/ws/messages/messageTopicLabel',
			data: {
				id: 0,
				name: $scope.newLabelName
			},
			unique: true,
			requestId: 'messageTopicLabel-save'
		  };

		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.userLabels.push(data.messageTopicLabel);
					$scope.userLabelsMap[data.messageTopicLabel.id] = data.messageTopicLabel.name;

					$scope.addLabel(data.messageTopicLabel.id);

					//update labels in the menu
					$rootScope.updateUserMessageTopicLabels();

					if ($scope.newLabelFormMove) {
						$('#viewMessageTopicModal').modal('hide');
					}
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar o marcador.');
				}
			});

		$scope.newLabelForm = false;
	}
	
	$scope.searchTopics = function() {		
		$scope.page = 1;
		$scope.fetchMessageTopics();		
	}
	
	$scope.previousTopics = function() {		
		$scope.page -= 1;
		$scope.fetchMessageTopics();		
	}
	
	$scope.nextTopics = function() {		
		$scope.page += 1;
		$scope.fetchMessageTopics();	
	}
	
	$scope.fetchMessageTopics = function() {
		
		LoadingUtil.show($("#messageTopicsLoad"));
		
		if ($scope.userGroupIdFilter == null) {
			$scope.userGroupIdFilter = 0;
		}
		
		if ($scope.toEntityProfileFilter == null) {
			$scope.toEntityProfileFilter = 0;
		}
		
		if ($scope.toEntityIdFilter == null) {
			$scope.toEntityIdFilter = 0;
		}
		
		if ($scope.toUserGroupIdFilter == null) {
			$scope.toUserGroupIdFilter = 0;
		}
		
		var query = "?label=" + $routeParams.label;
		if ($scope.filterType == 'FROM') {
			query += "&type=FROM";
			if ($scope.toEntityProfileFilter != '0') {
				query += "&fromEntityProfile=" + $scope.toEntityProfileFilter;
			}
			if ($scope.toEntityIdFilter != '0') {
				query += "&fromEntityId=" + $scope.toEntityIdFilter;
			}
			if ($scope.toUserGroupIdFilter != '0') {
				query += "&fromUserGroupId=" + $scope.toUserGroupIdFilter;
			}
		} else if ($scope.filterType == 'TO') {
			query += "&type=TO";
			if ($scope.toEntityProfileFilter != '0') {
				query += "&toEntityProfile=" + $scope.toEntityProfileFilter;
			}
			if ($scope.toEntityIdFilter != '0') {
				query += "&toEntityId=" + $scope.toEntityIdFilter;
			}
			if ($scope.toUserGroupIdFilter != '0') {
				query += "&toUserGroupId=" + $scope.toUserGroupIdFilter;
			}
		}
		
		if ($scope.titleFilter.trim() != '') {
			query += "&title=" + encodeURI($scope.titleFilter);
		}
		
		//Load messages		
		var configMessages = {
			  method: 'GET',
			  url: '/ws/messages/userGroupId/'+$scope.userGroupIdFilter+'/paged/'+$scope.page+'/pageSize/'+$scope.pageSize+query,
			  unique: false,
			  requestId: 'messageTopics-load'
			};

		httpSvc(configMessages)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					
					$scope.messageTopics = data.messageTopics;
					$scope.nextEnabled = data.hasMore;
					
					$scope.messageTopicsUnreadNotifications = data.messageTopicsUnreadNotifications;
					$scope.entities = data.entities;
					$scope.userGroups = data.userGroups;
					
					Object.keys($scope.messageTopicsUnreadNotifications).forEach(function(key) {
						$scope.messageTopicsUnreadNotifications[key].forEach(function(notification) {
							$scope.messageCommentsUnreadNotifications[notification.referenceId2] = notification;
						});
					});
							
					LoadingUtil.hide($("#messageTopicsLoad"));
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.message + data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao buscar as mensagens.');
				}
			});	
		
	}
	
	$scope.onViewModalClose = function() {
		
		if ($scope.messageTopicsUnreadNotifications[$scope.messageTopic.id]) {
			
			var notificationIds = [];
			$scope.messageTopicsUnreadNotifications[$scope.messageTopic.id].forEach(function(notification) {
				notificationIds.push(notification.id);
			});

			var config = {
		  			  method: 'PUT',
		  			  url: '/ws/messages/markAsRead',
		  			  data: notificationIds,
		  			  unique: true,
		  			  requestId: 'messageTopics-markAsRead'
		  			};
			
				httpSvc(config)
					.success(function(data){
						if (data.returnMessage.code > 0) {
							
							//update unread messages badge in the menu
							$rootScope.updateNewMessageCount();
							
							//CleanUp notifications
							$scope.messageTopicsUnreadNotifications[$scope.messageTopic.id].forEach(function(notification) {
								delete $scope.messageCommentsUnreadNotifications[notification.referenceId2];
							});
							delete $scope.messageTopicsUnreadNotifications[$scope.messageTopic.id];
							
						} 
					});
		}
	}
	
    $scope.checkRead = function(item) {
    	
    	item.open = !item.open;
    	
    	if (item.open) {
    		item.fromNotifications = [];
    		item.toNotifications = [];
    		item.notificationUsers = [];
    		
    		var config = {
	  			  method: 'GET',
	  			  url: '/ws/messages/comment/'+item.id+'/notifications',
	  			  unique: true,
	  			  requestId: 'messageComment-getNotifications'
	  			};
		
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						
						data.notifications.forEach(function(notification) {
							if (notification.userId == $scope.loggedUser.id) {
								//discard, as we do not want to show the own user
							} else {
								if (notification.userGroupId == null) {
									//old notifications do not have userGroupId information saved
									//split can be done by entity profile because it was always different
									if (notification.entityProfile == $scope.messageTopic.fromEntityProfile) {
										item.fromNotifications.push(notification);
									} else if (notification.entityProfile == $scope.messageTopic.toEntityProfile) {
										item.toNotifications.push(notification);
									}
								} else {
									// new notifications have this userGroupId saved
									// split needs to be done by group, because profiles can be the same
									if (notification.userGroupId == $scope.messageTopic.fromUserGroupId) {
										item.fromNotifications.push(notification);
									} else if (notification.userGroupId == $scope.messageTopic.toUserGroupId) {
										item.toNotifications.push(notification);
									}
								}
								
							}
								
						});
						
						item.notificationUsers = data.users;
					} 
				});
    	}
    }
    
    var resetFilters = function() {
    	$scope.userGroupIdFilter = '0';
		$scope.toEntityProfileFilter = '0';
        $scope.toEntityIdFilter = '0';
        $scope.toEntitiesFilter = [];
        $scope.toUserGroupIdFilter = '0';
        $scope.toUserGroupsFilter = [];
        $scope.titleFilter = "";
    }
    
    $scope.toggleAdvancedFilter = function() {	 
        $scope.advancedFilter = !$scope.advancedFilter;
        
        if ($scope.advancedFilter) {
        	$scope.filterType = 'TO';
        	resetFilters();
        } else {
        	$scope.filterType = 'none';
        	resetFilters();
        	$scope.searchTopics();
        }
    	
    }
    
    $scope.changeToEntityProfileFilter = function() {
    	
    	if ($scope.toEntityProfileFilter == null) {
    		$scope.toEntityProfileFilter = '0';
        }
        $scope.toEntityIdFilter = '0';
        $scope.toEntitiesFilter = [];    
        $scope.toUserGroupIdFilter = '0';
        $scope.toUserGroupsFilter = [];
     
        if ($scope.toEntityProfileFilter != '0') {      
          if ($scope.toEntityProfileFilter == 'ADMINISTRATOR') {
            loadToUserGroupsFilter();
          } else if ($scope.toEntityProfileFilter == 'FRANCHISEE') {
            loadToEntitiesFilter();
          } else if ($scope.toEntityProfileFilter == 'FRANCHISOR') {
            if ($scope.loggedUser.selectedRole == 'FRANCHISEE') {
              loadToUserGroupsFilter();
            } else if ($scope.loggedUser.selectedRole == 'FRANCHISOR') {
              $scope.toEntityIdFilter = $scope.loggedUserEntity.id;
              loadToUserGroupsFilter();
            } else {
              loadToEntitiesFilter();
            }      
          } else if ($scope.toEntityProfileFilter == 'SUPPLIER') {
            loadToEntitiesFilter();
          }
        }    
      }
     
	$scope.changeToEntityFilter = function() {
        if ($scope.toEntityIdFilter == null) {
          $scope.toEntityIdFilter = 0;
        }
        $scope.toUserGroupIdFilter = '0';
        $scope.toUserGroupsFilter = [];
     
        if ($scope.toEntityIdFilter != '0') {
          if ($scope.toEntityProfileFilter == 'ADMINISTRATOR') {
            //noting to do
          } else if ($scope.toEntityProfileFilter == 'FRANCHISEE') {
            loadToUserGroupsFilter();
          } else if ($scope.toEntityProfileFilter == 'FRANCHISOR') {
            loadToUserGroupsFilter();
          } else if ($scope.toEntityProfileFilter == 'SUPPLIER') {
            loadToUserGroupsFilter();
          }
        }  
      }

	  $scope.changeToUserGroupFilter = function() {
		    if ($scope.toUserGroupIdFilter == null) {
		      $scope.toUserGroupIdFilter = 0;
		    }
		  }
	  
	  var loadToEntitiesFilter = function() {
		    var configToEntities = {
		          method: 'GET',
		          url: '/ws/messages/toEntities/' + $scope.toEntityProfileFilter,
		          unique: false,
		          requestId: 'toEntities-load'
		        };
		 
		    httpSvc(configToEntities)
		      .success(function(data){
		        if (data.returnMessage.code > 0) {
		            $scope.toEntitiesFilter = data.entities;
		        } else {
		          $scope.toEntitiesFilter = [{id: 0, name: 'Erro ao carregar!'}];
		        }
		      })
		      .error(function(data){
		        NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os destinatários!');
		      });
		  }

	  var loadToUserGroupsFilter = function() {
		    var configToUserGroups = {
		          method: 'GET',
		          url: '/ws/messages/toUserGroups/' + $scope.toEntityProfileFilter + '/' + $scope.toEntityIdFilter,
		          unique: false,
		          requestId: 'toUserGroups-load'
		        };
		 
		      httpSvc(configToUserGroups)
		        .success(function(data){
		          if (data.returnMessage.code > 0) {
		              $scope.toUserGroupsFilter = data.userGroups;
		              //Auto select Entity ID for the scenarios where there is no manual entity selection (FRANCHISEE -> FRANCHISOR)
		              if ($scope.toEntityIdFilter == 0) {
		                $scope.toEntityIdFilter = $scope.toUserGroupsFilter[0].entityId;
		              }
		              if ($scope.toUserGroupsFilter.length == 0) {
		                $scope.toUserGroupsFilter = [{id: 0, name: 'Não existem áreas de destino cadastradas para receber mensagens!'}];
		              }
		              if ($scope.toUserGroupsFilter.length == 1) {
		                $scope.toUserGroupIdFilter = $scope.toUserGroupsFilter[0].id;
		              }
		          } else {
		            $scope.toUserGroupsFilter = [{id: 0, name: 'Erro ao carregar!'}];
		          }
		        })
		        .error(function(data){
		          NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os perfis dos destinatários!');
		        });
		  }
		 
	var init = function() {
		
		$scope.page = 1;
		$scope.pageSize = 10;
		$scope.messageTopics = [];
		$scope.toEntities = [];
		$scope.toUserGroups = [];
		$scope.users = [];
		$scope.messageCommentsUnreadNotifications = {};
		$scope.userGroupIdFilter = 0;
		$scope.userLabels = [];
		$scope.userLabelsMap = {};
		
		$scope.advancedFilter = false;
		$scope.filterType = "none";
    	resetFilters();
	 	
		$('#viewMessageTopicModal').on('hidden.bs.modal', $scope.onViewModalClose);
		
		$scope.searchTopics();
		
		//Load logged user profile
		var configUser = {
				  method: 'GET',
				  url: '/ws/users/logged',
				  unique: false,
				  requestId: 'user-load'
				};

		httpSvc(configUser)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.loggedUser = data.user;
					$scope.fromUserGroups = data.userGroups.filter(userGroup => userGroup.receiveMessage == true);
					$scope.loggedUserEntity = data.entity;
				} else {
					$scope.fromGroups = [{id: 0, name: 'Erro ao carregar!'}];
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar o perfil do usuário!');
			});
		
		//Load Entity Profiles
		var configToEntityProfiles = {
				  method: 'GET',
				  url: '/ws/messages/toProfileTypes',
				  unique: false,
				  requestId: 'toProfileTypes-load'
				};

			httpSvc(configToEntityProfiles)
				.success(function(data){
					if (data.returnMessage.code > 0) {
				    	$scope.toProfileTypes = data.enumValues;
					} else {
						$scope.toProfileTypes = [{key: '0', value: 'Erro ao carregar!'}];
					}
				})
				.error(function(data){
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os tipos de perfil do destinatário!');
				});

		//Load user labels
		var configMessageTopicLabels = {
			method: 'GET',
			url: '/ws/messages/messageTopicLabel',
			unique: false,
			requestId: 'messageTopicLabels-load'
		};

		httpSvc(configMessageTopicLabels)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.userLabels = data.messageTopicLabels;
					$scope.userLabels.forEach(function(entry, index, array) {
						$scope.userLabelsMap[entry.id] = entry.name;
					});
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os marcadores!');
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os marcadores!');
			});
	}
	
	init();
	
});