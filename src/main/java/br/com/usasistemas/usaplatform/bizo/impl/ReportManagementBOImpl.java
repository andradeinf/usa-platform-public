package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import br.com.usasistemas.usaplatform.bizo.DeliveryManagementBO;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.MessageManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.ReportManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.dao.TimeRangeReportDAO;
import br.com.usasistemas.usaplatform.model.data.TimeRangeReportData;
import br.com.usasistemas.usaplatform.model.enums.OperatorTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.ReportStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.TimeRangeReportTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;
import br.com.usasistemas.usaplatform.model.report.DeliveryRequestTimeRangeReportSheet;
import br.com.usasistemas.usaplatform.model.report.MessageTimeRangeReportSheet;
import br.com.usasistemas.usaplatform.model.report.ReportSheet;

public class ReportManagementBOImpl implements ReportManagementBO {
	
	private static final Logger log = Logger.getLogger(ReportManagementBOImpl.class.getName());
	private TimeRangeReportDAO timeRangeReportDAO;
	private FileManagementBO fileManagement;
	private DeliveryManagementBO deliveryManagement;
	private ProductManagementBO productManagement;
	private FranchiseeManagementBO franchiseeManagement;
	private FranchisorManagementBO franchisorManagement;
	private SupplierManagementBO supplierManagement;
	private MessageManagementBO messageManagement;
	private UserManagementBO userManagement;

	public TimeRangeReportDAO getTimeRangeReportDAO() {
		return this.timeRangeReportDAO;
	}

	public void setTimeRangeReportDAO(TimeRangeReportDAO timeRangeReportDAO) {
		this.timeRangeReportDAO = timeRangeReportDAO;
	}
	
	public FileManagementBO getFileManagement() {
		return fileManagement;
	}

	public void setFileManagement(FileManagementBO fileManagement) {
		this.fileManagement = fileManagement;
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

	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManagement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}

	public SupplierManagementBO getSupplierManagement() {
		return supplierManagement;
	}

	public void setSupplierManagement(SupplierManagementBO supplierManagement) {
		this.supplierManagement = supplierManagement;
	}

	public MessageManagementBO getMessageManagement() {
		return this.messageManagement;
	}

	public void setMessageManagement(MessageManagementBO messageManagement) {
		this.messageManagement = messageManagement;
	}

	public UserManagementBO getUserManagement() {
		return this.userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}	

	@Override
	public List<TimeRangeReportData> getTimeRangeReports(TimeRangeReportTypeEnum type, UserProfileEnum entityProfile, Long entityId) {
		return timeRangeReportDAO.findByTypeAndEntityProfileAndEntityId(type, entityProfile, entityId);
	}

	@Override
	public void getTimeRangeReport(Long id, HttpServletResponse response) {
		
		TimeRangeReportData timeRangeReport = timeRangeReportDAO.findById(id);
		
		fileManagement.readFile(timeRangeReport.getFileKey(), timeRangeReport.getFileName(), true, response);
	}

	@Override
	public TimeRangeReportData createTimeRangeReport(TimeRangeReportTypeEnum type, UserProfileEnum entityProfile, Long entityId, Long entityUserId, Date initialDate, Date finalDate, Long filterSupplierId, Long filterFranchisorId, Long filterFranchiseeId) {
		
		TimeRangeReportData timeRangeReport = new TimeRangeReportData();
		timeRangeReport.setDate(new Date());
		timeRangeReport.setType(type);
		timeRangeReport.setEntityProfile(entityProfile);
		timeRangeReport.setEntityId(entityId);
		timeRangeReport.setEntityUserId(entityUserId);
		timeRangeReport.setInitDate(initialDate);
		timeRangeReport.setEndDate(finalDate);
		if (filterSupplierId != null) timeRangeReport.setFilterSupplierId(filterSupplierId);
		if (filterFranchisorId != null) timeRangeReport.setFilterFranchisorId(filterFranchisorId);
		if (filterFranchiseeId != null) timeRangeReport.setFilterFranchiseeId(filterFranchiseeId);
		timeRangeReport.setStatus(ReportStatusEnum.WAITING);
		timeRangeReport = timeRangeReportDAO.save(timeRangeReport);
		
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/job/report/generateTimeRangeReport/"+timeRangeReport.getId()));
		
		return timeRangeReport;
	} 

