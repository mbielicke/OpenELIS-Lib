package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    protected Logger logger = Logger.getLogger("Widget");

    /**
     * The Constructor now sets the wrapped GWT TextBox as the element widget of
     * this composite and adds an anonymous ValueCahngeHandler to handle input
     * from the user.
     */
    public TextBox() {
        init();
    }

    public void init() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.init()");
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
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.init()");
    }

    // ************** Methods for TextBox attributes ***********************

    /**
     * This method is overwritten to implement case management. Use the
     * setValue/getValue methods for normal screen use.
     */
    public String getText() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.getText()");
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
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setText() : value = "+text);
        textbox.setText(text);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setText()");
    }
    
    /**
     * This method is overridden to make sure the Case style is applied to the widget  
     */
    @Override
    public void setStyleName(String style) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setStyle() : value = "+style);
    	super.setStyleName(style);
    	setCase(this.textCase == null ? TextBox.Case.MIXED : this.textCase);
    	logger.finest("Exiting org.openelis.gwt.widget.TextBox.setStyle()");
    }

    /**
     * Set the text case for input.
     */
    public void setCase(TextBox.Case textCase) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setCase() : value = "+textCase);
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
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setCase()");
    }

    /**
     * Sets the maximum input characters allowed for this text field.
     */
    public void setMaxLength(int maxLength) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setMaxLengh() : value = "+maxLength);
        this.maxLength = maxLength;
        textbox.setMaxLength(maxLength);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setMaxLength()");
    }

    /**
     * Set the text alignment.
     */
    public void setTextAlignment(TextAlignment alignment) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setTextAlignment() : value = "+alignment);
        this.alignment = alignment;
        textbox.setAlignment(alignment);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setTextAlignment()");
    }

    /**
     * Adds an input mask to the textbox
     * 
     * @param mask
     */
    public void setMask(String mask) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setMask() : value = "+mask);
        // If a maskHandler is in force already, call suspend to remove the old
        // handler from
        // listening until it can be cleaned up by the browser garbage
        // collection.
        // May not be necessary
        if (maskHandler != null)
            maskHandler.suspend();

        maskHandler = new MaskHandler(mask);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setMask()");
    }

    /**
     * Method used to set if this widget is required to have a value inputed.
     * @param required
     */
    public void setRequired(boolean required) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setRequired() : value = "+required);
        this.required = required;
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setRequired()");
    }
    
    // ************** Implementation of ScreenWidgetInt ********************

    /**
     * Enables or disables the textbox for editing.
     */
    public void setEnabled(boolean enabled) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setEnabled() : value = "+enabled);
        this.enabled = enabled;
        textbox.setReadOnly( !enabled);
        /*
         * if ( !enabled) unsinkEvents(Event.KEYEVENTS); else
         * sinkEvents(Event.KEYEVENTS);
         */
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setEnabled()");
    }

    /**
     * Returns whether the text is enabled for editing
     */
    public boolean isEnabled() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.isEnabled() : value = "+enabled);
        return enabled;
    }

    public void addFocusStyle(String style) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.addFocusStyle() : value = "+style);
        textbox.addStyleName(style);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.addFocusStyle()");
    }

    public void removeFocusStyle(String style) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.removeFocusStyle() : value = "+style);
        textbox.removeStyleName(style);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.removeFocusStyle()");
    }

    // ******** Implementation of Queryable *****************
    /**
     * This method will toggle TextBox into and from query mode and suspend or
     * resume any format restrictions
     */
    public void setQueryMode(boolean query) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setQueryMode() : value = "+query);
        if (queryMode == query) {
        	logger.fine("org.openelis.gwt.widget.TextBox.setQueryMode() : "+((query ? "Already in query mode" : "Already in edit mode"))+", exiting");
            return;
        } else if (query) {
        	logger.fine("org.openelis.gwt.widget.TextBox.setQueryMode() : Entering query mode");
            queryMode = true;
            if (maskHandler != null)
                maskHandler.suspend();
            textbox.setMaxLength(255);
            textbox.setAlignment(TextAlignment.LEFT);
        } else {
        	logger.fine("org.openelis.gwt.widget.TextBox.setQueryMode() : Exiting query mode");
            queryMode = false;
            if (maskHandler != null)
                maskHandler.resume();
            textbox.setMaxLength(maxLength);
            textbox.setAlignment(TextAlignment.LEFT);
        }
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setQueryMode()");
    }

    /**
     * Returns a single QueryData object representing the query string entered
     * by the user. The Helper class is used here to create the correct
     * QueryData object for the passed type T.
     */
    public Object getQuery() {
    	Object query;
    	
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.getQuery()");
   
    	query = helper.getQuery(textbox.getText());
        
    	if(query != null)
        	logger.fine("org.openelis.gwt.widget.TextBox.getQuery() : value = "+((QueryData)query).getQuery());
        
    	logger.finest("Exiting org.openelis.gwt.widget.TextBox.getQuery()");
        
        return query;
    }
    
    /**
     * Sets a query string to this widget when loaded from a table model
     */
    public void setQuery(QueryData qd) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setQuery() : value = "+qd.getQuery());
        if(qd != null)
            textbox.setText(qd.getQuery());
        else
            textbox.setText("");
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setQuery()");
    }

    // ********** Implementation of HasHelper ***************************
    /**
     * Sets the implentation of the WidgetHelper<T> interface to be used by this
     * widget.
     */
    public void setHelper(WidgetHelper<T> helper) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setHelper())");
        this.helper = helper;
        logger.finest("Exiting org.openelis.gwt.widget.textBox.setHelper()");
    }

    /**
     * Returns the helper set for this widget
     */
    public WidgetHelper<T> getHelper() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.getHelper()");
        return helper;
    }

    // ************** Implementation of HasValue<T> interface ***************

    /**
     * Returns the current value for this widget.
     */
    public T getValue() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.getValue()");
    	logger.fine("org.openelis.gwt.widget.TextBox.getValue() : value = "+value);
        return value;
    }

    /**
     * Sets the current value of this widget without firing the
     * ValueChangeEvent.
     */
    public void setValue(T value) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setValue() : value = "+value);
    	if(!logger.isLoggable(Level.FINEST))
    		logger.finest("org.openelis.gwt.widget.TextBox.setValue() value = "+value);
        setValue(value, false);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setValue()");
    }

    /**
     * Sets the current value of this widget and will fire a ValueChangeEvent if
     * the value is different than what is currently stored.
     */
    public void setValue(T value, boolean fireEvents) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setValue() : value = "+value+", fireEvents = "+fireEvents);
        if(!Util.isDifferent(this.value, value)) {
        	logger.fine("org.openelis.gwt.widget.TextBox.setValue(T,boolean) : Value is not different, exiting");
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
        	logger.fine("org.openelis.gwt.widget.TextBox.setValue(T,boolean) : Firing ValueChangeEvent");
            ValueChangeEvent.fire(this, value);
        }
        
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setValue(T,boolean)");
    }

    /**
     * This method is made available so the Screen can on commit make sure all
     * required fields are entered without having the user visit each widget on
     * the screen.
     */
    public void validateValue() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.validateValue()");
        validateValue(false);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.validateValue()");
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
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.validateValue(boolean) : fireEvents = "+fireEvents);
        validateExceptions = null;
        try {
            setValue(helper.getValue(getText()), fireEvents);
            if (required && value == null) {
                addValidateException(new LocalizedException("gen.fieldRequiredException"));
                logger.fine("org.openelis.gwt.widget.TextBox.validateValue(boolean) : Field required Exception set");
            }
        } catch (LocalizedException e) {
        	logger.fine("org.openelis.gwt.widget.TextBox.validateValue(boolean) : "+e.getLocalizedMessage());
            addValidateException(e);
            setValue(null,fireEvents);
        }
        ExceptionHelper.checkExceptionHandlers(this);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.validateValue(boolean)");
    }

    /**
     * Method used to validate the inputed query string by the user.
     */
    public void validateQuery() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.validateQuery()");
        try {
            validateExceptions = null;
            helper.validateQuery(getText());
        } catch (LocalizedException e) {
        	logger.fine("org.openelis.gwt.widget.TextBox.validateQuery() : "+e.getLocalizedMessage());
            addValidateException(e);
        }
        ExceptionHelper.checkExceptionHandlers(this);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.validateQuery()");
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
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.hasExceptions()");
        return endUserExceptions != null || validateExceptions != null;
    }

    /**
     * Adds a manual Exception to the widgets exception list.
     */
    public void addException(LocalizedException error) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.addException(LocalizedException) : "+error.getLocalizedMessage());
        if (endUserExceptions == null)
            endUserExceptions = new ArrayList<LocalizedException>();
        endUserExceptions.add(error);
        ExceptionHelper.checkExceptionHandlers(this);
        logger.finest("Exiting org.openeleis.gwt.widget.TextBox.addException(LocalizedException)");
    }

    protected void addValidateException(LocalizedException error) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.addValidateException(LocalizedException) : "+error.getLocalizedMessage());
        if (validateExceptions == null)
            validateExceptions = new ArrayList<LocalizedException>();
        validateExceptions.add(error);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.addValidateException(LocalizedException)");
    }

    /**
     * Combines both exceptions list into a single list to be displayed on the
     * screen.
     */
    public ArrayList<LocalizedException> getValidateExceptions() {
    	logger.finest("Getting org.openelis.gwt.widget.TextBox.getValidateExceptions()");
        return validateExceptions;
    }

    public ArrayList<LocalizedException> getEndUserExceptions() {
    	logger.finest("Getting org.openelis.gwt.widget.TextBox.getEndUserExceptions()"); 
        return endUserExceptions;
    }

    /**
     * Clears all manual and validate exceptions from the widget.
     */
    public void clearExceptions() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.clearExceptions()");
        endUserExceptions = null;
        validateExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.clearExceptions()");
    }
    
    public void clearEndUserExceptions() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.clearEndeUserExceptions()");
        endUserExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.clearEndUserExcepitons()");
    }
    
    public void clearValidateExceptions() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.clearValidateExceptions()");
        validateExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.clearValidateExceptions()");
    }


    /**
     * Will add the style to the widget.
     */
    public void addExceptionStyle(String style) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.addExceptionStyle(String) : style = "+style);
        addStyleName(style);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.addExceptionStyle(String)");
    }

    /**
     * will remove the style from the widget
     */
    public void removeExceptionStyle(String style) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.removeExceptionStyle(String) : style = "+style);
        removeStyleName(style);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.removeExceptionStyle(String)");
    }

    // ************* Implementation of Focusable ******************

    /**
     * Method only implemented to satisfy Focusable interface.
     */
    public int getTabIndex() {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.getTabIndex()");
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
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setFocus(boolean) : focused = "+focused);
        textbox.setFocus(true);
        logger.finest("Exiting org.openelis.gwt.widget.TextBox.setFocus(boolean)");
    }

    // ************ Handler Registration methods *********************

    /**
     * The Screen will add its screenHandler here to register for the
     * onValueChangeEvent
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.addValueChangeHandler(handler)");
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * This Method is here so the Focus logic of ScreenPanel can be notified
     */
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.addBlurHandler(handler)");
        return addDomHandler(handler, BlurEvent.getType());
    }

    /**
     * This method is here so the Focus logic of ScreenPanel can be notified
     */
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.addFocusHandler(handler)");
        return addDomHandler(handler, FocusEvent.getType());
    }

    /**
     * Adds a mouseover handler to the textbox for displaying Exceptions
     */
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.addMouseOverHandler(handler)");
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    /**
     * Adds a MouseOut handler for hiding exceptions display
     */
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
    	logger.finest("Exiting org.openelis.gwt.widget.TextBox.addMouseOutHandler(handler)");
        return addDomHandler(handler, MouseOutEvent.getType());
    }
    
	public void setLogger(Logger logger) {
		logger.finest("Entering org.openelis.gwt.widget.TextBox.setLogger(Logger)");
		this.logger = logger;
		logger.finest("Exiting org.openelis.gwt.widget.TextBox.setLogger(Logger)");
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
