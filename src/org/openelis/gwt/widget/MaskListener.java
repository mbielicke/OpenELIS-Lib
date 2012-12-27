package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

public class MaskListener implements KeyUpHandler, KeyDownHandler, BlurHandler {
    
    private String mask;
    private HashSet<String> literals = new HashSet<String>();
    private ArrayList<String> masks = new ArrayList<String>();
    private TextBox textbox;
    
    {
        masks.add("Z");
        masks.add("9");
        masks.add("X");
        masks.add("A");
    }

    public boolean noMask;
    
    public MaskListener(TextBox textbox, String mask) {
        this.textbox = textbox;
        textbox.addKeyUpHandler(this);
        textbox.addKeyDownHandler(this);
        setMask(mask);
    }

    /**
     * This method sets the mask format for the widget. The string
     * uses the following chars for formatting:
     * 
     * Z - Zero suppression
     * 9 - Character must be a number
     * X - Character can be anything
     * A - Character must be a letter
     * 
     * Any other Characters will be used as literals
     * 
     * @param mask
     */
    public void setMask(String mask) {
        this.mask = mask;
        for (int i = 0; i < mask.length(); i++) {
            if (!masks.contains(String.valueOf(mask.charAt(i))))
                literals.add(String.valueOf(mask.charAt(i)));
        }
    }

    /**
     * Call this method to format the text.
     */
    public void format() {
        if (textbox.getText().equals("") || noMask)
            return;
        char[] chars = textbox.getText().toCharArray();
        String text = "";
        int i = 0;
        boolean end = false;
        while (text.length() < mask.length()) {
            if (i < chars.length) {
                if (literals.contains(String.valueOf(chars[i])) && String.valueOf(chars[i])
                                                                         .equals(String.valueOf(text.charAt(text.length() - 1))))
                    i++;
                text += String.valueOf(chars[i]);
            } else
                end = true;
            text = applyMask(text, end);
            i++;
        }
        textbox.setText(text);
    }

    
    public String applyMask(String text, boolean end) {
    	if("".equals(text))
        	return "";
        String retText = "";
        String input = String.valueOf(text.charAt(text.length() - 1));
        String maskChar = String.valueOf(mask.charAt(text.length() - 1));
        while(text.length() == 1 && literals.contains(maskChar)){
            retText += maskChar;
            maskChar = String.valueOf(mask.charAt(retText.length()));
        }
        retText += text;
        if (literals.contains(String.valueOf(input)) || end) {
            int li = text.length();
            if (!end) {
                while (li < mask.length() && !input.equals(String.valueOf(mask.charAt(li))))
                    li++;
            } else {
                while (li < mask.length() && !literals.contains(String.valueOf(mask.charAt(li))))
                    li++;
            }
            if (li == mask.length() && !end) {
                retText = (text.substring(0, text.length() - 1));
                return retText;
            } else if (li < mask.length() && end) {
                text += String.valueOf(mask.charAt(li));
            }
            int ss = li - 1;
            while (ss > 0 && !literals.contains(String.valueOf(mask.charAt(ss - 1))))
                ss--;
            int inl = 0;
            if (ss != text.length()) {
                int is = text.length() - 1;
                if (literals.contains(String.valueOf(text.charAt(is))))
                    is--;
                int iL = 0;
                while (is >= 0 && !literals.contains(String.valueOf(text.charAt(is)))) {
                    iL++;
                    is--;
                }
                inl = (li - ss) - iL;
            } else {
                inl = (li - ss);
            }
            for (int i = 0; i < inl; i++) {
                if (mask.charAt(i + ss) == '9')
                    text = text.substring(0, ss) + "0"
                           + text.substring(ss, text.length());
                else
                    text = text.substring(0, ss) + '\ufeff'
                           + text.substring(ss, text.length());
            }
            retText = text;
            return retText;
        }
        if (checkMask(input, maskChar)) {
            if (text.length() == mask.length())
                return text;
            while(literals.contains(String.valueOf(mask.charAt(retText.length())))){
                retText += String.valueOf(mask.charAt(retText.length()));
            }
            if (input.equals("0") && maskChar.equals("Z"))
                retText = text.substring(0, text.length() - 1) + " ";
        } else {
            retText = text.substring(0, text.length() - 1);
        }
        return retText;
    }

    public boolean checkMask(String input, String maskChar) {
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
        // TODO Auto-generated method stub
        if(!textbox.enforceMask)
            return;
        if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
            if (literals.contains(String.valueOf(textbox.getText().charAt(textbox.getText().length() - 1))))
                textbox.setText(textbox.getText().substring(0, textbox.getText().length() - 1));
        }
    }

    public void onKeyUp(KeyUpEvent event) {
        // TODO Auto-generated method stub
        if(!textbox.enforceMask)
            return;
        if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE || event.getNativeKeyCode() == KeyCodes.KEY_SHIFT || noMask) {
            return;
        }
        String text = textbox.getText();
        if (text.length() > mask.length()) {
            textbox.setText(text.substring(0, text.length() - 1));
            return;
        }
        textbox.setText(applyMask(text, false));
        if (text.length() == mask.length() && event.getNativeKeyCode() != KeyCodes.KEY_TAB){
            complete();
        }
            
    }

    public void onBlur(BlurEvent event) {
        if(textbox.isReadOnly() || !textbox.enforceMask)
            return;
        format();
    }
    
    public void complete(){
    }

}
