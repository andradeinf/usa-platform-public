<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div ng-show="!isLogin && !isFranchiseeContact && !isSupplierContact">
	<nav class="navbar navbar-default navbar-fixed-top navbar-login">
	  <div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="collapsed navbar-toggle" data-toggle="collapse" data-target="#navbar-login-items" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-left" href="#">
				<img class="img-responsive" src="/img/foodhubs_logo.png" />
			</a>
		</div>
		<div class="collapse navbar-collapse" id="navbar-login-items">
			<ul class="nav navbar-nav">
				<li class="menu-item"><span><a class="menu-item-link" ng-click="gotoLocation('theSystem')">QUEM SOMOS</a></span></li>
				<li class="menu-item"><span><a class="menu-item-link" ng-click="gotoLocation('advantages')">BENEFÍCIOS</a></span></li>
				<li class="menu-item"><span><a class="menu-item-link" ng-click="gotoLocation('tools')">FERRAMENTAS</a></span></li>
				<li class="menu-item"><span><a class="menu-item-link" ng-click="gotoLocation('contact')">FAÇA PARTE</a></span></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
			    <li class="menu-item"><span class="navbar-btn"><a class="menu-item-link btn btn-default login-button" ng-click="toggleLogin()">LOGIN</a></span></li>
			</ul>
		</div>
	  </div>
	</nav>	
	<div class="container-fluid main-content">
		<a id="home" class="main-mark"></a>
		<div class="row page1">
			<div class="col-md-4">
				<div class="content center-block">
					<p class="text-center">Plataforma que une e gerencia a compra de insumos para 
						estabelecimentos do mesmo segmento, com objetivo da compra coletiva, 
						gerando volume e negociando melhores preços.
					</p>
				</div>
				<!--div class="text-wrapper center-block">
					<img class="img-responsive" src="/img/usafood_beneficios_capa.png">
				</div-->
			</div>
			<div class="col-md-8 nopadding">
				<div class="pull-right">
					<img class="img-responsive" src="/img/foodhubs_banner_capa.png">
				</div>				
			</div>
		</div>
		<a id="theSystem" class="main-mark"></a>
		<div class="row page2">
			<div class="col-md-7">
				<div class="content center-block">
					<p class="text-center">Somos uma startup com solução completa para integrar, interagir, 
					  registrar e agilizar a compra de insumos entre estabelecimentos e indústrias.</br>
					 </br>
					 Com objetivo de reduzir custos, gerando volume com a compra coletiva, a ferramenta
					  se torna uma união entre os estabelecimentos.</p>
					<a ng-click="gotoLocation('contact')"><img class="img-responsive center-block map-rs pointer-cursor" src="/img/usasistemas_faca_parte.png"></a>
				</div>
			</div>	
			<div class="col-md-5">
				<div class="img-wrapper center-block">
					<img class="img-responsive" src="/img/foodhubs_quem_somos.png">
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
		<a id="tools" class="main-mark"></a>
		<div class="row page4">			
			<div class="col-md-8 tools">
				<div class="row">
					<div class="col-sm-4">
						<div class="tool-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone7.png"/>
							<p>Catálogo de produtos<br/>e atualização de<br/>preço online</p>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="tool-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone8.png"/>
							<p>Espaço para<br/>promoções<br/>e avisos</p>
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
							<p>Espaço para envio<br/>de arquivos para a rede<br/>(Geral ou Individual)</p>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="tool-item">
							<img class="img-responsive center-block" src="/img/usasistemas_icone11.png"/>
							<p>Relatório de<br/>consumo do<br/>estabelecimento</p>
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
					<div class="col-md-6 text-center">
						<button type="button" class="btn btn-default contact-button" ng-click="toggleFranchiseeContact()">Quero Comprar pelo FoodHubs</button>
					</div>
					<div class="col-md-6 text-center">
						<button type="button" class="btn btn-default contact-button" ng-click="toggleSupplierContact()">Quero Vender pelo FoodHubs</button>
					</div>
				</div>
				<div class="row contact-row-divider"></div>
				<div class="row contact-row">
					<div class="col-md-4">
						<div class="img-wrapper center-block">
							<img class="img" src="/img/foodhubs_contact.png">
						</div>
						<div class="content center-block">
							<p class="text-center">Contato</p>
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
								<div class="col-xs-9"><p><a href="mailto:contato@foodhubs.com.br">contato@foodhubs.com.br</a></p></div>
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
			<p><span class="highlight">conectando</span> negócios</p>
		</div>
	</nav>
