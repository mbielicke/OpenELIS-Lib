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

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.widget.ButtonPanel;

/**
 * ScreenButtonPanel wraps a ButtonPanel widget to be displayed on a screen
 * @author tschmidt
 *
 */
public class ScreenButtonPanel extends ScreenWidget {
	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "buttonPanel";
	/**
	 * Widget wrapped by this class
	 */
	private ButtonPanel bPanel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenButtonPanel() {
    }
  
    public ScreenButtonPanel(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            bPanel = (ButtonPanel)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            bPanel = new ButtonPanel();
        NodeList buttons = node.getChildNodes();
        for (int k = 0; k < buttons.getLength(); k++) {
            if(buttons.item(k).getNodeType() == Node.ELEMENT_NODE){
               Widget wid = ScreenWidget.loadWidget(buttons.item(k), screen);
               bPanel.addWidget(wid);
            }
        }
        
        //we only want to find buttons if we didnt have any button nodes above
        if(bPanel.numberOfButtons() == 0)
            bPanel.findButtons(bPanel.hp);
        
        initWidget(bPanel);
        bPanel.setStyleName("ScreenButtonPanel");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenButtonPanel(node, screen);
    }
    
    public void destroy(){
        bPanel = null;
        super.destroy();
    }
    

}
