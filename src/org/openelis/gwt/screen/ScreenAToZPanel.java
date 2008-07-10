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

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.widget.AToZPanel;

public class ScreenAToZPanel extends ScreenWidget {
	
	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "aToZ";
	/**
	 * Widget wrapped by this class
	 */

    public AToZPanel azPanel;
    
	public ScreenAToZPanel() {
    }

    public ScreenAToZPanel(Node node, ScreenBase screen) {
        super(node);
        
        azPanel = new AToZPanel();
        
        if (node.getAttributes().getNamedItem("tablewidth") != null) {
            azPanel.setTableWidth(node.getAttributes().getNamedItem("tablewidth").getNodeValue());
		}
        if (node.getAttributes().getNamedItem("title") != null){
            azPanel.view.setTitle(node.getAttributes().getNamedItem("title").getNodeValue());
        }
        azPanel.setMaxRows((Integer.parseInt(node.getAttributes().getNamedItem("maxRows").getNodeValue())));
        String[] widths = node.getAttributes().getNamedItem("colwidths").getNodeValue().split(",");
        int[] width = new int[widths.length];
        for (int i = 0; i < widths.length; i++) {
            width[i] = Integer.parseInt(widths[i]);
        }
        azPanel.setColWidths(width);
        if(node.getAttributes().getNamedItem("headers") != null){
            String[] headers = node.getAttributes().getNamedItem("headers").getNodeValue().split(",");
            azPanel.view.setHeaders(headers);
        }
        Node bpanel = ((Element)node).getElementsByTagName("buttonPanel").item(0);
        azPanel.setButtonPanel(ScreenWidget.loadWidget(bpanel, screen));
        azPanel.view.initTable(azPanel);
        //azPanel.sizeTable();
        
        ((AppScreen)screen).addKeyboardListener(azPanel);
        ((AppScreen)screen).addClickListener(azPanel);
		initWidget(azPanel);		
        setDefaults(node, screen);
    }
    

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenAToZPanel(node, screen);
    }

}
