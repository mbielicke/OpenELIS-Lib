package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;

public class LookUp extends Composite implements SourcesClickEvents, SourcesMouseEvents {
    
    protected TextBox textbox;
    protected IconContainer icon;
    protected HorizontalPanel hp;
    protected DelegatingClickListenerCollection clickListeners;
    
    public LookUp() {
        textbox = new TextBox();
        icon = new IconContainer();
        hp = new HorizontalPanel();
        hp.add(textbox);
        hp.add(icon);
        hp.setSpacing(0);
        initWidget(hp);
    }
    
    public void setIconStyle(String style) {
        icon.setStyleName(style);
    }
    
    public void setTextBoxStyle(String style) {
        textbox.setStyleName(style);
    }
    
    public void addIconStyle(String style) {
        icon.addStyleName(style);
    }
    
    public void addTextBoxStyle(String style) {
        textbox.addStyleName(style);
    }
    
    public void removeTextBoxStyle(String style) {
        textbox.removeStyleName(style);
    }
    
    public void removeIconStyle(String style) {
        icon.removeStyleName(style);
    }
    
    public void addClickListener(ClickListener listener) {
        if(clickListeners == null)
            clickListeners = new DelegatingClickListenerCollection(this, icon);
        clickListeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
        if(clickListeners != null)
            clickListeners.remove(listener);
    }

    public void addMouseListener(MouseListener listener) {
       icon.addMouseListener(listener);
    }

    public void removeMouseListener(MouseListener listener) {
       icon.addMouseListener(listener);
    }
    
    public void setText(String text) {
        textbox.setText(text);
    }
    
    public String getText() {
        return textbox.getText();
    }
    
    public void enable(boolean enabled) {
        textbox.setReadOnly(!enabled);
        icon.enable(enabled);    
    }
    
    public void setWidth(String width) {
       hp.setWidth(width);
    }

    public void getHeight(String height) {
       hp.setHeight(height);
    }
    
    public void setTextBoxHeight(String height) {
        textbox.setHeight(height);
    }
    
    public void setTextBoxWidth(String width) {
        textbox.setWidth(width);
    }
    
    public void setIconHeight(String height) {
        icon.setHeight(height);
    }
    
    public void setIconWidth(String width) {
        icon.setWidth(width);
    }
    
    public void setFocus(boolean focus) {
        textbox.setFocus(focus);
    }
    
    

}
