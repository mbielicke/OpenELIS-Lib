package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.ScrollPanel;

public class Tab extends ScrollPanel {
    
    private String text,key;
    
    public Tab() {
        
    }
    
    public void setText(String text) {
        this.text = text; 
    }
    
    public String getText() {
        return text;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
    

}
