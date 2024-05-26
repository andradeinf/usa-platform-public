<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row">
    <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
        <form class="login-form">
            <div class="input-group" id="email">
                <span class="input-group-addon"><i class="icon-append fa fa-envelope"></i></span>
                <input type="text" name="email" placeholder="E-mail" class="form-control" ng-model="resetPassword.email">
            </div>                    
            <div class="row">
	            <div class="col-md-10 col-md-offset-1">
	                <button type="button" class="btn btn-default btn-block" ng-click="doResetPassword()">Solicitar</button>
	            </div>
            </div>
        </form>            
    </div>
</div><!--/row-->