<%@tag description="Generic Page Template" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="customhead" fragment="true" %>
<%@attribute name="postscript" fragment="true" %>
<html>
	<head>
		<title>${domainConfiguration.title}</title>
	
	    <!-- Meta -->
	    <meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    
		<!-- Favicon -->
	    <link rel="shortcut icon" href="/${domainConfiguration.favicon}">
	    
	    <!-- CSS Vendors -->
	    <link rel="stylesheet" href="/vendor/bootstrap/css/bootstrap.min.css">
	    <link rel="stylesheet" href="/vendor/fancybox/source/jquery.fancybox.css">
	    <link rel="stylesheet" href="/vendor/font-awesome/css/font-awesome.min.css">
	    <link rel="stylesheet" href="/vendor/box-shadows/css/box-shadows.css">
	    <link rel='stylesheet' href='/vendor/text-angular/textAngular.css'>
	    <link rel="stylesheet" href="/app/styles/vendors.css" type="text/css" />
	    
	    <!-- CSS Application -->
		<link rel="stylesheet" href="/app/styles/${domainConfiguration.mainCss}" type="text/css" />
		
		<!-- Custom head additions -->
	    <jsp:invoke fragment="customhead"/>
	    
	</head> 
	<body ng-app="franchisorLogistics">
	
		<jsp:doBody/>
		
		<!-- JS Vendors -->           
		<script type="text/javascript" src="/vendor/jquery/jquery-3.1.1.min.js"></script>
		<script type="text/javascript" src="/vendor/jquery/jquery-migrate-1.4.1.min.js"></script>
		<script type="text/javascript" src="/vendor/bootstrap/js/bootstrap.min.js"></script> 
		<script type="text/javascript" src="/vendor/angular/angular.min.js"></script>
		<script type="text/javascript" src="/vendor/angular/angular-animate.min.js"></script>
		<script type="text/javascript" src="/vendor/angular/angular-route.min.js"></script>
		<script type="text/javascript" src="/vendor/angular/angular-locale_pt-br.js"></script>
		<script type="text/javascript" src="/vendor/angular-filter/angular-filter.min.js"></script>
		<script type="text/javascript" src="/vendor/angular-google-analytics/angular-google-analytics.min.js"></script>
		<script type="text/javascript" src="/vendor/angular-toArray/toArrayFilter.js"></script>
		<script type="text/javascript" src='/vendor/text-angular/textAngular-rangy.min.js'></script>
		<script type="text/javascript" src='/vendor/text-angular/textAngular-sanitize.min.js'></script>
		<script type="text/javascript" src='/vendor/text-angular/textAngular.min.js'></script>
		<script type="text/javascript" src="/vendor/fancybox/source/jquery.fancybox.pack.js"></script>
		<script type="text/javascript" src="/vendor/ui-bootstrap/ui-bootstrap-tpls-2.4.0.min.js"></script>
		<script type="text/javascript" src="/vendor/videogular/js/videogular.min.js"></script>
		<script type="text/javascript" src="/vendor/videogular/js/vg-controls.min.js"></script>
		<script type="text/javascript" src="/vendor/jqueryui/jquery-ui.min.js"></script>
		<script type="text/javascript" src="/vendor/angular-ui-sortable/sortable.min.js"></script>

		<!-- YT library and this directive -->  
		<script src="https://www.youtube.com/iframe_api"></script>
		<script type="text/javascript" src="/vendor/angular-youtube-embed/angular-youtube-embed.min.js"></script>
		
		<!-- JS Application -->
		<script src="/app/scripts/AppCore.min.js" type="text/javascript"></script>
		<script src="/app/scripts/AppComponents.min.js" type="text/javascript"></script>
		
		<!-- Custom script -->
		<jsp:invoke fragment="postscript"/>
		
		<!-- Initialize Vendor -->           
		<script src="/app/scripts/AppVendor.min.js" type="text/javascript"></script>
		<script type="text/javascript">
		    jQuery(document).ready(function() {
		        VendorInit.initFancybox(); 
		    });
		</script>	
		
		<!--[if lt IE 9]>
		    <script src="/vendor/legacy-browser/respond.js"></script>
		    <script src="/vendor/legacy-browser/html5shiv.js"></script>
		<![endif]-->
	</body>
</html>