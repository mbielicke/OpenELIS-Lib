package org.openelis.gwt.widget;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

/**
 * This class is an extension of GWT's TextBox widget to add our Masking, Alignment, and Case
 * logic so it can be wrapped by other widgets in the library
 *
 */
public class TextBase extends com.google.gwt.user.client.ui.TextBox {
	
    /**
     * Textbox attributes
     */
    protected TextAlignment                         alignment = TextAlignment.LEFT;
    protected Case                                  textCase  = Case.MIXED;

	
    protected boolean                               enforceMask,maskEnabled;
    
    public enum Case {
        MIXED, UPPER, LOWER
    };
    
    protected String                                picture,mask;
    
    protected TextBase                              source = this;
    
    /**
     * This method is overwritten to implement case management. Use the
     * setValue/getValue methods for normal screen use.
     */
    public String getText() {
    	String text;
    	
    	text = super.getText();
    	
    	if(enforceMask && text.equals(picture))
    		text = "";
    	
        switch (textCase) {
            case UPPER:
                return text.toUpperCase();
            case LOWER:
                return text.toLowerCase();
            default:
                return text;
        }
    }
        
    /**
     * This method is overridden to make sure the Case style is applied to the widget  
     */
    @Override
    public void setStyleName(String style) {
    	super.setStyleName(style);
    	setCase(this.textCase);
    }

    /**
     * Set the text case for input.
     */
    public void setCase(Case textCase) {
    	if(textCase == null)
    		textCase = Case.MIXED;
    	
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
     * Set the text alignment.
     */
    public void setTextAlignment(TextAlignment alignment) {
        this.alignment = alignment;
        super.setAlignment(alignment);
    }
    
    /**
     * Sets the current mask to be used in the textbox and if first mask set will set up
     * handlers to receive events to apply the mask.
     */
    public void setMask(String msk) {
    	StringBuffer pic;
    
    	this.mask = msk;
    	
    	/*
    	 * If passed mask is null, set properties to turn off masking
    	 */
    	if(msk == null) {
    		enforceMask = false;
    		picture = null;
    		return;
    	}
    	
    	enforceMask = true;
    	
    	/*
    	 * Create default picture with spaces so we can 
    	 * identify when the user wants to null out the field
    	 */
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
    	 * If mask has been previously enabled we don't want or need to 
    	 * re-add the key handlers
    	 */
    	if(!maskEnabled) {
    		maskEnabled = true;
    		/*
    		 * Delete and BackSpace keys are handled in KeyDown because Chrome and IE do not 
    		 * pass these keys to the KeyPressEvent.  
    		 */
    		addKeyDownHandler(new KeyDownHandler() {
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
    				
    				if((event.isAnyModifierKeyDown() && !event.isShiftKeyDown()) ||
    				   (event.getNativeKeyCode() >= KeyCodes.KEY_F1 && event.getNativeKeyCode() <= KeyCodes.KEY_F12))
    					return;

    				input = getText();  // Current state of the Textbox including selection.

    				cursor = getCursorPos(); //Current position of cursor when key was pressed.

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
    					if(getSelectionLength() > 0) {
    						applied = new StringBuffer();

    						selectStart = getText().indexOf(getSelectedText());  // Start position of selection
    						selectEnd = selectStart + getSelectionLength();              // End positon of selection.

    						applied.append(input.substring(0, selectStart));  // Copy the start of the input up to the start of selection into buffer.

    						/*
    						 * Loop through the selected portion and either blank out or insert mask literals
    						 */
    						for(int i = 0; i < getSelectionLength(); i++) {
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
    					setText(applied.toString());
    					setCursorPos(cursor);

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
    		addKeyPressHandler(new KeyPressHandler() {
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
    				
    				if((event.isAltKeyDown() || event.isControlKeyDown() || event.isMetaKeyDown()) ||
    			       (event.getNativeEvent().getKeyCode() >= KeyCodes.KEY_F1 && event.getNativeEvent().getKeyCode() <= KeyCodes.KEY_F12))
    					return;

    				input = getText();  // Current state of the Textbox including selection.

    				cursor = getCursorPos(); //Current position of cursor when key was pressed.
    		        
    				selectStart = cursor;
    		        
    				/*
    				 * If part of the text is selected we want to blank out the selection preserving any mask literals 
    				 * and the current length of length of textbox input. 
    				 */
    				if(getSelectionLength() > 0) {
    					applied = new StringBuffer();

    					selectStart = getText().indexOf(getSelectedText());  // Start position of selection
    					selectEnd = selectStart + getSelectionLength();              // End positon of selection.

    					applied.append(input.substring(0, selectStart));  // Copy the start of the input up to the start of selection into buffer.

    					/*
    					 * Loop through the selected portion and either blank out or insert mask literals
    					 */
    					for(int i = 0; i < getSelectionLength(); i++) {
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


    				ch = event.getUnicodeCharCode() == 0 ? (char)event.getNativeEvent().getKeyCode() : event.getCharCode();   // character typed by user
    				
    				applied.append(input.substring(0,cursor));  // Copy the portion of input up to the cursor to the buffer

    				/*
    				 * This event is before the textbox's check for Max Length so we need to do the check 
    				 */
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
    						else {
    							applied = new StringBuffer(input);
    							cursor = input.length() - 1;     // Decrement cursor and through input if not right type
    						}
    						break;
    					case 'X' :
    						if(Character.isLetterOrDigit(ch)) // if input matches add to buffer 
    							applied.append(ch);
    						else {
    							applied = new StringBuffer(input);
    							cursor = input.length() - 1;    // Decrement cursor and through input if not right type
    						}
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
    				setText(applied.toString());
    				setCursorPos(cursor);

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
    }
    	
    
    /**
     * Method to allow wrapping widget to toggle enforcing the setMask.  Used primarily for widgets
     * that participate in Query.
     * @param enforce
     */
    public void enforceMask(boolean enforce) {
    	enforceMask = enforce;
    }
}