</div>
<div ng-show="isLogin">
	<div class="container-fluid">
		<div class="row login-page">
			<div class="col-md-4 col-md-offset-4">
				<div class="row">
                	<img class="login-form-img" src="/img/foodhubs_logo_login.png" />
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
<div ng-show="isFranchiseeContact">
	<div class="container-fluid">
		<div class="row contact-page">
			<div class="col-md-8 col-md-offset-2">
				<div class="row">
                	<img class="contact-page-img" src="/img/foodhubs_logo_login.png" />
                </div>
				<form class="contact-form form-horizontal">
		            <div class="contact-form-header">            
						<div class="row">
		                	<p><b>Cadastre-se como Estabelecimento</b></p>
		                </div>
		            </div>
					<div class="form-group" id="franchiseeName">
						<label for="inputFranchiseeName" class="col-sm-3 control-label">Nome do Estabelecimento</label>
						<div class="col-sm-9">
							<input id="inputFranchiseeName" type="text" class="form-control" ng-model="franchisee.name">
						</div>
					</div>
					<div class="form-group" id="franchiseeCorporateName">
						<label for="inputFranchiseeCorporateName" class="col-sm-3 control-label">Razão Social do Estabelecimento</label>
						<div class="col-sm-9">
							<input id="inputFranchiseeCorporateName" type="text" class="form-control" ng-model="franchisee.corporateName">
						</div>
					</div>
					<div class="form-group" id="franchiseeFiscalId">
						<label for="inputFranchiseeFiscalId" class="col-sm-3 control-label">CNPJ</label>
						<div class="col-sm-9">
							<input id="inputFranchiseeFiscalId" type="text" class="form-control" ng-model="franchisee.fiscalId">
						</div>
					</div>
					<div class="form-group" id="franchiseeAddress">
						<label for="inputFranchiseeAddress" class="col-sm-3 control-label">Endereço Completo</label>
						<div class="col-sm-9">
							<textarea id="inputFranchiseeAddress" class="form-control" ng-model="franchisee.address" rows="2"></textarea>
						</div>
					</div>
					<div class="form-group" id="franchiseeContactName">
						<label for="inputFranchiseeContactName" class="col-sm-3 control-label">Nome para Contato</label>
						<div class="col-sm-9">
							<input id="inputFranchiseeContactName" type="text" class="form-control" ng-model="franchisee.contact">
						</div>
					</div>
					<div class="form-group" id="franchiseeContactPhone">
						<label for="inputFranchiseeContactPhone" class="col-sm-3 control-label">Telefone</label>
						<div class="col-sm-9">
							<input id="inputFranchiseeContactPhone" type="text" class="form-control" ng-model="franchisee.phone">
						</div>
					</div>
					<div class="form-group" id="franchiseeContactEmail">
						<label for="inputFranchiseeContactEmail" class="col-sm-3 control-label">Email</label>
						<div class="col-sm-9">
							<input id="inputFranchiseeContactEmail" type="text" class="form-control" ng-model="franchisee.email">
						</div>
					</div>		
					<div class="form-group" id="franchiseeDeliveryDetails">
						<label for="inputFranchiseeDeliveryDetails" class="col-sm-3 control-label">Qual o melhor horário para<br />entrega de produtos?</label>
						<div class="col-sm-9">
							<textarea id="inputFranchiseeDeliveryDetails" class="form-control" ng-model="franchisee.deliveryDetails" rows="2"></textarea>
						</div>
					</div>					
					<div class="form-group" id="franchiseeComments">
						<label for="inputFranchiseeComments" class="col-sm-3 control-label">Conte-nos um pouco sobre<br />o seu negócio</label>
						<div class="col-sm-9">
							<textarea id="inputFranchiseeComments" class="form-control" ng-model="franchisee.comments" rows="2"></textarea>
						</div>
					</div>
		            <div class="row">
			            <div class="col-md-10 col-md-offset-1">
			                <button type="button" class="btn btn-default btn-block" ng-click="sendNewFranchisee()">Enviar</button>
			            </div>
		            </div>
		            <div class="contact-form-footer">            
						<div class="row">
		                	<small>Envie-nos os dados acima para se tornar um estabelecimento cadastrado e comprar através do FoodHubs</small>
		                </div>
		            </div>
		        </form> 
			</div>
			<div class="back-button-wrapper">
				<a ng-click="toggleFranchiseeContact()" title="Voltar"><img class="pointer-cursor back-button" src="/img/usasistemas_login_back.png" /></a>s
			</div>
		</div>
	</div>
