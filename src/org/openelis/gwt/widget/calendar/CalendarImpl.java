package org.openelis.gwt.widget.calendar;

import java.util.Date;

import com.google.gwt.i18n.client.TimeZone;

public class CalendarImpl implements Comparable<CalendarImpl> {

	private Date time;
	private long timeInMilli;

	public static final int AM = 0;
	public static final int PM = 1;

	public static final int JANUARY = 0;
	public static final int FEBRUARY = 1;
	public static final int MARCH = 2;
	public static final int APRIL = 3;
	public static final int MAY = 4;
	public static final int JUNE = 5;
	public static final int JULY = 6;
	public static final int AUGUST = 7;
	public static final int SEPTEMBER = 8;
	public static final int OCTOBER = 9;
	public static final int NOVEMBER = 10;
	public static final int DECEMBER = 11;
	public static final int UNDECIMBER = 12;

	public static final int ERA = 0;
	public static final int YEAR = 1;
	public static final int MONTH = 2;
	public static final int WEEK_OF_YEAR = 3;
	public static final int WEEK_OF_MONTH = 4;
	public static final int DATE = 5;
	public static final int DAY_OF_MONTH = 5;
	public static final int DAY_OF_YEAR = 6;
	public static final int DAY_OF_WEEK = 7;
	public static final int DAY_OF_WEEK_IN_MONTH = 8;
	public static final int AM_PM = 9;
	public static final int HOUR = 10;
	public static final int HOUR_OF_DAY = 11;
	public static final int MINUTE = 12;
	public static final int SECOND = 13;
	public static final int MILLISECOND = 14;
	public static final int ZONE_OFFSET = 15;
	public static final int DST_OFFSET = 16;
	public static final int FIELD_COUNT = 17;

	public static final int SUNDAY = 1;
	public static final int MONDAY = 2;
	public static final int TUESDAY = 3;
	public static final int WEDNESDAY = 4;
	public static final int THURSDAY = 5;
	public static final int FRIDAY = 6;
	public static final int SATURDAY = 7;

	private int era;
	private int year;
	private int month;
	private int weekOfYear;
	private int weekOfMonth;
	private int date;
	private int dayOfMonth;
	private int dayOfYear;
	private int dayOfWeek;
	private int dayOfWeekInMonth;
	private int amPm;
	private int hour;
	private int hourOfDay;
	private int minute;
	private int second;
	private int millisecond;
	private int zoneOffset;
	private int dstOffset;
	private int fieldCount;

	private CalendarImpl() {
		time = new Date();
		timeInMilli = time.getTime();
		setTime(new Date());		
	}
	
	@SuppressWarnings("deprecation")
	private void setTime(Date date) {
		set(time.getYear(),time.getMonth(),time.getDate(),time.getHours(),time.getMinutes(),time.getSeconds());
		set(ZONE_OFFSET,time.getTimezoneOffset());
		set(AM_PM, hour < 12 ? 0 : 1);
		set(DAY_OF_WEEK,time.getDay());
		set(MILLISECOND,(int)(timeInMilli - time.getTime()));
		TimeZone tz = TimeZone.createTimeZone(zoneOffset);
		set(DST_OFFSET,tz.getDaylightAdjustment(time));
	}

	public static CalendarImpl getInstance() {
		return new CalendarImpl();
	}

	@SuppressWarnings("deprecation")
	public void add(int field, int amount) {
		time = getTime();
		switch (field) {
		case YEAR:
			time.setYear(year + amount);
			break;
		case MONTH:
			time.setMonth(month + amount);
			break;
		case DATE:
			time.setDate(date + amount);
			break;
		case HOUR:
			time.setHours(hour + amount);
			break;
		case MINUTE:
			time.setMinutes(minute + amount);
			break;
		case SECOND :
		    time.setSeconds(second + amount);
		}
		setTime(time);
	}

	public boolean after(Object obj) {
		Date param = null;
		
		if(obj instanceof Date)
			param = (Date)obj;
		else if(obj instanceof CalendarImpl)
			param = ((CalendarImpl)obj).getTime();
		return getTime().after(param);
	}

	public boolean before(Object obj) {
		Date param = null;
		
		if(obj instanceof Date)
			param = (Date)obj;
		else if(obj instanceof CalendarImpl)
			param = ((CalendarImpl)obj).getTime();
		return getTime().before(param);
	}

	public void clear() {
		
	}

	public void clearField(int field) {
		set(field,-1);
	}

	public int compareTo(CalendarImpl comp) {
		if(after(comp))
			return 1;
		else if(before(comp))
			return -1;
		else
			return 0;
	}

	public boolean equals(Object obj) {
		Date param = null;
		
		if(obj instanceof Date)
			param = (Date)obj;
		else if(obj instanceof CalendarImpl)
			param = ((CalendarImpl)obj).getTime();
		return getTime().equals(param);
	}

