package br.com.usasistemas.usaplatform.bizo.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import org.json.JSONException;

import br.com.usasistemas.usaplatform.api.request.data.ContactData;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.bizo.DocumentManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.MailManagementBO;
import br.com.usasistemas.usaplatform.bizo.NotificationManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.model.data.CalendarEventData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeUserData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.FranchisorUserData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.data.NotificationData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.data.ProductSizePriceHistoryData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.SupplierUserData;
import br.com.usasistemas.usaplatform.model.data.UserData;
import br.com.usasistemas.usaplatform.model.enums.DomainKeysEnum;
import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.OperationTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

public class MailManagementBOImpl implements MailManagementBO{
	
	private static final Logger log = Logger.getLogger(MailManagementBOImpl.class.getName());
	
	private SupplierManagementBO supplierManagement;
	private FranchisorManagementBO franchisorManagement;
	private FranchiseeManagementBO franchiseeManagement;
	private DeliveryManagementBO deliveryManagement;
	private ProductManagementBO productManagement;
	private DocumentManagementBO documentManagement;
	private NotificationManagementBO notificationManagement;
	private UserManagementBO userManagement;
	private ConfigurationManagementBO configurationManagement;
	
	public MailManagementBOImpl() {}
	
	public SupplierManagementBO getSupplierManagement() {
		return supplierManagement;
	}

	public void setSupplierManagement(SupplierManagementBO supplierManagement) {
		this.supplierManagement = supplierManagement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManagement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}

	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public DeliveryManagementBO getDeliveryManagement() {
		return deliveryManagement;
	}

	public void setDeliveryManagement(DeliveryManagementBO deliveryManagement) {
		this.deliveryManagement = deliveryManagement;
	}

	public ProductManagementBO getProductManagement() {
		return productManagement;
	}

	public void setProductManagement(ProductManagementBO productManagement) {
		this.productManagement = productManagement;
	}
	
	public NotificationManagementBO getNotificationManagement() {
		return notificationManagement;
	}

	public DocumentManagementBO getDocumentManagement() {
		return documentManagement;
	}

	public void setDocumentManagement(DocumentManagementBO documentManagement) {
		this.documentManagement = documentManagement;
	}

