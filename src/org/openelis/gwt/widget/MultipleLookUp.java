package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;

public class MultipleLookUp extends Composite implements SourcesCommandEvents {
    
    protected TextBox textbox;
    protected HorizontalPanel hp;
    protected HorizontalPanel iconPanel;
    protected CommandListenerCollection commandListeners;
    
    public MultipleLookUp() {
        textbox = new TextBox();
        hp = new HorizontalPanel();
        iconPanel = new HorizontalPanel();
        hp.add(textbox);
        hp.add(iconPanel);
        hp.setSpacing(0);
        initWidget(hp);
        hp.setWidth("auto");
    }
    
    public void addButton(String style, final Enum command, final Object data, MouseListener mouse) {
        IconContainer icon = new IconContainer(style);
        icon.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                commandListeners.fireCommand(command, data);
            }
        });
        icon.setStyleName(style);
        icon.addMouseListener(mouse);
        iconPanel.add(icon);
    }
    
    public void setTextBoxStyle(String style) {
        textbox.setStyleName(style);
    }
        
    public void addTextBoxStyle(String style) {
        textbox.addStyleName(style);
    }
    
    public void removeTextBoxStyle(String style) {
        textbox.removeStyleName(style);
    }
    
    
    public void setText(String text) {
        textbox.setText(text);
    }
    
    public String getText() {
        return textbox.getText();
    }
    
    public void enable(boolean enabled) {
        textbox.setReadOnly(!enabled);
        for(int i = 0; i < iconPanel.getWidgetCount(); i++){
           ((IconContainer)iconPanel.getWidget(i)).enable(enabled);
        }
    }
    
    public void setWidth(String width) {
       textbox.setWidth(width);
    }

    public void getHeight(String height) {
       textbox.setHeight(height);
    }
    
    public void setTextBoxHeight(String height) {
        textbox.setHeight(height);
    }
    
    public void setTextBoxWidth(String width) {
        textbox.setWidth(width);
    }
    
    public void setFocus(boolean focus) {
        textbox.setFocus(focus);
    }
    
    public TextBox getTextBox() {
        return textbox;
    }

    public void addCommandListener(CommandListener listener) {
       if(commandListeners == null) {
           commandListeners = new CommandListenerCollection();
       }
       commandListeners.add(listener);
        
    }

    public void removeCommandListener(CommandListener listener) {
        if(commandListeners != null){
            commandListeners.remove(listener);
        }
        
    }
    

}
