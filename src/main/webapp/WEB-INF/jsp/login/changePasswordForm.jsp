<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row">
    <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
        <form class="login-form">
        	<input type=hidden name=uid value="${uid}">
        	<div class="form-group">
				<label for="inputNewPassword">Nova Senha</label>
                <div class="input-group" id="newPassword">
                    <span class="input-group-addon"><i class="icon-append fa fa-lock"></i></span>
                    <input type="password" name="newPassword" placeholder="Informe a nova senha" class="form-control" ng-model="changePassword.newPassword">
                </div>
            </div> 
            <div class="form-group">
				<label for="inputNewPassword">Repetir Nova Senha</label>
                <div class="input-group" id="newPasswordConfirmation">
                    <span class="input-group-addon"><i class="icon-append fa fa-lock"></i></span>
                    <input type="password" name="newPasswordConfirmation" placeholder="Repita a nova senha" class="form-control" ng-model="changePassword.newPasswordConfirmation">
                </div>
            </div>                   
            <div class="row">
	            <div class="col-md-10 col-md-offset-1">
	                <button type="button" class="btn btn-default btn-block" ng-click="doChangePassword()">Alterar senha</button>
	            </div>
            </div>
        </form>            
    </div>
</div><!--/row-->