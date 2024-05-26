<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Add File Modal -->
<div class="modal fade" id="newFileModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Selecionar</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
		            <div class="form-group" id="file">
		                <label for="inputFile">Selecione o Arquivo:</label>
		                <input id="inputFile" type="file" class="form-control" file-model="file"/>
		            </div>
		            <div ng-show="progressVisible" class="progress progress-u progress-xxs" id="progress">
		                <div class="progress-bar progress-bar-u" role="progressbar" aria-valuenow="{{progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{progress}}%">
		                </div>
		            </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
	            <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Cancelar</i></button>
        		<button ng-click="uploadCatalog()" class="btn btn-default"><i class="fa fa-save"> Upload</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- Add Image Modal -->
<div class="modal fade" id="addImageModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Selecionar</h4>
	        </div>
	        <div class="modal-body">
				<form role="form">
		            <div class="form-group" id="file">
		                <label for="inputFile">Selecione o Arquivo:</label>
		                <input id="inputFile" type="file" class="form-control" file-model="productFile"/>
		            </div>
		            <div ng-show="progressVisible" class="progress progress-u progress-xxs">
		                <div class="progress-bar progress-bar-u" role="progressbar" aria-valuenow="{{progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{progress}}%">
		                </div>
		            </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
	            <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Cancelar</i></button>
        		<button ng-click="uploadImage()" class="btn btn-default"><i class="fa fa-save"> Upload</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- New Product Modal -->
