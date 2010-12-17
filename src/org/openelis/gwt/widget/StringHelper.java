package org.openelis.gwt.widget;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;

/**
 * This class is used by ScreenWidgets that implement HasValue<String> to
 * provide methods for formatting, validating and query by String values.
 * 
 * @author tschmidt
 * 
 */
public class StringHelper implements WidgetHelper<String> {


    /**
     * Public no arg constructor
     */
    public StringHelper() {

    }

    /**
     * Will return a new QueryData object using the passed query string and and
     * the setting the type to QueryData.Type.STRING.
     */
    public QueryData getQuery(String input) {
        // Do nothing and return if passed null or empty string
        if (input == null || "".equals(input))
            return null;

        return new QueryData(QueryData.Type.STRING,input);
    }

    /**
     * Returns null if empty string or null value passed. Otherwise will echo
     * back the input parameter.
     */
    public String getValue(String input) throws LocalizedException {

        // If null or empty string return value as null;
        if (input == null || "".equals(input)) 
            return null;

        return input;
    }

    /**
     * This method will ensure the passed query string is in the correct format
     * and that the query params are all valid double values.
     */
    public void validateQuery(String input) throws LocalizedException {
        QueryFieldUtil qField;

        // If null or empty string do nothing and return.
        if (input == null || input.equals(""))
            return;

        // Parse query and if invalid set exception and return right away.
        qField = new QueryFieldUtil();

        qField.parse(input);
    }

    /**
     * This method will return a string value for display applying any
     * formatting if needed.
     */
    public String format(String value) {

        if (value == null)
            return "";

        return value;
    }

}
