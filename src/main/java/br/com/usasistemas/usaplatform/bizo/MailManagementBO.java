package br.com.usasistemas.usaplatform.bizo;

import java.util.List;

import br.com.usasistemas.usaplatform.api.request.data.ContactData;
import br.com.usasistemas.usaplatform.model.data.CalendarEventData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.DomainConfigurationData;
import br.com.usasistemas.usaplatform.model.data.ManufactureRequestData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.UserData;
import br.com.usasistemas.usaplatform.model.enums.OperationTypeEnum;

public interface MailManagementBO {
	
	public void sendNewManufactureRequest(ManufactureRequestData manufactureRequest);
	public void sendNewDeliveryRequest(List<DeliveryRequestData> deliveryRequests);
	public void sendStockBelowMinimun(ProductData product);
	public void sendResetPassword(UserData user, String uid, DomainConfigurationData domainConfiguration);
	public void checkPendingDeliveryRequestNotifications();
	public void checkPendingCancellationDeliveryRequestNotifications();
	public void sendContact(ContactData contactData, String type, DomainConfigurationData domainConfiguration);
	public void sendCancelledDeliveryRequest(List<DeliveryRequestData> deliveryRequests);
	public void sendTestMail();
	public void checkPendingNewFileNotifications();
	public void checkPendingNewMessageCommentNotifications();
	public void sendCalendarEventUpdate(CalendarEventData calendarEventOld, CalendarEventData calendarEventNew, OperationTypeEnum operation);
	public void checkPendingProductSizePriceHistoryNotifications();
	public void sendErrorMail(String errorSubject, String errorMessage);
}