<div class="modal fade" id="newProductModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel"><c:out value="${domainConfiguration.labels['PRODUCT']}">Produto</c:out></h4>
	        </div>
	        <div class="modal-body">
	        	<form role="form">
					<input ng-model="product.id" type="hidden" value="0"/>
					<div class="form-group" id="productCategory">
		                <label for="inputProductCategory">Categoria</label>
		                <select id="inputProductCategory" class="form-control" ng-model="product.productCategoryId" ng-options="unit.id as unit.name for unit in productCategorySelect | orderBy:'name'"></select>
		            </div>
					<div class="form-group" id="supplier">
		                <label for="inputSupplier"><c:out value="${domainConfiguration.labels['SUPPLIER']}">Fornecedor</c:out></label>
		                <select id="inputSupplier" class="form-control" ng-model="product.supplierId" ng-options="unit.id as unit.name for unit in suppliers | orderBy:'name'"></select>
		            </div>
		            <div class="form-group" id="name">
		                <label for="inputName">Nome</label>
		                <input id="inputName" type="text" class="form-control" ng-model="product.name" placeholder="Informe o nome"/>
		            </div>
		            <div class="form-group text-angular-wrapper" id="description">
		                <label for="inputDescription">Descrição</label>
		                <div text-angular="text-angular" name="htmlcontent" ng-model="product.description"></div>
		            </div>
		            <div class="form-group" id="type">
		                <label for="inputProductType">Tipo</label>
		                <select id="inputProductType" class="form-control" ng-model="product.type" ng-options="unit.key as unit.value for unit in types | orderBy:'value'"></select>
		            </div>
		            <div class="row" ng-show="product.type != '0' && product.type != 'CATALOG'">
	                    <div class="col-xs-6">
		                    <div class="form-group" id="allowChangeUnitPrice">
		                        <input type="checkbox" ng-model="product.allowChangeUnitPrice"> <label for="inputAllowChangeUnitPrice">Permite alterar valor unitário ao atualizar pedido</label>
		                    </div>
		            	</div>
		            	<div class="col-xs-6">
		                    <div class="form-group" id="allowSupplierChangeUnitPrice">
		                        <input type="checkbox" ng-model="product.allowSupplierChangeUnitPrice"> <label for="inputAllowSupplierChangeUnitPrice">Permite fornecedor alterar valor unitário</label>
		                    </div>
		            	</div>
		            </div>
		            <div class="row" ng-show="product.type != '0' && product.type != 'CATALOG'">
		            	<div class="col-xs-6">
		                    <div class="form-group" id="unit">
		                        <label for="inputUnit">Unidade</label>
		                        <input id="inputUnit" type="text" class="form-control" ng-model="product.unit" placeholder="Ex.: unidade"/>
		                    </div>
		            	</div>
		            	<div class="col-xs-6">
		                    <div class="form-group" id="deliveryUnit">
		                        <input type="checkbox" ng-model="product.hasDeliveryUnit" ng-change="changeHasDeliveryUnit()"> <label for="inputDeliveryUnit">Unidade de Entrega</label>
		                        <input id="inputDeliveryUnit" type="text" class="form-control" ng-model="product.deliveryUnit" ng-readonly="!product.hasDeliveryUnit" placeholder="Ex.: Pacote"/>
		                    </div>
		            	</div>
		            </div>
		            <div class="row" ng-show="product.type == 'WITH_STOCK_CONTROL'">
		            	<div class="col-xs-6">
		                    <div class="form-group" id="allowNegativeStock">
		                        <input type="checkbox" ng-model="product.allowNegativeStock"> <label for="inputAllowNegativeStock">Permite estoque negativo</label>
		                    </div>
		            	</div>
		            	<div class="col-xs-6">
		                    <div class="form-group" id="manufactureMinQty">
		                        <input type="checkbox" ng-model="product.hasManufactureMinQty"> <label for="inputManufactureMinQty">Possui quantidade mínima para produção</label>
		                    </div>
		            	</div>
		            </div>
		            <div class="row" ng-show="product.type != '0' && product.type != 'CATALOG'">
		            	<div class="col-xs-6" ng-show="product.type == 'WITH_STOCK_CONTROL'">
							<div class="form-group" id="manufactureTime">
		                        <label for="inputManufactureTime">Prazo de produção</label>
		                        <div class="input-group">
		                        	<span class="input-group-addon">#</span>
		                        	<input id="inputManufactureTime" type="text" class="form-control" ng-model="product.manufactureTime" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
		                        	<span class="input-group-addon"> dias</span>
		                        </div>
		                    </div>
		            	</div>
		            	<div class="col-xs-6">
		                    <div class="form-group" id="deliveryTime">
		                        <label for="inputDeliveryTime">Prazo de preparação para entrega</label>
		                        <div class="input-group">
		                        	<span class="input-group-addon">#</span>
		                        	<input id="inputDeliveryTime" type="text" class="form-control" ng-model="product.deliveryTime" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
		                        	<span class="input-group-addon"> dias</span>
		                        </div>
		                    </div>
		            	</div>
		            </div>
		            <div class="form-group" id="sizeName" ng-show="product.type != '0' && product.type != 'CATALOG'">
		                <label for="inputSizeName">Título para as opções</label>
		                <input id="inputSizeName" type="text" class="form-control" ng-model="product.sizeName" placeholder="tamanho, cor, peso, etc"/>
		            </div>
		            <div class="form-group" id="groupSizes" ng-show="product.type != '0' && product.type != 'CATALOG'">
                    <input type="checkbox" ng-model="product.groupSizes"> <label>Agrupar opções</label>
                </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Cancelar</i></button>
    			<button ng-click="save()" class="btn btn-default"><i class="fa fa-save"> Salvar</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- New Product Size Modal -->
