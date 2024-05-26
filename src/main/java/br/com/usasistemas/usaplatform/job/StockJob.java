package br.com.usasistemas.usaplatform.job;

import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.bizo.StockManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;

@Controller
@RequestMapping(value = UrlMapping.STOCK_JOB)
public class StockJob {
	
	private static final Logger log = Logger.getLogger(StockJob.class.getName());
	private StockManagementBO stockManagement;
	
	public StockManagementBO getStockManagement() {
		return stockManagement;
	}

	public void setStockManagement(StockManagementBO stockManagement) {
		this.stockManagement = stockManagement;
	}
	
	@RequestMapping(value = "/updateProductStock", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse updateProductStock() {
		
		log.info("Running stock consolidation...");
		stockManagement.consolidateStock();
		
		return new GenericResponse();
	}
	
	@RequestMapping(value = "/movingBackFromHistory", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse movingBackFromHistory() {
		
		log.info("Running stock consolidation...");
		stockManagement.movingBackFromHistory();
		
		return new GenericResponse();
	}

}
