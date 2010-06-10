package org.openelis.gwt.widget;

public interface Enable {
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
}
