package br.com.usasistemas.usaplatform.api.request;

import java.util.List;

public class DocumentsFilesSaveRequest {
	
	private List<Long> fileIds;
	private Long folderId;
	private Boolean accessRestricted;
	private List<Long> franchiseeIds;

	public List<Long> getFileIds() {
		return this.fileIds;
	}

	public void setFileIds(List<Long> fileIds) {
		this.fileIds = fileIds;
	}

	public Long getFolderId() {
		return this.folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public Boolean getAccessRestricted() {
		return this.accessRestricted;
	}

	public void setAccessRestricted(Boolean accessRestricted) {
		this.accessRestricted = accessRestricted;
	}

	public List<Long> getFranchiseeIds() {
		return this.franchiseeIds;
	}

	public void setFranchiseeIds(List<Long> franchiseeIds) {
		this.franchiseeIds = franchiseeIds;
	}
	
}
