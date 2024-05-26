<%@tag description="Generic Logged Page Template" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag import="br.com.usasistemas.usaplatform.model.data.UserProfileData" %>
<%@attribute name="user" required="true" type="br.com.usasistemas.usaplatform.model.data.UserProfileData"%>
<%@attribute name="customhead" fragment="true" %>
<%@attribute name="postscript" fragment="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<t:masterpage>
	<jsp:attribute name="customhead">
	    <jsp:invoke fragment="customhead"/>
	</jsp:attribute>
	<jsp:attribute name="postscript">
		<script src="/vendor/input-mask/jquery.inputmask.js" type="text/javascript"></script>
        <script src="/vendor/input-mask/jquery.inputmask.numeric.extensions.js" type="text/javascript"></script>
        <script src="/vendor/input-mask/jquery.inputmask.date.extensions.js" type="text/javascript"></script>
        <script src="/vendor/input-mask/jquery.inputmask.extensions.js" type="text/javascript"></script>
	
		<jsp:invoke fragment="postscript"/>
	</jsp:attribute>
    <jsp:body>
		<div id="wrapper" class="container">
			<div id="content">
				<!--=== Header ===--> 
		    	<div class="header">
		    		
		    		<!-- Topbar -->
		        	<div class="topbar clearfix">
						<c:if test="${domainConfiguration.featureFlags['LOGO']}">
							<a href="/"><img src="/img/${domainConfiguration.mainLogo}" alt="" class="topbar-logo"></a>
						</c:if>		        		
		            	<ul class="loginbar top-buffer pull-right">
		                    <li>${user.email}</li>  
		                    <li class="topbar-devider"></li>   
		                    <li><i class="fa fa-sign-out"></i> <a href="/login/logout">Sair</a></li>   
		                </ul>                 
			        </div>
			        <!-- End Topbar -->  
		    	</div>
		    	<!--=== End Header ===--> 
		    	
		    	<div id="notificationBar">
					<div id="notificationAreaWrap">
						<div id="notificationArea"></div>
					</div>
				</div>
		    	
		    	<div id="content-body">
					<jsp:doBody/>
				</div>
			</div>
		</div><!--/wrapper-->			
    </jsp:body>
</t:masterpage>