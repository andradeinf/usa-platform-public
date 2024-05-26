package br.com.usasistemas.usaplatform.api;

import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.WSTimeRangeReportDataRequest;
import br.com.usasistemas.usaplatform.api.response.WSTimeRangeReportListResponse;
import br.com.usasistemas.usaplatform.api.response.WSTimeRangeReportResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ReportManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.TimeRangeReportTypeEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.REPORT_SERVICE)
public class ReportService {
	
	private static final Logger log = Logger.getLogger(ReportService.class.getName());
	private ReportManagementBO reportManagement;
	
	public ReportManagementBO getReportManagement() {
		return reportManagement;
	}
	
	public void setReportManagement(ReportManagementBO reportManagement) {
		this.reportManagement = reportManagement;
	}

	@RequestMapping(value="/getTimeRangeReport/{id}", method=RequestMethod.GET)
	public void getTimeRangeReport(@PathVariable Long id, HttpServletResponse response) {
		reportManagement.getTimeRangeReport(id, response);
	}

	@RequestMapping(value = "/timeRangeReport/{reportType}", method=RequestMethod.GET)
	@ResponseBody
	public WSTimeRangeReportListResponse listTimeRangeReport(@PathVariable String reportType, HttpSession session) {
		
		WSTimeRangeReportListResponse response = new WSTimeRangeReportListResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			response.setTimeRangeReports(reportManagement.getTimeRangeReports(TimeRangeReportTypeEnum.valueOf(reportType.toUpperCase()), user.getSelectedRole(), user.getSelectedEntityId()));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Time Range Reports: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/timeRangeReport/{reportType}", method=RequestMethod.POST)
	@ResponseBody
	public WSTimeRangeReportResponse createTimeRangeRequest(@PathVariable String reportType, @RequestBody WSTimeRangeReportDataRequest timeRangeReportDataRequest, HttpSession session) {
		
		WSTimeRangeReportResponse response = new WSTimeRangeReportResponse();
		
		try {
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			//Start date should be at 00:00:00
			String initDateWithoutTimeStr = DateUtil.getDate(timeRangeReportDataRequest.getInitDate(), DateUtil.SLASH_PATTERN);
			Date initDate = DateUtil.getDate(initDateWithoutTimeStr, DateUtil.SLASH_PATTERN);
			log.fine("initDate: " + DateUtil.getDate(initDate));
			
			//End date should be at 23:59:59
			String endDateWithoutTimeStr = DateUtil.getDate(timeRangeReportDataRequest.getEndDate(), DateUtil.SLASH_PATTERN);
			Date endDate = DateUtil.getDate(endDateWithoutTimeStr + " 23:59:59", DateUtil.SLASH_FULL_PATTERN);
			log.fine("endDate: " + DateUtil.getDate(endDate));

			//Report Type
			TimeRangeReportTypeEnum reportTypeEnum = TimeRangeReportTypeEnum.valueOf(reportType.toUpperCase());

			if (endDate.before(initDate)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.INVALID_TIME_RANGE.code());
				rm.setMessage(ResponseCodesEnum.INVALID_TIME_RANGE.message());
				rm.setDetails("A data final deve ser maior que a data inicial");
				response.setReturnMessage(rm);
				log.warning("End date before start date");
		
			} else if (
					(
						(reportTypeEnum.equals(TimeRangeReportTypeEnum.DELIVERY_REQUEST) && DateUtil.getDaysBetween(initDate, endDate) > 31) ||
						(reportTypeEnum.equals(TimeRangeReportTypeEnum.MESSAGE) && DateUtil.getDaysBetween(initDate, endDate) > 92)
					)					
					&& !user.getSelectedRole().equals(UserProfileEnum.ADMINISTRATOR)) {
				log.fine("Days difference: " + DateUtil.getDaysBetween(initDate, endDate));
				
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.INVALID_TIME_RANGE.code());
				rm.setMessage(ResponseCodesEnum.INVALID_TIME_RANGE.message());
				if (reportTypeEnum.equals(TimeRangeReportTypeEnum.DELIVERY_REQUEST)) {}
				rm.setDetails("O período informado é superior ao período máximo permitido!");
				response.setReturnMessage(rm);
				log.warning("Time range bigger than expected");
		
			} else {
			
				response.setTimeRangeReport(reportManagement.createTimeRangeReport(reportTypeEnum, user.getSelectedRole(), user.getSelectedEntityId(), user.getSelectedEntityUserId(), initDate, endDate, timeRangeReportDataRequest.getFilterSupplierId(), timeRangeReportDataRequest.getFilterFranchisorId(), timeRangeReportDataRequest.getFilterFranchiseeId()));
			
			}
		} catch (BusinessException be) {
			response.setReturnMessage(be.getReturnMessage());
			log.warning("Error creating Time Range Report: " + be.toString());
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Time Range Report: " + e.toString());
		}
		
		return response;
	}

}
