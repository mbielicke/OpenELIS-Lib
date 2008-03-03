package org.openelis.util;

import org.apache.log4j.Category;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple date & time class that mimics some of the Informix-Database datetime
 * functionality.
 */
public class Datetime implements java.io.Serializable {
    private static Category log = Category.getInstance(Datetime.class.getName());
    /**
     * Field numbers indicating start and end codes for datetime field.
     */
    public static final byte YEAR = 0, MONTH = 1, DAY = 2, HOUR = 3,
                    MINUTE = 4, SECOND = 5, FRACTION = 6;
    protected byte startCode, endCode;
    protected Date date;
    protected boolean parsed;
    protected final static String[][] YMD = { {"yyyy",
                                               "yyyy-MM",
                                               "yyyy-MM-dd",
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
    public Datetime(byte startCode, byte endCode, Date date) {
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
    public Datetime(byte startCode, byte endCode, Object date) {
        this(startCode, endCode, (Date)null);
        SimpleDateFormat format = new SimpleDateFormat();
        format.setLenient(false);
        format.applyLocalizedPattern(YMD[startCode][endCode]);
        try {
            this.date = format.parse(date.toString());
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Invalid Date");
        }
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
    public boolean after(Datetime when) {
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
    public boolean before(Datetime when) {
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
        Datetime when = (Datetime)obj;
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
     * Returns a Datetime object with specified range (start to end). Empty
     * fields are initialized with their default value.
     */
    public Datetime extend(byte startCode, byte endCode) {
        return new Datetime(startCode, endCode, date);
    }

    /**
     * Returns a new Datetime object with the specified field incremented
     * (+amount) or decremented (-amount). This implementation uses
     * Calendar.add() method to provide this functionality.
     * 
     * @see java.util.Calendar
     */
    public Datetime add(byte field, int amount) {
        Calendar cal;
        Datetime datetime = null;
        if (date != null) {
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
            datetime = new Datetime(startCode, endCode, cal.getTime());
        }
        return datetime;
    }

    /**
     * Returns a new Datetime object with the specified field rolled up or down
     * This implementation uses Calendar.roll() method to provide this
     * functionality.
     * 
     * @see java.util.Calendar
     */
    public Datetime roll(byte field, boolean up) {
        Calendar cal;
        Datetime datetime = null;
        if (date != null) {
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
            datetime = new Datetime(startCode, endCode, cal.getTime());
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
        SimpleDateFormat simpleFormat;
        parse();
        simpleFormat = new SimpleDateFormat(format);
        return simpleFormat.format(date);
    }

    //
    // private
    //
    protected void parse() {
        Calendar format;
        if (parsed || date == null)
            return;
        format = Calendar.getInstance();
        format.setTime(date);
        switch (startCode) {
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
        switch (endCode) {
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
