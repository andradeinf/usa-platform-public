<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<t:masterloggedpage user="${user}">

	<jsp:attribute name="customhead">

	</jsp:attribute>

    <jsp:attribute name="postscript">
    	<script type="text/javascript">
			var gUserId="${user.id}";
			var gSupplierId="${user.supplier.supplierId}";
			var gHasStockControl="${supplierCategory.hasStockControl}";
			var gReceiveMessage="${supplierCategory.receiveMessage}";
			var gSiteName="${domainConfiguration.name}";
		</script>
    
		<script src="/app/scripts/AppSupplier.min.js" type="text/javascript"></script>
		<script src="/app/scripts/AppCommon.min.js" type="text/javascript"></script>
    </jsp:attribute>

    <jsp:body>
    
    		<div class="processing-message" loader>
				<span>Processando</span>
				<img src="/img/spinner_24_primary_2.gif"/>
			</div>
	    	
    		<div class="row">
    			<!--Left Sidebar-->
            	<div class="col-md-3">
            	
	                <!-- Menu -->
	                <t:menu user="${user}" supplier="${supplier}" supplierCategory="${supplierCategory}"/> 
	                
	                <!-- Site update info -->
                	<t:systemUpdateInfo/> 
            	
            	</div>
            	<!--End Left Sidebar-->
    		
    			<!--Right Content-->
    			<div class="col-md-9">
	                <div class="view-body">
	                	<div ng-view></div>
	                </div>	                
	            </div>

    		</div><!--/end row-->

    </jsp:body>
</t:masterloggedpage>