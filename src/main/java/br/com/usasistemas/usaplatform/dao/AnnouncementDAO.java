package br.com.usasistemas.usaplatform.dao;

import java.util.List;

import br.com.usasistemas.usaplatform.dao.response.AnnouncementPagedResponse;
import br.com.usasistemas.usaplatform.model.data.AnnouncementData;
import br.com.usasistemas.usaplatform.model.entity.Announcement;
import br.com.usasistemas.usaplatform.model.enums.AnnouncementStatusEnum;

public interface AnnouncementDAO extends GenericDAO<Announcement, AnnouncementData> {

	public AnnouncementPagedResponse findByFranchisorId(Long franchisorId, Long pageSize, Long page);
	public AnnouncementPagedResponse findByFranchisorIdAndFranchiseeIdsAndSatus(Long franchisorId, List<Long> franchiseeIds, AnnouncementStatusEnum status, Long pageSize, Long page);
	public String addSearchIndexDocument(AnnouncementData announcement);
}
