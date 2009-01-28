package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.LookUp;

public class ScreenLookUp extends ScreenInputWidget {
    
    protected LookUp look;
    public static String TAG_NAME = "lookup";
    public ScreenLookUp() {
        
    }
    
    public ScreenLookUp(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            look = (LookUp)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            look = new LookUp();

        if(node.getAttributes().getNamedItem("icon") != null){
            look.setIconStyle(node.getAttributes().getNamedItem("icon").getNodeValue());
        }
        if(node.getAttributes().getNamedItem("onclick") != null) {
            String click = node.getAttributes().getNamedItem("onclick").getNodeValue();
            if(click.equals("this"))
                look.addClickListener((ClickListener)screen);
            else
                look.addClickListener((ClickListener)ClassFactory.forName(click));
        }
        if(node.getAttributes().getNamedItem("mouse") != null) {
            String click = node.getAttributes().getNamedItem("mouse").getNodeValue();
            if(click.equals("this"))
                look.addMouseListener((MouseListener)screen);
            else
                look.addMouseListener((MouseListener)ClassFactory.forName(click));
        }
         
        look.setStyleName("ScreenLookUp");
        initWidget(look);
        setDefaults(node,screen);
    }
    
    public void destroy() {
        look = null;
        super.destroy();
    }
    
    public void load(AbstractField field) {
        if(queryMode)
            queryWidget.load(field);
        else{
            look.setText((String)field.getValue());
            super.load(field);
        }
    }
    
    
    public void submit(AbstractField field) {
        if(queryMode)
            queryWidget.submit(field);
        else{
            field.setValue(look.getText());
            super.submit(field);
        }
    }
    
    
    public void enable(boolean enabled){
        if(queryMode)
            queryWidget.enable(enabled);
        else{
            look.enable(enabled);
            super.enable(enabled);
        }
    }
    
    public void setFocus(boolean focus){
        if(queryMode)
            queryWidget.setFocus(focus);
        else
            look.setFocus(focus);
    }
    


}