<div class="modal fade" id="newProductSizeModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h4 class="modal-title" id="myModalLabel">Opção</h4>
	        </div>
	        <div class="modal-body">
	        	<form role="form">
					<input ng-model="productSize.id" type="hidden" value="0"/>
					<input ng-model="productSize.productId" type="hidden" value="0"/>
					<div class="row">
		            	<div class="col-xs-12">
		                    <div class="form-group" id="sizeName2">
				                <label for="inputName2">Nome</label>
				                <input id="inputName2" type="text" class="form-control" ng-model="productSize.name" placeholder="Informe um nome para a opção"/>
				            </div>
		            	</div>
		            </div>
		            <div class="row">
		            	<div class="col-xs-12">
		                    <div class="form-group" id="description">
				                <label for="inputDescription">Descrição</label>
				                <input id="inputDescription" type="text" class="form-control" ng-model="productSize.description" placeholder="Informe uma descrição para a opção (opcional)"/>
				            </div>
		            	</div>
		            </div>
		            <div class="row">
		            	<div class="col-xs-12">
		                    <div class="form-group" id="available">
				                <label for="inputAvailable">Status</label>
				                <label class="radio-inline"><input type="radio" name="optradio"><input type="radio" ng-model="productSize.isAvailable" ng-value="true"><span class="text-success">Disponível</span></label>
								<label class="radio-inline"><input type="radio" name="optradio"><input type="radio" ng-model="productSize.isAvailable" ng-value="false"><span class="text-danger">Indisponível</span></label>
				            </div>
		            	</div>
		            </div>
		            <div class="row">
		            	<div class="col-xs-12">
		                    <div class="form-group" id="active">
				                <label for="inputActive">Ativo</label>
				                <label class="radio-inline"><input type="radio" name="optradio"><input type="radio" ng-model="productSize.isActive" ng-value="true"><span class="text-success">Sim</span></label>
								<label class="radio-inline"><input type="radio" name="optradio"><input type="radio" ng-model="productSize.isActive" ng-value="false"><span class="text-danger">Não</span></label>
				            </div>
		            	</div>
		            </div>
		            <div class="row">
		            	<div class="col-xs-12">
		                    <div class="form-group" id="groupName">
				                <label for="inputGroupName">Grupo</label>
				                <input id="inputGroupName" type="text" class="form-control" ng-model="productSize.groupName" placeholder="Informe um nome para o grupo da opção' (opcional)"/>
				            </div>
		            	</div>
		            </div>
		            <div class="row">
		            	<div class="col-xs-6">
		                    <div class="form-group" id="sizeUnitPrice">
		                        <label for="inputUnitPrice">Valor unitário</label>
		                        <div class="input-group">
		                        	<span class="input-group-addon">R$</span>
		                        	<input id="inputUnitPrice" type="text" class="form-control" currency ng-model="productSize.unitPrice"  placeholder="0,0000" onkeypress="return checkCurrencyValue(event);" onblur="javascript:formatCurrencyField(this);"/>
		                        	<span class="input-group-addon"> por {{product.unit}}</span>
		                        </div>
		                    </div>
		            	</div>
		            	<div class="col-xs-6">
							<div class="form-group" id="sizeDeliveryQty" ng-show="product.hasDeliveryUnit">
		                        <label for="inputDeliveryQty">Quantidade por Unidade de Entrega</label>
		                        <div class="input-group">
		                        	<span class="input-group-addon">#</span>
		                        	<input id="inputDeliveryQty" type="text" class="form-control" ng-model="productSize.deliveryQty" ng-readonly="!product.hasDeliveryUnit" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
		                        	<span class="input-group-addon">{{product.unit}} por {{product.deliveryUnit}}</span>
		                        </div>
		                    </div>
		            	</div>
		            </div>
		            <div class="row" ng-show="product.type == 'WITH_STOCK_CONTROL'">
		            	<div class="col-xs-6">
		                    <div class="form-group" id="sizeManufactureMinQty"  ng-show="product.hasManufactureMinQty">
		                        <label for="inputDeliveryQty">Quantidade mínima para produção</label>
		                        <div class="input-group">
		                        	<span class="input-group-addon">#</span>
		                        	<input id="inputManufactureMinQty" type="text" class="form-control" ng-model="productSize.manufactureMinQty" placeholder="0" onkeypress="return checkOnlynumbers(event);"/>
		                        	<span class="input-group-addon">
		                        		<span ng-show="product.hasDeliveryUnit">{{product.deliveryUnit}}</span>
		                        		<span ng-show="!product.hasDeliveryUnit">{{product.unit}}</span>
		                        	</span>
		                        </div>
		                    </div>
		            	</div>
		            	<div class="col-xs-6">
		                    <div class="form-group" ng-show="product.hasDeliveryUnit && product.hasManufactureMinQty">
		                        <label for="inputManufactureMinTotalQty">Quantidade total mínima para produção</label>
		                        <div class="input-group">
		                        	<span class="input-group-addon">#</span>
		                        	<input id="inputManufactureMinTotalQty" type="text" class="form-control" value="{{calculateManufactureMinTotalQty(product, productSize)}}" readonly/>
		                        	<span class="input-group-addon"> {{product.unit}}</span>
		                        </div>
		                    </div>
		            	</div>
		            </div>
		    	</form>
	        </div>
	        <div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"> Cancelar</i></button>
    			<button ng-click="saveSize()" class="btn btn-default"><i class="fa fa-save"> Salvar</i></button>
	        </div>
	    </div>
	</div>
