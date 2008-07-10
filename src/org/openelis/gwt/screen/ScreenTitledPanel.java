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

import org.openelis.gwt.widget.ButtonPanel;
import org.openelis.gwt.widget.TitledPanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class ScreenTitledPanel extends ScreenWidget{

	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "titledPanel";
	/**
	 * Widget wrapped by this class
	 */
	private TitledPanel titledPanel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTitledPanel() {
    }
    
    public ScreenTitledPanel(Node node, ScreenBase screen) {
        super(node);
        titledPanel = new TitledPanel();
        
        Element legend = (Element) ((Element)node).getElementsByTagName("legend").item(0);
        NodeList widgets = legend.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                titledPanel.setTitle(wid);
            }
        }
        
        legend = (Element) ((Element)node).getElementsByTagName("content").item(0);
        widgets = legend.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                titledPanel.setWidget(wid);
            }
        }
       
        initWidget(titledPanel);
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTitledPanel(node, screen);
    }
}
