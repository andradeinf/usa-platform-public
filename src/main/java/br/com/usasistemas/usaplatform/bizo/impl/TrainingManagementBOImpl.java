package br.com.usasistemas.usaplatform.bizo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.TrainingManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.common.exception.BusinessException;
import br.com.usasistemas.usaplatform.dao.TrainingDAO;
import br.com.usasistemas.usaplatform.dao.TrainingViewControlDAO;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.PublicUserData;
import br.com.usasistemas.usaplatform.model.data.TrainingData;
import br.com.usasistemas.usaplatform.model.data.TrainingViewControlData;
import br.com.usasistemas.usaplatform.model.data.UploadedFileData;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;
import br.com.usasistemas.usaplatform.model.enums.TrainingViewControlStatusEnum;

public class TrainingManagementBOImpl implements TrainingManagementBO {

	private static final Logger log = Logger.getLogger(TrainingManagementBOImpl.class.getName());
	private TrainingDAO trainingDAO;
	private TrainingViewControlDAO trainingViewControlDAO;
	private FileManagementBO fileManegement;
	private FranchisorManagementBO franchisorManegement;
	private UserManagementBO userManagement;

	public TrainingDAO getTrainingDAO() {
		return trainingDAO;
	}

	public void setTrainingDAO(TrainingDAO trainingDAO) {
		this.trainingDAO = trainingDAO;
	}

	public TrainingViewControlDAO getTrainingViewControlDAO() {
		return trainingViewControlDAO;
	}

	public void setTrainingViewControlDAO(TrainingViewControlDAO trainingViewControlDAO) {
		this.trainingViewControlDAO = trainingViewControlDAO;
	}

	public FileManagementBO getFileManegement() {
		return fileManegement;
	}

	public void setFileManegement(FileManagementBO fileManegement) {
		this.fileManegement = fileManegement;
	}

	public FranchisorManagementBO getFranchisorManagement() {
		return franchisorManegement;
	}

	public void setFranchisorManagement(FranchisorManagementBO franchisorManegement) {
		this.franchisorManegement = franchisorManegement;
	}

