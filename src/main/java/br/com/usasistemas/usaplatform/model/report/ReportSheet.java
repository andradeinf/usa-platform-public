package br.com.usasistemas.usaplatform.model.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.common.util.DateUtil;
import br.com.usasistemas.usaplatform.model.data.TimeRangeReportData;

public abstract class ReportSheet {

    protected static final Logger log = Logger.getLogger(ReportSheet.class.getName());

    TimeRangeReportData timeRangeReport;

    protected FileManagementBO fileManagement;

    public String fileName;
    public String fileKey;

    protected Workbook workbook;
    protected Sheet sheet;

    protected CellStyle boldStyle;
    protected CellStyle moneyStyle;
    protected CellStyle moneyBoldStyle;

    protected int rowCount;

    public ReportSheet(TimeRangeReportData timeRangeReport, FileManagementBO fileManagement) {

        this.timeRangeReport = timeRangeReport;
        this.fileManagement = fileManagement;

        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();

        boldStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        boldStyle.setFont(font);
        
        moneyStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        moneyStyle.setDataFormat(format.getFormat("0.00"));
        
        moneyBoldStyle = workbook.createCellStyle();
        moneyBoldStyle.setFont(font);
        moneyBoldStyle.setDataFormat(format.getFormat("0.00"));
        
        //header
        rowCount = 0;
        this.addHeaderRow();
    }

    protected String nvl(Date date) {
        if (date == null) {
            return "";
        } else {
            return DateUtil.getDate(date, DateUtil.SLASH_FULL_PATTERN);
        }
    }
    
    protected String nvlBase(Date date) {
        if (date == null) {
            return "";
        } else {
            return DateUtil.getDate(date, DateUtil.SLASH_PATTERN);
        }
    }
    
    protected String nvl(String string) {
        if (string == null) {
            return "";
        } else {
            return string;
        }
    }
    
    protected abstract void addHeaderRow();

    public void saveAndClose(String reportName) {
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            fileName = reportName + "_" + timeRangeReport.getId() + ".xls";
            fileKey = fileManagement.createExcelFile("reports/timeRange/" + timeRangeReport.getType().getFolderName() + "/" + timeRangeReport.getEntityProfile() + "/" + timeRangeReport.getEntityId() + "/" + fileName, outputStream);
            workbook.close();		
        } catch (IOException ioe) {
            log.severe("Error generating report");
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.severe("Error closing file output stream");
            }
        }
    }
}