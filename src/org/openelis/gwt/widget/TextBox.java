package org.openelis.gwt.widget;

public class TextBox extends com.google.gwt.user.client.ui.TextBox {
    
    public enum Case {MIXED,UPPER,LOWER};
    public Case textCase = Case.MIXED;
    
    public boolean enforceMask = true;
    public boolean enforceLength = true;
    public boolean autoNext = false;
    public int length = 255;
    
    public void setCase(Case textCase){
        this.textCase = textCase;
        if (textCase == Case.UPPER){
            addStyleName("Upper");
            removeStyleName("Lower");
        }
        if (textCase == Case.LOWER){
            addStyleName("Lower");
            removeStyleName("Upper");
        }
    }
    
    public void setLength(int length) {
        this.length = length;
        setMaxLength(length);
    }
    
    public String getText() {
        if (textCase == Case.UPPER)
            return super.getText().toUpperCase();
        else if(textCase == Case.LOWER)
            return super.getText().toLowerCase();
        else
            return super.getText();
    }
    
    public void setText(String text) {
        if(textCase == Case.UPPER)
            super.setText(text.toUpperCase());
        else if(textCase == Case.LOWER)
            super.setText(text.toLowerCase());
        else 
            super.setText(text);
    }
    
    public void setMask(String mask) {
        new MaskListener(this,mask);
        setLength(mask.length()); 
    }
    

}
