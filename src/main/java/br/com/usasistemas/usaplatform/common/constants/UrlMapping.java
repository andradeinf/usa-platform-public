package br.com.usasistemas.usaplatform.common.constants;

public class UrlMapping {
	
	/*
	 * RESOURCES
	 */
	
	//controllers
	public static final String SETUP = "/setup";
	
	//views
	public static final String OK_VIEW = "/resources/ok";
	
	/*
	 * LOGIN
	 */
	
	//controllers
	public static final String LOGIN = "/login";
	public static final String RESET_PASSWORD = "/resetPassword";
	public static final String RESET_PASSWORD_DO = "/resetPassword/do";
	public static final String RESET_PASSWORD_REQUEST = "/resetPasswordRequest";
	public static final String RESET_PASSWORD_REQUEST_DO = "/resetPasswordRequest/do";
	
	//views
	public static final String LOGIN_HOME_VIEW = "/login/home";
	public static final String LOGIN_FORM_VIEW = "/login/loginForm";
	public static final String LOGIN_FORM_DEVELOPMENT_VIEW = "/login/loginFormDevelopment";
	public static final String LOGIN_FORM_USA_FOOD_VIEW = "/login/loginFormUsaFood";
	public static final String LOGIN_FORM_FOODHUBS_VIEW = "/login/loginFormFoodHubs";
	public static final String LOGIN_FORM_USA_FRANQUIAS_VIEW = "/login/loginFormUsaFranquias";
	public static final String LOGIN_FORM_USA_FRANQUIAS_LUZDALUA_VIEW = "/login/loginFormUsaFranquiasLuzDaLua";
	public static final String LOGIN_FORM_USA_SISTEMAS_VIEW = "/login/loginFormUsaSistemas";
	public static final String LOGIN_FORM_INTERVENCAO_COMPORTAMENTAL_VIEW = "/login/loginFormIntervencaoComportamental";
	public static final String LOGIN_FORM_CUSTOM_VIEW = "/login/loginFormCustom";
	public static final String RESET_PASSWORD_FORM_VIEW = "/login/resetPasswordForm";
	public static final String CHANGE_PASSWORD_FORM_VIEW = "/login/changePasswordForm";
	public static final String PROFILE_SELECTION_VIEW = "/login/profileSelection";
	
	/*
	 * ADMIN
	 */
	
	//controllers
	public static final String ADMIN = "/admin";
	public static final String FRANCHISORS = "/franchisors";
	public static final String FRANCHISEES = "/franchisees";
	public static final String SUPPLIERS = "/suppliers";
	public static final String USERS = "/users";
	public static final String SYSTEM_CONFIGURATION = "/systemConfiguration";
	public static final String TUTORIALS = "/tutorials";
	public static final String TRAININGS = "/trainings";
	
	//views	
	public static final String ADMIN_HOME_VIEW = "/admin/home";	
	public static final String ADMIN_LANDING_VIEW = "/admin/landing";
	public static final String FRANCHISORS_VIEW = "/admin/franchisors";
	public static final String FRANCHISOR_USERS_VIEW = "/admin/franchisorUsers";
	public static final String FRANCHISEES_VIEW = "/admin/franchisees";
	public static final String FRANCHISEE_USERS_VIEW = "/admin/franchiseeUsers";
	public static final String PRODUCTS_VIEW = "/admin/products";
	public static final String PRODUCT_CATEGORIES_VIEW = "/admin/productCategories";
	public static final String SUPPLIERS_VIEW = "/admin/suppliers";
	public static final String SUPPLIER_CATEGORIES_VIEW = "/admin/supplierCategories";
	public static final String SUPPLIER_USERS_VIEW = "/admin/supplierUsers";
	public static final String SUPPLIER_FRANCHISORS_VIEW = "/admin/supplierFranchisors";
	public static final String ADMIN_DELIVERIES_BY_SUPPLIER_REPORT_VIEW = "/admin/deliveriesBySupplierReport";
	public static final String ADMIN_USERS_VIEW = "/admin/users";
	public static final String ADMIN_USER_GROUPS_VIEW = "/admin/userGroups";
	public static final String ADMIN_SYSTEM_CONFIGURATION_VIEW = "/admin/systemConfiguration";
	public static final String ADMIN_TUTORIALS_VIEW = "/admin/tutorials";
	
	//resources
	public static final String FRANCHISORS_RESOURCE = "/ws/franchisors";
	public static final String FRANCHISEES_RESOURCE = "/ws/franchisees";
	public static final String PRODUCTS_RESOURCE = "/ws/products";
	public static final String USERS_RESOURCE = "/ws/users";
	public static final String MANUFACTURE_SERVICE = "/ws/manufacture";
	public static final String ANNOUNCEMENT_SERVICE = "/ws/announcements";
	public static final String LOGIN_SERVICE = "/ws/login";
	public static final String DELIVERY_SERVICE = "/ws/delivery";
	public static final String IMAGE_SERVICE = "/ws/image";
	public static final String SUPPLIERS_RESOURCE = "/ws/suppliers";
	public static final String USERS_CONFIGURATION = "/ws/configuration";
	public static final String MESSAGES_SERVICE = "/ws/messages";
	public static final String CALENDAR_SERVICE = "/ws/calendar";
	public static final String STATES_AND_CITIES_RESOURCE = "/ws/statesAndCities";
	public static final String REVIEW_SERVICE = "/ws/review";
	public static final String CONTACT_SERVICE = "/ws/contact";
	public static final String REPORT_SERVICE = "/ws/report";
	public static final String DOCUMENTS_SERVICE = "/ws/documents";
	public static final String TRAINING_RESOURCE = "/ws/trainings";
	public static final String TUTORIALS_RESOURCE = "/ws/tutorials";
	