</div>
<div ng-show="isSupplierContact">
	<div class="container-fluid">
		<div class="row contact-page">
			<div class="col-md-8 col-md-offset-2">
				<div class="row">
                	<img class="contact-page-img" src="/img/foodhubs_logo_login.png" />
                </div>
				<form class="contact-form form-horizontal">
		            <div class="contact-form-header">            
						<div class="row">
		                	<p><b>Cadastre-se como Fornecedor</b></p>
		                </div>
		            </div>
					<div class="form-group" id="supplierName">
						<label for="inputSupplierName" class="col-sm-3 control-label">Nome da Empresa</label>
						<div class="col-sm-9">
							<input id="inputSupplierName" type="text" class="form-control" ng-model="supplier.name">
						</div>
					</div>
					<div class="form-group" id="supplierFiscalId">
						<label for="inputSupplierFiscalId" class="col-sm-3 control-label">CNPJ</label>
						<div class="col-sm-9">
							<input id="inputSupplierFiscalId" type="text" class="form-control" ng-model="supplier.fiscalId">
						</div>
					</div>
					<div class="form-group" id="supplierAddress">
						<label for="inputSupplierAddress" class="col-sm-3 control-label">Endereço Completo</label>
						<div class="col-sm-9">
							<textarea id="inputSupplierAddress" class="form-control" ng-model="supplier.address" rows="2"></textarea>
						</div>
					</div>
					<div class="form-group" id="supplierContactName">
						<label for="inputSupplierContactName" class="col-sm-3 control-label">Nome para Contato</label>
						<div class="col-sm-9">
							<input id="inputSupplierContactName" type="text" class="form-control" ng-model="supplier.contact">
						</div>
					</div>
					<div class="form-group" id="supplierContactPhone">
						<label for="inputSupplierContactPhone" class="col-sm-3 control-label">Telefone</label>
						<div class="col-sm-9">
							<input id="inputSupplierContactPhone" type="text" class="form-control" ng-model="supplier.phone">
						</div>
					</div>
					<div class="form-group" id="supplierContactEmail">
						<label for="inputSupplierContactEmail" class="col-sm-3 control-label">Email</label>
						<div class="col-sm-9">
							<input id="inputSupplierContactEmail" type="text" class="form-control" ng-model="supplier.email">
						</div>
					</div>					
					<div class="form-group" id="supplierComments">
						<label for="inputSupplierComments" class="col-sm-3 control-label">Conte-nos um pouco sobre<br />o seu negócio</label>
						<div class="col-sm-9">
							<textarea id="inputSupplierComments" class="form-control" ng-model="supplier.comments" rows="2"></textarea>
						</div>
					</div>
		            <div class="row">
			            <div class="col-md-10 col-md-offset-1">
			                <button type="button" class="btn btn-default btn-block" ng-click="sendNewSupplier()">Enviar</button>
			            </div>
		            </div>
		            <div class="contact-form-footer">            
						<div class="row">
		                	<small>Envie-nos os dados acima para se tornar um fornecedor cadastrado e vender através do FoodHubs</small>
		                </div>
		            </div>
		        </form> 
			</div>
			<div class="back-button-wrapper">
				<a ng-click="toggleSupplierContact()" title="Voltar"><img class="pointer-cursor back-button" src="/img/usasistemas_login_back.png" /></a>s
			</div>
		</div>
	</div>
</div>