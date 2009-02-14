/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.screen;

import java.util.EnumSet;

import org.openelis.gwt.event.CommandHandler;
import org.openelis.gwt.screen.AppScreenForm.State;
import org.openelis.gwt.widget.AppButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

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
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            button = (AppButton)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            button = new AppButton();
        button.command = ClassFactory.getEnum(node.getAttributes().getNamedItem("command").getNodeValue());
        if(node.getAttributes().getNamedItem("value") != null){
            button.value = node.getAttributes().getNamedItem("value").getNodeValue();
        }
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
        if (node.getAttributes().getNamedItem("listeners") != null){
            String[] listeners = node.getAttributes().getNamedItem("listeners").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    button.addCommand((CommandHandler)screen);
                else
                    button.addCommand((CommandHandler)ClassFactory.forName(listeners[i]));
            }
        }
        
    
        if(node.getAttributes().getNamedItem("enabledStates") != null && !"".equals(node.getAttributes().getNamedItem("enabledStates").getNodeValue())){
            EnumSet<State> enabledStateSet = EnumSet.noneOf(State.class);
        	 String[] enabledStates = node.getAttributes().getNamedItem("enabledStates").getNodeValue().split(",");
        	 for(int i = 0; i < enabledStates.length; i++)
        		 enabledStateSet.add(State.valueOf(enabledStates[i].toUpperCase()));
        	 button.setEnabledStates(enabledStateSet);
        }else{
            button.setEnabledStates(EnumSet.noneOf(State.class));
        }
        
        if(node.getAttributes().getNamedItem("lockedStates") != null && !"".equals(node.getAttributes().getNamedItem("lockedStates").getNodeValue())){
        	 EnumSet<State> lockedStateSet = EnumSet.noneOf(State.class);
        	 String[] lockedStates = node.getAttributes().getNamedItem("lockedStates").getNodeValue().split(",");
        	 for(int i = 0; i < lockedStates.length; i++)
        		 lockedStateSet.add(State.valueOf(lockedStates[i].toUpperCase()));
        	 button.setLockedStates(lockedStateSet);
        }else{
            button.setLockedStates(EnumSet.noneOf(State.class));
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
	            button.changeState(AppButton.ButtonState.UNPRESSED);
	        else
	            button.changeState(AppButton.ButtonState.DISABLED);
            button.setEnabled(enabled);
            super.enable(enabled);
    	}else{
            button.setEnabled(enabled);
    	    super.enable(true);
        }
    }

    public void destroy() {
        button = null;
        super.destroy();
    }

}
