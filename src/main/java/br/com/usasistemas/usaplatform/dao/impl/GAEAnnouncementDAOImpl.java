package br.com.usasistemas.usaplatform.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;

import br.com.usasistemas.usaplatform.dao.AnnouncementDAO;
import br.com.usasistemas.usaplatform.dao.response.AnnouncementPagedResponse;
import br.com.usasistemas.usaplatform.model.data.AnnouncementData;
import br.com.usasistemas.usaplatform.model.entity.Announcement;
import br.com.usasistemas.usaplatform.model.enums.AnnouncementStatusEnum;

public class GAEAnnouncementDAOImpl extends GAEGenericDAOImpl<Announcement, AnnouncementData> implements AnnouncementDAO {
	
	private static final Logger log = Logger.getLogger(GAEAnnouncementDAOImpl.class.getName());
	private static final String INDEX_NAME = "AnnouncementSearchIndex";

	private Index index;
	private Date referenceDate;
	
	public GAEAnnouncementDAOImpl() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME).build();
		index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		
		referenceDate = new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime();
	}

	@Override
	public String addSearchIndexDocument(AnnouncementData announcement) {
		
		Builder indexBuilder = 
			Document.newBuilder()
			.setId(announcement.getId().toString())
			.setRank(calculateDocumentRank(announcement.getDate()));

		indexBuilder
			.addField(Field.newBuilder().setName("franchisorId").setAtom(announcement.getFranchisorId().toString()))
			.addField(Field.newBuilder().setName("status").setAtom(announcement.getStatus().toString()))
			.addField(Field.newBuilder().setName("title").setText(announcement.getTitle().toString()))
			.addField(Field.newBuilder().setName("date").setDate(announcement.getDate()));

		announcement.getFranchiseeIds().forEach(franchiseeId -> {
			indexBuilder.addField(Field.newBuilder().setName("franchiseeId").setAtom(franchiseeId.toString()));
		});
		
		return index.put(indexBuilder.build()).getIds().get(0);
	}

	private int calculateDocumentRank(Date updatedDate) {
		return Long.valueOf((updatedDate.getTime() - referenceDate.getTime())/1000).intValue();
	}

	@Override
	public AnnouncementPagedResponse findByFranchisorId(Long franchisorId, Long pageSize, Long page) {
		AnnouncementPagedResponse result = new AnnouncementPagedResponse();
		
		try {
			Long startIndex = (page - 1) * pageSize;

			List<Announcement> announcements = ofy().load().type(Announcement.class)
				.filter("franchisorId", franchisorId)
				.offset(startIndex.intValue())
				.limit(pageSize.intValue() + 1) //Try to get 1 record more than requested
				.order("-date")
				.list();
			
			if (announcements != null && !announcements.isEmpty()) {
				
				//If got the extra record, means that there is more to retrieve later
				result.setHasMore(announcements.size() > pageSize);
				
				List<AnnouncementData> announcecmentsResult = this.getConverter().convertToDataList(announcements);
				//Remove the extra item when sending the response in case the extra item was returned
				if (announcecmentsResult.size() > pageSize)
					announcecmentsResult.remove(announcecmentsResult.size() - 1);
				result.setAnnouncements(announcecmentsResult);				
			}
		} catch (Exception e) {
			log.warning("Error when querying for Annnouncement by franchisorId: " + e.toString());
		}			
	
		return result;
	}
	
	@Override
	public AnnouncementPagedResponse findByFranchisorIdAndFranchiseeIdsAndSatus(Long franchisorId, List<Long> franchiseeIds, AnnouncementStatusEnum status, Long pageSize, Long page) {
		AnnouncementPagedResponse result = new AnnouncementPagedResponse();
		
		try {
			QueryOptions.Builder builder = 
			QueryOptions
				.newBuilder();
					  
			if (page != null && pageSize != null) {
				Long offset = (page - 1) * pageSize;

				builder
					.setLimit(pageSize.intValue())
					.setNumberFoundAccuracy(pageSize.intValue() + 1)
					.setOffset(offset.intValue());
			}

			QueryOptions options = builder.build();

			String queryOperator = "";
			StringBuilder franchiseeIdsFilter = new StringBuilder("(");
			for (Long id : franchiseeIds) {
				franchiseeIdsFilter.append(queryOperator).append(id);
				queryOperator = " OR ";
			}
			franchiseeIdsFilter.append(")");

			// prepare query
			StringBuilder queryString = new StringBuilder();
			queryString.append("franchiseeId: ").append(franchiseeIdsFilter.toString());
			queryString.append(" AND franchisorId: ").append(franchisorId);
			queryString.append(" AND status: ").append(status);

			Query query = Query.newBuilder().setOptions(options).build(queryString.toString());
			Results<ScoredDocument> searchResult = index.search(query);

			result.setHasMore(searchResult.getNumberFound() > searchResult.getNumberReturned());

			List<AnnouncementData> announcements = this.findByIds(
				searchResult
					.getResults()
					.stream()
					.map(doc -> Long.parseLong(doc.getId()))
					.collect(Collectors.toList())
			);

			if (announcements == null) {
				announcements = new ArrayList<AnnouncementData>();
			}

			result.setAnnouncements(announcements);

		} catch (Exception e) {
			log.warning("Error when querying for Annnouncement by franchisorId: " + e.toString());
		}			
	
		return result;
	}
}
