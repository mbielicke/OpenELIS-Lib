package org.openelis.gwt.widget;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;

public interface WidgetHelper<T> {

    /**
     * Will create and return the correct QueryData object based on the passed
     * type T. if passed null or empty string the a null value will be returned.
     * 
     * @param query
     *        - Is the entered query string by the user
     * @return
     */
    public QueryData getQuery(String input);

    /**
     * This method is used to take raw input entered in a widget such as textbox
     * and to parse into the correct passed typed T.
     * 
     * @param input
     *        - String inputed by the user.
     * @param pattern
     *        - Any formatting pattern that is set in the widget so the value
     *        can be parsed correctly if used.
     * @return
     * @throws LocalizedException
     */
    public T getValue(String input) throws LocalizedException;

    /**
     * This method will take a query string entered by a user, parse it to make
     * sure it is invalid query format and also make sure all parameters entered
     * are of the the passed type T.
     * 
     * @param query
     * @return
     */
    public void validateQuery(String input) throws LocalizedException;

    /**
     * Formats the value the of the passed type T to a string. Applies any
     * formating supplied by the pattern parameter.
     * 
     * @param value
     * @param Pattern
     * @return
     */
    public String format(T value);

}
