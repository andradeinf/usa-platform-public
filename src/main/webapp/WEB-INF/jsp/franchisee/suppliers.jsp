<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- List of Existing Suppliers -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-truck"></i> <c:out value="${domainConfiguration.labels['SUPPLIERS']}">Fornecedores</c:out></h3>
</div>
<div class="panel panel-default panel-with-group">
    <div class="panel-body panel-filter" >
		<div class="clearfix">
			<span class="pull-right"><a class="filter" href ng-click="toggleFilter()"><i class="fa fa-search"></i> Filtrar</a></span>
		</div>
    	<div class="row" ng-show="showFilter">
    		<label class="col-md-3 view-form-label" for="filter">Filtrar: </label>
    		<div class="col-md-6"><input type="text" ng-model="supplierFilter" class="form-control" id="filter" placeholder="Digite um texto para filtrar"></div>
    	</div>
    </div>
    <div class="panel-body">
    	<uib-accordion close-others="false">
			<div uib-accordion-group is-open="stateMap[stateId].open" ng-repeat="(stateId, suppliers) in supplierList | filter:supplierFilter | groupBy: 'stateId'">
				<uib-accordion-heading>
					<i class="fa" ng-class="{'fa-angle-up': stateMap[stateId].open, 'fa-angle-down': !stateMap[stateId].open}"></i> {{stateMap[stateId].name}}
				</uib-accordion-heading>
				<div class="panel-list-group">
					<ul class="list-group">
						<li class="list-group-item" ng-repeat="item in suppliers | orderBy:'name'">
							<div class="row list-group-item-row">	
								<div class="col-sm-3 list-group-item-column">
									<div class="thumbnail">
										<img class="img-responsive" ng-src="{{item.thumbnail}}" alt="" />
					    			</div>
					    		</div>
								<div class="col-sm-9 list-group-item-column">
									<h4 class='list-group-item-title'>
			                            <strong>{{item.name}}</strong>
			                            <small>{{item.segment}}</small>
			                        </h4>
			                        <div class="container-fluid">
										<div class="row">
											<label class="col-md-2 view-form-label">Contato:</label>
										    <p class="col-md-5 view-form-field">{{item.contactName}}</p>
											<label class="col-md-2 view-form-label">Telefone:</label>
										    <p class="col-md-3 view-form-field">{{item.contactPhone}}</p>
										</div>
										<div class="row">
											<label class="col-md-2 view-form-label">E-mail:</label>
										    <p class="col-md-5 view-form-field">{{item.contactEmail}}</p>
											<label class="col-md-2 view-form-label">Cidade:</label>
										    <p class="col-md-3 view-form-field">{{item.city.name}}</p>
				                        </div>
				                        <div class="row">
											<label class="col-md-2 view-form-label">Endereço:</label>
										    <p class="col-md-10 view-form-field">{{item.address}}</p>
				                        </div>
				                        <div class="row">
											<label class="col-md-2 view-form-label">Conte com ele para:</label>
											<span class="col-md-10 view-form-field view-form-field-double" ta-bind="text" ng-model="item.description"></span>
				                        </div>
				                        <div class="row" ng-if="item.qualityCoun + item.deliveryCount + item.priceCount + item.paymentConditionCount > 0">
											<label class="col-md-2 view-form-label">Avaliação:</label>
											<div class="col-md-10">
												<div class="center-block review-request">
													<div class="row" ng-if="item.qualityCount > 0">
														<div class="col-md-6 review-request-label">Qualidade do Produto</div>
														<div class="col-md-6 review-request-rate">
															<i class="fa" ng-class="rateClass(item.qualityRate, 1)"></i>
															<i class="fa" ng-class="rateClass(item.qualityRate, 2)"></i>
															<i class="fa" ng-class="rateClass(item.qualityRate, 3)"></i>
															<i class="fa" ng-class="rateClass(item.qualityRate, 4)"></i>
															<i class="fa" ng-class="rateClass(item.qualityRate, 5)"></i>
															&nbsp;({{item.qualityRate | number:1}})
														</div>
													</div>
													<div class="row" ng-if="item.deliveryCount > 0">
														<div class="col-md-6 review-request-label"> Qualidade da Entrega</div>
														<div class="col-md-6 review-request-rate">
															<i class="fa" ng-class="rateClass(item.deliveryRate, 1)"></i>
															<i class="fa" ng-class="rateClass(item.deliveryRate, 2)"></i>
															<i class="fa" ng-class="rateClass(item.deliveryRate, 3)"></i>
															<i class="fa" ng-class="rateClass(item.deliveryRate, 4)"></i>
															<i class="fa" ng-class="rateClass(item.deliveryRate, 5)"></i>
															&nbsp;({{item.deliveryRate | number:1}})
														</div>
													</div>
													<div class="row" ng-if="item.priceCount > 0">
														<div class="col-md-6 review-request-label">Preço do Produto</div>
														<div class="col-md-6 review-request-rate">
															<i class="fa" ng-class="rateClass(item.priceRate, 1)"></i>
															<i class="fa" ng-class="rateClass(item.priceRate, 2)"></i>
															<i class="fa" ng-class="rateClass(item.priceRate, 3)"></i>
															<i class="fa" ng-class="rateClass(item.priceRate, 4)"></i>
															<i class="fa" ng-class="rateClass(item.priceRate, 5)"></i>
															&nbsp;({{item.priceRate | number:1}})
														</div>
													</div>
													<div class="row" ng-if="item.paymentConditionCount > 0">
														<div class="col-md-6 review-request-label">Condição de Pagamento</div>
														<div class="col-md-6 review-request-rate">
															<i class="fa" ng-class="rateClass(item.paymentConditionRate, 1)"></i>
															<i class="fa" ng-class="rateClass(item.paymentConditionRate, 2)"></i>
															<i class="fa" ng-class="rateClass(item.paymentConditionRate, 3)"></i>
															<i class="fa" ng-class="rateClass(item.paymentConditionRate, 4)"></i>
															<i class="fa" ng-class="rateClass(item.paymentConditionRate, 5)"></i>
															&nbsp;({{item.paymentConditionRate | number:1}})
														</div>
													</div>
												</div>
											</div>
				                        </div>
				                    </div>
								</div>
							</div>	
						</li>
					</ul>
				</div>
			</div uib-accordion-group>		
		</uib-accordion>
		<p ng-show="noRecordsFound" class="text-info text-center margin-top-15">Não há dados para esta categoria!</p>
    </div>
    <!-- Loading -->
    <div id="list-overlay" class="overlay"></div>
    <div id="list-loading-img" class="loading-img"></div>
    <!-- end loading -->
</div>