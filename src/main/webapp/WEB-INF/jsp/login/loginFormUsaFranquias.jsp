<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div ng-show="!isLogin">
	<div class="topnav">
		<div class="topnav-logo">
			<img ng-click="gotoLocation('home')" class="img-responsive pointer-cursor" src="/img/usafranquias_logo.png" />
		</div>
		<div class="topnav-menu-button">
			<button class="topnav-button" data-toggle="collapse" data-target="#popup-menu"><i class="fa fa-bars"></i></button>
		</div>
		<div class="topnav-menu-items">
			<a ng-click="gotoLocation('theSystem')" class="pointer-cursor">QUEM SOMOS</a>
			<a ng-click="gotoLocation('advantages')" class="pointer-cursor">BENEFÍCIOS</a>
			<a ng-click="gotoLocation('tools')" class="pointer-cursor">FERRAMENTAS</a>
			<a ng-click="gotoLocation('contact')" class="pointer-cursor">FAÇA PARTE</a>
			<a ng-click="toggleLogin()" class="login-button pointer-cursor">LOGIN</a>
		</div>
	</div>
	<div class="topnav-popup collapse" id="popup-menu">
		<a ng-click="gotoLocation('theSystem')" class="pointer-cursor">QUEM SOMOS</a>
		<a ng-click="gotoLocation('advantages')" class="pointer-cursor">BENEFÍCIOS</a>
		<a ng-click="gotoLocation('tools')" class="pointer-cursor">FERRAMENTAS</a>
		<a ng-click="gotoLocation('contact')" class="pointer-cursor">FAÇA PARTE</a>
		<a ng-click="toggleLogin()" class="login-button pointer-cursor">LOGIN</a>
	</div>
	<div class="container-fluid main-content">
		<a id="home" class="main-mark"></a>
		<div class="row page1">
			<div class="col-md-4">
				<div class="text-wrapper center-block">
					<img class="img-responsive" src="/img/usafranquias_beneficios_capa.png">
				</div>
			</div>
			<div class="col-md-8">
				<div class="img-wrapper center-block">
					<img class="img-responsive" src="/img/usasistemas_mapa_capa.png">
				</div>				
			</div>
		</div>
		<a id="theSystem" class="main-mark"></a>
		<div class="row page2">
			<div class="col-md-7">
				<div class="content center-block">
					<p class="text-center">Somos uma solução completa para integrar, registrar e agilizar a gestão
					das redes de franquias.</br>
					 </br>
					 Com o <b>usa|franquias</b> você garante o compartilhamento de informações entre fornecedor e
					 franqueado e facilita a operação dos franqueados na aquisição dos produtos homologados pela franquia.</br>
					 </br>
					 Nosso sistema opera em uma plataforma online e responsiva, garantindo que as trocas de documentos
					 sejam feitas com segurança e controle, trazendo resultados imediatos para a operação do franqueador.</p>
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
				<div class="title">BENEFÍCIOS PARA REDE</div>
				<div class="row">	
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone6.png"/>
							<p>Maior relacionamento<br/>entre a rede, reduzindo<br/>falhas e gerando maior<br/>credibilidade do</br>franqueador</p>
						</div>
					</div>
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone1.png"/>
							<p>Sistema com usabilidade<br/>simplificada, onde franqueador,<br/>franqueados e fornecedores<br/>compartilham a mesma<br/>plataforma</p>
						</div>
					</div>
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone2.png"/>
							<p>O franqueador não precisa<br/>comprar os insumos para <br/>revender aos franqueados,<br/>evitando custo de estoque,<br/>transporte e bi-tributação</p>
						</div>
					</div>
					<div class="col-md-3">
						<div class="advantage-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone3.png"/>
							<p>O Franqueador segue com o total<br/>controle dos pedidos feitos<br/>pelos franqueados, rececbendo<br/>relatórios mensais de consumo</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<a id="tools" class="main-mark"></a>
		<div class="row page4">			
			<div class="col-md-8 tools">
				<div class="row">
					<div class="col-sm-4">
						<div class="tool-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone7.png"/>
							<p>Central de compras<br/>com atualização de<br/>preço online</p>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="tool-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone8.png"/>
							<p>Espaço para<br/>informativos<br/><small>(Geral ou individual)</small></p>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="tool-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone9.png"/>
							<p>Troca de<br/>mensagens<br/>online</p>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4">
						<div class="tool-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone10.png"/>
							<p>Espaço para envio<br/>de arquivos para a rede<br/><small>(Geral ou Individual)</small></p>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="tool-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone11.png"/>
							<p>Relatório de<br/>consumo do<br/>franqueado</p>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="tool-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone12.png"/>
							<p>Calendário<br/>para eventos<br/>e promoções</p>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="img-wrapper center-block">
					<img class="img-responsive" src="/img/usasistemas_ferramentas.png">
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
								<div class="col-xs-9"><p><a href="mailto:contato@usafranquias.com.br">contato@usafranquias.com.br</a></p></div>
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
			<p>mais <span class="highlight">usa</span> , mais gestão</p>
		</div>
	</nav>
</div>
<div ng-show="isLogin">
	<div class="container-fluid">
		<div class="row login-page">
			<div class="col-md-4 col-md-offset-4">
				<div class="row">
                	<img class="login-form-img" src="/img/usafranquias_logo_login.png" />
                </div>
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
			<div class="back-button-wrapper">
				<a ng-click="toggleLogin()" title="Voltar"><img class="pointer-cursor back-button" src="/img/usasistemas_login_back.png" /></a>s
			</div>
		</div>
	</div>
</div>