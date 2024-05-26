package br.com.usasistemas.usaplatform.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.bizo.DocumentManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.MessageManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;

@Controller
@RequestMapping(value = UrlMapping.INDEXING_JOB)
public class IndexingJob {
	
	private static final Logger log = Logger.getLogger(IndexingJob.class.getName());
	private MessageManagementBO messageManagement;
	private DocumentManagementBO documentManagement;
	private FranchisorManagementBO franchisorManagement;

	public MessageManagementBO getMessageManagement() {
		return messageManagement;
	}

	public void setMessageManagement(MessageManagementBO messageManagement) {
		this.messageManagement = messageManagement;
	}

	public DocumentManagementBO getDocumentManagement() {
		return this.documentManagement;
	}

	public void setDocumentManagement(DocumentManagementBO documentManagement) {
		this.documentManagement = documentManagement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return this.franchisorManagement;
	}
	
	public void setFranchisorManagement(FranchisorManagementBO franchisorManagement) {
		this.franchisorManagement = franchisorManagement;
	}

	@RequestMapping(value = "/updateDocumentsFranchiseeIndex/{franchisorId}", method=RequestMethod.POST)
	@ResponseBody
	public GenericResponse updateDocumentsFranchiseeIndex(@PathVariable Long franchisorId, @RequestParam("requestDate") String requestDate) {
		
		log.info("Running updateDocumentsFranchiseeIndex for franchisorId " + franchisorId + "...");
		
		Date parsedRequestDate;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			parsedRequestDate = formatter.parse(requestDate);
		} catch (Exception e) {
			parsedRequestDate = new Date();
			log.severe("Error parsing request date: " + requestDate);
		}		

		documentManagement.updateDocumentsFranchiseeIndex(franchisorId, parsedRequestDate);
		
		return new GenericResponse();
	}

	@RequestMapping(value = "/updateAllDocumentsFranchiseeIndex", method=RequestMethod.GET)
	@ResponseBody
	public GenericResponse updateAllDocumentsFranchiseeIndex() {

		log.info("Running updateAllDocumentsFranchiseeIndex...");
		Date currentDate = new Date();

		franchisorManagement.getAllFranchisors().stream().forEach(franchisor -> {
			log.info("Running updateDocumentsFranchiseeIndex for franchisorId " + franchisor.getId() + "...");
			documentManagement.updateDocumentsFranchiseeIndex(franchisor.getId(), currentDate);
		});
		
		return new GenericResponse();
	}
	
}