</div><!--/end modal-->

<!-- List of Existing Franchisor Products -->
<div class="panel panel-default panel-with-group">
	<div class="panel-heading">
        <i class="fa fa-tags"></i>
        <h3 class="panel-title">
            <c:out value="${domainConfiguration.labels['PRODUCTS']}">Produtos</c:out> - <c:out value="${domainConfiguration.labels['SUPPLIER_FRANCHISOR']}">Franquia</c:out>
        </h3>
    </div>
    <div class="panel-body panel-body-with-subpanels">
    	<div class="panel panel-default subpanel" ng-repeat="category in productCategories | orderBy:'order'">
    		<div class="panel-heading pointer-cursor" ng-click="toggleCategory(category)">
				<i class="fa" ng-class="{'fa-angle-up': category.open, 'fa-angle-down': !category.open}"></i>
				<h3 class="panel-title">{{category.name}}</h3>
				<i class="fa fa-lock" ng-show="category.restrictions.length > 0" ng-attr-title="{{getRestrictionList(category.restrictions)}}"></i>
			</div>
			<div class="panel-list-group" ng-show="category.open">
	    		<ul class="list-group">
	    			<li class="list-group-item list-group-item-notes list-group-item-warning" ng-show="{{category.notes != ''}}">{{category.notes}}</li>
					<li class="list-group-item text-info text-center" ng-show="category.products.length == 0">Não há produtos nesta categoria!</li>
					<li class="list-group-item" ng-repeat="item in category.products | orderBy:'name'">
						<div class="row list-group-item-row">
							<div class="col-sm-3 list-group-item-column">
		            			<div class="thumbnail zoomer">            				
				    				<img class="img-responsive" ng-src="{{getImage(item)}}" alt="" />
				    				<i class="zoomer-icon add-icon zoom-fa fa fa-camera" ng-click="addFile(item)" data-toggle="modal" data-target="#addImageModal"></i>
				    			</div>
							</div>
							<div class="col-sm-9 list-group-item-column clearfix">
								<h4 class='list-group-item-title'>
		                            <strong>{{item.name}}</strong>
		                        </h4>
		                        <ul class="list-unstyled">
		                        	<li><i class="fa fa-check"></i> <strong>Descrição:</strong> <span ta-bind="text" ng-model="item.description"></span></li>
									<li><i class="fa fa-check"></i> <strong><c:out value="${domainConfiguration.labels['SUPPLIER']}">Fornecedor</c:out>:</strong> {{supplierList[item.supplierId].name}}</li>
									<li ng-show="item.manufactureTime > 0" ng-if="item.type == 'WITH_STOCK_CONTROL'"><i class="fa fa-check"></i> <strong>Prazo de produção:</strong> {{item.manufactureTime}} dias</li>
									<li ng-show="item.deliveryTime > 0" ng-if="item.type != 'CATALOG'"><i class="fa fa-check"></i> <strong>Prazo de preparação para entrega:</strong> {{item.deliveryTime}} dias</li>
									<li ng-show="item.catalogName" ng-if="item.type == 'CATALOG'"><i class="fa fa-check"></i> <strong>Catálogo:</strong> <a href ng-href="/ws/documents/catalog/{{item.id}}" target="_blank" title="Abrir catálogo"><i class="fa" ng-class="contentTypeClass(item.catalogContentType)"></i> {{item.catalogName}}</a></li>
		                        </ul>
							</div>
						</div>
						<div class="row list-group-item-row">
		                    <div class="col-sm-12 list-group-item-column">
								<table class="table table-bordered table-hover size-table" ng-if="item.type != 'CATALOG'">
				                    <thead>
				                        <tr>
				                            <th class="size-table-head">{{item.sizeName}}</th>
				                            <th class="size-table-head">Valor</th>
				                            <th ng-show="item.hasDeliveryUnit" class="size-table-head">Entrega</th>
				                            <th class="size-table-head" style="width: 50px">Ações</th>
				                        </tr>
				                    </thead>
				                    <tbody>
										<tr ng-repeat="size in item.sizes | orderBy:['groupName', 'name']">
				                            <td><i class="fa fa-circle" ng-class="statusClass(size)" title="{{statusTitle(size)}}"></i> <span ng-show="size.groupName != null && size.groupName != ''">{{size.groupName}} - </span>{{size.name}}<span ng-show="size.description != null && size.description != ''"> - {{size.description}}</span></td>
				                            <td>{{size.unitPrice | currency:undefined:4}} / {{item.unit}}</td>
				                            <td ng-show="item.hasDeliveryUnit">{{item.deliveryUnit}} com {{size.deliveryQty}} {{item.unit}}</td>
				                            <td>
						                    	<button class="pull-center btn btn-default btn-sm" ng-click="editSize(item, size)" title="Editar" data-toggle="modal" data-target="#newProductSizeModal"><i class="fa fa-pencil"></i></button>
						                    	<button class="pull-center btn btn-default btn-sm" ng-click="deleteSize(item, size)" title="Excluir"><i class="fa fa-trash-o"></i></button>
						                    </td>
				                        </tr>
				                    </tbody>
			                	</table>
		                        <div class="list-group-item-actions">
			                    	<button class="pull-center btn btn-default" ng-click="edit(item)" title="Editar" data-toggle="modal" data-target="#newProductModal"><i class="fa fa-pencil"></i></button>
			                    	<button class="pull-center btn btn-default" ng-click="delete(item)" title="Excluir"><i class="fa fa-trash-o"></i></button>
			                    	<button class="pull-center btn btn-default" ng-click="duplicate(item)" title="Duplicar" data-toggle="modal" data-target="#newProductModal"><i class="fa fa-copy"></i></button>
			                    	<button class="pull-center btn btn-default" ng-click="newSize(item)"  ng-if="item.type != 'CATALOG'" title="Novo {{item.sizeName}}" data-toggle="modal" data-target="#newProductSizeModal"><i class="fa fa-plus"></i></button>
			                    	<button class="pull-center btn btn-default" ng-click="addFile(item)" ng-if="item.type == 'CATALOG'" title="Carregar Catálogo" data-toggle="modal" data-target="#newFileModal"><i class="fa fa-upload"></i></button>
		                        </div>
							</div>
						</div>
					</li>
	    		</ul>
	    	</div>
    	</div>    	
    </div>
    <div class="panel-footer clearfix">
    	<div class="pull-right">
    		<button class="btn btn-default" ng-click="new()" data-toggle="modal" data-target="#newProductModal"><i class="fa fa-plus"> Novo</i></button>
    	</div>
    </div>
</div>
<div id="products-loadingArea"></div>
