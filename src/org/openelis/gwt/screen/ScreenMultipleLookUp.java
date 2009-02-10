package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.widget.MultipleLookUp;

public class ScreenMultipleLookUp extends ScreenInputWidget {
    
    protected MultipleLookUp look;
    public static String TAG_NAME = "multLookup";
    public ScreenMultipleLookUp() {
        
    }
    
    public ScreenMultipleLookUp(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            look = (MultipleLookUp)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            look = new MultipleLookUp();
        if (node.getAttributes().getNamedItem("listeners") != null){
            String[] listeners = node.getAttributes().getNamedItem("listeners").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    look.addCommandListener((CommandListener)screen);
                else
                    look.addCommandListener((CommandListener)ClassFactory.forName(listeners[i]));
            }
        }
        NodeList icons = node.getChildNodes();
        for(int i = 0; i < icons.getLength(); i++){
            if(icons.item(i).getNodeType() == Node.ELEMENT_NODE){
                String style = icons.item(i).getAttributes().getNamedItem("style").getNodeValue();
                Enum command = ClassFactory.getEnum(icons.item(i).getAttributes().getNamedItem("command").getNodeValue());
                MouseListener listener = null;
                if(icons.item(i).getAttributes().getNamedItem("mouse") != null) {
                    String click = icons.item(i).getAttributes().getNamedItem("mouse").getNodeValue();
                    if(click.equals("this"))
                        listener = ((MouseListener)screen);
                    else
                        listener = ((MouseListener)ClassFactory.forName(click));
                }
                look.addButton(style, command, null, listener);
            }
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
