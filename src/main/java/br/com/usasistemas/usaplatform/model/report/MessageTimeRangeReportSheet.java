package br.com.usasistemas.usaplatform.model.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.UserManagementBO;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.MessageTopicData;
import br.com.usasistemas.usaplatform.model.data.TimeRangeReportData;
import br.com.usasistemas.usaplatform.model.data.UserGroupData;

public class MessageTimeRangeReportSheet extends ReportSheet {

    private FranchiseeManagementBO franchiseeManagement;
    private UserManagementBO userManagement;

    private Map<Long, FranchiseeData> franchiseeMap;
    private Map<Long, UserGroupData> userGroupMap;

    public MessageTimeRangeReportSheet(TimeRangeReportData timeRangeReport, FileManagementBO fileManagement, FranchiseeManagementBO franchiseeManagement, UserManagementBO userManagement) {
        super(timeRangeReport, fileManagement);

        this.franchiseeManagement = franchiseeManagement;
        this.userManagement = userManagement;

        franchiseeMap = new HashMap<Long, FranchiseeData>();
        userGroupMap = new HashMap<Long, UserGroupData>();
    }

    @Override
    protected void addHeaderRow() {
        
        int cellCount = 0;
			Cell cell = null;
			Row row = sheet.createRow(rowCount++);
            cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Data da Primeira Mensagem");
            cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Data da Última Mensagem");
            cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("De");
            cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Para");
			cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Título");
    }

    public void writeData(List<MessageTopicData> messageTopics) {

        if ((messageTopics != null) && (!messageTopics.isEmpty())) {
            for (MessageTopicData messageTopic : messageTopics) {

                if (!franchiseeMap.containsKey(messageTopic.getFromEntityId())) {
                    FranchiseeData franchisee = franchiseeManagement.getFranchisee(messageTopic.getFromEntityId());
                    franchiseeMap.put(franchisee.getId(), franchisee);
                }

                if (!userGroupMap.containsKey(messageTopic.getFromUserGroupId())) {
                    UserGroupData userGroup = userManagement.getUserGroup(messageTopic.getFromUserGroupId());
                    userGroupMap.put(userGroup.getId(), userGroup);
                }

                if (!userGroupMap.containsKey(messageTopic.getToUserGroupId())) {
                    UserGroupData userGroup = userManagement.getUserGroup(messageTopic.getToUserGroupId());
                    userGroupMap.put(userGroup.getId(), userGroup);
                }
                
                //add data
                this.addDataRow(
                    messageTopic.getDate(),
                    messageTopic.getUpdateDate(),
                    messageTopic.getTitle(),
                    franchiseeMap.get(messageTopic.getFromEntityId()).getName(),
                    userGroupMap.get(messageTopic.getFromUserGroupId()).getName(),
                    userGroupMap.get(messageTopic.getToUserGroupId()).getName()
                );
            }
        }
    }

    private void addDataRow(Date createdDate, Date updatedDate, String title, String franchiseeName, String franchiseeUserGroupName, String franchisorUserGroupName) {

        int cellCount = 0;
        Cell cell = null;
        Row row = sheet.createRow(rowCount++);
        cell = row.createCell(cellCount++); cell.setCellValue(nvl(createdDate));
        cell = row.createCell(cellCount++); cell.setCellValue(nvl(updatedDate));
        cell = row.createCell(cellCount++); cell.setCellValue(franchiseeName + " - " + franchiseeUserGroupName);
        cell = row.createCell(cellCount++); cell.setCellValue(franchisorUserGroupName);
        cell = row.createCell(cellCount++); cell.setCellValue(title);
    }

    public void saveAndClose() {
        super.saveAndClose("RelatorioDeMensagens");
    }
    
}