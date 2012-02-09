package org.openelis.gwt.widget;

import java.util.Date;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * This class is used by ScreenWidgets that implement HasValue<Date> to
 * provide methods for formatting, validating and query by Date values. 
 * 
 * @author tschmidt
 * 
 */
public class DateHelper implements WidgetHelper<Datetime> {
    
    /**
     * Widget value attributes
     */
    protected String  pattern;
    protected byte    begin, end;
    
    /**
     * Public no arg constructor;
     */
    public DateHelper() {

    }
    
    /**
     * Will return a new QueryData object using the passed query string and and
     * the setting the type to QueryData.Type.DATE.
     */
    public QueryData getQuery(String input) {

        // Do nothing and return if passed null or empty string
        if (input == null || "".equals(input))
            return null;

        return new QueryData(QueryData.Type.DATE,input);
    }

    /**
     * Will parse the input value from string to a Datetime and return if
     * successful otherwise an InvalidDate exception will be thrown to the
     * widget.
     */
    public Datetime getValue(String input) throws LocalizedException {
        Date date;
        
        // If null or empty string return value as null;
        if (input == null || "".equals(input))
            return null;

                
        try {
            date =  DateTimeFormat.getFormat(pattern).parseStrict(input);
        }catch(Exception e) {
            throw new LocalizedException("invalidDateFormat");
        }
        
        return Datetime.getInstance(begin,end,date);
    }

    /**
     * This method will ensure the passed query string is in the correct format
     * and that the query params are all valid date values. 
     */
    public void validateQuery(String input) throws LocalizedException {
        QueryFieldUtil qField;

        // If null or empty string do nothing and return.
        if (input == null || input.equals(""))
            return;

        // Parse query and if invalid set exception and return right away.
        qField = new QueryFieldUtil();
        
        qField.parseDate(input, pattern);

    }

    /**
     * This method will return a string value for display applying any
     * formatting if needed.
     */
    public String format(Datetime value) {
   
        if(value == null)
            return "";
        
        if(pattern != null) 
            return DateTimeFormat.getFormat(pattern).format(value.getDate());
        
        return value.toString();
        
    }
    
    /**
     * Sets the Formatting pattern to be used when displaying the 
     * widgets value on the screen.
     * @param pattern
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Sets the begin precision for this Datetime to be returned by getValue()
     * @param begin
     */
    public void setBegin(byte begin) {
        this.begin = begin;
    }
    
    /**
     * Returns the begin precision used by this helper
     * @return
     */
    public byte getBegin() {
    	return begin;
    }
    
    /**
     * Sets the end precision for this Datetime to be returned by getValue()
     * @param begin
     */
    public void setEnd(byte end) {
        this.end = end;
    }
    
    /**
     * Returns the end precision used by this helper
     * @return
     */
    public byte getEnd() {
    	return end;
    }

}
