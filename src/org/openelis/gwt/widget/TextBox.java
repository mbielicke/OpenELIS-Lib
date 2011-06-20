package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.HashSet;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

/**
 * This class is used by OpenELIS Screens to display and input values in forms
 * and in table cells. TextBox is parameterized to handle different data types
 * with the help of an implementation of WidgetHelper<T>.
 * 
 * @author tschmidt
 * 
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class TextBox<T> extends Composite implements ScreenWidgetInt, 
                                                     Queryable, 
                                                     Focusable,
                                                     HasBlurHandlers, 
                                                     HasFocusHandlers,
                                                     HasValue<T>, 
                                                     HasHelper<T>,
                                                     HasExceptions {

    /**
     * Wrapped GWT TextBox
     */
    protected com.google.gwt.user.client.ui.TextBox textbox;

    /**
     * Textbox attributes
     */
    protected int                                   maxLength = 255;
    protected TextAlignment                         alignment = TextAlignment.LEFT;
    protected Case                                  textCase  = Case.MIXED;

    /**
     * Exceptions list
     */
    protected ArrayList<LocalizedException>         endUserExceptions, validateExceptions;

    /**
     * Data moved from Field to the widget
     */
    protected boolean                               queryMode, required,enabled;
    protected T                                     value;

    /**
     * This class replaces the functionality that Field used to provide but now
     * in a static way.
     */
    protected WidgetHelper<T>                       helper    = (WidgetHelper<T>)new StringHelper();

    /**
     * Reference to an Implementation of the private class MaskHandler.
     */
    protected MaskHandler                           maskHandler;

    public enum Case {
        MIXED, UPPER, LOWER
    };

    /**
     * The Constructor now sets the wrapped GWT TextBox as the element widget of
     * this composite and adds an anonymous ValueCahngeHandler to handle input
     * from the user.
     */
    public TextBox() {
        init();
    }

    public void init() {
        textbox = new com.google.gwt.user.client.ui.TextBox();
        setEnabled(false);
        textbox.addValueChangeHandler(new ValueChangeHandler<String>() {
            /*
             * This event calls validate(true) so that that the valueChangeEvent
             * for the HasValue<T> interface will be fired. In Query mode it
             * will validate the query string through the helper class
             */
            public void onValueChange(ValueChangeEvent<String> event) {
                if (queryMode) {
                    validateQuery();
                } else
                    validateValue(true);
            }

        });
        initWidget(textbox);
    }

    // ************** Methods for TextBox attributes ***********************

    /**
     * This method is overwritten to implement case management. Use the
     * setValue/getValue methods for normal screen use.
     */
    public String getText() {
        switch (textCase) {
            case UPPER:
                return textbox.getText().toUpperCase();
            case LOWER:
                return textbox.getText().toLowerCase();
            default:
                return textbox.getText();
        }
    }
    
    public void setText(String text) {
        textbox.setText(text);
    }
    
    /**
     * This method is overridden to make sure the Case style is applied to the widget  
     */
    @Override
    public void setStyleName(String style) {
    	super.setStyleName(style);
    	setCase(this.textCase == null ? TextBox.Case.MIXED : this.textCase);
    }

    /**
     * Set the text case for input.
     */
    public void setCase(TextBox.Case textCase) {
        this.textCase = textCase;

        switch (textCase) {
            case UPPER:
                textbox.addStyleName("Upper");
                textbox.removeStyleName("Lower");
                break;
            case LOWER:
                textbox.addStyleName("Lower");
                textbox.removeStyleName("Upper");
                break;
            default:
                textbox.removeStyleName("Upper");
                textbox.removeStyleName("Lower");
        }
    }

    /**
     * Sets the maximum input characters allowed for this text field.
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        textbox.setMaxLength(maxLength);
    }

    /**
     * Set the text alignment.
     */
    public void setTextAlignment(TextAlignment alignment) {
        this.alignment = alignment;
        textbox.setAlignment(alignment);
    }

    /**
     * Adds an input mask to the textbox
     * 
     * @param mask
     */
    public void setMask(String mask) {

        // If a maskHandler is in force already, call suspend to remove the old
        // handler from
        // listening until it can be cleaned up by the browser garbage
        // collection.
        // May not be necessary
        if (maskHandler != null)
            maskHandler.suspend();

        maskHandler = new MaskHandler(mask);
    }

    /**
     * Method used to set if this widget is required to have a value inputed.
     * @param required
     */
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    // ************** Implementation of ScreenWidgetInt ********************

    /**
     * Enables or disables the textbox for editing.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        textbox.setReadOnly( !enabled);
        /*
         * if ( !enabled) unsinkEvents(Event.KEYEVENTS); else
         * sinkEvents(Event.KEYEVENTS);
         */
    }

    /**
     * Returns whether the text is enabled for editing
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void addFocusStyle(String style) {
        textbox.addStyleName(style);
    }

    public void removeFocusStyle(String style) {
        textbox.removeStyleName(style);
    }

    // ******** Implementation of Queryable *****************
    /**
     * This method will toggle TextBox into and from query mode and suspend or
     * resume any format restrictions
     */
    public void setQueryMode(boolean query) {
        if (queryMode == query) {
            return;
        } else if (query) {
            queryMode = true;
            if (maskHandler != null)
                maskHandler.suspend();
            textbox.setMaxLength(255);
            textbox.setAlignment(TextAlignment.LEFT);
        } else {
            queryMode = false;
            if (maskHandler != null)
                maskHandler.resume();
            textbox.setMaxLength(maxLength);
            textbox.setAlignment(TextAlignment.LEFT);
        }
    }

    /**
     * Returns a single QueryData object representing the query string entered
     * by the user. The Helper class is used here to create the correct
     * QueryData object for the passed type T.
     */
    public Object getQuery() {
        return helper.getQuery(textbox.getText());
    }
    
    /**
     * Sets a query string to this widget when loaded from a table model
     */
    public void setQuery(QueryData qd) {
        if(qd != null)
            textbox.setText(qd.getQuery());
        else
            textbox.setText("");
    }

    // ********** Implementation of HasHelper ***************************
    /**
     * Sets the implentation of the WidgetHelper<T> interface to be used by this
     * widget.
     */
    public void setHelper(WidgetHelper<T> helper) {
        this.helper = helper;
    }

    /**
     * Returns the helper set for this widget
     */
    public WidgetHelper<T> getHelper() {
        return helper;
    }

    // ************** Implementation of HasValue<T> interface ***************

    /**
     * Returns the current value for this widget.
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the current value of this widget without firing the
     * ValueChangeEvent.
     */
    public void setValue(T value) {
        setValue(value, false);
    }

    /**
     * Sets the current value of this widget and will fire a ValueChangeEvent if
     * the value is different than what is currently stored.
     */
    public void setValue(T value, boolean fireEvents) {

        if(!Util.isDifferent(this.value, value)) {
        	if(value != null)
        		textbox.setText(helper.format(value));
            return;
        }
        
        this.value = value;
        if (value != null) {
            textbox.setText(helper.format(value));
        } else {
            textbox.setText("");
        }

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    /**
     * This method is made available so the Screen can on commit make sure all
     * required fields are entered without having the user visit each widget on
     * the screen.
     */
    public void validateValue() {
        validateValue(false);
    }

    /**
     * This method will call the Helper to get the T value from the entered
     * string input. if invalid input is entered, Helper is expected to throw an
     * en exception and that exception will be added to the validate exceptions
     * list.
     * 
     * @param fireEvents
     */
    protected void validateValue(boolean fireEvents) {
        validateExceptions = null;
        try {
            setValue(helper.getValue(getText()), fireEvents);
            if (required && value == null)
                addValidateException(new LocalizedException("gen.fieldRequiredException"));
        } catch (LocalizedException e) {
            addValidateException(e);
            setValue(null,fireEvents);
        }
        ExceptionHelper.checkExceptionHandlers(this);
    }

    /**
     * Method used to validate the inputed query string by the user.
     */
    public void validateQuery() {
        try {
            validateExceptions = null;
            helper.validateQuery(getText());
        } catch (LocalizedException e) {
            addValidateException(e);
        }
        ExceptionHelper.checkExceptionHandlers(this);
    }

    // ********** Implementation of HasException interface ***************
    /**
     * Convenience method to check if a widget has exceptions so we do not need
     * to go through the cost of merging the logical and validation exceptions
     * in the getExceptions method.
     * 
     * @return
     */
    public boolean hasExceptions() {
        return endUserExceptions != null || validateExceptions != null;
    }

    /**
     * Adds a manual Exception to the widgets exception list.
     */
    public void addException(LocalizedException error) {
        if (endUserExceptions == null)
            endUserExceptions = new ArrayList<LocalizedException>();
        endUserExceptions.add(error);
        ExceptionHelper.checkExceptionHandlers(this);
    }

    protected void addValidateException(LocalizedException error) {
        if (validateExceptions == null)
            validateExceptions = new ArrayList<LocalizedException>();
        validateExceptions.add(error);
    }

    /**
     * Combines both exceptions list into a single list to be displayed on the
     * screen.
     */
    public ArrayList<LocalizedException> getValidateExceptions() {
        return validateExceptions;
    }

    public ArrayList<LocalizedException> getEndUserExceptions() {
        return endUserExceptions;
    }

    /**
     * Clears all manual and validate exceptions from the widget.
     */
    public void clearExceptions() {
        endUserExceptions = null;
        validateExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
    }
    
    public void clearEndUserExceptions() {
        endUserExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
    }
    
    public void clearValidateExceptions() {
        validateExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
    }


    /**
     * Will add the style to the widget.
     */
    public void addExceptionStyle(String style) {
        addStyleName(style);
    }

    /**
     * will remove the style from the widget
     */
    public void removeExceptionStyle(String style) {
        removeStyleName(style);
    }

    // ************* Implementation of Focusable ******************

    /**
     * Method only implemented to satisfy Focusable interface.
     */
    public int getTabIndex() {
        return -1;
    }

    /**
     * Method only implemented to satisfy Focusable interface.
     */
    public void setTabIndex(int index) {

    }

    /**
     * Method only implemented to satisfy Focusable interface.
     */
    public void setAccessKey(char key) {

    }

    /**
     * This is need for Focusable interface and to allow programmatic setting of
     * focus to this widget. We use the wrapped TextBox to make this work.
     */
    public void setFocus(boolean focused) {
        textbox.setFocus(true);
    }

    // ************ Handler Registration methods *********************

    /**
     * The Screen will add its screenHandler here to register for the
     * onValueChangeEvent
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * This Method is here so the Focus logic of ScreenPanel can be notified
     */
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return addDomHandler(handler, BlurEvent.getType());
    }

    /**
     * This method is here so the Focus logic of ScreenPanel can be notified
     */
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return addDomHandler(handler, FocusEvent.getType());
    }

    /**
     * Adds a mouseover handler to the textbox for displaying Exceptions
     */
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    /**
     * Adds a MouseOut handler for hiding exceptions display
     */
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    // *************** Class to manage Mask handling *********************

    /**
     * MaskHandler is a private class implementation of basically what was
     * MaskListener. An instance of this class is only created if setMask is
     * called on the Textbox
     * 
     * @author tschmidt
     * 
     */
    private class MaskHandler implements KeyDownHandler, KeyUpHandler, BlurHandler {

        protected HashSet<String>   literals = new HashSet<String>();
        protected ArrayList<String> masks    = new ArrayList<String>();
        protected String            mask;

        /**
         * References to the HandlerRegistrations so that the mask enforcement
         * can be suspended for queryMode.
         */
        private HandlerRegistration keyUpHandler;
        private HandlerRegistration keyDownHandler;
        private HandlerRegistration blurHandler;

        /**
         * This method sets the mask format for the widget. The string uses the
         * following chars for formatting:
         * 
         * Z - Zero suppression 9 - Character must be a number X - Character can
         * be anything A - Character must be a letter
         * 
         * Any other Characters will be used as literals
         * 
         * @param mask
         */
        public MaskHandler(String mask) {
        	masks.add("9");
        	masks.add("Z");
        	masks.add("X");
            this.mask = mask;
            for (int i = 0; i < mask.length(); i++ ) {
                if ( !masks.contains(String.valueOf(mask.charAt(i))))
                    literals.add(String.valueOf(mask.charAt(i)));
            }
            resume();
        }

        /**
         * Call this method to format the text.
         */
        protected void format() {
            if (textbox.getText().equals(""))
                return;
            char[] chars = textbox.getText().toCharArray();
            String text = "";
            int i = 0;
            boolean end = false;
            while (text.length() < mask.length()) {
                if (i < chars.length) {
                    if (literals.contains(String.valueOf(chars[i])) &&
                        String.valueOf(chars[i])
                              .equals(String.valueOf(text.charAt(text.length() - 1))))
                        i++ ;
                    text += String.valueOf(chars[i]);
                } else
                    end = true;
                text = applyMask(text, end);
                i++ ;
            }
            textbox.setText(text);
        }

        protected String applyMask(String text, boolean end) {
            if ("".equals(text))
                return "";
            String retText = "";
            String input = String.valueOf(text.charAt(text.length() - 1));
            String maskChar = String.valueOf(mask.charAt(text.length() - 1));
            while (text.length() == 1 && literals.contains(maskChar)) {
                retText += maskChar;
                maskChar = String.valueOf(mask.charAt(retText.length()));
            }
            retText += text;
            if (literals.contains(String.valueOf(input)) || end) {
                int li = text.length();
                if ( !end) {
                    while (li < mask.length() && !input.equals(String.valueOf(mask.charAt(li))))
                        li++ ;
                } else {
                    while (li < mask.length() &&
                           !literals.contains(String.valueOf(mask.charAt(li))))
                        li++ ;
                }
                if (li == mask.length() && !end) {
                    retText = (text.substring(0, text.length() - 1));
                    return retText;
                } else if (li < mask.length() && end) {
                    text += String.valueOf(mask.charAt(li));
                }
                int ss = li - 1;
                while (ss > 0 && !literals.contains(String.valueOf(mask.charAt(ss - 1))))
                    ss-- ;
                int inl = 0;
                if (ss != text.length()) {
                    int is = text.length() - 1;
                    if (literals.contains(String.valueOf(text.charAt(is))))
                        is-- ;
                    int iL = 0;
                    while (is >= 0 && !literals.contains(String.valueOf(text.charAt(is)))) {
                        iL++ ;
                        is-- ;
                    }
                    inl = (li - ss) - iL;
                } else {
                    inl = (li - ss);
                }
                for (int i = 0; i < inl; i++ ) {
                    if (mask.charAt(i + ss) == '9')
                        text = text.substring(0, ss) + "0" + text.substring(ss, text.length());
                    else
                        text = text.substring(0, ss) + '\ufeff' + text.substring(ss, text.length());
                }
                retText = text;
                return retText;
            }
            if (checkMask(input, maskChar)) {
                if (text.length() == mask.length())
                    return text;
                while (literals.contains(String.valueOf(mask.charAt(retText.length())))) {
                    retText += String.valueOf(mask.charAt(retText.length()));
                }
                if (input.equals("0") && maskChar.equals("Z"))
                    retText = text.substring(0, text.length() - 1) + " ";
            } else {
                retText = text.substring(0, text.length() - 1);
            }
            return retText;
        }

        protected boolean checkMask(String input, String maskChar) {
            if (maskChar.equals("9") || maskChar.equals("A")) {
                try {
                    Integer.parseInt(input);
                } catch (Exception e) {
                    if (maskChar.equals("9"))
                        return false;
                }
                if (mask.equals("A"))
                    return false;
            }
            return true;
        }

        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                if (literals.contains(String.valueOf(textbox.getText().charAt(
                                                                              textbox.getText()
                                                                                     .length() - 1))))
                    textbox.setText(textbox.getText().substring(0, textbox.getText().length() - 1));
            }
        }

        public void onKeyUp(KeyUpEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE ||
                event.getNativeKeyCode() == KeyCodes.KEY_SHIFT) {
                return;
            }
            String text = textbox.getText();
            if (text.length() > mask.length()) {
                textbox.setText(text.substring(0, text.length() - 1));
                return;
            }
            textbox.setText(applyMask(text, false));
        }

        public void onBlur(BlurEvent event) {
            if (textbox.isReadOnly())
                return;
            format();
        }

        public void suspend() {
            keyDownHandler.removeHandler();
            keyUpHandler.removeHandler();
            blurHandler.removeHandler();
        }

        public void resume() {
            keyUpHandler = textbox.addKeyUpHandler(this);
            keyDownHandler = textbox.addKeyDownHandler(this);
            blurHandler = textbox.addBlurHandler(this);
        }

    }

}
