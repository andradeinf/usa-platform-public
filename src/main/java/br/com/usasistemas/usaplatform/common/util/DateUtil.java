package br.com.usasistemas.usaplatform.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {
	
	public static String SLASH_PATTERN = "dd/MM/yyyy";
	public static String DASH_PATTERN = "dd-MM-yyyy";
	public static String INVERSE_DASH_PATTERN = "yyyy-MM-dd";
	public static String SLASH_FULL_PATTERN = "dd/MM/yyyy HH:mm:ss";
	public static String DASH_FULL_PATTERN = "dd-MM-yyyy HH:mm:ss";
	private static String timezone = "America/Sao_Paulo";
	
	//return a calendar in the Brazilian timezone
	public static Calendar getLocalCalendar() {
		return Calendar.getInstance(TimeZone.getTimeZone(timezone));
	}
	
	//return current date time in Brazilian timezone
	public static Date getCurrentDate() {
		
		DateTimeZone zone = DateTimeZone.forID(timezone);
		DateTime currentDate = new DateTime(zone);
	
		return currentDate.toDate();
	}
	
	//return current date time in Brazilian timezone considering offset
	public static Date getDateWithOffset(Long offSet) {
		
		DateTimeZone zone = DateTimeZone.forID(timezone);
		DateTime currentDate = new DateTime(zone);
		if (offSet < 0) {
			currentDate = currentDate.minusDays(offSet.intValue() * -1);
		} else {
			currentDate = currentDate.plusDays(offSet.intValue());
		}	
	
		return currentDate.toDate();
	}
	
	//return date time in Brazilian timezone
	public static Date getDate(String strDate) {
		return getDate(strDate, SLASH_PATTERN);
	}
	
	public static Date getDate(String strDate, String pattern) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		DateTime date = formatter.withZone(DateTimeZone.forID(timezone)).parseDateTime(strDate);
		return date.toDate();
	}
	
	//return date time in Brazilian timezone
	public static String getDate(Date date) {
		return getDate(date, SLASH_FULL_PATTERN);
	}
	
	public static String getDate(Date date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		DateTime jdate = new DateTime(date);
		return formatter.withZone(DateTimeZone.forID(timezone)).print(jdate);
	}
	
	public static Long getDaysBetween(Date initDate, Date endDate) {
		DateTime jinitDate = new DateTime(initDate);
		DateTime jendDate = new DateTime(endDate);
		
		return Long.valueOf(Days.daysBetween(jinitDate.withTimeAtStartOfDay() , jendDate.withTimeAtStartOfDay() ).getDays());
	}

}
