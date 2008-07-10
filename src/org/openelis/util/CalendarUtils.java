/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.util;

import java.util.Calendar;
import java.util.Date;

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
        return today;
    }
    
    public static boolean isSelected(Calendar cal, String date){
        Calendar dcal = Calendar.getInstance();
        dcal.setTime(new Date(date));
        return getDateString(cal).equals(getDateString(dcal));
    }
    
    

}
