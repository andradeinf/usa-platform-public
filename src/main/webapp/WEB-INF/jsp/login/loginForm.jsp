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
				<li><a ng-click="gotoLocation('theSystem')">O Sistema</a></li>
				<li><a ng-click="gotoLocation('advantages')">Vantagens</a></li>
				<li><a ng-click="gotoLocation('tools')">Ferramentas</a></li>
				<li><a ng-click="gotoLocation('contact')">Contato</a></li>
			</ul>
		</div>
	  </div>
	</nav>
	
	<div class="container-fluid main-content">
		<a id="home" class="main-mark"></a>
		<div class="row page1">
			<div class="col-md-8">
				<img class="img-responsive center-block map" src="/img/mapa.png">
			</div>
			<div class="col-md-4">
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
		</div>
		<a id="theSystem" class="main-mark"></a>
		<div class="row page2">
			<div class="col-xs-12">
				<div class="page-title-bar"><span class="page-title">O Sistema</span></div>
				<div class="row">
					<div class="col-md-7">
						<div class="content">
							<div class="title"><b>Um projeto que traz mais de 10 anos de experiência no franchising</b></div>
							<div class="body-text">
								<p>O Sistema para Franquias foi desenvolvido pensando na necessidade de registrar e
								 agilizar a troca de informações entre franqueadores, franqueados e fornecedores.</br>
								 </br>
								 Com o objetivo de padronizar a franquia na gestão dos pontos de venda, garantir o
								 compartilhamento de informações entre franqueador e franqueados e facilitar a
								 operação dos franqueados na aquisição dos produtos homologados pela franquia,
								 o Sistema para Franquias se torna uma ferramenta central no dia-a-dia da rede.</br>
								 </br>
								 Nosso sistema opera em uma plataforma online e responsiva, facilitando o acesso dos
								 franqueados através de todos os canais: computador, tablet e celular. O sistema,
								 está hospedado na nuvem, reduzindo os custos operacionais e garantindo que as 
								 trocas de documentos e transações realizadas dentro da plataforma sejam feitas com
								 segurança e controle, trazendo resultados imediatos para a operação do franqueador.</p>
							</div>
							<div class="body-footer">
								<p class="text-center">Entre em contato conosco para conhecer mais o sistema que
								 vai ajudar a sua franquia a crescer!</p>
							</div>
						</div>
					</div>	
					<div class="col-md-5">
						<img class="img-responsive center-block" src="/img/pagina2.png">
					</div>	
				</div>
			</div>		
		</div>
		<a id="advantages" class="main-mark"></a>
		<div class="row page3">		
			<div class="col-xs-12">
				<div class="page-title-bar"><span class="page-title">Vantagens para a Franquia</span></div>
				<div class="row">	
					<div class="col-md-6">
						<div class="media">
							<div class="media-left media-middle">
								<img class="media-object" src="/img/icone1.png"/>
							</div>
							<div class="media-body">
								Sistema com usabilidade simplificada, onde Franqueador, Franqueados e Fornecedores
							 	compartilham a mesma plataforma;
							</div>
						</div>
						<div class="media">
							<div class="media-left media-middle">
								<img class="media-object" src="/img/icone2.png"/>
							</div>
							<div class="media-body">
								Contato direto entre Franqueados e Fornecedores, agilizando a compra de insumos e
							 	garantindo segurança nos dados e nas compras;
							</div>
						</div>
						<div class="media">
							<div class="media-left media-middle">
								<img class="media-object" src="/img/icone5.png"/>
							</div>
							<div class="media-body">
								Reduz consideravelmente o trabalho da Franqueadora. O Franqueado enxerga todos os produtos
							 	homologados pela Franqueadora, aumentando a credibilidade com Fornecedores;
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="media">
							<div class="media-left media-middle">
								<img class="media-object" src="/img/icone3.png"/>
							</div>
							<div class="media-body">
								Franqueadora segue com total controle dos pedidos feitos pelos Franqueados, recebendo
							 	relatórios mensais de consumos;
							</div>
						</div>
						<div class="media">
							<div class="media-left media-middle">
								<img class="media-object" src="/img/icone4.png"/>
							</div>
							<div class="media-body">
								O Franqueador não precisa comprar os insumos para revender aos Franqueados, evitando
							 	custos de estoque, transporte e bi-tributação;
							</div>
						</div>
						<div class="media">
							<div class="media-left media-middle">
								<img class="media-object" src="/img/icone6.png"/>
							</div>
							<div class="media-body">
								A entrega é feita diretamente na franquia, com o Fornecedor informando diretamente os dados
							 	no sistema e permitindo ao Franqueado rastrear o seu pedido até a entrega;
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<a id="tools" class="main-mark"></a>
		<div class="row page4">			
			<div class="col-xs-12">
				<div class="page-title-bar"><span class="page-title">Ferramentas</span></div>
				<div class="row">
					<div class="col-sm-4">
						<div class="content">
							<div class="title">PARA O FRANQUEADOR</div>
							<div class="body-text">
								<ul>
									<li>Cadastramento de produtos homologados;</li>
									<li>Cadastramento de fornecedores homologados;</li>
									<li>Acompanhamento de pedidos feitos pelos franqueados;</li>
									<li>Histórico de produção dos fornecedores;</li>
									<li>Controle de estoque disponível;</li>
									<li>Relatório mensal de consumo por fornecedor;</li>
									<li>Envio de documentos, imagens e informativos para rede ou personalizado para cada franqueado;</li>
								</ul>
							</div>
						</div>
					</div>	
					<div class="col-sm-4">
						<div class="content">
							<div class="title">PARA OS FRANQUEADOS</div>
							<div class="body-text">
								<ul>
									<li>Lista e compra de produtos homologados;</li>
									<li>Lista de fornecedores homologados;</li>
									<li>Acompanhamento de status dos pedidos;</li>
									<li>Histórico de compra por produto;</li>
									<li>Histórico geral de compras;</li>
									<li>Controle de conta a pagar;</li>
									<li>Recebimento de documentos, arquivos e informativos disponibilizados pelo franqueador;</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="content">
							<div class="title">PARA OS FORNECEDORES</div>
							<div class="body-text">
								<ul>
									<li>Recebimento e atualização de status de pedidos online;</li>
									<li>Dados cadastrais dos franqueados para emissão de notas fiscais;</li>
									<li>Gerenciamento de estoque;</li>
									<li>Relatório de entregas;</li>
									<li>Histórico de produção;</li>
									<li>Controle de contas a receber;</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<a id="contact" class="main-mark"></a>
		<div class="row page5">			
			<div class="col-xs-12">
				<div class="page-title-bar"><span class="page-title">Contato</span></div>
				<div class="row contact-row">
					<div class="col-md-4">
						<div class="title">FRANQUEADOR,</div>
						<div class="body">
							<p> queremos conhecer um pouco do seu negócio.</p>
						</div>
					</div>
					<div class="col-md-8">
						<form class="form-horizontal">
							<div class="form-group" id="franchisorName">
								<label for="franchisorInputName" class="col-sm-3 control-label">Nome da franquia</label>
								<div class="col-sm-9">
									<input id="franchisorInputName" type="text" class="form-control" ng-model="franchisor.name">
								</div>
							</div>
							<div class="form-group" id="franchisorSegment">
								<label for="franchisorInputSegment" class="col-sm-3 control-label">Segmento</label>
								<div class="col-sm-9">
									<input id="franchisorInputSegment" type="text" class="form-control" ng-model="franchisor.segment">
								</div>
							</div>
							<div class="form-group" id="franchisorState">
								<label for="franchisorInputState" class="col-sm-3 control-label">Cidade/Estado</label>
								<div class="col-sm-9">
									<input id="franchisorInputState" type="text" class="form-control" ng-model="franchisor.state">
								</div>
							</div>
							<div class="form-group" id="franchisorEmail">
								<label for="franchisorInputEmail" class="col-sm-3 control-label">Email</label>
								<div class="col-sm-9">
									<input id="franchisorInputEmail" type="text" class="form-control" ng-model="franchisor.email">
								</div>
							</div>
							<div class="form-group" id="franchisorContact">
								<label for="franchisorInputContact" class="col-sm-3 control-label">Contato</label>
								<div class="col-sm-9">
									<input id="franchisorInputContact" type="text" class="form-control" ng-model="franchisor.contact">
								</div>
							</div>
							<div class="form-group" id="franchisorComments">
								<label for="franchisorInputComments" class="col-sm-3 control-label">Conte um pouco sobre sua franquia</label>
								<div class="col-sm-9">
									<textarea id="franchisorInputComments" class="form-control" ng-model="franchisor.comments" rows="4"></textarea>
								</div>
							</div>
							<button ng-click="sendFranchisor()" class="btn btn-default pull-right">Enviar</button>
						</form>
					</div>
				</div>
				<div class="row contact-row-divider"></div>
				<div class="row contact-row">
					<div class="col-md-4">
						<div class="title">FORNECEDOR,</div>
						<div class="body">
							<p> queremos conhecer um pouco do seu negócio.</p>
						</div>
					</div>
					<div class="col-md-8">
						<form class="form-horizontal">
							<div class="form-group" id="supplierName">
								<label for="supplierInputName" class="col-sm-3 control-label">Nome da empresa</label>
								<div class="col-sm-9">
									<input id="supplierInputName" type="text" class="form-control" ng-model="supplier.name">
								</div>
							</div>
							<div class="form-group" id="supplierSegment">
								<label for="supplierInputSegment" class="col-sm-3 control-label">Segmento</label>
								<div class="col-sm-9">
									<input id="supplierInputSegment" type="text" class="form-control" ng-model="supplier.segment">
								</div>
							</div>
							<div class="form-group" id="supplierState">
								<label for="supplierInputState" class="col-sm-3 control-label">Cidade/Estado</label>
								<div class="col-sm-9">
									<input id="supplierInputState" type="text" class="form-control" ng-model="supplier.state">
								</div>
							</div>
							<div class="form-group" id="supplierEmail">
								<label for="supplierInputEmail" class="col-sm-3 control-label">Email</label>
								<div class="col-sm-9">
									<input id="supplierInputEmail" type="text" class="form-control" ng-model="supplier.email">
								</div>
							</div>
							<div class="form-group" id="supplierContact">
								<label for="supplierInputContact" class="col-sm-3 control-label">Contato</label>
								<div class="col-sm-9">
									<input id="supplierInputContact" type="text" class="form-control" ng-model="supplier.contact">
								</div>
							</div>
							<div class="form-group" id="supplierComments">
								<label for="supplierInputComments" class="col-sm-3 control-label">Conte um pouco sobre o seu negócio</label>
								<div class="col-sm-9">
									<textarea id="supplierInputComments" class="form-control" ng-model="supplier.comments" rows="4"></textarea>
								</div>
							</div>
							<button ng-click="sendSupplier()" class="btn btn-default pull-right">Enviar</button>
						</form>
					</div>
				</div>
				<div class="row contact-row-divider"></div>
				<div class="row contact-row">
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