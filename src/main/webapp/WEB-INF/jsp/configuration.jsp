<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="page-header">
	<h3 class="panel-title"><i class="fa fa-cog"></i> Meus Dados</h3>
</div>
<div class="panel panel-default">
	<div class="panel-body">
		<form>
			<div class="form-group">
				<label for="inputName">Nome</label>
				<div class="input-group">
                    <span class="input-group-addon"><i class="icon-append fa fa-user"></i></span>
                    <input type="text" id="inputName" class="form-control" ng-model="profile.name" placeholder="Informe o nome">
                </div> 
			</div>
			<div class="form-group">
				<label for="inputEmail">E-mail</label>
				<div class="input-group">
                    <span class="input-group-addon"><i class="icon-append fa fa-envelope"></i></span>
                    <input type="email" id="inputEmail" class="form-control" ng-model="profile.email" placeholder="Informe o email">
                </div> 
			</div>
			<div class="form-group">
				<label for="inputNewPassword">Nova Senha</label>
				<div class="input-group">
                    <span class="input-group-addon"><i class="icon-append fa fa-lock"></i></span>
                    <input type="password" id="inputNewPassword" class="form-control" ng-model="profile.newPassword" placeholder="Informe a nova senha">
                </div> 
			</div>
			<div class="form-group">
				<label for="inputNewPassword2">Repetir Nova Senha</label>
				<div class="input-group">
                    <span class="input-group-addon"><i class="icon-append fa fa-lock"></i></span>
                    <input type="password" id="inputNewPassword2" class="form-control" ng-model="profile.newPassword2" placeholder="Repita a nova senha">
                </div> 
			</div>
			<div class="form-group">
                <button type="submit" class="btn btn-default btn-block" ng-click="save()"><i class="fa fa-save"> Salvar</i></button>
            </div>
		</form>
	</div>
	<!-- Loading -->
    <div id="profile-overlay" class="overlay"></div>
	<div id="profile-loading-img" class="loading-img"></div>
    <!-- end loading -->
</div>
