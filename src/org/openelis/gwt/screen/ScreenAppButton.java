package org.openelis.gwt.screen;

import java.util.HashMap;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.FormInt;

public class ScreenAppButton extends ScreenWidget {
    
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
    public static String TAG_NAME = "appButton";
    /**
     * Widget wrapped by this class
     */
    private AppButton button;
    private static HashMap statesHash;
    
    static {
    	statesHash = new HashMap();
    	statesHash.put("default", new Integer(FormInt.DEFAULT));
    	statesHash.put("display", new Integer(FormInt.DISPLAY));
    	statesHash.put("update", new Integer(FormInt.UPDATE));
    	statesHash.put("add", new Integer(FormInt.ADD));
    	statesHash.put("query", new Integer(FormInt.QUERY));
    	statesHash.put("browse", new Integer(FormInt.BROWSE));
    	statesHash.put("delete", new Integer(FormInt.DELETE));
    }
    /**
     * Default no-arg constructor used to create reference in the WidgetMap class
     */
    public ScreenAppButton() {
    }

    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget<br/>
     * <br/>
     * &lt;button key="string" html="escaped HTML" text="string" constant="boolean"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenAppButton(Node node, final ScreenBase screen) {
        super(node);
        button = new AppButton();
        button.action = node.getAttributes().getNamedItem("action").getNodeValue();
        if(node.getAttributes().getNamedItem("toggle") != null){
            if(node.getAttributes().getNamedItem("toggle").getNodeValue().equals("true"))
                button.toggle = true;
        }
        NodeList widgets = node.getChildNodes();
        for (int l = 0; l < widgets.getLength(); l++) {
            if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {                       
                Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                button.setWidget(wid);
            }
        }
        if (node.getAttributes().getNamedItem("onclick") != null){
            String[] listeners = node.getAttributes().getNamedItem("onclick").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    button.addClickListener((ClickListener)screen);
                else
                    button.addClickListener((ClickListener)ClassFactory.forName(listeners[i]));
            }
        }
        
    
        if(node.getAttributes().getNamedItem("enabledStates") != null && !"".equals(node.getAttributes().getNamedItem("enabledStates").getNodeValue())){
        	int enabledStatesInt = 0;
        	 String[] enabledStates = node.getAttributes().getNamedItem("enabledStates").getNodeValue().split(",");
        	 for(int i = 0; i < enabledStates.length; i++)
        		 enabledStatesInt = enabledStatesInt + ((Integer)statesHash.get(enabledStates[i])).intValue();
        	 button.setMaskedEnabledState(enabledStatesInt);
        }
        
        if(node.getAttributes().getNamedItem("lockedStates") != null && !"".equals(node.getAttributes().getNamedItem("lockedStates").getNodeValue())){
        	int lockedStatesInt = 0;
        	 String[] lockedStates = node.getAttributes().getNamedItem("lockedStates").getNodeValue().split(",");
        	 for(int i = 0; i < lockedStates.length; i++)
        		 lockedStatesInt = lockedStatesInt + ((Integer)statesHash.get(lockedStates[i])).intValue();
        	 button.setMaskedLockedState(lockedStatesInt);
        }
        
        initWidget(button);
        setDefaults(node, screen);
        
        if (node.getAttributes().getNamedItem("style") != null)
        	button.setStyleName(node.getAttributes().getNamedItem("style").getNodeValue());
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen){
        return new ScreenAppButton(node,screen);
    }
    
    public void enable(boolean enabled){
    	if(!alwaysEnabled){
	        if(enabled)
	            button.changeState(AppButton.UNPRESSED);
	        else
	            button.changeState(AppButton.DISABLED);
    	}
    }

    public void destroy() {
        button = null;
        super.destroy();
    }

}
