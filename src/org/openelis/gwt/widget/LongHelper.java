package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * This class is used by ScreenWidgets that implement HasValue<Long> to
 * provide methods for formatting, validating and query by Long values.
 *  
 */
public class LongHelper implements WidgetHelper<Long> {

    /**
     * Widget value attributes
     */
    protected String  pattern;
    
	/**
	 * Public no arg constructor
	 */
	public LongHelper() {

	}

	/**
	 * Will return a new QueryData object using the passed query string and and
	 * the setting the type to QueryData.Type.INTEGER
	 */
	public QueryData getQuery(String input) {
		// Do nothing and return if passed null or empty string
		if (input == null || "".equals(input))
			return null;

		return new QueryData(QueryData.Type.INTEGER,input);
	}

	/**
	 * Will parse the input value from string to a Long and return if
	 * successful otherwise an InvalidInteger exception will be thrown to the
	 * widget.
	 */
	public Long getValue(String input)
			throws LocalizedException {

		// If null or empty string return value as null;
		if (input == null || "".equals(input))
			return null;

		// Try and parse the input regardless of pattern.
		try {
			return Long.valueOf(input);
		} catch (Exception e) {
			// Try it again if a pattern is present. If the input doesn't match the pattern,
			// the NumberFormat will throw the exception.
			if (pattern != null) {
				try {
					return new Long(Double.valueOf(NumberFormat.getFormat(pattern).parse(input)).intValue());
				} catch (Exception e2) {
					// Do nothing to Fall through and throw the exception.
				}
			}
			throw new LocalizedException("exc.invalidNumeric");
		}
	}

	/**
	 * This method will ensure the passed query string is in the correct format
	 * and that the query params are all valid long values. A list of
	 * exceptions will be returned if any or null will be returned if
	 * successful.
	 */
	public void validateQuery(String input) throws LocalizedException {
		QueryFieldUtil qField;

		// If null or empty string do nothing and return.
		if (input == null || input.equals(""))
			return;

		// Parse query and if invalid set exception and return right away.
		qField = new QueryFieldUtil();
		
		qField.parseLong(input);
	}

	/**
	 * This method will return a string value for display applying any
	 * formatting if needed.
	 */
	public String format(Long value) {

		if (value == null)
			return "";

		if (pattern != null)
			return NumberFormat.getFormat(pattern).format(value);

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

	@Override
	public boolean isCorrectType(Object value) {
		return value == null || value instanceof Long;
	}
	
	public ArrayList<LocalizedException> validate(Object value) {
		ArrayList<LocalizedException> exceptions = new ArrayList<LocalizedException>();
		
		if(!isCorrectType(value)) 
			exceptions.add(new LocalizedException("exc.invalidNumeric"));
		
		return exceptions;
	}

}
