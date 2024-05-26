package br.com.usasistemas.usaplatform.model.data;

public class CalendarMonthDefinitionData {
	
	private Long currentDay;
	private Long currentMonth;
	private Long currentYear;
	private CalendarMonthData selectedMonth;
	private CalendarMonthData previousMonth;
	private CalendarMonthData nextMonth;

	public Long getCurrentDay() {
		return currentDay;
	}

	public void setCurrentDay(Long currentDay) {
		this.currentDay = currentDay;
	}

	public Long getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(Long currentMonth) {
		this.currentMonth = currentMonth;
	}

	public Long getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(Long currentYear) {
		this.currentYear = currentYear;
	}

	public CalendarMonthData getSelectedMonth() {
		return selectedMonth;
	}

	public void setSelectedMonth(CalendarMonthData selectedMonth) {
		this.selectedMonth = selectedMonth;
	}

	public CalendarMonthData getPreviousMonth() {
		return previousMonth;
	}

	public void setPreviousMonth(CalendarMonthData previousMonth) {
		this.previousMonth = previousMonth;
	}

	public CalendarMonthData getNextMonth() {
		return nextMonth;
	}

	public void setNextMonth(CalendarMonthData nextMonth) {
		this.nextMonth = nextMonth;
	}

}