	//jobs
	public static final String STOCK_JOB = "/job/stock";
	public static final String REPORT_JOB = "/job/report";
	public static final String MAIL_NOTIFICATION_JOB = "/job/mailNotification";
	public static final String DELIVERY_JOB = "/job/delivery";
	public static final String ASYNC_JOB = "/job/async";
	public static final String INDEXING_JOB = "/job/indexing";
	
	/*
	 * FRANCHISOR
	 */
	
	//controller
	public static final String FRANCHISOR = "/franchisor";
	
	//view
	public static final String FRANCHISOR_HOME_VIEW = "/franchisor/home";
	public static final String FRANCHISOR_MAIN_VIEW = "/franchisor/main";
	public static final String FRANCHISOR_PRODUCTS_VIEW = "/franchisor/products";
	public static final String FRANCHISOR_MY_MANUFACTURE_REQUESTS_VIEW = "/franchisor/myManufactureRequests";
	public static final String FRANCHISOR_FRANCHISEES_DELIVERY_REQUESTS_VIEW = "/franchisor/franchiseesDeliveryRequests";
	public static final String FRANCHISOR_DELIVERY_TIME_RANGE_REPORT_VIEW = "/franchisor/deliverieRequestsByTimeRangeReport";
	public static final String FRANCHISOR_FRANCHISEE_MESSAGES_TIME_RANGE_REPORT_VIEW = "/franchisor/franchiseeMessagesByTimeRangeReport";
	public static final String FRANCHISOR_ANNOUNCEMENTS_VIEW = "/franchisor/announcements";
	public static final String FRANCHISOR_CALENDAR_VIEW = "/franchisor/calendar";
	public static final String FRANCHISOR_DOCUMENTS_VIEW = "/franchisor/documents";
	public static final String FRANCHISOR_SUPPLIERS_VIEW = "/franchisor/suppliers";
	public static final String FRANCHISOR_TUTORIALS_VIEW = "/franchisor/tutorials";
	public static final String FRANCHISOR_TRAININGS_VIEW = "/franchisor/trainings";
	
	/*
	 * FRANCHISEE
	 */
	
	//controller
	public static final String FRANCHISEE = "/franchisee";
	
	//view
	public static final String FRANCHISEE_HOME_VIEW = "/franchisee/home";
	public static final String FRANCHISEE_MAIN_VIEW = "/franchisee/main";
	public static final String FRANCHISEE_PRODUCTS_VIEW = "/franchisee/products";
	public static final String FRANCHISEE_MY_DELIVERY_REQUESTS_VIEW = "/franchisee/myDeliveryRequests";
	public static final String FRANCHISEE_DELIVERY_TIME_RANGE_REPORT_VIEW = "/franchisee/franchiseeDeliveriesByTimeRangeReport";
	public static final String FRANCHISEE_ACCOUNTS_PAYABLE_REPORT_VIEW = "/franchisee/accountsPayableReport";
	public static final String FRANCHISEE_ANNOUNCEMENTS_VIEW = "/franchisee/announcements";
	public static final String FRANCHISEE_CALENDAR_VIEW = "/franchisee/calendar";
	public static final String FRANCHISEE_DOCUMENTS_VIEW = "/franchisee/documents";
	public static final String FRANCHISEE_TUTORIALS_VIEW = "/franchisee/tutorials";
	public static final String FRANCHISEE_TRAININGS_VIEW = "/franchisee/trainings";
	public static final String FRANCHISEE_SUPPLIERS_VIEW = "/franchisee/suppliers";
	
	/*
	 * SUPPLIER
	 */
	
	//controller
	public static final String SUPPLIER = "/supplier";
	
	//view
	public static final String SUPPLIER_HOME_VIEW = "/supplier/home";
	public static final String SUPPLIER_REQUESTS_VIEW = "/supplier/requests";
	public static final String SUPPLIER_DELIVERY_REQUESTS_VIEW = "/supplier/deliveryRequests";
	public static final String SUPPLIER_DELIVERY_REQUESTS_PRINT_VIEW = "/supplier/deliveryRequestsPrint";
	public static final String SUPPLIER_MANUFACTURE_REQUESTS_VIEW = "/supplier/manufactureRequests";
	public static final String SUPPLIER_PRODUCTS_VIEW = "/supplier/products";
	public static final String SUPPLIER_STOCK_VIEW = "/supplier/stock";
	public static final String SUPPLIER_DELIVERY_REPORT_VIEW = "/supplier/deliveryReport";
	public static final String SUPPLIER_DELIVERY_TIME_RANGE_REPORT_VIEW = "/supplier/supplierDeliveriesByTimeRangeReport";
	public static final String SUPPLIER_ACCOUNTS_RECEIVABLE_REPORT_VIEW = "/supplier/accountsReceivableReport";
	public static final String SUPPLIER_TUTORIALS_VIEW = "/supplier/tutorials";

	/*
	 * MAIN
	 */
	
	//view
	public static final String CONFIGURATION_VIEW = "/configuration";
	public static final String MESSAGES_VIEW = "/messages";
	public static final String MESSAGE_LABELS_VIEW = "/messageLabels";
	public static final String ERROR_VIEW = "/error";
	
	//controllers
	public static final String HOME = "/";
	public static final String CONFIGURATION = "/configuration";
	
}
