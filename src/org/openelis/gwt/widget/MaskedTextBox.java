package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * MaskedTextBox extends a GWT TextBox to add formatting functionality
 * to it.  
 * @author tschmidt
 *
 */
public class MaskedTextBox extends TextBox implements
                                          KeyboardListener,
                                          FocusListener {
    private String mask;
    private HashSet<String> literals = new HashSet<String>();
    private ArrayList<String> masks = new ArrayList<String>();
    {
        masks.add("Z");
        masks.add("9");
        masks.add("X");
        masks.add("A");
    }

    public boolean noMask;
    
    public MaskedTextBox() {
        super();
        addKeyboardListener(this);
        addFocusListener(this);
    }
    

    /**
     * if typed param is true then the formatting of the text 
     * will be done as the user is typing. If false then the 
     * formatting will not occur until the user takes the focus
     * away from this widget.
     * @param typed
     */
    public void setAsTyped(boolean typed) {
        if (!typed)
            removeKeyboardListener(this);
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
        if (getText().equals("") || noMask)
            return;
        char[] chars = getText().toCharArray();
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
        setText(text);
    }

    
    public String applyMask(String text, boolean end) {
        
        String retText = text;
        String input = String.valueOf(text.charAt(text.length() - 1));
        String maskChar = String.valueOf(mask.charAt(text.length() - 1));
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
            if (literals.contains(String.valueOf(mask.charAt(text.length()))))
                retText = text + String.valueOf(mask.charAt(text.length()));
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

    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        if (keyCode == KeyboardListener.KEY_BACKSPACE) {
            if (literals.contains(String.valueOf(getText().charAt(getText().length() - 1))))
                setText(getText().substring(0, getText().length() - 1));
        }
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        if (keyCode == KeyboardListener.KEY_BACKSPACE || keyCode == KeyboardListener.KEY_SHIFT || noMask) {
            return;
        }
        String text = getText();
        if (text.length() > mask.length()) {
            setText(text.substring(0, text.length() - 1));
            return;
        }
        setText(applyMask(text, false));
        if (text.length() == mask.length() && keyCode != KeyboardListener.KEY_TAB){
            complete();
        }
            
    }

    public void onFocus(Widget sender) {
        // TODO Auto-generated method stub

    }

    public void onLostFocus(Widget sender) {
        if(!isReadOnly())
        	format();
    }
    
    public void complete(){
    }

}