	public void setNotificationManagement(NotificationManagementBO notificationManagement) {
		this.notificationManagement = notificationManagement;
	}

	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	private void sendMail(List<PublicUserData> toList, String subject, String textContent, String htmlContent, String domainKey) throws JSONException {
		
		//get app configuration to retrieve mail info
		DomainConfigurationData configuration = this.getConfigurationManagement().getDomainConfigurationByKey(domainKey);
	    
	    Mail mail = new Mail();
	    mail.setFrom(new Email(configuration.getMailSender(), configuration.getTitle()));
	    mail.setReplyTo(new Email(configuration.getMailSender(), configuration.getTitle()));
	    mail.setSubject(subject);
	    
	    Content mailTextContent = new Content("text/plain", textContent);
	    mail.addContent(mailTextContent);
	    
	    Content mailHtmlContent = new Content("text/html", htmlContent);
	    mail.addContent(mailHtmlContent);
	    
	    for (PublicUserData to: toList) {
	    	Personalization recipients = new Personalization();
	    	recipients.addTo(new Email(to.getEmail(), to.getName()));
			mail.addPersonalization(recipients);
		}	    	    	    
		
		SendGrid sg = new SendGrid(configuration.getMailApiKey());
		Request request = new Request();
		
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			
			log.info("E-mail sent sucessfully: " + response.getStatusCode());
	    } catch (IOException ex) {
	    	log.severe("Error sending e-mail: " + ex.getLocalizedMessage());
	    }
	
	}

	@Override
	public void sendNewManufactureRequest(ManufactureRequestData manufactureRequest) {
		
		List<PublicUserData> receivers = new ArrayList<PublicUserData>();
		
		log.info("Preparing manufacture request e-mail.");
		
		//send e-mail to supplier users
		List<SupplierUserData> supplierUsers = this.supplierManagement.getSupplierUsers(manufactureRequest.getSupplierId());
		for (SupplierUserData supplierUser: supplierUsers){
			receivers.add(supplierUser.getUser());
		}		
				
		//send mail if there are receivers
		if (receivers != null && !receivers.isEmpty()) {
			
			try {

				/*
				 * Prepare e-mail content
				 */
			    FranchisorData franchisor = this.franchisorManagement.getFranchisor(manufactureRequest.getFranchisorId());			    
				
			    String subject = "Novo Pedido de Produção - " + franchisor.getName();
		        String textContent = "Um novo pedido de produção foi criado para o franqueador " + franchisor.getName() + ". Acesse o sistema para ver mais detalhes do pedido.";
		        String htmlContent = "Um novo pedido de produção foi criado para o franqueador " + franchisor.getName() + ".<br />Acesse o sistema para ver mais detalhes do pedido.";

		        SupplierData supplier = this.supplierManagement.getSupplier(manufactureRequest.getSupplierId());
				this.sendMail(receivers, subject, textContent, htmlContent, supplier.getPreferedDomainKey());
			    
			    log.info("Manufacture request e-mail sent");

			} catch (Exception e) {
				log.info("Error sending New Manufacture Request e-mail");
				log.info(e.toString());
			}
			
		} else {
			log.info("No receivers to send e-mail");
		}
		
	}

	@Override
	public void sendNewDeliveryRequest(List<DeliveryRequestData> deliveryRequests) {
		
		Map<Long, ProductData> products = new HashMap<Long, ProductData>();
		Map<Long, FranchiseeData> franchisees = new HashMap<Long, FranchiseeData>();
		Map<Long, FranchisorData> franchisors = new HashMap<Long, FranchisorData>();
		
		StringBuffer sbTextVersion = new StringBuffer();
		StringBuffer sbHtmlVersion = new StringBuffer();
		
		Collections.sort(deliveryRequests, new Comparator<DeliveryRequestData>() {
	        @Override
	        public int compare(DeliveryRequestData request1, DeliveryRequestData request2)
	        {
	            return  request1.getDate().compareTo(request2.getDate());
	        }
	    });
		
		log.info("Getting Delivery Request data to create the e-mail.");
		for (DeliveryRequestData deliveryRequest: deliveryRequests){
			
			//get product data
			ProductData product = products.get(deliveryRequest.getProductId());
			if (product == null){
				product = productManagement.getProduct(deliveryRequest.getProductId());
				products.put(deliveryRequest.getProductId(), product);
			}
			
			//get product size data
			ProductSizeData productSize = null;
			for (ProductSizeData size: product.getSizes()){
				if (size.getId().equals(deliveryRequest.getProductSizeId())){
					productSize = size;
				}
			}
			
			//get franchisee and franchisor Data
			FranchiseeData franchisee = franchisees.get(deliveryRequest.getFranchiseeId());
			if (franchisee == null){
				franchisee = franchiseeManagement.getFranchisee(deliveryRequest.getFranchiseeId());
				franchisees.put(deliveryRequest.getFranchiseeId(), franchisee);
			}
			
			FranchisorData franchisor = franchisors.get(franchisee.getFranchisorId());
			if (franchisor == null){
				franchisor = franchisorManagement.getFranchisor(franchisee.getFranchisorId());
				franchisors.put(franchisee.getFranchisorId(), franchisor);
			}
			
			//create Text version for the item
			sbTextVersion.append("   * " + DateUtil.getDate(deliveryRequest.getDate()));
			sbTextVersion.append(" - " + franchisor.getName());
			sbTextVersion.append(" - " + franchisee.getName());
			sbTextVersion.append(" - " + product.getName());
			if (product.getSizes().size() > 1) {
				sbTextVersion.append(" - " + product.getSizeName() + ": " + productSize.getName());
			}
			sbTextVersion.append(" - Quantidade: " + deliveryRequest.getQuantity() + " " + product.getUnit());
			if (product.getHasDeliveryUnit()) {
				sbTextVersion.append(" = " + deliveryRequest.getQuantity()/productSize.getDeliveryQty() + " " + product.getDeliveryUnit());
			}				
			
			//create HTML version for the item
			sbHtmlVersion.append("<li>" + DateUtil.getDate(deliveryRequest.getDate()));
			sbHtmlVersion.append(" - " + franchisor.getName());
			sbHtmlVersion.append(" - " + franchisee.getName());
			sbHtmlVersion.append(" - " + product.getName());
			if (product.getSizes().size() > 1) {
				sbHtmlVersion.append(" - " + product.getSizeName() + ": " + productSize.getName());
			}
			sbHtmlVersion.append(" - Quantidade: " + deliveryRequest.getQuantity() + " " + product.getUnit());
			if (product.getHasDeliveryUnit()) {
				sbHtmlVersion.append(" = " + deliveryRequest.getQuantity()/productSize.getDeliveryQty() + " " + product.getDeliveryUnit());
			}				
			sbHtmlVersion.append("</li>");
			
		}
		
		log.info("Preparing delivery request e-mail.");
		
		List<PublicUserData> toList = new ArrayList<PublicUserData>();
		
		//send e-mail to supplier users
		//ATTENTION: get supplier ID from first delivery request, 
		// as it is expected that only delivery request for ONE SINGLE supplier is received
		List<SupplierUserData> supplierUsers = this.supplierManagement.getSupplierUsers(deliveryRequests.get(0).getSupplierId());
		for (SupplierUserData supplierUser: supplierUsers){
			toList.add(supplierUser.getUser());
		}
				
		//send mail if there are receivers
		if (toList != null && !toList.isEmpty()) {
			
			try {
				SupplierData supplier = this.supplierManagement.getSupplier(deliveryRequests.get(0).getSupplierId());
				DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByKey(supplier.getPreferedDomainKey());

				/*
				 * Prepare e-mail content
				 */
			    String subject = "Novos Pedidos de Entrega Recebidos";
			    String textContent = "Veja abaixo os novos pedidos recebidos:\n" +
       				 				 sbTextVersion.toString() +
       				 				 "\nAcesse o sistema para ver mais detalhes: " + domainConfiguration.getMainURL();
			    String htmlContent = "Veja abaixo os novos pedidos recebidos." +
    								 "<ul>" + sbHtmlVersion.toString() + "</ul>" +
    								 "Acesse o sistema para ver mais detalhes: " +
    								 "<a href='"+domainConfiguration.getMainURL()+"'>"+domainConfiguration.getMainURL()+"</a>";
			    					    
			    this.sendMail(toList, subject, textContent, htmlContent, supplier.getPreferedDomainKey());
			    
			    log.info("Delivery request e-mail sent");

			} catch (Exception e) {
				log.info("Error sending New Delivery Request e-mail");
				log.info(e.toString());
			}
			
		} else {
			log.info("No receivers to send e-mail");
		}
	}
	
	@Override
	public void sendCancelledDeliveryRequest(List<DeliveryRequestData> deliveryRequests) {
		
		Map<Long, ProductData> products = new HashMap<Long, ProductData>();
		Map<Long, FranchiseeData> franchisees = new HashMap<Long, FranchiseeData>();
		Map<Long, FranchisorData> franchisors = new HashMap<Long, FranchisorData>();
		
		StringBuffer sbTextVersion = new StringBuffer();
		StringBuffer sbHtmlVersion = new StringBuffer();
		
		Collections.sort(deliveryRequests, new Comparator<DeliveryRequestData>() {
	        @Override
	        public int compare(DeliveryRequestData request1, DeliveryRequestData request2)
	        {
	            return  request1.getDate().compareTo(request2.getDate());
	        }
	    });
		
		log.info("Getting Delivery Request data to create the e-mail.");
		for (DeliveryRequestData deliveryRequest: deliveryRequests){
			
			//get product data
			ProductData product = products.get(deliveryRequest.getProductId());
			if (product == null){
				product = productManagement.getProduct(deliveryRequest.getProductId());
				products.put(deliveryRequest.getProductId(), product);
			}
			
			//get product size data
			ProductSizeData productSize = null;
			for (ProductSizeData size: product.getSizes()){
				if (size.getId().equals(deliveryRequest.getProductSizeId())){
					productSize = size;
				}
			}
			
			//get franchisee and franchisor Data
			FranchiseeData franchisee = franchisees.get(deliveryRequest.getFranchiseeId());
			if (franchisee == null){
				franchisee = franchiseeManagement.getFranchisee(deliveryRequest.getFranchiseeId());
				franchisees.put(deliveryRequest.getFranchiseeId(), franchisee);
			}
			
			FranchisorData franchisor = franchisors.get(franchisee.getFranchisorId());
			if (franchisor == null){
				franchisor = franchisorManagement.getFranchisor(franchisee.getFranchisorId());
				franchisors.put(franchisee.getFranchisorId(), franchisor);
			}
			
			//create Text version for the item
			sbTextVersion.append("   * " + DateUtil.getDate(deliveryRequest.getDate()));
			sbTextVersion.append(" - " + franchisor.getName());
			sbTextVersion.append(" - " + franchisee.getName());
			sbTextVersion.append(" - " + product.getName());
			if (product.getSizes().size() > 1) {
				sbTextVersion.append(" - " + product.getSizeName() + ": " + productSize.getName());
			}
			sbTextVersion.append(" - Quantidade: " + deliveryRequest.getQuantity() + " " + product.getUnit());
			if (product.getHasDeliveryUnit()) {
				sbTextVersion.append(" = " + deliveryRequest.getQuantity()/productSize.getDeliveryQty() + " " + product.getDeliveryUnit());
			}				
			
			//create HTML version for the item
			sbHtmlVersion.append("<li>" + DateUtil.getDate(deliveryRequest.getDate()));
			sbHtmlVersion.append(" - " + franchisor.getName());
			sbHtmlVersion.append(" - " + franchisee.getName());
			sbHtmlVersion.append(" - " + product.getName());
			if (product.getSizes().size() > 1) {
				sbHtmlVersion.append(" - " + product.getSizeName() + ": " + productSize.getName());
			}
			sbHtmlVersion.append(" - Quantidade: " + deliveryRequest.getQuantity() + " " + product.getUnit());
			if (product.getHasDeliveryUnit()) {
				sbHtmlVersion.append(" = " + deliveryRequest.getQuantity()/productSize.getDeliveryQty() + " " + product.getDeliveryUnit());
			}				
			sbHtmlVersion.append("</li>");
			
		}
		
		log.info("Preparing cancelled delivery request e-mail.");
		
		List<PublicUserData> toList = new ArrayList<PublicUserData>();
		
		//send e-mail to supplier users
		//ATTENTION: get supplier ID from first delivery request, 
		// as it is expected that only delivery request for ONE SINGLE supplier is received
		List<SupplierUserData> supplierUsers = this.supplierManagement.getSupplierUsers(deliveryRequests.get(0).getSupplierId());
		for (SupplierUserData supplierUser: supplierUsers){
			toList.add(supplierUser.getUser());
		}
				
		//send mail if there are receivers
		if (toList != null && !toList.isEmpty()) {
			
			try {
			    SupplierData supplier = this.supplierManagement.getSupplier(deliveryRequests.get(0).getSupplierId());
			    DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByKey(supplier.getPreferedDomainKey());
			    
				/*
				 * Prepare e-mail content
				 */
			    String subject = "Pedidos de Entrega Cancelados pelo Solicitante";
			    String textContent = "Veja abaixo os pedidos cancelados pelo solicitante:\n" +
       				 				 sbTextVersion.toString() +
       				 				 "\nAcesse o sistema para ver mais detalhes: " + domainConfiguration.getMainURL();
			    String htmlContent = "Veja abaixo os pedidos cancelados pelo solicitante." +
    								 "<ul>" + sbHtmlVersion.toString() + "</ul>" +
    								 "Acesse o sistema para ver mais detalhes: " +
    								 "<a href='"+domainConfiguration.getMainURL()+"'>"+domainConfiguration.getMainURL()+"</a>";
			    		
			    this.sendMail(toList, subject, textContent, htmlContent, supplier.getPreferedDomainKey());
			    
			    log.info("Delivery request cancellation e-mail sent");

			} catch (Exception e) {
				log.info("Error sending Cancelled Delivery Request e-mail");
				log.info(e.toString());
			}
			
		} else {
			log.info("No receivers to send e-mail");
		}
	}

	@Override
	public void sendStockBelowMinimun(ProductData product) {

		List<PublicUserData> toList = new ArrayList<PublicUserData>();
		
		log.info("Preparing stock below minimal e-mail.");
		
		//send e-mail to franchisor users
		List<FranchisorUserData> franchisorUsers = this.franchisorManagement.getFranchisorUsers(product.getFranchisorId());
		for (FranchisorUserData franchisorUser: franchisorUsers){
			toList.add(franchisorUser.getUser());
		}		
				
		//send mail if there are receivers
		if (toList != null && !toList.isEmpty()) {
			
			try {

				/*
				 * Prepare e-mail content
				 */
		        String subject = "Estoque baixo para o produto " + product.getName();
			    String textContent = "O estoque do produto  " + product.getName() + 
       		         				 " esta abaixo do estoque mínimo indicado por você (" + 
       		         				 product.getMinStock() + " " + product.getUnit() + 
       		         				 "). Entre em contato para solicitar uma nova produção.";
			    String htmlContent = "O estoque do produto  " + product.getName() + 
	         						 " esta abaixo do estoque mínimo indicado por você (" + 
	         						 product.getMinStock() + " " + product.getUnit() + 
	         						 ").<br />Entre em contato para solicitar uma nova produção.";
			    
			    FranchisorData franchisor = this.franchisorManagement.getFranchisor(product.getFranchisorId());
			    this.sendMail(toList, subject, textContent, htmlContent, franchisor.getPreferedDomainKey());
			    
			    log.info("Stock below minimal e-mail sent");

			} catch (Exception e) {
				log.info("Error sending stock below minimal e-mail");
				log.info(e.toString());
			}
			
		} else {
			log.info("No receivers to send e-mail");
		}		
	}
	
	@Override
	public void sendResetPassword(UserData user, String uid, DomainConfigurationData domainConfiguration) {
		
		log.info("Preparing reset password e-mail.");		
		
		try {	
		    
		    PublicUserData userTo = new PublicUserData();
		    userTo.setName(user.getName());
		    userTo.setEmail(user.getEmail());
			
			List<PublicUserData> toList = new ArrayList<PublicUserData>();
			toList.add(userTo);

			/*
			 * Prepare e-mail content
			 */
	        String subject = "Solicitação de troca de senha";
	        String textContent = "Você esta recebendo este e-mail pois a troca de senha foi solicitada através do site. " +
                    			 "Para alterar sua senha, acesse: https://"+domainConfiguration.getMainURL()+"/login#/changePassword/" + uid+". " +
                    			 "Caso não tenha solicitado a troca de senha, por favor, ignore este e-mail e siga acessando o sistema normalmente, pois a senha não será alterada.";
	        String htmlContent = "Você esta recebendo este e-mail pois a troca de senha foi solicitada através do site.<br />" +
                    			 "Para alterar sua senha <a href=\"https://"+domainConfiguration.getMainURL()+"/login#/changePassword/"+uid+"\">clique aqui</a>.<br />" +
                    			 "Caso não tenha solicitado a troca de senha, por favor, ignore este e-mail e siga acessando o sistema normalmente, pois a senha não será alterada.";
		    
		    this.sendMail(toList, subject, textContent, htmlContent, domainConfiguration.getKey());
		    
		    log.info("Reset password e-mail sent");

		} catch (Exception e) {
			log.info("Error sending reset password e-mail");
			log.info(e.toString());
		}		
	}
	
	@Override
	public void checkPendingDeliveryRequestNotifications(){
		
		Map<Long, List<DeliveryRequestData>> supplierPendingNotificationsMap = new HashMap<Long, List<DeliveryRequestData>>();
		
		//get all delivery requests pending notification
		List<DeliveryRequestData> requestsPendingNotifications = deliveryManagement.getPendingNotifications();
		log.info("Total DeliveryRequests pending notification: " + requestsPendingNotifications.size());
		
		//create array with requests by supplier
		if (requestsPendingNotifications != null && !requestsPendingNotifications.isEmpty()) {
			for (DeliveryRequestData deliveryRequest: requestsPendingNotifications){
				List<DeliveryRequestData> supplierPendingNotifications = supplierPendingNotificationsMap.get(deliveryRequest.getSupplierId());
				if (supplierPendingNotifications == null) {
					supplierPendingNotifications = new ArrayList<DeliveryRequestData>();
					supplierPendingNotificationsMap.put(deliveryRequest.getSupplierId(), supplierPendingNotifications);
				}
				supplierPendingNotifications.add(deliveryRequest);
			}
		}		
		
		//send e-mail for each supplier and update notification status
		for (Long supplierId: supplierPendingNotificationsMap.keySet()){
			try {
				sendNewDeliveryRequest(supplierPendingNotificationsMap.get(supplierId));
				deliveryManagement.updateNotificationStatus(supplierPendingNotificationsMap.get(supplierId), MailNotificationStatusEnum.SENT);
			} catch (Exception e) {
				log.severe("Error while sending created Delivery Request e-mail to supplier " + supplierId + ": " + e.toString());
			}
			
		}
		
	}
	
	@Override
	public void checkPendingCancellationDeliveryRequestNotifications() {

		Map<Long, List<DeliveryRequestData>> supplierPendingNotificationsMap = new HashMap<Long, List<DeliveryRequestData>>();
		
		//get all delivery requests pending cancellation notification
		List<DeliveryRequestData> requestsPendingNotifications = deliveryManagement.getPendingCancellationNotifications();
		log.info("Total DeliveryRequests pending cancellation notification: " + requestsPendingNotifications.size());
		
		//create array with requests by supplier
		for (DeliveryRequestData deliveryRequest: requestsPendingNotifications){
			List<DeliveryRequestData> supplierPendingNotifications = supplierPendingNotificationsMap.get(deliveryRequest.getSupplierId());
			if (supplierPendingNotifications == null) {
				supplierPendingNotifications = new ArrayList<DeliveryRequestData>();
				supplierPendingNotificationsMap.put(deliveryRequest.getSupplierId(), supplierPendingNotifications);
			}
			supplierPendingNotifications.add(deliveryRequest);
		}
		
		//send e-mail for each supplier and update notification status
		for (Long supplierId: supplierPendingNotificationsMap.keySet()){
			try {
				sendCancelledDeliveryRequest(supplierPendingNotificationsMap.get(supplierId));
				deliveryManagement.updateNotificationStatus(supplierPendingNotificationsMap.get(supplierId), MailNotificationStatusEnum.SENT);
			} catch (Exception e) {
				log.severe("Error while sending cancelled Delivery Request e-mail to supplier " + supplierId + ": " + e.toString());
			}
			
		}
	}

	@Override
	public void sendContact(ContactData contactData, String type, DomainConfigurationData domainConfiguration) {
		
		StringBuffer sbTextVersion = new StringBuffer();
		StringBuffer sbHtmlVersion = new StringBuffer();
		
		sbTextVersion.append(" - Nome: " + contactData.getName());
		sbHtmlVersion.append("<li>Nome: " + contactData.getName() + "</li>");

		sbTextVersion.append(" - Razão Social: " + contactData.getCorporateName());
		sbHtmlVersion.append("<li>Razão Social: " + contactData.getCorporateName() + "</li>");
		
		sbTextVersion.append(" - CNPJ: " + contactData.getFiscalId());
		sbHtmlVersion.append("<li>CNPJ: " + contactData.getFiscalId() + "</li>");
		
		sbTextVersion.append(" - Segmento: " + contactData.getSegment());
		sbHtmlVersion.append("<li>Segmento: " + contactData.getSegment() + "</li>");
		
		sbTextVersion.append(" - Endereço: " + contactData.getAddress());
		sbHtmlVersion.append("<li>Endereço: " + contactData.getAddress() + "</li>");
		
		sbTextVersion.append(" - Cidade/Estado: " + contactData.getState());
		sbHtmlVersion.append("<li>Cidade/Estado: " + contactData.getState() + "</li>");
		
		sbTextVersion.append(" - Telefone: " + contactData.getPhone());
		sbHtmlVersion.append("<li>Telefone: " + contactData.getPhone() + "</li>");
			
		sbTextVersion.append(" - E-mail: " + contactData.getEmail());
		sbHtmlVersion.append("<li>E-mail: " + contactData.getEmail() + "</li>");
		
		sbTextVersion.append(" - Contato: " + contactData.getContact());
		sbHtmlVersion.append("<li>Contato: " + contactData.getContact() + "</li>");
		
		if (contactData.getDeliveryDetails() != null) {
			sbTextVersion.append(" - Entrega: " + contactData.getDeliveryDetails());
			sbHtmlVersion.append("<li>Entrega: " + contactData.getDeliveryDetails() + "</li>");	
		}

		sbTextVersion.append(" - Comentários: " + contactData.getComments());
		sbHtmlVersion.append("<li>Comentários: " + contactData.getComments() + "</li>");
		
		try {
			
			PublicUserData user = new PublicUserData();
			user.setName("Contato " + domainConfiguration.getTitle());
			user.setEmail(domainConfiguration.getMailSender());
			
			List<PublicUserData> toList = new ArrayList<PublicUserData>();
			toList.add(user);
			
			user = new PublicUserData();
			user.setName("Rafael Andrade");
			user.setEmail("rafael@andrade.inf.br");
			toList.add(user);

			/*
			 * Prepare e-mail content
			 */
			String emailSource = domainConfiguration.getLabels().get(type);
		    String textContent = "Novo contato de " + emailSource + " recebido através do site:\n" + sbTextVersion.toString();
	        String htmlContent = "Novo contato de " + emailSource + " recebido através do site: <ul>" + sbHtmlVersion.toString();
	        String subject = "Novo contato de " + emailSource + " recebido através do site " + domainConfiguration.getTitle();
		    
	        this.sendMail(toList, subject, textContent, htmlContent, domainConfiguration.getKey());
		    
		    log.info("Contact e-mail sent");

		} catch (Exception e) {
			log.info("Error sending Contact e-mail");
			log.info(e.toString());
		}
		
	}

	@Override
	public void sendTestMail() {
		try {
			
			List<PublicUserData> toList = new ArrayList<PublicUserData>();			
			PublicUserData user = null;
			
			user = new PublicUserData();
			user.setName("Rafael Andrade");
			user.setEmail("rafael@andrade.inf.br");
			toList.add(user);
			
			user = new PublicUserData();
			user.setName("Usuário Gráfica Demonstração");
			user.setEmail("sistema+grafica@andrade.inf.br");
			toList.add(user);

			/*
			 * Prepare e-mail content
			 */
		    String textContent = "Testing e-mail";
	        String htmlContent = "Testing";
	        String subject = "Testing mail send";
		    
	        this.sendMail(toList, subject, textContent, htmlContent, ConfigurationManagementBO.DEFAULT_KEY);
		    
		    log.info("Test e-mail sent");

		} catch (Exception e) {
			log.info("Error sending Test e-mail");
			log.info(e.toString());
		}
		
	}

	@Override
	public void checkPendingNewFileNotifications() {
		
		//get all document files pending notification
		List<DocumentsFileData> documentsFilesPendingNotifications = documentManagement.getDocumentsFilePendingNotifications();
		log.info("Total DocumentsFile pending notification: " + documentsFilesPendingNotifications.size());
		
		//create array with files by franchisor | franchisee
		if (documentsFilesPendingNotifications != null && !documentsFilesPendingNotifications.isEmpty()) {
			
			//Group notifications per franchisor/franchisee
			Map<String, List<DocumentsFileData>> franchiseesPendingNotificationsMap = new HashMap<String, List<DocumentsFileData>>();
			for (DocumentsFileData documentsFile: documentsFilesPendingNotifications){
				
				if (documentsFile.getAccessRestricted()) {
					//do not send notification for Franchisor Restricted Files
					continue;
				}
				
				if (documentsFile.getFranchiseeIds() == null || documentsFile.getFranchiseeIds().isEmpty()) {
					//no restriction for specific franchisees
					List<DocumentsFileData> franchiseePendingNotifications = franchiseesPendingNotificationsMap.get(documentsFile.getFranchisorId()+"-");
					if (franchiseePendingNotifications == null) {
						franchiseePendingNotifications = new ArrayList<DocumentsFileData>();
						franchiseesPendingNotificationsMap.put(documentsFile.getFranchisorId()+"-", franchiseePendingNotifications);
					}
					franchiseePendingNotifications.add(documentsFile);
				} else {
					//restriction by franchisee, so add document separate for each franchisee
					for (Long franchiseeId : documentsFile.getFranchiseeIds()) {
						List<DocumentsFileData> franchiseePendingNotifications = franchiseesPendingNotificationsMap.get(documentsFile.getFranchisorId()+"-"+franchiseeId);
						if (franchiseePendingNotifications == null) {
							franchiseePendingNotifications = new ArrayList<DocumentsFileData>();
							franchiseesPendingNotificationsMap.put(documentsFile.getFranchisorId()+"-"+franchiseeId, franchiseePendingNotifications);
						}
						franchiseePendingNotifications.add(documentsFile);
					}
				}
			}

			Map<Long, Boolean> featureFlag = new HashMap<Long, Boolean>();
		
			//for each franchisor/franchisee, send e-mail to all franchisee users and update notification status
			for (String key: franchiseesPendingNotificationsMap.keySet()){
				try {
					String[] keyParts = key.split("-");
					Long franchisorId = Long.valueOf(keyParts[0]);
					Long franchiseeId = null;
					if (keyParts.length > 1) {
						franchiseeId = Long.valueOf(keyParts[1]);
					}

					//check feature flag
					Boolean hasFranchiseeDocumentsEnabled = featureFlag.get(franchisorId);
					if (hasFranchiseeDocumentsEnabled == null) {
						DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByKey(franchisorManagement.getFranchisor(franchisorId).getPreferedDomainKey());
						hasFranchiseeDocumentsEnabled = domainConfiguration.getFeatureFlags().get("FRANCHISEE_DOCUMENTS");
						featureFlag.put(franchisorId, hasFranchiseeDocumentsEnabled);
					}				
					if (hasFranchiseeDocumentsEnabled) {
						sendNewDocumentsFile(franchisorId, franchiseeId, franchiseesPendingNotificationsMap.get(key));
					} else {
						log.info("Skip e-mail notification for new Document Files as feature flag FRANCHISEE_DOCUMENTS is disabled for franchisorId " + franchisorId);
					}	
				} catch (Exception e) {
					log.severe("Error while sending created Documents File e-mail to " + key + ": " + e.toString());
				}
				
			}
			
			//update documents notification status
			documentManagement.updateDocumentsFileNotificationStatus(documentsFilesPendingNotifications, MailNotificationStatusEnum.SENT);
		}
	}

	private String mapFolderPath(Long folderId) {
		if (folderId.equals(0L)) {
			return "/ Pasta Principal";
		} else {
			DocumentsFolderData folder = documentManagement.getDocumentsFolder(folderId);
			return mapFolderPath(folder.getParentId()) + " / " + folder.getName();
		}
	}

	private void sendNewDocumentsFile(Long franchisorId, Long franchiseeId, List<DocumentsFileData> documentsFiles) {
		
		// Generate document paths for all e-mails
		List<String> filePaths = documentsFiles.stream()
									.map(file -> mapFolderPath(file.getFolderId()) + " / " + file.getName())
									.collect(Collectors.toList());
		filePaths.sort(String.CASE_INSENSITIVE_ORDER);
		
		StringBuffer sbTextVersion = new StringBuffer();
		StringBuffer sbHtmlVersion = new StringBuffer();
		
		log.info("Create e-mail.");
		filePaths.stream().forEach(filePath -> {

			//create Text version for the item
			sbTextVersion.append("   * " + filePath);

			//create HTML version for the item
			sbHtmlVersion.append("<li>" + filePath + "</li>");
		});
		
		log.info("Preparing new document file e-mail.");
		
		List<PublicUserData> toList = new ArrayList<PublicUserData>();
		
		//send e-mail to franchisee users
		List<FranchiseeUserData> franchiseeUsers = new ArrayList<FranchiseeUserData>();
		
		if (franchiseeId == null) {
			
		    log.info("Send to all users from all franchisees if there is no restriction in the documents");
			List<FranchiseeData> franchisees = this.franchiseeManagement.getFranchiseesByFranchisorId(franchisorId);
			if (franchisees != null && !franchisees.isEmpty()) {
				for (FranchiseeData franchisee: franchisees) {
					franchiseeUsers.addAll(this.franchiseeManagement.getFranchiseeUsers(franchisee.getId()));
				}			
			}
		} else {
			
		    log.info("Send to all users from the specific franchisee if the document has restrictions");			
			franchiseeUsers.addAll(this.franchiseeManagement.getFranchiseeUsers(franchiseeId));
			
		}		
		
		for (FranchiseeUserData franchiseeUser: franchiseeUsers){
			toList.add(franchiseeUser.getUser());
		}
				
		//send mail if there are receivers
		if (toList != null && !toList.isEmpty()) {
			
			try {
				
				String domainKey = franchisorManagement.getFranchisor(franchisorId).getPreferedDomainKey();
				DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByKey(domainKey);

				/*
				 * Prepare e-mail content
				 */
			    String subject = "Novos arquivos adicionados";
			    String textContent = "Veja abaixo os novos arquivos adicionados:\n" +
       				 				 sbTextVersion.toString() +
       				 				 "\nAcesse o sistema e selecione o menu \"Documentos\" para ver os novos documentos: " + domainConfiguration.getMainURL();
			    String htmlContent = "Veja abaixo os novos arquivos adicionados:" +
    								 "<ul>" + sbHtmlVersion.toString() + "</ul>" +
    								 "Acesse o sistema e selecione o menu \"Documentos\" para ver os novos documentos: " +
    								 "<a href='"+domainConfiguration.getMainURL()+"'>"+domainConfiguration.getMainURL()+"</a>";
			    
			    this.sendMail(toList, subject, textContent, htmlContent, domainKey);
			    
			    log.info("New Documents e-mail sent to " + toList.size() + " people.");

			} catch (Exception e) {
				log.info("Error sending New Documents e-mail");
				log.info(e.toString());
			}
			
		} else {
			log.info("No receivers to send e-mail");
		}		
	}
	
	@Override
	public void checkPendingNewMessageCommentNotifications() {
		
		for (DomainKeysEnum domain : DomainKeysEnum.values()) {
			
			log.info("Checking pending Notifications for domain " + domain.name() + "...");
			
			Map<Long, List<NotificationData>> userMessageCommentsPendingNotificationsMap = new HashMap<Long, List<NotificationData>>();
			Map<Long, List<NotificationData>> userAnnouncementsPendingNotificationsMap = new HashMap<Long, List<NotificationData>>();
			
			//get all message comments pending notification
			List<NotificationData> messageCommentsPendingNotifications = notificationManagement.getMessageCommentsPendingNotifications(domain.name());
			log.info("Total message comments pending notification: " + messageCommentsPendingNotifications.size());
			
			//create array with message comment notifications per user
			if (messageCommentsPendingNotifications != null && !messageCommentsPendingNotifications.isEmpty()) {
				for (NotificationData messageCommentNotification: messageCommentsPendingNotifications){
					List<NotificationData> userPendingNotifications = userMessageCommentsPendingNotificationsMap.get(messageCommentNotification.getUserId());
					if (userPendingNotifications == null) {
						userPendingNotifications = new ArrayList<NotificationData>();
						userMessageCommentsPendingNotificationsMap.put(messageCommentNotification.getUserId(), userPendingNotifications);
					}
					userPendingNotifications.add(messageCommentNotification);
				}
			}
			
			//get all announcement comments pending notification
			List<NotificationData> announcementPendingNotifications = notificationManagement.getAnnouncementPendingNotifications(domain.name());
			log.info("Total annoucements pending notification: " + announcementPendingNotifications.size());
			
			//create array with announcement comment notifications per user
			if (announcementPendingNotifications != null && !announcementPendingNotifications.isEmpty()) {
				for (NotificationData announcementNotification: announcementPendingNotifications){
					List<NotificationData> userPendingNotifications = userAnnouncementsPendingNotificationsMap.get(announcementNotification.getUserId());
					if (userPendingNotifications == null) {
						userPendingNotifications = new ArrayList<NotificationData>();
						userAnnouncementsPendingNotificationsMap.put(announcementNotification.getUserId(), userPendingNotifications);
					}
					userPendingNotifications.add(announcementNotification);
				}
			}
			
			//consolidate all user ids
			Set<Long> userIds = new HashSet<Long>();
			userIds.addAll(userMessageCommentsPendingNotificationsMap.keySet());
			userIds.addAll(userAnnouncementsPendingNotificationsMap.keySet());
			
			//send e-mail for each user and update notification status
			for (Long userId: userIds){
				try {
					sendNewMessageComments(userMessageCommentsPendingNotificationsMap.get(userId), userAnnouncementsPendingNotificationsMap.get(userId), domain.name());
					
					if (userMessageCommentsPendingNotificationsMap.get(userId) != null && !userMessageCommentsPendingNotificationsMap.get(userId).isEmpty()) {
						notificationManagement.updateNotificationStatus(userMessageCommentsPendingNotificationsMap.get(userId), MailNotificationStatusEnum.SENT);
					}
					
					if (userAnnouncementsPendingNotificationsMap.get(userId) != null && !userAnnouncementsPendingNotificationsMap.get(userId).isEmpty()) {
						notificationManagement.updateNotificationStatus(userAnnouncementsPendingNotificationsMap.get(userId), MailNotificationStatusEnum.SENT);
					}				
				} catch (Exception e) {
					log.log(Level.SEVERE, "Error while sending New Message Comment e-mail to user " + userId, e);
				}
				
			}
		}
	}

	private void sendNewMessageComments(List<NotificationData> messageCommentNotifications, List<NotificationData> announcementNotifications, String domainKey) {
		
		//send e-mail to user
		//ATTENTION: get user ID from first notification, 
		// as it is expected that only notifications for ONE SINGLE user is received
		Long userId;
		if (messageCommentNotifications != null && !messageCommentNotifications.isEmpty()) {
			userId = messageCommentNotifications.get(0).getUserId();
		} else {
			userId = announcementNotifications.get(0).getUserId();
		}
		PublicUserData user = userManagement.getUser(userId);
		 
		List<PublicUserData> toList = new ArrayList<PublicUserData>();
		toList.add(user);
				
		//send mail if there are receivers
		if (toList != null && !toList.isEmpty()) {
			
			try {
				
				DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByKey(domainKey);

				/*
				 * Prepare e-mail content
				 */
			    String subject = "";
			    String content = "Você possui ";
			    String andContent = "";
			    
			    if (messageCommentNotifications != null && !messageCommentNotifications.isEmpty()) {
			    	subject = subject + "Nova mensagem";
			    	andContent = " e ";
			    	
			    	// create set with unique message comment ids (reference 2)
			    	// as we may have duplicated due to internal FRANCHISOR messages 
					Set<Long> messageCommentNotificationIds = new HashSet<Long>();
					for (NotificationData notification : messageCommentNotifications) {
						messageCommentNotificationIds.add(notification.getReferenceId2());
					}
			    	
			    	if (messageCommentNotificationIds.size() == 1) {
				    	content = content + "1 nova mensagem";				    	
				    } else {
				    	content = content + messageCommentNotificationIds.size() + " novas mensagens";
				    }
			    }
			    
			    if (announcementNotifications != null && !announcementNotifications.isEmpty()) {
			    	subject = subject + andContent + "Novo aviso";

			    	if (announcementNotifications.size() == 1) {
				    	content = content + andContent + "1 novo aviso";
				    } else {
				    	content = content + andContent + announcementNotifications.size() + " novos avisos";
				    }
			    }
			    
			    String textContent = content + "\nAcesse o sistema para ver mais detalhes: " + domainConfiguration.getMainURL();
			    String htmlContent = content + "<br />Acesse o sistema para ver mais detalhes: " +
    								 "<a href='"+domainConfiguration.getMainURL()+"'>"+domainConfiguration.getMainURL()+"</a>";
			    		
			    this.sendMail(toList, subject, textContent, htmlContent, domainConfiguration.getKey());
			    
			    log.info("New notifications e-mail sent to user " + userId);

			} catch (Exception e) {
				log.log(Level.SEVERE, "Error sending New notifications e-mail to user " + userId, e);
			}
			
		} else {
			log.info("No receivers to send e-mail");
		}
		
	}

	@Override
	public void sendCalendarEventUpdate(CalendarEventData calendarEventOld, CalendarEventData calendarEventNew, OperationTypeEnum operation) {

		if (operation == OperationTypeEnum.CREATE || operation == OperationTypeEnum.DELETE) {
			
			Map<Long, PublicUserData> toMap = new HashMap<Long, PublicUserData>();
					
			CalendarEventData event = null;
			if (operation == OperationTypeEnum.CREATE) {
				event = calendarEventNew;
			} else if (operation == OperationTypeEnum.DELETE) {
				event = calendarEventOld;
			}
			
			// Always notify franchisor users
			for (FranchisorUserData franchisorUser : franchisorManagement.getFranchisorUsers(event.getEntityId())) {
				toMap.put(franchisorUser.getUser().getId(), franchisorUser.getUser());
			}
			
			// Check if need to notify franchisee users and which ones			
			if (!event.getAccessRestricted()) {
				
				// check if event has franchisse restriction
				List<Long> franchiseeIds = event.getFranchiseeIds();
				if (franchiseeIds != null && !franchiseeIds.isEmpty()) {
					
					// Notify only franchisse users for franchisees in the list of restricted access
					for (Long franchiseeId : franchiseeIds) {
						for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchiseeId)) {
							toMap.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
						}
					}
					
				} else {
					
					// Notify franchisee users from all franchisees from the franchisor
					for (FranchiseeData franchisee : franchiseeManagement.getFranchiseesByFranchisorId(event.getEntityId())) {
						for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchisee.getId())) {
							toMap.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
						}
					}
				}
				
			}
			
			//send mail
			List<PublicUserData> toList = new ArrayList<PublicUserData>(toMap.values());
			sendCalendarEventCreateDelete(toList, event, operation);
			
			
		} else if (operation == OperationTypeEnum.UPDATE) {
			
			Map<Long, PublicUserData> toMapUpdate = new HashMap<Long, PublicUserData>();
			Map<Long, PublicUserData> toMapCreate = new HashMap<Long, PublicUserData>();
			Map<Long, PublicUserData> toMapDelete = new HashMap<Long, PublicUserData>();
			
			boolean contentChange = calendarEventContentChange(calendarEventNew, calendarEventOld);
			
			// if there is a change in the event content, always notify franchisor users
			if (contentChange) {
				for (FranchisorUserData franchisorUser : franchisorManagement.getFranchisorUsers(calendarEventNew.getEntityId())) {
					toMapUpdate.put(franchisorUser.getUser().getId(), franchisorUser.getUser());
				}
			}
			
			if (calendarEventNew.getAccessRestricted() && !calendarEventOld.getAccessRestricted()) {
				
				/*
				 * Access Restriction changed to ONLY FRANCHISOR - need to send DELETE message to franchisees
				 */
								
				// check if event HAD franchisse restriction before
				List<Long> franchiseeIds = calendarEventOld.getFranchiseeIds();
				if (franchiseeIds != null && !franchiseeIds.isEmpty()) {
					
					// Notify only franchisse users for franchisees in the list of restricted access
					for (Long franchiseeId : franchiseeIds) {
						for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchiseeId)) {
							toMapDelete.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
						}
					}
					
				} else {
					
					// Notify franchisee users from all franchisees from the franchisor
					for (FranchiseeData franchisee : franchiseeManagement.getFranchiseesByFranchisorId(calendarEventNew.getEntityId())) {
						for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchisee.getId())) {
							toMapDelete.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
						}
					}
				}
					
			} else if (!calendarEventNew.getAccessRestricted() && calendarEventOld.getAccessRestricted()) {
				
				/*
				 * Access Restriction changed to FRANCHISOR AND FRANCHISEES - need to send CREATE message to franchisees
				 */
				
				// check if event HAVE franchisse restriction
				List<Long> franchiseeIds = calendarEventNew.getFranchiseeIds();
				if (franchiseeIds != null && !franchiseeIds.isEmpty()) {
					
					// Notify only franchisse users for franchisees in the list of restricted access
					for (Long franchiseeId : franchiseeIds) {
						for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchiseeId)) {
							toMapCreate.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
						}
					}
					
				} else {
					
					// Notify franchisee users from all franchisees from the franchisor
					for (FranchiseeData franchisee : franchiseeManagement.getFranchiseesByFranchisorId(calendarEventNew.getEntityId())) {
						for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchisee.getId())) {
							toMapCreate.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
						}
					}
				}
				
			} else if (!calendarEventNew.getAccessRestricted()) {
				
				/*
				 * Access Restriction DID NOT CHANGE and is set to FRANCHISOR AND FRANCHISEES 
				 */
				
				List<Long> oldFranchiseeIds = calendarEventOld.getFranchiseeIds();
				List<Long> newFranchiseeIds = calendarEventNew.getFranchiseeIds();
				
				if ((oldFranchiseeIds == null || oldFranchiseeIds.isEmpty()) && (newFranchiseeIds != null && !newFranchiseeIds.isEmpty())) {
					
					//There was no restriction on franchisees and now it has.
					for (FranchiseeData franchisee : franchiseeManagement.getFranchiseesByFranchisorId(calendarEventNew.getEntityId())) {
						if (!newFranchiseeIds.contains(franchisee.getId())) {
							//Need to send DELETE message to all franchisees that are not in the list
							for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchisee.getId())) {
								toMapDelete.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
							}
						} else {
							//Need to send UPDATE message to all franchisees that were already in the list if there are changes
							if (contentChange) {
								for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchisee.getId())) {
									toMapUpdate.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
								}								
							}
						}
					}
					
				} else if ((oldFranchiseeIds != null && !oldFranchiseeIds.isEmpty()) && (newFranchiseeIds == null || newFranchiseeIds.isEmpty())) {
					
					//There was restriction on franchisees and now it does not have.
					for (FranchiseeData franchisee : franchiseeManagement.getFranchiseesByFranchisorId(calendarEventNew.getEntityId())) {
						if (!oldFranchiseeIds.contains(franchisee.getId())) {
							//Need to send CREATE message to all franchisees that were not in the list
							for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchisee.getId())) {
								toMapCreate.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
							}
						} else {
							//Need to send UPDATE message to all franchisees that were already in the list if there are changes
							if (contentChange) {
								for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchisee.getId())) {
									toMapUpdate.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
								}								
							}
						}				
					}
					
				} else if ((oldFranchiseeIds == null || oldFranchiseeIds.isEmpty()) && (newFranchiseeIds == null || newFranchiseeIds.isEmpty())) {
					
					//There is no restriction on franchisees and also there was no restrition before
					//Need to send UPDATE if something changed in the event
					if (contentChange) {
						for (FranchiseeData franchisee : franchiseeManagement.getFranchiseesByFranchisorId(calendarEventNew.getEntityId())) {
							for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchisee.getId())) {
								toMapUpdate.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
							}
						}
					}
					
				} else {
					
					//There was restriction on franchisees and now it still have
					//Need to check changes in the list of restricted franchisees and send message accordingly:
					
					for (Long franchiseeId : newFranchiseeIds) {
						
						if (oldFranchiseeIds.contains(franchiseeId)) {
							
							//if franchisse in the new list was already in the list, need to send UPDATE only if there are changes in the event
							if (contentChange) {
								for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchiseeId)) {
									toMapUpdate.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
								}
							}
							
						} else {
							
							//if franchisee was NOT in the list but is now, need to send CREATE
							for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchiseeId)) {
								toMapCreate.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
							}
						}
					}
					
					for (Long franchiseeId : oldFranchiseeIds) {
						
						if (!newFranchiseeIds.contains(franchiseeId)) {
							
							//if franchisee was in the list but is NOT anymore, need to send DELETE message
							for (FranchiseeUserData franchiseeUser : franchiseeManagement.getFranchiseeUsers(franchiseeId)) {
								toMapDelete.put(franchiseeUser.getUser().getId(), franchiseeUser.getUser());
							}
						}						
					}
				}				
			}
			
			//remove duplicated e-mails and change notification type if same e-mail in multiple lists
			List<PublicUserData> toListUpdate = new ArrayList<PublicUserData>(toMapUpdate.values());
			
			List<Long> userIds = new ArrayList<Long>(toMapCreate.keySet());					
			for (Long userId : userIds) {
				if (toMapUpdate.containsKey(userId)) {
					//user already notified by an update, so no need to send CREATE message
					toMapCreate.remove(userId);
				} else if (toMapDelete.containsKey(userId)) {
					//DELETE and CREATE at same time - User from multiple franchisees
					if (contentChange) {
						toMapUpdate.put(userId, toMapCreate.get(userId));
						toListUpdate.add(toMapCreate.get(userId));
					}
					toMapCreate.remove(userId);
					toMapDelete.remove(userId);
				}
			}
			List<PublicUserData> toListCreate = new ArrayList<PublicUserData>(toMapCreate.values());
			
			userIds = new ArrayList<Long>(toMapDelete.keySet());
			for (Long userId : userIds) {
				if (toMapUpdate.containsKey(userId)) {
					//user already notified by an update, so no need to send DELETE message
					toMapDelete.remove(userId);
				}
			}
			List<PublicUserData> toListDelete = new ArrayList<PublicUserData>(toMapDelete.values());

			//send e-mail
			sendCalendarEventCreateDelete(toListCreate, calendarEventNew, OperationTypeEnum.CREATE);
			sendCalendarEventCreateDelete(toListDelete, calendarEventOld, OperationTypeEnum.DELETE);
			sendCalendarEventUpdate(toListUpdate, calendarEventOld, calendarEventNew);
		}
	}

	private boolean calendarEventContentChange(CalendarEventData calendarEventNew, CalendarEventData calendarEventOld) {
		
		if (!calendarEventNew.getFromHour().equals(calendarEventOld.getFromHour())) return true;
		
		if (!calendarEventNew.getToHour().equals(calendarEventOld.getToHour())) return true;
		
		if (!calendarEventNew.getAllDay().equals(calendarEventOld.getAllDay())) return true;
		
		if (!calendarEventNew.getTitle().equals(calendarEventOld.getTitle())) return true;
		
		if (!calendarEventNew.getLocation().equals(calendarEventOld.getLocation())) return true;
		
		if (!calendarEventNew.getDescription().equals(calendarEventOld.getDescription())) return true;
		
		return false;
		
	}
	
	private void sendCalendarEventCreateDelete(List<PublicUserData> toList, CalendarEventData event, OperationTypeEnum operation) {
		
		if (toList != null && !toList.isEmpty()) {
			
			String subject = null;
			
			try {
				
				FranchisorData franchisor = franchisorManagement.getFranchisor(event.getEntityId());
				DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByKey(franchisor.getPreferedDomainKey());

				/*
				 * Prepare e-mail content
				 */			    
				String textContent = "";
				String htmlContent = "";				
				
			    if (operation == OperationTypeEnum.CREATE) {
			    	subject = "Novo evento criado: " + event.getTitle();
			    	textContent += "Veja abaixo os detalhes do novo evento criado:";
			    	htmlContent += "Veja abaixo os detalhes do novo evento criado";
			    } else {
			    	subject = "Evento cancelado: " + event.getTitle();
			    	textContent += "Veja abaixo os detalhes do evento cancelado:";
			    	htmlContent += "Veja abaixo os detalhes do evento cancelado:";
			    }
			    
			    textContent += "- Título: " + event.getTitle() + "\n"
		    			     + "- Local: " + event.getLocation() + "\n";
		    	if (event.getAllDay()) {
		    		textContent += "- Data e Hora: " + DateUtil.getDate(event.getFromHour(), DateUtil.SLASH_PATTERN)  + " - o dia todo\n";
		    	} else {
		    		textContent += "- Data e Hora: " + DateUtil.getDate(event.getFromHour(), DateUtil.SLASH_PATTERN)  + " das " + DateUtil.getDate(event.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(event.getToHour(), "HH:mm") + "\n";
		    	}				    	
		    	textContent += "- Descrição: " + event.getDescription();
			    textContent += "\nAcesse o sistema para ver mais detalhes: " + domainConfiguration.getMainURL();
			    
			    
			    htmlContent += "<ul>";
			    htmlContent += "<li><b>Título: </b>" + event.getTitle() + "</li>"
			    		     + "<li><b>Local: </b>" + event.getLocation() + "</li>";
			    if (event.getAllDay()) {
			    	htmlContent += "<li><b>Data e Hora: </b>" + DateUtil.getDate(event.getFromHour(), DateUtil.SLASH_PATTERN)  + " - o dia todo</li>";
		    	} else {
		    		htmlContent += "<li><b>Data e Hora: </b>" + DateUtil.getDate(event.getFromHour(), DateUtil.SLASH_PATTERN)  + " das " + DateUtil.getDate(event.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(event.getToHour(), "HH:mm") + "</li>";
		    	}	
			    htmlContent += "<li><b>Descrição: </b>" + event.getDescription() + "</li>";
			    htmlContent += "</ul>";
			    htmlContent += "Acesse o sistema para ver mais detalhes: " +
    								 "<a href='"+domainConfiguration.getMainURL()+"'>"+domainConfiguration.getMainURL()+"</a>";
			    		
			    this.sendMail(toList, subject, textContent, htmlContent, domainConfiguration.getKey());
			    
			    log.fine(subject);
			    for (PublicUserData user : toList) {
			    	log.fine(" - " + user.getName() + " - " + user.getEmail());
			    }


			} catch (Exception e) {
				log.info("Error sending calendar event");
				log.info(e.toString());
				
			    log.fine(subject);
			    for (PublicUserData user : toList) {
			    	log.fine(" - " + user.getName() + " - " + user.getEmail());
			    }
			}
			
		} else {
			log.info("No receivers to send e-mail");
		}
		
	}
	
	private void sendCalendarEventUpdate(List<PublicUserData> toList, CalendarEventData eventOld, CalendarEventData eventNew) {

		if (toList != null && !toList.isEmpty()) {
			
			String subject = null;
			
			try {
				
				FranchisorData franchisor = franchisorManagement.getFranchisor(eventNew.getEntityId());
				DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByKey(franchisor.getPreferedDomainKey());

				/*
				 * Prepare e-mail content
				 */
				if (eventOld.getTitle().equals(eventNew.getTitle())) {
					subject = "Evento atualizado: " + eventOld.getTitle();
				} else {
					subject = "Evento atualizado: " + eventNew.getTitle() + " (" + eventOld.getTitle() + ")";
				}
			    
			    String textContent = "Veja abaixo os detalhes do evento atualizado:";
			    String htmlContent = "Veja abaixo os detalhes do evento atualizado:";
			    htmlContent += "<ul>";
			    
			    if (eventOld.getTitle().equals(eventNew.getTitle())) {
			    	textContent += "- Título: " + eventOld.getTitle() + "\n";
			    	htmlContent += "<li><b>Título: </b>" + eventOld.getTitle() + "</li>";
				} else {
					textContent += "- Título: " + eventNew.getTitle() + " (" + eventOld.getTitle() + ")\n";
					htmlContent += "<li><b>Título: </b>" + eventNew.getTitle() + " <i><strike>(" + eventOld.getTitle() + ")</strike></i></li>";
				}
			    
			    if (eventOld.getLocation().equals(eventNew.getLocation())) {
			    	textContent += "- Local: " + eventOld.getLocation() + "\n";
			    	htmlContent += "<li><b>Local: </b>" + eventOld.getLocation() + "</li>";
				} else {
					textContent += "- Local: " + eventNew.getLocation() + " (" + eventOld.getLocation() + ")\n";
					htmlContent += "<li><b>Local: </b>" + eventNew.getLocation() + " <i><strike>(" + eventOld.getLocation() + ")</strike></i></li>";
				}
			    
			    if (!eventOld.getAllDay() && eventNew.getAllDay()) {
			    	//Changed from defined time to all day
			    	textContent += "- Data e Hora: " + DateUtil.getDate(eventNew.getFromHour(), DateUtil.SLASH_PATTERN) + " - o dia todo (" + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + " das " + DateUtil.getDate(eventOld.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(eventOld.getToHour(), "HH:mm") + ")\n";
			    	htmlContent += "<li><b>Data e Hora: </b>" + DateUtil.getDate(eventNew.getFromHour(), DateUtil.SLASH_PATTERN) + " - o dia todo <i><strike>(" + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + " das " + DateUtil.getDate(eventOld.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(eventOld.getToHour(), "HH:mm") + ")</strike></i></li>";
			    } else if (eventOld.getAllDay() && !eventNew.getAllDay()) {
			    	//Changed from all day to defined time
			    	textContent += "- Data e Hora: " + DateUtil.getDate(eventNew.getFromHour(), DateUtil.SLASH_PATTERN) + " das " + DateUtil.getDate(eventNew.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(eventNew.getToHour(), "HH:mm") + " (" + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN)  + " - o dia todo)\n";
			    	htmlContent += "<li><b>Data e Hora: </b>" + DateUtil.getDate(eventNew.getFromHour(), DateUtil.SLASH_PATTERN) + " das " + DateUtil.getDate(eventNew.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(eventNew.getToHour(), "HH:mm") + " <i><strike>(" + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + " - o dia todo)</strike></i></li>";
			    } else if (eventOld.getAllDay()) {
			    	//Already all day, check if day changed
			    	if (eventOld.getFromHour().equals(eventNew.getFromHour())) {
			    		textContent += "- Data e Hora: " + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + " - o dia todo";
			    		htmlContent += "<li><b>Data e Hora: </b>" + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + " - o dia todo</li>";
			    	} else {
			    		textContent += "- Data e Hora: " + DateUtil.getDate(eventNew.getFromHour(), DateUtil.SLASH_PATTERN) + " (" + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + ") - o dia todo\n";
			    		htmlContent += "<li><b>Data e Hora: </b>" + DateUtil.getDate(eventNew.getFromHour(), DateUtil.SLASH_PATTERN) + " <i><strike>(" + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + ")</strike></i> - o dia todo</li>";
			    	}
			    } else {
			    	//Already defined time, check if day or time changed
			    	if (eventOld.getFromHour().equals(eventNew.getFromHour()) && eventOld.getToHour().equals(eventNew.getToHour())) {
			    		textContent += "- Data e Hora: " + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + " das " + DateUtil.getDate(eventOld.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(eventOld.getToHour(), "HH:mm")+"\n";
				    	htmlContent += "<li><b>Data e Hora: </b>" + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + " das " + DateUtil.getDate(eventOld.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(eventOld.getToHour(), "HH:mm") + "</li>";
			    	} else {
			    		textContent += "- Data e Hora: " + DateUtil.getDate(eventNew.getFromHour(), DateUtil.SLASH_PATTERN) + " das " + DateUtil.getDate(eventNew.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(eventNew.getToHour(), "HH:mm")+" (" + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + " das " + DateUtil.getDate(eventOld.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(eventOld.getToHour(), "HH:mm") + ")\n";
				    	htmlContent += "<li><b>Data e Hora: </b>" + DateUtil.getDate(eventNew.getFromHour(), DateUtil.SLASH_PATTERN) + " das " + DateUtil.getDate(eventNew.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(eventNew.getToHour(), "HH:mm") + " <i><strike>(" + DateUtil.getDate(eventOld.getFromHour(), DateUtil.SLASH_PATTERN) + " das " + DateUtil.getDate(eventOld.getFromHour(), "HH:mm") + " às " + DateUtil.getDate(eventOld.getToHour(), "HH:mm") + ")</strike></i></li>";
			    	}
			    }
			    
			    if (eventOld.getDescription().equals(eventNew.getDescription())) {
			    	textContent += "- Descrição: " + eventOld.getDescription() + "\n";
			    	htmlContent += "<li><b>Descrição: </b>" + eventOld.getDescription() + "</li>";
				} else {
					textContent += "- Descrição: " + eventNew.getDescription() + " (" + eventOld.getDescription() + ")\n";
					htmlContent += "<li><b>Descrição: </b>" + eventNew.getDescription() + " <i><strike>(" + eventOld.getDescription() + ")</strike></i></li>";
				}
			    
			    textContent += "\nAcesse o sistema para ver mais detalhes: " + domainConfiguration.getMainURL();
			    htmlContent += "</ul>";
			    htmlContent += "Acesse o sistema para ver mais detalhes: " +
    								 "<a href='"+domainConfiguration.getMainURL()+"'>"+domainConfiguration.getMainURL()+"</a>";
			    		
			    this.sendMail(toList, subject, textContent, htmlContent, domainConfiguration.getKey());
			    
			    log.fine(subject);
			    for (PublicUserData user : toList) {
			    	log.fine(" - " + user.getName() + " - " + user.getEmail());
			    }


			} catch (Exception e) {
				log.info("Error sending calendar event");
				log.info(e.toString());
				
				log.fine(subject);
			    for (PublicUserData user : toList) {
			    	log.fine(" - " + user.getName() + " - " + user.getEmail());
			    }
			}
			
		} else {
			log.info("No receivers to send e-mail");
		}
		
	}

	@Override
	public void checkPendingProductSizePriceHistoryNotifications() {

		//get all Product Size Price History pending notification
		List<ProductSizePriceHistoryData> productSizePriceHistoryPendingNotifications = productManagement.getProductSizePriceHistoryPendingNotifications();
		log.info("Total Product Size Price Histories pending notification: " + productSizePriceHistoryPendingNotifications.size());
		
		if (productSizePriceHistoryPendingNotifications != null && !productSizePriceHistoryPendingNotifications.isEmpty()) {
			try {
				sendPendingProductSizePriceHistoryNotifications(productSizePriceHistoryPendingNotifications);
				productManagement.updateProductSizePriceHistoryNotificationStatus(productSizePriceHistoryPendingNotifications, MailNotificationStatusEnum.SENT);
			} catch (Exception e) {
				log.severe("Error while sending Product Size Price History e-mail: " + e.toString());
			}
		}
		
	}

	private void sendPendingProductSizePriceHistoryNotifications(List<ProductSizePriceHistoryData> productSizePriceHistoryPendingNotifications) {
		
		DecimalFormat priceFormatter = new DecimalFormat("###,##0.0000");
		
		Map<Long, ProductData> products = new HashMap<Long, ProductData>();
		Map<Long, PublicUserData> users = new HashMap<Long, PublicUserData>();
		Map<Long, FranchisorData> franchisors = new HashMap<Long, FranchisorData>();
		
		StringBuffer sbTextVersion = new StringBuffer();
		StringBuffer sbHtmlVersion = new StringBuffer();
		
		Collections.sort(productSizePriceHistoryPendingNotifications, new Comparator<ProductSizePriceHistoryData>() {
	        @Override
	        public int compare(ProductSizePriceHistoryData request1, ProductSizePriceHistoryData request2)
	        {
	            return  request1.getDate().compareTo(request2.getDate());
	        }
	    });
		
		log.info("Getting Product Size Price History data to create the e-mail.");
		for (ProductSizePriceHistoryData productSizePriceHistory: productSizePriceHistoryPendingNotifications){
			
			//get product data
			ProductData product = products.get(productSizePriceHistory.getProductId());
			if (product == null){
				product = productManagement.getProduct(productSizePriceHistory.getProductId());
				products.put(productSizePriceHistory.getProductId(), product);
			}
			
			FranchisorData franchisor = franchisors.get(product.getFranchisorId());
			if (franchisor == null){
				franchisor = franchisorManagement.getFranchisor(product.getFranchisorId());
				franchisors.put(product.getFranchisorId(), franchisor);
			}
			
			//get product size data
			ProductSizeData productSize = null;
			for (ProductSizeData size: product.getSizes()) {
				if (size.getId().equals(productSizePriceHistory.getProductSizeId())) {
					productSize = size;
				}
			}
			
			//get user data
			PublicUserData user = users.get(productSizePriceHistory.getUserId());
			if (user == null){
				user = userManagement.getUser(productSizePriceHistory.getUserId());
				users.put(productSizePriceHistory.getUserId(), user);
			}
			
			//create Text version for the item
			sbTextVersion.append("   * " + DateUtil.getDate(productSizePriceHistory.getDate()));
			sbTextVersion.append(" - " + franchisor.getPreferedDomainKey());
			sbTextVersion.append(" - " + franchisor.getName());
			sbTextVersion.append(" - " + product.getName());
			if (product.getSizes().size() > 1) {
				sbTextVersion.append(" - " + product.getSizeName() + ": " + productSize.getName());
			}
			sbTextVersion.append(" - alterado por " + user.getName());
			sbTextVersion.append(" - preço anterior R$" + priceFormatter.format(productSizePriceHistory.getOldUnitPrice()));
			sbTextVersion.append(" - novo preço R$" + priceFormatter.format(productSizePriceHistory.getNewUnitPrice()));
			
			//create HTML version for the item
			sbHtmlVersion.append("<li>" + DateUtil.getDate(productSizePriceHistory.getDate()));
			sbHtmlVersion.append(" - " + product.getName());
			if (product.getSizes().size() > 1) {
				sbHtmlVersion.append(" - " + product.getSizeName() + ": " + productSize.getName());
			}
			sbHtmlVersion.append(" - alterado por " + user.getName());
			sbHtmlVersion.append(" - preço anterior R$" + priceFormatter.format(productSizePriceHistory.getOldUnitPrice()));
			sbHtmlVersion.append(" - novo preço R$" + priceFormatter.format(productSizePriceHistory.getNewUnitPrice()));
			sbHtmlVersion.append("</li>");			
		}
		
		log.info("Preparing product size price history e-mail.");
		
		//send e-mail to admin users
		List<PublicUserData> toList = userManagement.getUsersByRole(UserProfileEnum.ADMINISTRATOR);
				
		//send mail if there are receivers
		if (toList != null && !toList.isEmpty()) {
			
			try {
			    //TODO: check how to get proper domain information to send this e-mail, instead of use default domain
				String domainKey = ConfigurationManagementBO.DEFAULT_KEY;
				DomainConfigurationData domainConfiguration = configurationManagement.getDomainConfigurationByKey(domainKey);

				/*
				 * Prepare e-mail content
				 */
			    String subject = "Preços de produtos alterados pelos fornecedores";
			    String textContent = "Veja abaixo os preços alterados pelos fornecedores:\n" +
       				 				 sbTextVersion.toString() +
       				 				 "\nAcesse o sistema para ver mais detalhes: " + domainConfiguration.getMainURL();
			    String htmlContent = "Veja abaixo os preços alterados pelos fornecedores:" +
    								 "<ul>" + sbHtmlVersion.toString() + "</ul>" +
    								 "Acesse o sistema para ver mais detalhes: " +
    								 "<a href='"+domainConfiguration.getMainURL()+"'>"+domainConfiguration.getMainURL()+"</a>";
			    		
			    this.sendMail(toList, subject, textContent, htmlContent, domainKey);
			    
			    log.info("Product size price history e-mail sent");

			} catch (Exception e) {
				log.info("Error sending Product size price history e-mail");
				log.info(e.toString());
			}
			
		} else {
			log.info("No receivers to send e-mail");
		}
	}

	@Override
	public void sendErrorMail(String errorSubject, String errorMessage) {
		try {
			
			List<PublicUserData> toList = new ArrayList<PublicUserData>();			
			PublicUserData user = null;
			
			user = new PublicUserData();
			user.setName("Rafael Andrade");
			user.setEmail("rafael@andrade.inf.br");
			toList.add(user);

			/*
			 * Prepare e-mail content
			 */
	        this.sendMail(toList, "A severe error happened: " + errorSubject, errorMessage, errorMessage, ConfigurationManagementBO.DEFAULT_KEY);
		    
		    log.info("Error e-mail sent");

		} catch (Exception e) {
			log.info("Error sending Error e-mail");
			log.info(e.toString());
		}
		
	}
}
