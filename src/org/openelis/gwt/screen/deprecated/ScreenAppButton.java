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
package org.openelis.gwt.screen.deprecated;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.screen.deprecated.AppScreenForm.State;
import org.openelis.gwt.widget.deprecated.AppButton;
import org.openelis.gwt.widget.deprecated.AppButton.ButtonState;

import java.util.EnumSet;

@Deprecated
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
    public ScreenAppButton(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            button = (AppButton)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
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
	            button.changeState(ButtonState.UNPRESSED);
	        else
	            button.changeState(ButtonState.DISABLED);
            super.enable(enabled);
    	}else{
    	    super.enable(true);
        }
    }

    public void destroy() {
        button = null;
        super.destroy();
    }

}
