package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface ScreenWidgetInt<T> {

    /**
     * Returns the state of screen widget.
     * 
     * @return TRUE if the screen widget is enabled, FALSE otherwise.
     */
    public boolean isEnabled();

    /**
     * Enables/Disables the screen widget. Disabled screen widgets will not
     * allow input.
     */
    public void setEnabled(boolean enabled);

    /**
     * The following methods get/set the contents of the widget, ie. the string
     * in a TextBox widget. Note that the ScreenWidget interface returns a
     * specific type rather than always returning a string.
     */
    public T getValue();

    public void setValue(T value);

    /**
     * Enables/disables the query mode mode on a widget. The effects of query
     * mode is dependent on the type of widget.
     */
    public void setQueryMode(boolean enabled);

    /**
     * Returns either a query data object or a array list of query data objects
     * that represents the query clause for ScreenWidget.
     */
    public Object getQuery();

    /**
     * Sets/gets a validator f
     */
    public void setValidator(ScreenWidgetInt<T> validator);

    public ScreenWidgetInt<T> getValidator();

    public void validate();

    public void addException(LocalizedException exception);

    public ArrayList<LocalizedException> getExceptions();

    public void clearExceptions();

    public void addExceptionStyle(String style);

    public void removeExceptionStyle(String style);

    public HandlerRegistration addFieldValueChangeHandler(ValueChangeHandler<T> handler);
}
