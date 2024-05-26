<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="panel panel-default">
	<div class="panel-heading">
        <i class="fa fa-exclamation-circle"></i>
        <h3 class="panel-title">Opppsssss</h3>
    </div>
	<div class="panel-body">
		{{errorMessage}}
	</div>
	<!-- Loading -->
	<div id="errorLoad"></div>
    <!-- end loading -->
</div>