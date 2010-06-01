package org.openelis.gwt.widget;

import org.openelis.gwt.screen.TabHandler;

import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.HasValue;

public interface ScreenWidgetInt<T> extends HasBlurHandlers, HasFocusHandlers, HasValueChangeHandlers<T>, HasValue<T>,  HasExceptions {

    /**
     * Enables/Disables the screen widget. Disabled screen widgets will not
     * allow input.
     */
    public void setEnabled(boolean enabled);

    /**
     * Returns the state of screen widget.
     * 
     * @return TRUE if the screen widget is enabled, FALSE otherwise.
     */
    public boolean isEnabled();

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
     * Sets a Helper instance for this widget.
     */
    public void setHelper(WidgetHelper<T> helper);

    /**
     * Method for the screen to validate widget values without require the user
     * to visit the widget.
     */
    public void validateValue();

    /**
     * Adds a TabHandler to the widget to override the default browser tabing
     * and use the tab order defined in the ScreenDef
     * 
     * @param handler
     */
    public void addTabHandler(TabHandler handler);

    public void addFocusStyle(String style);
    
    public void removeFocusStyle(String style);
}
