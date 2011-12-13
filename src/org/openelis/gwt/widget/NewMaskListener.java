package org.openelis.gwt.widget;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class NewMaskListener {
	
	String mask;
	final TextBox<?> textbox;
	
	public NewMaskListener(TextBox<?> box, String mask) {
		this.mask = mask;
		this.textbox = box;
		textbox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if(!textbox.enforceMask)
		            return;
				if(event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE || event.getNativeKeyCode() == KeyCodes.KEY_DELETE)
					return;	
				
				applyMask();
			}
		});
		textbox.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				applyMask();
				
			}
		});
	}
	
	private void applyMask() {
		String input;
		StringBuffer applied;
		char mc;
		int pos;
		boolean loop;
		
		applied = new StringBuffer();
		input = textbox.getText();
		pos = 0;
		/*
		 * Loop through input applying mask chars when needed
		 */
		for(char in : input.toCharArray()) {
		    do {
		    	loop = false;
		    	mc = mask.charAt(pos); 
		    	switch(mc) {
		    		case '9' :					
		    			if(Character.isDigit(in)) {  
		    				applied.append(in);
		    				pos++;
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
		 
		if(pos < mask.length()) {
			mc = mask.charAt(pos);
			while(mc != 'X' && mc != '9') {
				applied.append(mc);
				mc = mask.charAt(++pos);
			}
		}
		*/
		
		
		textbox.setText(applied.toString());
	}

}
