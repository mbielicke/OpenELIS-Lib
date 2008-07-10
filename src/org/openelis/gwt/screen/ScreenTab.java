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

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
/**
 * ScreenTab wraps a GWT TabPanel for displaying widgets 
 * on a Screen in Tab Layout.
 * @author tschmidt
 *
 */
public class ScreenTab extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "TabPanel";
	/**
	 * Widget wrapped by this class
	 */
	private TabPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTab() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="tab" key="string"&gt;
     *   &lt;tab text="string"&gt;
     *    ....
     *   &lt;tab&gt;
     *   &lt;tab text="string"&gt;
     *    ....
     *   &lt;/tab&gt;
     * &lt;/panel&gt;
     *     
     * @param node
     * @param screen
     */	
    public ScreenTab(Node node, ScreenBase screen) {
        super(node);
        panel = new TabPanel();
        panel.setStyleName("ScreenTab");
        initWidget(panel);
        NodeList tabs = ((Element)node).getElementsByTagName("tab");
        for (int k = 0; k < tabs.getLength(); k++) {
            NodeList widgets = tabs.item(k).getChildNodes();
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                    ScrollPanel scroll = new ScrollPanel();
                    scroll.add(wid);
                    scroll.setWidth("100%");
                    //tabs can not have a constant or hard coded text
                    panel.add(scroll, tabs.item(k).getAttributes()
                                             .getNamedItem("text")
                                             .getNodeValue());
                }
            }
        }
        panel.selectTab(0);
        panel.addTabListener((TabListener)screen);
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTab(node, screen);
    }
    
    public void destroy(){
        panel = null;
        super.destroy();
    }

}
