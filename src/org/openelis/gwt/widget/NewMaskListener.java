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
				
				if(textbox.enforceMask)
					applyMask();
			}
		});
		textbox.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				if(textbox.enforceMask)
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
		    	System.out.println(applied.toString());
		    	loop = false;
		    	mc = mask.charAt(pos); 
		    	switch(mc) {
		    		case '9' :			
		    			// If input is digit, apply and move on
		    			if(Character.isDigit(in)) {  
		    				applied.append(in);
		    				pos++;
		    			//If the input is the next expected literal in mask
		    			//increment up the mask and loop again
		    			}else if(isNextLiteral(in,pos)){
		    				mc = mask.charAt(++pos);
		    				loop = true;
		    			}
		    			break;
		    		case 'X' :
		    			// input is ok, apply and move on
		    			if(Character.isLetterOrDigit(in)) {  
		    				applied.append(in);
		    				pos++;
			    		//If the input is the next expected literal in mask
			    		//increment up the mask and loop again
		    			}else if(isNextLiteral(in,pos)){
		    				mc = mask.charAt(++pos);
		    				loop = true;
		    			}
		    			break;
		    		default :
		    			applied.append(mc);
		    			pos++;
		    			
		    			// if true means that a literal was jut added for the user and we want to 
		    			// go up a position in the mask before applying in.
		    			if(mc != in) {
	    					mc = mask.charAt(pos);
	    					loop = true;
		    			}
		    	}
			} while(loop && pos < mask.length());
		}
		
		textbox.setText(applied.toString());
	}
	
    private boolean isNextLiteral(char in, int pos) {
    	char mc;
    	
    	mc = mask.charAt(pos);
    	while(pos < mask.length() && (mc == '9' || mc == 'X')) {
    		pos++;
    		mc = mask.charAt(pos);
    	}
    	
    	return pos < mask.length() && mc == in;
    	
    }

}
