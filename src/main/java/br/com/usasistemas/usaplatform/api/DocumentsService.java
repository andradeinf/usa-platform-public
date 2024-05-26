package br.com.usasistemas.usaplatform.api;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.request.DocumentsFilesSaveRequest;
import br.com.usasistemas.usaplatform.api.request.data.MoveFolderOrFileData;
import br.com.usasistemas.usaplatform.api.response.DocumentsUrlResponse;
import br.com.usasistemas.usaplatform.api.response.UrlResponse;
import br.com.usasistemas.usaplatform.api.response.WSDocumentsFileListResponse;
import br.com.usasistemas.usaplatform.api.response.WSDocumentsFileResponse;
import br.com.usasistemas.usaplatform.api.response.WSDocumentsFolderResponse;
import br.com.usasistemas.usaplatform.api.response.WSDocumentsListResponse;
import br.com.usasistemas.usaplatform.api.response.WSDocumentsMoveResponse;
import br.com.usasistemas.usaplatform.api.response.WSDocumentsTreeResponse;
import br.com.usasistemas.usaplatform.api.response.WSUploadedFileResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.DocumentManagementBO;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.DOCUMENTS_SERVICE)
public class DocumentsService {
	
	private static final Logger log = Logger.getLogger(DocumentsService.class.getName());
	private FileManagementBO fileManagement;
	private DocumentManagementBO documentManagement;
	private FranchiseeManagementBO franchiseeManagement;
	private ProductManagementBO productManagement;
	
	public FileManagementBO getFileManagement() {
		return fileManagement;
	}

	public void setFileManagement(FileManagementBO fileManagement) {
		this.fileManagement = fileManagement;
	}
	
	public DocumentManagementBO getDocumentManagement() {
		return documentManagement;
	}

	public void setDocumentManagement(DocumentManagementBO documentManagement) {
		this.documentManagement = documentManagement;
	}

	public FranchiseeManagementBO getFranchiseeManagement() {
		return franchiseeManagement;
	}

	public void setFranchiseeManagement(FranchiseeManagementBO franchiseeManagement) {
		this.franchiseeManagement = franchiseeManagement;
	}
	
	public ProductManagementBO getProductManagement() {
		return productManagement;
	}

	public void setProductManagement(ProductManagementBO productManagement) {
		this.productManagement = productManagement;
	}
	
	/*
	 * Get URL to upload file
	 */
	@Deprecated //Remove after version upgrade. Kept only for JS compatibility
	@RequestMapping(value = "/url", method=RequestMethod.GET)
	@ResponseBody
	public DocumentsUrlResponse getDocumentUrl(HttpSession session) {
		
		DocumentsUrlResponse response = new DocumentsUrlResponse();
		Boolean noFileRestriction = false;
		
		UserProfileData user = SessionUtil.getLoggedUser(session);
		if (user.getAdministrator() != null) {
			log.info("User uploading file is also an admin, so no size restriction applied");
			noFileRestriction = true;
		}
		
		response.setUrl(fileManagement.getDocumentsFileUploadUrl(UrlMapping.DOCUMENTS_SERVICE + "/upload", noFileRestriction));
		return response;	
	}

	/*
	 * Get URL to upload file
	 */
	@RequestMapping(value = "/uploadUrl", method=RequestMethod.GET)
	@ResponseBody
	public DocumentsUrlResponse getDocumentUploadUrl(HttpSession session) {
		
		DocumentsUrlResponse response = new DocumentsUrlResponse();
		Boolean noFileRestriction = false;
		
		UserProfileData user = SessionUtil.getLoggedUser(session);
		if (user.getAdministrator() != null) {
			log.info("User uploading file is also an admin, so no size restriction applied");
			noFileRestriction = true;
		}
		
		response.setUrl(fileManagement.getDocumentsFileUploadUrl(UrlMapping.DOCUMENTS_SERVICE + "/uploadFile", noFileRestriction));
		return response;	
	}
	
