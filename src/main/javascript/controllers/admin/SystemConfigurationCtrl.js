angular.module('franchisorLogistics').controller('SystemConfigurationCtrl', function ($scope, httpSvc) {
	
	var errorClass = 'has-error';
	
	$scope.new = function(){
		reset();
	}
	
	$scope.save = function(){
		if (validate()) {
		
			var config = {
				  method: ($scope.configuration.id == 0 ? 'POST' : 'PUT') ,
				  url: '/ws/configuration',
				  data: $scope.configuration,
				  unique: true,
				  requestId: 'configuration-createOrUpdate'
				};
				
			httpSvc(config)
				.success(function(data){
					if (data.returnMessage.code > 0) {
						if ($scope.configuration.id == 0) {
							$scope.configurationList.push(data.systemConfiguration);
						}
						$('#newConfiguratinModal').modal('hide');
						NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Dados da configuração salvos com sucesso!');
					} else {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados da configuração: ' + data.returnMessage.message);
					}
				})
				.error(function(data){
					//499 - duplicated request
					if (data.status != 499) {
						NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Erro ao salvar os dados da configuração!');
					}
				});						
		};
	}
	
	$scope.edit = function(item){
		$scope.configuration = item;
	}
	
	$scope.delete = function(item){
		$scope.itemToRemove = item;
		ConfirmationUtil.show($('#notificationArea'), 'Excluir', 'Tem certeza que deseja excluir a configuração?', doDelete, 'Não', 'Sim');
	}
		
	var doDelete = function(){
	
		var config = {
				method: 'DELETE' ,
				url: '/ws/configuration/'+$scope.itemToRemove.id,
				unique: true,
				requestId: 'configuration-delete'
			};
	
		httpSvc(config)
			.success(function(data){
				if (data.returnMessage.code > 0) {
					$scope.configurationList = $scope.configurationList.filter(configuration => configuration.id != $scope.itemToRemove.id);
					NotificationUtil.show($('#notificationArea'), 'success', 'Sucesso!', 'Configuração excluída com sucesso!');
				} else {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir a configuração: ' + data.returnMessage.message);
				}
			})
			.error(function(data){
				//499 - duplicated request
				if (data.status != 499) {
					NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao excluir a configuração!');
				}
			});
	}
	
	var resetValidation = function() {
		$('#notificationArea').html("");
		$('#key').removeClass(errorClass);
		$('#name').removeClass(errorClass);
		$('#type').removeClass(errorClass);
		$('#stringValue').removeClass(errorClass);
		$('#stringHtmlValue').removeClass(errorClass);
		$('#longValue').removeClass(errorClass);
		$('#booleanValue').removeClass(errorClass);
	}
	
	var validate = function() {
		resetValidation();
		var result = true;
		var errorMsg = "";
		
		if ($scope.configuration.name == "") {
			$('#key').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Chave não pode ser vazio</li>";
		}
		
		if ($scope.configuration.name == "") {
			$('#name').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Nome não pode ser vazio</li>";
		}
		
		if ($scope.configuration.type == "0") {
			$('#type').addClass(errorClass);
			result = false;
			errorMsg += "<li>Selecione um tipo para a configuração</li>";
		}
		
		if ($scope.configuration.type == "STRING" && ($scope.configuration.stringValue == "" || $scope.configuration.stringValue == null)) {
			$('#stringValue').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Valor não pode ser vazio</li>";
		}
		
		if ($scope.configuration.type == "HTML_STRING" && ($scope.configuration.stringValue == "" || $scope.configuration.stringValue == null)) {
			$('#stringHtmlValue').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Valor não pode ser vazio</li>";
		}
		
		if ($scope.configuration.type == "LONG" && ($scope.configuration.longValue == "" || $scope.configuration.longValue == null)) {
			$('#longValue').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Valor não pode ser vazio</li>";
		}
		
		if ($scope.configuration.type == "BOOLEAN" && $scope.configuration.booleanValue == null) {
			$('#booleanValue').addClass(errorClass);
			result = false;
			errorMsg += "<li>Campo Valor não pode ser vazio</li>";
		}
		
		if (!result) {
			NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', errorMsg);
		}
		
		return result;
	}
	
	var reset = function() {
		$scope.configuration = { id: 0
					  			, key: ""
					  			, name: ""
					  			, type: "0"
					  			, stringValue: null
					  			, longValue: null
					  			, booleanValue: null}
						 	
		//clear validadtion
		resetValidation();								 	
	}
	
	var init = function() {
		
		$scope.flagSelect = [{key: null, value: 'Selecione...'}, {key: true, value: 'Sim'}, {key: false, value: 'Não'}]
	
		var config = {
			  method: 'GET' ,
			  url: '/ws/configuration',
			  unique: false,
			  requestId: 'configuration-load'
			};
	
		httpSvc(config)
			.success(function(data){
				$scope.configurationList = data.systemConfigurationList;
				$('#list-overlay').remove();
				$('#list-loading-img').remove();
			});
	
		//get configuration types
		var configurationTypesConfig = {
				  method: 'GET',
				  url: '/ws/configuration/types',
				  unique: false,
				  requestId: 'configurationTypes-load'
				};
		
		httpSvc(configurationTypesConfig)
			.success(function(data){
				if (data.returnMessage.code > 0) {
			    	$scope.configurationTypeSelect = data.enumValues;
			    	$scope.configurationTypeSelect.unshift({'key':'0','value':' Selecione ...'})
				} else {
					$scope.configurationTypeSelect = [{'key': '0', 'value': 'Erro ao carregar!'}];
				}
			})
			.error(function(data){
				NotificationUtil.show($('#notificationArea'), 'danger', 'Erro!', 'Problema ao carregar os tipos de configuração!');
			});
		
		reset();
	}
	
	init();
});