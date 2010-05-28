package org.openelis.gwt.widget;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * This class is used by ScreenWidgets that implement HasValue<Integer> to
 * provide methods for formatting, validating and query by Integer values. Since
 * this class has no state and can be used by multiple widgets through the
 * WidgetHelper<T> interface a Singleton pattern is used instead of static class
 * methods.
 * 
 * @author tschmidt
 * 
 */
public class IntegerHelper implements WidgetHelper<Integer> {

    /**
     * Widget value attributes
     */
    protected boolean required;
    protected String  pattern;

	/**
	 * Public no arg constructor
	 */
	public IntegerHelper() {

	}

	/**
	 * Will return a new QueryData object using the passed query string and and
	 * the setting the type to QueryData.Type.INTEGER.
	 */
	public QueryData getQuery(String input) {
		QueryData qd;

		// Do nothing and return if passed null or empty string
		if (input == null || "".equals(input))
			return null;

		qd = new QueryData();
		qd.query = input;
		qd.type = QueryData.Type.INTEGER;

		return qd;

	}

	/**
	 * Will parse the input value from string to a Integer and return if
	 * successful otherwise an InvalidInteger exception will be thrown to the
	 * widget.
	 */
	public Integer getValue(String input) throws LocalizedException {

		// If null or empty string return value as null;
		if (input == null || "".equals(input))
			return null;

		// Try and parse the input regardless of pattern.
		try {
			return Integer.valueOf(input);
		} catch (Exception e) {
			// Try it again if a pattern is present. If the input doesn't match the pattern,
			// the NumberFormat will throw the exception.
			if (pattern != null) {
				try {
					return Double.valueOf(NumberFormat.getFormat(pattern).parse(input)).intValue();
				} catch (Exception e2) {
					// Do nothing to Fall through and throw the exception.
				}
			}
			throw new LocalizedException("invalidInteger");
		}
	}

	/**
	 * This method will ensure the passed query string is in the correct format
	 * and that the query params are all valid integer values. A list of
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
		
		qField.parse(input);
		/*
		// Loop through query params and make sure each is a valid double value
		for (String param : qField.parameter) {
			String[] vals = param.split("\\.\\.");
			for (int i = 0; i < vals.length; i++) {
				if (!vals[i].equalsIgnoreCase("null")) {
					try {
						Double.parseDouble(vals[i]);
					} catch (Exception e) {
						if (exceptions == null)
							exceptions = new ArrayList<LocalizedException>();
						exceptions.add(new LocalizedException("invalidDouble"));
					}
				}
			}
		}
  		*/
	}

	/**
	 * This method will return a string value for display applying any
	 * formatting if needed.
	 */
	public String format(Integer value) {

		if (value == null)
			return "";

		if (pattern != null)
			return NumberFormat.getFormat(pattern).format(value);

		return value.toString();
	}
	
	/**
	 * Boolean flag indicating if a value is required by the widget.
	 * @param required
	 */
	public void setRequired(boolean required) {
	    this.required = required;
	}
	
	/**
	 * Sets the Formatting pattern to be used when displaying the 
	 * widgets value on the screen.
	 * @param pattern
	 */
	public void setPattern(String pattern) {
	    this.pattern = pattern;
	}

}
