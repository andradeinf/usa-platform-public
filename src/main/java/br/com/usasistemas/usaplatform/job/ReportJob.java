package br.com.usasistemas.usaplatform.job;

import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ReportManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

@Controller
@RequestMapping(value = UrlMapping.REPORT_JOB)
public class ReportJob {
	
	private static final Logger log = Logger.getLogger(ReportJob.class.getName());
	private ReportManagementBO reportManagement;
	
	private static final Long CLEANUP_THRESHOLD = 32L;
	
	public ReportManagementBO getReportManagement() {
		return reportManagement;
	}
	
	public void setReportManagement(ReportManagementBO reportManagement) {
		this.reportManagement = reportManagement;
	}

	@RequestMapping(value="/generateTimeRangeReport/{id}", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse generateTimeRangeReport(@PathVariable Long id) {
		GenericResponse response = new GenericResponse();
	    
		try {
	    	reportManagement.generateTimeRangeReport(id);	    	
	    } catch (BusinessException be) {
	      response.setReturnMessage(be.getReturnMessage());
	      log.warning("Error generating report: " + be.toString());
	    } catch (Exception e) {
	      ReturnMessage rm = new ReturnMessage();
	      rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
	      rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
	      rm.setDetails(e.toString());
	      response.setReturnMessage(rm);
	      log.warning("Error generating report: " + e.toString());
	    }
	
	    return response;
	}
	
	@RequestMapping(value = "/cleanUpReports", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse cleanUpReports() {
		
		GenericResponse response = new GenericResponse();
		
		log.info("Triggering reports clean up...");
		
		try {			
			reportManagement.cleanUpTimeRangeReports(CLEANUP_THRESHOLD);
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error when reports clean up: " + e.toString());
		}		
				
		return response;
	}

}
