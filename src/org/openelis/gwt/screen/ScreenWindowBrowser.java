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

import org.openelis.gwt.widget.WindowBrowser;

/**
 * ScreenWindowBrowser wraps a WindowBrowser widget to be displayed 
 * on a Screen.
 * @author tschmidt
 *
 */
public class ScreenWindowBrowser extends ScreenWidget {
	/** 
	 * Default XML Tag used in XML Definition 
	 */
	public static String TAG_NAME = "winbrowser";
	/**
	 * Widget wrapped by this class
	 */
    private WindowBrowser browser;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenWindowBrowser() {
        
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * 
     * <br/><br/>
     * &lt;winbrowser key="string" winLimit="int" sizeToWindow="boolean"&gt;
     *  
     * @param node
     * @param screen
     */	
    public ScreenWindowBrowser(Node node, ScreenBase screen){
        super(node);
        int limit = 10;
        if(node.getAttributes().getNamedItem("winLimit") != null){
            limit = Integer.parseInt(node.getAttributes().getNamedItem("winLimit").getNodeValue());
        }
        if(node.getAttributes().getNamedItem("sizeToWindow") != null)
            browser = new WindowBrowser(true,limit);
        else
            browser = new WindowBrowser(false,limit);
        initWidget(browser);
        browser.setStyleName("ScreenWindowBrowser");
        setDefaults(node,screen);
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen){
        return new ScreenWindowBrowser(node,screen);
    }
    
    public void destroy() {
        browser = null;
        super.destroy();
    }
    

}
