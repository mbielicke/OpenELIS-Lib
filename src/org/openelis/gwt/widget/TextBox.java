package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.TabHandler;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;

public class TextBox<T> extends com.google.gwt.user.client.ui.TextBox implements
                                                                     ScreenWidgetInt<T> {

    protected int               length      = 255;
    protected boolean           enforceMask = true, enforceLength = true, autoNext = false,
                                enabled;
    protected String            mask;
    protected TextAlignConstant alignment   = TextBox.ALIGN_LEFT;
    protected Field<T>          field;

    protected Case              textCase    = Case.MIXED;

    public enum Case {
        MIXED, UPPER, LOWER
    };

    public TextBox() {
    }
    

    /**
     * Set the text case for input.
     */
    public void setCase(Case textCase) {
        this.textCase = textCase;

        switch (textCase) {
            case UPPER:
                addStyleName("Upper");
                removeStyleName("Lower");
                break;
            case LOWER:
                addStyleName("Lower");
                removeStyleName("Upper");
                break;
            default:
                removeStyleName("Upper");
                removeStyleName("Lower");
        }
    }

    /**
     * Sets the maximum input characters allowed for this text field.
     */
    public void setMaxLength(int length) {
        this.length = length;
        super.setMaxLength(length);
    }

    /**
     * Sets the mask for this input field. 
     * @see MaskListener 
     */
    public void setMask(String mask) {
        this.mask = mask;
        new MaskListener(this, mask);
        setMaxLength(mask.length());
    }

    /**
     * Set the text alignment. 
     */
    public void setTextAlignment(TextAlignConstant alignment) {
        this.alignment = alignment;
        super.setTextAlignment(alignment);
    }

    public void addException(LocalizedException error) {
    }

    public void clearExceptions() {
    }

    public void setQueryMode(boolean query) {
        if (field.queryMode == query) {
            return;
        } else if (query) {
            field.setQueryMode(true);
            enforceMask = false;
            super.setMaxLength(255);
            super.setTextAlignment(TextBox.ALIGN_LEFT);
        } else {
            field.setQueryMode(false);
            enforceMask = true;
            super.setMaxLength(length);
            super.setTextAlignment(TextBox.ALIGN_LEFT);
        }
    }

    public Object getQuery() {
        if ( !field.queryMode)
            return null;
    
        return field.getQuery();
    }

    public ArrayList<LocalizedException> getExceptions() {
    }

    public void setEnabled(boolean enabled) {
        setReadOnly( !enabled);
        /*if ( !enabled)
            unsinkEvents(Event.KEYEVENTS);
        else
            sinkEvents(Event.KEYEVENTS); */
    }

    public boolean isEnabled() {
        return !isReadOnly();
    }

    public T getValue() {
        return field.getValue();
    }

    public void setValue(T value) {
        field.setValue(value);
        if (value != null)
            setText(field.format());
        else
            setText("");
    }

    public HandlerRegistration addFieldValueChangeHandler(ValueChangeHandler<T> handler) {
        return field.addValueChangeHandler(handler);
    }

    public void addExceptionStyle(String style) {
        addStyleName(style);
    }

    public Object getWidgetValue() {
        return getText();
    }

    public void removeExceptionStyle(String style) {
        removeStyleName(style);
    }

    public void addTabHandler(TabHandler handler) {
        addDomHandler(handler, KeyPressEvent.getType());
    }

    /**
     * This method is overwritten to implement case management. Use the
     * setValue/getValue methods for normal screen use.
     */
    public String getText() {
        switch (textCase) {
            case UPPER:
                return super.getText().toUpperCase();
            case LOWER:
                return super.getText().toLowerCase();
            default:
                return super.getText();
        }
    }
}
