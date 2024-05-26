package br.com.usasistemas.usaplatform.model.data;

import java.util.List;

public class CalendarMonthData {
	
	private Long month;
	private Long year;
	private List<Long> days;
	
	public Long getMonth() {
		return month;
	}
	
	public void setMonth(Long month) {
		this.month = month;
	}
	
	public Long getYear() {
		return year;
	}
	
	public void setYear(Long year) {
		this.year = year;
	}
	
	public List<Long> getDays() {
		return days;
	}
	
	public void setDays(List<Long> days) {
		this.days = days;
	}

}
