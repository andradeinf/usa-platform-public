angular.module('franchisorLogistics').controller('MessageLabelsCtrl', function ($scope, $rootScope, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.save = function(){
		if (validate()) {

			var config = {
				method: ($scope.label.id == 0 ? 'POST' : 'PUT'),
				url: '/ws/messages/messageTopicLabel',
				data: $scope.label,
				unique: true,
				requestId: 'messageTopicLabel-createOrUpdate'
			  };
	
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if ($scope.label.id == 0) {
							$scope.labelList.push(data.messageTopicLabel);
						}
						$('#newMessageLabelModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Marcador salvo com sucesso!');
						
						//update labels in the menu
						$rootScope.updateUserMessageTopicLabels();
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
		};
	}
	
	$scope.edit = function(item){
		$scope.label = item;
	}
	
	var doDelete = function(item){
		
		var config = {
				method: 'DELETE' ,
				url: '/ws/messages/messageTopicLabel/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'messageTopicLabel-delete'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					var index = $scope.labelList.indexOf($scope.itemToRemove);
					$scope.labelList.splice(index, 1);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Marcador excluído com sucesso!');

					//update labels in the menu
					$rootScope.updateUserMessageTopicLabels();
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', data.returnMessage.details);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir o marcador!');
				}
			});
	}

	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir Marcador', 'Tem certeza que deseja excluir o marcador "' + item.name + '"?', doDelete, 'Não', 'Sim');
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#name').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.label.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo 'Nome' não pode ser vazio</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		$scope.label = { id: 0
						, name: ""}
						 	
		//clear validation
		resetValidation();								 	
	}
	
	var init = function() {

		LoadingUtil.show($("#messageTopicLabelsLoad"));

		var configMessageTipocLabels = {
			method: 'GET',
			url: '/ws/messages/messageTopicLabel',
			unique: false,
			requestId: 'messageTopicLabels-load'
		};

		httpSvc(configMessageTipocLabels)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.labelList = data.messageTopicLabels.filter(function(label, index, arr){
						return label.id != 0;
					});

				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os marcadores!');
				}
				LoadingUtil.hide($("#messageTopicLabelsLoad"));
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os marcadores!');
			});
	
		reset();
	}
	
	init();
});