	/*
	 * Get URL to upload catalog
	 */
	@RequestMapping(value = "/catalog/url", method=RequestMethod.GET)
	@ResponseBody
	public DocumentsUrlResponse getCatalogUrl() {
		DocumentsUrlResponse response = new DocumentsUrlResponse();
		response.setUrl(fileManagement.getCatalogUploadUrl(UrlMapping.DOCUMENTS_SERVICE + "/catalog/upload"));
		return response;	
	}

	/*
	 * Process uploaded file
	 */
	@Deprecated //Remove after version upgrade. Kept only for JS compatibility
	@RequestMapping(value = "/upload", method=RequestMethod.POST)
	@ResponseBody
	public WSDocumentsFileResponse processUploadedFile(HttpServletRequest request, HttpSession session) {
		WSDocumentsFileResponse response = new WSDocumentsFileResponse();

		try {

			UserProfileData user = SessionUtil.getLoggedUser(session);
			
			UploadedFileData file = fileManagement.getUploadedFileInfo(request, "uploadedFile");
			if (file != null) {			
				Long folderId = Long.valueOf(request.getParameter("folderId"));
				Boolean accessRestricted = Boolean.valueOf(request.getParameter("accessRestricted"));
				List<Long> franchiseeIds = new ArrayList<Long>();
				if (!accessRestricted) {
					String[] franchiseeIdsStr = request.getParameterValues("franchiseeId");			
					if (franchiseeIdsStr != null && franchiseeIdsStr.length > 0) {
						for (String franchiseeId : franchiseeIdsStr) {
							franchiseeIds.add(Long.valueOf(franchiseeId));
						}
					}
				}

				List<UploadedFileData> files = new ArrayList<UploadedFileData>();
				files.add(file);
				List<DocumentsFileData> createdFiles = documentManagement.createDocumentsFiles(files, folderId, user.getFranchisor().getFranchisorId(), accessRestricted, franchiseeIds);
				
				response.setDocumentsFile(createdFiles.get(0));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.FAIL_TO_UPLOAD_DOCUMENT.code());
			rm.setMessage(ResponseCodesEnum.FAIL_TO_UPLOAD_DOCUMENT.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error uploading document file: " + e.toString());
		}
		
		return response;	
	}

	/*
	 * Process uploaded file
	 */
	@RequestMapping(value = "/uploadFile", method=RequestMethod.POST)
	@ResponseBody
	public WSUploadedFileResponse processUploadedFile(HttpServletRequest request) {
		WSUploadedFileResponse response = new WSUploadedFileResponse();
		
		try {
			UploadedFileData file = fileManagement.getUploadedFileInfo(request, "uploadedFile");	
			response.setUploadedFile(fileManagement.createUploadedFile(file));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.FAIL_TO_UPLOAD_DOCUMENT.code());
			rm.setMessage(ResponseCodesEnum.FAIL_TO_UPLOAD_DOCUMENT.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error uploading document file: " + e.toString());
		}
		
		return response;	
	}
	
	/*
	 * Process uploaded file
	 */
	@RequestMapping(value = "/catalog/upload", method=RequestMethod.POST)
	@ResponseBody
	public UrlResponse processUploadedCatalog(HttpServletRequest request) {
		UrlResponse response = new UrlResponse();
		
		try {

			UploadedFileData file = fileManagement.getUploadedFileInfo(request, "uploadedFile");		
			
			if (file != null) {
				Long productId = Long.valueOf(request.getParameter("productId"));
				ProductData product = productManagement.addCatalog(productId, file);
				response.setUrl(product.getCatalogName());
			}
			
		} catch (Exception e) {			
			log.warning("Error uploading catalog file: " + e.toString());
		}
		
		return response;	
	}
	
	@RequestMapping(value="/file/{id}", method=RequestMethod.GET)
	public void getFile(@PathVariable Long id, HttpServletResponse response) {
		DocumentsFileData file = documentManagement.getDocumentsFile(id);
		if (file != null) {
			fileManagement.readFile(file.getFileKey(), file.getName(), true, response);
		} else {
			try {
				response.sendRedirect(UrlMapping.HOME + "#/error/fileNotFound");
			} catch (Exception e) {
				log.warning("Unable to log error for file download");		
			}			
		}
	}
	
	@RequestMapping(value="/catalog/{id}", method=RequestMethod.GET)
	public void getCatalog(@PathVariable Long id, HttpServletResponse response) {
		ProductData product = productManagement.getProduct(id);
		fileManagement.readFile(product.getCatalogKey(), product.getCatalogName(), false, response);
	}

	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public WSDocumentsListResponse getDocuments(HttpSession session) {
		
		WSDocumentsListResponse response = new WSDocumentsListResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (user == null || (
					!user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR) &&
					!user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE))) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access Documents");
			
			} else {
				
				Long franchisorId = null;
				Long entityId = null;
				if (user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR)) {
					franchisorId = user.getFranchisor().getFranchisorId();
					entityId = user.getFranchisor().getFranchisorId();
				} else if (user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE)) {
					franchisorId = franchiseeManagement.getFranchisee(user.getFranchisee().getFranchiseeId()).getFranchisorId();
					entityId = user.getFranchisee().getFranchiseeId();
				}
				
				response.setFiles(documentManagement.getFranchisorDocumentsFiles(franchisorId, entityId).stream().filter(file -> !file.getFolderId().equals(0L)).collect(Collectors.toList()));
				response.setFolders(documentManagement.getFranchisorDocumentsFolders(franchisorId, entityId, response.getFiles()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Documents: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/folder/{folderId}", method=RequestMethod.GET)
	@ResponseBody
	public WSDocumentsListResponse getDocumentsByFolderId(@PathVariable Long folderId, HttpSession session) {
		
		WSDocumentsListResponse response = new WSDocumentsListResponse();
	
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (user == null || !user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access Documents");
			
			} else if (!user.getFeatureFlags().getFlagDocuments()) {

				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.GENERIC_SUCCESS.code());
				rm.setMessage(ResponseCodesEnum.GENERIC_SUCCESS.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access documents!");
				
			} else {

				Long franchisorId = user.getFranchisor().getFranchisorId();				
				response.setFolders(documentManagement.getFranchisorDocumentsFoldersByParentId(franchisorId, folderId));
				response.setFiles(documentManagement.getFranchisorDocumentsFilesByFolderId(franchisorId, folderId));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Documents: " + e.toString());
		}

		return response;
	}

	@RequestMapping(value = "/tree", method=RequestMethod.GET)
	@ResponseBody
	public WSDocumentsTreeResponse getDocumentsTree(HttpSession session) {
		
		WSDocumentsTreeResponse response = new WSDocumentsTreeResponse();
	
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (user == null || !user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE)) {

				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access Documents");

			} else if (!user.getFeatureFlags().getFlagDocuments()) {

				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.GENERIC_SUCCESS.code());
				rm.setMessage(ResponseCodesEnum.GENERIC_SUCCESS.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access documents!");

			} else {
				response.setDocumentTree(documentManagement.getFranchisorDocumentsByFranchiseeId(user.getFranchisee().getFranchiseeId()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Documents: " + e.toString());
		}

		return response;
	}

	@RequestMapping(value = "/folder", method=RequestMethod.POST)
	@ResponseBody
	public WSDocumentsFolderResponse createDocumentsFolder(@RequestBody DocumentsFolderData folder, HttpSession session) {
		
		WSDocumentsFolderResponse response = new WSDocumentsFolderResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (!user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to create Document Folder");
			
			} else {
				
				if (folder.getId() == 0) {
					folder.setId(null);
					folder.setFranchisorId(user.getFranchisor().getFranchisorId());
				}
				
				response.setDocumentsFolder(documentManagement.createDocumentsFolder(folder));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Documents Folder: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/folder", method=RequestMethod.PUT)
	@ResponseBody
	public WSDocumentsFolderResponse updateFolder(@RequestBody DocumentsFolderData folder, HttpSession session) {
		
		WSDocumentsFolderResponse response = new WSDocumentsFolderResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (!user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to update Document Folder");
			
			} else {
				
				response.setDocumentsFolder(documentManagement.createDocumentsFolder(folder));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Documents Folder: " + e.toString());
		}
		
		return response;
	}

	/*
	 * Process uploaded files
	 */
	@RequestMapping(value = "/file", method=RequestMethod.POST)
	@ResponseBody
	public WSDocumentsFileListResponse saveFiles(@RequestBody DocumentsFilesSaveRequest files, HttpSession session) {
		WSDocumentsFileListResponse response = new WSDocumentsFileListResponse();

		try {

			UserProfileData user = SessionUtil.getLoggedUser(session);

			List<UploadedFileData> filesInfo = new ArrayList<UploadedFileData>();
			if (files.getFileIds() != null && files.getFileIds().size() > 0) {
				for (Long fileId : files.getFileIds()) {
					filesInfo.add(fileManagement.getUploadedFile(fileId));
				}

				response.setFiles(documentManagement.createDocumentsFiles(filesInfo, files.getFolderId(), user.getFranchisor().getFranchisorId(), files.getAccessRestricted(), files.getFranchiseeIds()));

				for (Long fileId : files.getFileIds()) {
					fileManagement.deleteUploadedFile(fileId);
				}
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.FAIL_TO_UPLOAD_DOCUMENT.code());
			rm.setMessage(ResponseCodesEnum.FAIL_TO_UPLOAD_DOCUMENT.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error uploading document file: " + e.toString());
		}
		
		return response;	
	}
	
	@RequestMapping(value = "/file", method=RequestMethod.PUT)
	@ResponseBody
	public WSDocumentsFileResponse updateFile(@RequestBody DocumentsFileData file, HttpSession session) {
		
		WSDocumentsFileResponse response = new WSDocumentsFileResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (!user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to update Document Folder");
			
			} else {
				
				if (file.getAccessRestricted()) {
					file.setFranchiseeIds(null);
				}
				response.setDocumentsFile(documentManagement.updateDocumentsFile(file));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Documents Folder: " + e.toString());
		}
		
		return response;
	}

	@RequestMapping(value = "/move", method=RequestMethod.PUT)
	@ResponseBody
	public WSDocumentsMoveResponse moveFolderOrFile(@RequestBody MoveFolderOrFileData moveData, HttpSession session) {
		
		WSDocumentsMoveResponse response = new WSDocumentsMoveResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (!user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to update Document Folder");
			
			} else {
				
				if (moveData.getType().equals("file")) {
					response.setFile(documentManagement.moveFile(moveData.getId(), moveData.getFolderId()));
				} else if (moveData.getType().equals("folder")) {
					response.setFolder(documentManagement.moveFolder(moveData.getId(), moveData.getFolderId()));
				}
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error creating Documents Folder: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value="/folder/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSDocumentsFolderResponse deleteFolder(@PathVariable Long id, HttpSession session) {
		
		WSDocumentsFolderResponse response = new WSDocumentsFolderResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (!user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to update Document Folder");
			
			} else {				
				response.setDocumentsFolder(documentManagement.deleteDocumentsFolder(id));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Documents Folder: " + e.toString());
		}
		
		return response;
	}
	
	@RequestMapping(value="/file/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSDocumentsFileResponse deleteFile(@PathVariable Long id, HttpSession session) {
		
		WSDocumentsFileResponse response = new WSDocumentsFileResponse();
		
		try {
			
			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);
			if (!user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR)) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.NOT_AUTHORIZED.code());
				rm.setMessage(ResponseCodesEnum.NOT_AUTHORIZED.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to update Document Folder");
			
			} else {				
				response.setDocumentsFile(documentManagement.deleteDocumentsFile(id));
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Documents File: " + e.toString());
		}
		
		return response;
	}

}
