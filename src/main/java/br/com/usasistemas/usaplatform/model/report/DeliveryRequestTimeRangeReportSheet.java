package br.com.usasistemas.usaplatform.model.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import br.com.usasistemas.usaplatform.bizo.FileManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchiseeManagementBO;
import br.com.usasistemas.usaplatform.bizo.FranchisorManagementBO;
import br.com.usasistemas.usaplatform.bizo.ProductManagementBO;
import br.com.usasistemas.usaplatform.bizo.SupplierManagementBO;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestData;
import br.com.usasistemas.usaplatform.model.data.DeliveryRequestStatusChangeData;
import br.com.usasistemas.usaplatform.model.data.FranchiseeData;
import br.com.usasistemas.usaplatform.model.data.FranchisorData;
import br.com.usasistemas.usaplatform.model.data.ProductData;
import br.com.usasistemas.usaplatform.model.data.ProductSizeData;
import br.com.usasistemas.usaplatform.model.data.SupplierData;
import br.com.usasistemas.usaplatform.model.data.TimeRangeReportData;
import br.com.usasistemas.usaplatform.model.enums.DeliveryRequestStatusEnum;

public class DeliveryRequestTimeRangeReportSheet extends ReportSheet {

    private ProductManagementBO productManagement;
    private SupplierManagementBO supplierManagement;
    private FranchiseeManagementBO franchiseeManagement;
    private FranchisorManagementBO franchisorManagement;
    
    private Map<Long, ProductData> productMap;
    private Map<Long, ProductSizeData> productSizeMap;
    private Map<Long, FranchiseeData> franchiseeMap;
    private Map<Long, FranchisorData> franchisorMap;
    private Map<Long, SupplierData> supplierMap;
    private double totalPrice;
    
    public DeliveryRequestTimeRangeReportSheet(TimeRangeReportData timeRangeReport, FileManagementBO fileManagement, ProductManagementBO productManagement, SupplierManagementBO supplierManagement, FranchiseeManagementBO franchiseeManagement, FranchisorManagementBO franchisorManagement) {
        super(timeRangeReport, fileManagement);

        this.productManagement = productManagement;
        this.supplierManagement = supplierManagement;
        this.franchiseeManagement = franchiseeManagement;
        this.franchisorManagement = franchisorManagement;

        productMap = new HashMap<Long, ProductData>();
        productSizeMap = new HashMap<Long, ProductSizeData>();
        franchiseeMap = new HashMap<Long, FranchiseeData>();
        franchisorMap = new HashMap<Long, FranchisorData>();
        supplierMap = new HashMap<Long, SupplierData>();
        totalPrice = 0;
    }

