package br.com.usasistemas.usaplatform.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.com.usasistemas.usaplatform.dao.AnnouncementDAO;
import br.com.usasistemas.usaplatform.dao.repository.ObjectifyRegistry;
import br.com.usasistemas.usaplatform.dao.response.AnnouncementPagedResponse;
import br.com.usasistemas.usaplatform.model.data.AnnouncementData;
import br.com.usasistemas.usaplatform.model.enums.AnnouncementStatusEnum;

@RunWith(JUnit4.class)
@Ignore
/*
    *** THIS TEST IS NOT COMPLETE AND NOT WORKING
    *** It fails when creating search index, as it seems we cannot do it outside the App Engine scope
*/
public class GAEAnnouncementDAOImplTest {

    private AnnouncementDAO announcementDAO = new GAEAnnouncementDAOImpl();
    private Closeable session;
    
    @BeforeClass
    public static void init() {
        ObjectifyRegistry.initialize();
    }

    @Before
    public void setUp() {
        session = ObjectifyService.begin();
    }

    @After
    public void tearDown() {
        session.close();
    }

    @Test
    public void testFindByFranchisorIdAndFranchiseeIdsAndSatus() {

        Long FRANCHISOR_ID = 1234567890L;
        Long FRANCHISEE_01_ID = 0000000001L;
        Long FRANCHISEE_02_ID = 0000000002L;
        Long NO_FRANCHISEE_ID = 0L;
        String ANNOUNCEMENT_TITLE_FRANCHISEE_01 = "AnnouncementTitleFranchisee01";
        String ANNOUNCEMENT_TITLE_FRANCHISEE_02 = "AnnouncementTitleFranchisee02";
        String ANNOUNCEMENT_TITLE_NO_FRANCHISEE = "AnnouncementTitleNoFranchisee";

        AnnouncementData newAnnouncement = null;
        List<AnnouncementData> announcements = new ArrayList<AnnouncementData>();
        newAnnouncement = new AnnouncementData();
        newAnnouncement.setDate(new Date());
        newAnnouncement.setFranchisorId(FRANCHISOR_ID);
        newAnnouncement.setStatus(AnnouncementStatusEnum.PUBLISHED);
        newAnnouncement.setTitle(ANNOUNCEMENT_TITLE_FRANCHISEE_01);
        newAnnouncement.setFranchiseeIds(Arrays.asList(FRANCHISEE_01_ID));
        announcements.add(newAnnouncement);
        newAnnouncement = new AnnouncementData();
        newAnnouncement.setDate(new Date());
        newAnnouncement.setFranchisorId(FRANCHISOR_ID);
        newAnnouncement.setStatus(AnnouncementStatusEnum.PUBLISHED);
        newAnnouncement.setTitle(ANNOUNCEMENT_TITLE_FRANCHISEE_02);
        newAnnouncement.setFranchiseeIds(Arrays.asList(FRANCHISEE_02_ID));
        announcements.add(newAnnouncement);
        newAnnouncement = new AnnouncementData();
        newAnnouncement.setDate(new Date());
        newAnnouncement.setFranchisorId(FRANCHISOR_ID);
        newAnnouncement.setStatus(AnnouncementStatusEnum.PUBLISHED);
        newAnnouncement.setTitle(ANNOUNCEMENT_TITLE_NO_FRANCHISEE);
        newAnnouncement.setFranchiseeIds(Arrays.asList(NO_FRANCHISEE_ID));
        announcements.add(newAnnouncement);
        List<AnnouncementData> savedAnnouncements = announcementDAO.saveAll(announcements);
        assertNotNull(savedAnnouncements);
        assertEquals(savedAnnouncements.size(), 3);

        for (AnnouncementData savedAnnouncement : savedAnnouncements) {
            String indexId = announcementDAO.addSearchIndexDocument(savedAnnouncement);
            assertNotNull(indexId);
        }

        AnnouncementPagedResponse result = announcementDAO.findByFranchisorIdAndFranchiseeIdsAndSatus(FRANCHISOR_ID, Arrays.asList(FRANCHISEE_01_ID), AnnouncementStatusEnum.PUBLISHED, 10L, 1L);
        assertNotNull(result);
        assertNotNull(result.getAnnouncements());
        assertEquals(result.getAnnouncements().size(), 1);
        assertEquals(result.getAnnouncements().get(0).getTitle(), ANNOUNCEMENT_TITLE_FRANCHISEE_01);

        List<Long> savedAnnouncementIds = savedAnnouncements.stream().map(announcement -> announcement.getId()).collect(Collectors.toList());
        List<AnnouncementData> deletedAnnouncements = announcementDAO.deleteAll(savedAnnouncementIds);
        assertNotNull(deletedAnnouncements);
        assertEquals(deletedAnnouncements.size(), 3);

    }
    
} 