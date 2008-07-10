/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.HasWidgets;
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
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;buttonPanel key="string" buttons="qunpbdcra"/&gt;
     * <br/>
     * <pre>
     * buttons attribute represents which buttons to display
     *   q - Query
     *   u - Update
     *   n - Next
     *   p - Previous
     *   b - Abort
     *   d - Delete
     *   c - Commit
     *   r - Reload
     *   a - Add
     * </pre>
     * @param node
     * @param screen
     */
    public ScreenButtonPanel(Node node, ScreenBase screen) {
        super(node);
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