	@Override
	public TimeRangeReportData generateTimeRangeReport(Long id) {
		
		TimeRangeReportData timeRangeReport = timeRangeReportDAO.findById(id);
		
		try {
		
			timeRangeReport.setStatus(ReportStatusEnum.PROCESSING);
			timeRangeReport = timeRangeReportDAO.save(timeRangeReport);
			
			ReportSheet reportSheet = null;
			
			if (timeRangeReport.getType().equals(TimeRangeReportTypeEnum.DELIVERY_REQUEST)) {
				reportSheet = generateDeliveryRequestTimeRangeReport(timeRangeReport);
			} else if (timeRangeReport.getType().equals(TimeRangeReportTypeEnum.MESSAGE)) {
				reportSheet = generateMessageTimeRangeReport(timeRangeReport);
			} else {
				log.warning("timeRangeReport Type not expected: " + timeRangeReport.getType());
			}

			if (reportSheet != null) {
				timeRangeReport.setStatus(ReportStatusEnum.COMPLETED);
				timeRangeReport.setFileKey(reportSheet.fileKey);
				timeRangeReport.setFileName(reportSheet.fileName);
				timeRangeReport = timeRangeReportDAO.save(timeRangeReport);
			} else {
				timeRangeReport.setStatus(ReportStatusEnum.ERROR);
				timeRangeReport = timeRangeReportDAO.save(timeRangeReport);
			}
			
		} catch (Exception ex) {
			
			timeRangeReport.setStatus(ReportStatusEnum.ERROR);
			timeRangeReport = timeRangeReportDAO.save(timeRangeReport);
			ex.printStackTrace();
		}
		
    	return timeRangeReport;
	}

	private ReportSheet generateMessageTimeRangeReport(TimeRangeReportData timeRangeReport) {
		
		MessageTimeRangeReportSheet messageTimeRangeReportSheet = new MessageTimeRangeReportSheet(timeRangeReport, fileManagement, franchiseeManagement, userManagement);

		Map<String,String> queryParams = new HashMap<String,String>();
		queryParams.put("label", "99");
		queryParams.put("type", "FROM");
		queryParams.put("fromEntityProfile", "FRANCHISEE");
		if (timeRangeReport.getFilterFranchiseeId() != null && timeRangeReport.getFilterFranchiseeId() != 0L) {
			queryParams.put("fromEntityId", timeRangeReport.getFilterFranchiseeId().toString());
		}

		messageTimeRangeReportSheet.writeData(
			messageManagement
				.getUserMessageTopics(timeRangeReport.getEntityId(), timeRangeReport.getEntityUserId(), 0L, timeRangeReport.getInitDate(), timeRangeReport.getEndDate(), null, null, queryParams)
				.getMessageTopics()
		);

		messageTimeRangeReportSheet.saveAndClose();

		return messageTimeRangeReportSheet;
	}

	@Override
	public void cleanUpTimeRangeReports(Long threshold) {
		
		Calendar thresholdDate = Calendar.getInstance();
		thresholdDate.add(Calendar.DAY_OF_MONTH, threshold.intValue() * -1);
		
		List<TimeRangeReportData> reports = timeRangeReportDAO.findByDate(thresholdDate.getTime(), OperatorTypeEnum.LOWER_OR_EQUAL_THAN);
		
		if (reports != null && !reports.isEmpty()) {
			for (TimeRangeReportData report : reports) {
				if (report.getFileKey() != null) {
					fileManagement.deleteFile(report.getFileKey());
				}
				timeRangeReportDAO.delete(report.getId());
			}
		}		
	}

	private ReportSheet generateDeliveryRequestTimeRangeReport(TimeRangeReportData timeRangeReport) {

		ReportSheet reportSheet = null;
		
		if (timeRangeReport.getEntityProfile().equals(UserProfileEnum.FRANCHISOR)) {
			reportSheet = generateFranchisorDeliveryRequestTimeRangeReport(timeRangeReport);
		} else if (timeRangeReport.getEntityProfile().equals(UserProfileEnum.FRANCHISEE)) {
			reportSheet = generateFranchiseeDeliveryRequestTimeRangeReport(timeRangeReport);
		} else if (timeRangeReport.getEntityProfile().equals(UserProfileEnum.SUPPLIER)) {
			reportSheet = generateSupplierDeliveryRequestTimeRangeReport(timeRangeReport);
		} else if (timeRangeReport.getEntityProfile().equals(UserProfileEnum.ADMINISTRATOR)) {
			reportSheet = generateAdministratorDeliveryRequestTimeRangeReport(timeRangeReport);
		} else {
			log.warning("timeRangeReport EntityProfile not expected: " + timeRangeReport.getEntityProfile());
		}

		return reportSheet;
	}

