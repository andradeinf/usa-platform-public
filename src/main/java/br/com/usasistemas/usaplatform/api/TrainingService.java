package br.com.usasistemas.usaplatform.api;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.usasistemas.usaplatform.api.response.GenericResponse;
import br.com.usasistemas.usaplatform.api.response.UrlResponse;
import br.com.usasistemas.usaplatform.api.response.WSTrainingListResponse;
import br.com.usasistemas.usaplatform.api.response.WSTrainingResponse;
import br.com.usasistemas.usaplatform.api.response.WSTrainingViewControlListResponse;
import br.com.usasistemas.usaplatform.api.response.WSTrainingViewControlResponse;
import br.com.usasistemas.usaplatform.api.response.WSUploadedFileResponse;
import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.ConfigurationManagementBO;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.TrainingManagementBO;
import br.com.usasistemas.usaplatform.common.constants.UrlMapping;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.common.util.SessionUtil;
import br.com.usasistemas.usaplatform.model.data.TrainingData;
import br.com.usasistemas.usaplatform.model.data.TrainingViewControlData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.data.UserProfileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.UserProfileEnum;

@Controller
@RequestMapping(value = UrlMapping.TRAINING_RESOURCE)
public class TrainingService {
	
	private static final Logger log = Logger.getLogger(TrainingService.class.getName());
	private TrainingManagementBO trainingManagement;
	private ConfigurationManagementBO configurationManagement;
	private FileManagementBO fileManagement;

	public TrainingManagementBO getTrainingManagement() {
		return trainingManagement;
	}

	public void setTrainingManagement(TrainingManagementBO trainingManagement) {
		this.trainingManagement = trainingManagement;
	}

	public ConfigurationManagementBO getConfigurationManagement() {
		return configurationManagement;
	}

	public void setConfigurationManagement(ConfigurationManagementBO configurationManagement) {
		this.configurationManagement = configurationManagement;
	}

	public FileManagementBO getFileManagement() {
		return fileManagement;
	}

	public void setFileManagement(FileManagementBO fileManagement) {
		this.fileManagement = fileManagement;
	}

