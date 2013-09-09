/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class DataBaseUtil {

    /*
     * Removed blanks from both sides of the string and nullifies empty strings
     */
    public static String trim(String result) {
        if (result != null) {
            result = result.trim();
            if (result.length() == 0)
                result = null;
        }
        return result;
    }

    /*
     * Convenience methods to covert a date or a datetime to datetime with date
     * precision.
     */
    public static Datetime toYD(Datetime yearToDay) {
        if (yearToDay != null && (yearToDay.getStartCode() != Datetime.YEAR || yearToDay.getEndCode() != Datetime.DAY))
            yearToDay = toYD(yearToDay.getDate());
        return yearToDay;
    }

    public static Datetime toYD(Date yearToDay) {
        Datetime dt;

        dt = null;
        if (yearToDay != null)
            dt = new Datetime(Datetime.YEAR, Datetime.DAY, yearToDay);

        return dt;
    }

    public static Datetime toYM(Datetime yearToMinute) {
        if (yearToMinute != null && (yearToMinute.getStartCode() != Datetime.YEAR || yearToMinute.getEndCode() != Datetime.MINUTE))
            yearToMinute = toYM(yearToMinute.getDate());
        return yearToMinute;
    }

    public static Datetime toYM(Date yearToMinute) {
        Datetime dt;

        dt = null;
        if (yearToMinute != null)
            dt = new Datetime(Datetime.YEAR, Datetime.MINUTE, yearToMinute);

        return dt;
    }

    public static Datetime toYS(Datetime yearToSecond) {
        if (yearToSecond != null && (yearToSecond.getStartCode() != Datetime.YEAR || yearToSecond.getEndCode() != Datetime.SECOND))
            yearToSecond = toYS(yearToSecond.getDate());
        return yearToSecond;
    }

    public static Datetime toYS(Date yearToSecond) {
        Datetime dt;

        dt = null;
        if (yearToSecond != null)
            dt = new Datetime(Datetime.YEAR, Datetime.SECOND, yearToSecond);

        return dt;
    }

    public static Datetime toHM(Datetime hourToMinute) {
        if (hourToMinute != null && (hourToMinute.getStartCode() != Datetime.HOUR || hourToMinute.getEndCode() != Datetime.MINUTE))
            hourToMinute = toHM(hourToMinute.getDate());

        return hourToMinute;
    }

    public static Datetime toHM(Date hourToMinute) {
        Datetime dt;

        dt = null;
        if (hourToMinute != null)
            dt = new Datetime(Datetime.HOUR, Datetime.MINUTE, hourToMinute);

        return dt;
    }
    
    public static Date toDate(Datetime datetime) {
        if (datetime != null)
            return datetime.getDate();

        return null;
    }
    
    /**
     * Compares the two parameters to see if they are different
     * 
     * @return true if object is the same; otherwise false
     */
    public static boolean isDifferent(Object a, Object b) {
        if (a instanceof String && b instanceof String)
            return !((String)a).trim().equals(((String)b).trim());
        return (a == null && b != null) || (a != null && !a.equals(b));
    }

    public static boolean isDifferentYD(Date a, Date b) {
        return isDifferent(toYD(a), toYD(b));
    }

    public static boolean isDifferentYD(Date a, Datetime b) {
        return isDifferent(toYD(a), toYD(b));
    }

    public static boolean isDifferentYD(Datetime a, Date b) {
        return isDifferent(toYD(a), toYD(b));
    }

    public static boolean isDifferentYD(Datetime a, Datetime b) {
        return isDifferent(toYD(a), toYD(b));
    }

    public static boolean isDifferentYM(Date a, Date b) {
        return isDifferent(toYM(a), toYM(b));
    }

    public static boolean isDifferentYM(Date a, Datetime b) {
        return isDifferent(toYM(a), toYM(b));
    }

    public static boolean isDifferentYM(Datetime a, Date b) {
        return isDifferent(toYM(a), toYM(b));
    }

    public static boolean isDifferentYM(Datetime a, Datetime b) {
        return isDifferent(toYM(a), toYM(b));
    }

    public static boolean isDifferentYS(Date a, Date b) {
        return isDifferent(toYS(a), toYS(b));
    }

    public static boolean isDifferentYS(Date a, Datetime b) {
        return isDifferent(toYS(a), toYS(b));
    }

    public static boolean isDifferentYS(Datetime a, Date b) {
        return isDifferent(toYS(a), toYS(b));
    }

    public static boolean isDifferentYS(Datetime a, Datetime b) {
        return isDifferent(toYS(a), toYS(b));
    }

    public static boolean isDifferentHM(Date a, Date b) {
        return isDifferent(toHM(a), toHM(b));
    }

    public static boolean isDifferentHM(Date a, Datetime b) {
        return isDifferent(toHM(a), toHM(b));
    }

    public static boolean isDifferentHM(Datetime a, Date b) {
        return isDifferent(toHM(a), toHM(b));
    }

    public static boolean isDifferentDT(Datetime a, Datetime b) {
        return isDifferent(toHM(a), toHM(b));
    }
    
    public static Double getPercentHoldingUsed(Datetime startedDate, Datetime collectionDate,
                                                Datetime collectionTime, Integer timeHolding) {
        Long hour, diff;
        Double numHrs;
        Date scd;
        Datetime stdt;
        
        if (collectionDate == null) 
            return 0.0;
        
        scd = collectionDate.getDate();
        if (collectionTime == null) {
            scd.setHours(0);
            scd.setMinutes(0);
        } else {
            scd.setHours(collectionTime.getDate().getHours());
            scd.setMinutes(collectionTime.getDate().getMinutes());
        }
        
        stdt = startedDate != null ? startedDate : Datetime.getInstance();
        diff = (stdt.getDate().getTime() - scd.getTime());
        hour = 3600000L;
        numHrs = diff.doubleValue() / hour.doubleValue();

        return (numHrs / timeHolding.doubleValue()) * 100;        
    }
    
    public static Double getPercentExpectedCompletion(Datetime collectionDate, Datetime collectionTime,
                                                      Datetime receivedDate, Integer priority,
                                                      Integer timeTaAverage) {
        Long day, diff;
        Double numDays, factor;
        Date scd;
        Datetime now, begin;

        if (collectionDate == null && receivedDate == null)
            return 0.0;

        if (collectionDate != null) {
            scd = collectionDate.getDate();
            if (collectionTime == null) {
                scd.setHours(0);
                scd.setMinutes(0);
            } else {
                scd.setHours(collectionTime.getDate().getHours());
                scd.setMinutes(collectionTime.getDate().getMinutes());
            }
            begin = collectionDate;
        } else {
            begin = receivedDate;
        }

        now = Datetime.getInstance();
        diff = now.getDate().getTime() - begin.getDate().getTime();
        day = 86400000L;
        numDays = diff.doubleValue() / day.doubleValue();

        factor = priority != null ? priority.doubleValue() : timeTaAverage.doubleValue();

        return (numDays / factor) * 100;
    }

    /*
     * Compute the number of days before the analysis is expected to be finshed
     */
    public static Integer getDueDays(Datetime received, Integer expectedDays) {
        long     due;
        Datetime now, expectedDate;
        
        if (received == null || expectedDays == null)
            return null;
        
        now = Datetime.getInstance(Datetime.YEAR, Datetime.MINUTE);
        
        expectedDate = received.add(expectedDays);
        
        due = expectedDate.getDate().getTime() - now.getDate().getTime();
        
        // convert from milliseconds to days
        due = due / 1000 / 60 / 60 / 24;
        
        return (int)due;
    }
    
    /*
     * Compute the Datetime after which the sample is no longer viable for analysis
     */
    public static Datetime getExpireDate(Datetime collectionDate, Datetime collectionTime, int holdingHours) {
        Date tempDate;
        Datetime expireDate;
        
        expireDate = null;
        if (collectionDate != null) {
            tempDate = collectionDate.getDate();
            if (collectionTime != null) {
                tempDate.setHours(collectionTime.get(Datetime.HOUR));
                tempDate.setMinutes(collectionTime.get(Datetime.MINUTE));
            } else {
                tempDate.setHours(0);
                tempDate.setMinutes(0);
            }
            tempDate.setTime(tempDate.getTime() + holdingHours * 60 * 60 * 1000);
            expireDate = new Datetime(Datetime.YEAR, Datetime.MINUTE, tempDate);
        }

        return expireDate;
    }

    /**
     * Compares the two parameters to see if they are the same
     * @return true if object is the same; otherwise false
     */
    public static boolean isSame(Object a, Object b) {
        return a != null && a.equals(b);
    }

    /**
     * Checks the parameter to see if its null or its length is 0.
     * 
     * @return true if object is empty; otherwise false
     */
    public static boolean isEmpty(Object a) {
        if (a instanceof String)
            return ((String)a).length() == 0;
        return a == null;
    }
    
    /**
     * Compares to see if the first date is after the second date 
     *
     * @return true if first date is after the second date
     */
    public static boolean isAfter(Datetime a, Datetime b) {
        return a != null && a.after(b);
    }

    /*
     * For paged result list, this method returns a subList of the query list
     * starting at first for max number of results.
     */
    public static <T> ArrayList<T> subList(List<T> query, int first, int max) {
        int to;
        Iterator<T> e;
        ArrayList<T> list;

        if (query == null || query.isEmpty() || first >= query.size())
            return null;

        to = Math.min(first + max, query.size());
        list = new ArrayList<T>(to - first);
        e = query.listIterator(first);
        for (; first < to; first++ )
            list.add(e.next());

        return list;
    }

    /**
     * Convert a List to ArrayList
     */
    public static <T> ArrayList<T> toArrayList(List<T> from) {
        if (from instanceof ArrayList)
            return (ArrayList<T>)from;
        return new ArrayList<T>(from);
    }  

    /**
     * Convenience methods to unwrap and merge error lists
     */
    public static void mergeException(ValidationErrorsList list, Exception e) {
        int i;
        ArrayList<Exception> el;
        FieldErrorException fe;

        if (e instanceof ValidationErrorsList) {
            el = ((ValidationErrorsList)e).getErrorList();
            for (i = 0; i < el.size(); i++ )
                mergeException(list, el.get(i));
        } else if (e instanceof FieldErrorException) {
            fe = (FieldErrorException)e;
            if (! isEmpty(fe.getFieldName()))
                list.add(fe);
            else
                list.add(new FormErrorException(fe.getKey(), fe.getParams()));
        } else if (e instanceof FormErrorException) {
            list.add(e);
        } else {
            list.add(new DatabaseException(e.getMessage()));
        }
    }

    public static void mergeException(ValidationErrorsList list, Exception e, String table, int row) {
        int i;
        ArrayList<Exception> el;
        FieldErrorException fe;

        if (e instanceof ValidationErrorsList) {
            el = ((ValidationErrorsList)e).getErrorList();
            for (i = 0; i < el.size(); i++ )
                mergeException(list, el.get(i), table, row);
        } else if (e instanceof FieldErrorException) {
            fe = (FieldErrorException)e;
            list.add(new TableFieldErrorException(fe.getKey(), row, fe.getFieldName(), table,
                                                  fe.getParams()));
        } else if (e instanceof FormErrorException) {
            list.add(e);
        } else {
            list.add(new DatabaseException(e.getMessage()));
        }
    }

    public static void mergeException(ValidationErrorsList list, Exception e, String table,
                                      int key1, int key2) {
        int i;
        ArrayList<Exception> el;
        FieldErrorException fe;

        if (e instanceof ValidationErrorsList) {
            el = ((ValidationErrorsList)e).getErrorList();
            for (i = 0; i < el.size(); i++ )
                mergeException(list, el.get(i), table, key1, key2);
        } else if (e instanceof FieldErrorException) {
            fe = (FieldErrorException)e;
            list.add(new GridFieldErrorException(fe.getKey(), key1, key2, fe.getFieldName(), table,
                                                 fe.getParams()));
        } else if (e instanceof FormErrorException) {
            list.add(e);
        } else {
            list.add(new DatabaseException(e.getMessage()));
        }
    }
    
    public static String toString(Object o) {
        if (o != null)
            return o.toString();
        return "";            
    }
    
    /**
     * Concats two strings together. Null parameters are ignored.
     */
    public static String concat(Object a, Object b) {
        StringBuffer buf;

        buf = new StringBuffer();
        if (a != null)
            buf.append(a.toString().trim());
        if (b != null)
            buf.append(b.toString().trim());

        return buf.toString();
    }

    /**
     * Concats two strings together with the specified delimiter. Null
     * parameters are ignored and the delimiter is not used.
     */
    public static String concatWithSeparator(Object a, Object delimiter, Object b) {
        StringBuffer buf;

        buf = new StringBuffer();
        if (a != null)
            buf.append(a.toString().trim());
        if (b != null) {
            if (a != null)
                buf.append(delimiter);
            buf.append(b.toString().trim());
        }
        return buf.toString();
    }

    /**
     * Concats a list of objects together using delimiter.
     */
    public static String concatWithSeparator(List<?> list, Object delimiter) {
        StringBuffer buf;

        buf = new StringBuffer();
        for (Object i : list) {
            if (i != null) {
                if (buf.length() > 0)
                    buf.append(delimiter);
                buf.append(i.toString().trim());
            }
        }
        return buf.toString();
    }
    
    public static ArrayList<Exception> getExceptions(ArrayList<Exception> formErrors) {
        ArrayList<Exception> errors = new ArrayList<Exception>();
        
        for(Exception le : formErrors) {
            if(le instanceof FormErrorWarning)
                errors.add(new org.openelis.ui.common.FormErrorWarning(le.getMessage()));
            else
                errors.add(new Exception(le.getMessage()));
        }
        
        return errors;
        
    } 
}