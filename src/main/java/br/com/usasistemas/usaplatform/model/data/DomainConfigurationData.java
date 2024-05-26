package br.com.usasistemas.usaplatform.model.data;

import java.util.Map;

import br.com.usasistemas.usaplatform.model.enums.DomainKeysEnum;

public class DomainConfigurationData {
	
	private Long id;
	private String key;
	private String name;
	private String loginCss;
	private String mainCss;
	private String title;
	private String favicon;
	private String mainLogo;
	private String loginForm;
	private String mailSender;
	private String mailUser;
	private String mailPassword;
	private String mailApiKey;
	private String mainURL;
	private DomainKeysEnum redirectTo;
	private Map<String, String> labels;
	private Map<String, Boolean> featureFlags;
	private Map<String, String> configuration;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginCss() {
		return loginCss;
	}

	public void setLoginCss(String loginCss) {
		this.loginCss = loginCss;
	}

	public String getMainCss() {
		return mainCss;
	}

	public void setMainCss(String mainCss) {
		this.mainCss = mainCss;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFavicon() {
		return favicon;
	}

	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}

	public String getMainLogo() {
		return mainLogo;
	}

	public void setMainLogo(String mainLogo) {
		this.mainLogo = mainLogo;
	}

	public String getLoginForm() {
		return loginForm;
	}

	public void setLoginForm(String loginForm) {
		this.loginForm = loginForm;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public String getMailSender() {
		return mailSender;
	}

	public void setMailSender(String mailSender) {
		this.mailSender = mailSender;
	}

	public String getMailUser() {
		return mailUser;
	}

	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	public String getMainURL() {
		return mainURL;
	}

	public void setMainURL(String mainURL) {
		this.mainURL = mainURL;
	}

	public DomainKeysEnum getRedirectTo() {
		return redirectTo;
	}

	public void setRedirectTo(DomainKeysEnum redirectTo) {
		this.redirectTo = redirectTo;
	}

	public String getMailApiKey() {
		return mailApiKey;
	}

	public void setMailApiKey(String mailApiKey) {
		this.mailApiKey = mailApiKey;
	}

	public Map<String, Boolean> getFeatureFlags() {
		return featureFlags;
	}

	public void setFeatureFlags(Map<String, Boolean> featureFlags) {
		this.featureFlags = featureFlags;
	}

	public Map<String, String> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}
	
}
