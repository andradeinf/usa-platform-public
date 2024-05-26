<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div>
	<div class="container-fluid main-content">
		<div class="row page1">
			<div class="col-lg-4 .col-lg-offset-4">
				<form class="login-form">
		            <div class="login-form-header">            
						<div class="row">
		                	<img class="img-responsive" src="/img/logo_development.png">
		                </div>
		                <div class="row">
		                	<p><b>Conecte-se a sua conta</b></p>
		                </div>
		            </div>
		            <div class="input-group" id="email">
		                <span class="input-group-addon"><i class="icon-append fa fa-envelope"></i></span>
		                <input type="text" name="email" placeholder="E-mail" class="form-control" ng-model="login.email">
		            </div>                    
		            <div class="input-group" id="password">
		                <span class="input-group-addon"><i class="icon-append fa fa-lock"></i></span>
		                <input type="password" name="password" placeholder="Senha" class="form-control" ng-model="login.password">
		            </div>
		            <div class="row">
			            <div class="col-md-10 col-md-offset-1">
			                <button type="button" class="btn btn-default btn-block" ng-click="doLogin()">Entrar</button>
			            </div>
		            </div>
		            <div class="login-form-footer">            
						<div class="row">
		                	<small><a href="#/resetPassword">Esqueci minha senha</a></small>
		                </div>
		            </div>
		        </form> 
			</div>
		</div>
	</div>		
</div>