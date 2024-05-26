package br.com.usasistemas.usaplatform.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.utils.SystemProperty;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.PasswordUtil;
import br.com.usasistemas.usaplatform.dao.AdministratorUserDAO;
import br.com.usasistemas.usaplatform.dao.AnnouncementDAO;
import br.com.usasistemas.usaplatform.dao.CityDAO;
import br.com.usasistemas.usaplatform.dao.DeliveryRequestDAO;
import br.com.usasistemas.usaplatform.dao.DeliveryRequestHistoryDAO;
import br.com.usasistemas.usaplatform.dao.DocumentsFileDAO;
import br.com.usasistemas.usaplatform.dao.DocumentsFolderDAO;
import br.com.usasistemas.usaplatform.dao.DocumentsFranchiseeIndexDAO;
import br.com.usasistemas.usaplatform.dao.FranchiseeDAO;
import br.com.usasistemas.usaplatform.dao.FranchiseeUserDAO;
import br.com.usasistemas.usaplatform.dao.FranchisorDAO;
import br.com.usasistemas.usaplatform.dao.FranchisorUserDAO;
import br.com.usasistemas.usaplatform.dao.ImageDAO;
import br.com.usasistemas.usaplatform.dao.LetsEncryptChallengeDAO;
import br.com.usasistemas.usaplatform.dao.ManufactureRequestDAO;
import br.com.usasistemas.usaplatform.dao.ManufactureRequestHistoryDAO;
import br.com.usasistemas.usaplatform.dao.MessageCommentDAO;
import br.com.usasistemas.usaplatform.dao.MessageTopicDAO;
import br.com.usasistemas.usaplatform.dao.NotificationDAO;
import br.com.usasistemas.usaplatform.dao.ProductCategoryDAO;
import br.com.usasistemas.usaplatform.dao.ProductDAO;
import br.com.usasistemas.usaplatform.dao.ProductSizeDAO;
import br.com.usasistemas.usaplatform.dao.ReviewRequestDAO;
import br.com.usasistemas.usaplatform.dao.SetupDAO;
import br.com.usasistemas.usaplatform.dao.StateDAO;
import br.com.usasistemas.usaplatform.dao.SupplierCategoryDAO;
import br.com.usasistemas.usaplatform.dao.SupplierDAO;
import br.com.usasistemas.usaplatform.dao.SupplierFranchisorDAO;
import br.com.usasistemas.usaplatform.dao.SupplierUserDAO;
import br.com.usasistemas.usaplatform.dao.TimeRangeReportDAO;
import br.com.usasistemas.usaplatform.dao.TutorialDAO;
import br.com.usasistemas.usaplatform.dao.UserDAO;
import br.com.usasistemas.usaplatform.dao.UserGroupDAO;
import br.com.usasistemas.usaplatform.dao.UserGroupEntityUserDAO;
import br.com.usasistemas.usaplatform.dao.response.AnnouncementPagedResponse;
import br.com.usasistemas.usaplatform.dao.response.NotificationPagedResponse;
import br.com.usasistemas.usaplatform.model.data.AdministratorUserData;
import br.com.usasistemas.usaplatform.model.data.CityData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.FranchisorUserData;
import br.com.usasistemas.usaplatform.model.data.LetsEncryptChallengeData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestHistoryData;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.PasswordData;
import br.com.usasistemas.usaplatform.model.data.ProductCategoryData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.data.StateData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.SupplierFranchisorData;
import br.com.usasistemas.usaplatform.model.data.SupplierUserData;
import br.com.usasistemas.usaplatform.model.data.UserData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;
import br.com.usasistemas.usaplatform.model.data.UserGroupEntityUserData;
import br.com.usasistemas.usaplatform.model.enums.AnnouncementStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.NotificationTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.SETUP)
public class SetupService{
	
	private static final Logger log = Logger.getLogger(SetupService.class.getName());
	private UserDAO userDAO;
	private UserGroupDAO userGroupDAO;
	private UserGroupEntityUserDAO userGroupEntityUserDAO;
	private AdministratorUserDAO administratorUserDAO;
	private DeliveryRequestDAO deliveryRequestDAO;
	private DeliveryRequestHistoryDAO deliveryRequestHistoryDAO;
	private DocumentsFileDAO documentsFileDAO;
	private DocumentsFolderDAO documentsFolderDAO;
	private DocumentsFranchiseeIndexDAO documentsFranchiseeIndexDAO;
	private FranchiseeDAO franchiseeDAO;
	private FranchiseeUserDAO franchiseeUserDAO;
	private FranchisorDAO franchisorDAO;
	private FranchisorUserDAO franchisorUserDAO;
	private ImageDAO imageDAO;
	private LetsEncryptChallengeDAO letsEncryptChallengeDAO;
	private ManufactureRequestDAO manufactureRequestDAO;
	private ManufactureRequestHistoryDAO manufactureRequestHistoryDAO;
	private ProductCategoryDAO productCategoryDAO;
	private ProductDAO productDAO;
	private ProductSizeDAO productSizeDAO;
	private SupplierCategoryDAO supplierCategoryDAO;
	private SupplierDAO supplierDAO;
	private SupplierFranchisorDAO supplierFranchisorDAO;
	private SupplierUserDAO supplierUserDAO;
	private StateDAO stateDAO;
	private CityDAO cityDAO;
	private TutorialDAO tutorialDAO;
	private SetupDAO setupDAO;
	private AnnouncementDAO announcementDAO;
	private MessageTopicDAO messageTopicDAO;
	private MessageCommentDAO messageCommentDAO;
	private NotificationDAO notificationDAO;
	private ReviewRequestDAO reviewRequestDAO;
	private TimeRangeReportDAO timeRangeReportDAO;
	
