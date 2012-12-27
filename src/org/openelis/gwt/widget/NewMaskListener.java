package org.openelis.gwt.widget;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

public class NewMaskListener {
	
	String mask;
	final TextBox<?> textbox;
	
	public NewMaskListener(TextBox<?> box, String msk) {
		this.mask = msk;
		this.textbox = box;
		
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
				if(!textbox.enforceMask)
		            return;
				
				if((event.isAnyModifierKeyDown() && !event.isShiftKeyDown()) || 
				    event.getNativeKeyCode() >= 112 && event.getNativeKeyCode() <= 124)
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
				if(!textbox.enforceMask)
		            return;
				
				if((event.isAnyModifierKeyDown() && !event.isShiftKeyDown()) || 
					event.getNativeEvent().getKeyCode() >= 112 && event.getNativeEvent().getKeyCode() <= 124)
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
}