    @Override
    protected void addHeaderRow() {
        
        int cellCount = 0;
        Cell cell = null;
        Row row = sheet.createRow(rowCount++);
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Data do Pedido");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Quantidade");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Preço Unitário");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Valor Total do Pedido");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Status Atual");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Motivo Cancelamento");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Produto");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Detalhes");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Fornecedor");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Enviado em");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Entrega até");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Transportadora");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Rastreamento");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Nota Fiscal");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Data Vcto do Boleto");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Franquia");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Loja");

        for (DeliveryRequestStatusEnum status : DeliveryRequestStatusEnum.values()) {
            cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Status - " + status.getDescription());
        }
    }

    public void writeData(List<DeliveryRequestData> deliveryRequests) {

        if ((deliveryRequests != null) && (!deliveryRequests.isEmpty())) {
            for (DeliveryRequestData deliveryRequest : deliveryRequests) {
                
                if (!productMap.containsKey(deliveryRequest.getProductId())) {
                    productMap.put(deliveryRequest.getProductId(), productManagement.getProduct(deliveryRequest.getProductId()));
                }
                
                if (!productSizeMap.containsKey(deliveryRequest.getProductSizeId())) {
                    productSizeMap.put(deliveryRequest.getProductSizeId(), productManagement.getProductSize(deliveryRequest.getProductSizeId()));
                }
                
                if (!supplierMap.containsKey(deliveryRequest.getSupplierId())) {
                    SupplierData supplier = supplierManagement.getSupplier(deliveryRequest.getSupplierId());
                    supplierMap.put(deliveryRequest.getSupplierId(), supplier);
                }

                if (!franchiseeMap.containsKey(deliveryRequest.getFranchiseeId())) {
                    FranchiseeData franchisee = franchiseeManagement.getFranchisee(deliveryRequest.getFranchiseeId());
                    franchiseeMap.put(deliveryRequest.getFranchiseeId(), franchisee);
                    franchisorMap.put(franchisee.getFranchisorId(), franchisorManagement.getFranchisor(franchisee.getFranchisorId()));
                }
                            
                double totalDeliveryPrice = 0;
                if (deliveryRequest.getStatus() != DeliveryRequestStatusEnum.CANCELLED) {
                    totalDeliveryPrice = deliveryRequest.getQuantity().longValue() * deliveryRequest.getDeliveryUnitPrice().doubleValue();
                }
                totalPrice += totalDeliveryPrice;

                Map<DeliveryRequestStatusEnum, Date> statusHistory = new HashMap<DeliveryRequestStatusEnum, Date>();
                if (deliveryRequest.getStatusHistory() != null) {
                    for (DeliveryRequestStatusChangeData statusChange : deliveryRequest.getStatusHistory().getItems()) {
                        statusHistory.put(statusChange.getNewStatus(), statusChange.getDate());
                    }
                }                
                
                //add data
                this.addDataRow(
                    deliveryRequest.getDate(),
                    deliveryRequest.getQuantity(),
                    deliveryRequest.getDeliveryUnitPrice(),
                    totalDeliveryPrice,
                    deliveryRequest.getStatusDescription(),
                    deliveryRequest.getCancellationComment(),
                    productMap.get(deliveryRequest.getProductId()).getName(),
                    productSizeMap.get(deliveryRequest.getProductSizeId()).getName(),
                    supplierMap.get(deliveryRequest.getSupplierId()).getName(),
                    deliveryRequest.getSentDate(),
                    deliveryRequest.getDeadlineDate(),
                    deliveryRequest.getCarrierName(),
                    deliveryRequest.getTrackingCode(),
                    deliveryRequest.getFiscalNumber(),
                    deliveryRequest.getDueDate(),
                    franchisorMap.get(franchiseeMap.get(deliveryRequest.getFranchiseeId()).getFranchisorId()).getName(),
                    franchiseeMap.get(deliveryRequest.getFranchiseeId()).getName(),
                    statusHistory
                );
            }
        }
    }

    private void addDataRow(Date date, Long quantity, Double unitPrice, Double totalPrice, String currentStatus, String cancellationComment,
			String productName, String productSizeName, String supplierName, Date sentDate, Date deadlineDate, String carrierName,
			String trackingCode, String fiscalNumber, Date dueDate, String franchisorName, String franchiseeName, Map<DeliveryRequestStatusEnum, Date> statusHistory) {

        int cellCount = 0;
        Cell cell = null;
        Row row = sheet.createRow(rowCount++);
        cell = row.createCell(cellCount++); cell.setCellValue(nvl(date));
        cell = row.createCell(cellCount++); cell.setCellValue(quantity);
        cell = row.createCell(cellCount++); cell.setCellValue(unitPrice); cell.setCellStyle(moneyStyle);
        cell = row.createCell(cellCount++); cell.setCellValue(totalPrice); cell.setCellStyle(moneyStyle);	    	
        cell = row.createCell(cellCount++); cell.setCellValue(currentStatus);
        cell = row.createCell(cellCount++); cell.setCellValue(nvl(cancellationComment));
        cell = row.createCell(cellCount++); cell.setCellValue(productName);
        cell = row.createCell(cellCount++); cell.setCellValue(productSizeName);
        cell = row.createCell(cellCount++); cell.setCellValue(supplierName);
        cell = row.createCell(cellCount++); cell.setCellValue(nvlBase(sentDate));
        cell = row.createCell(cellCount++); cell.setCellValue(nvlBase(deadlineDate));
        cell = row.createCell(cellCount++); cell.setCellValue(nvl(carrierName));
        cell = row.createCell(cellCount++); cell.setCellValue(nvl(trackingCode));
        cell = row.createCell(cellCount++); cell.setCellValue(nvl(fiscalNumber));
        cell = row.createCell(cellCount++); cell.setCellValue(nvlBase(dueDate));
        cell = row.createCell(cellCount++); cell.setCellValue(franchisorName);
        cell = row.createCell(cellCount++); cell.setCellValue(franchiseeName);

        for (DeliveryRequestStatusEnum status : DeliveryRequestStatusEnum.values()) {
            cell = row.createCell(cellCount++); cell.setCellValue(nvl(statusHistory.get(status)));
        }
    }

    private void addTotalRow(Double totalPrice) {

        int cellCount = 0;
        Cell cell = null;
        Row row = sheet.createRow(rowCount++);
        cell = row.createCell(cellCount++); cell.setCellValue("");
        cell = row.createCell(cellCount++); cell.setCellValue("");
        cell = row.createCell(cellCount++); cell.setCellStyle(boldStyle); cell.setCellValue("Valor Total:");
        cell = row.createCell(cellCount++); cell.setCellStyle(moneyBoldStyle); cell.setCellValue(totalPrice);
    }

    public void saveAndClose() {
        
        if (totalPrice > 0) {
            //write sum total
            this.addTotalRow(totalPrice);
        }

        super.saveAndClose("RelatorioDeEntregas");
    }
    
}