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
     * Mask to be applied.
     */
    protected String                                mask;

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
                	applyMask();
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
    	if(this.mask == null) {
    		textbox.addKeyUpHandler(new KeyUpHandler() {
    			public void onKeyUp(KeyUpEvent event) {
    				if(event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE || event.getNativeKeyCode() == KeyCodes.KEY_DELETE)
    					return;
				
    				applyMask();
    			}
    		});
    	}
    	this.mask = mask;

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
            textbox.setMaxLength(255);
            textbox.setAlignment(TextAlignment.LEFT);
        } else {
        	logger.fine("org.openelis.gwt.widget.TextBox.setQueryMode() : Exiting query mode");
            queryMode = false;
            textbox.setMaxLength(maxLength);
            textbox.setAlignment(TextAlignment.LEFT);
            textbox.setText("");
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
    	logger.finest("Entering org.openelis.gwt.widget.TextBox.setQuery()");
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
        
        applyMask();

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

	private void applyMask() {
		String input;
		StringBuffer applied;
		char mc;
		int pos;
		boolean loop;
		
		if(mask == null || mask.equals("") || queryMode)
			return;
		
		applied = new StringBuffer();
		input = textbox.getText();
		pos = 0;
		/*
		 * Loop through input applying mask chars when needed
		 */
		for(char in : input.toCharArray()) {
			if(pos >= mask.length())
				break;
			
			mc = mask.charAt(pos);
		   
			do {
		    	loop = false;
		    	switch(mc) {
		    		case '9' :					
		    			if(Character.isDigit(in)) {  
		    				applied.append(in);
		    				pos++;
		    			}else if(isNextLiteral(in,pos)) {
		    				applied.insert(applied.length()-1,"0");
		    				mc = mask.charAt(++pos);
		    				loop = true;
		    			}
		    			break;
		    		case 'X' :
		    			if(Character.isLetterOrDigit(in)) {  
		    				applied.append(in);
		    				pos++;
		    			}
		    			break;
		    		default :
		    			applied.append(mc);
		    			pos++;
		    			if(mc != in) {
		    				mc = mask.charAt(pos);
		    				loop = true;
		    			}
		    	}
			} while(loop && pos < mask.length());
		}
		
		/*
		 *	Check if Literal characters need to be added to the end of the string 
		 */
		if(pos < mask.length()) {
			mc = mask.charAt(pos);
			while(mc != 'X' && mc != '9') {
				applied.append(mc);
				mc = mask.charAt(++pos);
			}
		}
		
		
		textbox.setText(applied.toString());
	}
	
	private boolean isNextLiteral(char in, int pos) {
		char mc;
				
		mc = mask.charAt(pos);
		while(mc == 'X' || mc == '9') { 
			pos++;
			if(pos >= mask.length())
				break;
			mc = mask.charAt(pos);
		}
		
		return pos < mask.length() && mc == in;
	}

}
