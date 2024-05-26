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
			var gFranchisorId="${user.franchisor.franchisorId}";
			var gSiteName="${domainConfiguration.name}";
		</script>
    	
		<script src="/app/scripts/AppFranchisor.min.js" type="text/javascript"></script>
		<script src="/app/scripts/AppCommon.min.js" type="text/javascript"></script>
		
		<script type="text/javascript">
			
		</script>
    </jsp:attribute>

    <jsp:body>
    
		<div class="processing-message" loader>
			<span>Processando</span>
			<img src="/img/spinner_24_primary_2.gif"/>
		</div>
    	
		<div class="row">
			<!--Left Sidebar-->
        	<div class="col-md-3">
        	
        		<!-- FRANCHISOR -->
    			<div class="shadow-wrapper" ng-controller="FranchisorCtrl">
                    <div class="tag-box box-shadow shadow-effect-2">
						<div ng-show="franchisor.imageURL">
                        	<img class="img-responsive" ng-src="{{franchisor.imageURL}}" alt="">
                        	<hr class="hr-md"/>
                        </div>	                        
                        <h2>{{franchisor.name}}</h2>
                        <ul class="list-unstyled">
	                        <li ng-if="franchisor.corporateName != ''"><strong><c:out value="${domainConfiguration.labels['FRANCHISOR_CORPORATE_NAME']}">Razão Social</c:out>:</strong> {{franchisor.corporateName}}</li>
	                        <li ng-if="franchisor.fiscalId != ''"><strong><c:out value="${domainConfiguration.labels['FRANCHISOR_FISCAL_ID']}">CNPJ</c:out>:</strong> {{franchisor.fiscalId}}</li>
	                        <li ng-if="franchisor.address != ''"><strong>Endereço:</strong> {{franchisor.address}}</li>
	                        <li ng-if="franchisor.contactName != ''"><strong>Contato:</strong> {{franchisor.contactName}}</li>
	                        <li ng-if="franchisor.contactPhone != ''"><strong>Telefone:</strong> {{franchisor.contactPhone}}</li>
	                        <li ng-if="franchisor.contactEmail != ''"><strong>E-mail:</strong> {{franchisor.contactEmail}}</li>
	                        <li ng-if="franchisor.additionalInformation != ''"><div ta-bind="text" ng-model="franchisor.additionalInformation"></div></li>
	                    </ul>
	                    <div id="franchisor-overlay" class="overlay"></div>
	            		<div id="franchisor-loading-img" class="loading-img"></div>
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
            </div>

		</div><!--/end row-->

    </jsp:body>
</t:masterloggedpage>