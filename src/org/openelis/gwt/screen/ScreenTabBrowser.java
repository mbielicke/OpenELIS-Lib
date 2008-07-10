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

import com.google.gwt.xml.client.Node;

import org.openelis.gwt.widget.TabBrowser;
/**
 * ScreenTabBrowser wraps a TabBrowser widget to be displayed
 * on a screen.
 * @author tschmidt
 *
 */
public class ScreenTabBrowser extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "tabbrowser";
	/**
	 * Widget wrapped by this class
	 */
    private TabBrowser tb;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTabBrowser() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;tabbrowser key="string" tabLimit="int" sizeToWindow="boolean"/&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenTabBrowser(Node node, ScreenBase screen) {
        super(node);
        int tabLimit = Integer.parseInt(node.getAttributes()
                                            .getNamedItem("tabLimit")
                                            .getNodeValue());
        if(node.getAttributes().getNamedItem("sizeToWindow") != null){
        	if(node.getAttributes().getNamedItem("sizeToWindow").getNodeValue().equals("true"))
        		tb = new TabBrowser(true, tabLimit);
        	else
        		tb = new TabBrowser(false, tabLimit);
        }
        initWidget(tb);
        tb.setStyleName("ScreenTabBrowser");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTabBrowser(node, screen);
    }
}
