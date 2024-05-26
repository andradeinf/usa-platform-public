<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div>

	<nav class="navbar navbar-default navbar-fixed-top navbar-login">
	  <div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="collapsed navbar-toggle" data-toggle="collapse" data-target="#navbar-login-items" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<img class="img-responsive-vertical" src="/img/logo.png">
		</div>
		<div class="collapse navbar-collapse" id="navbar-login-items">
			<ul class="nav navbar-nav">
				<li><a ng-click="gotoLocation('home')">Home</a></li>
				<li><a href="/login#/home#theSystem" target="_blank">O Sistema</a></li>
				<li><a ng-click="gotoLocation('contact')">Contato</a></li>
			</ul>
		</div>
	  </div>
	</nav>

	<div class="container-fluid main-content">
		<a id="home" class="main-mark"></a>
		<div class="row custom-home-page">
		    <div class="col-md-8">
	        	<div class="custom-home text-center">
	    			<p>Sistema de suporte à gestão dos pontos de venda da Franqueadora</p>
	    			<img class="img-responsive home-logo" ng-src="{{franchisor.imageURL}}" alt="">
	    			<p><span class="home-highlight-small">Acesse o Sistema para Franquias sempre que desejar adquirir um produto homologado,
	    			acompanhar seus pedidos, entre outros processos disponíveis para você.</span></p>
	    		</div>
		    </div>
			<div class="col-md-4">
				<a id="loginForm"></a>
		        <form class="login-form">
		            <div class="login-form-header">            
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
		</div><!--/row-->
	</div>
	<a id="contact" class="main-mark"></a>
	<div class="row pageX">
		<div class="col-xs-12">
			<div class="page-title-bar"><span class="page-title">Contato</span></div>
			<div class="content">
				<div class="row">
					<div class="col-md-4">
						<div class="title">Ficou com dúvida?</div>
						<div class="body">
							<p>Entre em contato conosco!</p>
						</div>
					</div>
					<div class="col-md-8">
						<div class="details">
							<div class="row">
								<div class="col-sm-6">
									<p class="details-title">Endereço:</p>
									<p class="details-info">Rua Dr. Timóteo, 395<br/>Floresta - Porto Alegre - RS - Brasil<br/>Cep: 90570-041</p>
								</div>
								<div class="col-sm-6">
									<p class="details-title">Telefone:</p>
									<p class="details-info">(51) 98227.3091</p>
									<p class="details-title">E-mail:</label>
									<p class="details-info"><a href="mailto:contato@sistemaparafranquias.com.br">contato@sistemaparafranquias.com.br</a></p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	
	<nav class="navbar navbar-default navbar-fixed-bottom navbar-footer hidden-xs hidden-sm">
		<div class="container-fluid">
			<p>UNINDO FRANQUEADORES, FRANQUEADOS E FORNECEDORES</p>
		</div>
	</nav>
</div>
