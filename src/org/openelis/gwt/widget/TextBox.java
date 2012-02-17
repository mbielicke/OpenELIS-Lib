package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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
    protected boolean                               queryMode, required,enabled,enforceMask;
    protected T                                     value;

    /**
     * This class replaces the functionality that Field used to provide but now
     * in a static way.
     */
    protected WidgetHelper<T>                       helper    = (WidgetHelper<T>)new StringHelper();

    public enum Case {
        MIXED, UPPER, LOWER
    };
    
    protected String                                picture;
    
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
                if (queryMode) 
                    validateQuery();
                else
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
            enforceMask = false;
            textbox.setMaxLength(255);
            textbox.setAlignment(TextAlignment.LEFT);
        } else {
            queryMode = false;
            enforceMask = true;
            textbox.setMaxLength(maxLength);
            textbox.setAlignment(TextAlignment.LEFT);
            textbox.setText("");
        }
    }

    /**
     * Returns a single QueryData object representing the query string entered
     * by the user. The Helper class is used here to create the correct
     * QueryData object for the passed type T.
     */
    public Object getQuery() {
    	Object query;
    	
    	query = helper.getQuery(textbox.getText());
                
        return query;
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
    
    /**
     * Method used to determine if widget is currently in Query mode
     */
    public boolean isQueryMode() {
    	return queryMode;
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
    	
    	if(enforceMask && picture.equals(value)) {
    		value = null;
    		textbox.setText("");
    	}
    	
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

        if (fireEvents) 
            ValueChangeEvent.fire(this, value);
        
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
    	String text;
    	
    	text = getText();
    	
    	if(enforceMask && picture.equals(text)) {
    		text = "";
    		textbox.setText("");
    	}
    		
    	validateExceptions = null;
        
    	try {
            setValue(helper.getValue(text), fireEvents);
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
    
    public void setMask(final String mask) {
    	StringBuffer pic;
    	setMaxLength(mask.length());
    	enforceMask = true;
    	
    	pic = new StringBuffer();
    	for(char mc : mask.toCharArray()) {
    		switch (mc) {
    			case '9' :
    			case 'X' :
    				pic.append(" ");
    				break;
    			default :
    				pic.append(mc);
    		}
    	}
    	
    	picture = pic.toString();
		/*
		 * Delete and BackSpace keys are handled in KeyDown because Chrome and IE do not 
		 * pass these keys to the KeyPressEvent.  
		 */
		textbox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				String input;
				char mc;
				int cursor,selectStart,selectEnd;
				StringBuffer applied;
				
				/*
				 * If return if mask is not be enforced such as when in Query Mode;
				 */
				if(!enforceMask)
		            return;
				
				input = textbox.getText();  // Current state of the Textbox including selection.
				
				cursor = textbox.getCursorPos(); //Current position of cursor when key was pressed.
				
				/*
				 * If backspace or delete key is hit we want to blank out the current positon and return
				 * if backspacing or deleting a mask literal the literal will be re-asserted and the text
				 * will not change 
				 */
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE || 
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DELETE) {
					
					
					/*
					 * If part of the text is selected we want to blank out the selection preserving any mask literals 
					 * and the current length of length of textbox input. 
					 */
					if(textbox.getSelectionLength() > 0) {
						applied = new StringBuffer();
						
						selectStart = textbox.getText().indexOf(textbox.getSelectedText());  // Start position of selection
						selectEnd = selectStart + textbox.getSelectionLength();              // End positon of selection.
											
						applied.append(input.substring(0, selectStart));  // Copy the start of the input up to the start of selection into buffer.
						
						/*
						 * Loop through the selected portion and either blank out or insert mask literals
						 */
						for(int i = 0; i < textbox.getSelectionLength(); i++) {
							if(mask.toCharArray()[applied.length()] == '9' || mask.toCharArray()[applied.length()] == 'X')
								applied.append(" ");
							else
								applied.append(mask.toCharArray()[applied.length()]);
						}
						
						applied.append(input.substring(selectEnd));    // Copy the portion of input after selection in to the buffer
											
						input = applied.toString();                    // Set input to the buffer so that the inputed char can be inserted
						
						cursor = selectStart;                          // Set the Cursor to beginning of selection since this is where we want start
					}
					
					applied = new StringBuffer();
					
					/*
					 * Subtract 1 from cursor if backspace and then can be treated like delete
					 */
					if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE)
						cursor--;
					
					if(cursor < 0)
						return;
					
					mc = mask.charAt(cursor);  // get current mask char based on cursor

					/*
					 * if mask position is not a literal we will remove the char
					 * and replace with blank.  If it is a literal we want to just echo 
					 * what is in the textbox.
					 */
					if(mc == '9' || mc == 'X') {
						applied.append(input.substring(0,cursor));
						applied.append(" ");
						applied.append(input.substring(cursor+1));
					}else
						applied.append(input);  
					
					/*
					 * Set new Text and cursor position into widget
					 */
					textbox.setText(applied.toString());
					textbox.setCursorPos(cursor);
					
					/*
					 * KeyPressEvent occurs before the browser applies changes to the textbox.
					 * We stop propogation and defualt since we already set the changes we wanted
					 * other wise the typed char would be repeated
					 */
					event.preventDefault();
					event.stopPropagation();
					
				}
			}
		});
		
		/*
		 * Masks are applied in all browsers in KeyPressEvent because it is the only method that will allow us to
		 * view the typed character before it being applied to the textbox itself. 
		 */
		textbox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				String input;
				int cursor,selectStart,selectEnd;
				char ch,mc;
				StringBuffer applied;
				
				boolean loop;
				
				/*
				 * If return if mask is not be enforced such as when in Query Mode;
				 */
				if(!enforceMask)
		            return;
				
				input = textbox.getText();  // Current state of the Textbox including selection.
				
				cursor = textbox.getCursorPos(); //Current position of cursor when key was pressed.
				
				/*
				 * If part of the text is selected we want to blank out the selection preserving any mask literals 
				 * and the current length of length of textbox input. 
				 */
				if(textbox.getSelectionLength() > 0) {
					applied = new StringBuffer();
					
					selectStart = textbox.getText().indexOf(textbox.getSelectedText());  // Start position of selection
					selectEnd = selectStart + textbox.getSelectionLength();              // End positon of selection.
										
					applied.append(input.substring(0, selectStart));  // Copy the start of the input up to the start of selection into buffer.
					
					/*
					 * Loop through the selected portion and either blank out or insert mask literals
					 */
					for(int i = 0; i < textbox.getSelectionLength(); i++) {
						if(mask.toCharArray()[applied.length()] == '9' || mask.toCharArray()[applied.length()] == 'X')
							applied.append(" ");
						else
							applied.append(mask.toCharArray()[applied.length()]);
					}
					
					applied.append(input.substring(selectEnd));    // Copy the portion of input after selection in to the buffer
										
					input = applied.toString();                    // Set input to the buffer so that the inputed char can be inserted
					
					cursor = selectStart;                          // Set the Cursor to beginning of selection since this is where we want start
				}
				
				applied = new StringBuffer();                      // Get new Buffer
				
				/*
				 * Do nothing if navigating or tabing out of box
				 */
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB ||
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_LEFT ||
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_RIGHT ||
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DELETE ||
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE)
					return;
				

				ch = event.getCharCode();   // character typed by user

				applied.append(input.substring(0,cursor));  // Copy the portion of input up to the cursor to the buffer

				if(applied.length() >= mask.length())
					return;
				
				mc = mask.charAt(applied.length());  // Get the Mask char for the position typed

				/*
				 * Perfrom switch at least once possibly more if literals are to be inserted
				 */
				do {
					loop = false;
					switch(mc) {
					case '9' : 
						if(Character.isDigit(ch))    // if input matches add to buffer 
							applied.append(ch);
						else
							cursor--;               // Decrement cursor and through input if not right type
						break;
					case 'X' :
						if(Character.isLetterOrDigit(ch)) // if input matches add to buffer 
							applied.append(ch);
						else
							cursor--;               // Decrement cursor and through input if not right type
						break;
					default :
						applied.append(mc);         // Apply literal from mask always in this case

						/*
						 * if inputed char does not match literal then we 
						 * want to loop again to try and match and apply the input to the 
						 * next position in the mask
						 */
						if(mc != ch) {
							loop = true;
							mc = mask.charAt(applied.length());
							cursor++;  
						}
					}
				}while (loop);

				cursor++;  // Advance cursor

				/*
				 * If cursor is not at end of input copy the the rest of input to buffer
				 */
				if(cursor < input.length())
					applied.append(input.substring(cursor));
				
				/*
				 * Set new Text and cursor position into widget
				 */
				textbox.setText(applied.toString());
				textbox.setCursorPos(cursor);
				
				/*
				 * KeyPressEvent occurs before the browser applies changes to the textbox.
				 * We stop propogation and defualt since we already set the changes we wanted
				 * other wise the typed char would be repeated
				 */
				event.preventDefault();
				event.stopPropagation();
			}
		});
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
    
}