	private final String STATE_FILE = "resources/states.csv";
	private final String CITY_FILE = "resources/cities.csv";
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserGroupDAO getUserGroupDAO() {
		return userGroupDAO;
	}

	public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
		this.userGroupDAO = userGroupDAO;
	}

	public UserGroupEntityUserDAO getUserGroupEntityUserDAO() {
		return userGroupEntityUserDAO;
	}

	public void setUserGroupEntityUserDAO(UserGroupEntityUserDAO userGroupEntityUserDAO) {
		this.userGroupEntityUserDAO = userGroupEntityUserDAO;
	}

	public AdministratorUserDAO getAdministratorUserDAO() {
		return administratorUserDAO;
	}

	public void setAdministratorUserDAO(AdministratorUserDAO administratorUserDAO) {
		this.administratorUserDAO = administratorUserDAO;
	}

	public DeliveryRequestDAO getDeliveryRequestDAO() {
		return deliveryRequestDAO;
	}

	public void setDeliveryRequestDAO(DeliveryRequestDAO deliveryRequestDAO) {
		this.deliveryRequestDAO = deliveryRequestDAO;
	}

	public DeliveryRequestHistoryDAO getDeliveryRequestHistoryDAO() {
		return deliveryRequestHistoryDAO;
	}

	public void setDeliveryRequestHistoryDAO(DeliveryRequestHistoryDAO deliveryRequestHistoryDAO) {
		this.deliveryRequestHistoryDAO = deliveryRequestHistoryDAO;
	}

	public DocumentsFileDAO getDocumentsFileDAO() {
		return documentsFileDAO;
	}

	public void setDocumentsFileDAO(DocumentsFileDAO documentsFileDAO) {
		this.documentsFileDAO = documentsFileDAO;
	}

	public DocumentsFolderDAO getDocumentsFolderDAO() {
		return documentsFolderDAO;
	}

	public void setDocumentsFolderDAO(DocumentsFolderDAO documentsFolderDAO) {
		this.documentsFolderDAO = documentsFolderDAO;
	}

	public DocumentsFranchiseeIndexDAO getDocumentsFfranchiseeIndexDAO() {
		return documentsFranchiseeIndexDAO;
	}

	public void setDocumentsFranchiseeIndexDAO(DocumentsFranchiseeIndexDAO documentsFranchiseeIndexDAO) {
		this.documentsFranchiseeIndexDAO = documentsFranchiseeIndexDAO;
	}

	public FranchiseeDAO getFranchiseeDAO() {
		return franchiseeDAO;
	}

	public void setFranchiseeDAO(FranchiseeDAO franchiseeDAO) {
		this.franchiseeDAO = franchiseeDAO;
	}

	public FranchiseeUserDAO getFranchiseeUserDAO() {
		return franchiseeUserDAO;
	}

	public void setFranchiseeUserDAO(FranchiseeUserDAO franchiseeUserDAO) {
		this.franchiseeUserDAO = franchiseeUserDAO;
	}

	public FranchisorDAO getFranchisorDAO() {
		return franchisorDAO;
	}

	public void setFranchisorDAO(FranchisorDAO franchisorDAO) {
		this.franchisorDAO = franchisorDAO;
	}

	public FranchisorUserDAO getFranchisorUserDAO() {
		return franchisorUserDAO;
	}

	public void setFranchisorUserDAO(FranchisorUserDAO franchisorUserDAO) {
		this.franchisorUserDAO = franchisorUserDAO;
	}

	public ImageDAO getImageDAO() {
		return imageDAO;
	}

	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	public LetsEncryptChallengeDAO getLetsEncryptChallengeDAO() {
		return letsEncryptChallengeDAO;
	}

	public void setLetsEncryptChallengeDAO(LetsEncryptChallengeDAO letsEncryptChallengeDAO) {
		this.letsEncryptChallengeDAO = letsEncryptChallengeDAO;
	}

	public ManufactureRequestDAO getManufactureRequestDAO() {
		return manufactureRequestDAO;
	}

	public void setManufactureRequestDAO(ManufactureRequestDAO manufactureRequestDAO) {
		this.manufactureRequestDAO = manufactureRequestDAO;
	}

	public ManufactureRequestHistoryDAO getManufactureRequestHistoryDAO() {
		return manufactureRequestHistoryDAO;
	}

	public void setManufactureRequestHistoryDAO(ManufactureRequestHistoryDAO manufactureRequestHistoryDAO) {
		this.manufactureRequestHistoryDAO = manufactureRequestHistoryDAO;
	}

	public ProductCategoryDAO getProductCategoryDAO() {
		return productCategoryDAO;
	}

	public void setProductCategoryDAO(ProductCategoryDAO productCategoryDAO) {
		this.productCategoryDAO = productCategoryDAO;
	}

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public ProductSizeDAO getProductSizeDAO() {
		return productSizeDAO;
	}

	public void setProductSizeDAO(ProductSizeDAO productSizeDAO) {
		this.productSizeDAO = productSizeDAO;
	}

	public SupplierCategoryDAO getSupplierCategoryDAO() {
		return supplierCategoryDAO;
	}

	public void setSupplierCategoryDAO(SupplierCategoryDAO supplierCategoryDAO) {
		this.supplierCategoryDAO = supplierCategoryDAO;
	}

	public SupplierDAO getSupplierDAO() {
		return supplierDAO;
	}

	public void setSupplierDAO(SupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}

	public SupplierUserDAO getSupplierUserDAO() {
		return supplierUserDAO;
	}

	public SupplierFranchisorDAO getSupplierFranchisorDAO() {
		return supplierFranchisorDAO;
	}

	public void setSupplierFranchisorDAO(SupplierFranchisorDAO supplierFranchisorDAO) {
		this.supplierFranchisorDAO = supplierFranchisorDAO;
	}

	public void setSupplierUserDAO(SupplierUserDAO supplierUserDAO) {
		this.supplierUserDAO = supplierUserDAO;
	}

	public StateDAO getStateDAO() {
		return stateDAO;
	}

	public void setStateDAO(StateDAO stateDAO) {
		this.stateDAO = stateDAO;
	}

	public CityDAO getCityDAO() {
		return cityDAO;
	}

	public void setCityDAO(CityDAO cityDAO) {
		this.cityDAO = cityDAO;
	}

	public TutorialDAO getTutorialDAO() {
		return tutorialDAO;
	}

	public void setTutorialDAO(TutorialDAO tutorialDAO) {
		this.tutorialDAO = tutorialDAO;
	}

	public SetupDAO getSetupDAO() {
		return setupDAO;
	}

	public void setSetupDAO(SetupDAO setupDAO) {
		this.setupDAO = setupDAO;
	}

	public AnnouncementDAO getAnnouncementDAO() {
		return announcementDAO;
	}

	public void setAnnouncementDAO(AnnouncementDAO announcementDAO) {
		this.announcementDAO = announcementDAO;
	}

	public MessageTopicDAO getMessageTopicDAO() {
		return messageTopicDAO;
	}

	public void setMessageTopicDAO(MessageTopicDAO messageTopicDAO) {
		this.messageTopicDAO = messageTopicDAO;
	}

	public MessageCommentDAO getMessageCommentDAO() {
		return messageCommentDAO;
	}

	public void setMessageCommentDAO(MessageCommentDAO messageCommentDAO) {
		this.messageCommentDAO = messageCommentDAO;
	}

	public NotificationDAO getNotificationDAO() {
		return notificationDAO;
	}

	public void setNotificationDAO(NotificationDAO notificationDAO) {
		this.notificationDAO = notificationDAO;
	}

	public ReviewRequestDAO getReviewRequestDAO() {
		return reviewRequestDAO;
	}

	public void setReviewRequestDAO(ReviewRequestDAO reviewRequestDAO) {
		this.reviewRequestDAO = reviewRequestDAO;
	}

	public TimeRangeReportDAO getTimeRangeReportDAO() {
		return this.timeRangeReportDAO;
	}

	public void setTimeRangeReportDAO(TimeRangeReportDAO timeRangeReportDAO) {
		this.timeRangeReportDAO = timeRangeReportDAO;
	}
	
	@RequestMapping(value = "/init", method=RequestMethod.GET)
	public String setup() {

		log.info(">>> Initialize Setup...");
		
		try {
			
			log.info(">>> Create Users...");
			
			UserData user = null;
			PasswordData pd = null;
			
			user = new UserData();
			user.setId(1L);
			user.setEmail("EMAIL@DOMAIN");
			user.setUsername(user.getEmail().toUpperCase());
			user.setName("Administrador do Sistema");
			user.setEnabled(true);
			pd = PasswordUtil.encrypt("PASSWORD_HERE");
			user.setPasswordHash(pd.getPasswordHash());
			user.setPasswordSalt(pd.getPasswordSalt());
			user = userDAO.save(user);
			
			AdministratorUserData administratorUser;
			administratorUser = new AdministratorUserData();
			administratorUser.setId(1L);
			administratorUser = administratorUserDAO.save(administratorUser);
			
			UserGroupData adminGroup = new UserGroupData();
			adminGroup.setEntityId(0L);
			adminGroup.setName("Atendimento");
			adminGroup.setReceiveMessage(true);
			adminGroup = userGroupDAO.save(adminGroup);
			
			UserGroupEntityUserData userGroupEntityUser = null;
			userGroupEntityUser = new UserGroupEntityUserData();
			userGroupEntityUser.setEntityProfile(UserProfileEnum.ADMINISTRATOR);
			userGroupEntityUser.setEntityUserId(1L);
			userGroupEntityUser.setUserGroupId(adminGroup.getId());
			userGroupEntityUser = userGroupEntityUserDAO.save(userGroupEntityUser);		
			
			log.info(">>> Setup Complete!!!");
			
		} catch (Exception e) {
			log.warning(">>> Error during setup!!! " + e.toString());
		}
				 
		return UrlMapping.OK_VIEW;
	 }
	
	@RequestMapping(value = "/removeNotUsedColumns", method=RequestMethod.GET)
	public String removeNotUsedColumns() {
		
		setupDAO.removeNotUsedColumns();
		
		return UrlMapping.OK_VIEW;		
	}
		
	@RequestMapping(value = "/triggerSetup/{setupMethod}", method=RequestMethod.GET)
	public String triggerSetup(@PathVariable String setupMethod) {
		
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/setup/"+setupMethod.replace('|', '/')));
		
		return UrlMapping.OK_VIEW;
	}
	
	@RequestMapping(value = "/reloadState", method=RequestMethod.POST)
	public String reloadState() {

		// first delete all existing State data
		log.fine("Delete all state data...");
		for (StateData state : stateDAO.listAll()) {
			stateDAO.delete(state.getId());
		}

		// load all state information from CSV file
		log.fine("Load state data from file...");
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			
			File stateFile = new File(STATE_FILE);
			
			br = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(stateFile), "UTF8"));

			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] stateArray = line.split(cvsSplitBy);

				System.out.println("State [id= " + stateArray[0] + " , code="
						+ stateArray[1] + " , name="
						+ stateArray[2] + "]");
				
				StateData state = new StateData();
				state.setId(Long.valueOf(stateArray[0].replace("uf", "")));
				state.setCode(stateArray[1]);
				state.setName(stateArray[2]);
				stateDAO.save(state);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return UrlMapping.OK_VIEW;
	}
	
	@RequestMapping(value = "/reloadCity", method=RequestMethod.POST)
	public String reloadCity() {

		// first delete all existing State data
		log.fine("Delete all city data...");
		for (CityData city : cityDAO.listAll()) {
			cityDAO.delete(city.getId());
		}

		// load all state information from CSV file
		log.fine("Load city data from file...");
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			File cityFile = new File(CITY_FILE);
			
			br = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(cityFile), "UTF8"));

			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] cityArray = line.split(cvsSplitBy);

				System.out.println("City [id= " + cityArray[2] + " , state_id="
						+ cityArray[0] + " , name="
						+ cityArray[3] + "]");
				
				CityData city = new CityData();
				city.setId(Long.valueOf(cityArray[2].replace("m", "")));
				city.setStateId(Long.valueOf(cityArray[0].replace("uf", "")));
				city.setName(cityArray[3]);
				cityDAO.save(city);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return UrlMapping.OK_VIEW;
	}
	
	@RequestMapping(value = "/checkInconsistencies/{object}/{mode}", method=RequestMethod.GET)
	public String checkInconsistencies(@PathVariable String object, @PathVariable String mode) {
		
		int count = 0;
		
		log.info(">>> Starting checkInconsistencies for " + object + " object(s) and mode " + mode);
		
		count = 0;
		if (object.equals("DeliveryRequest") || object.equals("ALL")) {
			List<DeliveryRequestData> deliveryRequests = deliveryRequestDAO.listAll();
			if (deliveryRequests != null && !deliveryRequests.isEmpty()) {
				log.info(">>> Checking " + deliveryRequests.size() + " DeliveryRequests");
				for (DeliveryRequestData deliveryRequest: deliveryRequests) {
					
					if (franchiseeDAO.findById(deliveryRequest.getFranchiseeId()) == null){
						log.info(">>> FranchiseeId " + deliveryRequest.getFranchiseeId() + " not found for DeliveryRequest Id " + deliveryRequest.getId());
						count++;
					}
					
					if (productDAO.findById(deliveryRequest.getProductId()) == null){
						log.info(">>> ProductId " + deliveryRequest.getProductId() + " not found for DeliveryRequest Id " + deliveryRequest.getId());
						count++;
					}
					
					if (productSizeDAO.findById(deliveryRequest.getProductSizeId()) == null){
						log.info(">>> ProductSizeId " + deliveryRequest.getProductSizeId() + " not found for DeliveryRequest Id " + deliveryRequest.getId());
						count++;
					}
					
					if (supplierDAO.findById(deliveryRequest.getSupplierId()) == null){
						log.info(">>> SupplierId " + deliveryRequest.getSupplierId() + " not found for DeliveryRequest Id " + deliveryRequest.getId());
						count++;
					}					
					
				}
				log.info(">>> Found " + count + " inconsistencies for DeliveryRequests");
			}
		}
		
		count = 0;
		if (object.equals("DeliveryRequestHistory") || object.equals("ALL")) {
			List<DeliveryRequestHistoryData> deliveryRequests = deliveryRequestHistoryDAO.listAll();
			if (deliveryRequests != null && !deliveryRequests.isEmpty()) {
				log.info(">>> Checking " + deliveryRequests.size() + " DeliveryRequestHistory");
				for (DeliveryRequestHistoryData deliveryRequest: deliveryRequests) {
					
					if (franchiseeDAO.findById(deliveryRequest.getFranchiseeId()) == null){
						log.info(">>> FranchiseeId " + deliveryRequest.getFranchiseeId() + " not found for DeliveryRequestHistory Id " + deliveryRequest.getId());
					}
					
					if (productDAO.findById(deliveryRequest.getProductId()) == null){
						log.info(">>> ProductId " + deliveryRequest.getProductId() + " not found for DeliveryRequestHistory Id " + deliveryRequest.getId());
					}
					
					if (productSizeDAO.findById(deliveryRequest.getProductSizeId()) == null){
						log.info(">>> ProductSizeId " + deliveryRequest.getProductSizeId() + " not found for DeliveryRequest Id " + deliveryRequest.getId());
					}
					
					if (supplierDAO.findById(deliveryRequest.getSupplierId()) == null){
						log.info(">>> SupplierId " + deliveryRequest.getSupplierId() + " not found for DeliveryRequestHistory Id " + deliveryRequest.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for DeliveryRequestHistory");
			}
		}
		
		count = 0;
		if (object.equals("DocumentsFile") || object.equals("ALL")) {
			List<DocumentsFileData> documentsFiles = documentsFileDAO.listAll();
			if (documentsFiles != null && !documentsFiles.isEmpty()) {
				log.info(">>> Checking " + documentsFiles.size() + " DocumentsFile");
				for (DocumentsFileData documentsFile: documentsFiles) {
					
					//TODO: add file content validation
					
					if (documentsFolderDAO.findById(documentsFile.getFolderId()) == null){
						log.info(">>> FolderId " + documentsFile.getFolderId() + " not found for DocumentFile Id " + documentsFile.getId());
					}
					
					if (franchisorDAO.findById(documentsFile.getFranchisorId()) == null){
						log.info(">>> FranchisorId " + documentsFile.getFranchisorId() + " not found for DocumentFile Id " + documentsFile.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for DocumentsFile");
			}
		}
		
		count = 0;
		if (object.equals("DocumentsFolder") || object.equals("ALL")) {
			List<DocumentsFolderData> documentsFolders = documentsFolderDAO.listAll();
			if (documentsFolders != null && !documentsFolders.isEmpty()) {
				log.info(">>> Checking " + documentsFolders.size() + " DocumentsFolder");
				for (DocumentsFolderData documentsFolder: documentsFolders) {
					
					if (franchisorDAO.findById(documentsFolder.getFranchisorId()) == null){
						log.info(">>> FranchisorId " + documentsFolder.getFranchisorId() + " not found for DocumentFolder Id " + documentsFolder.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for DocumentsFolder");
			}
		}
		
		count = 0;
		if (object.equals("Franchisee") || object.equals("ALL")) {
			List<FranchiseeData> franchisees = franchiseeDAO.listAll();
			if (franchisees != null && !franchisees.isEmpty()) {
				log.info(">>> Checking " + franchisees.size() + " Franchisee");
				for (FranchiseeData franchisee: franchisees) {
					
					if (franchisorDAO.findById(franchisee.getFranchisorId()) == null){
						log.info(">>> FranchisorId " + franchisee.getFranchisorId() + " not found for Franchisee Id " + franchisee.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for Franchisee");
			}
		}
		
		count = 0;
		if (object.equals("FranchiseeUser") || object.equals("ALL")) {
			List<FranchiseeUserData> franchiseeUsers = franchiseeUserDAO.listAll();
			if (franchiseeUsers != null && !franchiseeUsers.isEmpty()) {
				log.info(">>> Checking " + franchiseeUsers.size() + " FranchiseeUser");
				for (FranchiseeUserData franchiseeUser: franchiseeUsers) {
					
					if (franchiseeDAO.findById(franchiseeUser.getFranchiseeId()) == null){
						log.info(">>> FranchiseeId " + franchiseeUser.getFranchiseeId() + " not found for FranchiseeUser Id " + franchiseeUser.getId());
					}
					
					if (userDAO.findById(franchiseeUser.getUserId()) == null){
						log.info(">>> UserId " + franchiseeUser.getUserId() + " not found for FranchiseeUser Id " + franchiseeUser.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for FranchiseeUser");
			}
		}
		
		count = 0;
		if (object.equals("Franchisor") || object.equals("ALL")) {
			List<FranchisorData> franchisors = franchisorDAO.listAll();
			if (franchisors != null && !franchisors.isEmpty()) {
				log.info(">>> Checking " + franchisors.size() + " Franchisor");
				for (FranchisorData franchisor: franchisors) {
					
					//TODO: Include Franchisor check of imageKey
					
				}
				log.info(">>> Found " + count + " inconsistencies for Franchisor");
			}
		}		
		
		count = 0;
		if (object.equals("FranchisorUser") || object.equals("ALL")) {
			List<FranchisorUserData> franchisorUsers = franchisorUserDAO.listAll();
			if (franchisorUsers != null && !franchisorUsers.isEmpty()) {
				log.info(">>> Checking " + franchisorUsers.size() + " FranchisorUser");
				for (FranchisorUserData franchisorUser: franchisorUsers) {
					
					if (franchisorDAO.findById(franchisorUser.getFranchisorId()) == null){
						log.info(">>> FranchiorId " + franchisorUser.getFranchisorId() + " not found for FranchisorUser Id " + franchisorUser.getId());
					}
					
					if (userDAO.findById(franchisorUser.getUserId()) == null){
						log.info(">>> UserId " + franchisorUser.getUserId() + " not found for FranchisorUser Id " + franchisorUser.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for FranchisorUser");
			}
		}
		
		count = 0;
		if (object.equals("ManufactureRequest") || object.equals("ALL")) {
			List<ManufactureRequestData> manufactureRequests = manufactureRequestDAO.listAll();
			if (manufactureRequests != null && !manufactureRequests.isEmpty()) {
				log.info(">>> Checking " + manufactureRequests.size() + " ManufactureRequest");
				for (ManufactureRequestData manufactureRequest: manufactureRequests) {
					
					if (franchisorDAO.findById(manufactureRequest.getFranchisorId()) == null){
						log.info(">>> FranchiorId " + manufactureRequest.getFranchisorId() + " not found for ManufactureRequest Id " + manufactureRequest.getId());
					}
					
					if (productDAO.findById(manufactureRequest.getProductId()) == null){
						log.info(">>> ProductId " + manufactureRequest.getProductId() + " not found for ManufactureRequest Id " + manufactureRequest.getId());
					}
					
					if (productSizeDAO.findById(manufactureRequest.getProductSizeId()) == null){
						log.info(">>> ProductSizeId " + manufactureRequest.getProductSizeId() + " not found for ManufactureRequest Id " + manufactureRequest.getId());
					}
					
					if (supplierDAO.findById(manufactureRequest.getSupplierId()) == null){
						log.info(">>> SupplierId " + manufactureRequest.getSupplierId() + " not found for ManufactureRequest Id " + manufactureRequest.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for ManufactureRequest");
			}
		}
		
		count = 0;
		if (object.equals("ManufactureRequestHistory") || object.equals("ALL")) {
			List<ManufactureRequestHistoryData> manufactureRequestHistories = manufactureRequestHistoryDAO.listAll();
			if (manufactureRequestHistories != null && !manufactureRequestHistories.isEmpty()) {
				log.info(">>> Checking " + manufactureRequestHistories.size() + " ManufactureRequestHistory");
				for (ManufactureRequestHistoryData manufactureRequestHistory: manufactureRequestHistories) {
					
					if (franchisorDAO.findById(manufactureRequestHistory.getFranchisorId()) == null){
						log.info(">>> FranchiorId " + manufactureRequestHistory.getFranchisorId() + " not found for ManufactureRequestHistory Id " + manufactureRequestHistory.getId());
					}
					
					if (productDAO.findById(manufactureRequestHistory.getProductId()) == null){
						log.info(">>> ProductId " + manufactureRequestHistory.getProductId() + " not found for ManufactureRequestHistory Id " + manufactureRequestHistory.getId());
					}
					
					if (productSizeDAO.findById(manufactureRequestHistory.getProductSizeId()) == null){
						log.info(">>> ProductSizeId " + manufactureRequestHistory.getProductSizeId() + " not found for ManufactureRequestHistory Id " + manufactureRequestHistory.getId());
					}
					
					if (supplierDAO.findById(manufactureRequestHistory.getSupplierId()) == null){
						log.info(">>> SupplierId " + manufactureRequestHistory.getSupplierId() + " not found for ManufactureRequestHistory Id " + manufactureRequestHistory.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for ManufactureRequestHistory");
			}
		}
		
		count = 0;
		if (object.equals("Product") || object.equals("ALL")) {
			List<ProductData> products = productDAO.listAll();
			if (products != null && !products.isEmpty()) {
				log.info(">>> Checking " + products.size() + " Product");
				for (ProductData product: products) {
					
					//TODO: add validation for imageKey
					
					if (franchisorDAO.findById(product.getFranchisorId()) == null){
						log.info(">>> FranchisorId " + product.getFranchisorId() + " not found for Product Id " + product.getId());
					}
					
					if (productCategoryDAO.findById(product.getProductCategoryId()) == null){
						log.info(">>> ProductCategoryId " + product.getProductCategoryId() + " not found for Product Id " + product.getId());
					}
					
					if (supplierDAO.findById(product.getSupplierId()) == null){
						log.info(">>> SupplierId " + product.getSupplierId() + " not found for Product Id " + product.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for Product");
			}
		}
		
		count = 0;
		if (object.equals("ProductCategory") || object.equals("ALL")) {
			List<ProductCategoryData> productCategories = productCategoryDAO.listAll();
			if (productCategories != null && !productCategories.isEmpty()) {
				log.info(">>> Checking " + productCategories.size() + " ProductCategory");
				for (ProductCategoryData productCategory: productCategories) {
					
					if (franchisorDAO.findById(productCategory.getFranchisorId()) == null){
						log.info(">>> FranchisorId " + productCategory.getFranchisorId() + " not found for ProductCategory Id " + productCategory.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for ProductCategory");
			}
		}
		
		count = 0;
		if (object.equals("ProductSize") || object.equals("ALL")) {
			List<ProductSizeData> productSizes = productSizeDAO.listAll();
			if (productSizes != null && !productSizes.isEmpty()) {
				log.info(">>> Checking " + productSizes.size() + " ProductSize");
				for (ProductSizeData productSize: productSizes) {
					
					if (productDAO.findById(productSize.getProductId()) == null){
						log.info(">>> ProductId " + productSize.getProductId() + " not found for ProductSize Id " + productSize.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for ProductSize");
			}
		}
		
		count = 0;
		if (object.equals("Supplier") || object.equals("ALL")) {
			List<SupplierData> suppliers = supplierDAO.listAll();
			if (suppliers != null && !suppliers.isEmpty()) {
				log.info(">>> Checking " + suppliers.size() + " Supplier");
				for (SupplierData supplier: suppliers) {
					
					//TODO: add validation for imageKey
					
					if (supplierCategoryDAO.findById(supplier.getCategoryId()) == null){
						log.info(">>> CategoryId " + supplier.getCategoryId() + " not found for Supplier Id " + supplier.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for Supplier");
			}
		}
		
		count = 0;
		if (object.equals("SupplierFranchisor") || object.equals("ALL")) {
			List<SupplierFranchisorData> supplierFranchisors = supplierFranchisorDAO.listAll();
			if (supplierFranchisors != null && !supplierFranchisors.isEmpty()) {
				log.info(">>> Checking " + supplierFranchisors.size() + " SupplierFranchisor");
				for (SupplierFranchisorData supplierFranchisor: supplierFranchisors) {
					
					if (franchisorDAO.findById(supplierFranchisor.getFranchisorId()) == null){
						log.info(">>> FranchisorId " + supplierFranchisor.getFranchisorId() + " not found for Supplier Franchisor Id " + supplierFranchisor.getId());
					}
					
					if (supplierDAO.findById(supplierFranchisor.getSupplierId()) == null){
						log.info(">>> SupplierId " + supplierFranchisor.getSupplierId() + " not found for Supplier Franchisor Id " + supplierFranchisor.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for SupplierFranchisor");
			}
		}
		
		count = 0;
		if (object.equals("SupplierUser") || object.equals("ALL")) {
			List<SupplierUserData> supplierUsers = supplierUserDAO.listAll();
			if (supplierUsers != null && !supplierUsers.isEmpty()) {
				log.info(">>> Checking " + supplierUsers.size() + " SupplierUser");
				for (SupplierUserData supplierUser: supplierUsers) {
					
					if (supplierDAO.findById(supplierUser.getSupplierId()) == null){
						log.info(">>> SupplierId " + supplierUser.getSupplierId() + " not found for Supplier User Id " + supplierUser.getId());
					}
					
					if (userDAO.findById(supplierUser.getUserId()) == null){
						log.info(">>> UserId " + supplierUser.getUserId() + " not found for Supplier User Id " + supplierUser.getId());
					}
					
				}
				log.info(">>> Found " + count + " inconsistencies for SupplierUser");
			}
		}
		
		
		return UrlMapping.OK_VIEW;		
	}
	
	@RequestMapping(value = "/addChallenge/{response}", method=RequestMethod.GET)
	public String addChallenge(@PathVariable String response) {
		
		String [] splitTemp = response.split("@");
		
		LetsEncryptChallengeData data = new LetsEncryptChallengeData();
		data.setChallenge(splitTemp[0]);
		data.setResponse(splitTemp[0]+"."+splitTemp[1]);		
		letsEncryptChallengeDAO.save(data);
		
		return UrlMapping.OK_VIEW;		
	}
	
	@RequestMapping(value = "/cleanUpExpiredSessions", method=RequestMethod.GET)
	public String cleanUpExpiredSessions() {
		
		setupDAO.cleanUpExpiredSessions();
		
		return UrlMapping.OK_VIEW;		
	}
	
	@RequestMapping(value = "/updateUserEnabled/{entityType}/{entityId}/{status}", method=RequestMethod.GET)
	public String updateUserEnabled(@PathVariable UserProfileEnum entityType, @PathVariable Long entityId, @PathVariable Boolean status) {
		
		System.out.println("updateUserEnabled [entityType=" + entityType + " entityId=" + entityId + " status=" + status + "]");
		
		if (entityType == UserProfileEnum.FRANCHISOR) {
			
			List<FranchisorUserData> franchisorUsers = franchisorUserDAO.findByFranchisorId(entityId);
			if (franchisorUsers != null && !franchisorUsers.isEmpty()) {
				for (FranchisorUserData franchisorUser: franchisorUsers) {
					UserData user = userDAO.findById(franchisorUser.getUserId());
					if (user != null) {
						user.setEnabled(status);
						userDAO.save(user);
					} else {
						log.info("User not found: " + franchisorUser.getUserId());
					}					
				}
			}
			
			List<FranchiseeData> franchisees = franchiseeDAO.findByFranchisorId(entityId);
			if (franchisees != null && !franchisees.isEmpty()) {
				for (FranchiseeData franchisee: franchisees) {
					List<FranchiseeUserData> franchiseeUsers = franchiseeUserDAO.findByFranchiseeId(franchisee.getId());
					if (franchiseeUsers != null && !franchiseeUsers.isEmpty()) {
						for (FranchiseeUserData franchiseeUser: franchiseeUsers) {
							UserData user = userDAO.findById(franchiseeUser.getUserId());
							if (user != null) {
								user.setEnabled(status);
								userDAO.save(user);
							} else {
								log.info("User not found: " + franchiseeUser.getUserId());
							}	
						}
					}
				}
			}
			
		} else if (entityType == UserProfileEnum.SUPPLIER) {
			
			List<SupplierUserData> supplierUsers = supplierUserDAO.findBySupplierId(entityId);
			if (supplierUsers != null && !supplierUsers.isEmpty()) {
				for (SupplierUserData supplierUser: supplierUsers) {
					UserData user = userDAO.findById(supplierUser.getUserId());
					if (user != null) {
						user.setEnabled(status);
						userDAO.save(user);
					} else {
						log.info("User not found: " + supplierUser.getUserId());
					}	
				}
			}
			
		} else if (entityType == UserProfileEnum.ADMINISTRATOR) {
			
			List<AdministratorUserData> administratorUsers = administratorUserDAO.listAll();
			if (administratorUsers != null && !administratorUsers.isEmpty()) {
				for (AdministratorUserData administratorUser: administratorUsers) {
					UserData user = userDAO.findById(administratorUser.getId());
					if (user != null) {
						user.setEnabled(status);
						userDAO.save(user);
					} else {
						log.info("User not found: " + administratorUser.getId());
					}
				}
			}
			
		}

		
		return UrlMapping.OK_VIEW;		
	}
		
	@RequestMapping(value = "/test", method=RequestMethod.GET)
	public String test() {
		
		log.info(">>> This application is: " + SystemProperty.applicationId.get());		
		
		return UrlMapping.OK_VIEW;	
	}
	
	@RequestMapping(value = "/initializeMessageTopicIndex/start/{strStartDate}/end/{strEndDate}", method=RequestMethod.GET)
	public String testSearchApi(@PathVariable String strStartDate, @PathVariable String strEndDate) {
		/*
		Queue queue = QueueFactory.getQueue("indexing");
		
		log.info(">>> Querying message topics from " + strStartDate + " to " + strEndDate);
		
		Date startDate = DateUtil.getDate(strStartDate, DateUtil.DASH_FULL_PATTERN);
		Date endDate = DateUtil.getDate(strEndDate, DateUtil.DASH_FULL_PATTERN);
		
		
		List<MessageTopicData> messageTopics = messageTopicDAO.findByUpdateDateRange(startDate, endDate);
		if (messageTopics != null && !messageTopics.isEmpty()) {
			messageTopics
				.stream()
				.forEach(x -> {
					log.info(">>> Adding message topic to indexing queue: " + x.getId());
					queue.add(TaskOptions.Builder.withUrl("/job/indexing/messageTopic/"+x.getId()));
				});
		} else {
			log.info(">>> EMPTY");
		}
		*/
		
		return UrlMapping.OK_VIEW;	
	}
	
	@RequestMapping(value = "/addMessagesToInbox/{startDate}/{endDate}/{pageSize}/{cursor}", method=RequestMethod.POST)
	public String continueAddMessagesToInbox(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String pageSize, @PathVariable String cursor) {

		log.info("Running continueAddMessagesToInbox...");
		this.addMessagesToInbox(startDate, endDate, pageSize, cursor);
		return UrlMapping.OK_VIEW;
	}

	//http://localhost:8080/setup/addMessagesToInbox/2020-01-01T00:00:00-0300/2020-03-22T00:00:00-0300/50/NONE
	@RequestMapping(value = "/addMessagesToInbox/{startDate}/{endDate}/{pageSize}/{cursor}", method=RequestMethod.GET)
	public String addMessagesToInbox(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String pageSize, @PathVariable String cursor) {

		log.info("Running addMessagesToInbox...");
		
		Date parsedStartDate;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			parsedStartDate = formatter.parse(startDate);
		} catch (Exception e) {
			parsedStartDate = new Date();
			log.severe("Error parsing startDate: " + startDate);
		}
		System.out.println("StartDate = " + parsedStartDate);

		Date parsedEndDate;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			parsedEndDate = formatter.parse(endDate);
		} catch (Exception e) {
			parsedEndDate = new Date();
			log.severe("Error parsing endDate: " + endDate);
		}
		System.out.println("EndDate = " + parsedEndDate);

		if (cursor.equals("NONE")) cursor = null;

		NotificationPagedResponse pagedNotifications = notificationDAO.findByIsReadAndTypeAndDateRange(false, NotificationTypeEnum.MESSAGE_COMMENT, parsedStartDate, parsedEndDate, Long.parseLong(pageSize), cursor);
		List<NotificationData> notifications = pagedNotifications.getNotifications();
		System.out.println("Found notifications: " + notifications.size());

		notifications.stream().forEach(notification -> {
			try {
				System.out.println("NotificationId: " + notification.getId() + ", Date: " + notification.getDate() + ", EntityProfile: " + notification.getEntityProfile());

				Long entityUserId = null;
				/*
				if (notification.getEntityProfile().equals(UserProfileEnum.FRANCHISOR)) {
					FranchisorUserData franchisorUser = franchisorUserDAO.findByFranchisorIdAndUserId(notification.getEntityId(), notification.getUserId());
					entityUserId = franchisorUser.getId();
				} else if (notification.getEntityProfile().equals(UserProfileEnum.FRANCHISEE)) {
					FranchiseeUserData franchiseeUser = franchiseeUserDAO.findByFranchiseeIdAndUserId(notification.getEntityId(), notification.getUserId());
					entityUserId = franchiseeUser.getId();
				} else if (notification.getEntityProfile().equals(UserProfileEnum.SUPPLIER)) {
					SupplierUserData supplierUser = supplierUserDAO.findBySupplierIdAndUserId(notification.getEntityId(), notification.getUserId());
					entityUserId = supplierUser.getId();
				} else if (notification.getEntityProfile().equals(UserProfileEnum.ADMINISTRATOR)) {
					entityUserId = notification.getUserId();
				}
				*/
				System.out.println(">>> Adding INBOX label for entityUserId: " + entityUserId + " to messageTopic " + notification.getReferenceId().toString());
				messageTopicDAO.addSearchIndexDocumentField(notification.getReferenceId().toString(), MessageTopicDAO.LABEL_FIELD, messageTopicDAO.buildLabelKey(entityUserId, MessageTopicDAO.LABEL_INBOX));
			} catch (Exception e) {
				System.out.println(">>> ERROR: unable to add notification: " + notification.getId() + " - " + e.toString());
			}
			
		});

		System.out.println("Cursor: " + pagedNotifications.getCursorString());
		if (pagedNotifications.getCursorString() != null && notifications.size() == Long.parseLong(pageSize)) {
			Queue queue = QueueFactory.getDefaultQueue();
			queue.add(TaskOptions.Builder.withUrl("/setup/addMessagesToInbox/"+startDate+"/"+endDate+"/"+pageSize+"/"+pagedNotifications.getCursorString()).countdownMillis(30000));
			System.out.println(">>> NEW TASK CREATED");
		} else {
			System.out.println(">>> NO MORE");
		}

		return UrlMapping.OK_VIEW;
	}

	@RequestMapping(value = "/initializeFranchisorAnnouncementIndex/{franchisorId}/{page}", method=RequestMethod.POST)
	public String initializeFranchisorAnnouncementIndex(@PathVariable String franchisorId, @PathVariable String page) {

		Long intPage = Long.valueOf(page);

		System.out.println("Initialize Announcement Index for franchisor " + franchisorId + " and page " + page);

		AnnouncementPagedResponse response = announcementDAO.findByFranchisorId(Long.valueOf(franchisorId), 50L, intPage);

		if (response.getAnnouncements() != null) {
			response.getAnnouncements().stream().forEach(announcement -> {
				if (announcement.getStatus().equals(AnnouncementStatusEnum.PUBLISHED)) {
					announcementDAO.addSearchIndexDocument(announcement);
				}				
			});

			if (response.getHasMore()) {
				intPage = intPage + 1;
				Queue queue = QueueFactory.getDefaultQueue();
				queue.add(TaskOptions.Builder.withUrl("/setup/initializeFranchisorAnnouncementIndex/"+franchisorId+"/"+intPage));
				System.out.println(">>> NEW TASK CREATED for " + franchisorId);
			} else {
				System.out.println(">>> NO MORE for " + franchisorId);
			}
		}		

		return UrlMapping.OK_VIEW;
	}
	
	@RequestMapping(value = "/initializeAnnouncementIndex", method=RequestMethod.GET)
	public String initializeAnnouncementIndex() {

		long countDown = 30000;

		for (FranchisorData franchisor: franchisorDAO.listAll()) {
			Queue queue = QueueFactory.getDefaultQueue();
			queue.add(TaskOptions.Builder.withUrl("/setup/initializeFranchisorAnnouncementIndex/"+franchisor.getId()+"/1").countdownMillis(countDown));
			countDown = countDown + 30000;
		};

		return UrlMapping.OK_VIEW;
	}

	@RequestMapping(value = "/initializeFranchisorConfiguration", method=RequestMethod.GET)
	public String initializeFranchisorConfiguration() {

		for (FranchisorData franchisor: franchisorDAO.listAll()) {
			franchisor.setFlagAnnouncement(true);
			franchisor.setFlagCalendar(true);
			franchisor.setFlagDocuments(true);
			franchisor.setFlagTraining(true);
			franchisorDAO.save(franchisor);
		};

		return UrlMapping.OK_VIEW;
	}
}
