<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- List of Existing Documents -->
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-folder"></i> <c:out value="${domainConfiguration.labels['FILES']}">Documentos</c:out></h3>
</div>
<div class="panel panel-default">
	<nav class="documents-path">
		<ol class="breadcrumb">
			<li class="breadcrumb-item" ng-repeat="item in path"><button type="button" class="btn btn-sm btn-light" ng-click="navigate(item)" >{{item.name}}</button></li>
		</ol>
	</nav>
	<table class="table">
		<thead>
			<tr>
				<th style="padding-left: 30px" class="pointer-cursor" ng-click="sort('name')">Nome <i class="fa fa-sort-desc" ng-show="sortColumn == 'name' && !sortOrder"></i><i class="fa fa-sort-asc" ng-show="sortColumn == 'name' && sortOrder"></i></th>
				<th style="width: 150px" class="pointer-cursor" ng-click="sort('date')">Data <i class="fa fa-sort-desc" ng-show="sortColumn == 'date' && !sortOrder"></i><i class="fa fa-sort-asc" ng-show="sortColumn == 'date' && sortOrder"></i></th>
				<th style="width: 100px" class="pointer-cursor" ng-click="sort('size')">Tamanho <i class="fa fa-sort-desc" ng-show="sortColumn == 'size' && !sortOrder"></i><i class="fa fa-sort-asc" ng-show="sortColumn == 'size' && sortOrder"></i></th>
				<th style="width: 130px; padding-left: 20px">Baixar</th>
			</tr>
		</thead>
		<tbody>
			<tr ng-repeat="folder in folders | orderBy:['order','name']">
				<td style="padding-left: 30px" colspan="4" class="pointer-cursor documents-folder" ng-click="openFolder(folder)"><i class="fa fa-folder icon-padding"></i>{{folder.name}}</td>
			</tr>
			<tr ng-repeat="file in files | orderBy:sortColumn:sortOrder">
				<td style="padding-left: 30px"><i class="fa" ng-class="contentTypeClass(file.contentType)"></i><input type="text" class="presentation-input" ng-model="file.name" title="{{file.name}}"/></td>
				<td style="width: 180px">{{file.date | date:'dd/MM/yyyy HH:mm'}}</td>
				<td style="width: 100px; text-align: right">{{formatFileSize(file.size)}}</td>
				<td style="width: 100px; padding-left: 20px">
					<div class="btn-group btn-group-sm" role="group">
						<a class="pull-center btn btn-sm btn-default" href ng-href="/ws/documents/file/{{file.id}}" title="Baixar arquivo" target="_blank"><i class="fa fa-download"></i></a>
					</div>
				</td>
			</tr>
			<tr ng-show="folders.length == 0 && files.length == 0">
				<td colspan="5" style="padding-left: 30px"><i>Não há documentos nesta pasta</i></td>
			</tr>
		</tbody>
	</table>
</div>
<!-- Loading -->
<div id="list-overlay" class="overlay"></div>
<div id="list-loading-img" class="loading-img"></div>
<!-- end loading -->