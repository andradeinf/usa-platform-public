<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- List of Existing Supplier Users -->
<div class="panel panel-default">
	<div class="panel-heading">
        <i class="fa fa-user"></i>
        <h3 class="panel-title">
            <c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISORS']}">Franquias</c:out>
        </h3>
    </div>
    <div class="panel-body panel-filter">
    	<div class="row">
    		<label class="col-md-3 view-form-label" for="filter">Filtrar: </label>
    		<div class="col-md-6"><input type="text" ng-model="franchisorFilter" class="form-control" id="filter" placeholder="Digite o nome"></div>
    	</div>
    </div>
	<div class="panel-body">
		<ul class="list-unstyled">
			<li ng-repeat="item in franchisorList | filter:franchisorFilter | orderBy:'name'"><input type="checkbox" ng-model="supplierFranchisorList[item.id]"> {{item.name}} <i ng-if="item.corporateName != ''">({{item.corporateName}})</i></li>
		</ul>
	</div>
	<!-- Loading -->
    <div id="list-overlay" class="overlay"></div>
    <div id="list-loading-img" class="loading-img"></div>
    <!-- end loading -->
    <div class="panel-footer clearfix">
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="save()"><i class="fa fa-save"> Salvar</i></button>
    	</div>
    </div>
</div>