	public int get(int field) {
	    getTime();
		switch (field) {
		case ERA:
			return era;
		case YEAR:
			return year;
		case MONTH:
			return month;
		case WEEK_OF_YEAR:
			return weekOfYear;
		case WEEK_OF_MONTH:
			return weekOfMonth;
		case DATE:
			return date;
		case DAY_OF_YEAR:
			return dayOfYear;
		case DAY_OF_WEEK:
			return dayOfWeek;
		case DAY_OF_WEEK_IN_MONTH:
			return dayOfWeekInMonth;
		case AM_PM:
			return amPm;
		case HOUR:
			return hour;
		case HOUR_OF_DAY:
			return hourOfDay;
		case MINUTE:
			return minute;
		case SECOND:
			return second;
		case MILLISECOND:
			return millisecond;
		case ZONE_OFFSET:
			return zoneOffset;
		case DST_OFFSET:
			return dstOffset;
		}
		return -1;
	}

	public int getActualMaximum(int field) {
		switch(field) {
		case YEAR :
			return 9999999;
		case MONTH :
			return 11;
		case DATE : 
			switch(month){
			case JANUARY :
				return 31;
			case FEBRUARY :
				if(isLeapYear(year))
					return 29;
				return 28;
			case MARCH :
				return 31;
			case APRIL : 
				return 30;
			case MAY :
				return 31;
			case JUNE :
				return 30;
			case JULY :
				return 31;
			case AUGUST :
				return 31;
			case SEPTEMBER :
				return 30;
			case OCTOBER :
				return 31;
			case NOVEMBER :
				return 30;
			case DECEMBER :
				return 31;
			}
		case HOUR :
			return 23;
		case MINUTE :
			return 59;
		case SECOND :
			return 59;
		case MILLISECOND :
			return 999;
		case DAY_OF_WEEK :
			return 7;
		case DAY_OF_YEAR :
			if(isLeapYear(year))
				return 357;
			return 356;
		case WEEK_OF_YEAR :
			return 52;
		}
		return -1;
	} 
	
	private boolean isLeapYear(int yr) {
		return yr % 4 == 0 && yr % 400 != 0; 
	}

	public int getActualMinimum(int field) {
		switch(field) {
		case YEAR :
			return -999999;
		case MONTH :
			return 0;
		case DATE :
			return 1;
		case HOUR : 
			return 0;
		case MINUTE :
			return 0;
		case SECOND :
			return 0;
		case MILLISECOND : 
			return 0;
		case DAY_OF_WEEK :
			return 1;
		}
		return -1;
	}

	@SuppressWarnings("deprecation")
	public Date getTime() {
		time = new Date(year,month,date,hour,minute,second);
		setTime(time);
		return time;
	}

	public long getTimeInMillis() {
		return getTime().getTime();
	}

	public void roll(int field, boolean up) {
		roll(field,up ? 1 : -1);
	}
	
	public void roll(int field, int amount) {
		switch(field){
		case YEAR :
			year += amount;
			break;
		case MONTH :
			month = checkRoll(MONTH,month+amount);
		    break;
		case DATE :
			date = checkRoll(DATE,date+amount);
			break;
		case HOUR :
			hour = checkRoll(HOUR,hour+amount);
			break;
		case MINUTE :
			minute = checkRoll(MINUTE,minute+amount);
			break;
		case SECOND :
			second = checkRoll(SECOND,second+amount);
			break;
		case MILLISECOND :
			millisecond = checkRoll(MILLISECOND,millisecond+amount); 
		}
	}
	
    private int checkRoll(int field, int val) {
		 if(date > getActualMaximum(field))
			 return getActualMinimum(field);
		 if(date < getActualMinimum(field))
			 return getActualMaximum(field);
		 return val;  
    }

	public void set(int field, int amount) {
		switch (field) {
		case ERA:
			this.era = amount;
			break;
		case YEAR:
			this.year = amount;
			break;
		case MONTH:
			this.month = amount;
			break;
		case WEEK_OF_YEAR:
			this.weekOfYear = amount;
			break;
		case WEEK_OF_MONTH:
			this.weekOfMonth = amount;
			break;
		case DATE:
			this.date = amount;
			break;
		case DAY_OF_YEAR:
			this.dayOfYear = amount;
			break;
		case DAY_OF_WEEK:
			this.dayOfWeek = amount;
			break;
		case DAY_OF_WEEK_IN_MONTH:
			this.dayOfWeekInMonth = amount;
			break;
		case AM_PM:
			this.amPm = amount;
			break;
		case HOUR:
			this.hour = amount;
			break;
		case HOUR_OF_DAY:
			this.hourOfDay = amount;
			break;
		case MINUTE:
			this.minute = amount;
			break;
		case SECOND:
			this.second = amount;
			break;
		case MILLISECOND:
			this.millisecond = amount;
			break;
		case ZONE_OFFSET:
			this.zoneOffset = amount;
			break;
		case DST_OFFSET:
			this.dstOffset = amount;
			break;
		}
	}

	public void set(int year, int month, int date) {
		set(YEAR, year);
		set(MONTH, month);
		set(DATE, date);
	}

	public void set(int year, int month, int date, int hour, int minute) {
		set(year, month, date);
		set(HOUR, hour);
		set(MINUTE, minute);
	}

	public void set(int year, int month, int date, int hour, int minute,
			int second) {
		set(year, month, date, hour, minute);
		set(SECOND, second);
	}
}
