package br.com.usasistemas.usaplatform.model.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FeatureFlagData implements Serializable {
	
	private Boolean flagAnnouncement;
	private Boolean flagCalendar;
	private Boolean flagDocuments;
	private Boolean flagTraining;

	public FeatureFlagData() {
		this.flagAnnouncement = true;
		this.flagCalendar = true;
		this.flagDocuments = true;
		this.flagTraining = true;
	}

	public Boolean getFlagAnnouncement() {
		return this.flagAnnouncement;
	}

	public void setFlagAnnouncement(Boolean flagAnnouncement) {
		this.flagAnnouncement = flagAnnouncement;
	}

	public Boolean getFlagCalendar() {
		return this.flagCalendar;
	}

	public void setFlagCalendar(Boolean flagCalendar) {
		this.flagCalendar = flagCalendar;
	}

	public Boolean getFlagDocuments() {
		return this.flagDocuments;
	}

	public void setFlagDocuments(Boolean flagDocuments) {
		this.flagDocuments = flagDocuments;
	}

	public Boolean getFlagTraining() {
		return this.flagTraining;
	}
	
	public void setFlagTraining(Boolean flagTraining) {
		this.flagTraining = flagTraining;
	}

}
