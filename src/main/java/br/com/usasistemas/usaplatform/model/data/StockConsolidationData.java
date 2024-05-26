package br.com.usasistemas.usaplatform.model.data;

import java.util.Date;

import br.com.usasistemas.usaplatform.model.enums.StockConsolidationStatusEnum;

public class StockConsolidationData {
	
	private Long id;
	private Date date;
	private StockConsolidationStatusEnum status;
	private String message;
	private String details;

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

	public StockConsolidationStatusEnum getStatus() {
		return status;
	}

	public void setStatus(StockConsolidationStatusEnum status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
