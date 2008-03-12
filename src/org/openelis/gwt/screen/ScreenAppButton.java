package org.openelis.gwt.screen;

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
            button.addClickListener(screen);
        }
        
        if(node.getAttributes().getNamedItem("enabledStates") != null){
        	int enabledStatesInt = 0;
        	 String[] enabledStates = node.getAttributes().getNamedItem("enabledStates").getNodeValue().split(",");
        	 for(int i = 0; i < enabledStates.length; i++){
        		 if("default".equals(enabledStates[i])){
        			 enabledStatesInt = enabledStatesInt + FormInt.DEFAULT;
        		 }else if("display".equals(enabledStates[i])){
        			 enabledStatesInt = enabledStatesInt + FormInt.DISPLAY;
        		 }else if("update".equals(enabledStates[i])){
        			 enabledStatesInt = enabledStatesInt + FormInt.UPDATE;
        		 }else if("add".equals(enabledStates[i])){
        			 enabledStatesInt = enabledStatesInt + FormInt.ADD;
        		 }else if("query".equals(enabledStates[i])){
        			 enabledStatesInt = enabledStatesInt + FormInt.QUERY;
        		 }else if("browse".equals(enabledStates[i])){
        			 enabledStatesInt = enabledStatesInt + FormInt.BROWSE;
        		 }else if("delete".equals(enabledStates[i])){
        			 enabledStatesInt = enabledStatesInt + FormInt.DELETE;
        		 }
        	 }
        	 button.setMaskedEnabledState(enabledStatesInt);
        }
        
        if(node.getAttributes().getNamedItem("lockedStates") != null){
        	int lockedStatesInt = 0;
        	 String[] lockedStates = node.getAttributes().getNamedItem("lockedStates").getNodeValue().split(",");
        	 for(int i = 0; i < lockedStates.length; i++){
        		 if("default".equals(lockedStates[i])){
        			 lockedStatesInt = lockedStatesInt + FormInt.DEFAULT;
        		 }else if("display".equals(lockedStates[i])){
        			 lockedStatesInt = lockedStatesInt + FormInt.DISPLAY;
        		 }else if("update".equals(lockedStates[i])){
        			 lockedStatesInt = lockedStatesInt + FormInt.UPDATE;
        		 }else if("add".equals(lockedStates[i])){
        			 lockedStatesInt = lockedStatesInt + FormInt.ADD;
        		 }else if("query".equals(lockedStates[i])){
        			 lockedStatesInt = lockedStatesInt + FormInt.QUERY;
        		 }else if("browse".equals(lockedStates[i])){
        			 lockedStatesInt = lockedStatesInt + FormInt.BROWSE;
        		 }else if("delete".equals(lockedStates[i])){
        			 lockedStatesInt = lockedStatesInt + FormInt.DELETE;
        		 }
        	 }
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
