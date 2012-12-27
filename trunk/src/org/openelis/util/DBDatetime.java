/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DBDatetime {
    public static  boolean formatMDY;
    public static  final int    YEAR     = 0,
                                MONTH    = 1,
                                DAY      = 2,
                                HOUR     = 3,
                                MINUTE   = 4,
                                SECOND   = 5,
                                FRACTION = 6;

    protected int     first, last;
    protected Date    date;
    protected boolean parsed;

    protected final static String[][]
        YMD =
        {{"yyyy","yyyy-MM","yyyy-MM-dd","yyyy-MM-dd HH","yyyy-MM-dd HH:mm","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.SSS"},
         {"",    "MM",     "MM-dd",     "MM-dd HH",     "MM-dd HH:mm",     "MM-dd HH:mm:ss",     "MM-dd HH:mm:ss.SSS"},
         {"",    "",       "dd",        "dd HH",        "dd HH:mm",        "dd HH:mm:ss",        "dd HH:mm:ss.SSS"},
         {"",    "",       "",          "HH",           "HH:mm",           "HH:mm:ss",           "HH:mm:ss.SSS"},
         {"",    "",       "",          "",             "mm",              "mm:ss",              "mm:ss.SSS"},
         {"",    "",       "",          "",             "",                "ss",                 "ss.SSS"},
         {"",    "",       "",          "",             "",                "",                   "SSS"}
        },
        MDY =
        {{"yyyy","MM-yyyy","MM-dd-yyyy","MM-dd-yyyy HH","MM-dd-yyyy HH:mm","MM-dd-yyyy HH:mm:ss","MM-dd-yyyy HH:mm:ss.SSS"},
         {"",    "MM",     "MM-dd",     "MM-dd HH",     "MM-dd HH:mm",     "MM-dd HH:mm:ss",     "MM-dd HH:mm:ss.SSS"},
         {"",    "",       "dd",        "dd HH",        "dd HH:mm",        "dd HH:mm:ss",        "dd HH:mm:ss.SSS"},
         {"",    "",       "",          "HH",           "HH:mm",           "HH:mm:ss",           "HH:mm:ss.SSS"},
         {"",    "",       "",          "",             "mm",              "mm:ss",              "mm:ss.SSS"},
         {"",    "",       "",          "",             "",                "ss",                 "ss.SSS"},
         {"",    "",       "",          "",             "",                "",                   "SSS"}
        };
    
    public DBDatetime(int first, int last) {
        this(first, last, null);
    }

    public DBDatetime(int first, int last, Date aDate) {
        date       = aDate;
        parsed     = false;
        this.first = (first < YEAR || first > FRACTION) ? YEAR : first;
        this.last  = (last  < YEAR || last  > FRACTION) ? FRACTION : last;
    }

    public int first() {
        return first;
    }

    public int last() {
        return last;
    }

    public static DBDatetime getInstance() {
        return new DBDatetime(YEAR, SECOND, new Date());
    }

    public static DBDatetime getInstance(int first, int last) {
        return new DBDatetime(first, last, new Date());
    }

    public static DBDatetime getInstance(int first, int last, Date aDate) {
        if (aDate == null)
            return null;
        return new DBDatetime(first, last, aDate);
    }

    public boolean after(DBDatetime aDate) {
        if (aDate == null)
            return false;

        parse();
        aDate.parse();
        if (date.getTime() > aDate.date.getTime())
            return true;
        return false;
    }

    public boolean before(DBDatetime aDate) {
        if (aDate == null)
            return false;

        parse();
        aDate.parse();
        if (date.getTime() < aDate.date.getTime())
            return true;
        return false;
    }

    public boolean equals(DBDatetime aDate) {
        if (aDate == null)
            return false;

        parse();
        aDate.parse();
        if (date.getTime() == aDate.date.getTime())
            return true;
        return false;
    }

    public void setDate(Date aDate) {
        date   = aDate;
        parsed = false;
    }

    public void setDate(Object aObj) throws ParseException {
        String objString = null;
        SimpleDateFormat format;
        
        parsed = false;

        if (aObj != null)
            objString = aObj.toString();

        if (objString.trim().length() == 0) {
            date = null;
            return;
        }

        format = new SimpleDateFormat();
        format.setLenient(false);
        if (formatMDY)
            format.applyLocalizedPattern(MDY[first][last]);
        else            
            format.applyLocalizedPattern(YMD[first][last]);
        date = format.parse(objString);
    }

    public Date getDate() {
        parse();
        return date;
    }

    public DBDatetime extend(int fromFirst, int toLast) {
        DBDatetime newDate;

        newDate = new DBDatetime(fromFirst, toLast, date);
        return newDate;
    }
        
    public DBDatetime add(int field, int amount) {
        Calendar cal;
        
        if (date == null)
            return null;
        parse();

        cal = Calendar.getInstance();
        cal.setTime(date);
        switch (field) {
            case YEAR:
                cal.add(Calendar.YEAR, amount);
                break;
            case MONTH:
                cal.add(Calendar.MONTH, amount);
                break;
            case DAY:
                cal.add(Calendar.DATE, amount);
                break;
            case HOUR:
                cal.add(Calendar.HOUR_OF_DAY, amount);
                break;
            case MINUTE:
                cal.add(Calendar.MINUTE, amount);
                break;
            case SECOND:
                cal.add(Calendar.SECOND, amount);
                break;
            case FRACTION:
                cal.add(Calendar.MILLISECOND, amount);
                break;
        }
        return new DBDatetime(first, last, cal.getTime());
    }        

    public DBDatetime roll(int field, boolean up) {
        Calendar cal;
        
        if (date == null)
            return null;
        parse();

        cal = Calendar.getInstance();
        cal.setTime(date);
        switch (field) {
            case YEAR:
                cal.roll(Calendar.YEAR, up);
                break;
            case MONTH:
                cal.roll(Calendar.MONTH, up);
                break;
            case DAY:
                cal.roll(Calendar.DATE, up);
                break;
            case HOUR:
                cal.roll(Calendar.HOUR_OF_DAY, up);
                break;
            case MINUTE:
                cal.roll(Calendar.MINUTE, up);
                break;
            case SECOND:
                cal.roll(Calendar.SECOND, up);
                break;
            case FRACTION:
                cal.roll(Calendar.MILLISECOND, up);
                break;
        }
        return new DBDatetime(first, last, cal.getTime());
    }        

    public String toString() {
        return toString(YMD[first][last]);
    }

    public String toDisplayString() {
        if (formatMDY)
            return toString(MDY[first][last]);
        return toString(YMD[first][last]);
    }

    public String toString(String picture) {
        SimpleDateFormat format;

        parse();
        format = new SimpleDateFormat(picture);

        return format.format(date);
    }

    protected void parse() {
        Calendar format;
        
        if (parsed || date == null)
            return;

        format = Calendar.getInstance();
        format.setTime(date);
        switch (first) {
            case FRACTION:
                format.set(Calendar.SECOND, 0);
            case SECOND:
                format.set(Calendar.MINUTE, 0);
            case MINUTE:
                format.set(Calendar.HOUR_OF_DAY, 0);
            case HOUR:
                format.set(Calendar.DATE, 0);
            case DAY:
                format.set(Calendar.MONTH, 0);
            case MONTH:
                format.set(Calendar.YEAR, 1);       
            default:
                break;
        }

        switch (last) {
            case YEAR:
                format.set(Calendar.MONTH, 0);
            case MONTH:
                format.set(Calendar.DATE, 1);
            case DAY:
                format.set(Calendar.HOUR_OF_DAY, 0);
            case HOUR:
                format.set(Calendar.MINUTE, 0);
            case MINUTE:
                format.set(Calendar.SECOND, 0);
            case SECOND:
                format.set(Calendar.MILLISECOND, 0);
            default:
                break;
        }
        date = format.getTime();
        parsed = true;
    }

}
