<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- New Report Modal -->
<div class="modal fade" id="newReportModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Filtro</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
		            <div class="form-group" id="time">
  						<label for="inputInitDate">Período:</label>
  						<div class="row">
  							<div class="col-sm-5">
  								<div class="input-group">
		  							<div class="input-group-addon">De</div>
		  							<input id="inputInitDate" type="text" class="form-control" uib-datepicker-popup="dd/MM/yyyy" ng-model="deliveriesBySupplierReport.initDate" is-open="filter.fromDateOpened" datepicker-options="dateOptions" show-button-bar="False" alt-input-formats="altInputFormats" on-open-focus="False" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
									<span class="input-group-btn">
						            	<button type="button" class="btn btn-default" ng-click="openFromDatePicker()"><i class="glyphicon glyphicon-calendar" style="padding: 3px;"></i></button>
						          	</span>
								</div>
  							</div>
  							<div class="col-sm-5">
  								<div class="input-group">
		  							<div class="input-group-addon">Até</div>
		  							<input id="inputEndDate" type="text" class="form-control" uib-datepicker-popup="dd/MM/yyyy" ng-model="deliveriesBySupplierReport.endDate" is-open="filter.toDateOpened" datepicker-options="dateOptions" show-button-bar="False" alt-input-formats="altInputFormats" on-open-focus="False" data-inputmask='"mask": "d/m/y", "showTooltip": "true"' input-mask-initialize />
									<span class="input-group-btn">
						            	<button type="button" class="btn btn-default" ng-click="openToDatePicker()"><i class="glyphicon glyphicon-calendar" style="padding: 3px;"></i></button>
						          	</span>
								</div>
  							</div>
  						</div>
					</div>
					<div class="form-group" id="supplier">
		                <label for="inputSupplier"><c:out value="${domainConfiguration.labels['SUPPLIER']}">Fornecedor</c:out></label>		
		                <select id="inputSupplier" class="form-control" ng-model="deliveriesBySupplierReport.filterSupplierId" ng-options="unit.id as unit.name for unit in suppliers | orderBy:'name'"></select>     
		            </div>
					<div class="form-group" id="franchisor">
		                <label for="inputFranchisor"><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISOR']}">Franquia</c:out></label>		
		                <select id="inputFranchisor" class="form-control" ng-model="deliveriesBySupplierReport.filterFranchisorId" ng-options="unit.id as unit.name for unit in franchisors | orderBy:'name'"></select>     
		            </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Cancelar</i></button>
    			<button ng-click="save()" class="btn btn-default"><i class="fa fa-save"> Gerar</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- List of Existing Users -->
<div class="panel panel-default">
	<div class="panel-heading">
        <i class="fa fa-file-text"></i>
        <h3 class="panel-title">
            Relatório de Entregas
        </h3>
    </div>
	<div class="panel-body">
		<table class="table table-striped table-bordered history-table">
            <thead>
            	<tr>
                    <th>Data</th>
                    <th><c:out value="${domainConfiguration.labels['SUPPLIER']}">Fornecedor</c:out></th>
                    <th><c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISOR']}">Franquia</c:out></th>
                    <th>Período</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="item in deliveriesBySupplierReportList | orderBy:'date':true">
                    <td>{{item.date | date:'dd/MM/yyyy'}}</td>
                    <td>{{suppliersMap[item.filterSupplierId].name}}</td>
                    <td>{{franchisorsMap[item.filterFranchisorId].name}}</td>
                    <td>De {{item.initDate | date:'dd/MM/yyyy'}} até {{item.endDate | date:'dd/MM/yyyy'}}</td>
                    <td>
                    	<img ng-show="item.status == 'PROCESSING'" src="/img/spinner_24_primary_2.gif">
                    	<span ng-show="item.status != 'COMPLETED'">{{item.statusDescription}}</span>
                    	<a class="btn btn-sm btn-default" ng-show="item.status == 'COMPLETED'" href ng-href="/ws/report/getTimeRangeReport/{{item.id}}" title="Baixar arquivo"><i class="fa fa-download"></i></a>
                    </td>
                </tr>
        	</tbody>
        </table>
        <p class="help-block">Os relatórios ficam disponíveis por 1 mês após a data da geração.</p>
	</div>
	<!-- Loading -->
    <div id="list-overlay" class="overlay"></div>
    <div id="list-loading-img" class="loading-img"></div>
    <!-- end loading -->
    <div class="panel-footer clearfix">
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newReportModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
</div>