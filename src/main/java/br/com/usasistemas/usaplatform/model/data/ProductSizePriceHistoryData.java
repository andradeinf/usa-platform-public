package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;

import br.com.usasistemas.usaplatform.model.enums.MailNotificationStatusEnum;

public class ProductSizePriceHistoryData {
	
	private Long id;
	private Long productId;
	private Long productSizeId;
	private Date date;
	private Long userId;
	private Double oldUnitPrice;
	private Double newUnitPrice;
	private MailNotificationStatusEnum notificationStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getOldUnitPrice() {
		return oldUnitPrice;
	}

	public void setOldUnitPrice(Double oldUnitPrice) {
		this.oldUnitPrice = oldUnitPrice;
	}

	public Double getNewUnitPrice() {
		return newUnitPrice;
	}

	public void setNewUnitPrice(Double newUnitPrice) {
		this.newUnitPrice = newUnitPrice;
	}

	public MailNotificationStatusEnum getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(MailNotificationStatusEnum notificationStatus) {
		this.notificationStatus = notificationStatus;
	}
	
}