	public UserManagementBO getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementBO userManagement) {
		this.userManagement = userManagement;
	}

	@Override
	public List<TrainingData> getFranchisorTrainings(Long franchisorId) {
		return trainingDAO.findByFranchisorId(franchisorId);
	}

	private Boolean listIsNullOrEmpty(List<Long> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}		
		
		return false;
	}

	@Override
	public List<TrainingData> getFanchiseeFranchisorTrainings(Long franchisorId, Long franchiseeId) {
		return trainingDAO.findByFranchisorId(franchisorId)
				.stream()
				.filter(training -> 
					!training.getAccessRestricted() && (
						listIsNullOrEmpty(training.getFranchiseeIds()) ||
						training.getFranchiseeIds().contains(franchiseeId)
					)					
				)
				.collect(Collectors.toList());
	}

	@Override
	public TrainingData getTraining(Long id) {
		return trainingDAO.findById(id);
	}

	@Override
	public TrainingData createTraining(TrainingData training) {

		UploadedFileData uploadedVideoFile = fileManegement.getUploadedFile(training.getVideoId());
		checkVideoFormat(uploadedVideoFile);
		checkStorageLimit(training, uploadedVideoFile);

		training.setUpdatedDate(new Date());

		// update training and clean up uploaded file record
		updateVideoInfo(training, uploadedVideoFile);

		return trainingDAO.save(training);
	}

	@Override
	public TrainingData updateTraining(TrainingData changedTraining) {

		TrainingData training = trainingDAO.findById(changedTraining.getId());
		training.setUpdatedDate(new Date());
		training.setOrder(changedTraining.getOrder());
		training.setTitle(changedTraining.getTitle());
		training.setDescription(changedTraining.getDescription());
		training.setAccessRestricted(changedTraining.getAccessRestricted());
		training.setFranchiseeIds(changedTraining.getFranchiseeIds());

		// check if video replaced
		if (!training.getVideoId().equals(changedTraining.getVideoId())) {

			UploadedFileData uploadedVideoFile = fileManegement.getUploadedFile(changedTraining.getVideoId());
			checkVideoFormat(uploadedVideoFile);
			checkStorageLimit(training, uploadedVideoFile);

			// delete the original video
			fileManegement.deleteFile(training.getVideoFileKey());

			// update training and clean up uploaded file record
			updateVideoInfo(training, uploadedVideoFile);
		}

		return trainingDAO.save(training);
	}

	private void checkStorageLimit(TrainingData training, UploadedFileData uploadedVideoFile) {

		if (uploadedVideoFile != null) {

			FranchisorData franchisor = franchisorManegement.getFranchisor(training.getFranchisorId());

			Long currentSize = trainingDAO.findByFranchisorId(training.getFranchisorId()).stream()
					.filter(item -> !item.getId().equals(training.getId())).map(item -> item.getVideoSize())
					.reduce(0L, (subtotal, size) -> subtotal + size);

			if (currentSize + uploadedVideoFile.getSize() > franchisor.getMaxStorageSizeMB() * 1024 * 1024) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.TRAINING_STORAGE_LIMITED_REACHED.code());
				rm.setMessage(ResponseCodesEnum.TRAINING_STORAGE_LIMITED_REACHED.message());
				throw new BusinessException(rm);
			}
		}
	}

	private void checkVideoFormat(UploadedFileData uploadedVideoFile) {

		if (uploadedVideoFile != null) {

			if (!uploadedVideoFile.getContentType().equals("video/mp4")) {
				ReturnMessage rm = new ReturnMessage();
				rm.setCode(ResponseCodesEnum.TRAINING_FILE_FORMAT_NOT_ACCEPTED.code());
				rm.setMessage(ResponseCodesEnum.TRAINING_FILE_FORMAT_NOT_ACCEPTED.message());
				throw new BusinessException(rm);
			}
		}
	}

	private void updateVideoInfo(TrainingData training, UploadedFileData uploadedVideoFile) {

		if (uploadedVideoFile != null) {

			training.setVideoName(uploadedVideoFile.getName());
			training.setVideoContentType(uploadedVideoFile.getContentType());
			training.setVideoStorePath(uploadedVideoFile.getStorePath());
			training.setVideoSize(uploadedVideoFile.getSize());
			training.setVideoFileKey(uploadedVideoFile.getFileKey());

			fileManegement.deleteUploadedFile(uploadedVideoFile.getId());

		} else {
			log.severe("Unable to find uploaded video for trainingId: " + training.getId());

			training.setVideoId(null);
			training.setVideoName(null);
			training.setVideoContentType(null);
			training.setVideoStorePath(null);
			training.setVideoSize(null);
			training.setVideoFileKey(null);
		}
	}

	@Override
	public TrainingData deleteTraining(Long id) {

		trainingViewControlDAO.deleteAll(
			trainingViewControlDAO
				.findByTrainingId(id)
				.stream()
				.map(trainingViewControl -> trainingViewControl.getId())
				.collect(Collectors.toList())
		);

		TrainingData training = trainingDAO.findById(id);
		if (training != null && training.getVideoFileKey() != null) {
			fileManegement.deleteFile(training.getVideoFileKey());
		}
		
		return trainingDAO.delete(id);
	}

	@Override
	public void updateTrainingsOrder(Map<Long, Long> trainingsOrder) {
		List<TrainingData> trainings = trainingDAO.findByIds(new ArrayList<Long>(trainingsOrder.keySet()));
		trainings.stream().forEach(training -> training.setOrder(trainingsOrder.get(training.getId())));
		trainingDAO.saveAll(trainings);
	}

	@Override
	public TrainingViewControlData updateTrainingViewControlCurrentTime(
			TrainingViewControlData updatedTrainingViewControl) {

		TrainingViewControlData trainingViewControl = trainingViewControlDAO.findByTrainingIdAndFranchiseeUserId(
				updatedTrainingViewControl.getTrainingId(), updatedTrainingViewControl.getFranchiseeUserId());
		if (trainingViewControl == null) {
			trainingViewControl = updatedTrainingViewControl;
			trainingViewControl.setUpdatedDate(new Date());
		} else {
			if (!trainingViewControl.getViewStatus().equals(TrainingViewControlStatusEnum.COMPLETED)) {
				if (updatedTrainingViewControl.getViewStatus().equals(TrainingViewControlStatusEnum.COMPLETED)) {
					trainingViewControl.setViewCurrentTime(0L);
					trainingViewControl.setViewStatus(TrainingViewControlStatusEnum.COMPLETED);
					trainingViewControl.setUpdatedDate(new Date());
				} else {
					if (trainingViewControl.getViewCurrentTime() < updatedTrainingViewControl.getViewCurrentTime()) {
						trainingViewControl.setViewCurrentTime(updatedTrainingViewControl.getViewCurrentTime());
						trainingViewControl.setUpdatedDate(new Date());
					}
				}
			}
		}

		return trainingViewControlDAO.save(trainingViewControl);

	}

	@Override
	public List<TrainingViewControlData> getTrainingViewControl(Long trainingId, Long franchiseeId) {
		return trainingViewControlDAO.findByTrainingIdAndFranchiseeId(trainingId, franchiseeId);
	}

	@Override
	public Map<Long, PublicUserData> getTrainingViewControlsUsers(List<TrainingViewControlData> trainingViewControls) {
		Map<Long, PublicUserData> response = new HashMap<Long, PublicUserData>();
		
		if (trainingViewControls != null & !trainingViewControls.isEmpty()) {
			for (TrainingViewControlData trainingViewControl: trainingViewControls) {
				
				PublicUserData userData = userManagement.getUser(trainingViewControl.getUserId());
				userData.setEmail(null);
				userData.setEnabled(null);
				userData.setUsername(null);
				userData.setSelectedRole(null);
				
				response.put(trainingViewControl.getUserId(), userData);
			}
		}
		
		return response;
	}

}
