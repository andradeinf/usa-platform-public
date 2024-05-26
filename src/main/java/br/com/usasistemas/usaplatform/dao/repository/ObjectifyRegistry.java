package br.com.usasistemas.usaplatform.dao.repository;

import com.googlecode.objectify.ObjectifyService;

import br.com.usasistemas.usaplatform.model.entity.AdministratorUser;
import br.com.usasistemas.usaplatform.model.entity.Announcement;
import br.com.usasistemas.usaplatform.model.entity.CalendarEvent;
import br.com.usasistemas.usaplatform.model.entity.CalendarEventHistory;
import br.com.usasistemas.usaplatform.model.entity.City;
import br.com.usasistemas.usaplatform.model.entity.DeliveryRequest;
import br.com.usasistemas.usaplatform.model.entity.DeliveryRequestHistory;
import br.com.usasistemas.usaplatform.model.entity.DocumentsFile;
import br.com.usasistemas.usaplatform.model.entity.DocumentsFolder;
import br.com.usasistemas.usaplatform.model.entity.DocumentsFranchiseeIndex;
import br.com.usasistemas.usaplatform.model.entity.Franchisee;
import br.com.usasistemas.usaplatform.model.entity.FranchiseeUser;
import br.com.usasistemas.usaplatform.model.entity.Franchisor;
import br.com.usasistemas.usaplatform.model.entity.FranchisorUser;
import br.com.usasistemas.usaplatform.model.entity.LetsEncryptChallenge;
import br.com.usasistemas.usaplatform.model.entity.ManufactureRequest;
import br.com.usasistemas.usaplatform.model.entity.ManufactureRequestHistory;
import br.com.usasistemas.usaplatform.model.entity.MessageAttachment;
import br.com.usasistemas.usaplatform.model.entity.MessageComment;
import br.com.usasistemas.usaplatform.model.entity.MessageTopic;
import br.com.usasistemas.usaplatform.model.entity.MessageTopicLabel;
import br.com.usasistemas.usaplatform.model.entity.Notification;
import br.com.usasistemas.usaplatform.model.entity.PasswordReset;
import br.com.usasistemas.usaplatform.model.entity.Product;
import br.com.usasistemas.usaplatform.model.entity.ProductCategory;
import br.com.usasistemas.usaplatform.model.entity.ProductSize;
import br.com.usasistemas.usaplatform.model.entity.ProductSizePriceHistory;
import br.com.usasistemas.usaplatform.model.entity.ReviewRequest;
import br.com.usasistemas.usaplatform.model.entity.State;
import br.com.usasistemas.usaplatform.model.entity.StockConsolidation;
import br.com.usasistemas.usaplatform.model.entity.Supplier;
import br.com.usasistemas.usaplatform.model.entity.SupplierCategory;
import br.com.usasistemas.usaplatform.model.entity.SupplierFranchisor;
import br.com.usasistemas.usaplatform.model.entity.SupplierUser;
import br.com.usasistemas.usaplatform.model.entity.SystemConfiguration;
import br.com.usasistemas.usaplatform.model.entity.TimeRangeReport;
import br.com.usasistemas.usaplatform.model.entity.Training;
import br.com.usasistemas.usaplatform.model.entity.TrainingViewControl;
import br.com.usasistemas.usaplatform.model.entity.Tutorial;
import br.com.usasistemas.usaplatform.model.entity.UploadedFile;
import br.com.usasistemas.usaplatform.model.entity.User;
import br.com.usasistemas.usaplatform.model.entity.UserGroup;
import br.com.usasistemas.usaplatform.model.entity.UserGroupEntityUser;

public class ObjectifyRegistry {
   
    public static void initialize() {
        
        ObjectifyService.init();

        ObjectifyService.register(AdministratorUser.class);
		ObjectifyService.register(Announcement.class);
		ObjectifyService.register(CalendarEvent.class);
		ObjectifyService.register(CalendarEventHistory.class);
		ObjectifyService.register(City.class);
		ObjectifyService.register(DeliveryRequest.class);
		ObjectifyService.register(DeliveryRequestHistory.class);
		ObjectifyService.register(DocumentsFile.class);
		ObjectifyService.register(DocumentsFolder.class);
		ObjectifyService.register(DocumentsFranchiseeIndex.class);
		ObjectifyService.register(Franchisee.class);
		ObjectifyService.register(FranchiseeUser.class);
		ObjectifyService.register(Franchisor.class);
		ObjectifyService.register(FranchisorUser.class);
		ObjectifyService.register(LetsEncryptChallenge.class);
		ObjectifyService.register(ManufactureRequest.class);
		ObjectifyService.register(ManufactureRequestHistory.class);
		ObjectifyService.register(MessageAttachment.class);
		ObjectifyService.register(MessageComment.class);
		ObjectifyService.register(MessageTopic.class);
		ObjectifyService.register(MessageTopicLabel.class);
		ObjectifyService.register(Notification.class);
		ObjectifyService.register(PasswordReset.class);
		ObjectifyService.register(Product.class);
		ObjectifyService.register(ProductCategory.class);
		ObjectifyService.register(ProductSize.class);
		ObjectifyService.register(ProductSizePriceHistory.class);
		ObjectifyService.register(ReviewRequest.class);
		ObjectifyService.register(State.class);
		ObjectifyService.register(StockConsolidation.class);
		ObjectifyService.register(Supplier.class);
		ObjectifyService.register(SupplierCategory.class);
		ObjectifyService.register(SupplierFranchisor.class);
		ObjectifyService.register(SupplierUser.class);
		ObjectifyService.register(SystemConfiguration.class);
		ObjectifyService.register(TimeRangeReport.class);
		ObjectifyService.register(Training.class);
		ObjectifyService.register(TrainingViewControl.class);
		ObjectifyService.register(Tutorial.class);
		ObjectifyService.register(UploadedFile.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(UserGroup.class);
		ObjectifyService.register(UserGroupEntityUser.class);
	}
}