	/*
	 * Get franchisor trainings
	 */
	@RequestMapping(value = "/franchisor/{franchisorId}", method=RequestMethod.GET)
	@ResponseBody
	public WSTrainingListResponse listFranchisorTrainings(@PathVariable Long franchisorId, HttpSession session) {
		WSTrainingListResponse response = new WSTrainingListResponse();
		
		try {

			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);

			if (!user.getFeatureFlags().getFlagTraining()) {

				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.GENERIC_SUCCESS.code());
				rm.setMessage(ResponseCodesEnum.GENERIC_SUCCESS.message());
				rm.setDetails("Usuário não autorizado a realizar essa operação");
				response.setReturnMessage(rm);
				log.warning("User not authorized trying to access trainings!");

			} else {

				if (user.getSelectedRole().equals(UserProfileEnum.FRANCHISOR)) {
					response.setTrainings(trainingManagement.getFranchisorTrainings(franchisorId));
				} else if (user.getSelectedRole().equals(UserProfileEnum.FRANCHISEE)) {
					response.setTrainings(trainingManagement.getFanchiseeFranchisorTrainings(franchisorId, user.getSelectedEntityId()));
				}
			}
			
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Franchisor Training List: " + e.toString());
			e.printStackTrace();
		}
		
		return response;
	}

	@RequestMapping(value = "/{trainingId}/viewControl/franchisee/{franchiseeId}", method=RequestMethod.GET)
	@ResponseBody
	public WSTrainingViewControlListResponse listTrainingViewControl(@PathVariable Long trainingId, @PathVariable Long franchiseeId) {
		
		WSTrainingViewControlListResponse response = new WSTrainingViewControlListResponse();
		
		try {
			List<TrainingViewControlData> trainingViewControls = trainingManagement.getTrainingViewControl(trainingId, franchiseeId);
			response.setTrainingViewControls(trainingViewControls);
			response.setUsers(trainingManagement.getTrainingViewControlsUsers(trainingViewControls));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error getting Training View Controls: " + e.toString());
		}
		
		return response;		
	}

	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public WSTrainingResponse postTraining(@RequestBody TrainingData training, HttpSession session) {

		WSTrainingResponse response = new WSTrainingResponse();

		try {

			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);

			if (training.getId() == 0) {
				training.setId(null);	
				training.setFranchisorId(user.getSelectedEntityId());
			}

			response.setTraining(trainingManagement.createTraining(training));

		} catch (BusinessException be) {

			response.setReturnMessage(be.getReturnMessage());
			log.warning("Error saving Training: " + be.toString());

		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Training: " + e.toString());
		}

		return response;

	}
	
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	public WSTrainingResponse putTraining(@RequestBody TrainingData training) {

		WSTrainingResponse response = new WSTrainingResponse();

		try {
			response.setTraining(trainingManagement.updateTraining(training));
		} catch (BusinessException be) {

			response.setReturnMessage(be.getReturnMessage());
			log.warning("Error saving Training: " + be.toString());

		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error saving Training: " + e.toString());
		}

		return response;

	}

	@RequestMapping(value = "/orders", method=RequestMethod.PUT)
	@ResponseBody
	public GenericResponse updateTrainingsOrder(@RequestBody Map<Long, Long> trainingsOrder) {

		GenericResponse response = new GenericResponse();

		try {
			trainingManagement.updateTrainingsOrder(trainingsOrder);
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Trainings orders: " + e.toString());
		}

		return response;
	}

	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public WSTrainingResponse deleteTraining(@PathVariable Long id) {

		WSTrainingResponse response = new WSTrainingResponse();

		try {
			response.setTraining(trainingManagement.deleteTraining(id));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error deleting Training: " + e.toString());
		}

		return response;
	}

	/*
	 * Get URL to upload training video
	 */
	@RequestMapping(value = "/video/url", method=RequestMethod.GET)
	@ResponseBody
	public UrlResponse getVideoUploadtUrl() {
		
		UrlResponse response = new UrlResponse();		
		response.setUrl(fileManagement.getTrainingVideoUploadUrl(UrlMapping.TRAINING_RESOURCE + "/video/upload"));
		return response;	
	}

	/*
	 * Process uploaded training video
	 */
	@RequestMapping(value = "/video/upload", method=RequestMethod.POST)
	@ResponseBody
	public WSUploadedFileResponse processUploadedVideo(HttpServletRequest request) {
		WSUploadedFileResponse response = new WSUploadedFileResponse();
		
		try {
			UploadedFileData file = fileManagement.getUploadedFileInfo(request, "uploadedFile");
			response.setUploadedFile(fileManagement.createUploadedFile(file));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.FAIL_TO_UPLOAD_ATTACHMENT.code());
			rm.setMessage(ResponseCodesEnum.FAIL_TO_UPLOAD_ATTACHMENT.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error uploading training video: " + e.toString());
		}
		
		return response;	
	}

	@RequestMapping(value="/video/stream/{id}", method=RequestMethod.GET)
	public void streamFile(@PathVariable Long id, HttpServletResponse response) {
		TrainingData training = trainingManagement.getTraining(id);
		fileManagement.readFile(training.getVideoFileKey(), training.getVideoName(), false, response);
	}

	@RequestMapping(value = "/updateTrainingViewControl", method=RequestMethod.PUT)
	@ResponseBody
	public WSTrainingViewControlResponse updateTrainingsViewControl(@RequestBody TrainingViewControlData trainingViewControl, HttpSession session) {

		WSTrainingViewControlResponse response = new WSTrainingViewControlResponse();

		try {

			//Get Logged User
			UserProfileData user = SessionUtil.getLoggedUser(session);

			trainingViewControl.setFranchiseeId(user.getSelectedEntityId());
			trainingViewControl.setFranchiseeUserId(user.getSelectedEntityUserId());
			trainingViewControl.setUserId(user.getId());

			response.setTrainingViewControl(trainingManagement.updateTrainingViewControlCurrentTime(trainingViewControl));
		} catch (Exception e) {
			ReturnMessage rm = new ReturnMessage();
			rm.setCode(ResponseCodesEnum.GENERIC_ERROR.code());
			rm.setMessage(ResponseCodesEnum.GENERIC_ERROR.message());
			rm.setDetails(e.toString());
			response.setReturnMessage(rm);
			log.warning("Error updating Trainings View Control Current Time: " + e.toString());
		}

		return response;
	}
}
