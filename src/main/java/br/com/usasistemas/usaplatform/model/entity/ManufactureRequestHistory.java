package br.com.usasistemas.usaplatform.model.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;
import br.com.usasistemas.usaplatform.model.enums.ManufactureRequestStatusEnum;

@Entity
@Index
public class ManufactureRequestHistory {
	
	@Id private Long id;
	private Date date;
	private Long supplierId;
	private Long franchisorId;
	private Long productId;
	private Long productSizeId;
	private Double manufactureUnitPrice;
	private Long quantity;
	private ManufactureRequestStatusEnum status;
	private String cancellationComment;
	private Date updateDate;
	private MailNotificationStatusEnum notificationStatus;
	private Long stockConsolidationId;
	private String domainKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getFranchisorId() {
		return franchisorId;
	}

	public void setFranchisorId(Long franchisorId) {
		this.franchisorId = franchisorId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getProductSizeId() {
		return productSizeId;
	}

	public void setProductSizeId(Long productSizeId) {
		this.productSizeId = productSizeId;
	}

	public Double getManufactureUnitPrice() {
		return manufactureUnitPrice;
	}

	public void setManufactureUnitPrice(Double manufactureUnitPrice) {
		this.manufactureUnitPrice = manufactureUnitPrice;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public ManufactureRequestStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ManufactureRequestStatusEnum status) {
		this.status = status;
	}

	public String getCancellationComment() {
		return cancellationComment;
	}

	public void setCancellationComment(String cancellationComment) {
		this.cancellationComment = cancellationComment;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public MailNotificationStatusEnum getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(MailNotificationStatusEnum notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public Long getStockConsolidationId() {
		return stockConsolidationId;
	}

	public void setStockConsolidationId(Long stockConsolidationId) {
		this.stockConsolidationId = stockConsolidationId;
	}

	public String getDomainKey() {
		return domainKey;
	}

	public void setDomainKey(String domainKey) {
		this.domainKey = domainKey;
	}
}
