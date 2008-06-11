package org.openelis.gwt.widget.richtext;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RichTextWidget extends Composite {
    
    private VerticalPanel vp = new VerticalPanel();
    private RichTextArea area = new RichTextArea();
    private RichTextToolbar toolbar = new RichTextToolbar(area);
    
    public RichTextWidget() {
        initWidget(vp);
        vp.add(toolbar);
        vp.add(area);
        area.setSize("100%","100%");

    }
    
    public void setText(String text){
        area.setHTML(text);
    }
    
    public String getText(){
        return area.getHTML();
    }
    
    public void addFocusListener(FocusListener listener){
        area.addFocusListener(listener);
    }
    
    public void removeFocusListener(FocusListener listener){
        area.removeFocusListener(listener);
    }
    
    public void setFocus(boolean focused) {
        area.setFocus(focused);
    }
    
    public boolean isEnabled(){
        return area.isEnabled();
    }
    
    public void setWidth(String width){
        vp.setWidth(width);
    }
    
    public void setHeight(String height){
        vp.setHeight(height);
    }

}