	//when the requester is a franchisor
	private ReportSheet generateFranchisorDeliveryRequestTimeRangeReport(TimeRangeReportData timeRangeReport) {

		DeliveryRequestTimeRangeReportSheet deliveryRequestTimeRangeReportSheet = new DeliveryRequestTimeRangeReportSheet(timeRangeReport, fileManagement, productManagement, supplierManagement, franchiseeManagement, franchisorManagement);

		if (timeRangeReport.getFilterFranchiseeId() == 0) {
			// retrieve data for all franchisees for the requester franchisor and the given supplier
			franchiseeManagement
				.getFranchiseesByFranchisorId(timeRangeReport.getEntityId())
				.forEach(franchisee -> 
					deliveryRequestTimeRangeReportSheet.writeData(this.deliveryManagement.getDeliveryRequestsByFilter(timeRangeReport.getInitDate(), timeRangeReport.getEndDate(), franchisee.getId(), timeRangeReport.getFilterSupplierId()))
				);
		} else {
			// retrieve data for the given franchisee and the given supplier
			deliveryRequestTimeRangeReportSheet.writeData(this.deliveryManagement.getDeliveryRequestsByFilter(timeRangeReport.getInitDate(), timeRangeReport.getEndDate(), timeRangeReport.getFilterFranchiseeId(), timeRangeReport.getFilterSupplierId()));
		}

		deliveryRequestTimeRangeReportSheet.saveAndClose();

		return deliveryRequestTimeRangeReportSheet;
	}

	//when the requester is a franchisee
	private ReportSheet generateFranchiseeDeliveryRequestTimeRangeReport(TimeRangeReportData timeRangeReport) {

		DeliveryRequestTimeRangeReportSheet deliveryRequestTimeRangeReportSheet = new DeliveryRequestTimeRangeReportSheet(timeRangeReport, fileManagement, productManagement, supplierManagement, franchiseeManagement, franchisorManagement);

		//retrieve data for the requester franchisee and the given supplier
		deliveryRequestTimeRangeReportSheet.writeData(this.deliveryManagement.getDeliveryRequestsByFilter(timeRangeReport.getInitDate(), timeRangeReport.getEndDate(), timeRangeReport.getEntityId(), timeRangeReport.getFilterSupplierId()));

		deliveryRequestTimeRangeReportSheet.saveAndClose();

		return deliveryRequestTimeRangeReportSheet;
	}

	//when the requester is a supplier
	private ReportSheet generateSupplierDeliveryRequestTimeRangeReport(TimeRangeReportData timeRangeReport) {

		DeliveryRequestTimeRangeReportSheet deliveryRequestTimeRangeReportSheet = new DeliveryRequestTimeRangeReportSheet(timeRangeReport, fileManagement, productManagement, supplierManagement, franchiseeManagement, franchisorManagement);

		if (timeRangeReport.getFilterFranchisorId() != 0) {
			// retrieve data for all franchisees for the selected franchisor and the requester supplier
			franchiseeManagement
				.getFranchiseesByFranchisorId(timeRangeReport.getFilterFranchisorId())
				.forEach(franchisee -> 
					deliveryRequestTimeRangeReportSheet.writeData(this.deliveryManagement.getDeliveryRequestsByFilter(timeRangeReport.getInitDate(), timeRangeReport.getEndDate(), franchisee.getId(), timeRangeReport.getEntityId()))
				);
		} else {
			// retrieve all data from requester supplier
			deliveryRequestTimeRangeReportSheet.writeData(this.deliveryManagement.getDeliveryRequestsByFilter(timeRangeReport.getInitDate(), timeRangeReport.getEndDate(), 0L, timeRangeReport.getEntityId()));
		}
		deliveryRequestTimeRangeReportSheet.saveAndClose();

		return deliveryRequestTimeRangeReportSheet;
	}

	//when the requester is a administrator
	private ReportSheet generateAdministratorDeliveryRequestTimeRangeReport(TimeRangeReportData timeRangeReport) {

		DeliveryRequestTimeRangeReportSheet deliveryRequestTimeRangeReportSheet = new DeliveryRequestTimeRangeReportSheet(timeRangeReport, fileManagement, productManagement, supplierManagement, franchiseeManagement, franchisorManagement);

		if (timeRangeReport.getFilterFranchisorId() != 0) {
			// retrieve data for all franchisees for the selected franchisor and the given supplier
			franchiseeManagement
				.getFranchiseesByFranchisorId(timeRangeReport.getFilterFranchisorId())
				.forEach(franchisee -> 
					deliveryRequestTimeRangeReportSheet.writeData(this.deliveryManagement.getDeliveryRequestsByFilter(timeRangeReport.getInitDate(), timeRangeReport.getEndDate(), franchisee.getId(), timeRangeReport.getFilterSupplierId()))
				);
		} else {
			// retrieve all data from the given supplier
			deliveryRequestTimeRangeReportSheet.writeData(this.deliveryManagement.getDeliveryRequestsByFilter(timeRangeReport.getInitDate(), timeRangeReport.getEndDate(), 0L, timeRangeReport.getFilterSupplierId()));
		}
		deliveryRequestTimeRangeReportSheet.saveAndClose();

		return deliveryRequestTimeRangeReportSheet;
	}

}
