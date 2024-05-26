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
			var gFranchiseeUserId="${user.franchisee.id}";
			var gFranchiseeId="${franchisee.id}";
			var gFranchisorId="${franchisee.franchisorId}";
			var gSiteName="${domainConfiguration.name}";
		</script>

		<script src="/app/scripts/AppFranchisee.min.js" type="text/javascript"></script>
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
        	
        		<!-- FRANCHISEE -->
    			<div class="shadow-wrapper" ng-controller="FranchiseeCtrl">
                    <div class="tag-box box-shadow shadow-effect-2">
						<div ng-show="franchisee.imageURL">
                        	<img class="img-responsive" ng-src="{{franchisee.imageURL}}" alt="">
                        	<hr class="hr-md"/>
                        </div>	
                        <h2>{{franchisee.name}}</h2>
                        <ul class="list-unstyled">
	                        <li ng-if="franchisee.corporateName != ''"><strong><c:out value="${domainConfiguration.labels['FRANCHISEE_CORPORATE_NAME']}">Razão Social</c:out>:</strong> {{franchisee.corporateName}}</li>
	                        <li ng-if="franchisee.fiscalId != ''"><strong><c:out value="${domainConfiguration.labels['FRANCHISEE_FISCAL_ID']}">CNPJ</c:out>:</strong> {{franchisee.fiscalId}}</li>
	                        <li ng-if="franchisee.address != ''"><strong>Endereço:</strong> {{franchisee.address}}</li>
	                        <li ng-if="franchisee.contactName != ''"><strong>Contato:</strong> {{franchisee.contactName}}</li>
	                        <li ng-if="franchisee.contactPhone != ''"><strong>Telefone:</strong> {{franchisee.contactPhone}}</li>
	                        <li ng-if="franchisee.contactEmail != ''"><strong>E-mail:</strong> {{franchisee.contactEmail}}</li>
	                        <li ng-if="franchisee.additionalInformation != ''"><div ta-bind="text" ng-model="franchisee.additionalInformation"></div></li>
	                    </ul>
	                    <div id="franchisee-overlay" class="overlay"></div>
	            		<div id="franchisee-loading-img" class="loading-img"></div>
                    </div>
                </div>
                
                <!-- Menu -->
                <t:menu user="${user}" supplierCategories="${supplierCategories}"/>
                
                <!-- Site update info -->
				<t:systemUpdateInfo/> 
				
				<!-- Radio -->
                <t:radioVinil/>
        	
        	</div>
        	<!--End Left Sidebar-->
		
			<!--Right Content-->
			<div class="col-md-9">
                <div class="view-body">
		            <div ng-view></div>
                </div>
            </div><!--end/ Right Content-->

		</div><!--/end row-->

    </jsp:body>
</t:masterloggedpage>