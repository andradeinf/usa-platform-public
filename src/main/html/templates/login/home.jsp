<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:masterpage>
	<jsp:attribute name="customhead">
	    <!-- CSS Page Style -->    
	    <link rel="stylesheet" href="/app/styles/${domainConfiguration.loginCss}">  
	</jsp:attribute>
	
	<jsp:attribute name="postscript">
		<script type="text/javascript">
			var gFranchisorId="${franchisor.id}";
			var gFranchisorImage="${franchisor.imageURL}";
			var gLoginForm="${domainConfiguration.loginForm}";
			var gSiteName="${domainConfiguration.name}";
		</script>
		
		<script src="/app/scripts/AppLogin.min.js" type="text/javascript"></script>
	</jsp:attribute>
	
    <jsp:body>
    	<div class="processing-message" loader>
			<span>Processando</span>
			<img src="/img/spinner_24_primary_2.gif"/>
		</div>

	    <div id="notificationBar">
			<div id="notificationAreaWrap">
				<div id="notificationArea"></div>
			</div>
		</div>
		
		<div ng-view></div>
    </jsp:body>
</t:masterpage>