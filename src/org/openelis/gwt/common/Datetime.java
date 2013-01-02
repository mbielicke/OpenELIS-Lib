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

import java.io.Serializable;
import java.util.Date;

/**
 * This class is used to manage date and date & time values. Because the class is
 * used in all 3 tiers, we can not use Java Calendar object therefore some of the
 * basic functionality is duplicated from the Calendar class. Additionally, because  
 * of time zone issues and the client/server issues, the date fields are managed in
 * separate int fields rather than a date field. 
 */
public class Datetime implements Serializable, Comparable<Datetime> {

    private static final long serialVersionUID = 1L;
    /**
     * Field numbers indicating start and end codes for datetime field.
     */
    public static final byte  YEAR = 0, MONTH = 1, DAY = 2, HOUR = 3, MINUTE = 4,
                              SECOND = 5, FRACTION = 6;
    private byte              startCode, endCode;
    private Date              timestamp;
    private int               year, month, date = -1;

    /**
     * Provided solely for GWT RPC Serialization
     */
    public Datetime() {
    }

    /**
     * Constructs a Datetime with specified range (start to end) and object.
     */
    public Datetime(byte startCode, byte endCode, Date date) {
        setDate(startCode, endCode, date);
    }

    public static Datetime getInstance(byte startCode, byte endCode, Date date) {
        if (date == null)
            return null;
        return new Datetime(startCode, endCode, date);
    }

    /**
     * Gets a datetime using default range (YEAR to SECOND) and current date.
     */
    public static Datetime getInstance() {
        return new Datetime(YEAR, SECOND, new Date());
    }

    /**
     * Gets a datetime using specified range (start to end) and current date.
     */
    public static Datetime getInstance(byte startCode, byte endCode) {
        return new Datetime(startCode, endCode, new Date());
    }

    /**
     * Returns the Date associated with this datetime field.
     */
    @SuppressWarnings("deprecation")
	public Date getDate() {
        if (endCode < HOUR)
            return new Date(year, month, date);
        return timestamp;
    }

    /**
     * Returns the starting field code of datetime.
     */
    public byte getStartCode() {
        return startCode;
    }

    /**
     * Returns the ending field code of datetime.
     */
    public byte getEndCode() {
        return endCode;
    }

    /**
     * Returns true if the current value of this Datetime is before the value of
     * Datetime when; false otherwise.
     */
    public boolean before(Datetime when) {
        return before(when.getDate());
    }

    @SuppressWarnings("deprecation")
	public boolean before(Date when) {
        if (endCode < HOUR)
            return new Date(year, month, date).before(when);

        return timestamp.before(when);
    }

    /**
     * Returns true if the current value of this Datetime is after the value of
     * Datetime when; false otherwise.
     */
    public boolean after(Datetime when) {
        return after(when.getDate());
    }

    @SuppressWarnings("deprecation")
	public boolean after(Date when) {
        if (endCode < HOUR) {
            return new Date(year, month, date).after(when);
        }
        return timestamp.after(when);
    }

    /**
     * Returns true if the current value of this Datetime is equal the value of
     * Datetime when; false otherwise.
     */
    @SuppressWarnings("deprecation")
	public boolean equals(Object obj) {
        Date compDate;

        if (obj instanceof Datetime)
            compDate = ((Datetime)obj).getDate();
        else if (obj instanceof Date)
            compDate = (Date)obj;
        else
            return false;

        if (endCode < HOUR)
            return new Date(year, month, date).equals(compDate);

        return timestamp.equals(compDate);
    }

    /**
     * Returns a new Datetime object with the specified field incremented
     * (+amount) or decremented (-amount). This implementation uses
     * Calendar.add() method to provide this functionality.
     * 
     * @see java.util.Calendar
     */
    @SuppressWarnings("deprecation")
	public Datetime add(int days) {
    	Date cal;
    	
    	cal = (Date)getDate().clone();
    	cal.setDate(cal.getDate()+days);
    	return Datetime.getInstance(startCode,endCode,cal);
    }

    /**
     * Comparable interface implementation.
     */
    public int compareTo(Datetime when) {
        if (after(when))
            return 1;
        if (before(when))
            return -1;
        return 0;
    }

    /**
     * Returns the portion of datetime requested 
     */
    @SuppressWarnings("deprecation")
	public int get(byte field) {
        switch (field) {
            case YEAR:
                if (endCode < HOUR)
                    return year;
                else
                    return timestamp.getYear();
            case MONTH:
                if (endCode < HOUR)
                    return month;
                else
                    return timestamp.getMonth();
            case DAY:
                if (endCode < HOUR)
                    return date;
                else
                    return timestamp.getDate();
            case HOUR:
                if (endCode < DAY)
                    return 0;
                else
                    return timestamp.getHours();
            case MINUTE:
                if (endCode < DAY)
                    return 0;
                else
                    return timestamp.getMinutes();
            case SECOND:
                if (endCode < DAY)
                    return 0;
                else
                    return timestamp.getSeconds();
        }
        return -1;
    }

    /**
     * Convenience method for debugging
     */
    public String toString() {
        if (endCode < HOUR)
            return (year + 1900) + "-" + (month + 1) + "-" + date;

        return timestamp.toString();
    }

    @SuppressWarnings("deprecation")
	protected void parse() {
        /*
         * Clear out values past set endCode
         */
        switch(endCode) {
            case YEAR :
                year = timestamp.getYear();
                month = -1;
                date = -1;
                timestamp = null;
                break;
            case MONTH :
                year = timestamp.getYear();
                month = timestamp.getMonth();
                date = -1;
                timestamp = null;
                break;
            case DAY :
                year = timestamp.getYear();
                month = timestamp.getMonth();
                date =  timestamp.getDate();
                timestamp = null;
                break;
            case HOUR :
                timestamp.setMinutes(0);
            case MINUTE :
                timestamp.setSeconds(0);
        }
        
        /*
         * Clear out values before set startCode
         */
        switch(startCode) {
                case FRACTION :
                    timestamp.setSeconds(0);
                case SECOND :
                    timestamp.setMinutes(0);
                case MINUTE :
                    timestamp.setHours(0);
                case HOUR :
                    timestamp.setDate(0);
                case DAY :
                    timestamp.setMonth(0);
                case MONTH :
                    timestamp.setYear(0);
        }
    }

    private void setDate(byte startCode, byte endCode, Date date) {
        if (startCode < YEAR || startCode > FRACTION)
            throw new IllegalArgumentException("Invalid value for startCode");
        if (endCode < YEAR || endCode > FRACTION)
            throw new IllegalArgumentException("Invalid value for endCode");
        if (startCode > endCode)
            throw new IllegalArgumentException("startCode > endCode");

        this.startCode = startCode;
        this.endCode = endCode;
        this.timestamp = date;
        parse();
    }
}
