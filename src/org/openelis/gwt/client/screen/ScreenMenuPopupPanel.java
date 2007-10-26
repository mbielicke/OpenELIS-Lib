package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class ScreenMenuPopupPanel extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "menuPopupPanel";
	/**
	 * Widget wrapped by this class
	 */
    public PopupPanel popupPanel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenMenuPopupPanel() {
    }
    
    public ScreenMenuPopupPanel(boolean autoHide,String key, Screen screen) {
    	this.screen = screen;
    	setWidget(popupPanel);
    	popupPanel = new PopupPanel(autoHide);
    	initWidget(popupPanel);
    	screen.widgets.put(key, this);
    	
    }
    
    public ScreenMenuPopupPanel(Node node, Screen screen) {
    	super(node);
    	
    	//autohide
    	if(node.getAttributes().getNamedItem("autoHide") != null && 
    			node.getAttributes().getNamedItem("autoHide").getNodeValue().equals("true")){
    		popupPanel = new PopupPanel(true);
    	}else{
    		popupPanel = new PopupPanel(false);
    	}
    	
    	//height
    	if(node.getAttributes().getNamedItem("height") != null){
    		popupPanel.setHeight(node.getAttributes().getNamedItem("height").getNodeValue());
    	}
    	
    	//width
    	if(node.getAttributes().getNamedItem("width") != null){
    		popupPanel.setWidth(node.getAttributes().getNamedItem("width").getNodeValue());
    	}
    	
//    	popuplistener
    	if(node.getAttributes().getNamedItem("popup") != null){
    		popupPanel.addPopupListener(screen);
    	}
    	
    	//add the widgets to the popup panel
    	NodeList widgets = node.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Node input = null;
                if (widgets.item(k).getNodeName().equals("widget")) {
                    NodeList inputList = widgets.item(k).getChildNodes();
                    for (int m = 0; m < inputList.getLength(); m++) {
                        if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
                            input = inputList.item(m);
                            m = 100;
                        }
                    }
                } else
                    input = widgets.item(k);
                Widget wid = Screen.getWidgetMap().getWidget(input, screen);
                popupPanel.setWidget(wid);
            }
        }
        
        if(node.getAttributes().getNamedItem("hidden") != null && node.getAttributes().getNamedItem("hidden").getNodeValue().equals("true")){
        	DeferredCommand.addCommand(new Command(){
        		public void execute(){
        			popupPanel.show();
        			popupPanel.hide();
        		}
        	});
        	
        }
    	
    	initWidget(popupPanel);
        popupPanel.setStyleName("PopupPanel");
        setDefaults(node, screen);
    }
    
    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenMenuPopupPanel(node, screen);
    }


}
