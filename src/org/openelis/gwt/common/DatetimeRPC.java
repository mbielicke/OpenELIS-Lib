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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.common;

import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

/**
 * A simple date & time class that mimics some of the Informix-Database datetime
 * functionality.
 */
public class DatetimeRPC implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Field numbers indicating start and end codes for datetime field.
     */
    public static final byte YEAR = 0, MONTH = 1, DAY = 2, HOUR = 3,
                    MINUTE = 4, SECOND = 5, FRACTION = 6;
    protected byte startCode, endCode;
    protected Date date;
    protected boolean parsed;
    protected final static String[][] YMD = { {"yyyy",
                                               "yyyy'-'MM",
                                               "yyyy'-'MM'-'dd",
                                               "yyyy-MM-dd HH",
                                               "yyyy-MM-dd HH:mm",
                                               "yyyy-MM-dd HH:mm:ss",
                                               "yyyy-MM-dd HH:mm:ss.SSS"},
                                             {"",
                                              "MM",
                                              "MM-dd",
                                              "MM-dd HH",
                                              "MM-dd HH:mm",
                                              "MM-dd HH:mm:ss",
                                              "MM-dd HH:mm:ss.SSS"},
                                             {"",
                                              "",
                                              "dd",
                                              "dd HH",
                                              "dd HH:mm",
                                              "dd HH:mm:ss",
                                              "dd HH:mm:ss.SSS"},
                                             {"",
                                              "",
                                              "",
                                              "HH",
                                              "HH:mm",
                                              "HH:mm:ss",
                                              "HH:mm:ss.SSS"},
                                             {"",
                                              "",
                                              "",
                                              "",
                                              "mm",
                                              "mm:ss",
                                              "mm:ss.SSS"},
                                             {"",
                                              "",
                                              "",
                                              "",
                                              "",
                                              "ss",
                                              "ss.SSS"},
                                             {"", "", "", "", "", "", "SSS"}};

    /**
     * Constructs a Datetime with specified range (start and end) and date.
     */
    public DatetimeRPC() {

    }

    private void setDate(byte startCode, byte endCode, Date date) {
        //
        // check param ranges
        //
        if (startCode < YEAR || startCode > FRACTION)
            throw new IllegalArgumentException("Invalid value for startCode");
        if (endCode < YEAR || endCode > FRACTION)
            throw new IllegalArgumentException("Invalid value for endCode");
        if (startCode > endCode)
            throw new IllegalArgumentException("startCode > endCode");
        this.startCode = startCode;
        this.endCode = endCode;
        this.date = date;
        parsed = false;
    }
    /**
     * Constructs a Datetime with specified range (start to end) and object.
     */
    public static DatetimeRPC getInstance(byte startCode, byte endCode, Object date) {
        DatetimeRPC retDate = new DatetimeRPC();
        Date newdate = new Date();
        newdate.setTime(((Date)date).getTime());
        retDate.setDate(startCode, endCode, newdate);
        retDate.parse();
        return retDate;
    }

    /**
     * Gets a datetime using default range (YEAR to SECOND) and current date.
     */
    public static DatetimeRPC getInstance() {
        DatetimeRPC dt = new DatetimeRPC();
        dt.setDate(YEAR,SECOND, new Date());
        return dt;
    }

    /**
     * Gets a datetime using specified range (start to end) and current date.
     */
    public static DatetimeRPC getInstance(byte startCode, byte endCode) {
        DatetimeRPC dt = new DatetimeRPC();
        dt.setDate(startCode, endCode, new Date());
        return dt;
    }

    /**
     * Returns the Date associated with this datetime field.
     */
    public Date getDate() {
        parse();
        return date;
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
     * Returns true if the current value of this Datetime is after the value of
     * Datetime when; false otherwise.
     */
    public boolean after(DatetimeRPC when) {
        boolean ok = false;
        if (when != null && date != null) {
            parse();
            when.parse();
            if (date.getTime() > when.date.getTime())
                ok = true;
        }
        return ok;
    }

    /**
     * Returns true if the current value of this Datetime is before the value of
     * Datetime when; false otherwise.
     */
    public boolean before(DatetimeRPC when) {
        boolean ok = false;
        if (when != null && date != null) {
            parse();
            when.parse();
            if (date.getTime() < when.date.getTime())
                ok = true;
        }
        return ok;
    }

    /**
     * Returns true if the current value of this Datetime is equal the value of
     * Datetime when; false otherwise.
     */
    public boolean equals(Object obj) {
        DatetimeRPC when = (DatetimeRPC)obj;
        boolean ok = false;
        if (when != null && date != null) {
            parse();
            when.parse();
            if (date.getTime() == when.date.getTime())
                ok = true;
        }
        return ok;
    }


    /**
     * Returns a new Datetime object with the specified field incremented
     * (+amount) or decremented (-amount). This implementation uses
     * Calendar.add() method to provide this functionality.
     * 
     * @see java.util.Calendar
     */
    public DatetimeRPC add(int days) {
        DatetimeRPC datetime = null;
        if (date != null) {
            parse();
            Date cal = date;
            long newTime = cal.getTime() + ((long)days * 86400000);
            cal.setTime(newTime);
            datetime = new DatetimeRPC();
            datetime.setDate(startCode, endCode, cal);
        }
        return datetime;
    }


    /**
     * Returns a String representation of this Datetime object.
     */
    public String toString() {
        return toString(YMD[startCode][endCode]);
    }

    /**
     * Returns a String representation of this Datetime object with the
     * specified format
     */
    public String toString(String format) {
        DateTimeFormat simpleFormat = DateTimeFormat.getFormat(format);
        parse();
        return simpleFormat.format(date);
    }

    //
    // private
    //
    protected void parse() {
        if (parsed || date == null)
            return;
        switch (startCode) {
            case FRACTION:
                date.setSeconds(0);
            case SECOND:
                date.setMinutes(0);
            case MINUTE:
                date.setHours(0);
            case HOUR:
                date.setDate(0);
            case DAY:
                date.setMonth(0);
            case MONTH:
                date.setYear(1);
            default:
                break;
        }
        switch (endCode) {
            case YEAR:
                date.setMonth(0);
            case MONTH:
                date.setDate(1);
            case DAY:
                date.setHours(0);
            case HOUR:
                date.setMinutes(0);
            case MINUTE:
                date.setSeconds(0);
            default:
                break;
        }
        parsed = true;
    }
}
