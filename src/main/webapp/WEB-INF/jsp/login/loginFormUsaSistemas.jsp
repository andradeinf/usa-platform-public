<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div ng-show="!isLogin">
	<nav class="navbar navbar-default navbar-fixed-top navbar-login">
	  <div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="collapsed navbar-toggle" data-toggle="collapse" data-target="#navbar-login-items" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<div class="logo-wrapper">
				<a class="navbar-brand" href="#">
					<img class="img-responsive navbar-logo" src="/img/usasistemas_logo.png" />
				</a>
			</div>
		</div>
		<div class="collapse navbar-collapse" id="navbar-login-items">
			<ul class="nav navbar-nav">
				<li class="menu-item"><span><a class="menu-item-link" ng-click="gotoLocation('theSystem')">QUEM SOMOS</a></span></li>
				<li class="menu-item"><span><a class="menu-item-link" ng-click="gotoLocation('advantages')">BENEFÍCIOS</a></span></li>
				<li class="menu-item"><span><a class="menu-item-link" ng-click="gotoLocation('contact')">FAÇA PARTE</a></span></li>
			</ul>
		</div>
	  </div>
	</nav>	
	<div class="container-fluid main-content">
		<a id="home" class="main-mark"></a>
		<div class="row page1">
			<div class="col-md-4">
				<div class="text-wrapper center-block">
					<img class="img-responsive" src="/img/usasistemas_beneficios_capa.png">
				</div>
			</div>
			<div class="col-md-8">
				<div class="row">
					<div class="col-md-6">
						<div class="text-wrapper center-block">
							<a href="https://www.usafood.com.br" target="_blank">
								<img class="img-responsive" src="/img/usasistemas_usafood_icon.png">
							</a>
						</div>
					</div>
					<div class="col-md-6">
						<div class="text-wrapper center-block">
							<a href="https://www.usabuild.com.br" target="_blank">
								<img class="img-responsive" src="/img/usasistemas_usabuild_icon.png">
							</a>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6 col-md-offset-3">
						<div class="text-wrapper center-block">
							<a href="https://www.usafranquias.com.br" target="_blank">
								<img class="img-responsive" src="/img/usasistemas_usafranquias_icon.png">
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<a id="theSystem" class="main-mark"></a>
		<div class="row page2">
			<div class="col-md-7">
				<div class="content center-block">
					<p class="text-center">Somos uma solução completa para integrar, registrar e agilizar a compra de insumos 
					 entre estabelecimentos e indústrias.</br>
					 </br>
					 Com objetivo de reduzir custos, gerando volume com a compra coletiva, a ferramenta
					  se torna uma união entre os estabelecimentos para serem mais competitivos nas negociações junto aos fornecedores.</p>
					<a ng-click="gotoLocation('contact')"><img class="img-responsive center-block map-rs pointer-cursor" src="/img/usasistemas_faca_parte.png"></a>
				</div>
			</div>	
			<div class="col-md-5">
				<div class="img-wrapper center-block">
					<img class="img-responsive" src="/img/usasistemas_quem_somos.png">
				</div>
			</div>	
		</div>
		<a id="advantages" class="main-mark"></a>
		<div class="row page3">		
			<div class="col-xs-12">
				<div class="title">ESTABELECIMENTOS</div>
				<div class="row">	
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone1.png"/>
							<p>Todos os produtos na<br/>plataforma, gerando<br/>praticidade e rapidez<br/>nos pedidos</p>
						</div>
					</div>
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone2.png"/>
							<p>Valores já negociados,<br/>com volume coletivo,<br/>gerando redução de custos<br/>nos insumos e itens operacionais</p>
						</div>
					</div>
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone3.png"/>
							<p>Gerenciamento dos<br/>pedidos, controle de<br/>entrega e recebimento de<br/>documentos via plataforma</p>
						</div>
					</div>
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone4.png"/>
							<p>Segurança nos<br/>dados de compra e<br/>sigilo total entre<br/>os estabelecimentos</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row page3">
			<div class="col-xs-12">
				<div class="title">FORNECEDORES</div>
				<div class="row">	
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone2.png"/>
							<p>Redução do custo em<br/>mão de obra comercial,<br/>com pedidos recebidos<br/>direto pela plataforma</p>
						</div>
					</div>
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone5.png"/>
							<p>Venda e promoção em<br/>escala, com apresentação<br/>de produtos para uma gama<br/>maior de estabelecimentos</p>
						</div>
					</div>
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone3.png"/>
							<p>Histórico de vendas por<br/>estabelecimento e geral,<br/>facilitando o controle<br/>de volume coletivo</p>
						</div>
					</div>
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone6.png"/>
							<p>Maior relacionamento<br/>com os estabelecimentos,<br/>reduzindo falhas e gerando<br/>maior credibilidade</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<a id="contact" class="main-mark"></a>
		<div class="row page5">			
			<div class="col-xs-12">
				<div class="row contact-row">
					<div class="col-md-4">
						<div class="img-wrapper center-block">
							<img class="img" src="/img/usasistemas_contact_faca_parte.png">
						</div>
					</div>
					<div class="col-md-8">
						<div class="form-wrapper center-block">
							<form class="form-horizontal">
								<div class="form-group" id="companyName">
									<label for="companyInputName" class="col-sm-3 control-label">Empresa</label>
									<div class="col-sm-9">
										<input id="companyInputName" type="text" class="form-control" ng-model="company.name">
									</div>
								</div>
								<div class="form-group" id="companyStateCity">
									<label for="companyInputStateCity" class="col-sm-3 control-label">Cidade/Estado</label>
									<div class="col-sm-9">
										<input id="companyInputStateCity" type="text" class="form-control" ng-model="company.state">
									</div>
								</div>
								<div class="form-group" id="companyContact">
									<label for="companyInputContact" class="col-sm-3 control-label">Contato</label>
									<div class="col-sm-9">
										<input id="companyInputContact" type="text" class="form-control" ng-model="company.contact">
									</div>
								</div>
								<div class="form-group" id="companyEmail">
									<label for="companyInputEmail" class="col-sm-3 control-label">Email</label>
									<div class="col-sm-9">
										<input id="companyInputEmail" type="text" class="form-control" ng-model="company.email">
									</div>
								</div>							
								<div class="form-group" id="companyComments">
									<label for="companyInputComments" class="col-sm-3 control-label">Conte um<br />pouco sobre<br />o seu negócio</label>
									<div class="col-sm-9">
										<textarea id="companyInputComments" class="form-control" ng-model="company.comments" rows="4"></textarea>
									</div>
								</div>
								<button ng-click="sendCompany()" class="btn btn-default pull-right">Enviar</button>
							</form>
						</div>
						
					</div>
				</div>
				<div class="row contact-row-divider"></div>
				<div class="row contact-row">
					<div class="col-md-4">
						<div class="img-wrapper center-block">
							<img class="img" src="/img/usasistemas_contact.png">
						</div>
					</div>
					<div class="col-md-8">
						<div class="form-wrapper center-block contact-details">
							<div class="row">
								<div class="col-xs-3"><p class="text-right"><i class="fa fa-mobile"></i></p></div>
								<div class="col-xs-9"><p>51.98227.3091</p></div>
							</div>
							<div class="row">
								<div class="col-xs-3"><p class="text-right"><i class="fa fa-envelope-o"></i></p></div>
								<div class="col-xs-9"><p><a href="mailto:contato@usasistemas.com.br">contato@usasistemas.com.br</a></p></div>
							</div>
							<div class="row">
								<div class="col-xs-3"><p class="text-right"><i class="fa fa-location-arrow"></i></p></div>
								<div class="col-xs-9"><p>Porto Alegre / RS</p></div>
							</div>
							<div class="row">
								<div class="col-xs-3"><p class="text-right"><i class="fa fa-linkedin-square"></i></p></div>
								<div class="col-xs-9"><p><a href="https://www.linkedin.com/in/fernandoandradeocv/" target="_blank">/fernandoandradeocv</a></p></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	
	<nav class="navbar navbar-default navbar-fixed-bottom navbar-footer hidden-xs hidden-sm">
		<div class="container-fluid">
			<p>mais <span class="highlight">usa</span> , mais benefícios</p>
		</div>
	</nav>
</div>