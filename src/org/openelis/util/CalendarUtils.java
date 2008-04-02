package org.openelis.util;

import java.util.Calendar;

public class CalendarUtils {
    
    public static Calendar getCalforMonth(String month,String year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Integer.parseInt(month));
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.DATE, 1);
        if(cal.get(Calendar.DAY_OF_WEEK) > 1)
            cal.add(Calendar.DATE, -cal.get(Calendar.DAY_OF_WEEK));
        else
            cal.add(Calendar.DATE, -8);
        return cal;
    }
    
    public static int getField(String field){
        if(field.equals("DATE"))
            return Calendar.DATE;
        if(field.equals("MONTH"))
            return Calendar.MONTH;
        return -1;      
    }
    
    public static int getNextDay(Calendar cal) {
        cal.add(Calendar.DATE, 1);
        return cal.get(Calendar.DATE);
    }
    
    public static boolean isToday(Calendar cal){
        Calendar today = Calendar.getInstance();
        if(today.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
           today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
           today.get(Calendar.DATE) == cal.get(Calendar.DATE)) 
            return true;
        return false;
    }
    
    public static String getMonthYear(String month, String year){
        switch(Integer.parseInt(month)){
            case Calendar.JANUARY:
                return "January " +year;
            case Calendar.FEBRUARY:
                return "Febuary " +year;
            case Calendar.MARCH: 
                return "March " +year;
            case Calendar.APRIL:
                return "April " +year;
            case Calendar.MAY:
                return "May " +year;
            case Calendar.JUNE:
                return "June " +year;
            case Calendar.JULY:
                return "July " +year;
            case Calendar.AUGUST:
                return "August " +year;
            case Calendar.SEPTEMBER:
                return "September " +year;
            case Calendar.OCTOBER:
                return "October " +year;
            case Calendar.NOVEMBER:
                return "November " +year;
            case Calendar.DECEMBER:
                return "December " +year;
        }
        return null;
    }
    
    public static String getMonthAbrv(String month){
        switch(Integer.parseInt(month)){
            case Calendar.JANUARY:
                return "Jan";
            case Calendar.FEBRUARY:
                return "Feb";
            case Calendar.MARCH: 
                return "Mar";
            case Calendar.APRIL:
                return "Apr";
            case Calendar.MAY:
                return "May";
            case Calendar.JUNE:
                return "Jun";
            case Calendar.JULY:
                return "Jul";
            case Calendar.AUGUST:
                return "Aug";
            case Calendar.SEPTEMBER:
                return "Sep";
            case Calendar.OCTOBER:
                return "Oct";
            case Calendar.NOVEMBER:
                return "Nov";
            case Calendar.DECEMBER:
                return "Dec";
        }
        return null;
    }
    
    
    public static String getDateString(Calendar cal) {
        return cal.get(Calendar.YEAR) +"/" +(cal.get(Calendar.MONTH)+1) + "/"+ cal.get(Calendar.DATE);
        
    }
    
    public static String getCurrentDateString(){
        String today = getDateString(Calendar.getInstance());
        System.out.println(today);
        return today;
    }
    
    

}
