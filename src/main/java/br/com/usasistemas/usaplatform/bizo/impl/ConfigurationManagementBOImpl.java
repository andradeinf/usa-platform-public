package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.usasistemas.usaplatform.api.response.data.EnumValidValueResponseData;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.dao.SystemConfigurationDAO;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.SystemConfigurationData;
import br.com.usasistemas.usaplatform.model.enums.DomainKeysEnum;
import br.com.usasistemas.usaplatform.model.enums.SystemConfigurationTypeEnum;

public class ConfigurationManagementBOImpl implements ConfigurationManagementBO {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ConfigurationManagementBOImpl.class.getName());
	private SystemConfigurationDAO systemConfigurationDAO;
	
	private Map<String, DomainConfigurationData> domainConfigurationsByKey = new HashMap<String, DomainConfigurationData>();
	private Map<String, DomainConfigurationData> domainConfigurationsByURL = new HashMap<String, DomainConfigurationData>();
	
	public SystemConfigurationDAO getSystemConfigurationDAO() {
		return systemConfigurationDAO;
	}
	
	public void setSystemConfigurationDAO(SystemConfigurationDAO systemConfigurationDAO) {
		this.systemConfigurationDAO = systemConfigurationDAO;
	}
	
	public ConfigurationManagementBOImpl() {
		initializeDomainConfigurations();
	}
	
	@Override
	public List<EnumValidValueResponseData> getTypes() {
		List<EnumValidValueResponseData> result = new ArrayList<EnumValidValueResponseData>();
		
		for (SystemConfigurationTypeEnum value: SystemConfigurationTypeEnum.values()) {
			EnumValidValueResponseData enumValue = new EnumValidValueResponseData();
			enumValue.setKey(value.name());
			enumValue.setValue(value.getDescription());
			result.add(enumValue);
		}

		return result;
	}
	
	@Override
	public List<SystemConfigurationData> getAllConfigurations() {
		return systemConfigurationDAO.listAll();
	}
	
	@Override
	public SystemConfigurationData saveConfiguration(SystemConfigurationData systemConfiguration) {
		return systemConfigurationDAO.save(systemConfiguration);
	}
	
	@Override
	public SystemConfigurationData getConfigurationByKey(String key) {
		return systemConfigurationDAO.findByKey(key);
	}
	
	@Override
	public List<SystemConfigurationData> getConfigurationsByKey(List<String> keys) {
		List<SystemConfigurationData> response = new ArrayList<SystemConfigurationData>();
		
		for (String key: keys) {
			SystemConfigurationData configuration = systemConfigurationDAO.findByKey(key);
			if (configuration != null) {
				response.add(configuration);
			}
		}
		
		return response;
	}
	
	@Override
	public SystemConfigurationData deleteConfiguration(Long id) {
		return systemConfigurationDAO.delete(id);
	}

	@Override
	public DomainConfigurationData getDomainConfigurationByURL(String serverName) {	
		
		DomainConfigurationData result = domainConfigurationsByURL.get(serverName);
		
		if (result == null) {
			result = domainConfigurationsByKey.get(DEFAULT_KEY);
		}
		
		return result;
	}
	
	@Override
	public DomainConfigurationData getDomainConfigurationByKey(String key) {	
		
		DomainConfigurationData result = domainConfigurationsByKey.get(key);
		
		if (result == null) {
			result = domainConfigurationsByKey.get(DEFAULT_KEY);
		}
		
		return result;
	}
	
	@Override
	public List<EnumValidValueResponseData> getDomains() {
		List<EnumValidValueResponseData> result = new ArrayList<EnumValidValueResponseData>();
		
		for (String key: domainConfigurationsByKey.keySet()) {
			EnumValidValueResponseData enumValue = new EnumValidValueResponseData();
			enumValue.setKey(key);
			enumValue.setValue(domainConfigurationsByKey.get(key).getTitle());
			result.add(enumValue);
		}

		return result;
	}
	
	private void initializeDomainConfigurations() {
		
		Map<String, String> labels = null;
		Map<String, Boolean> featureFlags = null;
		Map<String, String> configuration = null;
		
		// Default configuration - Sistema para Franquias
		DomainConfigurationData defaultDomainConfiguration = new DomainConfigurationData();
		defaultDomainConfiguration.setId(1L);
		defaultDomainConfiguration.setKey(ConfigurationManagementBO.DEFAULT_KEY);
		defaultDomainConfiguration.setName("SistemaParaFranquias");
		defaultDomainConfiguration.setLoginCss("login.css");
		defaultDomainConfiguration.setMainCss("franchiseLogistics.css");
		defaultDomainConfiguration.setTitle("Sistema para Franquias");
		defaultDomainConfiguration.setFavicon("ocvfranquias.ico");
		defaultDomainConfiguration.setMainLogo("logo.png");
		defaultDomainConfiguration.setLoginForm("loginForm");
		defaultDomainConfiguration.setMailSender("contato@sistemaparafranquias.com.br");
		defaultDomainConfiguration.setMailUser("USERNAME");
		defaultDomainConfiguration.setMailPassword("PASSWORD");
		defaultDomainConfiguration.setMainURL("www.sistemaparafranquias.com.br");
		defaultDomainConfiguration.setMailApiKey("API_KEY");
		
		domainConfigurationsByKey.put(defaultDomainConfiguration.getKey(), defaultDomainConfiguration);
		domainConfigurationsByURL.put(defaultDomainConfiguration.getMainURL(), defaultDomainConfiguration);
		domainConfigurationsByURL.put("sistemaparafranquias", defaultDomainConfiguration);
		domainConfigurationsByURL.put("sistemaparafranquias-dot-ocvfranquias.appspot.com", defaultDomainConfiguration);
		domainConfigurationsByURL.put("sistemaparafranquias-dot-ocvfranquias-dev.appspot.com", defaultDomainConfiguration);
		domainConfigurationsByURL.put("sistemaparafranquias.com.br", defaultDomainConfiguration);
		
		labels = new HashMap<String, String>();
		labels.put("ANNOUNCEMENTS", "Quadro de Avisos");
		labels.put("CALENDAR", "Calendário");
		labels.put("FILES", "Documentos");
		labels.put("FRANCHISEE", "Franqueado");
		labels.put("FRANCHISEE_CORPORATE_NAME", "Razão Social");
		labels.put("FRANCHISEE_FISCAL_ID", "CNPJ");
		labels.put("FRANCHISEES", "Franqueados");
		labels.put("FRANCHISEES_ALL", "TODOS");
		labels.put("FRANCHISOR", "Franqueador");
		labels.put("FRANCHISOR_CORPORATE_NAME", "Razão Social");
		labels.put("FRANCHISOR_FISCAL_ID", "CNPJ");
		labels.put("MESSAGES", "Central de Atendimento");
		labels.put("PRODUCT", "Produto");
		labels.put("PRODUCT_LOWER", "produto");
		labels.put("PRODUCTS", "Produtos");
		labels.put("SUPPLIER", "Fornecedor");
		labels.put("SUPPLIER_LOWER", "fornecedor");
		labels.put("SUPPLIERS", "Fornecedores");
		labels.put("SUPPLIERS_ALL", "TODOS");
		labels.put("SUPPLIER_FRANCHISEE", "Loja");
		labels.put("SUPPLIER_FRANCHISEES", "Lojas");
		labels.put("SUPPLIER_FRANCHISOR", "Franquia");
		labels.put("SUPPLIER_FRANCHISORS", "Franquias");
		labels.put("SUPPLIER_FRANCHISORS_ALL", "TODAS");
		labels.put("VISIBILITY_FRANCHISEES_ALL", "Todos os franqueados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_ANNOUNCEMENT", "Restringir aviso apenas para os franqueados selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_CALENDAR", "Restringir evento apenas para os franqueados selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_FILE", "Restringir acesso apenas para os franqueados selecionados abaixo");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_ANNOUNCEMENT", "Caso nenhum franqueado seja selecionado, todos poderão visualizar esse aviso");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_CALENDAR", "Caso nenhum franqueado seja selecionado, todos poderão visualizar esse evento");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_FILE", "Caso nenhum franqueado seja selecionado, todos terão acesso");
		labels.put("VISIBILITY_FRANCHISOR_ALL_FRANCHISEES", "Franqueador e Todos os Franqueados");
		labels.put("VISIBILITY_FRANCHISOR_FRANCHISEES", "Franqueador e Franqueados");
		labels.put("VISIBILITY_FRANCHISOR_SOME_FRANCHISEES", "Franqueador e Alguns Franqueados");
		labels.put("VISIBILITY_ONLY_FRANCHISOR", "Apenas Franqueador");
		labels.put("VISIBILITY_SOME_FRANCHISEES", "Alguns Franqueados");
		labels.put("VISIBILITY_ALL_FRANCHISEES", "Todos os Franqueados");
		labels.put("TRAININGS", "Treinamentos");
		defaultDomainConfiguration.setLabels(labels);
		
		featureFlags = new HashMap<String, Boolean>();
		featureFlags.put("FRANCHISEE_DOCUMENTS", true);
		featureFlags.put("SUPPLIER_STOCK", true);
		featureFlags.put("REVIEW_REQUEST", false);
		featureFlags.put("PRODUCTS", true);
		featureFlags.put("TUTORIALS", true);
		featureFlags.put("LOGO", true);
		featureFlags.put("DOCUMENT_FILE_DEFAULT_ACCESS_RESTRICTED", false);
		defaultDomainConfiguration.setFeatureFlags(featureFlags);
		
		configuration = new HashMap<String, String>();
		configuration.put("STOCK_CONSOLIDATION_WINDOW", "7");
		defaultDomainConfiguration.setConfiguration(configuration);
		
		// Configuration - Usa Franquias
		DomainConfigurationData usaFranquias = new DomainConfigurationData();
		usaFranquias.setId(2L);
		usaFranquias.setKey(DomainKeysEnum.USA_FRANQUIAS.name());
		usaFranquias.setName("UsaFranquias");
		usaFranquias.setRedirectTo(DomainKeysEnum.FRANQUIAS);
		usaFranquias.setMainURL("www.usafranquias.com.br");
		usaFranquias.setLoginCss("usafranquias_login.css");
		usaFranquias.setMainCss("usafranquias.css");
		usaFranquias.setTitle("Usa | Franquias");
		usaFranquias.setFavicon("usasistemas.ico");
		usaFranquias.setMainLogo("usafranquias_logo.png");
		usaFranquias.setLoginForm("loginFormUsaFranquias");
		usaFranquias.setMailSender("contato@usafranquias.com.br");
		usaFranquias.setMailUser("USERNAME");
		usaFranquias.setMailPassword("PASSWORD");
		usaFranquias.setMailApiKey("API_KEY");

		domainConfigurationsByKey.put(usaFranquias.getKey(), usaFranquias);
		domainConfigurationsByURL.put(usaFranquias.getMainURL(), usaFranquias);
		domainConfigurationsByURL.put("usafranquias", usaFranquias);
		domainConfigurationsByURL.put("usafranquias.com.br", usaFranquias);
		domainConfigurationsByURL.put("luzdalua.usafranquias", usaFranquias);
		domainConfigurationsByURL.put("luzdalua.usafranquias.com.br", usaFranquias);
		domainConfigurationsByURL.put("usafranquias-dot-ocvfranquias.appspot.com", usaFranquias);
		domainConfigurationsByURL.put("usafranquias-dot-ocvfranquias-dev.appspot.com", usaFranquias);
		
		labels = new HashMap<String, String>();
		labels.put("ANNOUNCEMENTS", "Quadro de Avisos");
		labels.put("CALENDAR", "Calendário");
		labels.put("FILES", "Documentos");
		labels.put("FRANCHISEE", "Franqueado");
		labels.put("FRANCHISEE_CORPORATE_NAME", "Razão Social");
		labels.put("FRANCHISEE_FISCAL_ID", "CNPJ");
		labels.put("FRANCHISEES", "Franqueados");
		labels.put("FRANCHISEES_ALL", "TODOS");
		labels.put("FRANCHISOR", "Franqueador");
		labels.put("FRANCHISOR_CORPORATE_NAME", "Razão Social");
		labels.put("FRANCHISOR_FISCAL_ID", "CNPJ");
		labels.put("MESSAGES", "Central de Atendimento");
		labels.put("PRODUCT", "Produto");
		labels.put("PRODUCT_LOWER", "produto");
		labels.put("PRODUCTS", "Produtos");
		labels.put("SUPPLIER", "Fornecedor");
		labels.put("SUPPLIER_LOWER", "fornecedor");
		labels.put("SUPPLIERS", "Fornecedores");
		labels.put("SUPPLIERS_ALL", "TODOS");
		labels.put("SUPPLIER_FRANCHISEE", "Loja");
		labels.put("SUPPLIER_FRANCHISEES", "Lojas");
		labels.put("SUPPLIER_FRANCHISOR", "Franquia");
		labels.put("SUPPLIER_FRANCHISORS", "Franquias");
		labels.put("SUPPLIER_FRANCHISORS_ALL", "TODAS");
		labels.put("VISIBILITY_FRANCHISEES_ALL", "Todos os franqueados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_ANNOUNCEMENT", "Restringir aviso apenas para os franqueados selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_CALENDAR", "Restringir evento apenas para os franqueados selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_FILE", "Restringir acesso apenas para os franqueados selecionados abaixo");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_ANNOUNCEMENT", "Caso nenhum franqueado seja selecionado, todos poderão visualizar esse aviso");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_CALENDAR", "Caso nenhum franqueado seja selecionado, todos poderão visualizar esse evento");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_FILE", "Caso nenhum franqueado seja selecionado, todos terão acesso");
		labels.put("VISIBILITY_FRANCHISOR_ALL_FRANCHISEES", "Franqueador e Todos os Franqueados");
		labels.put("VISIBILITY_FRANCHISOR_FRANCHISEES", "Franqueador e Franqueados");
		labels.put("VISIBILITY_FRANCHISOR_SOME_FRANCHISEES", "Franqueador e Alguns Franqueados");
		labels.put("VISIBILITY_ONLY_FRANCHISOR", "Apenas Franqueador");
		labels.put("VISIBILITY_SOME_FRANCHISEES", "Alguns Franqueados");
		labels.put("VISIBILITY_ALL_FRANCHISEES", "Todos os Franqueados");
		labels.put("TRAININGS", "Treinamentos");
		usaFranquias.setLabels(labels);
		
		featureFlags = new HashMap<String, Boolean>();
		featureFlags.put("FRANCHISEE_DOCUMENTS", true);
		featureFlags.put("SUPPLIER_STOCK", true);
		featureFlags.put("REVIEW_REQUEST", false);
		featureFlags.put("PRODUCTS", true);
		featureFlags.put("TUTORIALS", true);
		featureFlags.put("LOGO", true);
		featureFlags.put("DOCUMENT_FILE_DEFAULT_ACCESS_RESTRICTED", false);
		usaFranquias.setFeatureFlags(featureFlags);
		
		configuration = new HashMap<String, String>();
		configuration.put("STOCK_CONSOLIDATION_WINDOW", "7");
		usaFranquias.setConfiguration(configuration);

		// Configuration - Sistema Gastronomico / Usa Food
		DomainConfigurationData gastronomico = new DomainConfigurationData();
		gastronomico.setId(3L);
		gastronomico.setKey(DomainKeysEnum.USA_FOOD.name());
		gastronomico.setName("UsaFood");
		gastronomico.setRedirectTo(DomainKeysEnum.GASTRONOMICO);
		gastronomico.setMainURL("www.usafood.com.br");
		gastronomico.setLoginCss("usafood_login.css");
		gastronomico.setMainCss("usafood.css");
		gastronomico.setTitle("Usa | Food");
		gastronomico.setFavicon("usasistemas.ico");
		gastronomico.setMainLogo("usafood_logo.png");
		gastronomico.setLoginForm("loginFormUsaFood");
		gastronomico.setMailSender("contato@usafood.com.br");
		gastronomico.setMailUser("USERNAME");
		gastronomico.setMailPassword("PASSWORD");
		gastronomico.setMailApiKey("API_KEY");
		
		domainConfigurationsByKey.put(gastronomico.getKey(), gastronomico);
		domainConfigurationsByURL.put(gastronomico.getMainURL(), gastronomico);
		domainConfigurationsByURL.put("usafood", gastronomico);
		domainConfigurationsByURL.put("usafood.com.br", gastronomico);
		domainConfigurationsByURL.put("usafood-dot-ocvfranquias.appspot.com", gastronomico);
		domainConfigurationsByURL.put("usafood-dot-ocvfranquias-dev.appspot.com", gastronomico);
		
		labels = new HashMap<String, String>();
		labels.put("ANNOUNCEMENTS", "Promoções e Avisos");
		labels.put("CALENDAR", "Calendário");
		labels.put("COMPANY", "empresa");
		labels.put("FILES", "Documentos");
		labels.put("FRANCHISEE", "Estabelecimento");
		labels.put("FRANCHISEE_CORPORATE_NAME", "Razão Social");
		labels.put("FRANCHISEE_FISCAL_ID", "CNPJ");
		labels.put("FRANCHISEES", "Estabelecimentos");
		labels.put("FRANCHISEES_ALL", "TODOS");
		labels.put("FRANCHISOR", "Grupo de Estabelecimentos");
		labels.put("FRANCHISOR_CORPORATE_NAME", "Razão Social");
		labels.put("FRANCHISOR_FISCAL_ID", "CNPJ");
		labels.put("MESSAGES", "Central de Atendimento");
		labels.put("PRODUCT", "Produto");
		labels.put("PRODUCT_LOWER", "produto");
		labels.put("PRODUCTS", "Produtos");
		labels.put("SUPPLIER", "Fornecedor");
		labels.put("SUPPLIER_LOWER", "fornecedor");
		labels.put("SUPPLIERS", "Fornecedores");
		labels.put("SUPPLIERS_ALL", "TODOS");
		labels.put("SUPPLIER_FRANCHISEE", "Estabelecimento");
		labels.put("SUPPLIER_FRANCHISEES", "Estabelecimentos");
		labels.put("SUPPLIER_FRANCHISOR", "Grupo de Estabelecimentos");
		labels.put("SUPPLIER_FRANCHISORS", "Grupos de Estabelecimentos");
		labels.put("SUPPLIER_FRANCHISORS_ALL", "TODAS");
		labels.put("VISIBILITY_FRANCHISEES_ALL", "Todos os estabelecimentos");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_ANNOUNCEMENT", "Restringir aviso apenas para os estabelecimentos selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_CALENDAR", "Restringir evento apenas para os estabelecimentos selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_FILE", "Restringir acesso apenas para os estabelecimentos selecionados abaixo");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_ANNOUNCEMENT", "Caso nenhum estabelecimento seja selecionado, todos poderão visualizar esse aviso");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_CALENDAR", "Caso nenhum estabelecimento seja selecionado, todos poderão visualizar esse evento");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_FILE", "Caso nenhum estabelecimento seja selecionado, todos terão acesso");
		labels.put("VISIBILITY_FRANCHISOR_ALL_FRANCHISEES", "Grupos de Estabelecimentos e Todos os Estabelecimentos");
		labels.put("VISIBILITY_FRANCHISOR_FRANCHISEES", "Estabelecimentos e Grupos de Estabelecimentos");
		labels.put("VISIBILITY_FRANCHISOR_SOME_FRANCHISEES", "Grupos de Estabelecimentos e Alguns Estabelecimentos");
		labels.put("VISIBILITY_ONLY_FRANCHISOR", "Apenas Grupo de Estabelecimentos");
		labels.put("VISIBILITY_SOME_FRANCHISEES", "Alguns Estabelecimentos");
		labels.put("VISIBILITY_ALL_FRANCHISEES", "Todos os Estabelecimentos");
		labels.put("TRAININGS", "Treinamentos");
		gastronomico.setLabels(labels);
		
		featureFlags = new HashMap<String, Boolean>();
		featureFlags.put("FRANCHISEE_DOCUMENTS", false);
		featureFlags.put("SUPPLIER_STOCK", false);
		featureFlags.put("REVIEW_REQUEST", true);
		featureFlags.put("PRODUCTS", true);
		featureFlags.put("TUTORIALS", true);
		featureFlags.put("LOGO", true);
		featureFlags.put("DOCUMENT_FILE_DEFAULT_ACCESS_RESTRICTED", false);
		gastronomico.setFeatureFlags(featureFlags);
		
		configuration = new HashMap<String, String>();
		configuration.put("STOCK_CONSOLIDATION_WINDOW", "1");
		gastronomico.setConfiguration(configuration);
		
		// Configuration - FoodHubs
		DomainConfigurationData foodhubs = new DomainConfigurationData();
		foodhubs.setId(4L);
		foodhubs.setKey(DomainKeysEnum.GASTRONOMICO.name());
		foodhubs.setName("FoodHubs");
		foodhubs.setLoginCss("foodhubs_login.css");
		foodhubs.setMainCss("foodhubs.css");
		foodhubs.setTitle("FoodHubs");
		foodhubs.setFavicon("foodhubs.ico");
		foodhubs.setMainLogo("foodhubs_logo.png");
		foodhubs.setLoginForm("loginFormFoodHubs");
		foodhubs.setMailSender("contato@foodhubs.com.br");
		foodhubs.setMailUser("USERNAME");
		foodhubs.setMailPassword("PASSWORD");
		foodhubs.setMainURL("www.foodhubs.com.br");
		foodhubs.setMailApiKey("API_KEY");
		
		domainConfigurationsByKey.put(foodhubs.getKey(), foodhubs);
		domainConfigurationsByURL.put(foodhubs.getMainURL(), foodhubs);
		domainConfigurationsByURL.put("foodhubs", foodhubs);
		domainConfigurationsByURL.put("foodhubs.com.br", foodhubs);
		domainConfigurationsByURL.put("foodhubs-dot-ocvfranquias.appspot.com", foodhubs);
		domainConfigurationsByURL.put("foodhubs-dot-ocvfranquias-dev.appspot.com", foodhubs);
		
		labels = new HashMap<String, String>();
		labels.put("ANNOUNCEMENTS", "Promoções e Avisos");
		labels.put("CALENDAR", "Calendário");
		labels.put("COMPANY", "empresa");
		labels.put("FILES", "Documentos");
		labels.put("FRANCHISEE", "Estabelecimento");
		labels.put("FRANCHISEE_CORPORATE_NAME", "Razão Social");
		labels.put("FRANCHISEE_FISCAL_ID", "CNPJ");
		labels.put("FRANCHISEES", "Estabelecimentos");
		labels.put("FRANCHISEES_ALL", "TODOS");
		labels.put("FRANCHISOR", "Hub");
		labels.put("FRANCHISOR_CORPORATE_NAME", "Razão Social");
		labels.put("FRANCHISOR_FISCAL_ID", "CNPJ");
		labels.put("MESSAGES", "Central de Atendimento");
		labels.put("PRODUCT", "Produto");
		labels.put("PRODUCT_LOWER", "produto");
		labels.put("PRODUCTS", "Produtos");
		labels.put("SUPPLIER", "Fornecedor");
		labels.put("SUPPLIER_LOWER", "fornecedor");
		labels.put("SUPPLIERS", "Fornecedores");
		labels.put("SUPPLIERS_ALL", "TODOS");
		labels.put("SUPPLIER_FRANCHISEE", "Estabelecimento");
		labels.put("SUPPLIER_FRANCHISEES", "Estabelecimentos");
		labels.put("SUPPLIER_FRANCHISOR", "Hub");
		labels.put("SUPPLIER_FRANCHISORS", "Hubs");
		labels.put("SUPPLIER_FRANCHISORS_ALL", "TODAS");
		labels.put("VISIBILITY_FRANCHISEES_ALL", "Todos os estabelecimentos");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_ANNOUNCEMENT", "Restringir aviso apenas para os estabelecimentos selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_CALENDAR", "Restringir evento apenas para os estabelecimentos selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_FILE", "Restringir acesso apenas para os estabelecimentos selecionados abaixo");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_ANNOUNCEMENT", "Caso nenhum estabelecimento seja selecionado, todos poderão visualizar esse aviso");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_CALENDAR", "Caso nenhum estabelecimento seja selecionado, todos poderão visualizar esse evento");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_FILE", "Caso nenhum estabelecimento seja selecionado, todos terão acesso");
		labels.put("VISIBILITY_FRANCHISOR_ALL_FRANCHISEES", "Hub e Todos os Estabelecimentos");
		labels.put("VISIBILITY_FRANCHISOR_FRANCHISEES", "Estabelecimentos e Hub");
		labels.put("VISIBILITY_FRANCHISOR_SOME_FRANCHISEES", "Hub e Alguns Estabelecimentos");
		labels.put("VISIBILITY_ONLY_FRANCHISOR", "Apenas Hub");
		labels.put("VISIBILITY_SOME_FRANCHISEES", "Alguns Estabelecimentos");
		labels.put("VISIBILITY_ALL_FRANCHISEES", "Todos os Estabelecimentos");
		labels.put("TRAININGS", "Treinamentos");
		foodhubs.setLabels(labels);
		
		featureFlags = new HashMap<String, Boolean>();
		featureFlags.put("FRANCHISEE_DOCUMENTS", false);
		featureFlags.put("SUPPLIER_STOCK", false);
		featureFlags.put("REVIEW_REQUEST", true);
		featureFlags.put("PRODUCTS", true);
		featureFlags.put("TUTORIALS", true);
		featureFlags.put("LOGO", true);
		featureFlags.put("DOCUMENT_FILE_DEFAULT_ACCESS_RESTRICTED", false);
		foodhubs.setFeatureFlags(featureFlags);
		
		configuration = new HashMap<String, String>();
		configuration.put("STOCK_CONSOLIDATION_WINDOW", "1");
		foodhubs.setConfiguration(configuration);

		// Configuration - FoodHub
		DomainConfigurationData foodhub = new DomainConfigurationData();
		foodhub.setId(5L);
		foodhub.setKey(DomainKeysEnum.FOODHUB.name());
		foodhub.setName("FoodHub");
		foodhub.setRedirectTo(DomainKeysEnum.GASTRONOMICO);
		foodhub.setLoginCss("foodhubs_login.css");
		foodhub.setMainCss("foodhubs.css");
		foodhub.setTitle("FoodHub");
		foodhub.setFavicon("foodhubs.ico");
		foodhub.setMainLogo("foodhubs_logo.png");
		foodhub.setLoginForm("loginFormFoodHubs");
		foodhub.setMailSender("contato@foodhub.com.br");
		foodhub.setMailUser("USERNAME");
		foodhub.setMailPassword("PASSWORD");
		foodhub.setMainURL("www.foodhub.com.br");
		foodhub.setMailApiKey("API_KEY");
		
		domainConfigurationsByKey.put(foodhub.getKey(), foodhub);
		domainConfigurationsByURL.put(foodhub.getMainURL(), foodhub);
		domainConfigurationsByURL.put("foodhub", foodhub);
		domainConfigurationsByURL.put("foodhub.com.br", foodhub);
		domainConfigurationsByURL.put("foodhub-dot-ocvfranquias.appspot.com", foodhub);
		domainConfigurationsByURL.put("foodhub-dot-ocvfranquias-dev.appspot.com", foodhub);
		
		labels = new HashMap<String, String>();
		labels.put("ANNOUNCEMENTS", "Promoções e Avisos");
		labels.put("CALENDAR", "Calendário");
		labels.put("COMPANY", "empresa");
		labels.put("FILES", "Documentos");
		labels.put("FRANCHISEE", "Estabelecimento");
		labels.put("FRANCHISEE_CORPORATE_NAME", "Razão Social");
		labels.put("FRANCHISEE_FISCAL_ID", "CNPJ");
		labels.put("FRANCHISEES", "Estabelecimentos");
		labels.put("FRANCHISEES_ALL", "TODOS");
		labels.put("FRANCHISOR", "Hub");
		labels.put("FRANCHISOR_CORPORATE_NAME", "Razão Social");
		labels.put("FRANCHISOR_FISCAL_ID", "CNPJ");
		labels.put("MESSAGES", "Central de Atendimento");
		labels.put("PRODUCT", "Produto");
		labels.put("PRODUCT_LOWER", "produto");
		labels.put("PRODUCTS", "Produtos");
		labels.put("SUPPLIER", "Fornecedor");
		labels.put("SUPPLIER_LOWER", "fornecedor");
		labels.put("SUPPLIERS", "Fornecedores");
		labels.put("SUPPLIERS_ALL", "TODOS");
		labels.put("SUPPLIER_FRANCHISEE", "Estabelecimento");
		labels.put("SUPPLIER_FRANCHISEES", "Estabelecimentos");
		labels.put("SUPPLIER_FRANCHISOR", "Hub");
		labels.put("SUPPLIER_FRANCHISORS", "Hubs");
		labels.put("SUPPLIER_FRANCHISORS_ALL", "TODAS");
		labels.put("VISIBILITY_FRANCHISEES_ALL", "Todos os estabelecimentos");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_ANNOUNCEMENT", "Restringir aviso apenas para os estabelecimentos selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_CALENDAR", "Restringir evento apenas para os estabelecimentos selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_FILE", "Restringir acesso apenas para os estabelecimentos selecionados abaixo");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_ANNOUNCEMENT", "Caso nenhum estabelecimento seja selecionado, todos poderão visualizar esse aviso");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_CALENDAR", "Caso nenhum estabelecimento seja selecionado, todos poderão visualizar esse evento");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_FILE", "Caso nenhum estabelecimento seja selecionado, todos terão acesso");
		labels.put("VISIBILITY_FRANCHISOR_ALL_FRANCHISEES", "Hub e Todos os Estabelecimentos");
		labels.put("VISIBILITY_FRANCHISOR_FRANCHISEES", "Estabelecimentos e Hub");
		labels.put("VISIBILITY_FRANCHISOR_SOME_FRANCHISEES", "Hub e Alguns Estabelecimentos");
		labels.put("VISIBILITY_ONLY_FRANCHISOR", "Apenas Hub");
		labels.put("VISIBILITY_SOME_FRANCHISEES", "Alguns Estabelecimentos");
		labels.put("VISIBILITY_ALL_FRANCHISEES", "Todos os Estabelecimentos");
		labels.put("TRAININGS", "Treinamentos");
		foodhub.setLabels(labels);
		
		featureFlags = new HashMap<String, Boolean>();
		featureFlags.put("FRANCHISEE_DOCUMENTS", false);
		featureFlags.put("SUPPLIER_STOCK", false);
		featureFlags.put("REVIEW_REQUEST", true);
		featureFlags.put("PRODUCTS", true);
		featureFlags.put("TUTORIALS", true);
		featureFlags.put("LOGO", true);
		featureFlags.put("DOCUMENT_FILE_DEFAULT_ACCESS_RESTRICTED", false);
		foodhub.setFeatureFlags(featureFlags);
		
		configuration = new HashMap<String, String>();
		configuration.put("STOCK_CONSOLIDATION_WINDOW", "1");
		foodhub.setConfiguration(configuration);
		
		//Development configuration, for testing
		DomainConfigurationData development = new DomainConfigurationData();
		development.setId(99L);
		development.setKey(DomainKeysEnum.DEVELOPMENT.name());
		development.setName("Desenvolvimento");
		development.setLoginCss("login_development.css");
		development.setMainCss("development.css");
		development.setTitle("Sistema Desenvolvimento");
		development.setFavicon("development.ico");
		development.setMainLogo("logo_development.png");
		development.setLoginForm("loginFormDevelopment");
		development.setMailSender("contato@sistemaparafranquias.com.br");
		development.setMailUser("USERNAME");
		development.setMailPassword("PASSWORD");
		development.setMainURL("www.sistemaparafranquias.com.br");
		development.setMailApiKey("API_KEY");
		
		domainConfigurationsByKey.put(development.getKey(), development);
		domainConfigurationsByURL.put("development", development);
		
		labels = new HashMap<String, String>();
		labels.put("ANNOUNCEMENTS", "ANNOUNCEMENTS");
		labels.put("CALENDAR", "CALENDAR");
		labels.put("FILES", "FILES");
		labels.put("FRANCHISEE", "FRANCHISEE");
		labels.put("FRANCHISEE_CORPORATE_NAME", "FRANCHISEER_CORPORATE_NAME");
		labels.put("FRANCHISEE_FISCAL_ID", "FRANCHISEE_FISCAL_ID");
		labels.put("FRANCHISEES", "FRANCHISEES");
		labels.put("FRANCHISEES_ALL", "FRANCHISEES_ALL");
		labels.put("FRANCHISOR", "FRANCHISOR");
		labels.put("FRANCHISOR_CORPORATE_NAME", "FRANCHISOR_CORPORATE_NAME");
		labels.put("FRANCHISOR_FISCAL_ID", "FRANCHISOR_FISCAL_ID");
		labels.put("MESSAGES", "MESSAGES");
		labels.put("PRODUCT", "PRODUCT");
		labels.put("PRODUCT_LOWER", "PRODUCT_LOWER");
		labels.put("PRODUCTS", "PRODUCTS");
		labels.put("SUPPLIER", "SUPPLIER");
		labels.put("SUPPLIER_LOWER", "SUPPLIER_LOWER");
		labels.put("SUPPLIERS", "SUPPLIERS");
		labels.put("SUPPLIERS_ALL", "SUPPLIERS_ALL");
		labels.put("SUPPLIER_FRANCHISEE", "SUPPLIER_FRANCHISEE");
		labels.put("SUPPLIER_FRANCHISEES", "SUPPLIER_FRANCHISEES");
		labels.put("SUPPLIER_FRANCHISOR", "SUPPLIER_FRANCHISOR");
		labels.put("SUPPLIER_FRANCHISORS", "SUPPLIER_FRANCHISORS");
		labels.put("SUPPLIER_FRANCHISORS_ALL", "SUPPLIER_FRANCHISORS_ALL");
		labels.put("VISIBILITY_FRANCHISEES_ALL", "VISIBILITY_FRANCHISEES_ALL");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_ANNOUNCEMENT", "VISIBILITY_FRANCHISEES_FILTER_ANNOUNCEMENT");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_CALENDAR", "VISIBILITY_FRANCHISEES_FILTER_CALENDAR");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_FILE", "VISIBILITY_FRANCHISEES_FILTER_FILE");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_ANNOUNCEMENT", "VISIBILITY_FRANCHISEES_WARNING_ANNOUNCEMENT");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_CALENDAR", "VISIBILITY_FRANCHISEES_WARNING_CALENDAR");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_FILE", "VISIBILITY_FRANCHISEES_WARNING_FILE");
		labels.put("VISIBILITY_FRANCHISOR_ALL_FRANCHISEES", "VISIBILITY_FRANCHISOR_ALL_FRANCHISEES");
		labels.put("VISIBILITY_FRANCHISOR_FRANCHISEES", "VISIBILITY_FRANCHISOR_FRANCHISEES");
		labels.put("VISIBILITY_FRANCHISOR_SOME_FRANCHISEES", "VISIBILITY_FRANCHISOR_SOME_FRANCHISEES");
		labels.put("VISIBILITY_ONLY_FRANCHISOR", "VISIBILITY_ONLY_FRANCHISOR");
		labels.put("VISIBILITY_SOME_FRANCHISEES", "VISIBILITY_SOME_FRANCHISEES");
		labels.put("VISIBILITY_ALL_FRANCHISEES", "VISIBILITY_ALL_FRANCHISEES");
		labels.put("TRAININGS", "TRAININGS");
		development.setLabels(labels);
		
		featureFlags = new HashMap<String, Boolean>();
		featureFlags.put("FRANCHISEE_DOCUMENTS", true);
		featureFlags.put("SUPPLIER_STOCK", true);
		featureFlags.put("REVIEW_REQUEST", true);
		featureFlags.put("PRODUCTS", true);
		featureFlags.put("TUTORIALS", true);
		featureFlags.put("LOGO", true);
		featureFlags.put("DOCUMENT_FILE_DEFAULT_ACCESS_RESTRICTED", false);
		development.setFeatureFlags(featureFlags);
		
		configuration = new HashMap<String, String>();
		configuration.put("STOCK_CONSOLIDATION_WINDOW", "0");
		development.setConfiguration(configuration);
		
		// Configuration - Usa Sistemas
		DomainConfigurationData sistemas = new DomainConfigurationData();
		sistemas.setId(100L);
		sistemas.setKey(DomainKeysEnum.USA_SISTEMAS.name());
		sistemas.setName("UsaSistemas");
		sistemas.setLoginCss("usasistemas_login.css");
		sistemas.setMainCss("usasistemas.css");
		sistemas.setTitle("Usa | Sistemas");
		sistemas.setFavicon("usasistemas.ico");
		sistemas.setMainLogo("usasistemas_logo.png");
		sistemas.setLoginForm("loginFormUsaSistemas");
		sistemas.setMailSender("contato@usasistemas.com.br");
		sistemas.setMailUser("USERNAME");
		sistemas.setMailPassword("PASSWORD");
		sistemas.setMainURL("www.usasistemas.com.br");
		sistemas.setMailApiKey("API_KEY");
		
		domainConfigurationsByKey.put(sistemas.getKey(), sistemas);
		domainConfigurationsByURL.put(sistemas.getMainURL(), sistemas);
		domainConfigurationsByURL.put("usasistemas", sistemas);
		domainConfigurationsByURL.put("usasistemas.com.br", sistemas);
		domainConfigurationsByURL.put("usasistemas-dot-ocvfranquias.appspot.com", sistemas);
		domainConfigurationsByURL.put("usasistemas-dot-ocvfranquias-dev.appspot.com", sistemas);
		
		labels = new HashMap<String, String>();
		sistemas.setLabels(labels);
		
		featureFlags = new HashMap<String, Boolean>();
		featureFlags.put("FRANCHISEE_DOCUMENTS", true);
		featureFlags.put("SUPPLIER_STOCK", true);
		featureFlags.put("REVIEW_REQUEST", false);
		featureFlags.put("PRODUCTS", true);
		featureFlags.put("TUTORIALS", true);
		featureFlags.put("LOGO", true);
		featureFlags.put("DOCUMENT_FILE_DEFAULT_ACCESS_RESTRICTED", false);
		sistemas.setFeatureFlags(featureFlags);
		
		configuration = new HashMap<String, String>();
		configuration.put("STOCK_CONSOLIDATION_WINDOW", "0");
		sistemas.setConfiguration(configuration);

		// Configuration - Intervencao Comportamental
		DomainConfigurationData ic = new DomainConfigurationData();
		ic.setId(200L);
		ic.setKey(DomainKeysEnum.INTERVENCAO_COMPORTAMENTAL.name());
		ic.setName("Intervencao Comportamental");
		ic.setLoginCss("intervencaocomportamental_login.css");
		ic.setMainCss("intervencaocomportamental.css");
		ic.setTitle("Intervenção Comportamental");
		ic.setFavicon("usasistemas.ico");
		ic.setMainLogo("usafranquias_logo.png");
		ic.setLoginForm("loginFormIntervencaoComportamental");
		ic.setMailSender("administrador@usasistemas.com.br");
		ic.setMailUser("USERNAME");
		ic.setMailPassword("PASSWORD");
		ic.setMainURL("www.intervencaocomportamental.com.br");
		ic.setMailApiKey("API_KEY");
		
		domainConfigurationsByKey.put(ic.getKey(), ic);
		domainConfigurationsByURL.put(ic.getMainURL(), ic);
		domainConfigurationsByURL.put("intervencaocomportamental", ic);
		domainConfigurationsByURL.put("intervencaocomportamental.com.br", ic);
		domainConfigurationsByURL.put("intervencaocomportamental.appspot.com", ic);
		domainConfigurationsByURL.put("test-dot-intervencaocomportamental.appspot.com", ic);
		
		labels = new HashMap<String, String>();
		labels.put("ANNOUNCEMENTS", "Avisos");
		labels.put("CALENDAR", "Calendário");
		labels.put("FILES", "Documentos");
		labels.put("FRANCHISEE", "Paciente");
		labels.put("FRANCHISEE_CORPORATE_NAME", "Nome Completo");
		labels.put("FRANCHISEE_FISCAL_ID", "CPF");
		labels.put("FRANCHISEES", "Pacientes");
		labels.put("FRANCHISEES_ALL", "TODOS");
		labels.put("FRANCHISOR", "Analista");
		labels.put("FRANCHISOR_CORPORATE_NAME", "Nome Completo");
		labels.put("FRANCHISOR_FISCAL_ID", "CPF/CNPJ");
		labels.put("MESSAGES", "Mensagens");
		labels.put("PRODUCT", "Produto");
		labels.put("PRODUCT_LOWER", "produto");
		labels.put("PRODUCTS", "Produtos");
		labels.put("SUPPLIER", "Fornecedor");
		labels.put("SUPPLIER_LOWER", "fornecedor");
		labels.put("SUPPLIERS", "Fornecedores");
		labels.put("SUPPLIERS_ALL", "TODOS");
		labels.put("SUPPLIER_FRANCHISEE", "Paciente");
		labels.put("SUPPLIER_FRANCHISEES", "Pacientes");
		labels.put("SUPPLIER_FRANCHISOR", "Analista");
		labels.put("SUPPLIER_FRANCHISORS", "Analistas");
		labels.put("SUPPLIER_FRANCHISORS_ALL", "TODOS");
		labels.put("VISIBILITY_FRANCHISEES_ALL", "Todos os pacientes");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_ANNOUNCEMENT", "Restringir aviso apenas para os pacientes selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_CALENDAR", "Restringir evento apenas para os pacientes selecionados");
		labels.put("VISIBILITY_FRANCHISEES_FILTER_FILE", "Restringir acesso apenas para os pacientes selecionados abaixo");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_ANNOUNCEMENT", "Caso nenhum pacientes seja selecionado, todos poderão visualizar esse aviso");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_CALENDAR", "Caso nenhum pacientes seja selecionado, todos poderão visualizar esse evento");
		labels.put("VISIBILITY_FRANCHISEES_WARNING_FILE", "Caso nenhum pacientes seja selecionado, todos terão acesso");
		labels.put("VISIBILITY_FRANCHISOR_ALL_FRANCHISEES", "Analista e Todos os Pacientes");
		labels.put("VISIBILITY_FRANCHISOR_FRANCHISEES", "Analista e Pacientes");
		labels.put("VISIBILITY_FRANCHISOR_SOME_FRANCHISEES", "Analista e Alguns Pacientes");
		labels.put("VISIBILITY_ONLY_FRANCHISOR", "Apenas Analista");
		labels.put("VISIBILITY_SOME_FRANCHISEES", "Alguns Pacientes");
		labels.put("VISIBILITY_ALL_FRANCHISEES", "Todos os Pacientes");
		labels.put("TRAININGS", "Videos");
		ic.setLabels(labels);
		
		featureFlags = new HashMap<String, Boolean>();
		featureFlags.put("FRANCHISEE_DOCUMENTS", true);
		featureFlags.put("SUPPLIER_STOCK", true);
		featureFlags.put("REVIEW_REQUEST", false);
		featureFlags.put("PRODUCTS", false);
		featureFlags.put("TUTORIALS", false);
		featureFlags.put("LOGO", false);
		featureFlags.put("DOCUMENT_FILE_DEFAULT_ACCESS_RESTRICTED", true);
		ic.setFeatureFlags(featureFlags);
		
		configuration = new HashMap<String, String>();
		configuration.put("STOCK_CONSOLIDATION_WINDOW", "7");
		ic.setConfiguration(configuration);
	}

}
