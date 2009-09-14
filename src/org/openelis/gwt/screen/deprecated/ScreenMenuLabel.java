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
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.widget.deprecated.MenuLabel;

/**
 * ScreenMenuLabel wraps a MenuLabel widget for display on a Screen.
 * @author tschmidt
 *
 */
@Deprecated
public class ScreenMenuLabel extends ScreenWidget implements SourcesClickEvents{
	/**
	 * Default XML Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "menulabel";
	/**
	 * Widget wrapped by this class
	 */
    private MenuLabel label;
    private DelegatingClickListenerCollection clickListeners;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenMenuLabel() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;menulabel key="string" onClick="string" image="string" text="string" value="string"/&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenMenuLabel(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            label = (MenuLabel)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            label = new MenuLabel();
        if (node.getAttributes().getNamedItem("text") != null){
            label.setText(node.getAttributes().getNamedItem("text").getNodeValue());
        }
        if(node.getAttributes().getNamedItem("image") !=  null)
            label.setImage(node.getAttributes().getNamedItem("image").getNodeValue());
        initWidget(label);
        label.setStyleName("ScreenLabel");
        if (node.getAttributes().getNamedItem("value") != null) {
            setUserObject(node.getAttributes().getNamedItem("value").getNodeValue());
        }
        if(node.getAttributes().getNamedItem("onClick") != null){
            String[] listeners = node.getAttributes().getNamedItem("onClick").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    addClickListener((ClickListener)screen);
                else
                    addClickListener((ClickListener)ClassFactory.forName(listeners[i]));
            }
        }
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenMenuLabel(node, screen);
    }

    public void addClickListener(ClickListener listener) {
        if (clickListeners == null) {
            clickListeners = new DelegatingClickListenerCollection(this, label.getLabel());
          }
          clickListeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
       if(clickListeners != null){
           clickListeners.remove(listener);
       }   
    }
    
    public void destroy() {
        clickListeners = null;
        label = null;
        super.destroy();
    }

}
